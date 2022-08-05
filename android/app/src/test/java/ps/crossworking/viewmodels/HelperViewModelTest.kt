package ps.crossworking.viewmodels

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import ps.crossworking.exceptions.UserNotFoundException
import ps.crossworking.helper.AccountState
import ps.crossworking.helper.GetOwnUserUseCase
import ps.crossworking.helper.HelperViewModel
import ps.crossworking.mockRepositories.AuthRepositoryMock
import ps.crossworking.screen.user.IsLoggedInUseCase
import ps.crossworking.screen.user.profile.own.SignOutUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class HelperViewModelTest  {

    private lateinit var authRepo: AuthRepositoryMock
    private lateinit var helperViewModel: HelperViewModel



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp(){
        authRepo = AuthRepositoryMock()
        helperViewModel = HelperViewModel(GetOwnUserUseCase(authRepo),IsLoggedInUseCase(authRepo), SignOutUseCase(authRepo))

        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
        fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test not logged in`() {
        runTest {

            helperViewModel.getUserIfExist()
            advanceUntilIdle()
            assertEquals(AccountState.NotLoggedIn, helperViewModel.state.value)

        }
    }

    @Test
    fun `test logged in`() {
        runTest {
            authRepo.startWithUserComplete()
            helperViewModel.getUserIfExist()
            advanceUntilIdle()
            assertEquals(AccountState.LoggedIn, helperViewModel.state.value)

        }
    }

    @Test
    fun `test need info`() {
        runTest {
            authRepo.startWithUserIncomplete()
            helperViewModel.getUserIfExist()
            advanceUntilIdle()
            assertEquals(AccountState.NeedsInfo, helperViewModel.state.value)

        }
    }

    @Test
    fun `test exception handle when is crossworking exception`() {
        runTest {
            authRepo.startWithUserComplete()
            authRepo.prepareException(UserNotFoundException())
            authRepo.restrictedToGetUser()
            helperViewModel.getUserIfExist()
            advanceUntilIdle()
            assertEquals(AccountState.NotLoggedIn, helperViewModel.state.value)

        }
    }

    @Test
    fun `test sign out cascade exception `() {
        runTest {
            authRepo.startWithUserComplete()
            authRepo.prepareException(UserNotFoundException())
            helperViewModel.getUserIfExist()
            advanceUntilIdle()
            assertEquals(AccountState.NotLoggedIn, helperViewModel.state.value)

        }
    }

    @Test
    fun `test is refreshing`() {
        runTest {
            helperViewModel.getUserIfExist()
            advanceUntilIdle()
            assertFalse(helperViewModel.isRefreshing.value)

        }
    }
}