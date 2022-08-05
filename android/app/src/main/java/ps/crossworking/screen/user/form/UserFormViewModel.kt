package ps.crossworking.screen.user.form

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ps.crossworking.R
import ps.crossworking.common.UserInstance
import ps.crossworking.exceptions.errorConversion
import javax.inject.Inject

sealed class UserFormState {
    object Idle : UserFormState()
    object WaitForComplete : UserFormState()
    object Added : UserFormState()
    object Completed : UserFormState()
}

@HiltViewModel
class UserFormViewModel @Inject constructor(
    application: Application,
    private val uploadImageAwsUseCase: UploadImageAwsUseCase,
    private val addNameAndPicUriUriUseCase: IAddNameAndPicUriUseCase
) : AndroidViewModel(application) {

    private val _userFormState: MutableState<UserFormState> = mutableStateOf(UserFormState.Idle)
    val userFormState: State<UserFormState> = _userFormState

    private val _errorMessageId: MutableState<Int?> = mutableStateOf(null)
    val errorMessageId: State<Int?> = _errorMessageId

    init {
        try {
            if (Amplify.Storage.plugins.isEmpty()) {
                Amplify.addPlugin(AWSCognitoAuthPlugin())
                Amplify.addPlugin(AWSS3StoragePlugin())
                Amplify.configure(application)
            }
        } catch (error: AmplifyException) {
            Amplify.removePlugin(AWSCognitoAuthPlugin())
            Amplify.addPlugin(AWSS3StoragePlugin())
            _errorMessageId.value = R.string.error_aws_s3
            _userFormState.value = UserFormState.Added
        }
    }

    fun editUser(name: String, about: String, pictureUri: Uri?) {
        _userFormState.value = UserFormState.WaitForComplete
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val profileImage = uploadImageAwsUseCase(pictureUri)
                val user = addNameAndPicUriUriUseCase(
                    name,
                    about,
                    profileImage
                )
                UserInstance.user.value = user
                _userFormState.value = UserFormState.Added
            } catch (e: Exception) {
                _errorMessageId.value = errorConversion(e)
            }
        }
    }

    fun makeComplete() {
        _userFormState.value = UserFormState.Completed
    }

    fun restoreInitialState() {
        _userFormState.value = UserFormState.Idle
    }

    fun clearError() {
        _errorMessageId.value = null
    }
}