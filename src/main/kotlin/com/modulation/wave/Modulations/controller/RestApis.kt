package com.modulation.wave.Modulations.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.modulation.wave.Modulations.bo.AmplitudeModulation
import com.modulation.wave.Modulations.bo.FrequencyModulation
import com.modulation.wave.Modulations.bo.Imagilization
import com.modulation.wave.Modulations.bo.SineWave
import com.modulation.wave.Modulations.controller.dto.*
import org.springframework.stereotype.Controller
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.imageio.ImageIO


@Controller
@ResponseBody
@RequestMapping("/img")
class RestApis {
    @GetMapping(value = ["/sine.png"], produces = [MimeTypeUtils.IMAGE_PNG_VALUE])
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

    @GetMapping(value = ["/modulation.png"], produces = [MimeTypeUtils.IMAGE_PNG_VALUE])
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

    @CrossOrigin(origins = ["http://localhost", "https://wave-modulation.herokuapp.com/"])
    @PostMapping(value = ["download.zip"], produces = [MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE])
    fun download(restModulationDto: RestModulationDownloadDto): ByteArray {
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

        val processor = Imagilization(10000.0, 750.0)

        val plots = mapOf(
            Pair("SineWave1", sineWave1),
            Pair("SineWave2", sineWave2),
            Pair(restModulationDto.type.fullName+"Modulation", modulation)
        )

        val stream = ByteArrayOutputStream()
        val out = ZipOutputStream(stream)


        for(plot in plots.entries) {
            val e = ZipEntry(plot.key+".png")
            out.putNextEntry(e)
            val image = processor.get(plot.value)
            val outImg = ByteArrayOutputStream()
            ImageIO.write(image, "png", outImg)
            val data = outImg.toByteArray()
            out.write(data, 0, data.size)
            out.closeEntry()
        }

        val om = ObjectMapper(YAMLFactory())
        val obj = mapOf(
            Pair(
                "sineWave1",
                SineWave(
                    restModulationDto.amplitude1,
                    restModulationDto.angularMultiplier1,
                    restModulationDto.angularOffset1,
                    restModulationDto.angularStep1
                )
            ),
            Pair(
                "sineWave2",
                SineWave(
                    restModulationDto.amplitude2,
                    restModulationDto.angularMultiplier2,
                    restModulationDto.angularOffset2,
                    restModulationDto.angularStep2
                )
            ),
            Pair(
                "modulation",
                mapOf(
                    Pair(
                        "type",
                        restModulationDto.type.fullName
                    ),
                    Pair(
                        "angularStep",
                        restModulationDto.angularStep
                    )
                )
            )
        )

        val outYml = ByteArrayOutputStream()

        om.writeValue(outYml, obj)

        val e = ZipEntry("details.yml")
        out.putNextEntry(e)
        val data = outYml.toByteArray()
        out.write(data, 0, data.size)
        out.closeEntry()

        out.close()

        return stream.toByteArray()

    }

    @PostMapping(value = ["/upload"], produces = [MimeTypeUtils.APPLICATION_JSON_VALUE])
    fun upload(@RequestParam file:MultipartFile): RestModulationDownloadDto {
        val input = BufferedInputStream(file.inputStream)
        val om = ObjectMapper(YAMLFactory())

        val obj = om.readValue(input.readBytes(), YmlDto::class.java)

        return RestModulationDownloadDto(
            obj.sineWave1.amplitude,
            obj.sineWave1.angularMultiplier,
            obj.sineWave1.angularOffset,
            obj.sineWave1.angularStep,
            obj.sineWave2.amplitude,
            obj.sineWave2.angularMultiplier,
            obj.sineWave2.angularOffset,
            obj.sineWave2.angularStep,
            obj.modulation.angularStep,
            if(obj.modulation.type.equals("Amplitude")) Modulation.ampl else Modulation.freq,
            0,
            0
        )

    }
}