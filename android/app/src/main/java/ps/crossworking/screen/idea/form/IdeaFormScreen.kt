package ps.crossworking.screen.idea.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import ps.crossworking.R
import ps.crossworking.ui.composables.*

@Composable
fun IdeaFormScreen(
    viewModel: IdeaFormViewModel = hiltViewModel(),
    ideaId: String?,
    onComplete: (ideaId: String) -> Unit,
) {
    val editMode = ideaId != null

    val ideaFormState by remember { viewModel.ideaFormState }
    val takingAction by remember { viewModel.takingAction }
    val errorMessageId by remember { viewModel.errorMessageId }

    val title = rememberSaveable { mutableStateOf("") }
    val smallDescription = rememberSaveable { mutableStateOf("") }
    val description = rememberSaveable { mutableStateOf("") }

    DisposableEffect(key1 = Unit) {
        if (editMode && (ideaFormState is IdeaFormState.Loading))
            viewModel.getIdea(ideaId!!)
        onDispose {
            if (ideaFormState is IdeaFormState.Completed)
                viewModel.restoreInitialState()
        }
    }

    when (val value: IdeaFormState = ideaFormState) {
        is IdeaFormState.CreatedIdea -> {
            onComplete(value.idea.ideaId)
            viewModel.makeCompleted()
            return
        }

        is IdeaFormState.EditedIdea -> {

            onComplete(value.idea.ideaId)
            viewModel.makeCompleted()
            return
        }

        is IdeaFormState.Error -> {
            ErrorScreen(stringResource(value.errorMessageId))
        }

        else -> {
            if (ideaFormState is IdeaFormState.GotIdea) {
                val idea = (ideaFormState as IdeaFormState.GotIdea).idea
                title.value = idea.title
                smallDescription.value = idea.smallDescription
                description.value = idea.description
            }
            IdeaForm(
                editMode,
                takingAction,
                title,
                smallDescription,
                description,
                errorMessageId,
                { s1, s2, s3 -> viewModel.createIdea(s1, s2, s3) },
                { s1, s2, s3 -> viewModel.editIdea(s1, s2, s3) }
            )
        }
    }
}

@Composable
fun IdeaForm(
    editMode: Boolean,
    takingAction: Boolean,
    title: MutableState<String>,
    smallDescription: MutableState<String>,
    description: MutableState<String>,
    errorMessageId: Int?,
    createIdea: (String, String, String) -> Unit,
    editIdea: (String, String, String) -> Unit
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
            MainTextField(
                labelText = stringResource(id = R.string.idea_title),
                state = title,
                focusManager = focusManager,
                toCapitalize = true
            )
            MainTextField(
                labelText = stringResource(id = R.string.idea_smallDescription),
                state = smallDescription,
                focusManager = focusManager,
                toCapitalize = true
            )
            MainTextField(
                labelText = stringResource(id = R.string.idea_description),
                state = description,
                long = true,
                focusManager = focusManager,
                toCapitalize = true,
                isLastAction = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (takingAction) {
                LoadingAnimation()
                Spacer(modifier = Modifier.height(24.dp))
            } else if (errorMessageId != null) {
                Text(
                    text = stringResource(errorMessageId),
                    style = errorTextStyle()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            BigRedButton(
                text = if (!editMode) stringResource(id = R.string.idea_create)
                else stringResource(id = R.string.idea_edit),
                isEnable = !takingAction && title.value != "" && smallDescription.value != "" && description.value != ""
            ) {
                focusManager.clearFocus()
                if (!editMode)
                    createIdea(title.value, smallDescription.value, description.value)
                else editIdea(title.value, smallDescription.value, description.value)
            }
        }
    }
}
