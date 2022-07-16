package com.modulation.wave.Modulations.bo

import java.awt.geom.Path2D
import kotlin.math.sin

open abstract class Plots(val angularStep: Double) {
    abstract fun calculate(width: Double, height: Double): Path2D.Double
}

class SineWave(val amplitude: Double, val angularMultiplier:Double,  val angularOffset: Double, angularStep: Double) : Plots(angularStep) {
    override fun calculate(width: Double, height: Double): Path2D.Double {
        val values = Path2D.Double()
        val AMPLITUDE_FRACTION = (height - 30)/amplitude
        val X_MIN = 10.0
        val X_MAX = width - 10
        var waveAngleStart = angularOffset

        var x = X_MIN

        values.moveTo(x, height/2)

        while (x<=X_MAX) {

            val sin = amplitude * sin(waveAngleStart)
            val amp = sin/2 * AMPLITUDE_FRACTION + height/2

            values.lineTo(x, amp)

            waveAngleStart += angularStep * angularMultiplier
            x++
        }

        return values
    }

}

open abstract class Modulation(val wave1: SineWave, val wave2: SineWave, angularStep: Double) : Plots(angularStep){

}

class AmplitudeModulation(wave1: SineWave, wave2: SineWave, angularStep: Double) : Modulation(wave1, wave2, angularStep) {
    override fun calculate(width: Double, height: Double): Path2D.Double {
        val values = Path2D.Double()
        val totalAmp = wave1.amplitude + wave2.amplitude
        val AMPLITUDE_FRACTION = (height - 30)/totalAmp
        val X_MIN = 10.0
        val X_MAX = width - 10
        var wave1AngleStart = wave1.angularOffset
        var wave2AngleStart = wave2.angularOffset

        var x = X_MIN

        values.moveTo(x, height/2)

        while (x<=X_MAX) {

            val sin1 = wave1.amplitude * sin(wave1AngleStart)
            val sin2 = wave2.amplitude * sin(wave2AngleStart)
            val amp = (sin1 + sin2)/2 * AMPLITUDE_FRACTION + height/2

            values.lineTo(x, amp)

            wave1AngleStart += angularStep * wave1.angularMultiplier
            wave2AngleStart += angularStep * wave2.angularMultiplier
            x++
        }

        return values

    }


}

class FrequencyModulation(wave1: SineWave, wave2: SineWave, angularStep: Double) : Modulation(wave1, wave2, angularStep) {
    override fun calculate(width: Double, height: Double): Path2D.Double {
        val values = Path2D.Double()
        val totalAmp = wave1.amplitude + wave2.amplitude
        val AMPLITUDE_FRACTION = (height - 30)/totalAmp
        val X_MIN = 10.0
        val X_MAX = width - 10
        var wave1AngleStart = wave1.angularOffset
        var wave2AngleStart = wave2.angularOffset

        var x = X_MIN

        values.moveTo(x, height/2)

        while (x<=X_MAX) {

            val sin2 = wave2.amplitude * sin(wave2AngleStart)
            val sin1 = wave1.amplitude * sin(wave1AngleStart * sin2)
            val amp = (sin1-10) * AMPLITUDE_FRACTION + height/2 + 5

            values.lineTo(x, amp)

            wave1AngleStart += angularStep * wave1.angularMultiplier
            wave2AngleStart += angularStep * wave2.angularMultiplier
            x++
        }

        return values

    }
}

