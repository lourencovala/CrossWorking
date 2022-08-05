package ps.crossworking.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ps.crossworking.BuildConfig
import ps.crossworking.model.User
import ps.crossworking.navigation.Screen

object UserInstance {
    val user: MutableState<User?> = mutableStateOf(null)
    var token: String = ""
}

object Navigation {
    var prevClick: Screen = Screen.Home
}

object GoogleOptions {
    val Options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(BuildConfig.REQUEST_ID_GOOGLE)
        .requestEmail()
        .build()
}

