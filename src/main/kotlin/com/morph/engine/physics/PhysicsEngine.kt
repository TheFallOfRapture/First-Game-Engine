package com.morph.engine.physics

import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.entities.given
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.RigidBody
import com.morph.engine.physics.components.Transform

class PhysicsEngine(game : Game, var gravity : Vector2f = Vector2f(0f, -1000f)) : GameSystem(game) {
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<RigidBody>() && e.hasComponent<Transform>()

    override fun initSystem() {}

    override fun fixedUpdate(e: Entity, dt: Float) {
        given<RigidBody>(e) { rb ->
            given<Transform>(e) { t ->
                rb.applyForce(gravity * rb.mass)
                val translation = rb.update(dt)
                t.translate(translation.asTranslationMatrix())
            }
        }
    }
}
