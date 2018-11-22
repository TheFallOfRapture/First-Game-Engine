package com.morph.demos.test.physics

import com.morph.engine.collision.CollisionSolver
import com.morph.engine.math.Vector2f

class DefaultCollisionSolver : CollisionSolver() {
    override fun collisionResponse(normal: Vector2f, vel: Vector2f): Vector2f = normal * (normal dot vel) * -1f
}