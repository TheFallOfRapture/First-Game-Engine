package com.morph.demos.test.particles

import com.morph.engine.core.Game
import com.morph.engine.core.OrthoCam2D
import com.morph.engine.graphics.EmitterSystem
import com.morph.engine.graphics.ParticleSystem
import com.morph.engine.math.Vector2f

class ParticleGame : Game(800, 600, "Particle Effect Testing Zone", 60f, false) {
    override fun initGame() {
        setWorld(ParticleWorld(this))
        camera = OrthoCam2D(Vector2f(0f, 0f), 0f, 10f * (width.toFloat() / height), 10f)
        addSystem(EmitterSystem(this))
        addSystem(ParticleSystem(this))
    }

    override fun preGameUpdate() {
    }

    override fun fixedGameUpdate(dt: Float) {
    }

    override fun postGameUpdate() {
    }

    override fun handleInput() {
    }
}
