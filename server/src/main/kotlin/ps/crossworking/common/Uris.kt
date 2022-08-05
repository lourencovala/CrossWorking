package ps.crossworking.common

import org.springframework.web.util.UriTemplate
import java.net.URI

const val HOME_PATH = "/api"
const val USER_PATH = "/api/users"
const val IDEAS_PATH = "/api/ideas"
const val CATEGORIES_PATH = "/api/categories"

interface UriHolder {
    fun makeSubPath(vararg values: String): URI
}

object Uris {

    object User {

        object SingleUser : UriHolder {
            const val PATH = "/{userId}"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("userId" to values[0]))
        }
    }

    object Idea {

        object UserIdeas : UriHolder {
            const val PATH = "/{userId}/ideas"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("userId" to values[0]))
        }

        object SingleIdea : UriHolder {
            const val PATH = "/{ideaId}"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("ideaId" to values[0]))
        }
    }

    object Candidate {

        object UserResults : UriHolder {
            const val PATH = "/{userId}/results"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("userId" to values[0]))
        }

        object UserCandidatures : UriHolder {
            const val PATH = "/{userId}/candidatures"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("userId" to values[0]))
        }

        object SingleUserCandidature : UriHolder {
            const val PATH = "/{userId}/candidatures/{ideaId}"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) =
                TEMPLATE.expand(mapOf("userId" to values[0], "ideaId" to values[1]))
        }

        object IdeaCandidates : UriHolder {
            const val PATH = "/{ideaId}/candidates"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("ideaId" to values[0]))
        }

        object SingleCandidate : UriHolder {
            const val PATH = "/{ideaId}/candidates/{candidateId}"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) =
                TEMPLATE.expand(mapOf("ideaId" to values[0], "candidateId" to values[1]))
        }
    }

    object Skills {

        object UserSkills : UriHolder {
            const val PATH = "/{userId}/skills"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("userId" to values[0]))
        }

        object SingleUserSkill : UriHolder {
            const val PATH = "/{userId}/skills/{skillId}"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) =
                TEMPLATE.expand(mapOf("userId" to values[0], "skillId" to values[1]))
        }

        object IdeaSkills : UriHolder {
            const val PATH = "/{ideaId}/skills"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) = TEMPLATE.expand(mapOf("ideaId" to values[0]))
        }

        object SingleIdeaSkill : UriHolder {
            const val PATH = "/{ideaId}/skills/{skillId}"
            private val TEMPLATE = UriTemplate(PATH)
            override fun makeSubPath(vararg values: String) =
                TEMPLATE.expand(mapOf("ideaId" to values[0], "skillId" to values[1]))
        }
    }
}