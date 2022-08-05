package ps.crossworking.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ps.crossworking.common.Navigation.prevClick
import ps.crossworking.ui.theme.appBar
import ps.crossworking.ui.theme.outline

@Composable
fun BottomBar(
    bottomBarState: MutableState<Boolean>,
    navController: NavHostController
) {
    if (!bottomBarState.value)
        return

    val screens = listOf(BottomBarItem.Home, BottomBarItem.CreateIdea, BottomBarItem.Profile)

    Column {
        Divider(color = colors.outline)
        BottomNavigation(
            backgroundColor = colors.appBar
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination


            screens.forEach { item ->
                BottomNavigationItem(
                    icon = item.icon,
                    selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                    onClick = {
                        if (item.screen == prevClick) {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id)

                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                                prevClick = item.screen
                            }
                        }

                    }
                )
            }
        }
    }
}


