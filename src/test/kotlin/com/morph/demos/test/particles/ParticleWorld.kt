package com.morph.demos.test.particles

import com.morph.engine.core.ListWorld
import com.morph.engine.entities.EntityFactory
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.components.Emitter
import com.morph.engine.graphics.shaders.InstancedShader
import com.morph.engine.graphics.shaders.TintShader
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f
import com.morph.engine.physics.components.Transform2D

class ParticleWorld(game : ParticleGame) : ListWorld(game) {
    override fun init() {
        val emitter1 = EntityFactory.getEntity("rainEmitter")
                .addComponent(Transform2D())
                .addComponent(Emitter(
                        color = Color(0.2f, 0.2f, 1f, 0.5f),
                        spawnRate = 60f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 5f,
                        shader = InstancedShader()
                ))

        val emitter2 = EntityFactory.getEntity("snowEmitter")
                .addComponent(Transform2D(position = Vector2f(5f, 0f)))
                .addComponent(Emitter(
                        color = Color(0.7f, 0.7f, 1.0f, 0.5f),
                        spawnRate = 60f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 5f,
                        shader = InstancedShader()
                ))

        addEntity(emitter1)
        addEntity(emitter2)
    }
}