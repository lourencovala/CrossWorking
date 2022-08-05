package ps.crossworking.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ps.crossworking.screen.user.authentication.signin.SignInScreen
import ps.crossworking.screen.user.authentication.signup.SignUpScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(startDestination = Screen.SignUp.route, route = AUTHENTICATION_ROUTE) {
        composable(Screen.SignUp.route) {
            SignUpScreen(
                goToComplementProfInfo = {
                    navController.navigate(Screen.UserSetup.route)
                },
                goToSignIn = {
                    navController.popBackStack()
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        composable(Screen.SignIn.route) {
            SignInScreen(
                gotToMainRoute = {
                    goToMainRoute(navController)
                },
                goToSignUp = {
                    navController.popBackStack()
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
    }
}
