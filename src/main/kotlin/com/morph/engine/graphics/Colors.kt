package com.morph.engine.graphics

data class Color @JvmOverloads constructor(var red: Float = 0f, var green: Float = 0f, var blue: Float = 0f, var alpha: Float = 1f) {
    override fun toString(): String {
        return "Color($red, $green, $blue, $alpha)"
    }

    fun scale(k: Float): Color {
        return Color(red * k, green * k, blue * k)
    }

    fun add(c: Color): Color {
        return Color(red + c.red, green + c.green, blue + c.blue)
    }

    fun clamp(): Color {
        val red = Math.max(0f, Math.min(1f, this.red))
        val green = Math.max(0f, Math.min(1f, this.green))
        val blue = Math.max(0f, Math.min(1f, this.blue))

        return Color(red, green, blue)
    }

    fun alphaBlend(dst: Color): Color {
        val red = this.red * alpha + dst.red * (1f - alpha)
        val green = this.green * alpha + dst.green * (1f - alpha)
        val blue = this.blue * alpha + dst.blue * (1f - alpha)

        return Color(red, green, blue, 1f)
    }

    fun setRGB(c: Color) {
        this.red = c.red
        this.green = c.green
        this.blue = c.blue
    }
}
object Colors {
    @JvmStatic fun fromARGBHex(argb: Int): Color {
        val alpha = (argb and -0x1000000 shr 24) / 256f
        val red = (argb and 0x00ff0000 shr 16) / 256f
        val green = (argb and 0x0000ff00 shr 8) / 256f
        val blue = (argb and 0x000000ff) / 256f
        return Color(red, green, blue, alpha)
    }

    @JvmStatic fun fromRGBHex(rgb: Int, alpha: Float): Color {
        val red = (rgb and 0xff0000 shr 16) / 256f
        val green = (rgb and 0x00ff00 shr 8) / 256f
        val blue = (rgb and 0x0000ff) / 256f
        return Color(red, green, blue, alpha)
    }

    @JvmStatic fun fromRGBHex(rgb: Int): Color {
        return fromRGBHex(rgb, 1f)
    }

    @JvmStatic fun Color.toARGBHex(): Int {
        val alpha = Math.floor((alpha * 0xff).toDouble()).toInt()
        val red = Math.floor((red * 0xff).toDouble()).toInt()
        val green = Math.floor((green * 0xff).toDouble()).toInt()
        val blue = Math.floor((blue * 0xff).toDouble()).toInt()

        return alpha shl 24 or (red shl 16) or (green shl 8) or blue
    }

    @JvmStatic fun Color.toRGBHex(): Int {
        val red = Math.floor((red * 0xff).toDouble()).toInt()
        val green = Math.floor((green * 0xff).toDouble()).toInt()
        val blue = Math.floor((blue * 0xff).toDouble()).toInt()

        return red shl 16 or (green shl 8) or blue
    }
}
