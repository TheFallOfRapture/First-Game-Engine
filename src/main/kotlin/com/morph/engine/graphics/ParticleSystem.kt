package com.morph.engine.graphics

import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.entities.given
import com.morph.engine.graphics.components.Emitter
import com.morph.engine.physics.components.Transform2D

class ParticleSystem(game : Game) : GameSystem(game) {
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<Emitter>() && e.hasComponent<Transform2D>()

    override fun initSystem() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fixedUpdate(e: Entity, dt: Float) {
        super.fixedUpdate(e, dt)

        given<Emitter>(e) { emitter ->

        }
    }
}