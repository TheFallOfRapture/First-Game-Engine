package com.morph.engine.collision

import com.morph.engine.entities.given
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.RigidBody
import com.morph.engine.physics.components.Velocity2D

abstract class CollisionSolver {
    fun solveCollision(coll: SweepCollision) {
        if (coll !is SweepCollision.Hit) return

        val a = coll.entity
        val b = coll.hit

        given<RigidBody>(a) { rb ->
            val vel = rb.velocity
            val blockDir = -coll.collisionData.normal
            val remove = blockDir * (blockDir dot vel) * (1.0f - coll.collisionData.time)

            val newVelocity = vel - remove + collisionResponse(blockDir, vel)

            rb.applyImpulse(collisionResponse(blockDir, vel) - remove)
//            rb.velocity = newVelocity
        }

        given<RigidBody>(b) { rb ->
            val vel = rb.velocity
            val blockDir = coll.collisionData.normal
            val remove = blockDir * (blockDir dot vel) * (1.0f - coll.collisionData.time)

            val newVelocity = vel - remove + collisionResponse(blockDir, vel)

            rb.applyImpulse(collisionResponse(blockDir, vel) - remove)
//            rb.velocity = newVelocity
        }
    }

    abstract fun collisionResponse(normal: Vector2f, vel: Vector2f) : Vector2f
}