package ps.crossworking.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ps.crossworking.common.withExceptionHandler
import ps.crossworking.dao.UserDao
import ps.crossworking.exception.UserNotFoundException
import ps.crossworking.model.UserInput
import ps.crossworking.model.UserOutputFull
import ps.crossworking.model.UserOutputShort
import ps.crossworking.model.UserUpdate

/**
 * Business logic for Users.
 */
@Component
class UserService(val dao: UserDao, val skillService: SkillService) {

    fun createUser(userInput: UserInput): UserOutputFull {
        return withExceptionHandler {
            dao.createUser(userInput)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getUsers(): List<UserOutputShort> {
        return withExceptionHandler {
            val users = dao.getAll()
            users.forEach { it.skills = skillService.getUserSkills(it.userId) }
            users
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getUser(userId: String): UserOutputFull {
        return withExceptionHandler {
            (dao.get(userId) ?: throw UserNotFoundException()).apply {
                this.skills = skillService.getUserSkills(userId)
            }
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateUser(userId: String, update: UserUpdate): UserOutputFull {
        return withExceptionHandler {
            (dao.updateUser(userId, update) ?: throw  UserNotFoundException()).apply {
                this.skills = skillService.getUserSkills(userId)
            }
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun deleteUser(userId: String) {
        withExceptionHandler {
            dao.deleteUser(userId)
        }
    }
}