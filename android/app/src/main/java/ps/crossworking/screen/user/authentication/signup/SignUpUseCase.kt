package ps.crossworking.screen.user.authentication.signup

import ps.crossworking.model.User
import ps.crossworking.repository.IAuthRepository
import javax.inject.Inject

interface ISignUpUseCase {
    suspend operator fun invoke(email: String, password: String): User
}

class SignUpUseCase @Inject constructor(
    val repository: IAuthRepository
) : ISignUpUseCase {

    override suspend operator fun invoke(email: String, password: String): User {
        return repository.signUp(email, password)
    }
}
