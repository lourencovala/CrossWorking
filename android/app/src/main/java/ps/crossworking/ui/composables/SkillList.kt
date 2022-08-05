package ps.crossworking.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import ps.crossworking.model.Skill

@Composable
fun SkillsList(skills: List<String>) {
    FlowRow(
        modifier = Modifier.padding(8.dp),
        mainAxisAlignment = MainAxisAlignment.Center,
        mainAxisSize = SizeMode.Expand,
        crossAxisSpacing = 12.dp,
        mainAxisSpacing = 8.dp
    ) {
        skills.forEach { skillName ->
            SkillLabel(skillName = skillName)
        }
    }
}

@Composable
fun SmallSkillDisplay(skills: List<Skill>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp,
                end = 16.dp,
                start = 16.dp,
                bottom = 16.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        skills.forEachIndexed { index, it ->
            if (index > 3)
                return@forEachIndexed
            SkillLabel(
                skillName = it.name,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun SkillLabel(
    skillName: String,
    modifier: Modifier = Modifier.padding(all = 12.dp)
) {
    Card(
        modifier = Modifier
            .wrapContentSize(),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(33.dp),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Text(
            text = skillName,
            style = normalTextStyle(),
            modifier = modifier
        )
    }
}
