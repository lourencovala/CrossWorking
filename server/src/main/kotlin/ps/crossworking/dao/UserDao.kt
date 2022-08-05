package ps.crossworking.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import ps.crossworking.model.UserInput
import ps.crossworking.model.UserOutputFull
import ps.crossworking.model.UserOutputShort
import ps.crossworking.model.UserUpdate

interface UserDao {

    @GetGeneratedKeys
    @SqlUpdate("insert into \"User\" (userId, email) values(:userId, :email)")
    fun createUser(@BindBean user: UserInput): UserOutputFull

    @SqlQuery("select userid, name, email, profileImage from \"User\"")
    fun getAll(): List<UserOutputShort>

    @SqlQuery("select * from \"User\" where userId = :userId")
    fun get(@Bind userId: String): UserOutputFull?

    @GetGeneratedKeys
    @SqlUpdate("update \"User\" set name = coalesce(:name, name), about = coalesce(:about, about), profileImage = coalesce(:profileImage, profileImage) where userId = :userId")
    fun updateUser(
        @Bind("userId") userId: String,
        @BindBean update: UserUpdate
    ): UserOutputFull?

    @SqlUpdate("delete from \"User\" where userid=:userId returning userid")
    @GetGeneratedKeys
    fun deleteUser(
        @Bind("userId") userId: String
    ): String?
}