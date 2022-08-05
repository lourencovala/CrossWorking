package ps.crossworking.mockRepositories

import ps.crossworking.model.*
import ps.crossworking.repository.ICandidatureRepository
import ps.crossworking.testCandidate
import ps.crossworking.testCandidature
import java.lang.Exception
import kotlin.collections.HashMap

class CandidatureRepositoryMock: ICandidatureRepository {

    val mem = HashMap<String, List<Candidate>>()

    private var excep: Exception? = null

    fun startWithDate(ideaId: String) {
        mem[ideaId] = listOf(testCandidate)
    }

    fun prepareException(exception: Exception) {
        excep = exception
    }


    override suspend fun applyToIdea(userId: String, ideaId: String): Candidature {
        if (excep != null)
            throw excep as Exception

        val user = mem[ideaId]!!.find {
            it.user.userId == userId
        }

        val newUser = Candidate(user!!.user, "Accepted", user.daysSinceCreatedDate, user.daysSinceLastUpdate)
        mem[ideaId] = mem[ideaId]!!.map { if(it.user.userId == userId) newUser else it }
        return Candidature(newUser.status, newUser.daysSinceCreatedDate, newUser.daysSinceLastUpdate)
    }

    override suspend fun getResults(userId: String, pageIndex: Int, pageSize: Int): List<CandidatureResult> {
        if (excep != null)
            throw excep as Exception
        return listOf(testCandidature)
    }

    override suspend fun getIdeaCandidates(ideaId: String, pageIndex: Int, pageSize: Int): List<Candidate> {
        if (excep != null)
            throw excep as Exception
        return mem[ideaId]!!
    }

    override suspend fun updateCandidate(
        ideaId: String,
        candidateId: String,
        isAccepted: Boolean
    ): Candidate {
        if (excep != null)
            throw excep as Exception
        val candidate = mem[ideaId]!!.find {it.user.userId == candidateId }
        val newCandidate = Candidate(candidate!!.user,"accepted", candidate.daysSinceCreatedDate, 0 )
        mem[ideaId] = mem[ideaId]!!.map {
            if (it.user.userId == candidateId)
                newCandidate
            else
                it
        }
        return newCandidate
    }

    override suspend fun getCandidatureStatus(userId: String, ideaId: String): Candidature? {
        if (excep != null)
            throw excep as Exception
        val candidate = mem[ideaId]!!.find {it.user.userId == userId }!!
        return Candidature(candidate.status, 14, 10)
    }

    override suspend fun undoApply(userId: String, ideaId: String) {
        if (excep != null)
            throw excep as Exception

        val user = mem[ideaId]!!.find {
            it.user.userId == userId
        }

        val newUser = Candidate(user!!.user, "Apply", user.daysSinceCreatedDate, user.daysSinceLastUpdate)
        mem[ideaId] = mem[ideaId]!!.map { if(it.user.userId == userId) newUser else it }
    }
}