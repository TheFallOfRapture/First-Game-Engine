package com.morph.engine.core

import com.morph.engine.math.Matrix4f
import com.morph.engine.math.Vector2f

interface Camera {
    val projectionMatrix: Matrix4f
}

data class OrthoCam2D(
        val position: Vector2f,
        val width: Float,
        val height: Float
) : Camera {
    override val projectionMatrix: Matrix4f
        get() = position.asTranslationMatrix() * Vector2f(width, height).asScaleMatrix()
}