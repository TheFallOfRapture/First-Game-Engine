package com.morph.engine.math

object MatrixUtils {
    @JvmStatic fun getOrthographicProjectionMatrix(top: Float, bottom: Float, left: Float, right: Float, near: Float, far: Float): Matrix4f {
        return Matrix4f(
                2f / (right - left), 0f, 0f, -((right + left) / (right - left)),
                0f, 2f / (top - bottom), 0f, -((top + bottom) / (top - bottom)),
                0f, 0f, -2f / (far - near), -((far + near) / (far - near)),
                0f, 0f, 0f, 1f
        )
    }

    @JvmStatic fun getPerspectiveProjectionMatrix(top: Float, bottom: Float, left: Float, right: Float, near: Float, far: Float): Matrix4f {
        return Matrix4f(
                (2f * near) / (right - left), 0f, (right + left) / (right - left), 0f,
                0f, (2 * near) / (top - bottom), (top + bottom) / (top - bottom), 0f,
                0f, 0f, -(far + near) / (far - near), (-2f * far * near) / (far - near),
                0f, 0f, -1f, 0f
        )
    }
}
