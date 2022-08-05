package ps.crossworking.screen.user.authentication.signup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.gms.auth.api.signin.GoogleSignIn
import ps.crossworking.R
import ps.crossworking.common.GoogleOptions
import ps.crossworking.ui.composables.*
import ps.crossworking.ui.theme.LogoFont
import ps.crossworking.ui.theme.outline

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    goToComplementProfInfo: () -> Unit,
    goToSignIn: () -> Unit,
) {
    val state by remember { viewModel.state }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val localFocusManager = LocalFocusManager.current

    val googleSignInClient = GoogleSignIn.getClient(LocalContext.current, GoogleOptions.Options)

    val authResultListener =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.signInWithGoogle(it.data)
        }

    DisposableEffect(key1 = Unit) {
        onDispose {
            if (state is SignUpState.Completed)
                viewModel.restartInitialState()
        }
    }

    if (state is SignUpState.Success) {
        goToComplementProfInfo()
        viewModel.makeCompleted()
        return
    }

    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppLogo()
            Text(
                text = stringResource(id = R.string.signup_text),
                style = TextStyle(
                    fontFamily = LogoFont,
                    color = MaterialTheme.colors.primary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 10.dp, top = 16.dp)
            )

            when (val value: SignUpState = state) {
                is SignUpState.Error -> {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(value.errorMessageId),
                        style = bigBoldMainColorTextStyle(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                is SignUpState.WaitingForSignUp -> {
                    Spacer(modifier = Modifier.height(20.dp))
                    LoadingAnimation()
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            MainTextField(
                labelText = stringResource(id = R.string.auth_email_prompt),
                state = email,
                focusManager = localFocusManager,
                keyboardType = KeyboardType.Email
            )
            MainTextField(
                labelText = stringResource(id = R.string.auth_password_prompt),
                toHide = true,
                state = password,
                focusManager = localFocusManager,
                keyboardType = KeyboardType.Password,
                isLastAction = true
            ) {
                viewModel.createAccount(email.value, password.value)
            }
            Spacer(modifier = Modifier.height(24.dp))
            BigRedButton(
                text = stringResource(id = R.string.signup_button),
                isEnable = viewModel.state.value !is SignUpState.WaitingForSignUp
            ) {
                localFocusManager.clearFocus()
                viewModel.createAccount(email.value, password.value)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Divider(
                    color = MaterialTheme.colors.outline,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 58.dp, end = 58.dp)
                )
                Text(
                    text = stringResource(id = R.string.or_text),
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .padding(start = 6.dp, end = 6.dp),
                    style = matchingOutlineTextStyle()
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .clickable {
                        authResultListener.launch(googleSignInClient.signInIntent)
                    },
                painter = painterResource(R.drawable.image),
                contentDescription = "Google logo"
            )

            Spacer(modifier = Modifier.height(44.dp))
            TextButton(
                onClick = { goToSignIn() }
            ) {
                Text(
                    text = stringResource(id = R.string.signup_has_account_text),
                    style = normalTextStyle()
                )
            }
        }
    }
}
