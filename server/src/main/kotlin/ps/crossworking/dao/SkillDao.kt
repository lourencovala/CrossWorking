package ps.crossworking.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import ps.crossworking.model.SkillInput
import ps.crossworking.model.SkillOutput
import java.util.*

interface SkillDao {

    @SqlQuery("select skillId, skillName as name, skillAbout as about, categoryName from add_user_skill(:userId, :name, :about, :categoryId)")
    fun addUserSkill(@Bind("userId") userId: String, @BindBean input: SkillInput): SkillOutput?

    @SqlQuery("select s.skillId, s.name, us.about, c.name as categoryName from userskill as us join skill as s on us.skillId=s.skillId join category as c on s.categoryId=c.categoryId where userid = :userId")
    fun getUserSkills(@Bind("userId") userId: String): List<SkillOutput>

    @SqlQuery("select s.skillId, s.name, us.about, c.name as categoryName from userskill as us join skill as s on us.skillId=s.skillId join category as c on s.categoryId=c.categoryId where userid = :userId and s.skillId = :skillId")
    fun getUserSkill(@Bind("userId") userId: String, @Bind("skillId") skillId: UUID): SkillOutput?

    @SqlUpdate("delete from userskill where userid = :userId and skillid = :skillId returning skillid")
    @GetGeneratedKeys
    fun deleteUserSkill(@Bind("userId") userId: String, @Bind("skillId") skillId: UUID): UUID?

    @SqlQuery("select skillId, skillName as name, skillAbout as about, categoryName from add_idea_skill(:ideaId, :name, :about, :categoryId)")
    fun addIdeaSkill(@Bind("ideaId") ideaId: UUID, @BindBean input: SkillInput): SkillOutput?

    @SqlQuery("select s.skillId, s.name, isl.about, c.name as categoryName from ideaskill as isl join skill as s on isl.skillId=s.skillId join category as c on s.categoryId=c.categoryId where ideaId=:ideaId")
    fun getIdeaSkills(@Bind("ideaId") ideaId: UUID): List<SkillOutput>

    @SqlQuery("select s.skillId, s.name, isl.about, c.name as categoryName from ideaskill as isl join skill as s on isl.skillId=s.skillId join category as c on s.categoryId=c.categoryId where ideaId=:ideaId and isl.skillId=:skillId")
    fun getIdeaSkill(@Bind("ideaId") ideaId: UUID, @Bind("skillId") skillId: UUID): SkillOutput?

    @SqlUpdate("delete from ideaskill where ideaId = :ideaId and skillid = :skillId returning ideaid")
    @GetGeneratedKeys
    fun deleteIdeaSkill(@Bind("ideaId") ideaId: UUID, @Bind("skillId") skillId: UUID): UUID?
}
