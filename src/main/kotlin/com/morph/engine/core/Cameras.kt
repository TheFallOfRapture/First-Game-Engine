package com.morph.engine.core

import com.morph.engine.entities.Entity
import com.morph.engine.math.*

interface Camera {
    val projectionMatrix: Matrix4f
    val transformationMatrix: Matrix4f
    val modelViewProjection: Matrix4f
        get() = projectionMatrix * transformationMatrix

    object Identity : Camera {
        override val projectionMatrix: Matrix4f = Matrix4f.identity
        override val transformationMatrix: Matrix4f = Matrix4f.identity
    }
}

data class OrthoCam2D(
        var position: Vector2f,
        var rotation: Float,
        var width: Float,
        var height: Float
) : Camera {
    override val projectionMatrix: Matrix4f
        get() = MatrixUtils.getOrthographicProjectionMatrix(height / 2f, -height / 2f, -width / 2f, width / 2f, 1f, -1f)
    override val transformationMatrix: Matrix4f
        get() = (-position).asTranslationMatrix() * Quaternion(Vector3f(0f, 0f, 1f), rotation).asRotationMatrix()

    fun linkPosition(e: Entity) {

    }
}

data class PerspectiveCam(
        var position: Vector3f,
        var orientation: Quaternion,
        var width: Float,
        var height: Float
) : Camera {
    override val projectionMatrix: Matrix4f
        get() = MatrixUtils.getPerspectiveProjectionMatrix(height / 2f, -height / 2f, -width / 2f, width / 2f, -1f, 1f)
    override val transformationMatrix: Matrix4f
        get() = (-position).asTranslationMatrix() * orientation.asRotationMatrix()
}
