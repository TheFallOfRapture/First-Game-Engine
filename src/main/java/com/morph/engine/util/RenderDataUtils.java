package com.morph.engine.util;

import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;

import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Shader;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.Vertex;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Vector2f;

public final class RenderDataUtils {
	public static RenderData createTintedSquare(Shader<?> shader, Texture texture) {
		RenderData data = RenderDataUtils.createSquare(shader, texture);
		data.setTint(new Color(1, 1, 1));

		return data;
	}

	public static RenderData createTintedSquare(Color c, Shader<?> shader, Texture texture) {
		RenderData data = RenderDataUtils.createSquare(new Color(1, 1, 1), shader, texture);
		data.setTint(c);

		return data;
	}

	public static RenderData createTintedSquare(Color c, Shader<?> shader, Texture texture, Texture altTexture, float lerpFactor) {
		RenderData data = RenderDataUtils.createSquare(new Color(1, 1, 1), shader, texture, altTexture, lerpFactor);
		data.setTint(c);

		return data;
	}

	public static RenderData createSquare(Color c, Shader<?> shader, Texture texture, Texture altTexture, float lerpFactor) {
		RenderData result = new RenderData(shader, texture);
		result.setTexture(altTexture, 1);
		result.setLerpFactor(lerpFactor);
		result.addVertex(new Vector2f(-0.5f, -0.5f), c, new Vector2f(0, 1));
		result.addVertex(new Vector2f(-0.5f, 0.5f), c, new Vector2f(0, 0));
		result.addVertex(new Vector2f(0.5f, 0.5f), c, new Vector2f(1, 0));
		result.addVertex(new Vector2f(0.5f, -0.5f), c, new Vector2f(1, 1));

		result.addIndices(0, 1, 3, 1, 2, 3);

		return result;
	}

	public static RenderData createSquare(Color c, Shader<?> shader, Texture texture) {
		RenderData result = new RenderData(shader, texture);
		result.addVertex(new Vector2f(-0.5f, -0.5f), c, new Vector2f(0, 1));
		result.addVertex(new Vector2f(-0.5f, 0.5f), c, new Vector2f(0, 0));
		result.addVertex(new Vector2f(0.5f, 0.5f), c, new Vector2f(1, 0));
		result.addVertex(new Vector2f(0.5f, -0.5f), c, new Vector2f(1, 1));
		
		result.addIndices(0, 1, 3, 1, 2, 3);
		
		return result;
	}

	public static RenderData createSquare(Shader<?> shader, Texture texture, Texture altTexture, float lerpFactor) {
		RenderData result = new RenderData(shader, texture);
		result.setTexture(altTexture, 1);
		result.setLerpFactor(lerpFactor);
		result.addVertex(new Vector2f(-0.5f, -0.5f), new Vector2f(0, 1));
		result.addVertex(new Vector2f(-0.5f, 0.5f), new Vector2f(0, 0));
		result.addVertex(new Vector2f(0.5f, 0.5f), new Vector2f(1, 0));
		result.addVertex(new Vector2f(0.5f, -0.5f), new Vector2f(1, 1));

		result.addIndices(0, 1, 3, 1, 2, 3);

		return result;
	}
	
	public static RenderData createSquare(Shader<?> shader, Texture texture) {
		RenderData result = new RenderData(shader, texture);
		result.addVertex(new Vector2f(-0.5f, -0.5f), new Vector2f(0, 1));
		result.addVertex(new Vector2f(-0.5f, 0.5f), new Vector2f(0, 0));
		result.addVertex(new Vector2f(0.5f, 0.5f), new Vector2f(1, 0));
		result.addVertex(new Vector2f(0.5f, -0.5f), new Vector2f(1, 1));
		
		result.addIndices(0, 1, 3, 1, 2, 3);
		
		return result;
	}
	
// Reference: LWJGL 3 STB Truetype Demos	
	
	public static RenderData createText(String text, String font, int size, Color color, Shader<?> shader) {
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Integer> indices = new ArrayList<Integer>();
		int bitmapWidth = 1024, bitmapHeight = 1024;
		
		int temp_texture = glGenTextures();
		STBTTBakedChar.Buffer chars = STBTTBakedChar.malloc(96);
		
		try {
			ByteBuffer fontBuffer = IOUtils.getFileAsByteBuffer(font, 160 * 1024);
			ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
			
			STBTruetype.stbtt_BakeFontBitmap(fontBuffer, size, bitmap, bitmapWidth, bitmapHeight, 32, chars);
			
			glBindTexture(GL_TEXTURE_2D, temp_texture);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, bitmapWidth, bitmapHeight, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer x = stack.floats(0.0f);
			FloatBuffer y = stack.floats(0.0f);
			STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c == '\n') {
					y.put(0, y.get(0) - size);
					x.put(0, 0);
				} else if (c < 32 || c >= 128)
					continue;
				
				STBTruetype.stbtt_GetBakedQuad(chars, bitmapWidth, bitmapHeight, c - 32, x, y, q, true);
				
				vertices.add(new Vertex(new Vector2f(q.x0(), -q.y0()), color, new Vector2f(q.s0(), q.t0())));
				vertices.add(new Vertex(new Vector2f(q.x1(), -q.y0()), color, new Vector2f(q.s1(), q.t0())));
				vertices.add(new Vertex(new Vector2f(q.x1(), -q.y1()), color, new Vector2f(q.s1(), q.t1())));
				vertices.add(new Vertex(new Vector2f(q.x0(), -q.y1()), color, new Vector2f(q.s0(), q.t1())));

				indices.add(0 + (i * 4));
				indices.add(1 + (i * 4));
				indices.add(3 + (i * 4));
				indices.add(1 + (i * 4));
				indices.add(2 + (i * 4));
				indices.add(3 + (i * 4));
			}
		}
		
		Texture textureAtlas = new Texture(font, size, bitmapWidth, bitmapHeight);
		RenderData result = new RenderData(shader, textureAtlas, vertices, indices);
		result.setTint(color);
		return result;
	}
	
	public static STBTTBakedChar.Buffer loadChars(String font, int size, int bitmapWidth, int bitmapHeight) {
		int texture = glGenTextures();
		STBTTBakedChar.Buffer chars = STBTTBakedChar.malloc(96);
		
		try {
			ByteBuffer fontBuffer = IOUtils.getFileAsByteBuffer(font, 160 * 1024);
			ByteBuffer bitmap = BufferUtils.createByteBuffer(bitmapWidth * bitmapHeight);
			
			STBTruetype.stbtt_BakeFontBitmap(fontBuffer, size, bitmap, bitmapWidth, bitmapHeight, 32, chars);
			
			glBindTexture(GL_TEXTURE_2D, texture);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, bitmapWidth, bitmapHeight, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return chars;
	}
}
