package com.morph.engine.graphics.shaders

import com.morph.engine.graphics.Color
import com.morph.engine.graphics.components.RenderData
import com.morph.engine.graphics.components.light.Light
import com.morph.engine.math.Matrix4f
import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f
import com.morph.engine.math.Vector4f
import com.morph.engine.physics.components.Transform
import org.lwjgl.opengl.GL20.*
import java.util.*

abstract class Uniforms {
    private var uniforms: HashMap<String, Int> = HashMap()
    protected lateinit var shader: Shader<*>

    fun init(shader: Shader<*>) {
        this.shader = shader
    }

    abstract fun defineUniforms(shader: Int)
    abstract fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f, lights: List<Light>)
    abstract fun unbind(t: Transform, data: RenderData)

    protected fun addUniform(name: String, shader: Int) {
        val location = glGetUniformLocation(shader, name)
        uniforms[name] = location
    }

    fun setUniform1i(name: String, value: Int) {
        val location = uniforms[name]
        location?.let { glUniform1i(it, value) }
    }

    fun setUniform1f(name: String, value: Float) {
        val location = uniforms[name]
        location?.let { glUniform1f(it, value) }
    }

    fun setUniform2f(name: String, value: Vector2f) {
        val location = uniforms[name]
        location?.let { glUniform2f(it, value.x, value.y) }
    }

    fun setUniform3f(name: String, value: Vector3f) {
        val location = uniforms[name]
        location?.let { glUniform3f(it, value.x, value.y, value.z) }
    }

    fun setUniform3f(name: String, value: Color) {
        val location = uniforms[name]
        location?.let { glUniform3f(it, value.red, value.green, value.blue) }
    }

    fun setUniform4f(name: String, value: Vector4f) {
        val location = uniforms[name]
        location?.let { glUniform4f(it, value.x, value.y, value.z, value.w) }
    }

    fun setUniform4f(name: String, value: Color) {
        val location = uniforms[name]
        location?.let { glUniform4f(it, value.red, value.green, value.blue, value.alpha) }
    }

    fun setUniformMatrix4fv(name: String, value: Matrix4f) {
        val location = uniforms[name]
        location?.let { glUniformMatrix4fv(it, false, value.asArray()) }
    }
}
