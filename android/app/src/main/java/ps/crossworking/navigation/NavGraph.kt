package ps.crossworking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = ROOT_ROUTE
    ) {
        homeNavGraph(navController = navController)
        authNavGraph(navController = navController)
        userSetupGraph(navController = navController)
    }
}

fun goToMainRoute(navController: NavHostController) {
    navController.popBackStack()
    navController.navigate(MAIN_ROUTE)
}

fun goToAuthRoute(navController: NavHostController) {
    navController.navigate(AUTHENTICATION_ROUTE) {
        popUpTo(Screen.Home.route) {
            inclusive = true
        }
    }
}

fun goToUserSetupRoute(navController: NavHostController) {
    navController.popBackStack()
    navController.navigate(USER_SETUP_ROUTE)
}
