package ps.crossworking.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.crossworking.common.IDEAS_PATH
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.Uris
import ps.crossworking.dto.SkillApiInput
import ps.crossworking.dto.SkillListApiOutput
import ps.crossworking.service.SkillService

/**
 * Controller for skill routes on the user path.
 */
@RestController
@RequestMapping(USER_PATH)
class UserSkillsController(val service: SkillService) {

    @PostMapping(Uris.Skills.UserSkills.PATH)
    fun addUserSkill(
        @PathVariable("userId") userId: String,
        @RequestBody body: SkillApiInput
    ) = ResponseEntity
        .status(201)
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.addUserSkill(userId, body.toInternal()).toExternal())

    @GetMapping(Uris.Skills.UserSkills.PATH)
    fun getUserSkills(
        @PathVariable("userId") userId: String
    ) = SkillListApiOutput(
        service.getUserSkills(userId).map {
            it.toExternal()
        }
    )

    @GetMapping(Uris.Skills.SingleUserSkill.PATH)
    fun getUserSkill(
        @PathVariable("userId") userId: String,
        @PathVariable("skillId") skillId: String
    ) = service.getUserSkill(userId, skillId).toExternal()

    @DeleteMapping(Uris.Skills.SingleUserSkill.PATH)
    fun deleteUserSkill(
        @PathVariable("userId") userId: String,
        @PathVariable("skillId") skillId: String
    ) = ResponseEntity.status(204).body(service.deleteUserSkill(userId, skillId))
}

/**
 * Controller for skill routes on the idea path.
 */
@RestController
@RequestMapping(IDEAS_PATH)
class IdeaSkillsController(val service: SkillService) {

    @PostMapping(Uris.Skills.IdeaSkills.PATH)
    fun addIdeaSkill(
        @PathVariable("ideaId") ideaId: String,
        @RequestBody body: SkillApiInput
    ) = ResponseEntity
        .status(201)
        .contentType(MediaType.APPLICATION_JSON)
        .body(service.addIdeaSkill(ideaId, body.toInternal()).toExternal())

    @GetMapping(Uris.Skills.IdeaSkills.PATH)
    fun getIdeaSkills(
        @PathVariable("ideaId") ideaId: String
    ) = SkillListApiOutput(
        service.getIdeaSkills(ideaId).map {
            it.toExternal()
        }
    )

    @GetMapping(Uris.Skills.SingleIdeaSkill.PATH)
    fun getIdeaSkill(
        @PathVariable("ideaId") ideaId: String,
        @PathVariable("skillId") skillId: String
    ) = service.getIdeaSkill(ideaId, skillId).toExternal()

    @DeleteMapping(Uris.Skills.SingleIdeaSkill.PATH)
    fun deleteIdeaSkill(
        @PathVariable("ideaId") ideaId: String,
        @PathVariable("skillId") skillId: String
    ) = ResponseEntity.status(204).body(service.deleteIdeaSkill(ideaId, skillId))
}
