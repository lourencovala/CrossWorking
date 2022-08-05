package ps.crossworking.ui.composables

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ps.crossworking.ui.theme.LogoFont
import ps.crossworking.ui.theme.MyruadRegular
import ps.crossworking.ui.theme.buttonText
import ps.crossworking.ui.theme.outline

@Composable
fun topBarTextStyle() = TextStyle(
    fontFamily = MyruadRegular,
    color = MaterialTheme.colors.primary,
    fontSize = 30.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun authTitleTextStyle() = TextStyle(
    fontFamily = LogoFont,
    color = MaterialTheme.colors.primary,
    fontSize = 40.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun bigBoldMainColorTextStyle() = TextStyle(
    color = MaterialTheme.colors.primary,
    fontFamily = MyruadRegular,
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun mediumBoldMainColorTextStyle() = TextStyle(
    color = MaterialTheme.colors.primary,
    fontFamily = MyruadRegular,
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun mediumMainColorTextStyle() = TextStyle(
    color = MaterialTheme.colors.primary,
    fontFamily = MyruadRegular,
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun mediumTextStyle() = TextStyle(
    fontFamily = MyruadRegular,
    color = MaterialTheme.colors.secondary,
    fontSize = 24.sp
)

@Composable
fun normalTextStyle() = TextStyle(
    color = MaterialTheme.colors.secondary,
    fontFamily = MyruadRegular,
    fontSize = 16.sp
)

@Composable
fun errorTextStyle() = TextStyle(
    fontFamily = MyruadRegular,
    color = MaterialTheme.colors.secondary,
    fontSize = 20.sp,
    textAlign = TextAlign.Center
)

@Composable
fun buttonTextStyle() = TextStyle(
    fontFamily = MyruadRegular,
    color = MaterialTheme.colors.buttonText,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold
)

@Composable
fun matchingOutlineTextStyle() = TextStyle(
    color = MaterialTheme.colors.outline,
    fontFamily = MyruadRegular
)