package ps.crossworking

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*
import ps.crossworking.common.IDEAS_PATH
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.Uris
import ps.crossworking.common.problemJsonMapper
import ps.crossworking.dto.CandidatureApiInput
import ps.crossworking.dto.CandidatureResultListApiOutput
import ps.crossworking.dto.CandidateListApiOutput
import ps.crossworking.exception.*
import ps.crossworking.testData.*

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Sql(scripts = ["classpath:/sql/insertTestValues.sql"])
class CandidateIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `get user candidature results`() {
        val expected = objectMapper.writeValueAsString(CandidatureResultListApiOutput(user1CandidatureResults))

        mvc.get(USER_PATH + Uris.Candidate.UserResults.makeSubPath(usersData[0].userId)).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(expected)
            }
        }
    }

    @Test
    fun `get idea candidates`() {
        val expected = objectMapper.writeValueAsString(CandidateListApiOutput(idea2CandidatureData))

        mvc.get(IDEAS_PATH + Uris.Candidate.IdeaCandidates.makeSubPath(ideasData[1].ideaId.toString())).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(expected)
            }
        }
    }

    @Test
    fun `apply to idea`() {
        val input = objectMapper.writeValueAsString(applyToIdeaInput)
        val expected = objectMapper.writeValueAsString(newPendingCandidature)

        mvc.post(
            USER_PATH + Uris.Candidate.UserCandidatures.makeSubPath(usersData[0].userId)
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(expected)
            }
        }
    }

    @Test
    fun `get idea candidature`() {
        val expected = objectMapper.writeValueAsString(newPendingCandidature)

        mvc.get(
            IDEAS_PATH +
                    Uris.Candidate.SingleCandidate.makeSubPath(ideasData[1].ideaId.toString(), usersData[0].userId)
        ).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(expected)
            }
        }
    }

    @Test
    fun `update candidature`() {
        val input = objectMapper.writeValueAsString(updateCandidature)
        val expected = objectMapper.writeValueAsString(acceptedNewCandidature)

        mvc.put(
            IDEAS_PATH +
                    Uris.Candidate.SingleCandidate.makeSubPath(ideasData[1].ideaId.toString(), usersData[0].userId)
        ) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(expected)
            }
        }
    }

    @Test
    fun `undo candidature`() {
        mvc.delete(
            USER_PATH +
                    Uris.Candidate.SingleUserCandidature.makeSubPath(usersData[0].userId, ideasData[1].ideaId.toString())
        ).andExpect {
            status { isNoContent() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    fun `apply to own idea`() {
        val input = objectMapper.writeValueAsString(CandidatureApiInput(ideasData[0].ideaId.toString()))

        mvc.post(USER_PATH + Uris.Candidate.UserCandidatures.makeSubPath(usersData[0].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[CandidateToSelfException::class]) }
        }
    }

    @Test
    fun `apply with non existent user`() {
        val input = objectMapper.writeValueAsString(CandidatureApiInput(ideasData[0].ideaId.toString()))

        mvc.post(USER_PATH + Uris.Candidate.UserCandidatures.makeSubPath("doesntExist")) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[CandidateSubjectNotFoundException::class]) }
        }
    }

    @Test
    fun `apply to non existent idea`() {
        val input = objectMapper.writeValueAsString(CandidatureApiInput("469d0d5a-0a18-4763-bc90-3aa735881b21"))

        mvc.post(USER_PATH + Uris.Candidate.UserCandidatures.makeSubPath(usersData[0].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[CandidateSubjectNotFoundException::class]) }
        }
    }

    @Test
    fun `candidate not found`() {
        mvc.get(IDEAS_PATH + Uris.Candidate.SingleCandidate.makeSubPath(ideasData[1].ideaId.toString(), "doesnt exist")).andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[CandidateNotFoundException::class]) }
        }
    }

    @Test
    fun `candidature already exists`() {
        val input = objectMapper.writeValueAsString(CandidatureApiInput(ideasData[1].ideaId.toString()))

        mvc.post(USER_PATH + Uris.Candidate.UserCandidatures.makeSubPath(usersData[0].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[CandidateAlreadyExistentException::class]) }
        }
    }
}