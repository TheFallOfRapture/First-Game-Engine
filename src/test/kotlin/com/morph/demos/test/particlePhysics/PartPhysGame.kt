package com.morph.demos.test.particlePhysics

import com.morph.engine.collision.CollisionEngine
import com.morph.engine.collision.RestitutionSolver
import com.morph.engine.core.Game
import com.morph.engine.core.OrthoCam2D
import com.morph.engine.graphics.EmitterSystem
import com.morph.engine.graphics.Framebuffer
import com.morph.engine.graphics.ParticleSystem
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.PhysicsEngine

class PartPhysGame : Game(1366, 768, "Particle Physics Testing", 60f, false) {
//    var prevTime = System.nanoTime()
    override fun initGame() {
        val size = 50f

        renderingEngine.setActiveFramebuffer(Framebuffer(width, height, false, 0))

        setWorld(PartPhysWorld(this))
        camera = OrthoCam2D(Vector2f(0f, 0f), 0f, size * (width.toFloat() / height), size)
        addSystem(ParticlePhysicsSystem(this))
        addSystem(CollisionEngine(this, RestitutionSolver(1.0f)))
        addSystem(PhysicsEngine(this))
        addSystem(EmitterSystem(this))
        addSystem(ParticleSystem(this))
        addSystem(FollowerSystem(this))
    }

    override fun preGameUpdate() {
    }

    override fun fixedGameUpdate(dt: Float) {
//        val currentTime = System.nanoTime()
//        val elapsedTime = currentTime - prevTime
//        val realFPS = 1000000000 * (1.0 / elapsedTime)
//        prevTime = currentTime
//        println("$realFPS frames per second")
    }

    override fun postGameUpdate() {
    }

    override fun handleInput() {
    }
}
