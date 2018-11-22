package com.morph.demos.test.particlePhysics

import com.morph.engine.core.ListWorld
import com.morph.engine.entities.EntityFactory
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.graphics.components.Emitter
import com.morph.engine.graphics.shaders.InstancedShader
import com.morph.engine.graphics.shaders.TintShader
import com.morph.engine.input.*
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f
import com.morph.engine.physics.components.RigidBody
import com.morph.engine.physics.components.Transform2D
import org.lwjgl.glfw.GLFW.*

class PartPhysWorld(game: PartPhysGame) : ListWorld(game) {
    override fun init() {
        val emitter1 = EntityFactory.getEntity("rainEmitter")
                .addComponent(Transform2D(position = Vector2f(-9f, 5f)))
                .addComponent(Emitter(
                        color = Color(0.2f, 0.2f, 1.0f, 1.0f),
                        spawnRate = 150f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 2f,
                        shader = InstancedShader(),
                        texture = Texture("textures/particle.png")
                ))
//                .addComponent(PlayerFollower())

        val emitter2 = EntityFactory.getEntity("snowEmitter")
                .addComponent(Transform2D(position = Vector2f(-3f, -5f)))
                .addComponent(Emitter(
                        color = Color(0.7f, 0.7f, 1.0f, 1.0f),
                        spawnRate = 150f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 2f,
                        shader = InstancedShader(),
                        texture = Texture("textures/particle.png")
                ))

        val emitter3 = EntityFactory.getEntity("fireEmitter")
                .addComponent(Transform2D(position = Vector2f(3f, 5f)))
                .addComponent(Emitter(
                        color = Color(1.0f, 0.2f, 0.2f, 1.0f),
                        spawnRate = 150f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 2f,
                        shader = InstancedShader(),
                        texture = Texture("textures/particle.png")
                ))

        val emitter4 = EntityFactory.getEntity("greenEmitter")
                .addComponent(Transform2D(position = Vector2f(9f, -5f)))
                .addComponent(Emitter(
                        color = Color(0.2f, 1.0f, 0.2f, 1.0f),
                        spawnRate = 150f,
                        velocity = Vector3f(0f, 1f, 0f),
                        lifetime = 2f,
                        shader = InstancedShader(),
                        texture = Texture("textures/particle.png")
                ))

        val crazyEmitter = EntityFactory.getEntity("crazyEmitter")
                .addComponent(Emitter(
                        color = Color(1f, 1f, 1f, 1f),
                        spawnRate = 100f,
                        velocity = Vector3f(0f, 0f, 0f),
                        lifetime = 10f,
                        shader = InstancedShader(),
                        texture = Texture("textures/particle.png")
                ))

        addEntity(emitter1)
        addEntity(emitter2)
        addEntity(emitter3)
        addEntity(emitter4)
//        addEntity(crazyEmitter)

        // TODO: Create entity that follows the mouse.
        val player = EntityFactory.getCustomTintRectangle("player", 1f, 1f, Color(0.5f, 0.5f, 0.5f), TintShader())
                .addComponent(PlayerFollower())
//                .addComponent(BoundingBox2D(Vector2f(), Vector2f(2.5f, 2.5f)))
                .addComponent(RigidBody())
                .addComponent(Emitter(
                        color = Color(0.2f, 0.75f, 0.2f, 1f),
                        spawnRate = 100f,
                        velocity = Vector3f(0f, 0f, 0f),
                        lifetime = 10f,
                        shader = InstancedShader(),
                        texture = Texture("textures/particle.png")
                ))

        addEntity(player)

        val locus = EntityFactory.getCustomTintRectangle("locus", 1f, 1f, Color(0.3f, 0f, 0f), TintShader()).addComponent(Transform2D(position = Vector2f(0f, 0f)))
        addEntity(locus)

        val inputMapping = InputMapping()
        inputMapping.mapKey(GLFW_KEY_LEFT, KeyRepeat) {
            Emitter.size -= 0.01f
        }

        inputMapping.mapKey(GLFW_KEY_RIGHT, KeyRepeat) {
            Emitter.size += 0.01f
        }

        inputMapping.mapKey(GLFW_KEY_DOWN, KeyRepeat) {
            Emitter.spread -= 0.01f
        }

        inputMapping.mapKey(GLFW_KEY_UP, KeyRepeat) {
            Emitter.spread += 0.01f
        }

        inputMapping.mapButton(GLFW_MOUSE_BUTTON_1, MousePress) {
            player.getComponent<Emitter>()!!.enabled = true
        }

        inputMapping.mapButton(GLFW_MOUSE_BUTTON_1, MouseRelease) {
            player.getComponent<Emitter>()!!.enabled = false
        }

        inputMapping.mapButton(GLFW_MOUSE_BUTTON_2, MousePress) {
            locus.getComponent<Transform2D>()?.position = Mouse.worldMousePosition
        }

        game.inputMapping = inputMapping
    }
}
