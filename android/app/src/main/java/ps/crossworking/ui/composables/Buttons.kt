package ps.crossworking.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BigRedButton(
    modifier: Modifier = Modifier
        .width(300.dp)
        .height(50.dp),
    text: String,
    isEnable: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(32.dp),
        modifier = modifier,
        enabled = isEnable
    ) {
        Text(
            text = text,
            style = buttonTextStyle()
        )
    }
}
