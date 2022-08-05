package ps.crossworking.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ps.crossworking.common.UserInstance
import ps.crossworking.screen.skill.addUserSkill.AddUserSkillScreen
import ps.crossworking.screen.skill.manageUser.ManageUserSkillsScreen
import ps.crossworking.screen.user.form.UserFormScreen

fun NavGraphBuilder.userSetupGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.UserSetup.route,
        route = USER_SETUP_ROUTE
    ) {
        composable(Screen.UserSetup.route) {
            UserFormScreen(
                onComplete = {
                    navController.navigate(Screen.UserSkillsSetup.route)
                }
            )
        }

        composable(Screen.UserSkillsSetup.route) {
            ManageUserSkillsScreen(
                userId = UserInstance.user.value?.userId!!,
                onComplete = { goToMainRoute(navController) }
            ) {
                navController.navigate(Screen.UserSetupAddSkill.route)
            }
        }

        composable(
            Screen.UserSetupAddSkill.route,
        ) {
            AddUserSkillScreen {
                navController.popBackStack()
            }
        }
    }
}
