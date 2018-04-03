package com.morph.engine.graphics

import org.lwjgl.opengl.GL20.glDeleteProgram

class TextureResource(val id: Int) {
    private var count: Int = 0

    fun finalize() {
        glDeleteProgram(id)
    }

    fun addReference() {
        count++
    }

    fun removeReference(): Boolean {
        count--
        return count == 0
    }
}
