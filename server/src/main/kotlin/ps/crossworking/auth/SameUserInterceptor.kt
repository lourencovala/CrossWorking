package ps.crossworking.auth

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.withExceptionHandler
import ps.crossworking.exception.ForbiddenAccessException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class SameUserInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as HandlerMethod

        if (request.method != "GET" || handlerMethod.method.isAnnotationPresent(UnsafeGet::class.java)) {
            withExceptionHandler {
                val uid: String = request.getAttribute("uid") as String? ?: throw ForbiddenAccessException()

                val userId = request.requestURI.split("$USER_PATH/")[1].split("/")[0]

                if (userId != uid)
                    throw ForbiddenAccessException()
            }
        }

        return true
    }
}