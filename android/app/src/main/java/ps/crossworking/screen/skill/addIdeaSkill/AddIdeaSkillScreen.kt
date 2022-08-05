package ps.crossworking.screen.skill.addIdeaSkill

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import ps.crossworking.model.Category
import ps.crossworking.screen.skill.SkillInputScreenPlaceHolder
import ps.crossworking.ui.composables.ErrorScreen
import ps.crossworking.ui.composables.LoadingAnimation
import ps.crossworking.ui.composables.LoadingScreen
import ps.crossworking.ui.composables.errorTextStyle

@Composable
fun AddIdeaSkillScreen(
    ideaId: String,
    viewModel: AddIdeaSkillViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val state by remember { viewModel.state }
    val isLoading by remember { viewModel.takingAction }
    val errorMessage by remember { viewModel.errorMessageId }

    DisposableEffect(key1 = Unit) {
        if (state !is AddIdeaSkillsState.FetchedCategories)
            viewModel.fetchCategories()
        onDispose {
            if (state is AddIdeaSkillsState.Completed)
                viewModel.restoreInitialState()
        }
    }

    if (state is AddIdeaSkillsState.Added) {
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
            when (val value: AddIdeaSkillsState = state) {
                is AddIdeaSkillsState.Loading -> {
                    LoadingScreen()
                }

                is AddIdeaSkillsState.FetchedCategories -> {
                    AddSkillToIdea(
                        list = value.categories,
                        isLoading = isLoading,
                        errorText = errorMessage
                    ) { skillName, about, categoryId ->
                        viewModel.addSkill(ideaId, skillName, about, categoryId)
                    }
                }

                is AddIdeaSkillsState.Error -> {
                    ErrorScreen(errorMessage = stringResource(id = value.errorMessageId))
                }
            }
        }
    }

}

@Composable
fun AddSkillToIdea(
    list: List<Category>,
    isLoading: Boolean,
    errorText: Int?,
    addSkill: (skillName: String, about: String, categoryId: String) -> Unit
) {
    SkillInputScreenPlaceHolder(list = list, addSkill = addSkill, isLoading = isLoading) {
        if (isLoading) {
            LoadingAnimation()
            Spacer(modifier = Modifier.height(24.dp))
        } else if (errorText != null) {
            Text(
                text = stringResource(id = errorText),
                style = errorTextStyle()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}