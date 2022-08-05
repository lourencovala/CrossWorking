package ps.crossworking

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ps.crossworking.testData.userCreate

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
class GlobalIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `unsupported media type`() {
        val obj = objectMapper.writeValueAsString(userCreate)

        mvc.post("/api/users") {
            content = obj
        }.andExpect {
            status { isUnsupportedMediaType() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
        }
    }

    @Test
    fun `not acceptable`() {
        mvc.get("/api/users") {
            accept = MediaType.TEXT_MARKDOWN
        }.andExpect {
            status { isNotAcceptable() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
        }
    }
}