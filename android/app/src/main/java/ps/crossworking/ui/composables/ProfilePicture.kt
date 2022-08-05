package ps.crossworking.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun ClickableBorderedProfilePicture(imageUrl: String, size: Dp, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            )
            .clickable {
                onClick()
            }
    ) {
        CoilImage(
            imageModel = imageUrl,
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(200),
        )
    }
}

@Composable
fun BorderedProfilePicture(imageUrl: String, size: Dp) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            ),
    ) {
        CoilImage(
            imageModel = imageUrl,
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(200),
        )
    }
}
