package com.morph.engine.collision

import com.morph.engine.math.Vector2f

class PerfectlyInelasticSolver : CollisionSolver() {
    override fun collisionResponse(normal: Vector2f, vel: Vector2f): Vector2f = Vector2f()
}

class RestitutionSolver(val restitution: Float) : CollisionSolver() {
    override fun collisionResponse(normal: Vector2f, vel: Vector2f): Vector2f = normal * (normal dot vel) * -restitution
}