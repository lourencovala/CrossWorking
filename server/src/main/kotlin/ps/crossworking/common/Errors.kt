package ps.crossworking.common

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import ps.crossworking.exception.*

/**
 * Errors representation
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemJson(
    val type: String,
    val title: String,
    val detail: String,
    val status: Int
)

val problemJsonMapper = mapOf(
    ConnectionFailureException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/" +
                "blob/main/docs/api/errors/ConnectionFailure.md#connection-failure",
        title = "Not found",
        detail = "Internal connection error",
        status = 500
    ),
    UnknownException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/" +
                "main/docs/api/errors/UnkownException.md#unknown-exception",
        title = "Unknown error",
        detail = "Sorry you found a new error, please contact administrator",
        status = 400
    ),
    IdeaNotFoundException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/" +
                "main/docs/api/errors/IdeaException.md#idea-not-found",
        title = "Not found",
        detail = "The idea you are trying to find or use does not exist",
        status = 404
    ),
    IdeaAlreadyExistsException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs/" +
                "api/errors/IdeaException.md#idea-already-exists",
        title = "Already exists",
        detail = "There is an idea with this title for this user already",
        status = 409
    ),
    UserNotFoundException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/UserException.md#user-not-found",
        title = "Not found",
        detail = "The user you are trying to find or use does not exist",
        status = 404
    ),
    UserAlreadyExistsException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs/api/" +
                "errors/UserException.md#user-already-existent",
        title = "Already exists",
        detail = "There is a user with this email already",
        status = 409
    ),
    CandidateToSelfException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/CandidateException.md#candidate-to-your-own-idea",
        title = "Self Candidate",
        detail = "You are trying to candidate to your own idea",
        status = 409
    ),
    SkillMaxReachedException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/SkillException.md#max-number-of-skills-reached",
        title = "Skill max number reached",
        detail = "This user has already 8 skills",
        status = 409
    ),
    SkillAlreadyExistsException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs/" +
                "api/errors/SkillException.md#skill-already-exists",
        title = "Skill already existent",
        detail = "The skill you are trying to add already exists on this context",
        status = 409
    ),
    SkillSubjectNotFoundException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/SkillException.md#skill-subject-not-found",
        title = "Skill subject not found",
        detail = "The subject used does not exist",
        status = 404
    ),
    UserSkillNotFoundException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs/" +
                "api/errors/UserException.md#user-skill-not-found",
        title = "Not Found",
        detail = "The user or skill you are trying to found does not exist",
        status = 404
    ),
    IdeaSkillNotFoundException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/IdeaException.md#idea-skill-not-found",
        title = "Not Found",
        detail = "The idea or skill you are trying to found does not exist",
        status = 404
    ),
    CandidateSubjectNotFoundException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/CandidateException.md#candidate-subject-not-found",
        title = "Candidate or subject not found",
        detail = "The subject or candidate used does not exist",
        status = 404
    ),
    CandidateNotFoundException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/CandidateException.md#candidate-not-found",
        title = "Candidate not found",
        detail = "The candidate you are trying to obtain does not exist",
        status = 404
    ),
    CandidateAlreadyExistentException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main" +
                "/docs/api/errors/CandidateException.md#candidate-already-exists",
        title = "Already candidate",
        detail = "This user is already applied to this idea",
        status = 409
    ),
    BadRequestException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/BadRequestException.md#bad-request",
        title = "Bad Request",
        detail = "The request made is not valid",
        status = 400
    ),
    HttpMediaTypeNotSupportedException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main" +
                "/docs/api/errors/MediaTypeException.md#media-type-not-supported",
        title = "The content type is not supported",
        detail = "The select content type is not supported",
        status = 415
    ),
    HttpMediaTypeNotAcceptableException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main" +
                "/docs/api/errors/MediaTypeException.md#media-type-not-acceptable",
        title = "The content type is not acceptable",
        detail = "The select content type is not acceptable",
        status = 406
    ),
    HttpRequestMethodNotSupportedException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/BadRequestException.md#bad-request",
        title = "Bad Request",
        detail = "The request made is not valid",
        status = 400
    ),
    HttpMessageNotReadableException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/BadRequestException.md#bad-request",
        title = "Bad Request",
        detail = "The request made is not valid",
        status = 400
    ),
    ForbiddenAccessException::class to ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs" +
                "/api/errors/ForbiddenException.md#forbidden",
        title = "Forbidden",
        detail = "You are not authorized to access this resource",
        status = 403
    )
)

val unknownException = ProblemJson(
    type = "https://github.com/lourencovala/crossworking-project/" +
            "blob/main/docs/api/errors/UnkownException.md#unknown-exception",
    title = "Unknown error",
    detail = "Sorry you found a new error, please contact administrator",
    status = 500
)