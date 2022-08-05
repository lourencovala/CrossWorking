package ps.crossworking.exceptions

val firebaseErrorMapping = mapOf(
    "ERROR_WEAK_PASSWORD" to WeakPasswordException(),
    "ERROR_EMAIL_ALREADY_IN_USE" to EmailAlreadyExistsException(),
    "ERROR_INVALID_EMAIL" to InvalidEmailException(),
    "ERROR_WRONG_PASSWORD" to InvalidPasswordException(),
    "ERROR_USER_DISABLED" to UserDisabledException(),
    "ERROR_TOO_MANY_REQUESTS" to TooManyRequestAuthException(),
    "ERROR_OPERATION_NOT_ALLOWED" to OperationNotAllowedAuthException(),
)
