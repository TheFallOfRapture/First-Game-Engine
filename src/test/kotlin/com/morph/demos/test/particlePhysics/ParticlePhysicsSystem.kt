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
    var time = 0f
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<Transform2D>() && e.hasComponent<RigidBody>() && e.hasComponent<Particle>()

    override fun initSystem() {}

    override fun fixedUpdate(e: Entity, dt: Float) {
        val rb = e.getComponent<RigidBody>()!!
        val gravitationalCenter = Mouse.worldMousePosition
//        val gravitationalCenter = Vector2f(Math.cos(time.toDouble()).toFloat(), Math.sin(time.toDouble()).toFloat()) * 5f
        val gravity = gravityForce(e.getComponent<Transform2D>()!!.position, gravitationalCenter, rb.mass, 2000f)

        rb.applyForce(gravity)
    }

    fun gravityForce(position : Vector2f, center : Vector2f, mass1 : Float, mass2 : Float) : Vector2f {
        val direction = (center - position).normalize()
        val gravityConstant = 1e-3f
        val forceStrength = gravityConstant * mass1 * mass2 / Math.abs((center - position) dot (center - position))

        return direction * forceStrength
    }

    fun gravityStrength(position : Vector2f, center : Vector2f, mass1 : Float, mass2 : Float) : Float {
        val direction = (center - position).normalize()
        val gravityConstant = 1e-3f
        val forceStrength = gravityConstant * mass1 * mass2 / Math.abs((center - position) dot (center - position))

        return forceStrength
    }

    override fun systemFixedUpdate(dt: Float) {
        time += dt
    }

//    override fun systemFixedUpdate(dt: Float) {
//        val entities = game.world.entities.filter(::acceptEntity)
//
//        for (i in 0 until entities.size) {
//            for (j in i + 1 until entities.size) {
//                val e1 = entities[i]
//                val e2 = entities[j]
//
//                val t1 = e1.getComponent<Transform2D>()!!
//                val t2 = e2.getComponent<Transform2D>()!!
//                val rb1 = e1.getComponent<RigidBody>()!!
//                val rb2 = e2.getComponent<RigidBody>()!!
//
//                val gravity = gravityStrength(
//                        t1.position,
//                        t2.position,
//                        rb1.mass,
//                        rb2.mass
//                )
//
//                val d1 = (t2.position - t1.position).normalize()
//                val d2 = (t1.position - t2.position).normalize()
//
//                rb1.applyForce(d1 * gravity)
//                rb2.applyForce(d2 * gravity)
//            }
//        }
//    }
}