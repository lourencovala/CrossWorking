package ps.crossworking.dao

import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DaoDependencies {

    @Bean
    fun ideaDao(jdbi: Jdbi): IdeaDao = jdbi.onDemand(IdeaDao::class.java)

    @Bean
    fun userDao(jdbi: Jdbi): UserDao = jdbi.onDemand(UserDao::class.java)

    @Bean
    fun skillDao(jdbi: Jdbi): SkillDao = jdbi.onDemand(SkillDao::class.java)

    @Bean
    fun category(jdbi: Jdbi): CategoryDao = jdbi.onDemand(CategoryDao::class.java)

    @Bean
    fun candidate(jdbi: Jdbi): CandidateDao = jdbi.onDemand(CandidateDao::class.java)
}