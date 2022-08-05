package ps.crossworking.testData

import ps.crossworking.dto.SkillApiInput
import java.util.*

val skillInput = SkillApiInput("Skill 1", "Some text", UUID.fromString("bd1fde89-4ccd-4c51-a4a4-e39dc40009dd"))

val alreadyExistentUserSkillInput = SkillApiInput("Android", "Should result in error", UUID.fromString("bd1fde89-4ccd-4c51-a4a4-e39dc40009dd"))
