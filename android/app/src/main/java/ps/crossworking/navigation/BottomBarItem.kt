package ps.crossworking.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ps.crossworking.R
import ps.crossworking.common.UserInstance
import ps.crossworking.ui.composables.BorderedProfilePicture

sealed class BottomBarItem(val screen: Screen, val icon: @Composable () -> Unit) {

    object Home : BottomBarItem(Screen.Home, icon = {
        Image(
            painter = painterResource(R.drawable.home_icon),
            contentDescription = "home icon",
            modifier = Modifier.size(30.dp)
        )
    })

    object CreateIdea : BottomBarItem(Screen.CreateIdea, icon = {
        Image(
            painter = painterResource(R.drawable.add_icon),
            contentDescription = "create idea icon",
            modifier = Modifier.size(38.dp)
        )
    })

    object Profile : BottomBarItem(Screen.OwnProfile, icon = {
        BorderedProfilePicture(UserInstance.user.value?.profileImage ?: "", size = 34.dp)
    })
}
