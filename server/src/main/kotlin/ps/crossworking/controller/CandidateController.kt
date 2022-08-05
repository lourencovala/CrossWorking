package ps.crossworking.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.crossworking.auth.UnsafeGet
import ps.crossworking.common.IDEAS_PATH
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.Uris
import ps.crossworking.dto.CandidateListApiOutput
import ps.crossworking.dto.CandidatureApiInput
import ps.crossworking.dto.CandidatureApiUpdate
import ps.crossworking.dto.CandidatureResultListApiOutput
import ps.crossworking.service.CandidateService

/**
 * Controller for candidature routes on user path.
 */
@RestController
@RequestMapping(USER_PATH)
class CandidateByUserController(val service: CandidateService) {

    @GetMapping(Uris.Candidate.UserResults.PATH)
    @UnsafeGet
    fun getUserCandidatureResults(
        @RequestParam(defaultValue = "1") pageIndex: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @PathVariable("userId") userId: String
    ) = CandidatureResultListApiOutput(
        service.getUserCandidatureResults(userId, pageIndex, pageSize).map {
            it.toExternal()
        }
    )

    @PostMapping(Uris.Candidate.UserCandidatures.PATH)
    fun applyToIdea(
        @PathVariable("userId") userId: String,
        @RequestBody body: CandidatureApiInput
    ) = ResponseEntity
        .status(201)
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.applyToIdea(userId, body.toInternal()).toExternal())

    @GetMapping(Uris.Candidate.SingleUserCandidature.PATH)
    @UnsafeGet
    fun getUserCandidature(
        @PathVariable("userId") userId: String,
        @PathVariable("ideaId") ideaId: String
    ) = service.getIdeaCandidature(ideaId, userId).toExternal()


    @DeleteMapping(Uris.Candidate.SingleUserCandidature.PATH)
    fun undoCandidature(
        @PathVariable("userId") userId: String,
        @PathVariable("ideaId") ideaId: String
    ) = ResponseEntity.status(204).body(service.undoCandidature(userId, ideaId))
}

/**
 * Controller for candidature routes on idea path.
 */
@RestController
@RequestMapping(IDEAS_PATH)
class CandidateByIdeaController(val service: CandidateService) {

    @GetMapping(Uris.Candidate.IdeaCandidates.PATH)
    @UnsafeGet
    fun getIdeaCandidates(
        @RequestParam(defaultValue = "1") pageIndex: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @PathVariable("ideaId") ideaId: String
    ) = CandidateListApiOutput(
        service.getIdeaCandidates(ideaId, pageIndex, pageSize).map {
            it.toExternal()
        }
    )

    @GetMapping(Uris.Candidate.SingleCandidate.PATH)
    @UnsafeGet
    fun getIdeaCandidature(
        @PathVariable("ideaId") ideaId: String,
        @PathVariable("candidateId") candidateId: String
    ) = service.getIdeaCandidature(ideaId, candidateId).toExternal()

    @PutMapping(Uris.Candidate.SingleCandidate.PATH)
    fun updateCandidate(
        @PathVariable("ideaId") ideaId: String,
        @PathVariable("candidateId") candidateId: String,
        @RequestBody body: CandidatureApiUpdate
    ) = service.updateCandidate(ideaId, candidateId, body.toInternal()).toExternal()
}