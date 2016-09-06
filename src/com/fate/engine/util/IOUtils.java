package com.fate.engine.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;


public class IOUtils {
	/**
	 * From org.lwjgl.demo.util.IOUtil.ioResourceToByteBuffer
	 * @throws IOException 
	 */
	public static ByteBuffer getFileAsByteBuffer(String filename, int size) throws IOException {
		ByteBuffer buffer;
		
		Path path = Paths.get(filename);
		if (Files.isReadable(path)) {
			try (SeekableByteChannel channel = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int)channel.size() + 1);
				while (channel.read(buffer) >= 0);
			}
		} else {
			try (
					InputStream source = IOUtils.class.getClassLoader().getResourceAsStream(filename); 
					ReadableByteChannel channel = Channels.newChannel(source);
				) {
				buffer = BufferUtils.createByteBuffer(size);
				while (true) {
					int bytes = channel.read(buffer);
					if (bytes == -1)
						break;
					if (buffer.remaining() == 0)
						buffer = resizeByteBuffer(buffer, buffer.capacity() * 2);
				}
			}
		}
		
		buffer.flip();
		return buffer;
	}
	
	/**
	 * From org.lwjgl.demo.util.IOUtil.resizeBuffer
	 */
	public static ByteBuffer resizeByteBuffer(ByteBuffer buffer, int capacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(capacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;
	}
}
