package ps.crossworking.dto

import ps.crossworking.model.IdeaInput
import ps.crossworking.model.IdeaUpdate
import java.util.*

/**
 * API representation for list of Ideas.
 */
data class IdeaListApiOutput(
    val ideas: List<IdeaApiOutputShort>
)

/**
 * API representation for full Idea output.
 */
data class IdeaApiOutputFull(
    val ideaId: UUID,
    val title: String,
    val smallDescription: String,
    val description: String,
    val user: UserApiOutputMini,
    val days: Int,
    val skills: List<SkillApiOutput>
)

/**
 * API representation for short Idea output.
 */
data class IdeaApiOutputShort(
    val ideaId: UUID,
    val title: String,
    val smallDescription: String,
    val user: UserApiOutputMini,
    val days: Int,
    val skills: List<SkillApiOutput>
)

/**
 * API representation for mini Idea output.
 */
data class IdeaApiOutputMini(val ideaId: UUID, val title: String)

/**
 * API representation for Idea input.
 */
data class IdeaApiInput(
    val title: String,
    val smallDescription: String,
    val description: String
) {
    fun toInternal() = IdeaInput(
        this.title,
        this.smallDescription,
        this.description
    )
}

/**
 * API representation for Idea update.
 */
data class IdeaApiUpdate(
    val title: String?,
    val smallDescription: String?,
    val description: String?
) {
    fun toInternal() = IdeaUpdate(this.title, this.smallDescription, this.description)
}
