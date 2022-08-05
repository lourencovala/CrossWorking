package ps.crossworking.testData

import ps.crossworking.dto.*
import java.util.*

val ideasData = listOf(
    IdeaApiOutputShort(
        UUID.fromString("c5e36241-e105-4d6c-9884-da5105473c48"),
        "Idea 1",
        "small text",
        getMiniUsersData[0],
        0,
        emptyList()
    ),
    IdeaApiOutputShort(
        UUID.fromString("9d21f1c9-2836-4755-a8b8-1b7cc767609e"),
        "Idea 2",
        "small text",
        getMiniUsersData[1],
        0,
        listOf(
            SkillApiOutput(
                UUID.fromString("7f860930-84ee-468b-8ba4-e88fb032e4da"),
                "JavaScript",
                "Needs to be efficient with JavaScript",
                "Technology"
            )
        )
    ),
    IdeaApiOutputShort(
        UUID.fromString("454f5b61-d931-45bf-ba2c-1d8f16f621af"),
        "Idea 3",
        "small text",
        getMiniUsersData[2],
        0,
        emptyList()
    )
)

val idea1Full = IdeaApiOutputFull(
    UUID.fromString("c5e36241-e105-4d6c-9884-da5105473c48"),
    "Idea 1",
    "small text",
    "Very large text. Contains a lot of information. Occupies a lot of lines ideally. A lot of text here. More text. Maybe some more. More wont hurt. This is for testing purposes.",
    getMiniUsersData[0],
    0,
    emptyList()
)

val ideaToCreate = IdeaApiInput("Create Test", "this is small", "this is description")

val ideaToUpdate = IdeaApiUpdate("UpdateTest", null, null)

val alreadyExistentIdeaInput = IdeaApiInput("Idea 1", "error", "title should repeat and fail")
