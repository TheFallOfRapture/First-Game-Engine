package com.morph.engine.physics.components

import com.morph.engine.entities.Component
import com.morph.engine.math.Matrix4f
import com.morph.engine.math.Quaternion

// TODO: Consider making Transform a sealed class, with two child classes Transform2D and Transform3D
abstract class Transform : Component() {
    val transformationMatrix: Matrix4f
        get() = translationMatrix * (rotationMatrix * scaleMatrix)

    abstract val translationMatrix: Matrix4f
    abstract val rotationMatrix: Matrix4f
    abstract val scaleMatrix: Matrix4f

    abstract fun translate(translation: Matrix4f)
    abstract fun rotate(rotation: Quaternion)
    abstract fun scale(scale: Matrix4f)

    fun transform(transform: Transform): Matrix4f {
        return transform.transformationMatrix * transformationMatrix
    }

    companion object {
        fun identity(): Transform {
            return object : Transform() {
                override val translationMatrix: Matrix4f
                    get() = Matrix4f.identity

                override val rotationMatrix: Matrix4f
                    get() = Matrix4f.identity

                override val scaleMatrix: Matrix4f
                    get() = Matrix4f.identity

                override fun translate(translation: Matrix4f) {}
                override fun rotate(rotation: Quaternion) {}
                override fun scale(scale: Matrix4f) {}
            }
        }
    }
}
