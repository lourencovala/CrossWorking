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
import ps.crossworking.mockRepositories.UserRepositoryMock
import ps.crossworking.screen.user.GetUserUseCase
import ps.crossworking.screen.user.profile.other.OtherProfileViewModel
import ps.crossworking.screen.user.profile.other.ProfileState
import ps.crossworking.testUser

@OptIn(ExperimentalCoroutinesApi::class)
class OtherProfileViewModelTest {
    private lateinit var userRepo: UserRepositoryMock
    private lateinit var otherProfileViewModel: OtherProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        userRepo = UserRepositoryMock()
        otherProfileViewModel = OtherProfileViewModel(
            GetUserUseCase(userRepo)
        )

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get profile`() {
        runTest {
            otherProfileViewModel.getProfile(testUser.userId)
            advanceUntilIdle()
            assertEquals(ProfileState.Success::class, otherProfileViewModel.profileState.value::class)
            assertEquals(testUser, (otherProfileViewModel.profileState.value as ProfileState.Success).data)
        }
    }

    @Test
    fun `exception while getting profile`() {
        runTest {
            val exception = UserNotFoundException()
            userRepo.prepareException(exception)
            otherProfileViewModel.getProfile(testUser.userId)
            advanceUntilIdle()
            assertEquals(ProfileState.Error::class, otherProfileViewModel.profileState.value::class)
            assertEquals(errorConversion(exception), (otherProfileViewModel.profileState.value as ProfileState.Error).errorMessageId)
        }
    }

    @Test
    fun `exception while refreshing`() {
        runTest {
            val exception = UserNotFoundException()
            otherProfileViewModel.getProfile(testUser.userId)
            advanceUntilIdle()
            userRepo.prepareException(exception)
            otherProfileViewModel.refresh(testUser.userId)
            advanceUntilIdle()
            assertEquals(errorConversion(exception), otherProfileViewModel.errorMessageId.value)
        }
    }
}