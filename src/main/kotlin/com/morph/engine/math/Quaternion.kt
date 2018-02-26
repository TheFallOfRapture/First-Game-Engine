package com.morph.engine.math

class Quaternion {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()
    var z: Float = 0.toFloat()
    var w: Float = 0.toFloat()

    val length: Float
        get() = Math.sqrt((x * x + y * y + z * z + w * w).toDouble()).toFloat()

    constructor(x: Float, y: Float, z: Float, w: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    constructor(axis: Vector3f, angle: Float) {
        val cos = Math.cos((angle / 2f).toDouble()).toFloat()
        val sin = Math.sin((angle / 2f).toDouble()).toFloat()

        this.x = axis.x * sin
        this.y = axis.y * sin
        this.z = axis.z * sin
        this.w = cos
    }

    fun mul(other: Quaternion): Quaternion {
        val newW = w * other.w - x * other.x - y * other.y - z * other.z
        val newX = w * other.x + x * other.w + y * other.z - z * other.y
        val newY = w * other.y - x * other.z + y * other.w + z * other.x
        val newZ = w * other.z + x * other.y - y * other.x + z * other.w

        return Quaternion(newX, newY, newZ, newW)
    }

    fun asRotationMatrix(): Matrix4f {
        val m1 = Matrix4f(
                w, z, -y, x,
                -z, w, x, y,
                y, -x, w, z,
                -x, -y, -z, w
        )

        val m2 = Matrix4f(
                w, z, -y, -x,
                -z, w, x, -y,
                y, -x, w, -z,
                x, y, z, w
        )

        return m1 * m2
    }

    fun toAxisAngle(): Vector4f {
        val axisX = (x / Math.sqrt((1f - w * w).toDouble())).toFloat()
        val axisY = (y / Math.sqrt((1f - w * w).toDouble())).toFloat()
        val axisZ = (z / Math.sqrt((1f - w * w).toDouble())).toFloat()
        val angle = (2 * Math.acos(w.toDouble())).toFloat()

        return Vector4f(axisX, axisY, axisZ, angle)
    }

    fun normalize(): Quaternion {
        val length = length
        val x = this.x / length
        val y = this.y / length
        val z = this.z / length
        val w = this.w / length

        return Quaternion(x, y, z, w)
    }
}
