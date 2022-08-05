package ps.crossworking.screen.skill

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ps.crossworking.R
import ps.crossworking.model.Category
import ps.crossworking.model.Skill
import ps.crossworking.ui.composables.*
import ps.crossworking.ui.theme.outline

@Composable
fun SingleSkill(
    skill: Skill,
    isDeletable: Boolean,
    deleteSkill: (skillId: String) -> Unit
) {
    var isSelected by remember {
        mutableStateOf(false)
    }

    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
            shape = RoundedCornerShape(33.dp),
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .weight(0.75f)
        ) {
            Column(
                modifier = if (isDeletable)
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clickable {
                            isSelected = !isSelected
                        }
                else
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
            ) {
                Text(
                    text = skill.name,
                    style = mediumTextStyle()
                )
                Spacer(modifier = Modifier.height(6.dp))
                if (skill.about != null) {
                    Text(
                        text = skill.about,
                        style = normalTextStyle()
                    )
                }
            }
        }

        if (isSelected) {
            Button(
                onClick = { deleteSkill(skill.skillId) },
                modifier = Modifier
                    .weight(0.25f)
                    .padding(end = 12.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.skill_list_delete_skill_button),
                    style = buttonTextStyle()
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SkillScreen(
    skills: List<Skill>,
    isDeletable: Boolean,
    goToAddSkill: () -> Unit,
    onComplete: ((Unit) -> Unit)?,
    deleteSkill: (skillId: String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = skills,
                key = { skill -> skill.skillId }
            ) { item ->
                SingleSkill(skill = item, isDeletable = isDeletable, deleteSkill = deleteSkill)
            }

        }
        if (isDeletable) {
            Divider(color = MaterialTheme.colors.outline)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                BigRedButton(
                    text = stringResource(id = R.string.skill_list_add_skill_button),
                    isEnable = skills.size <= 7,
                    modifier = Modifier
                        .height(50.dp)
                        .width(190.dp)
                ) {
                    goToAddSkill()
                }
                if (onComplete != null) {
                    BigRedButton(
                        text = stringResource(id = R.string.skill_list_complete_signup_button),
                        modifier = Modifier
                            .height(50.dp)
                            .width(190.dp)
                    ) {
                        onComplete(Unit)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun SkillInputScreenPlaceHolder(
    list: List<Category>,
    addSkill: (skillName: String, about: String, categoryId: String) -> Unit,
    isLoading: Boolean,
    toDisplay: @Composable () -> Unit
) {
    val skillName = remember { mutableStateOf("") }
    val about = remember { mutableStateOf("") }
    val txt = stringResource(id = R.string.insert_skill_category)
    val selectedText = remember { mutableStateOf(txt) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        toDisplay()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainTextField(
                labelText = stringResource(id = R.string.insert_skill_name),
                state = skillName,
                focusManager = focusManager,
                toCapitalize = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            MainTextField(
                labelText = stringResource(id = R.string.insert_skill_about),
                state = about,
                long = true,
                focusManager = focusManager,
                toCapitalize = true,
                isLastAction = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomDropdownMenu(
                options = list.map { it.name },
                selectedState = selectedText
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        BigRedButton(
            text = stringResource(id = R.string.insert_skill_add_button),
            isEnable = !isLoading &&
                    skillName.value.isNotBlank() &&
                    selectedText.value != txt
        ) {
            focusManager.clearFocus()

            addSkill(
                skillName.value,
                about.value,
                list.find { it.name == selectedText.value }!!.categoryId
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}