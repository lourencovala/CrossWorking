package ps.crossworking.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ps.crossworking.R

@Composable
fun AppLogo() {
    Image(
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 20.dp,
                bottom = 20.dp,
                end = 20.dp
            )
            .width(100.dp)
            .height(100.dp),
        painter = painterResource(R.drawable.brand_logo),
        contentDescription = "Brand Logo"
    )
}
