package ps.crossworking.dto

import ps.crossworking.model.*

data class UserCreateDto(val userId: String, val email: String)
data class UserUpdateDto(val name: String, val about: String, val profileImage: String?)

data class ShortIdeaListDto(val ideas: List<ShortIdea>)

data class IdeaCreateDto(val title: String, val smallDescription: String, val description: String)
data class IdeaUpdateDto(val title: String, val smallDescription: String, val description: String)

data class SkillListDto(val skills: List<Skill>)

data class SkillCreateDto(val name: String, val about: String, val categoryId: String)

data class CategoryListDto(val categories: List<Category>)

data class CandidateListDto(val candidates: List<Candidate>)
data class CandidatureResultListDto(val results: List<CandidatureResult>)

data class CandidatureCreateDto(val ideaId: String)
data class CandidatureUpdateDto(val status: String)
