package ps.crossworking.helper

import ps.crossworking.model.User
import ps.crossworking.repository.IAuthRepository
import javax.inject.Inject

interface IGetOwnUserUseCase {
    suspend operator fun invoke(): User
}

class GetOwnUserUseCase @Inject constructor(
    val repository: IAuthRepository
) : IGetOwnUserUseCase {

    override suspend fun invoke(): User {
        return repository.getOwnUser()
    }
}