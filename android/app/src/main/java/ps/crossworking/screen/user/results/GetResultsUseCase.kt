package ps.crossworking.screen.user.results

import ps.crossworking.model.CandidatureResult
import ps.crossworking.repository.ICandidatureRepository
import javax.inject.Inject

interface IGetResultsUseCase {
    suspend operator fun invoke(userId: String, currentSize: Int): List<CandidatureResult>
}

class GetResultsUseCase @Inject constructor(
    private val repository: ICandidatureRepository
) : IGetResultsUseCase {

    private val pageSize = 10

    override suspend fun invoke(userId: String, currentSize: Int): List<CandidatureResult> {
        if ((currentSize % pageSize) == 0) {
            return repository.getResults(userId, currentSize / pageSize + 1, pageSize)
        }

        return emptyList()
    }
}
