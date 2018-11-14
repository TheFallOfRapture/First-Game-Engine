package com.morph.demos.test.particlePhysics

import com.morph.engine.core.ListWorld
import com.morph.engine.entities.EntityFactory
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.components.Emitter
import com.morph.engine.graphics.shaders.InstancedShader
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f
import com.morph.engine.physics.components.Transform2D

class PartPhysWorld(game: PartPhysGame) : ListWorld(game) {
    override fun init() {
        val emitter1 = EntityFactory.getEntity("rainEmitter")
                .addComponent(Transform2D(position = Vector2f(-3f, -3f)))
                .addComponent(Emitter(
                        color = Color(0.2f, 0.2f, 1f, 0.5f),
                        spawnRate = 30f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 10f,
                        shader = InstancedShader()
                ))

        val emitter2 = EntityFactory.getEntity("snowEmitter")
                .addComponent(Transform2D(position = Vector2f(-3f, 3f)))
                .addComponent(Emitter(
                        color = Color(0.7f, 0.7f, 1.0f, 0.5f),
                        spawnRate = 30f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 10f,
                        shader = InstancedShader()
                ))

        val emitter3 = EntityFactory.getEntity("fireEmitter")
                .addComponent(Transform2D(position = Vector2f(3f, 3f)))
                .addComponent(Emitter(
                        color = Color(1.0f, 0.2f, 0.2f, 0.5f),
                        spawnRate = 30f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 10f,
                        shader = InstancedShader()
                ))

        val emitter4 = EntityFactory.getEntity("greenEmitter")
                .addComponent(Transform2D(position = Vector2f(3f, -3f)))
                .addComponent(Emitter(
                        color = Color(0.2f, 1.0f, 0.2f, 0.5f),
                        spawnRate = 30f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 10f,
                        shader = InstancedShader()
                ))

        addEntity(emitter1)
        addEntity(emitter2)
        addEntity(emitter3)
        addEntity(emitter4)

        // TODO: Create entity that follows the mouse.
    }
}
