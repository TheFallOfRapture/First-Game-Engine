package com.morph.engine.math

class Matrix2f(
        m00: Float, m01: Float,
        m10: Float, m11: Float
) {
    private val values: FloatArray = floatArrayOf(m00, m01, m10, m11)

    val determinant: Float
        get() = values[0] * values[3] - values[1] * values[2]

    val inverse: Matrix2f
        get() {
            val invDet = 1f / determinant
            return this * (invDet)
        }

    val adjugate: Matrix2f
        get() = Matrix2f(
                values[3], values[1],
                values[2], values[0]
        )

    constructor(m: FloatArray) : this(m[0], m[1], m[2], m[3])

    @JvmName("scale")
    operator fun times(k: Float): Matrix2f {
        return Matrix2f(values[0] * k, values[1] * k, values[2] * k, values[3] * k)
    }

    operator fun get(i: Int, j: Int): Float {
        return values[i + j * 2]
    }

    companion object {
        @JvmStatic fun identity() = Matrix2f(
                1f, 0f,
                0f, 1f
        )

        @JvmStatic fun empty() = Matrix2f(0f, 0f, 0f, 0f)
    }
}
