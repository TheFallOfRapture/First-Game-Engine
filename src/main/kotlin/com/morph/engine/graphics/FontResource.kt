package com.morph.engine.graphics

class FontResource(val font: LoadedFont) {
    private var count: Int = 0

    fun finalize() {
        font.destroy()
    }

    fun addReference() {
        count++
    }

    fun removeReference(): Boolean {
        count--
        return count == 0
    }
}
