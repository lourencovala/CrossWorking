package ps.crossworking.testData

import ps.crossworking.dto.*
import java.util.UUID

val getMiniUsersData = listOf(
    UserApiOutputMini(
        "g2Nb9AA541fRNGGk8oE8yh42qJn1",
        "User 1",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/NYCS-bull-trans-1.svg/1200px-NYCS-bull-trans-1.svg.png"
    ),
    UserApiOutputMini(
        "Mz3IB4kpBugVffdIdC8SvaKlc0u1",
        "User 2",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/NYCS-bull-trans-2.svg/2048px-NYCS-bull-trans-2.svg.png"
    ),
    UserApiOutputMini(
        "j9rSb9GUagWFv5rsgmRviZYFYlm1",
        "User 3",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/NYCS-bull-trans-3.svg/1200px-NYCS-bull-trans-3.svg.png"
    )
)

val usersData = listOf(
    UserApiOutputShort(
        "g2Nb9AA541fRNGGk8oE8yh42qJn1",
        "User 1",
        "a@gmail.com",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/NYCS-bull-trans-1.svg/1200px-NYCS-bull-trans-1.svg.png",
        listOf(
            SkillApiOutput(
                UUID.fromString("91990805-003b-4103-8936-1ba7f4ab19d4"),
                "Kotlin",
                "One of my main programming languages.",
                "Technology"
            ),
            SkillApiOutput(
                UUID.fromString("e6503132-8ca0-4d38-b9b4-7d30c1123d56"),
                "Android",
                "Been developing Android for 2 years. Mainly in Kotlin",
                "Technology"
            ),
            SkillApiOutput(
                UUID.fromString("7f860930-84ee-468b-8ba4-e88fb032e4da"),
                "JavaScript",
                "Used JavaScript a lot for web development.",
                "Technology"
            )
        )
    ),
    UserApiOutputShort(
        "Mz3IB4kpBugVffdIdC8SvaKlc0u1",
        "User 2",
        "b@gmail.com",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/NYCS-bull-trans-2.svg/2048px-NYCS-bull-trans-2.svg.png",
        emptyList()
    ),
    UserApiOutputShort(
        "j9rSb9GUagWFv5rsgmRviZYFYlm1",
        "User 3",
        "c@gmail.com",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/NYCS-bull-trans-3.svg/1200px-NYCS-bull-trans-3.svg.png",
        emptyList()
    )
)

val userCreate = UserApiInput("0000000000", "testcreate@gmail.com")
val userCreateResult = UserApiOutputFull("0000000000", null, "testcreate@gmail.com", null, "https://crossworkingbucket.s3.eu-west-2.amazonaws.com/profile_default_picture.png", emptyList())

val userUpdate = UserApiUpdate(null, "Test", null)
val updateResult = UserApiOutputFull("j9rSb9GUagWFv5rsgmRviZYFYlm1", "User 3", "c@gmail.com", "Test", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/NYCS-bull-trans-3.svg/1200px-NYCS-bull-trans-3.svg.png", emptyList())

val alreadyExistentUserInput = UserApiInput("j9rSb9GUagWFv5rsgmRviZYFYlm1", "c@gmail.com")