package ps.crossworking.screen.user.authentication

import android.content.Intent
import com.google.firebase.auth.AuthCredential
import ps.crossworking.exceptions.AuthServiceInternalException
import ps.crossworking.model.User
import ps.crossworking.repository.IAuthRepository
import javax.inject.Inject

interface ISignUpGoogleUseCase {
    suspend operator fun invoke(intent: Intent?): User
}

class SignUpGoogleUseCase @Inject constructor(
    val repository: IAuthRepository
) : ISignUpGoogleUseCase {

    override suspend operator fun invoke(intent: Intent?): User {
        if(intent == null)
            throw AuthServiceInternalException()
        return repository.signUpGoogle(intent)
    }
}
