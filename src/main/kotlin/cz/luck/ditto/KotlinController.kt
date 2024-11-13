package cz.luck.ditto

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kotlin")
class KotlinController {

    @GetMapping("/test")
    fun getTestMessage(): String {
        return "Hello from kotlin side of the world!"
    }
}
