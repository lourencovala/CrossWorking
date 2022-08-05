package ps.crossworking.exceptions

open class CrossWorkingException : Exception()

class UnknownException : CrossWorkingException()

class NetworkException : CrossWorkingException()

class CandidateOrIdeaNotFoundException : CrossWorkingException()
class AlreadyCandidateException : CrossWorkingException()

class UserNotFoundException : CrossWorkingException()

class IdeaNotFoundException : CrossWorkingException()
class IdeaAlreadyExistsException : CrossWorkingException()
class IdeaInvalidRequestException: CrossWorkingException()
class IdeaTitleTooLongException: CrossWorkingException()
class IdeaSmallDescriptionTooLongException: CrossWorkingException()

class SkillAlreadyExistsException : CrossWorkingException()
class SkillOrUserNotFoundException : CrossWorkingException()
class SkillOrIdeaNotFoundException : CrossWorkingException()

class AuthServiceInternalException : CrossWorkingException()
class AuthServiceInvalidUserException : CrossWorkingException()
class InconsistentAuthState : CrossWorkingException()

class ApiInternalException : CrossWorkingException()
class RequestApiException : CrossWorkingException()

class UploadingPhotoException : CrossWorkingException()

class WeakPasswordException : CrossWorkingException()
class EmailAlreadyExistsException : CrossWorkingException()
class InvalidEmailException : CrossWorkingException()
class InvalidPasswordException : CrossWorkingException()
class UserDisabledException : CrossWorkingException()
class TooManyRequestAuthException : CrossWorkingException()
class OperationNotAllowedAuthException : CrossWorkingException()
class MissingFieldException : CrossWorkingException()
