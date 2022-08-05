package ps.crossworking.model

import ps.crossworking.dto.IdeaApiOutputFull
import ps.crossworking.dto.IdeaApiOutputShort
import ps.crossworking.dto.UserApiOutputMini
import java.util.*

/**
 * Internal representation for Idea output.
 */
data class IdeaOutput(
    val ideaId: UUID,
    val title: String,
    val smallDescription: String,
    val description: String,
    val days: Int,
    val userId: String,
    var name: String?,
    var profileImage: String?,
    var skills: List<SkillOutput>?
) {
    fun toExternalShort() = IdeaApiOutputShort(
        ideaId,
        title,
        smallDescription,
        UserApiOutputMini(userId, name, profileImage!!),
        days,
        skills?.map { it.toExternal() } ?: emptyList()
    )

    fun toExternalFull() = IdeaApiOutputFull(
        ideaId,
        title,
        smallDescription,
        description,
        UserApiOutputMini(userId, name, profileImage!!),
        days,
        skills?.map { it.toExternal() } ?: emptyList()
    )
}

/**
 * Internal representation for Idea input.
 */
data class IdeaInput(
    val title: String,
    val smallDescription: String,
    val description: String
)

/**
 * Internal representation for Idea update.
 */
data class IdeaUpdate(
    val title: String?,
    val smallDescription: String?,
    val description: String?
)
