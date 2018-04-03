package com.morph.engine.graphics;

import com.morph.engine.util.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture {
	private TextureResource resource;
	private String filename;

	private static HashMap<String, TextureResource> loadedTextures = new HashMap<>();
	
	public Texture(String filename) {
		this.filename = filename != null ? filename : "textures/solid.png";
		TextureResource oldResource = loadedTextures.get(this.filename);
		if (oldResource != null) {
			this.resource = oldResource;
			resource.addReference();
		} else {
			resource = loadTexture(this.filename);
			loadedTextures.put(this.filename, resource);
		}
	}
	
	public Texture(String font, int size, int bitmapWidth, int bitmapHeight) {
		this.filename = font != null ? font : "C:/Windows/Fonts/arial.ttf";
		TextureResource oldResource = loadedTextures.get(this.filename + ":" + size);
		if (oldResource != null) {
			this.resource = oldResource;
			resource.addReference();
		} else {
			resource = loadFont(this.filename, size, bitmapWidth, bitmapHeight);
			loadedTextures.put(this.filename, resource);
		}
	}

	public Texture(String id, int width, int height, ByteBuffer pixels) {
		this.filename = id;
		TextureResource oldResource = loadedTextures.get(this.filename);
		if (oldResource != null) {
			this.resource = oldResource;
			resource.addReference();
		} else {
			resource = loadTextureFromByteBuffer(width, height, pixels);
			loadedTextures.put(this.filename, resource);
		}
	}

	private TextureResource loadTextureFromByteBuffer(int width, int height, ByteBuffer buffer) {
		int ID = glGenTextures();
		buffer.flip();

		glBindTexture(GL_TEXTURE_2D, ID);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, width, height, 0, GL_ALPHA, GL_UNSIGNED_BYTE, buffer);

		return new TextureResource(ID);
	}
	
	private TextureResource loadTexture(String filename) {
		try {
			BufferedImage image = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(filename));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
			
			boolean hasAlpha = image.getColorModel().hasAlpha();
			
			for (int y = 0; y < image.getHeight(); y++) {
				for (int x = 0; x < image.getWidth(); x++) {
					int color = pixels[x + y * image.getWidth()];
					buffer.put((byte)((color >> 16) & 0xff));
					buffer.put((byte)((color >> 8) & 0xff));
					buffer.put((byte)(color & 0xff));
					
					if (hasAlpha)
						buffer.put((byte)((color >> 24) & 0xff));
					else
						buffer.put((byte)(0xff));
				}
			}
			
			buffer.flip();
			
			int ID = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, ID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			return new TextureResource(ID);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private TextureResource loadFont(String font, int size, int bitmapWidth, int bitmapHeight) {
		int texture = glGenTextures();
		STBTTBakedChar.Buffer chars = STBTTBakedChar.malloc(96);
		
		try {
			ByteBuffer fontBuffer = IOUtils.INSTANCE.getFileAsByteBuffer(font, 160 * 1024);
			ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
			
			STBTruetype.stbtt_BakeFontBitmap(fontBuffer, size, bitmap, bitmapWidth, bitmapHeight, 32, chars);
			
			glBindTexture(GL_TEXTURE_2D, texture);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, bitmapWidth, bitmapHeight, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return new TextureResource(texture);
	}

	public void destroy() {
		resource.removeReference();
	}
	
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, resource.getId());
	}

	public void bind(int i) {
		glActiveTexture(GL_TEXTURE0 + i);
		glBindTexture(GL_TEXTURE_2D, resource.getId());
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public String getFilename() {
		return filename;
	}
}
