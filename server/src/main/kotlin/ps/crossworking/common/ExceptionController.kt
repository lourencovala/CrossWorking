package ps.crossworking.common

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionController {

    @ExceptionHandler
    fun handlerConnectionFailure(error: Exception): ResponseEntity<ProblemJson> {
        val errorValue = problemJsonMapper[error::class] ?: unknownException
        return ResponseEntity
            .status(errorValue.status)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                errorValue
            )
    }
}
