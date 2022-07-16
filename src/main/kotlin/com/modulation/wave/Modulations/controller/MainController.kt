package com.modulation.wave.Modulations.controller

import com.modulation.wave.Modulations.controller.dto.Modulation
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MainController {
    @RequestMapping(value = ["/"])
    fun home() = "home"

    @RequestMapping(value = ["/{type}"])
    fun index(@PathVariable type: Modulation, modelMap: ModelMap) : String {
        modelMap["type"] = type.name
        modelMap["name"] = type.fullName
        return "index"
    }
}