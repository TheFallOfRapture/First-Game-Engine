package com.morph.demos.test.particlePhysics

import com.morph.engine.collision.components.BoundingBox2D
import com.morph.engine.core.ListWorld
import com.morph.engine.entities.EntityFactory
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.components.Emitter
import com.morph.engine.graphics.shaders.InstancedShader
import com.morph.engine.graphics.shaders.TintShader
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f
import com.morph.engine.physics.components.RigidBody
import com.morph.engine.physics.components.Transform2D

class PartPhysWorld(game: PartPhysGame) : ListWorld(game) {
    override fun init() {
        val emitter1 = EntityFactory.getEntity("rainEmitter")
                .addComponent(Transform2D(position = Vector2f(-1f, -1f)))
                .addComponent(Emitter(
                        color = Color(0.2f, 0.2f, 1f, 0.5f),
                        spawnRate = 25f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 5f,
                        shader = InstancedShader()
                ))

        val emitter2 = EntityFactory.getEntity("snowEmitter")
                .addComponent(Transform2D(position = Vector2f(-1f, 1f)))
                .addComponent(Emitter(
                        color = Color(0.7f, 0.7f, 1.0f, 0.5f),
                        spawnRate = 25f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 5f,
                        shader = InstancedShader()
                ))

        val emitter3 = EntityFactory.getEntity("fireEmitter")
                .addComponent(Transform2D(position = Vector2f(1f, 1f)))
                .addComponent(Emitter(
                        color = Color(1.0f, 0.2f, 0.2f, 0.5f),
                        spawnRate = 25f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 5f,
                        shader = InstancedShader()
                ))

        val emitter4 = EntityFactory.getEntity("greenEmitter")
                .addComponent(Transform2D(position = Vector2f(1f, -1f)))
                .addComponent(Emitter(
                        color = Color(0.2f, 1.0f, 0.2f, 0.5f),
                        spawnRate = 25f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 5f,
                        shader = InstancedShader()
                ))

        addEntity(emitter1)
        addEntity(emitter2)
        addEntity(emitter3)
        addEntity(emitter4)

        // TODO: Create entity that follows the mouse.
        val player = EntityFactory.getCustomTintRectangle("player", 0.2f, 0.2f, Color(0.75f, 0.75f, 0.75f), TintShader())
        player.addComponent(PlayerFollower())
        player.addComponent(BoundingBox2D(Vector2f(), Vector2f(0.1f, 0.1f)))
        player.addComponent(RigidBody())
        addEntity(player)
    }
}
