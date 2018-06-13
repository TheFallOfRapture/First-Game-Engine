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

    val determinant: Float
        get() {
            val (reduction, swaps) = this.computeLUDecomposition()
            val power = (sequenceOf(1) + generateSequence { -1 }).take(swaps + 1).reduce {a, b -> a * b}
            return power * reduction[0, 0] * reduction[1, 1] * reduction[2, 2]
        }

    @Deprecated("Extremely slow O(n!) algorithm") val oldDeterminant: Float
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

    val inverse: Matrix3f
        get() {
            var result = Matrix3f(this.values)
            var inverse = Matrix3f.identity()

            fun swap(a: Int, b: Int) {
                result = result.swapRows(a, b)
                inverse = inverse.swapRows(a, b)
                println("Swapped rows $a and $b, result: $result, inverse: $inverse")
            }

            fun scale(a: Int, k: Float) {
                result = result.scaleRow(a, k)
                inverse = inverse.scaleRow(a, k)
                println("Scaled row $a by $k, result: $result, inverse: $inverse")
            }

            fun addScaled(a: Int, b: Int, k: Float) {
                result = result.addScaledRow(a, k, b)
                inverse = inverse.addScaledRow(a, k, b)
                println("Row operation on row $a using row $b by $k, result: $result, inverse: $inverse")
            }

            var row = 0
            var col = 0
            while (row <= 2 && col <= 2) {
                val max = (row..2).maxBy { Math.abs(result[it, col]) }!!
                if (result[max, col] == 0f) {
                    col++
                } else {
                    swap(row, max)
                    for (i in (row + 1)..2) {
                        val k = result[i, col] / result[row, col]
                        addScaled(i, row, -k)
                    }

                    row++
                    col++
                }
            }

            if (result[2, 2] != 0f) {
                addScaled(1, 2, -result[1, 2] / result[2, 2])
            }

            if (result[1, 1] != 0f) {
                addScaled(0, 1, -result[0, 1] / result[1, 1])
            }

            if (result[2, 2] != 0f) {
                addScaled(0, 2, -result[0, 2] / result[2, 2])
            }

            if (result[0, 0] != 0f) scale(0,  1 / result[0, 0])
            if (result[1, 1] != 0f) scale(1, 1 / result[1, 1])
            if (result[2, 2] != 0f) scale(2, 1 / result[2, 2])

            return inverse
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

    operator fun set(i: Int, j: Int, value: Float) {
        this.values[j + (i * 3)] = value
    }

    fun swapRows(a: Int, b: Int) : Matrix3f {
        require(a < 3 && b < 3) { "Attempt to swap rows outside 0-2" }
        val rows = values.asList().chunked(3).toMutableList()
        val temp = rows[a]
        rows[a] = rows[b]
        rows[b] = temp
        return Matrix3f(rows.flatten().toFloatArray())
    }

    fun scaleRow(a: Int, k: Float) : Matrix3f {
        val result = Matrix3f(this.values)
        result[a, 0] *= k
        result[a, 1] *= k
        result[a, 2] *= k
        return result
    }

    fun addScaledRow(unscaledRow: Int, scaleFactor: Float, scaledRow: Int) : Matrix3f {
        val rowAdded = Vector3f(this[scaledRow, 0], this[scaledRow, 1], this[scaledRow, 2]) * scaleFactor
        val rowKept = Vector3f(this[unscaledRow, 0], this[unscaledRow, 1], this[unscaledRow, 2])
        val newRow = rowAdded + rowKept
        val result = Matrix3f(this.values)
        result[unscaledRow, 0] = newRow.x
        result[unscaledRow, 1] = newRow.y
        result[unscaledRow, 2] = newRow.z
        return result
    }

    fun computeLUDecomposition() : Pair<Matrix3f, Int> {
        var result = this
        val pivot1 = sequenceOf(0, 1, 2).filter { result[it, 0] != 0f }.min()
        var swaps : Int = 0

        pivot1?.let {
            if (it != 0) {
                result = result.swapRows(it, 0)
                swaps++
            }
            for (i in 1..2) {
                if (result[i, 0] != 0f) {
                    result = result.addScaledRow(i, -result[i, 0] / result[0, 0], 0)
                }
            }
        }

        val pivot2 = sequenceOf(1, 2).filter { result[it, 1] != 0f }.min()
        pivot2?.let {
            if (it != 1) {
                result = result.swapRows(it, 1)
                swaps++
            }
            if (result[2, 1] != 0f) {
                result = result.addScaledRow(2, -result[2, 1] / result[1, 1], 1)
            }
        }

        return result to swaps
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
