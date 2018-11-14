package com.morph.demos.test.particlePhysics

import com.morph.demos.test.particlePhysics.PartPhysGame
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.graphics.components.Particle
import com.morph.engine.input.Mouse
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.RigidBody
import com.morph.engine.physics.components.Transform2D

class ParticlePhysicsSystem(game : PartPhysGame) : GameSystem(game) {
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<Transform2D>() && e.hasComponent<RigidBody>() && e.hasComponent<Particle>()

    override fun initSystem() {}

    override fun fixedUpdate(e: Entity, dt: Float) {
        val rb = e.getComponent<RigidBody>()!!
        val gravitationalCenter = Mouse.worldMousePosition
        val gravity = gravityForce(e.getComponent<Transform2D>()!!.position, gravitationalCenter, rb.mass, 2000f)

        rb.applyForce(Vector2f(0.1f, 0f))
        rb.applyForce(gravity)
    }

    fun gravityForce(position : Vector2f, center : Vector2f, mass1 : Float, mass2 : Float) : Vector2f {
        val direction = (center - position).normalize()
        val gravityConstant = 1e-3f
        val forceStrength = gravityConstant * mass1 * mass2 / Math.abs((center - position) dot (center - position))

        return direction * forceStrength
    }
}