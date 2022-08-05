package ps.crossworking

import okhttp3.MediaType
import okhttp3.ResponseBody
import ps.crossworking.model.*
import retrofit2.HttpException
import retrofit2.Response

var testIdea = Idea(
    "test",
    "idea test",
    "small description test",
    "description test",
    User("67890", "","","", "", emptyList()),
    10, emptyList())

var testIdeaShort = ShortIdea(
    testIdea.ideaId,
    testIdea.title,
    testIdea.smallDescription,
    testIdea.days,
    testIdea.user,
    testIdea.skills
)

var testCategory = Category("12345", "test Category")

var testSkill = Skill("12345", "test", "this is a test","test")

var testUser = User(
    "12345",
    "test User",
    "test@user.com",
    "test about",
    "",
    emptyList()
)

var testCandidate = Candidate(
    testUser,
    "pending",
    10,
    14
)

var testCandidature = CandidatureResult(
    "pending",
    10,
    14,
    MiniIdea(testIdea.ideaId, testIdea.title)
)

var testUserNoInfo = User(
    "12345",
    null,
    "test@user.com",
    "test about",
    "",
    emptyList()
)

val errorException = HttpException(Response.error<ResponseBody>(404, ResponseBody.create(MediaType.parse("application/problem+json"), "Not Found")))