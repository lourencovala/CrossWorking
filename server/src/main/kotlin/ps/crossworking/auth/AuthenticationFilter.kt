package ps.crossworking.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.apache.http.HttpStatus
import org.apache.http.entity.ContentType
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ps.crossworking.common.ProblemJson
import ps.crossworking.common.unknownException
import ps.crossworking.exception.MissingAuthToken
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@ConditionalOnProperty(name = ["app.security"], havingValue = "true")
class AuthenticationFilter : Filter {

    val missingTokenError = ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs/api/errors/UnauthorizedException.md",
        title = "Authorization token Missing",
        detail = "To access this resource you need to provide an valid authorization token",
        401
    )
    val invalidToken = ProblemJson(
        type = "https://github.com/lourencovala/crossworking-project/blob/main/docs/api/errors/UnauthorizedException.md",
        title = "Invalid Authorization Token",
        detail = "The provided token is not valid ",
        401
    )

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val req = request as HttpServletRequest

        try {
            val idToken = req.getHeader("Authorization")?.replace("Bearer ", "") ?: throw MissingAuthToken()
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken)

            req.setAttribute("uid", decodedToken.uid)

            chain?.doFilter(request, response)
        } catch (exception: Exception) {
            val res = response as HttpServletResponse

            when (exception) {
                is MissingAuthToken -> {
                    res.status = HttpStatus.SC_UNAUTHORIZED
                    res.contentType = ContentType.APPLICATION_JSON.toString()
                    res.writer.write(convertObjectToJson(missingTokenError))
                }
                is FirebaseAuthException -> {
                    res.status = HttpStatus.SC_UNAUTHORIZED
                    res.contentType = ContentType.APPLICATION_JSON.toString()
                    res.writer.write(convertObjectToJson(invalidToken))
                }
                else -> {
                    res.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
                    res.contentType = ContentType.APPLICATION_JSON.toString()
                    res.writer.write(convertObjectToJson(unknownException))
                }
            }
        }
    }


    fun convertObjectToJson(obj: Any): String {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(obj)
    }
}
