package ps.crossworking.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ps.crossworking.R
import ps.crossworking.ui.composables.topBarTextStyle
import ps.crossworking.ui.theme.MyruadRegular
import ps.crossworking.ui.theme.topBar

private val stackStart = listOf(
    Screen.Home.route,
    Screen.CreateIdea.route,
    Screen.OwnProfile.route
)

private val titleIds = mapOf(
    Screen.CreateIdea.route to R.string.top_bar_create_idea,
    Screen.OwnProfile.route to R.string.top_bar_profile,
    Screen.IdeaDetails.route to R.string.top_bar_idea_details,
    Screen.IdeaCandidates.route to R.string.top_bar_idea_candidates,
    Screen.ViewIdeaSkills.route to R.string.top_bar_view_idea_skills,
    Screen.ManageIdeaSkills.route to R.string.top_bar_idea_skills,
    Screen.AddIdeaSkill.route to R.string.top_bar_add_skill,
    Screen.EditIdea.route to R.string.top_bar_edit_idea,
    Screen.EditProfile.route to R.string.top_bar_edit_profile,
    Screen.OtherProfile.route to R.string.top_bar_profile,
    Screen.UserIdeas.route to R.string.top_bar_user_ideas,
    Screen.ManageUserSkills.route to R.string.top_bar_user_skills,
    Screen.AddUserSkill.route to R.string.top_bar_add_skill,
    Screen.Results.route to R.string.top_bar_results
)

@Composable
fun TopBar(
    navController: NavHostController,
    topBarState: MutableState<Boolean>
) {
    if (!topBarState.value)
        return

    TopAppBar(
        title = {
            if (navController.currentBackStackEntry?.destination?.route == Screen.Home.route) {
                Image(
                    modifier = Modifier
                        .padding(
                            top = 12.dp,
                            bottom = 12.dp
                        )
                        .size(60.dp),
                    painter = painterResource(R.drawable.brand_logo),
                    contentDescription = "Brand Logo"
                )
            } else {
                val titleId = titleIds[navController.currentBackStackEntry?.destination?.route]
                if (titleId != null)
                    Text(
                        text = stringResource(id = titleId),
                        style = topBarTextStyle()
                    )
            }
        },
        navigationIcon = if (!stackStart.contains(navController.currentBackStackEntry?.destination?.route)) {
            {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Image(
                        painter = painterResource(R.drawable.back_icon),
                        contentDescription = "Brand Logo",
                        Modifier.padding(start = 12.dp)
                    )
                }
            }
        } else {
            null
        },
        backgroundColor = MaterialTheme.colors.topBar
    )
}
