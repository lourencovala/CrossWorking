package ps.crossworking.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ps.crossworking.common.withExceptionHandler
import ps.crossworking.dao.IdeaDao
import ps.crossworking.dao.UserDao
import ps.crossworking.exception.BadRequestException
import ps.crossworking.exception.IdeaNotFoundException
import ps.crossworking.exception.UserNotFoundException
import ps.crossworking.model.IdeaInput
import ps.crossworking.model.IdeaOutput
import ps.crossworking.model.IdeaUpdate
import java.util.*

/**
 * Business logic for Ideas.
 */
@Component
class IdeaService(val dao: IdeaDao, val userDao: UserDao, val skillService: SkillService) {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getIdeaList(pageIndex: Int, pageSize: Int): List<IdeaOutput> {
        return withExceptionHandler {
            if (pageIndex < 1 || pageSize < 1)
                throw BadRequestException()

            val ideas = dao.getIdeasList((pageIndex - 1) * pageSize, pageSize)
            ideas.forEach { it.skills = skillService.getIdeaSkills(it.ideaId.toString()) }
            ideas
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun getIdea(ideaId: String): IdeaOutput {
        return withExceptionHandler {
            (dao.getIdea(UUID.fromString(ideaId)) ?: throw IdeaNotFoundException())
                .apply {
                    skills = skillService.getIdeaSkills(ideaId)
                }
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateIdea(ideaId: String, update: IdeaUpdate): IdeaOutput {
        withExceptionHandler {
            dao.updateIdea(UUID.fromString(ideaId), update)
        }

        return withExceptionHandler {
            (dao.getIdea(UUID.fromString(ideaId)) ?: throw IdeaNotFoundException())
                .apply {
                    skills = skillService.getIdeaSkills(ideaId)
                }
        }

    }

    fun deleteIdea(ideaId: String) {
        withExceptionHandler {
            dao.deleteIdea(UUID.fromString(ideaId)) ?: throw IdeaNotFoundException()
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createIdea(userId: String, input: IdeaInput): IdeaOutput {
        return withExceptionHandler {
            val idea = dao.createIdea(userId, input)

            val user = userDao.get(userId)
            idea.name = user?.name
            idea.profileImage = user?.profileImage

            return idea
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getUserIdeasList(userId: String, pageIndex: Int, pageSize: Int): List<IdeaOutput> {
        return withExceptionHandler {
            val ideas = dao.getUserIdeasList(userId, (pageIndex - 1) * pageSize, pageSize)

            if (ideas.isEmpty())
                userDao.get(userId) ?: throw UserNotFoundException()

            ideas.map { idea ->
                idea.apply {
                    this.skills = skillService.getIdeaSkills(this.ideaId.toString())
                }
            }
        }
    }

    fun getIdeaOwner(ideaId: String): String {
        return withExceptionHandler {
            dao.getIdeaOwner(UUID.fromString(ideaId)) ?: throw IdeaNotFoundException()
        }
    }
}