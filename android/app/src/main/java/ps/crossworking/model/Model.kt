package ps.crossworking.model

class User(
    val userId: String,
    val name: String?,
    val email: String,
    val about: String?,
    val profileImage: String?,
    val skills: List<Skill>
)

data class Idea(
    val ideaId: String,
    val title: String,
    val smallDescription: String,
    val description: String,
    val user: User,
    val days: Int,
    val skills: List<Skill>
)

data class ShortIdea(
    val ideaId: String,
    val title: String,
    val smallDescription: String,
    val days: Int,
    val user: User,
    val skills: List<Skill>
)

data class MiniIdea(
    val ideaId: String,
    val title: String
)

data class Skill(
    val skillId: String,
    val name: String,
    val about: String?,
    val categoryName: String
)

data class Category(
    val categoryId: String,
    val name: String
)

data class Candidate(
    val user: User,
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int
)

data class Candidature(
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int
)

data class CandidatureResult(
    val status: String,
    val daysSinceCreatedDate: Int,
    val daysSinceLastUpdate: Int,
    val idea: MiniIdea
)
