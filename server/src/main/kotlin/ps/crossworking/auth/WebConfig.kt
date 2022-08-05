package ps.crossworking.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ps.crossworking.common.IDEAS_PATH
import ps.crossworking.common.USER_PATH


@Configuration
@EnableWebMvc
@ConditionalOnProperty(name = ["app.security"], havingValue = "true")
class WebConfig : WebMvcConfigurer {

    @Autowired
    lateinit var sameUserInterceptor: SameUserInterceptor

    @Autowired
    lateinit var ideaOwnerInterceptor: IdeaOwnerInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(sameUserInterceptor).addPathPatterns("$USER_PATH/**").excludePathPatterns(USER_PATH)
        registry.addInterceptor(ideaOwnerInterceptor).addPathPatterns("$IDEAS_PATH/**")
    }
}