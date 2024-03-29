package com.modulation.wave.Modulations.bo

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.awt.geom.Rectangle2D
import java.awt.image.BufferedImage

class Imagilization(val width: Double, val height: Double) {
    fun get(plots: Plots): BufferedImage {
        val image = BufferedImage(width.toInt(), height.toInt(), BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()

        graphics.renderingHints[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        graphics.renderingHints[RenderingHints.KEY_ANTIALIASING] = RenderingHints.VALUE_ANTIALIAS_ON
        graphics.renderingHints[RenderingHints.KEY_TEXT_ANTIALIASING] = RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        graphics.renderingHints[RenderingHints.KEY_COLOR_RENDERING] = RenderingHints.VALUE_COLOR_RENDER_QUALITY
        graphics.renderingHints[RenderingHints.KEY_STROKE_CONTROL] = RenderingHints.VALUE_STROKE_PURE
        graphics.renderingHints[RenderingHints.KEY_DITHERING] = RenderingHints.VALUE_DITHER_ENABLE

        graphics.color = Color.BLUE
        graphics.stroke = BasicStroke(3.0f)
        val path = plots.calculate(width, height)
        graphics.draw(path)

        graphics.stroke = BasicStroke(5.0f)
        val boundary = Rectangle2D.Double(10.0, 10.0, width - 20, height - 20)
        graphics.draw(boundary)

        graphics.stroke = BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 0.0f, FloatArray(1) { 9.0f }, 0f)

        graphics.drawLine(10, height.toInt()/2, width.toInt()-10, height.toInt()/2)

        graphics.color = Color.RED
        graphics.font = graphics.font.deriveFont(Font.BOLD)

        graphics.drawString(plots.maxHeight().toString(), 20, 30)

        graphics.drawString("-" + plots.maxHeight().toString(), 20, height.toInt() - 20)

        return image

    }
}