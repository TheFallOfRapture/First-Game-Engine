package com.morph.engine.graphics

import com.morph.engine.util.IOUtils
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.stb.STBTTBakedChar
import org.lwjgl.stb.STBTruetype
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*
import javax.imageio.ImageIO

class Texture {
    val filename: String
    private val resource: TextureResource? // TODO: Make non-nullable, an error in texture loading should return an empty Texture

    constructor(filename: String?) {
        this.filename = filename ?: "textures/solid.png"
        val oldResource = loadedTextures[this.filename]
        if (oldResource != null) {
            this.resource = oldResource
            resource.addReference()
        } else {
            resource = loadTexture(this.filename)
            loadedTextures[this.filename] = resource!!
        }
    }

    constructor(font: String?, size: Int, bitmapWidth: Int, bitmapHeight: Int) {
        this.filename = font ?: "C:/Windows/Fonts/arial.ttf"
        val oldResource = loadedTextures[this.filename + ":" + size]
        if (oldResource != null) {
            this.resource = oldResource
            resource.addReference()
        } else {
            resource = loadFont(this.filename, size, bitmapWidth, bitmapHeight)
            loadedTextures[this.filename] = resource
        }
    }

    constructor(id: String, width: Int, height: Int, pixels: ByteBuffer) {
        this.filename = id
        val oldResource = loadedTextures[this.filename]
        if (oldResource != null) {
            this.resource = oldResource
            resource.addReference()
        } else {
            resource = loadTextureFromByteBuffer(width, height, pixels)
            loadedTextures[this.filename] = resource
        }
    }

    private fun loadTextureFromByteBuffer(width: Int, height: Int, buffer: ByteBuffer): TextureResource {
        val id = glGenTextures()
        buffer.flip()

        glBindTexture(GL_TEXTURE_2D, id)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, width, height, 0, GL_ALPHA, GL_UNSIGNED_BYTE, buffer)

        return TextureResource(id)
    }

    private fun loadTexture(filename: String): TextureResource? {
        try {
            val image = ImageIO.read(Texture::class.java.classLoader.getResourceAsStream(filename))
            val pixels = image.getRGB(0, 0, image.width, image.height, null, 0, image.width)

            val buffer = BufferUtils.createByteBuffer(image.width * image.height * 4)

            val hasAlpha = image.colorModel.hasAlpha()

            for (y in 0 until image.height) {
                for (x in 0 until image.width) {
                    val color = pixels[x + y * image.width]
                    buffer.put((color shr 16 and 0xff).toByte())
                    buffer.put((color shr 8 and 0xff).toByte())
                    buffer.put((color and 0xff).toByte())

                    if (hasAlpha)
                        buffer.put((color shr 24 and 0xff).toByte())
                    else
                        buffer.put(0xff.toByte())
                }
            }

            buffer.flip()

            val id = glGenTextures()

            glBindTexture(GL_TEXTURE_2D, id)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)

            return TextureResource(id)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun loadFont(font: String, size: Int, bitmapWidth: Int, bitmapHeight: Int): TextureResource {
        val texture = glGenTextures()
        val chars = STBTTBakedChar.malloc(96)

        try {
            val fontBuffer = IOUtils.getFileAsByteBuffer(font, 160 * 1024)
            val bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight)

            STBTruetype.stbtt_BakeFontBitmap(fontBuffer, size.toFloat(), bitmap, bitmapWidth, bitmapHeight, 32, chars)

            glBindTexture(GL_TEXTURE_2D, texture)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, bitmapWidth, bitmapHeight, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return TextureResource(texture)
    }

    fun destroy() {
        resource!!.removeReference()
    }

    fun bind() {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, resource!!.id)
    }

    fun bind(i: Int) {
        glActiveTexture(GL_TEXTURE0 + i)
        glBindTexture(GL_TEXTURE_2D, resource!!.id)
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    companion object {
        private val loadedTextures = HashMap<String, TextureResource>()
    }
}
