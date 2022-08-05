package ps.crossworking.viewmodels

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.exceptions.UnknownException
import ps.crossworking.exceptions.UserNotFoundException
import ps.crossworking.exceptions.errorConversion
import ps.crossworking.mockRepositories.AuthRepositoryMock
import ps.crossworking.mockRepositories.UserRepositoryMock
import ps.crossworking.repository.AuthRepository
import ps.crossworking.screen.user.GetUserUseCase
import ps.crossworking.screen.user.profile.other.OtherProfileViewModel
import ps.crossworking.screen.user.profile.other.ProfileState
import ps.crossworking.screen.user.profile.own.LogoutState
import ps.crossworking.screen.user.profile.own.OwnProfileViewModel
import ps.crossworking.screen.user.profile.own.SignOutUseCase
import ps.crossworking.testUser

@OptIn(ExperimentalCoroutinesApi::class)
class OwnProfileViewModelTest {
    private lateinit var userRepo: UserRepositoryMock
    private lateinit var authRepo: AuthRepositoryMock
    private lateinit var ownProfileViewModel: OwnProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        userRepo = UserRepositoryMock()
        authRepo = AuthRepositoryMock()
        ownProfileViewModel = OwnProfileViewModel(
            GetUserUseCase(userRepo),
            SignOutUseCase(authRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun logout() {
        runTest {
            ownProfileViewModel.logOut()
            advanceUntilIdle()
            assertEquals(LogoutState.LoggedOut,ownProfileViewModel.isLoggedOut.value)
        }
    }

    @Test
    fun `exception while logging out`() {
        runTest {
            val exception = UnknownException()
            authRepo.prepareException(exception)
            ownProfileViewModel.logOut()
            advanceUntilIdle()
            assertEquals(errorConversion(exception), ownProfileViewModel.errorMessageId.value)
        }
    }
}