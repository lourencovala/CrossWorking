package ps.crossworking.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ps.crossworking.common.withExceptionHandler
import ps.crossworking.dao.IdeaDao
import ps.crossworking.dao.SkillDao
import ps.crossworking.dao.UserDao
import ps.crossworking.exception.IdeaNotFoundException
import ps.crossworking.exception.IdeaSkillNotFoundException
import ps.crossworking.exception.UserNotFoundException
import ps.crossworking.exception.UserSkillNotFoundException
import ps.crossworking.model.SkillInput
import ps.crossworking.model.SkillOutput
import java.util.*

/**
 * Business logic for Skills.
 */
@Component
class SkillService(val dao: SkillDao, val userDao: UserDao, val ideaDao: IdeaDao) {

    fun addUserSkill(userId: String, input: SkillInput): SkillOutput {
        return withExceptionHandler {
            dao.addUserSkill(userId, input)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getUserSkills(userId: String): List<SkillOutput> {
        return withExceptionHandler {
            val skillList = dao.getUserSkills(userId)

            if (skillList.isEmpty())
                userDao.get(userId) ?: throw UserNotFoundException()

            skillList
        }
    }

    fun getUserSkill(userId: String, skillId: String): SkillOutput {
        return withExceptionHandler {
            dao.getUserSkill(userId, UUID.fromString(skillId)) ?: throw UserSkillNotFoundException()
        }
    }

    fun deleteUserSkill(userId: String, skillId: String) {
        withExceptionHandler {
            dao.deleteUserSkill(userId, UUID.fromString(skillId)) ?: throw UserSkillNotFoundException()
        }
    }

    fun addIdeaSkill(ideaId: String, input: SkillInput): SkillOutput {
        return withExceptionHandler {
            dao.addIdeaSkill(UUID.fromString(ideaId), input)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getIdeaSkills(ideaId: String): List<SkillOutput> {
        return withExceptionHandler {
            val list = dao.getIdeaSkills(UUID.fromString(ideaId))

            if (list.isEmpty())
                ideaDao.getIdea(UUID.fromString(ideaId)) ?: throw IdeaNotFoundException()

            list
        }
    }

    fun getIdeaSkill(ideaId: String, skillId: String): SkillOutput {
        return withExceptionHandler {
            dao.getIdeaSkill(UUID.fromString(ideaId), UUID.fromString(skillId)) ?: throw IdeaSkillNotFoundException()
        }
    }

    fun deleteIdeaSkill(ideaId: String, skillId: String) {
        withExceptionHandler {
            dao.deleteIdeaSkill(UUID.fromString(ideaId), UUID.fromString(skillId)) ?: throw IdeaSkillNotFoundException()
        }
    }
}