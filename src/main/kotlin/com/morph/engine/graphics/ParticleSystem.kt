package com.morph.engine.graphics

import com.morph.engine.core.Game
import com.morph.engine.core.GameSystem
import com.morph.engine.entities.Entity
import com.morph.engine.graphics.components.Particle
import com.morph.engine.physics.components.Transform2D

class ParticleSystem(game : Game) : GameSystem(game) {
    val start = Color(0f, 1f, 0f, 1f)
    val end = Color(0f, 0f, 1f, 0f)
    override fun acceptEntity(e: Entity): Boolean = e.hasComponent<Particle>() && e.hasComponent<Transform2D>()

    override fun initSystem() {}

    override fun fixedUpdate(e: Entity, dt: Float) {
        super.fixedUpdate(e, dt)

        val particle = e.getComponent<Particle>()!!
        val t2d = e.getComponent<Transform2D>()!!

        val k = particle.age / particle.emitter.lifetime
        val color = start * (1f - k) + end * k

        particle.age += dt
        particle.color = Color(particle.color.red, particle.color.green, particle.color.blue, 1f - k)

//        t2d.position -= Vector2f(0f, 0.01f)
    }
}