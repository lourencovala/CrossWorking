package ps.crossworking.model

import ps.crossworking.dto.SkillApiOutput
import java.util.*

/**
 * Internal representation for Skill input.
 */
data class SkillInput(
    val name: String,
    val about: String?,
    val categoryId: UUID
)

/**
 * Internal representation for Skill output.
 */
data class SkillOutput(
    val skillId: UUID,
    val name: String,
    val about: String?,
    val categoryName: String
) {
    fun toExternal() = SkillApiOutput(skillId, name, about, categoryName)
}
