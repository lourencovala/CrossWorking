package ps.crossworking.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ps.crossworking.common.HOME_PATH


@RestController
@RequestMapping(HOME_PATH)
class HomeAppController {

    @GetMapping
    fun homeScreen() = "Crossworking APP"
}