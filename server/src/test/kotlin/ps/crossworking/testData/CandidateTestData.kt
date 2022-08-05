package ps.crossworking.testData

import ps.crossworking.dto.*
import java.util.*

val user1CandidatureResults = listOf(
    CandidatureResultApiOutput(
        "pending",
        0,
        0,
        IdeaApiOutputMini(
            UUID.fromString("9d21f1c9-2836-4755-a8b8-1b7cc767609e"),
            "Idea 2"
        )
    )
)

val user3CandidatureResults = listOf(
    CandidatureResultApiOutput(
        "accepted",
        0,
        0,
        IdeaApiOutputMini(
            UUID.fromString("9d21f1c9-2836-4755-a8b8-1b7cc767609e"),
            "Idea 2"
        )
    )
)

val idea2CandidatureData = listOf(
    CandidateApiOutput(
        UserApiOutputShort(
            "g2Nb9AA541fRNGGk8oE8yh42qJn1",
            "User 1",
            "a@gmail.com",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/NYCS-bull-trans-1.svg/1200px-NYCS-bull-trans-1.svg.png",
            emptyList()
        ),
        "pending",
        0,
        0
    ),
    CandidateApiOutput(
        UserApiOutputShort(
            "j9rSb9GUagWFv5rsgmRviZYFYlm1",
            "User 3",
            "c@gmail.com",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/NYCS-bull-trans-3.svg/1200px-NYCS-bull-trans-3.svg.png",
            emptyList()
        ),
        "accepted",
        0,
        0
    )
)

val newPendingCandidature = CandidatureApiOutput(
    "pending",
    0,
    0
)

val applyToIdeaInput = CandidatureApiInput(ideasData[2].ideaId.toString())

val updateCandidature = CandidatureApiUpdate("accepted")

val acceptedNewCandidature = CandidateApiOutput(
    UserApiOutputShort(
        "g2Nb9AA541fRNGGk8oE8yh42qJn1",
        "User 1",
        "a@gmail.com",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/NYCS-bull-trans-1.svg/1200px-NYCS-bull-trans-1.svg.png",
        emptyList()
    ),
    "accepted",
    0,
    0
)