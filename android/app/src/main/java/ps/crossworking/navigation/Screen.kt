package ps.crossworking.navigation

const val ROOT_ROUTE = "root"
const val MAIN_ROUTE = "main"
const val USER_SETUP_ROUTE = "setup"
const val AUTHENTICATION_ROUTE = "authentication"

sealed class Screen(val route: String) {

    /**
     * Authentication route.
     * Sign up and sign in screens.
     */
    object SignUp : Screen("signup")
    object SignIn : Screen("signin")

    /**
     * User setup route
     * User form, skill list and add skill screens.
     */
    object UserSetup : Screen("setup/user")
    object UserSkillsSetup : Screen("setup/skills")
    object UserSetupAddSkill : Screen("setup/addSkill")

    /**
     * Bottom bar screens.
     */
    object Home : Screen("home") // aka Feed
    object CreateIdea : Screen("form/ideas")
    object OwnProfile : Screen("profile")

    /**
     * Other screens.
     */
    object IdeaDetails : Screen("ideas/{ideaId}") {
        fun makeRoute(ideaId: String) = "ideas/${ideaId}"
    }

    object IdeaCandidates : Screen("ideas/{ideaId}/candidatures") {
        fun makeRoute(ideaId: String) = "ideas/${ideaId}/candidatures"
    }

    object ViewIdeaSkills : Screen("ideas/{ideaId}/skills") {
        fun makeRoute(ideaId: String) = "ideas/${ideaId}/skills"
    }

    object ManageIdeaSkills : Screen("ideas/{ideaId}/skills/self") {
        fun makeRoute(ideaId: String) = "ideas/${ideaId}/skills/self"
    }

    object AddIdeaSkill : Screen("ideas/{ideaId}/addSkills") {
        fun makeRoute(ideaId: String) = "ideas/${ideaId}/addSkills"
    }

    object EditIdea : Screen("form/ideas/{ideaId}") {
        fun makeRoute(ideaId: String) = "form/ideas/${ideaId}"
    }

    object EditProfile : Screen("form/users")

    object OtherProfile : Screen("users/{userId}") {
        fun makeRoute(userId: String) = "users/${userId}"
    }

    object UserIdeas : Screen("users/{userId}/ideas") {
        fun makeRoute(userId: String) = "users/${userId}/ideas"
    }

    object ManageUserSkills : Screen("users/{userId}/skills") {
        fun makeRoute(userId: String) = "users/${userId}/skills"
    }

    object AddUserSkill : Screen("users/{userId}/addSkill") {
        fun makeRoute(userId: String) = "users/${userId}/addSkill"
    }

    object Results : Screen("results")
}
