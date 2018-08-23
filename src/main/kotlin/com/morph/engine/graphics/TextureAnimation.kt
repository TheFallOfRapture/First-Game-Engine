package com.morph.engine.graphics

import com.morph.engine.entities.Component
import com.morph.engine.graphics.components.RenderData

/**
 * Created by Fernando on 1/18/2017.
 */
class TextureAnimation(private val data: RenderData, private var delay: Float, vararg files: String) : Component() {
    private val frames: Array<Texture> = files.map { Texture(it) }.toTypedArray()
    private var accumulator: Float = delay
    private var counter: Int = 0
    private val numFrames: Int = files.size

    fun update(dt: Float) {
        if (accumulator >= delay) {
            data.setTexture(frames[counter], 0)

            accumulator = 0f
            counter++
        }

        if (counter >= numFrames) {
            counter = 0
        }

        accumulator += dt
    }
}
