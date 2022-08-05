package ps.crossworking.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import ps.crossworking.model.IdeaInput
import ps.crossworking.model.IdeaOutput
import ps.crossworking.model.IdeaUpdate
import java.util.*

interface IdeaDao {

    @SqlQuery("select ideaId, title, smallDescription, description, date_part('day', current_date::timestamp - date::timestamp) as days, u.userId, u.name, u.profileImage from (idea join \"User\" as u on idea.userid=u.userid) order by date desc limit :limit offset :offset")
    fun getIdeasList(@Bind("offset") pageIndex: Int, @Bind("limit") pageSize: Int): List<IdeaOutput>

    @SqlQuery("select ideaId, title, smallDescription, description, date_part('day', current_date::timestamp - date::timestamp) as days, u.userId, u.name, u.profileImage from idea join \"User\" as u on idea.userid=u.userid where ideaid=:ideaId")
    fun getIdea(@Bind("ideaId") ideaId: UUID): IdeaOutput?

    @SqlUpdate(
        "update idea set title = coalesce(:title, title), " +
                "smallDescription = coalesce(:smallDescription, smallDescription), " +
                "description = coalesce(:description, description) where ideaId = :ideaId"
    )
    fun updateIdea(
        @Bind("ideaId") ideaId: UUID?,
        @BindBean current: IdeaUpdate
    )

    @SqlUpdate("delete from idea where ideaId = :ideaId returning ideaid")
    @GetGeneratedKeys
    fun deleteIdea(@Bind("ideaId") ideaId: UUID?): UUID?

    @SqlUpdate("insert into idea(userId, title, smallDescription, description) values(:userId, :title, :smallDescription, :description) returning ideaid, title, smallDescription, description, date_part('day', current_date::timestamp - date::timestamp) as days, userid")
    @GetGeneratedKeys
    fun createIdea(@Bind("userId") userId: String, @BindBean input: IdeaInput): IdeaOutput

    @SqlQuery("select ideaId, title, smallDescription, description, date_part('day', current_date::timestamp - date::timestamp) as days, u.userId, u.name, u.profileImage from idea join \"User\" as u on idea.userid=u.userid  where u.userid=:userId order by date desc limit :limit offset :offset")
    fun getUserIdeasList(
        @Bind("userId") userId: String,
        @Bind("offset") pageIndex: Int,
        @Bind("limit") pageSize: Int
    ): List<IdeaOutput>

    @SqlQuery("select userid from idea where ideaid = :ideaId")
    fun getIdeaOwner(@Bind("ideaId") ideaId: UUID?): String?
}