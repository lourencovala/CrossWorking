package ps.crossworking.screen.idea.details

import ps.crossworking.repository.ICandidatureRepository
import javax.inject.Inject

interface IUndoApplyIdeaUseCase {
    suspend operator fun invoke(userId: String, ideaId: String)
}

class UndoApplyIdeaUseCase @Inject constructor(
    val repository: ICandidatureRepository
) : IUndoApplyIdeaUseCase {

    override suspend fun invoke(userId: String, ideaId: String) {
        return repository.undoApply(userId, ideaId)
    }
}
