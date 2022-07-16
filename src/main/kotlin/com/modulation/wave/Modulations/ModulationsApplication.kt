package com.modulation.wave.Modulations

import com.modulation.wave.Modulations.bo.AmplitudeModulation
import com.modulation.wave.Modulations.bo.FrequencyModulation
import com.modulation.wave.Modulations.bo.Imagilization
import com.modulation.wave.Modulations.bo.SineWave
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File
import javax.imageio.ImageIO

@SpringBootApplication
class ModulationsApplication

fun main(args: Array<String>) {
	runApplication<ModulationsApplication>(*args)

/*	val delta = 0.004

	val wave1 = SineWave(300.0, 0.2, 0.01, 0.1)
	val wave2 = SineWave(200.0, 1.0, 0.1, delta)
	val modulation = FrequencyModulation(wave1, wave2, delta)
	val processor = Imagilization(1500.0, 500.0)

	val plots = mapOf(
		Pair("Wave1", wave1),
		Pair("Wave2", wave2),
		Pair("Modulation", modulation)
	)

	for (plot in plots.entries) {
		val image = processor.get(plot.value)

		ImageIO.write(image, "png", File("C:\\Users\\himag\\Documents\\Modulations\\${plot.key}.png"))

	}*/
}
