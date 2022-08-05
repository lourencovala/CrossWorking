package ps.crossworking.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import ps.crossworking.screen.idea.candidatures.IdeaCandidatesScreen
import ps.crossworking.screen.idea.details.IdeaDetailsScreen
import ps.crossworking.screen.idea.feed.FeedScreen
import ps.crossworking.screen.idea.form.IdeaFormScreen
import ps.crossworking.screen.idea.userlist.UserIdeaListScreen
import ps.crossworking.screen.skill.addIdeaSkill.AddIdeaSkillScreen
import ps.crossworking.screen.skill.addUserSkill.AddUserSkillScreen
import ps.crossworking.screen.skill.manageIdea.ManageIdeaSkillScreen
import ps.crossworking.screen.skill.manageUser.ManageUserSkillsScreen
import ps.crossworking.screen.user.form.UserFormScreen
import ps.crossworking.screen.user.profile.other.OtherProfileScreen
import ps.crossworking.screen.user.profile.own.OwnProfileScreen
import ps.crossworking.screen.user.results.ResultsScreen

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Home.route,
        route = MAIN_ROUTE
    ) {
        composable(Screen.Home.route) {
            FeedScreen(
                onUserClick = { navController.navigate(Screen.OtherProfile.makeRoute(it)) },
                onIdeaClick = { navController.navigate(Screen.IdeaDetails.makeRoute(it)) }
            )
        }

        composable(Screen.CreateIdea.route) {
            IdeaFormScreen(
                ideaId = null,
                onComplete = { ideaId: String ->
                    navController.popBackStack()
                    navController.navigate(Screen.ManageIdeaSkills.makeRoute(ideaId))
                }
            )
        }

        composable(Screen.OwnProfile.route) {
            OwnProfileScreen(
                goToUserForm = {
                    navController.navigate(Screen.EditProfile.route)
                },
                goToIdeaList = { userId: String ->
                    navController.navigate(Screen.UserIdeas.makeRoute(userId))
                },
                goToSkillList = {
                    navController.navigate(Screen.ManageUserSkills.makeRoute(it))
                },
                goToResults = {
                    navController.navigate(Screen.Results.route)
                }
            ) { goToAuthRoute(navController) }
        }

        composable(
            Screen.IdeaDetails.route,
            arguments = listOf(navArgument("ideaId") { type = NavType.StringType })
        ) {
            IdeaDetailsScreen(
                ideaId = it.arguments?.getString("ideaId")!!,
                goBack = { navController.popBackStack() },
                onUserClick = { userId: String ->
                    navController.navigate(
                        Screen.OtherProfile.makeRoute(
                            userId
                        )
                    )
                },
                goToIdeaCandidates = { ideaId: String ->
                    navController.navigate(
                        Screen.IdeaCandidates.makeRoute(
                            ideaId
                        )
                    )
                },
                onEditClick = { ideaId: String ->
                    navController.navigate(
                        Screen.EditIdea.makeRoute(
                            ideaId
                        )
                    )
                },
                onViewSkillsClick = { ideaId: String, ownIdea: Boolean ->
                    if (ownIdea)
                        navController.navigate(Screen.ManageIdeaSkills.makeRoute(ideaId))
                    else
                        navController.navigate(Screen.ViewIdeaSkills.makeRoute(ideaId))
                }
            )
        }

        composable(
            Screen.IdeaCandidates.route,
            arguments = listOf(navArgument("ideaId") { type = NavType.StringType })
        ) {
            IdeaCandidatesScreen(
                ideaId = it.arguments?.getString("ideaId")!!
            ) { userId: String ->
                navController.navigate(
                    Screen.OtherProfile.makeRoute(
                        userId
                    )
                )
            }
        }

        composable(
            Screen.EditIdea.route,
            arguments = listOf(navArgument("ideaId") { type = NavType.StringType })
        ) {
            IdeaFormScreen(
                ideaId = it.arguments?.getString("ideaId"),
                onComplete = { ideaId: String ->
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate(Screen.IdeaDetails.makeRoute(ideaId))
                }
            )
        }

        composable(Screen.EditProfile.route) {
            UserFormScreen(
                onComplete = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.OtherProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            OtherProfileScreen(
                userId = it.arguments?.getString("userId")!!,
                goToSkillList = { userId: String ->
                    navController.navigate(Screen.ManageUserSkills.makeRoute(userId))
                },
                goToIdeaList = { userId: String ->
                    navController.navigate(Screen.UserIdeas.makeRoute(userId))
                }
            )
        }

        composable(
            Screen.UserIdeas.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) {
            UserIdeaListScreen(
                userId = it.arguments?.getString("userId")!!
            ) { ideaId: String ->
                navController.navigate(Screen.IdeaDetails.makeRoute(ideaId))
            }
        }

        composable(
            Screen.ManageUserSkills.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            ManageUserSkillsScreen(
                userId = it.arguments?.getString("userId")!!,
                onComplete = { navController.popBackStack() }
            ) {
                navController.navigate(Screen.AddUserSkill.route)
            }
        }

        composable(
            Screen.AddUserSkill.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            AddUserSkillScreen {
                navController.popBackStack()
            }
        }

        composable(
            Screen.ViewIdeaSkills.route,
            arguments = listOf(navArgument("ideaId") { type = NavType.StringType })
        ) {
            ManageIdeaSkillScreen(
                ideaId = it.arguments?.getString("ideaId")!!,
                isEditable = false,
                goToAddSkill = null,
                onComplete = null
            )
        }

        composable(
            Screen.ManageIdeaSkills.route,
            arguments = listOf(navArgument("ideaId") { type = NavType.StringType })
        ) {
            ManageIdeaSkillScreen(
                ideaId = it.arguments?.getString("ideaId")!!,
                isEditable = true,
                goToAddSkill = { ideaId ->
                    navController.navigate(Screen.AddIdeaSkill.makeRoute(ideaId))
                },
                onComplete = { ideaId ->
                    navController.popBackStack()
                    navController.navigate(Screen.IdeaDetails.makeRoute(ideaId))
                }
            )
        }

        composable(
            Screen.AddIdeaSkill.route,
            arguments = listOf(navArgument("ideaId") { type = NavType.StringType })
        ) {
            AddIdeaSkillScreen(ideaId = it.arguments?.getString("ideaId")!!) {
                navController.popBackStack()
            }
        }

        composable(Screen.Results.route) {
            ResultsScreen { ideaId: String ->
                navController.navigate(Screen.IdeaDetails.makeRoute(ideaId))
            }
        }
    }
}
