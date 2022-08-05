package ps.crossworking.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.crossworking.common.USER_PATH
import ps.crossworking.common.Uris
import ps.crossworking.dto.UserApiInput
import ps.crossworking.dto.UserApiOutputFull
import ps.crossworking.dto.UserApiUpdate
import ps.crossworking.dto.UserListApiOutput
import ps.crossworking.service.UserService

/**
 * Controller for user routes.
 */
@RestController
@RequestMapping(USER_PATH)
class UserController(val service: UserService) {

    @PostMapping
    fun createUser(
        @RequestBody user: UserApiInput
    ): ResponseEntity<UserApiOutputFull> {
        val userOutput = service.createUser(user.toInternal())

        return ResponseEntity
            .status(201)
            .contentType(MediaType.APPLICATION_JSON)
            .body(userOutput.toExternal())
    }

    @GetMapping
    fun getUsers() = UserListApiOutput(
        service.getUsers().map {
            it.toExternal()
        }
    )

    @GetMapping(Uris.User.SingleUser.PATH)
    fun getUser(
        @PathVariable("userId") userId: String
    ) = service.getUser(userId).toExternal()

    @PutMapping(Uris.User.SingleUser.PATH)
    fun updateUser(
        @PathVariable("userId") userId: String,
        @RequestBody body: UserApiUpdate
    ) = service.updateUser(userId, body.toInternal()).toExternal()

    @DeleteMapping(Uris.User.SingleUser.PATH)
    fun deleteUser(
        @PathVariable("userId") userId: String
    ) = ResponseEntity.status(204).body(service.deleteUser(userId))
}
