package ps.crossworking.mockRepositories

import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.AuthCredential
import ps.crossworking.exceptions.UserNotFoundException
import ps.crossworking.model.User
import ps.crossworking.repository.IAuthRepository
import ps.crossworking.testUser
import ps.crossworking.testUserNoInfo

class AuthRepositoryMock: IAuthRepository {

    private var user: User? = null
    private var exception: Exception? = null
    private var isUnmapped = false
    private var isRestricted = false

    fun startWithUserComplete() {
        user = testUser
    }

    fun prepareException(e: Exception) {
        exception = e
    }

    fun restrictedToGetUser() {
        isRestricted = true
    }

    fun unmappedException(e: Exception) {
        exception = e
        isUnmapped = true
    }

    fun startWithUserIncomplete() {
        user = testUserNoInfo
    }

    override suspend fun signUp(email: String, password: String): User {
        if(exception != null)
            throw exception as Exception
        return testUserNoInfo
    }

    override suspend fun signUpGoogle(intent: Intent): User {
        if(exception != null)
            throw exception as Exception
        return testUserNoInfo
    }

    override suspend fun signIn(email: String, password: String): User {
        if(exception != null)
            throw exception as Exception
        return testUserNoInfo

    }

    override suspend fun updateOwnUser(name: String, about: String, pictureUri: Uri?): User {
        return User(
            userId = "",
            name = name,
            email = "",
            about = about,
            profileImage = pictureUri.toString(),
            skills = emptyList()
        )
    }

    override suspend fun getOwnUser(): User {
        if(exception != null)
                throw exception as Exception

        return user ?: throw UserNotFoundException()
    }

    override suspend fun signOut() {
        if(exception != null && !isRestricted)
            throw exception as Exception
    }

    override fun isLoggedIn(): Boolean {
        return user != null
    }
}