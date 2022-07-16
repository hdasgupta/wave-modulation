package com.modulation.wave.Modulations.controller

import com.modulation.wave.Modulations.bo.AmplitudeModulation
import com.modulation.wave.Modulations.bo.FrequencyModulation
import com.modulation.wave.Modulations.bo.Imagilization
import com.modulation.wave.Modulations.bo.SineWave
import com.modulation.wave.Modulations.controller.dto.Modulation
import com.modulation.wave.Modulations.controller.dto.RestModulationDto
import com.modulation.wave.Modulations.controller.dto.RestSineDto
import org.springframework.stereotype.Controller
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Controller
@ResponseBody
@RequestMapping("/img")
class RestApis {
    @RequestMapping(value = ["/sine.png"], produces = [MimeTypeUtils.IMAGE_PNG_VALUE])
    fun sine(restSineDto: RestSineDto) : ByteArray {
        val sineWave = SineWave(
            restSineDto.amplitude,
            restSineDto.angularMultiplier,
            restSineDto.angularOffset,
            restSineDto.angularStep
        )
        val processor = Imagilization(restSineDto.width.toDouble(), restSineDto.height.toDouble())
        val image = processor.get(sineWave)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "png", out)

        return out.toByteArray()
    }

    @RequestMapping(value = ["/modulation.png"], produces = [MimeTypeUtils.IMAGE_PNG_VALUE])
    fun modulation(restModulationDto: RestModulationDto) : ByteArray {
        val sineWave1 = SineWave(
            restModulationDto.amplitude1,
            restModulationDto.angularMultiplier1,
            restModulationDto.angularOffset1,
            restModulationDto.angularStep
        )
        val sineWave2 = SineWave(
            restModulationDto.amplitude2,
            restModulationDto.angularMultiplier2,
            restModulationDto.angularOffset2,
            restModulationDto.angularStep
        )

        val modulation = when (restModulationDto.type) {
            Modulation.ampl->AmplitudeModulation(sineWave1, sineWave2, restModulationDto.angularStep)
            else -> FrequencyModulation(sineWave1, sineWave2, restModulationDto.angularStep)
        }

        val processor = Imagilization(restModulationDto.width.toDouble(), restModulationDto.height.toDouble())
        val image = processor.get(modulation)
        val out = ByteArrayOutputStream()
        ImageIO.write(image, "png", out)

        return out.toByteArray()
    }
}