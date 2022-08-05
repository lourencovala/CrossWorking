package ps.crossworking.screen.user.authentication.signin

import ps.crossworking.model.User
import ps.crossworking.repository.IAuthRepository
import javax.inject.Inject

interface ISignInUseCase {
    suspend operator fun invoke(email: String, password: String): User
}

class SignInUseCase @Inject constructor(
    val repository: IAuthRepository
) : ISignInUseCase {

    override suspend fun invoke(email: String, password: String): User {
        return repository.signIn(email, password)
    }
}
