package ps.crossworking.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ps.crossworking.common.withExceptionHandler
import ps.crossworking.dao.CandidateDao
import ps.crossworking.dao.IdeaDao
import ps.crossworking.dao.UserDao
import ps.crossworking.exception.CandidateNotFoundException
import ps.crossworking.exception.IdeaNotFoundException
import ps.crossworking.exception.UserNotFoundException
import ps.crossworking.model.*
import java.util.*

/**
 * Business logic for Candidatures.
 */
@Component
class CandidateService(val candidateDao: CandidateDao, val ideaDao: IdeaDao, val userDao: UserDao) {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getUserCandidatureResults(userId: String, pageIndex: Int, pageSize: Int): List<CandidatureResultOutput> {
        return withExceptionHandler {
            val list = candidateDao.getUserCandidatureResults(userId, (pageIndex - 1) * pageSize, pageSize)

            if (list.isEmpty())
                userDao.get(userId) ?: throw UserNotFoundException()

            list
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getIdeaCandidates(ideaId: String, pageIndex: Int, pageSize: Int): List<CandidateOutput> {
        return withExceptionHandler {
            val list = candidateDao.getIdeaCandidates(UUID.fromString(ideaId), (pageIndex - 1) * pageSize, pageSize)

            if (list.isEmpty())
                ideaDao.getIdea(UUID.fromString(ideaId)) ?: throw IdeaNotFoundException()

            list
        }
    }

    fun applyToIdea(userId: String, input: CandidatureInput): CandidatureOutput {
        return withExceptionHandler {
            candidateDao.applyToIdea(userId, input)
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun getIdeaCandidature(ideaId: String, candidateId: String): CandidatureOutput {
        return withExceptionHandler {
            candidateDao.getIdeaCandidature(UUID.fromString(ideaId), candidateId)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateCandidate(ideaId: String, candidateId: String, update: CandidatureUpdate): CandidateOutput {
        return withExceptionHandler {
            candidateDao.updateCandidate(UUID.fromString(ideaId), candidateId, update)

            candidateDao.getIdeaCandidate(UUID.fromString(ideaId), candidateId)
        }
    }

    fun undoCandidature(userId: String, ideaId: String) {
        withExceptionHandler {
            candidateDao.undoCandidature(userId, UUID.fromString(ideaId)) ?: throw CandidateNotFoundException()
        }
    }
}