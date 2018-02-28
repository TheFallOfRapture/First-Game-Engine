package com.morph.engine.core

import com.morph.engine.math.Matrix4f
import com.morph.engine.math.MatrixUtils
import com.morph.engine.math.Vector2f

interface Camera {
    val projectionMatrix: Matrix4f
    val transformationMatrix: Matrix4f

    object Identity : Camera {
        override val projectionMatrix: Matrix4f = Matrix4f.identity()
        override val transformationMatrix: Matrix4f = Matrix4f.identity()
    }
}

data class OrthoCam2D(
        var position: Vector2f,
        var width: Float,
        var height: Float
) : Camera {
    override val projectionMatrix: Matrix4f
        get() = MatrixUtils.getOrthographicProjectionMatrix(height / 2f, -height / 2f, -width / 2f, width / 2f, 1f, -1f)
    override val transformationMatrix: Matrix4f
        get() = position.asTranslationMatrix() * Vector2f(width, height).asScaleMatrix()
}