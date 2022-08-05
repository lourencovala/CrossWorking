package ps.crossworking.service

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import ps.crossworking.exceptions.AuthServiceInternalException
import ps.crossworking.exceptions.AuthServiceInvalidUserException
import javax.inject.Inject

interface IAuthService {
    suspend fun createUserWithEmailAndPassword(email: String, password: String)
    suspend fun logInWithEmailAndPassword(email: String, password: String)
    suspend fun enterWithGoogle(intent: Intent)
    suspend fun signOut()
    suspend fun deleteUser()
    fun isLoggedIn(): Boolean
    fun getUserUid(): String
    fun getEmailFirebase(): String
    suspend fun getUserToken(): String
}

class AuthService @Inject constructor(
    private val auth: FirebaseAuth
) : IAuthService {
    override suspend fun createUserWithEmailAndPassword(email: String, password: String) {
        val response = auth.createUserWithEmailAndPassword(email, password).await()

        if (response.user == null || response.user?.uid == null)
            throw AuthServiceInternalException()
    }

    override suspend fun logInWithEmailAndPassword(email: String, password: String) {
        val response = auth.signInWithEmailAndPassword(email, password).await()

        if (response.user == null || response.user?.uid == null)
            throw AuthServiceInvalidUserException()
    }

    override suspend fun enterWithGoogle(intent: Intent) {

        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        val account = task.getResult(AuthServiceInvalidUserException::class.java)!!

        val credentials = GoogleAuthProvider.getCredential(account.idToken!!, null)

        val response = auth.signInWithCredential(credentials).await()

        if (response.user == null || response.user?.uid == null)
            throw AuthServiceInvalidUserException()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getUserUid(): String {
        return auth.currentUser?.uid ?: throw AuthServiceInvalidUserException()
    }

    override suspend fun getUserToken(): String {
        return auth.currentUser?.getIdToken(true)?.await()?.token
            ?: throw AuthServiceInvalidUserException()
    }

    override fun getEmailFirebase(): String {
        return auth.currentUser?.email ?: throw AuthServiceInvalidUserException()
    }

    override suspend fun deleteUser() {
        auth.currentUser?.delete()?.await() ?: throw AuthServiceInvalidUserException()
    }
}
