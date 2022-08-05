package ps.crossworking.screen.skill.addUserSkill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import ps.crossworking.model.Category
import ps.crossworking.screen.skill.SkillInputScreenPlaceHolder
import ps.crossworking.ui.composables.ErrorScreen
import ps.crossworking.ui.composables.LoadingAnimation
import ps.crossworking.ui.composables.LoadingScreen
import ps.crossworking.ui.composables.errorTextStyle

@Composable
fun AddUserSkillScreen(
    viewModel: AddUserSkillViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val state by remember { viewModel.state }
    val isLoading by remember { viewModel.takingAction }
    val errorMessageId by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (state !is AddUserSkillsState.FetchedCategories)
            viewModel.fetchCategories()
        onDispose {
            if (state is AddUserSkillsState.Completed)
                viewModel.restoreInitialState()
        }
    }

    if (viewModel.state.value is AddUserSkillsState.Added) {
        onComplete()
        viewModel.makeComplete()
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
            when (val value: AddUserSkillsState = state) {
                is AddUserSkillsState.Loading -> {
                    LoadingScreen()
                }

                is AddUserSkillsState.FetchedCategories -> {
                    AddSkillToUser(
                        list = value.categories,
                        isLoading,
                        errorMessageId
                    ) { skillName, about, categoryId ->
                        viewModel.addSkill(skillName, about, categoryId)
                    }
                }

                is AddUserSkillsState.Error -> {
                    ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
                }
            }
        }
    }
}

@Composable
fun AddSkillToUser(
    list: List<Category>,
    isLoading: Boolean,
    errorId: Int?,
    addSkill: (skillName: String, about: String, categoryId: String) -> Unit
) {
    SkillInputScreenPlaceHolder(list = list, addSkill = addSkill, isLoading = isLoading) {
        if (isLoading)
            LoadingAnimation()
        else if (errorId != null) {
            Text(
                text = stringResource(id = errorId),
                style = errorTextStyle()
            )
        }
    }
}

