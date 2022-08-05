package ps.crossworking.viewmodels

import junit.framework.TestCase.assertEquals
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
import ps.crossworking.screen.user.authentication.signin.SignInUseCase
import ps.crossworking.screen.user.authentication.signin.SignInViewModel
import ps.crossworking.testUser
import ps.crossworking.testUserNoInfo

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {
    private lateinit var authRepo: AuthRepositoryMock
    private lateinit var signInViewModel: SignInViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        authRepo = AuthRepositoryMock()
        signInViewModel = SignInViewModel(
            SignInUseCase(authRepo),
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
            signInViewModel.login(testUser.email, "password")
            advanceUntilIdle()
            assertEquals(SignInViewModel.SignInState.Success::class, signInViewModel.state.value::class)
            assertEquals(testUserNoInfo, (signInViewModel.state.value as SignInViewModel.SignInState.Success).user)
        }
    }

    @Test
    fun `exception while signing in`() {
        runTest {
            val exception = UserNotFoundException()
            authRepo.prepareException(exception)
            signInViewModel.login(testUser.email, "password")
            advanceUntilIdle()
            assertEquals(SignInViewModel.SignInState.Error::class, signInViewModel.state.value::class)
            assertEquals(errorConversion(exception), (signInViewModel.state.value as SignInViewModel.SignInState.Error).errorMessageId)
        }
    }
}