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
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.Uris
import ps.crossworking.common.problemJsonMapper
import ps.crossworking.dao.UserDao
import ps.crossworking.dto.UserListApiOutput
import ps.crossworking.exception.UserAlreadyExistsException
import ps.crossworking.exception.UserNotFoundException
import ps.crossworking.testData.*

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Sql(scripts = ["classpath:/sql/insertTestValues.sql"])
class UserIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `get users`() {
        val obj = objectMapper.writeValueAsString(UserListApiOutput(usersData))

        mvc.get(USER_PATH) {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(obj)
            }
        }
    }

    @Test
    fun `get user`() {
        val obj = objectMapper.writeValueAsString(usersData[0])

        mvc.get(USER_PATH + Uris.User.SingleUser.makeSubPath(usersData[0].userId)) {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(obj)
            }
        }
    }

    @Test
    fun `create user`() {
        val objCreate = objectMapper.writeValueAsString(userCreate)
        val objResult = objectMapper.writeValueAsString(userCreateResult)

        mvc.post(USER_PATH) {
            contentType = MediaType.APPLICATION_JSON
            content = objCreate
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(objResult)
            }
        }
    }

    @Test
    fun `update user`() {
        val objToUpdate = objectMapper.writeValueAsString(userUpdate)
        val result = objectMapper.writeValueAsString(updateResult)

        mvc.put(USER_PATH + Uris.User.SingleUser.makeSubPath(usersData[2].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = objToUpdate
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(result)
            }
        }
    }

    @Test
    fun `delete user`() {
        mvc.delete(USER_PATH + Uris.User.SingleUser.makeSubPath(usersData[1].userId)).andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun `user not found`() {
        mvc.get(USER_PATH + Uris.User.SingleUser.makeSubPath("nonexistent")).andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[UserNotFoundException::class]) }
        }
    }

    @Test
    fun `user already exists`() {
        val userToCreate = objectMapper.writeValueAsString(alreadyExistentUserInput)

        mvc.post(USER_PATH) {
            contentType = MediaType.APPLICATION_JSON
            content = userToCreate
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[UserAlreadyExistsException::class]) }
        }
    }
}