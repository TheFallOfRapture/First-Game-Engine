package com.morph.engine.math

class Matrix4f(
        m00: Float, m01: Float, m02: Float, m03: Float,
        m10: Float, m11: Float, m12: Float, m13: Float,
        m20: Float, m21: Float, m22: Float, m23: Float,
        m30: Float, m31: Float, m32: Float, m33: Float
) {
    val values: FloatArray = floatArrayOf(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
    )

    val transpose: Matrix4f
        get() = Matrix4f(
                values[0], values[4], values[8], values[12],
                values[1], values[5], values[9], values[13],
                values[2], values[6], values[10], values[14],
                values[3], values[7], values[11], values[15]
        )

    val inverse: Matrix4f
        get() {
            val matOfMinors = Matrix4f(
                    minorDet(0, 0), minorDet(1, 0), minorDet(2, 0), minorDet(3, 0),
                    minorDet(0, 1), minorDet(1, 1), minorDet(2, 1), minorDet(3, 1),
                    minorDet(0, 2), minorDet(1, 2), minorDet(2, 2), minorDet(3, 2),
                    minorDet(0, 3), minorDet(1, 3), minorDet(2, 3), minorDet(3, 3)
            )

            val sign = Matrix4f(
                    1f, -1f, 1f, -1f,
                    -1f, 1f, -1f, 1f,
                    1f, -1f, 1f, -1f,
                    -1f, 1f, -1f, 1f
            )

            val cofactor = matOfMinors.mulComp(sign)
            val adjugate = cofactor.transpose
            val invDet = 1f / determinant

            val inverse = adjugate * invDet

            return adjugate * invDet
        }

    val determinant: Float
        get() {
            var det = 0f

            for (i in 0..3) {
                val sign = if (i % 2 == 0) 1 else -1
                det += sign.toFloat() * values[i] * minorDet(i, 0)
            }

            return det
        }

    constructor(m: FloatArray) : this(
            m[0], m[1], m[2], m[3],
            m[4], m[5], m[6], m[7],
            m[8], m[9], m[10], m[11],
            m[12], m[13], m[14], m[15]
    )

    override fun toString(): String {
        val result = StringBuilder("Matrix4f(")
        for (i in 0..14) {
            result.append(values[i]).append(", ")
            if (i % 4 == 3)
                result.append("\n         ")
        }
        result.append(values[15]).append(")")

        return result.toString()
    }

    operator fun get(i: Int, j: Int): Float {
        return values[j + (i * 4)]
    }

    operator fun set(i: Int, j: Int, value: Float) {
        this.values[j + (i * 4)] = value
    }

    fun asTranslationVector(): Vector3f {
        return Vector3f(values[3], values[7], values[11])
    }

    fun as2DRotation(): Float {
        val cos = values[0]
        val sin = values[4]
        return Math.atan2(sin.toDouble(), cos.toDouble()).toFloat()
    }

    fun asScaleVector(): Vector3f {
        return Vector3f(values[0], values[5], values[10])
    }

    @JvmName("mul")
    operator fun times(other: Matrix4f): Matrix4f {
        val result = Matrix4f.empty()

        for (i in 0..3) {
            for (j in 0..3) {
                result[i, j] = this[i, 0] * other[0, j] +
                        this[i, 1] * other[1, j] +
                        this[i, 2] * other[2, j] +
                        this[i, 3] * other[3, j]
            }
        }

        return result
    }

    @JvmName("mul")
    operator fun times(v: Vector4f) = Vector4f(
            x = this[0, 0] * v.x + this[0, 1] * v.y + this[0, 2] * v.z + this[0, 3] * v.w,
            y = this[1, 0] * v.x + this[1, 1] * v.y + this[1, 2] * v.z + this[1, 3] * v.w,
            z = this[2, 0] * v.x + this[2, 1] * v.y + this[2, 2] * v.z + this[2, 3] * v.w,
            w = this[3, 0] * v.x + this[3, 1] * v.y + this[3, 2] * v.z + this[3, 3] * v.w
    )

    fun toArray(): FloatArray {
        return values
    }

    @JvmName("mul")
    operator fun times(v: Vector2f): Vector2f {
        return times(Vector4f(v, 0, 1)).xy
    }

    @JvmName("mul")
    operator fun times(v: Vector3f): Vector3f {
        return times(Vector4f(v, 1f)).xyz
    }

    private fun minor(i: Int, j: Int): Matrix3f {
        val values = FloatArray(9)
        var index = 0
        for (y in 0..3) {
            for (x in 0..3) {
                if (x != i && y != j) {
                    values[index] = get(x, y)
                    index++
                }
                if (index == 9) {
                    break
                }
            }
        }

        return Matrix3f(values)
    }

    private fun minorDet(i: Int, j: Int): Float {
        return minor(i, j).determinant
    }

    @JvmName("scale")
    operator fun times(k: Float): Matrix4f {
        val values = FloatArray(16)

        for (i in 0..15) {
            values[i] = this.values[i] * k
        }

        return Matrix4f(values)
    }

    private fun mulComp(mat: Matrix4f): Matrix4f {
        val values = FloatArray(16)

        for (i in 0..15) {
            values[i] = this.values[i] * mat[i]
        }

        return Matrix4f(values)
    }

    operator fun get(i: Int): Float {
        return values[i]
    }

    companion object {
        @JvmStatic fun identity(): Matrix4f {
            return Matrix4f(
                    1f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 1f
            )
        }

        @JvmStatic fun empty(): Matrix4f {
            return Matrix4f(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        }
    }
}
