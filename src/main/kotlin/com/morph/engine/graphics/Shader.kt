package com.morph.engine.graphics

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.HashMap

import com.morph.engine.math.Matrix4f
import com.morph.engine.math.Vector3f
import com.morph.engine.math.Vector4f
import javax.inject.Inject

open class Shader<out T : Uniforms>(shaderURL: String, val uniforms: T) {
    private var resource: ShaderResource

    init {
        val oldResource = loadedShaders[shaderURL]
        if (oldResource != null) {
            this.resource = oldResource
            oldResource.addReference()
        } else {
            this.resource = Shader.loadShaderProgram(shaderURL)
            loadedShaders.put(shaderURL, resource)
        }
    }

    fun init() {
        uniforms.init(this)
        uniforms.defineUniforms(resource.id)
    }

    fun bind() {
        glUseProgram(resource.id)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun setUniform3f(name: String, value: Vector3f) {
        val location = glGetUniformLocation(resource.id, name)
        glUniform3f(location, value.x, value.y, value.z)
    }

    fun setUniform3f(name: String, value: Color) {
        val location = glGetUniformLocation(resource.id, name)
        glUniform3f(location, value.red, value.green, value.blue)
    }

    fun setUniform4f(name: String, value: Vector4f) {
        val location = glGetUniformLocation(resource.id, name)
        glUniform4f(location, value.x, value.y, value.z, value.w)
    }

    fun setUniform4f(name: String, value: Color) {
        val location = glGetUniformLocation(resource.id, name)
        glUniform4f(location, value.red, value.green, value.blue, value.alpha)
    }

    fun setUniformMatrix4fv(name: String, value: Matrix4f) {
        val location = glGetUniformLocation(resource.id, name)
        glUniformMatrix4fv(location, false, value.asArray())
    }

    fun removeReference() {
        resource.removeReference()
    }

    fun setUniform1i(name: String, value: Int) {
        val location = glGetUniformLocation(resource.id, name)
        glUniform1i(location, value)
    }

    companion object {
        private val loadedShaders = HashMap<String, ShaderResource>()

        fun loadShaderProgram(shaderURL: String): ShaderResource {
            var shaderProgramID = 0
            try {
                val vertexShaderID = loadVertexShader(shaderURL + ".vsh")
                val fragmentShaderID = loadFragmentShader(shaderURL + ".fsh")
                shaderProgramID = glCreateProgram()

                glAttachShader(shaderProgramID, vertexShaderID)
                glAttachShader(shaderProgramID, fragmentShaderID)

                glLinkProgram(shaderProgramID)

                if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE)
                    throw RuntimeException("Failed to link shader program: " + glGetProgramInfoLog(shaderProgramID, 1024))

                glValidateProgram(shaderProgramID)

                if (glGetProgrami(shaderProgramID, GL_VALIDATE_STATUS) == GL_FALSE)
                    throw RuntimeException("Failed to validate shader program: " + glGetProgramInfoLog(shaderProgramID, 1024))

                glDeleteShader(vertexShaderID)
                glDeleteShader(fragmentShaderID)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            return ShaderResource(shaderProgramID)
        }

        @Throws(IOException::class)
        private fun loadShader(filename: String, type: Int): Int {
            val shaderSource = StringBuilder()
            val reader = BufferedReader(InputStreamReader(Shader::class.java.classLoader.getResourceAsStream(filename)))

            reader.useLines {
                it.forEach {
                    shaderSource.append(it).append("\n")
                }
            }

            val shaderID = glCreateShader(type)
            glShaderSource(shaderID, shaderSource)

            glCompileShader(shaderID)

            if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
                throw RuntimeException("""Failed to initialize shader of type $type: ${glGetShaderInfoLog(shaderID, 1024)}""")

            return shaderID
        }

        @Throws(IOException::class)
        private fun loadVertexShader(filename: String): Int {
            return loadShader(filename, GL_VERTEX_SHADER)
        }

        @Throws(IOException::class)
        private fun loadFragmentShader(filename: String): Int {
            return loadShader(filename, GL_FRAGMENT_SHADER)
        }
    }
}
