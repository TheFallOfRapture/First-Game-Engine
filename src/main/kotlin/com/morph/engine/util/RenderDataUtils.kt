package com.morph.engine.util

/**
 * Created on 7/30/2017.
 */

import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.graphics.components.RenderData
import com.morph.engine.graphics.shaders.Shader
import com.morph.engine.math.Vector2f

object RenderDataUtils {
    fun createTintedSquare(shader: Shader<*>, texture: Texture): RenderData {
        val data = RenderDataUtils.createSquare(shader, texture)
        data.tint = Color(1f, 1f, 1f)

        return data
    }

    fun createTintedSquare(c: Color, shader: Shader<*>, texture: Texture): RenderData {
        val data = RenderDataUtils.createSquare(Color(1f, 1f, 1f), shader, texture)
        data.tint = c

        return data
    }

    fun createTintedSquare(c: Color, shader: Shader<*>, texture: Texture, altTexture: Texture, lerpFactor: Float): RenderData {
        val data = RenderDataUtils.createSquare(Color(1f, 1f, 1f), shader, texture, altTexture, lerpFactor)
        data.tint = c

        return data
    }

    fun createSquare(c: Color, shader: Shader<*>, texture: Texture, altTexture: Texture, lerpFactor: Float): RenderData {
        val result = RenderData(shader, texture)
        result.setTexture(altTexture, 1)
        result.lerpFactor = lerpFactor
        result.addVertex(Vector2f(-0.5f, -0.5f), c, Vector2f(0f, 1f))
        result.addVertex(Vector2f(-0.5f, 0.5f), c, Vector2f(0f, 0f))
        result.addVertex(Vector2f(0.5f, 0.5f), c, Vector2f(1f, 0f))
        result.addVertex(Vector2f(0.5f, -0.5f), c, Vector2f(1f, 1f))

        result.addIndices(0, 1, 3, 1, 2, 3)

        return result
    }

    fun createSquare(c: Color, shader: Shader<*>, texture: Texture): RenderData {
        val result = RenderData(shader, texture)
        result.addVertex(Vector2f(-0.5f, -0.5f), c, Vector2f(0f, 1f))
        result.addVertex(Vector2f(-0.5f, 0.5f), c, Vector2f(0f, 0f))
        result.addVertex(Vector2f(0.5f, 0.5f), c, Vector2f(1f, 0f))
        result.addVertex(Vector2f(0.5f, -0.5f), c, Vector2f(1f, 1f))

        result.addIndices(0, 1, 3, 1, 2, 3)

        return result
    }

    fun createSquare(shader: Shader<*>, texture: Texture, altTexture: Texture, lerpFactor: Float): RenderData {
        val result = RenderData(shader, texture)
        result.setTexture(altTexture, 1)
        result.lerpFactor = lerpFactor
        result.addVertex(Vector2f(-0.5f, -0.5f), Vector2f(0f, 1f))
        result.addVertex(Vector2f(-0.5f, 0.5f), Vector2f(0f, 0f))
        result.addVertex(Vector2f(0.5f, 0.5f), Vector2f(1f, 0f))
        result.addVertex(Vector2f(0.5f, -0.5f), Vector2f(1f, 1f))

        result.addIndices(0, 1, 3, 1, 2, 3)

        return result
    }

    fun createSquare(shader: Shader<*>, texture: Texture): RenderData {
        val result = RenderData(shader, texture)
        result.addVertex(Vector2f(-0.5f, -0.5f), Vector2f(0f, 1f))
        result.addVertex(Vector2f(-0.5f, 0.5f), Vector2f(0f, 0f))
        result.addVertex(Vector2f(0.5f, 0.5f), Vector2f(1f, 0f))
        result.addVertex(Vector2f(0.5f, -0.5f), Vector2f(1f, 1f))

        result.addIndices(0, 1, 3, 1, 2, 3)

        return result
    }
}

inline fun <T : RenderData> T.updateAll(body : T.() -> Unit) = with(this) {
    body()
    refreshData()
}

inline fun <T : RenderData> T.updateVertices(body : T.() -> Unit) = with(this) {
    body()
    refreshVertices()
}

inline fun <T : RenderData> T.updateColors(body : T.() -> Unit) = with(this) {
    body()
    refreshColors()
}

inline fun <T : RenderData> T.updateTexCoords(body : T.() -> Unit) = with(this) {
    body()
    refreshTexCoords()
}

inline fun <T : RenderData> T.updateIndices(body : T.() -> Unit) = with(this) {
    body()
    refreshIndices()
}