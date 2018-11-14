package com.morph.demos.test.particlePhysics

import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.physics.components.RigidBody
import com.morph.engine.physics.components.Transform2D

class PhysicsSystem(game : PartPhysGame) : GameSystem(game) {
    override fun initSystem() {}

    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<RigidBody>() && e.hasComponent<Transform2D>()

    override fun fixedUpdate(e : Entity, dt : Float) {
        val rb = e.getComponent<RigidBody>()!!
        val translation = rb.update(dt)

        val t2d = e.getComponent<Transform2D>()!!
        t2d.position += translation
    }
}