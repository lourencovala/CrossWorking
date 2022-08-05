package ps.crossworking.common

import com.google.common.collect.HashBasedTable
import org.jdbi.v3.core.JdbiException
import ps.crossworking.exception.*
import ps.crossworking.model.*
import java.sql.SQLException


/**
 * Mapping sql exceptions to domain specific exceptions
 */
val errorMap = HashBasedTable.create<String?, Any?, Exception?>().apply {
    this.put("08001", Any::class, ConnectionFailureException())

    this.put("noValue", UserOutputFull::class, UserNotFoundException())
    this.put("23505", UserOutputFull::class, UserAlreadyExistsException())

    this.put("noValue", IdeaOutput::class, IdeaNotFoundException())
    this.put("23505", IdeaOutput::class, IdeaAlreadyExistsException())

    this.put("23503", SkillOutput::class, SkillSubjectNotFoundException())
    this.put("23505", SkillOutput::class, SkillAlreadyExistsException())

    this.put("23503", CandidatureOutput::class, CandidateSubjectNotFoundException())
    this.put("23505", CandidatureOutput::class, CandidateAlreadyExistentException())
    this.put("noValue", CandidatureOutput::class, CandidateNotFoundException())

    this.put("noValue", CandidateOutput::class, CandidateNotFoundException())

    this.put("80000", Unit::class, SkillMaxReachedException())
    this.put("80001", CandidatureOutput::class, CandidateToSelfException())
}

/**
 * Helper function to convert default errors in domain specific errors
 */
inline fun <reified T> withExceptionHandler(func: () -> (T?)): T {
    try {
        return func() ?: throw errorMap["noValue", T::class]!!
    } catch (e: Exception) {
        when (e) {
            is JdbiException -> {
                val state = (e.cause as SQLException).sqlState
                throw errorMap[state, T::class] ?: UnknownException()
            }

            else -> throw e
        }
    }
}
