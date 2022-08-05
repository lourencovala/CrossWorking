package ps.crossworking.screen.user

import ps.crossworking.model.User
import ps.crossworking.repository.IUserRepository
import javax.inject.Inject

interface IGetUserUseCase {
    suspend operator fun invoke(userId: String): User
}

class GetUserUseCase @Inject constructor(
    val repo: IUserRepository
) : IGetUserUseCase {

    override suspend fun invoke(userId: String): User {
        return repo.getUser(userId)
    }
}
