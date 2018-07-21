package com.morph.engine.util

import org.lwjgl.BufferUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Paths


object IOUtils {
    /**
     * From org.lwjgl.demo.util.IOUtil.ioResourceToByteBuffer
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getFileAsByteBuffer(filename: String, size: Int): ByteBuffer {
        var buffer: ByteBuffer? = null

        val path = Paths.get(filename)
        if (Files.isReadable(path)) {
            Files.newByteChannel(path).use { channel ->
                buffer = BufferUtils.createByteBuffer(channel.size().toInt() + 1)
                while (channel.read(buffer) >= 0);
            }
        } else {
            IOUtils::class.java.classLoader.getResourceAsStream(filename).use { source ->
                Channels.newChannel(source).use { channel ->
                    buffer = BufferUtils.createByteBuffer(size)
                    while (true) {
                        val bytes = channel.read(buffer)
                        if (bytes == -1)
                            break
                        if (buffer?.remaining() == 0)
                            buffer = resizeByteBuffer(buffer!!, buffer?.capacity()!! * 2)
                    }
                }
            }
        }

        buffer?.flip()
        return buffer!!
    }

    @Throws(IOException::class)
    fun getFileAsString(filename: String): String {
        val fileSource = StringBuilder()
        val reader = BufferedReader(InputStreamReader(IOUtils::class.java.classLoader.getResourceAsStream(filename)))

        reader.useLines {
            it.forEach {
                fileSource.append(it).append("\n")
            }
        }

//        while ((line = reader.readLine()) != null) {
//            fileSource.append(line).append("\n")
//        }

        return fileSource.toString()
    }

    @Throws(IOException::class)
    fun getFileAsStringAbsolute(filename: String): String {
        return Files.readAllLines(Paths.get(filename)).stream().reduce("") { a, b -> a + "\n" + b }
    }

    /**
     * From org.lwjgl.demo.util.IOUtil.resizeBuffer
     */
    fun resizeByteBuffer(buffer: ByteBuffer, capacity: Int): ByteBuffer {
        val newBuffer = BufferUtils.createByteBuffer(capacity)
        buffer.flip()
        newBuffer.put(buffer)
        return newBuffer
    }
}
