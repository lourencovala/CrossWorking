package ps.crossworking.auth

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import ps.crossworking.common.withExceptionHandler
import ps.crossworking.exception.ForbiddenAccessException
import ps.crossworking.service.IdeaService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class IdeaOwnerInterceptor(val ideaService: IdeaService) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val handlerMethod = handler as HandlerMethod

        if (request.method != "GET" || handlerMethod.method.isAnnotationPresent(UnsafeGet::class.java)) {
            withExceptionHandler {
                val uid: String = request.getAttribute("uid") as String? ?: throw ForbiddenAccessException()

                val ideaId = request.requestURI.split("ideas/")[1].split("/")[0]

                val ideaOwner = ideaService.getIdeaOwner(ideaId)

                if (ideaOwner != uid)
                    throw ForbiddenAccessException()
            }
        }

        return true
    }
}