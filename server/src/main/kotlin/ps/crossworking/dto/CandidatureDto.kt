package ps.crossworking.dto

import ps.crossworking.model.CandidatureInput
import ps.crossworking.model.CandidatureUpdate
import java.util.*

/**
 * API representation for Candidature input.
 */
data class CandidatureApiInput(val ideaId: String) {
    fun toInternal() = CandidatureInput(UUID.fromString(ideaId))
}

/**
 * API representation for Candidature output.
 */
data class CandidatureApiOutput(val status: String, val daysSinceCreatedDate: Int, val daysSinceLastUpdate: Int)

/**
 * API representation for Candidature update.
 */
data class CandidatureApiUpdate(val status: String) {
    fun toInternal() = CandidatureUpdate(status)
}

/**
 * API representation for list of Candidature Results.
 */
data class CandidatureResultListApiOutput(
    val results: List<CandidatureResultApiOutput>
)

/**
 * API representation for Candidature Result output.
 */
data class CandidatureResultApiOutput(
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int,
    val idea: IdeaApiOutputMini
)

/**
 * API representation for list of Candidates.
 */
data class CandidateListApiOutput(
    val candidates: List<CandidateApiOutput>
)

/**
 * API representation for Candidate output.
 */
data class CandidateApiOutput(
    val user: UserApiOutputShort,
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int
)
