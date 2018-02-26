package com.morph.engine.math

import java.util.function.Function

class Vector2f {
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()

    val length: Float
        get() = Math.sqrt((x * x + y * y).toDouble()).toFloat()

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    /**
     * Clones a Vector2f.
     */
    constructor(v: Vector2f) {
        this.x = v.x
        this.y = v.y
    }

    constructor() {
        this.x = 0f
        this.y = 0f
    }

    constructor(x: Double, y: Double) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    override fun toString(): String {
        return "Vector2f($x, $y)"
    }

    override fun equals(o: Any?): Boolean {
        if (o is Vector2f) {
            val v = o as Vector2f?
            val epsilon = 1e-8f

            return Math.abs(x - v!!.x) < epsilon && Math.abs(y - v.y) < epsilon
        }

        return false
    }

    fun add(v: Vector2f): Vector2f {
        return Vector2f(x + v.x, y + v.y)
    }

    fun sub(v: Vector2f): Vector2f {
        return Vector2f(x - v.x, y - v.y)
    }

    fun mul(v: Vector2f): Vector2f {
        return Vector2f(x * v.x, y * v.y)
    }

    operator fun div(v: Vector2f): Vector2f {
        return Vector2f(x / v.x, y / v.y)
    }

    fun scale(k: Float): Vector2f {
        return Vector2f(x * k, y * k)
    }

    fun invScale(k: Float): Vector2f {
        return Vector2f(x / k, y / k)
    }

    fun normalize(): Vector2f {
        val length = length
        return this.invScale(length)
    }

    fun abs(): Vector2f {
        return Vector2f(Math.abs(x), Math.abs(y))
    }

    operator fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun set(v: Vector2f) {
        this.x = v.x
        this.y = v.y
    }

    fun asTranslationMatrix(): Matrix4f {
        return Matrix4f(
                1f, 0f, 0f, x,
                0f, 1f, 0f, y,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        )
    }

    fun asScaleMatrix(): Matrix4f {
        return Matrix4f(
                x, 0f, 0f, 0f,
                0f, y, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
        )
    }

    fun cross(v: Vector2f): Float {
        return x * v.y - y * v.x
    }

    fun clear() {
        this.x = 0f
        this.y = 0f
    }

    fun negate(): Vector2f {
        return Vector2f(-x, -y)
    }

    fun dot(v: Vector2f): Float {
        return x * v.x + y * v.y
    }

    fun map(`fun`: Function<Float, Float>): Vector2f {
        return Vector2f(`fun`.apply(x), `fun`.apply(y))
    }

    companion object {

        fun reflect(n: Vector2f, v: Vector2f): Vector2f {
            return v.add(n.scale(-2 * v.dot(n)))
        }
    }
}
