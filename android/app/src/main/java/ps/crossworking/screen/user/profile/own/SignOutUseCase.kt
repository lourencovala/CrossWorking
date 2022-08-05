package ps.crossworking.screen.user.profile.own

import ps.crossworking.repository.IAuthRepository
import javax.inject.Inject

interface ISignOutUseCase {
    suspend operator fun invoke()
}

class SignOutUseCase @Inject constructor(
    val repository: IAuthRepository
) : ISignOutUseCase {

    override suspend fun invoke() {
        repository.signOut()
    }
}
