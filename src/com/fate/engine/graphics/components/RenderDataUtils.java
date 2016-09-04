package com.fate.engine.graphics.components;

import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.Texture;
import com.fate.engine.math.Vector2f;

public final class RenderDataUtils {
	public static RenderData createSquare(Color c, Shader<?> shader, Texture texture) {
		RenderData result = new RenderData(shader, texture);
		result.addVertex(new Vector2f(-0.5f, -0.5f), c, new Vector2f(0, 1));
		result.addVertex(new Vector2f(-0.5f, 0.5f), c, new Vector2f(0, 0));
		result.addVertex(new Vector2f(0.5f, 0.5f), c, new Vector2f(1, 0));
		result.addVertex(new Vector2f(0.5f, -0.5f), c, new Vector2f(1, 1));
		
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
