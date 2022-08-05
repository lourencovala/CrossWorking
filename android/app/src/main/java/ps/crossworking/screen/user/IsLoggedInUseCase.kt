package ps.crossworking.screen.user

import ps.crossworking.repository.IAuthRepository
import javax.inject.Inject

interface IIsLoggedInUseCase {
    suspend operator fun invoke(): Boolean
}

class IsLoggedInUseCase @Inject constructor(
    val repo: IAuthRepository
) : IIsLoggedInUseCase {

    override suspend fun invoke(): Boolean {
        return repo.isLoggedIn()
    }
}
