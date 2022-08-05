package ps.crossworking.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CustomDropdownMenu(
    options: List<String>,
    selectedState: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        Modifier.wrapContentSize(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(33.dp),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            Modifier
                .clickable {
                    expanded = !expanded
                }
                .padding(top = 16.dp, bottom = 16.dp)
                .width(300.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedState.value,
                style = normalTextStyle(),
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Drop down menu")
            DropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(color = MaterialTheme.colors.background)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .width(400.dp),
            ) {
                options.forEach { itemText ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            selectedState.value = itemText
                        }, modifier = Modifier.clip(CircleShape)
                    ) {
                        Text(
                            text = itemText,
                            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                            style = normalTextStyle()
                        )
                    }
                }
            }
        }
    }
}
