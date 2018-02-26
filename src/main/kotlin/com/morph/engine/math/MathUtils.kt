package com.morph.engine.math

object MathUtils {
    fun <T : Comparable<T>> clamp(value: T, min: T, max: T): T = when {
        value < min -> min
        value > max -> max
        else -> value
    }
}
