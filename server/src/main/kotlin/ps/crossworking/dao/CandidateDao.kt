package ps.crossworking.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import ps.crossworking.model.*
import java.util.*

interface CandidateDao {

    @SqlQuery("select c.status, date_part('day', current_date::timestamp - c.createddate::timestamp) as daysSinceCreatedDate, date_part('day', current_date::timestamp - c.lastupdatedate::timestamp) as daysSinceLastUpdate, i.ideaId, i.title from candidate c join idea i on c.ideaid = i.ideaid where c.userid=:userId order by createddate desc limit :limit offset :offset")
    fun getUserCandidatureResults(
        @Bind("userId") userId: String,
        @Bind("offset") pageIndex: Int,
        @Bind("limit") pageSize: Int
    ): List<CandidatureResultOutput>

    @SqlQuery("select u.userId, name, email, profileimage, status, date_part('day', current_date::timestamp - c.createddate::timestamp) as daysSinceCreatedDate, date_part('day', current_date::timestamp - c.lastupdatedate::timestamp) as daysSinceLastUpdate from Candidate as c join \"User\" as u on c.userId = u.userId where ideaId = :ideaId and (c.status = 'pending' or c.status = 'accepted') order by createddate desc limit :limit offset :offset")
    fun getIdeaCandidates(
        @Bind("ideaId") ideaId: UUID,
        @Bind("offset") pageIndex: Int,
        @Bind("limit") pageSize: Int
    ): List<CandidateOutput>

    @SqlUpdate("insert into Candidate(userId, ideaId) values (:userId,:ideaId) returning status, date_part('day', current_date::timestamp - createddate::timestamp) as daysSinceCreatedDate, date_part('day', current_date::timestamp - lastupdatedate::timestamp) as daysSinceLastUpdate")
    @GetGeneratedKeys
    fun applyToIdea(@Bind("userId") userId: String, @BindBean input: CandidatureInput): CandidatureOutput

    @SqlQuery("select status, date_part('day', current_date::timestamp - createddate::timestamp) as daysSinceCreatedDate, date_part('day', current_date::timestamp - lastupdatedate::timestamp) as daysSinceLastUpdate from candidate where ideaid = :ideaId and userid = :candidateId")
    fun getIdeaCandidature(@Bind("ideaId") ideaId: UUID, @Bind("candidateId") candidateId: String): CandidatureOutput

    @SqlQuery("select u.userId, name, email, profileimage, status, date_part('day', current_date::timestamp - c.createddate::timestamp) as daysSinceCreatedDate, date_part('day', current_date::timestamp - c.lastupdatedate::timestamp) as daysSinceLastUpdate from Candidate as c join \"User\" as u on c.userId = u.userId where ideaId = :ideaId and u.userid = :userId")
    fun getIdeaCandidate(@Bind("ideaId") ideaId: UUID, @Bind("userId") candidateId: String): CandidateOutput

    @SqlUpdate("update Candidate set status = :status, lastupdatedate = current_date where ideaId = :ideaId and userId = :candidateId")
    fun updateCandidate(
        @Bind("ideaId") ideaId: UUID,
        @Bind("candidateId") candidateId: String,
        @BindBean update: CandidatureUpdate
    )

    @SqlUpdate("delete from Candidate where ideaId = :ideaId and userId = :candidateId and status = 'pending' returning userid")
    @GetGeneratedKeys
    fun undoCandidature(@Bind("candidateId") userId: String, @Bind("ideaId") ideaId: UUID): String?
}