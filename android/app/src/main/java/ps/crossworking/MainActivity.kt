package ps.crossworking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ps.crossworking.helper.AccountState
import ps.crossworking.helper.HelperViewModel
import ps.crossworking.navigation.*
import ps.crossworking.screen.user.form.UserFormState
import ps.crossworking.ui.composables.ErrorScreen
import ps.crossworking.ui.composables.LoadingAnimation
import ps.crossworking.ui.theme.CrossWorkingTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: HelperViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getUserIfExist()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.value is AccountState.Loading
            }
        }

        setContent {
            CrossWorkingTheme {
                // Set Right Color on status bar
                val systemUiController = rememberSystemUiController()
                val materialColors = colors

                val isRefreshing by remember {
                    viewModel.isRefreshing
                }
                var startDestination: String? by remember {
                    mutableStateOf(null)
                }

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = materialColors.background,
                        darkIcons = materialColors.isLight
                    )
                    systemUiController.setNavigationBarColor(
                        color = materialColors.background,
                        darkIcons = materialColors.isLight
                    )
                }

                LaunchedEffect(viewModel.state.value) {

                    startDestination = when (viewModel.state.value) {
                        is AccountState.LoggedIn -> MAIN_ROUTE
                        is AccountState.NeedsInfo -> USER_SETUP_ROUTE
                        is AccountState.NotLoggedIn -> AUTHENTICATION_ROUTE
                        else -> null
                    }

                }

                val navController = rememberNavController()

                val bottomAndTopBarState = rememberSaveable { (mutableStateOf(false)) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                when (navBackStackEntry?.destination?.route) {
                    Screen.SignUp.route -> {
                        bottomAndTopBarState.value = false
                    }

                    Screen.UserSetup.route -> {
                        bottomAndTopBarState.value = false
                    }

                    Screen.SignIn.route -> {
                        bottomAndTopBarState.value = false
                    }

                    Screen.UserSkillsSetup.route -> {
                        bottomAndTopBarState.value = false
                    }

                    Screen.UserSetupAddSkill.route -> {
                        bottomAndTopBarState.value = false
                    }

                    null -> {
                        bottomAndTopBarState.value = false
                    }
                    else -> {
                        bottomAndTopBarState.value = true
                    }
                }

                ProvideWindowInsets {
                    Scaffold(
                        topBar = {
                            TopBar(
                                navController = navController,
                                topBarState = bottomAndTopBarState
                            )
                        },
                        bottomBar = {
                            BottomBar(
                                bottomBarState = bottomAndTopBarState,
                                navController = navController
                            )
                        }
                    ) {
                        Box(
                            modifier = Modifier.padding(it),
                        ) {
                            if (viewModel.state.value is AccountState.Loading ||
                                (startDestination == null &&
                                        viewModel.state.value !is AccountState.NetworkError)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LoadingAnimation()
                                }
                            } else {
                                if (viewModel.state.value !is AccountState.NetworkError)
                                    SetupNavGraph(
                                        navController = navController,
                                        startDestination = startDestination!!
                                    )
                                else
                                    InitialErrorScreen(
                                        isRefresh = isRefreshing,
                                        R.string.network_error
                                    ) {
                                        viewModel.refresh()
                                    }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun InitialErrorScreen(isRefresh: Boolean, errorMessageId: Int, refresh: () -> (Unit)) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefresh),
        onRefresh = { refresh() },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                // Pass the SwipeRefreshState + trigger through
                state = state,
                refreshTriggerDistance = trigger,
                // Enable the scale animation
                scale = true,
                // Change the color and shape
                contentColor = colors.primary,
                backgroundColor = colors.background,
                shape = RoundedCornerShape(33.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .verticalScroll(
                    rememberScrollState()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ErrorScreen(errorMessage = stringResource(id = errorMessageId))
        }
    }
}
