package com.morph.engine.physics.components

import com.morph.engine.entities.Component
import com.morph.engine.math.Vector2f

class Velocity2D @JvmOverloads constructor(var velocity: Vector2f = Vector2f(0f, 0f)) : Component() {

    constructor(x: Float, y: Float) : this(Vector2f(x, y))

    fun addVelocity(velocity: Vector2f) {
        this.velocity += velocity
    }

    fun getTranslation(dt: Float): Vector2f {
        return velocity * dt
    }
}
