package ps.crossworking.dto

import ps.crossworking.model.SkillInput
import java.util.*


/**
 * API representation for Skill input.
 */
data class SkillApiInput(
    val name: String,
    val about: String?,
    val categoryId: UUID
) {
    fun toInternal() = SkillInput(name, about, categoryId)
}

/**
 * API representation for list of Skills.
 */
data class SkillListApiOutput(
    val skills: List<SkillApiOutput>
)

/**
 * API representation for Skill output.
 */
data class SkillApiOutput(
    val skillId: UUID,
    val name: String,
    val about: String?,
    val categoryName: String
)
