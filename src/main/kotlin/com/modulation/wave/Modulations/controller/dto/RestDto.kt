package com.modulation.wave.Modulations.controller.dto

class RestSineDto(
    var amplitude: Double,
    var angularMultiplier:Double,
    var angularOffset: Double,
    var angularStep: Double,
    var width: Int,
    var height: Int
    )

enum class Modulation(val fullName: String) {
    ampl("Amplitude"), freq("Frequency")
}
class RestModulationDto(
    var amplitude1: Double,
    var angularMultiplier1:Double,
    var angularOffset1: Double,
    var amplitude2: Double,
    var angularMultiplier2:Double,
    var angularOffset2: Double,
    var angularStep: Double,
    var type: Modulation,
    var width: Int,
    var height: Int
)