package com.morph.engine.graphics

import java.util.*

/**
 * Created on 7/31/2017.
 */
object FontManager {
    private val loadedFonts = HashMap<String, FontResource>()

    fun loadFont(font: String) = loadedFonts[font] ?: FontResource(LoadedFont(font)).apply { loadedFonts[font] = this }

    fun unloadFont(font: String) = loadedFonts[font]?.let {
        if (it.removeReference()) loadedFonts.remove(font)
    }
}
