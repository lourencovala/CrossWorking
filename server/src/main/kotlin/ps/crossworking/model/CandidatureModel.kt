package ps.crossworking.model

import ps.crossworking.dto.*
import java.util.*

/**
 * Internal representation for Candidature input.
 */
data class CandidatureInput(val ideaId: UUID)

/**
 * Internal representation for Candidature output.
 */
data class CandidatureOutput(
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int
) {
    fun toExternal() = CandidatureApiOutput(status, daysSinceCreatedDate, daysSinceLastUpdate)
}

/**
 * Internal representation for Candidature update.
 */
data class CandidatureUpdate(val status: String)

/**
 * Internal representation for Candidature Result output.
 */
data class CandidatureResultOutput(
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int,
    val ideaId: UUID,
    val title: String
) {
    fun toExternal() = CandidatureResultApiOutput(
        status,
        daysSinceCreatedDate,
        daysSinceLastUpdate,
        IdeaApiOutputMini(ideaId, title)
    )
}

data class CandidateOutput(
    val userId: String,
    val name: String?,
    val email: String,
    val profileImage: String,
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int
) {
    fun toExternal() = CandidateApiOutput(
        UserApiOutputShort(userId, name, email, profileImage, emptyList()),
        status,
        daysSinceCreatedDate,
        daysSinceLastUpdate
    )
}
