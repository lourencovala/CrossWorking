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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ps.crossworking.common.IDEAS_PATH
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.Uris
import ps.crossworking.common.problemJsonMapper
import ps.crossworking.dao.SkillDao
import ps.crossworking.dto.SkillListApiOutput
import ps.crossworking.exception.SkillAlreadyExistsException
import ps.crossworking.exception.SkillSubjectNotFoundException
import ps.crossworking.testData.*

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.properties"])
@Sql(scripts = ["classpath:/sql/insertTestValues.sql"])
class SkillIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var skillDao: SkillDao

    /**
     * User skills
     */
    @Test
    fun `get user skills`() {
        val expected = objectMapper.writeValueAsString(SkillListApiOutput(usersData[0].skills))

        mvc.get(USER_PATH + Uris.Skills.UserSkills.makeSubPath(usersData[0].userId))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(expected)
                }
            }
    }

    @Test
    fun `get user skill`() {
        val expected = objectMapper.writeValueAsString(usersData[0].skills[0])

        mvc.get(
            USER_PATH + Uris.Skills.SingleUserSkill.makeSubPath(
                usersData[0].userId,
                usersData[0].skills[0].skillId.toString()
            )
        ).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(expected)
            }
        }
    }

    @Test
    fun `add user skills`() {
        val body = objectMapper.writeValueAsString(skillInput)

        mvc.post(USER_PATH + Uris.Skills.UserSkills.makeSubPath(usersData[1].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        val result = skillDao.getUserSkills(usersData[1].userId)

        assert(skillInput.name == result[result.size - 1].name)
    }

    @Test
    fun `delete user skills`() {
        mvc.delete(
            USER_PATH + Uris.Skills.SingleUserSkill.makeSubPath(
                usersData[0].userId,
                usersData[0].skills[2].skillId.toString()
            )
        ).andExpect {
            status { isNoContent() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    /**
     * Idea skills
     */
    @Test
    fun `get idea skills`() {
        val expected = objectMapper.writeValueAsString(SkillListApiOutput(ideasData[0].skills))

        mvc.get(IDEAS_PATH + Uris.Skills.IdeaSkills.makeSubPath(ideasData[0].ideaId.toString()))
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content {
                    json(expected)
                }
            }
    }

    @Test
    fun `get idea skill`() {
        val expected = objectMapper.writeValueAsString(ideasData[1].skills[0])

        mvc.get(
            IDEAS_PATH + Uris.Skills.SingleIdeaSkill.makeSubPath(
                ideasData[1].ideaId.toString(),
                ideasData[1].skills[0].skillId.toString()
            )
        ).andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content {
                json(expected)
            }
        }
    }

    @Test
    fun `add idea skill`() {
        val body = objectMapper.writeValueAsString(skillInput)

        mvc.post(IDEAS_PATH + Uris.Skills.IdeaSkills.makeSubPath(ideasData[2].ideaId.toString())) {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }

        val result = skillDao.getIdeaSkills(ideasData[2].ideaId)

        assert(skillInput.name == result[result.size - 1].name)
    }

    @Test
    @Order(5)
    fun `delete idea skill`() {
        mvc.delete(
            IDEAS_PATH + Uris.Skills.SingleUserSkill.makeSubPath(
                ideasData[1].ideaId.toString(),
                ideasData[1].skills[0].skillId.toString()
            )
        ).andExpect {
            status { isNoContent() }
            content { contentType(MediaType.APPLICATION_JSON) }
        }
    }

    @Test
    fun `skill not found`() {
        mvc.get(USER_PATH + Uris.Skills.SingleUserSkill.makeSubPath(usersData[0].userId, "469d0d5a-0a18-4763-bc90-3aa735881b21")).andExpect {
            status { isNotFound() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[SkillSubjectNotFoundException::class]) }
        }
    }

    @Test
    fun `already existent skill`() {
        val input = objectMapper.writeValueAsString(alreadyExistentUserSkillInput)

        mvc.post(USER_PATH + Uris.Skills.UserSkills.makeSubPath(usersData[0].userId)) {
            contentType = MediaType.APPLICATION_JSON
            content = input
        }.andExpect {
            status { isConflict() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            content { objectMapper.writeValueAsString(problemJsonMapper[SkillAlreadyExistsException::class]) }
        }
    }
}