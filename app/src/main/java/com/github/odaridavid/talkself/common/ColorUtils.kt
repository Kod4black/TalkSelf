package com.github.odaridavid.talkself.common

import kotlin.math.roundToInt

object ColorUtils {
    fun getColor(red: Int, green: Int, blue: Int, alpha: Double): String {

        var originalColor = java.lang.String.format("#%02x%02x%02x", red, green, blue)

        val alphaFixed = (alpha * 255).roundToInt()
        var alphaHex = java.lang.Long.toHexString(alphaFixed.toLong())

        if (alphaHex.length == 1) {
            alphaHex = "0$alphaHex"
        }

        originalColor = originalColor.replace("#", "#$alphaHex")

        return originalColor
    }
}
