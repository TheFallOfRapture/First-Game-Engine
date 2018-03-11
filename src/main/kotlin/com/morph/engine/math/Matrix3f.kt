package com.morph.engine.math

class Matrix3f(
        m00: Float, m01: Float, m02: Float,
        m10: Float, m11: Float, m12: Float,
        m20: Float, m21: Float, m22: Float
) {
    private val values: FloatArray = floatArrayOf(
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22
    )

    // TODO: BROKEN!
    //		float det = 0;
    //
    //		for (int i = 0; i < 3; i++) {
    //			int sign = i % 2 == 0 ? 1 : -1;
    //			det += sign * m[i] * minorDet(i, 0);
    //		}
    val determinant: Float
        get() {
            var det = 0f

            for (sign in 0..1) {
                for (diagonal in 0..2) {
                    var d = 1f
                    for (value in 0..2) {
                        d *= if (sign == 0) {
                            get((value + diagonal) % 3, value % 3)
                        } else {
                            get(2 - (value + diagonal) % 3, value % 3)
                        }
                    }

                    if (sign == 0) {
                        det += d
                    } else {
                        det -= d
                    }
                }
            }

            return det
        }

    //		Matrix3f matOfMinors = new Matrix3f(
    //				getMatrix2f(4, 5, 7, 8).getDeterminant(), getMatrix2f(3, 5, 6, 8).getDeterminant(), getMatrix2f(3, 4, 6, 7).getDeterminant(),
    //				getMatrix2f(1, 2, 7, 8).getDeterminant(), getMatrix2f(0, 2, 6, 8).getDeterminant(), getMatrix2f(0, 1, 6, 7).getDeterminant(),
    //				getMatrix2f(1, 2, 4, 5).getDeterminant(), getMatrix2f(0, 2, 3, 5).getDeterminant(), getMatrix2f(0, 1, 3, 4).getDeterminant()
    //		);
    val inverse: Matrix3f
        get() {

            val matOfMinors = Matrix3f(
                    minorDet(0, 0), minorDet(1, 0), minorDet(2, 0),
                    minorDet(0, 1), minorDet(1, 1), minorDet(2, 1),
                    minorDet(0, 2), minorDet(1, 2), minorDet(2, 2)
            )

            val sign = Matrix3f(
                    1f, -1f, 1f,
                    -1f, 1f, -1f,
                    1f, -1f, 1f
            )

            val cofactor = matOfMinors.mulComp(sign)
            val adjugate = cofactor.transpose
            val invDet = 1f / determinant

            return adjugate * (invDet)
        }

    constructor(m: FloatArray) : this(m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8])

    private val transpose: Matrix3f
        get() = Matrix3f(
                values[0], values[3], values[6],
                values[1], values[4], values[7],
                values[2], values[5], values[8]
        )

    override fun toString(): String {
        val result = StringBuilder("Matrix3f(")

        for (i in 0..7) {
            result.append(values[i]).append(", ")
            if (i % 3 == 2)
                result.append("\n")
        }

        return result.toString() + values[8] + ")"
    }

    @JvmName("scale")
    operator fun times(k: Float): Matrix3f {
        val values = FloatArray(9)
        for (i in 0..8) {
            values[i] = this.values[i] * k
        }

        return Matrix3f(values)
    }

    fun mulComp(mat: Matrix3f): Matrix3f {
        val values = FloatArray(9)
        for (i in 0..8) {
            values[i] = this.values[i] * mat[i]
        }

        return Matrix3f(values)
    }

    operator fun get(i: Int): Float {
        return values[i]
    }

    operator fun get(i: Int, j: Int): Float {
        return values[j + i * 3]
    }

    fun getMatrix2f(a: Int, b: Int, c: Int, d: Int): Matrix2f {
        return Matrix2f(values[a], values[b], values[c], values[d])
    }

    private fun minor(i: Int, j: Int): Matrix2f {
        val values = FloatArray(4)
        var index = 0
        for (y in 0..2) {
            for (x in 0..2) {
                if (x != i && y != j) {
                    values[index] = get(x, y)
                    index++
                }
                if (index == 4) {
                    break
                }
            }
        }

        return Matrix2f(values)
    }

    private fun minorDet(i: Int, j: Int): Float {
        return minor(i, j).determinant
    }

    companion object {
        @JvmStatic fun identity() = Matrix3f(
                1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f
        )

        @JvmStatic fun empty() = Matrix3f(
                0f, 0f, 0f,
                0f, 0f, 0f,
                0f, 0f, 0f
        )
    }
}
