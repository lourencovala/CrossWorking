package ps.crossworking.screen.idea.candidatures

import ps.crossworking.model.Candidate
import ps.crossworking.repository.ICandidatureRepository
import javax.inject.Inject

interface IGetIdeaCandidaturesUseCase {
    suspend operator fun invoke(ideaId: String, currentSize: Int): List<Candidate>
}

class GetIdeaCandidaturesUseCase @Inject constructor(
    private val candidatureRepository: ICandidatureRepository
) : IGetIdeaCandidaturesUseCase {

    private val pageSize = 10

    override suspend fun invoke(ideaId: String, currentSize: Int): List<Candidate> {
        if ((currentSize % pageSize) == 0) {
            return candidatureRepository.getIdeaCandidates(
                ideaId,
                currentSize / pageSize + 1,
                pageSize
            )
        }

        return emptyList()
    }
}
