package ps.crossworking.screen.user.form

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.skydoves.landscapist.coil.CoilImage
import ps.crossworking.R
import ps.crossworking.common.UserInstance
import ps.crossworking.ui.composables.BigRedButton
import ps.crossworking.ui.composables.LoadingAnimation
import ps.crossworking.ui.composables.MainTextField
import ps.crossworking.ui.composables.errorTextStyle

@Composable
fun UserFormScreen(
    viewModel: UserFormViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val state by remember { viewModel.userFormState }
    val errorMessageId by remember { viewModel.errorMessageId }
    val resultImagePath = remember { mutableStateOf<Uri?>(null) }
    val name = rememberSaveable { mutableStateOf("") }
    val about = rememberSaveable { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        resultImagePath.value = it
    }

    DisposableEffect(key1 = Unit) {
        if (UserInstance.user.value != null && UserInstance.user.value!!.name != null) {
            val user = UserInstance.user.value!!
            if (user.name != null)
                name.value = user.name
            if (user.about != null)
                about.value = user.about
            if (user.profileImage != null)
                resultImagePath.value = Uri.parse(user.profileImage)
        }
        onDispose {
            if (state is UserFormState.Completed) {
                viewModel.restoreInitialState()
            }
        }
    }

    if (state is UserFormState.Added) {
        onComplete()
        viewModel.makeComplete()
        return
    }

    UserForm(state, launcher, resultImagePath, name, about, errorMessageId) { v1, v2, v3 ->
        viewModel.editUser(v1, v2, v3)
    }
}

@Composable
fun UserForm(
    userFormState: UserFormState,
    launcher: ManagedActivityResultLauncher<String, Uri?>,
    resultImagePath: MutableState<Uri?>,
    name: MutableState<String>,
    about: MutableState<String>,
    errorMessageId: Int?,
    editUser: (String, String, Uri?) -> Unit
) {
    val focusManager = LocalFocusManager.current
    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                CoilImage(
                    imageModel = (
                            if (resultImagePath.value == null)
                                Image(
                                    painter = painterResource(id = R.drawable.profile_default_picture),
                                    contentDescription = "default"
                                )
                            else
                                resultImagePath.value
                            ),
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            MainTextField(
                labelText = stringResource(id = R.string.userinfo_name_prompt),
                state = name,
                focusManager = focusManager,
                toCapitalize = true
            )
            Spacer(modifier = Modifier.height(30.dp))
            MainTextField(
                labelText = stringResource(id = R.string.userinfo_about_prompt),
                state = about,
                long = true,
                focusManager = focusManager,
                toCapitalize = true,
                isLastAction = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (userFormState == UserFormState.WaitForComplete) {
                LoadingAnimation()
                Spacer(modifier = Modifier.height(24.dp))
            } else if (errorMessageId != null) {
                Text(
                    text = stringResource(errorMessageId),
                    style = errorTextStyle()
                )
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }

            BigRedButton(
                text = stringResource(id = R.string.userinfo_done_button),
                isEnable = userFormState == UserFormState.Idle && name.value.isNotEmpty()
            ) {
                focusManager.clearFocus()
                editUser(name.value, about.value, resultImagePath.value)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}