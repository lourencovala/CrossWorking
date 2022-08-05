package ps.crossworking.screen.idea

import ps.crossworking.model.Idea
import ps.crossworking.repository.IIdeaRepository
import javax.inject.Inject

interface IGetIdeaUseCase {
    suspend operator fun invoke(ideaId: String): Idea
}

class GetIdeaUseCase @Inject constructor(
    val repository: IIdeaRepository
) : IGetIdeaUseCase {

    override suspend fun invoke(ideaId: String): Idea {
        return repository.getIdea(ideaId)
    }
}
