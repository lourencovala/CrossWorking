package ps.crossworking

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ps.crossworking.common.CATEGORIES_PATH
import ps.crossworking.common.Uris
import ps.crossworking.dto.CategoryListApiOutput
import ps.crossworking.testData.categoriesData

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Sql(scripts = ["classpath:/sql/insertTestValues.sql"])
class CategoryIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `get categories`() {
        val expected = objectMapper.writeValueAsString(CategoryListApiOutput(categoriesData))

        mvc.get(CATEGORIES_PATH).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(expected) }
        }
    }
}