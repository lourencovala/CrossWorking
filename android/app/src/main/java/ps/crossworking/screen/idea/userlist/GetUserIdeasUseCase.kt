package ps.crossworking.screen.idea.userlist

import ps.crossworking.model.ShortIdea
import ps.crossworking.repository.IIdeaRepository
import javax.inject.Inject

interface IGetUserIdeasUseCase {
    suspend operator fun invoke(userId: String, currentSize: Int): List<ShortIdea>
}

class GetUserIdeasUseCase @Inject constructor(
    val repository: IIdeaRepository
) : IGetUserIdeasUseCase {

    private val pageSize = 10

    override suspend fun invoke(userId: String, currentSize: Int): List<ShortIdea> {
        if ((currentSize % pageSize) == 0) {
            return repository.getUserIdeas(userId, currentSize / pageSize + 1, pageSize)
        }
        return emptyList()
    }
}
