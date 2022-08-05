package ps.crossworking.model

import ps.crossworking.dto.UserApiOutputFull
import ps.crossworking.dto.UserApiOutputShort

/**
 * Internal representation for User input.
 */
data class UserInput(
    val userId: String,
    val email: String
)

/**
 * Internal representation for full User output.
 */
data class UserOutputFull(
    val userId: String,
    val name: String?,
    val email: String,
    val about: String?,
    val profileImage: String,
    var skills: List<SkillOutput>?
) {
    fun toExternal() =
        UserApiOutputFull(
            userId,
            name,
            email,
            about,
            profileImage,
            skills?.map { it.toExternal() } ?: emptyList()
        )
}

/**
 * Internal representation for short User output.
 */
data class UserOutputShort(
    val userId: String,
    val name: String?,
    val email: String,
    val profileImage: String,
    var skills: List<SkillOutput>?
) {
    fun toExternal() = UserApiOutputShort(
        userId,
        name,
        email,
        profileImage,
        skills?.map { it.toExternal() } ?: emptyList()
    )
}

/**
 * Internal representation for User update.
 */
data class UserUpdate(
    val name: String?,
    val about: String?,
    val profileImage: String?
)
