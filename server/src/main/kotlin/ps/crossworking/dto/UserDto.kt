package ps.crossworking.dto

import ps.crossworking.model.UserInput
import ps.crossworking.model.UserUpdate

/**
 * API representation for User input.
 */
data class UserApiInput(
    val userId: String,
    val email: String
) {
    fun toInternal() = UserInput(userId, email)
}

/**
 * API representation for list of Users.
 */
data class UserListApiOutput(
    val users: List<UserApiOutputShort>
)

/**
 * API representation for full User output.
 */
data class UserApiOutputFull(
    val userId: String,
    val name: String?,
    val email: String,
    val about: String?,
    val profileImage: String,
    val skills: List<SkillApiOutput>
)

/**
 * API representation for short User output.
 */
data class UserApiOutputShort(
    val userId: String,
    val name: String?,
    val email: String,
    val profileImage: String,
    val skills: List<SkillApiOutput>
)

/**
 * API representation for mini User output.
 */
data class UserApiOutputMini(
    val userId: String,
    val name: String?,
    val profileImage: String,
)

/**
 * API representation for User update.
 */
data class UserApiUpdate(
    val name: String?,
    val about: String?,
    val profileImage: String?
) {
    fun toInternal() = UserUpdate(name, about, profileImage)
}