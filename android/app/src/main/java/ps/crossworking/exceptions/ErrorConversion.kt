package ps.crossworking.exceptions

import ps.crossworking.R

val errorToMessageMapping = mapOf(
    UnknownException::class to R.string.unknown_error,
    CandidateOrIdeaNotFoundException::class to R.string.candidature_not_found,
    AlreadyCandidateException::class to R.string.already_candidate,
    UserNotFoundException::class to R.string.user_not_found,
    IdeaNotFoundException::class to R.string.idea_not_found,
    IdeaAlreadyExistsException::class to R.string.idea_already_existent,
    IdeaInvalidRequestException::class to R.string.invalid_create_idea,
    IdeaTitleTooLongException::class to R.string.idea_title_too_long,
    IdeaSmallDescriptionTooLongException::class to R.string.idea_smalldescription_too_long,
    SkillAlreadyExistsException::class to R.string.skill_already_existent,
    SkillOrUserNotFoundException::class to R.string.skill_not_found,
    SkillOrIdeaNotFoundException::class to R.string.skill_not_found,
    AuthServiceInternalException::class to R.string.internal_error,
    AuthServiceInvalidUserException::class to R.string.user_operation_error,
    InconsistentAuthState::class to R.string.user_error,
    ApiInternalException::class to R.string.internal_error,
    UploadingPhotoException::class to R.string.image_upload_error,
    WeakPasswordException::class to R.string.weak_password,
    EmailAlreadyExistsException::class to R.string.repeated_email,
    InvalidEmailException::class to R.string.invalid_email,
    InvalidPasswordException::class to R.string.wrong_password_error,
    UserDisabledException::class to R.string.user_disabled_error,
    TooManyRequestAuthException::class to R.string.too_many_requests,
    OperationNotAllowedAuthException::class to R.string.operation_not_allowed,
    MissingFieldException::class to R.string.missing_email_or_password,
    NetworkException::class to R.string.network_error
)

fun errorConversion(e: Exception): Int {
    return errorToMessageMapping[e::class] ?: R.string.unknown_error
}
