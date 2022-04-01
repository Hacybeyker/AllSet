package com.hacybeyker.allset.view.extractcolor

import androidx.core.graphics.*
import androidx.palette.graphics.Palette

/**
 * Created by Carlos Osorio on 29/07/2021
 */

object ConvertColor {

    fun colorToRgbString(color: Int): String {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    fun hslToRgbString(hsl: FloatArray?): String {
        hsl?.let {
            val color = ColorUtils.HSLToColor(it)
            val red: Int = color.red
            val green: Int = color.green
            val blue: Int = color.blue
            val alpha: Int = color.alpha
            return String.format("#%02x%02x%02x", red, green, blue)
        }
        return "none"
    }

    fun paletteSwatchToString(item: Palette.Swatch?): String {
        item?.let {
            return """
                ${hslToRgbString(it.hsl)}
                ${it.titleTextColor}
            """.trimIndent()
        }
        return ""
    }
}