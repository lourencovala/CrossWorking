package ps.crossworking.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp

@Composable
fun MainTextField(
    labelText: String,
    toHide: Boolean = false,
    state: MutableState<String>,
    long: Boolean = false,
    focusManager: FocusManager,
    toCapitalize: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    isLastAction: Boolean = false,
    onDone: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it },
        textStyle = normalTextStyle(),
        label = { Text(text = labelText) },
        visualTransformation =
        if (toHide)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .width(300.dp)
            .height(if (long) 200.dp else 66.dp),
        keyboardOptions = KeyboardOptions(
            capitalization = if (toCapitalize) KeyboardCapitalization.Sentences else KeyboardCapitalization.None,
            keyboardType = keyboardType,
            imeAction = if (isLastAction) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },
            onDone = {
                focusManager.clearFocus()
                if (onDone != null)
                    onDone()
            }
        )
    )
}
