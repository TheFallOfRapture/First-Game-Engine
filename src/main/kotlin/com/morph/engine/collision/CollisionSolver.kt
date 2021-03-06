package com.morph.engine.collision

import com.morph.engine.entities.given
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.RigidBody

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

            rb.velocity = newVelocity
            rb.applyForce(-blockDir * (blockDir dot rb.netForce))
        }

        given<RigidBody>(b) { rb ->
            val vel = rb.velocity
            val blockDir = coll.collisionData.normal
            val remove = blockDir * (blockDir dot vel) * (1.0f - coll.collisionData.time)

            val newVelocity = vel - remove + collisionResponse(blockDir, vel)

            rb.velocity = newVelocity
            rb.applyForce(-blockDir * (blockDir dot rb.netForce))
        }
    }

    abstract fun collisionResponse(normal: Vector2f, vel: Vector2f) : Vector2f
}