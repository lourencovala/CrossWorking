package ps.crossworking.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.crossworking.common.IDEAS_PATH
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.Uris
import ps.crossworking.dto.IdeaApiInput
import ps.crossworking.dto.IdeaApiUpdate
import ps.crossworking.dto.IdeaListApiOutput
import ps.crossworking.service.IdeaService

/**
 * Controller for idea routes on the idea path.
 */
@RestController
@RequestMapping(IDEAS_PATH)
class IdeaController(val service: IdeaService) {

    @GetMapping
    fun getIdeasList(
        @RequestParam(defaultValue = "1") pageIndex: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ) = IdeaListApiOutput(
        service.getIdeaList(pageIndex, pageSize).map {
            it.toExternalShort()
        }
    )

    @GetMapping(Uris.Idea.SingleIdea.PATH)
    fun getIdea(
        @PathVariable("ideaId") ideaId: String
    ) = service.getIdea(ideaId).toExternalFull()


    @PutMapping(Uris.Idea.SingleIdea.PATH)
    fun updateIdea(
        @PathVariable("ideaId") ideaId: String,
        @RequestBody body: IdeaApiUpdate
    ) = service.updateIdea(ideaId, body.toInternal()).toExternalFull()


    @DeleteMapping(Uris.Idea.SingleIdea.PATH)
    fun deleteIdea(
        @PathVariable("ideaId") ideaId: String
    ) = ResponseEntity.status(204).body(service.deleteIdea(ideaId))
}


/**
 * Controller for idea routes on the user path.
 */
@RestController
@RequestMapping(USER_PATH)
class UserIdeaController(val service: IdeaService) {

    @PostMapping(Uris.Idea.UserIdeas.PATH)
    fun createIdea(
        @PathVariable("userId") userId: String,
        @RequestBody body: IdeaApiInput
    ) = ResponseEntity
        .status(201)
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.createIdea(userId, body.toInternal()))

    @GetMapping(Uris.Idea.UserIdeas.PATH)
    fun getUserIdeasList(
        @RequestParam(defaultValue = "1") pageIndex: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @PathVariable("userId") userId: String
    ) = IdeaListApiOutput(
        service.getUserIdeasList(userId, pageIndex, pageSize).map {
            it.toExternalShort()
        }
    )
}