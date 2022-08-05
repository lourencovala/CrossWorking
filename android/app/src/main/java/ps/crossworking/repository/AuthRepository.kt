package ps.crossworking.repository

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import ps.crossworking.common.UserInstance
import ps.crossworking.dto.UserCreateDto
import ps.crossworking.dto.UserUpdateDto
import ps.crossworking.exceptions.*
import ps.crossworking.model.User
import ps.crossworking.service.IAuthService
import ps.crossworking.service.UserService
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

interface IAuthRepository {
    suspend fun signUp(email: String, password: String): User
    suspend fun signUpGoogle(intent: Intent): User
    suspend fun signIn(email: String, password: String): User
    suspend fun updateOwnUser(name: String, about: String, pictureUri: Uri?): User
    suspend fun getOwnUser(): User
    suspend fun signOut()
    fun isLoggedIn(): Boolean
}

class AuthRepository @Inject constructor(
    private val authService: IAuthService,
    private val userService: UserService
) : IAuthRepository {

    override suspend fun signUp(
        email: String,
        password: String
    ): User {
        try {
            if (isLoggedIn())
                return getOwnUser()
            authService.createUserWithEmailAndPassword(email, password)
            UserInstance.token = authService.getUserToken()
            return userService.createUser(UserCreateDto(authService.getUserUid(), email))
        } catch (e: Exception) {
            deleteIfSignIn()

            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        409 -> throw InconsistentAuthState()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw e
                    }
                }

                is UnknownHostException -> throw NetworkException()

                is FirebaseAuthException -> {
                    throw firebaseErrorMapping[e.errorCode] ?: UnknownException()
                }

                is IllegalArgumentException -> {
                    throw MissingFieldException()
                }

                is CrossWorkingException -> {
                    throw e
                }

                else -> {
                    throw UnknownException()
                }
            }
        }
    }

    override suspend fun signUpGoogle(intent: Intent): User {

        try {
            authService.enterWithGoogle(intent)
            UserInstance.token = authService.getUserToken()
            return userService.createUser(
                UserCreateDto(
                    authService.getUserUid(),
                    authService.getEmailFirebase()
                )
            )
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        409 -> return getOwnUser()
                        404 -> {
                            deleteIfSignIn()
                            throw RequestApiException()
                        }
                        in 400..499 -> {
                            throw RequestApiException()
                        }
                        in 500..599 -> {
                            throw ApiInternalException()
                        }
                        else -> {
                            deleteIfSignIn()
                            throw e
                        }
                    }
                }
                is UnknownHostException -> {
                    throw NetworkException()
                }

                is FirebaseAuthException -> {
                    throw firebaseErrorMapping[e.errorCode] ?: UnknownException()
                }

                is CrossWorkingException -> {
                    throw e
                }

                else -> {
                    throw UnknownException()
                }
            }
        }
    }

    override suspend fun updateOwnUser(name: String, about: String, pictureUri: Uri?): User {
        try {
            return userService.addNameAndPict(
                authService.getUserUid(),
                UserUpdateDto(name, about, pictureUri?.toString())
            )
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw e
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun getOwnUser(): User {
        try {
            UserInstance.token = authService.getUserToken()
            return userService.getUser(authService.getUserUid())
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw e
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun signIn(email: String, password: String): User {
        try {
            authService.logInWithEmailAndPassword(email, password)
            UserInstance.token = authService.getUserToken()
            return userService.getUser(authService.getUserUid())
        } catch (e: Exception) {

            if (authService.isLoggedIn())
                signOut()

            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw e
                    }
                }

                is FirebaseAuthException -> throw firebaseErrorMapping[e.errorCode]
                    ?: UnknownException()

                is AuthServiceInternalException -> throw e

                is IllegalArgumentException -> throw MissingFieldException()

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun signOut() {
        authService.signOut()
        UserInstance.user.value = null
    }

    override fun isLoggedIn(): Boolean {
        return authService.isLoggedIn()
    }

    private suspend fun deleteIfSignIn() {
        try {
            if (authService.isLoggedIn())
                authService.deleteUser()
        } catch (e: Exception) {
            throw AuthServiceInternalException()
        }
    }
}
