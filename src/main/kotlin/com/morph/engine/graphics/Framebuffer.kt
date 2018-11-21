package com.morph.engine.graphics

import com.morph.engine.graphics.shaders.FramebufferShader
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import java.util.*

class Framebuffer(val width: Int, val height: Int, depthBufferEnabled: Boolean, vararg textureAttachments: Int) {
    val shader: FramebufferShader = FramebufferShader()
    private val id: Int = glGenFramebuffers()
    private val textures: MutableMap<Int, TextureResource> = mutableMapOf()
    private val depthID: Optional<Int>
    val vao: Int = glGenVertexArrays()

    init {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
        textureAttachments.forEach { initTexture(it, width, height) }
//        glDrawBuffers(textureAttachments.map { it + GL_COLOR_ATTACHMENT0 }.toIntArray())
        depthID = if (depthBufferEnabled) Optional.of(initDepthBuffer(width, height)) else Optional.empty()

        val positionBuffer = glGenBuffers()
        val texCoordBuffer = glGenBuffers()
        val indexBuffer = glGenBuffers()

        val positionData = doubleArrayOf(
                -1.0, -1.0, 0.0,
                -1.0, 1.0, 0.0,
                1.0, 1.0, 0.0,
                1.0, -1.0, 0.0
        )

        val texCoordData = doubleArrayOf(
                0.0, 0.0,
                0.0, 1.0,
                1.0, 1.0,
                1.0, 0.0
        )

        val indexData = intArrayOf(0, 1, 3, 1, 2, 3)

        glBindBuffer(GL_ARRAY_BUFFER, positionBuffer)
        glBufferData(GL_ARRAY_BUFFER, positionData, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER, texCoordBuffer)
        glBufferData(GL_ARRAY_BUFFER, texCoordData, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER, 0)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)

        glBindVertexArray(vao)
            glBindBuffer(GL_ARRAY_BUFFER, positionBuffer)
            glEnableVertexAttribArray(0)
            glVertexAttribPointer(0, 3, GL_DOUBLE, false, 0, 0)

            glBindBuffer(GL_ARRAY_BUFFER, texCoordBuffer)
            glEnableVertexAttribArray(1)
            glVertexAttribPointer(1, 2, GL_DOUBLE, false, 0, 0)

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer)
        glBindVertexArray(0)

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException("Incomplete framebuffer initialization!")
        } else {
            println("Framebuffer initialized successfully.")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    private fun initTexture(target: Int, width: Int, height: Int) {
        val texID = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texID)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        glBindTexture(GL_TEXTURE_2D, 0)

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + target, GL_TEXTURE_2D, texID, 0)

        textures[target] = TextureResource(id)
    }

    private fun initDepthBuffer(width: Int, height: Int): Int {
        val tempID = glGenRenderbuffers()
        glBindRenderbuffer(GL_RENDERBUFFER, tempID)
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, tempID)
        return tempID
    }

    fun bindRenderTargets() {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
        glViewport(0, 0, width, height)
    }

    fun unbindRenderTargets() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun bindTexture(target: Int) {
//        glActiveTexture(GL_TEXTURE0)
        textures[target]?.let { glBindTexture(GL_TEXTURE_2D, it.id) }
    }

    fun unbindTextures() {
//        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, 0)
    }
}