package ps.crossworking.screen.idea.feed

import ps.crossworking.model.ShortIdea
import ps.crossworking.repository.IIdeaRepository
import javax.inject.Inject

interface IGetFeedUseCase {
    suspend operator fun invoke(currentSize: Int): List<ShortIdea>
}

class GetFeedUseCase @Inject constructor(
    val repo: IIdeaRepository
) : IGetFeedUseCase {

    private val pageSize = 10

    override suspend fun invoke(currentSize: Int): List<ShortIdea> {
        if ((currentSize % pageSize) == 0) {
            return repo.getFeed(currentSize / pageSize + 1, pageSize)
        }

        return emptyList()
    }
}
