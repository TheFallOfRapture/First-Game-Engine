package com.morph.engine.util;

import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.shaders.Shader;
import com.morph.engine.graphics.Texture;
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
}
