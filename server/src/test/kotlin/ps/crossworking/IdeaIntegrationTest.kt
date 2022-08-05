package ps.crossworking

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Order
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
import ps.crossworking.dao.IdeaDao
import ps.crossworking.dto.IdeaListApiOutput
import ps.crossworking.exception.IdeaAlreadyExistsException
import ps.crossworking.exception.IdeaNotFoundException
import ps.crossworking.testData.*

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Sql(scripts = ["classpath:/sql/insertTestValues.sql"])
class IdeaIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var ideaDao: IdeaDao

    @Test
    fun `get ideas`() {
        val obj = objectMapper.writeValueAsString(IdeaListApiOutput(ideasData))

        mvc.get(IDEAS_PATH).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(obj)
            }
        }
    }

    @Test
    fun `get idea`() {
        val obj = objectMapper.writeValueAsString(idea1Full)

        mvc.get(IDEAS_PATH + Uris.Idea.SingleIdea.makeSubPath(idea1Full.ideaId.toString())).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(obj)
            }
        }
    }

    @Test
    fun `create idea`() {
        val obj = objectMapper.writeValueAsString(ideaToCreate)

        mvc.post(USER_PATH + Uris.Idea.UserIdeas.makeSubPath(usersData[0].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = obj
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        val ideasList = ideaDao.getIdeasList(1, 10)
        val ideaCreated = ideasList[ideasList.size - 1]
        assert(ideaCreated.title == ideaToCreate.title)
        assert(ideaCreated.smallDescription == ideaToCreate.smallDescription)
    }

    @Test
    fun `delete idea`() {
        mvc.delete(IDEAS_PATH + Uris.Idea.SingleIdea.makeSubPath(ideasData[2].ideaId.toString())) {

        }.andExpect {
            status { isNoContent() }
        }

        val result = ideaDao.getIdea(ideasData[2].ideaId)
        assert(result == null)
    }

    @Test
    fun `update idea`() {
        val objUpdate = objectMapper.writeValueAsString(ideaToUpdate)

        mvc.put(IDEAS_PATH + Uris.Idea.SingleIdea.makeSubPath(ideasData[1].ideaId.toString())) {
            contentType = MediaType.APPLICATION_JSON
            content = objUpdate
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        val ideaResult = ideaDao.getIdea(ideasData[1].ideaId)
        assert(ideaResult?.title == ideaToUpdate.title)
    }

    @Test
    fun `get user ideas`() {
        val result = objectMapper.writeValueAsString(IdeaListApiOutput(listOf(ideasData[0])))

        mvc.get(USER_PATH + Uris.Idea.UserIdeas.makeSubPath(usersData[0].userId)).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(result)
            }
        }
    }

    @Test
    fun `idea not found`() {
        mvc.get(IDEAS_PATH + Uris.Idea.SingleIdea.makeSubPath("469d0d5a-0a18-4763-bc90-3aa735881b21")).andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[IdeaNotFoundException::class]) }
        }
    }

    @Test
    fun `idea already exists`() {
        val input = objectMapper.writeValueAsString(alreadyExistentIdeaInput)

        mvc.post(USER_PATH + Uris.Idea.UserIdeas.makeSubPath(usersData[0].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[IdeaAlreadyExistsException::class]) }
        }
    }
}