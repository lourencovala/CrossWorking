package ps.crossworking.viewmodels

import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.exceptions.UserNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.AuthRepositoryMock
import ps.crossworking.screen.user.authentication.SignUpGoogleUseCase
import ps.crossworking.screen.user.authentication.signup.SignUpState
import ps.crossworking.screen.user.authentication.signup.SignUpUseCase
import ps.crossworking.screen.user.authentication.signup.SignUpViewModel
import ps.crossworking.testUser

@OptIn(ExperimentalCoroutinesApi::class)
class SIgnUpViewModelTest {
    private lateinit var authRepo: AuthRepositoryMock
    private lateinit var signUpViewModel: SignUpViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        authRepo = AuthRepositoryMock()
        signUpViewModel = SignUpViewModel(
            SignUpUseCase(authRepo),
            SignUpGoogleUseCase(authRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `sign in`() {
        runTest {
            signUpViewModel.createAccount(testUser.email, "password")
            advanceUntilIdle()
            TestCase.assertEquals(SignUpState.Success::class, signUpViewModel.state.value::class)
        }
    }

    @Test
    fun `exception while signing in`() {
        runTest {
            val exception = UserNotFoundException()
            authRepo.prepareException(exception)
            signUpViewModel.createAccount(testUser.email, "password")
            advanceUntilIdle()
            TestCase.assertEquals(SignUpState.Error::class, signUpViewModel.state.value::class)
            TestCase.assertEquals(errorConversion(exception), (signUpViewModel.state.value as SignUpState.Error).errorMessageId)
        }
    }
}