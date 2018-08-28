package com.morph.engine.physics.components

import com.morph.engine.math.Matrix4f
import com.morph.engine.math.Quaternion
import com.morph.engine.math.Vector2f

data class Transform2D @JvmOverloads constructor(var position: Vector2f = Vector2f(0f, 0f), var orientation: Float = 0f, var scale: Vector2f = Vector2f(1f, 1f)) : Transform() {

    override val translationMatrix: Matrix4f
        get() = position.asTranslationMatrix()

    override val rotationMatrix: Matrix4f
        get() {
            val cos = Math.cos(orientation.toDouble()).toFloat()
            val sin = Math.sin(orientation.toDouble()).toFloat()

            return Matrix4f(
                    cos, -sin, 0f, 0f,
                    sin, cos, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 1f
            )
        }

    override val scaleMatrix: Matrix4f
        get() = scale.asScaleMatrix()

    override fun translate(translation: Matrix4f) {
        position += translation.asTranslationVector().xy
    }

    override fun rotate(rotation: Quaternion) {
        orientation += rotation.toAxisAngle().w
    }

    override fun scale(scaling: Matrix4f) {
        scale += scaling.asScaleVector().xy
    }

    fun translate(translation: Vector2f) {
        position += translation
    }

    fun scale(scaling: Vector2f) {
        scale += scaling
    }
}
