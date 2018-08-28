package com.morph.engine.physics.components

import com.morph.engine.math.Matrix4f
import com.morph.engine.math.Quaternion
import com.morph.engine.math.Vector3f

data class Transform3D @JvmOverloads constructor(var position: Vector3f = Vector3f(0f, 0f ,0f), var orientation: Quaternion = Quaternion(Vector3f(0f, 1f, 0f), 0f), var scale: Vector3f = Vector3f(1f, 1f, 1f)) : Transform() {
    override val translationMatrix: Matrix4f
        get() = position.asTranslationMatrix()

    override val rotationMatrix: Matrix4f
        get() = orientation.asRotationMatrix()

    override val scaleMatrix: Matrix4f
        get() = scale.asScaleMatrix()

    override fun translate(translation: Matrix4f) {
        position += translation.asTranslationVector()
    }

    override fun rotate(rotation: Quaternion) {
        orientation *= rotation
    }

    override fun scale(scale: Matrix4f) {
        this.scale += scale.asScaleVector()
    }

    fun translate(translation: Vector3f) {
        position += translation
    }

    fun scale(scaling: Vector3f) {
        scale += scaling
    }
}
