package ps.crossworking.screen.idea.details

import ps.crossworking.repository.IIdeaRepository
import javax.inject.Inject

interface IDeleteIdeaUseCase {
    suspend operator fun invoke(ideaId: String)
}

class DeleteIdeaUseCase @Inject constructor(
    val repository: IIdeaRepository
) : IDeleteIdeaUseCase {

    override suspend fun invoke(ideaId: String) {
        repository.deleteIdea(ideaId)
    }
}
