package com.morph.engine.graphics;

import com.morph.engine.core.Game;
import com.morph.engine.core.GameSystem;
import com.morph.engine.entities.Entity;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.newgui.Element;
import com.morph.engine.physics.components.Transform;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.tiles.Tile;
import com.morph.engine.tiles.TileEmpty;
import com.morph.engine.tiles.Tilemap;

import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class GLRenderingEngine extends GameSystem {
	public static Matrix4f projectionMatrix = new Matrix4f();
	private List<Entity> gameRenderables;
	private List<Element> guiRenderables;

	public GLRenderingEngine(Game game) {
		super(game);
		this.gameRenderables = new ArrayList<>();
		this.guiRenderables = new ArrayList<>();
	}

	public void render(RenderData data, Transform transform) {
		if (data == null || transform == null)
			return;

		data.getShader().bind();
		data.getShader().getUniforms().setUniforms(transform, data);

		glBindVertexArray(data.getVertexArrayObject());
		glDrawElements(GL_TRIANGLES, data.getIndices().size(), GL_UNSIGNED_INT, NULL);
		glBindVertexArray(0);

		data.getShader().getUniforms().unbind(transform, data);
		data.getShader().unbind();
	}

	private void render(Entity e) {
		render(e.getComponent(RenderData.class), e.getComponent(Transform.class));
	}

	private void render(Element e) {
		render(e.getRenderData(), e.getTransform());
	}

	public void register(Entity e) {
		gameRenderables.add(0, e);
	}

	public boolean unregister(Entity e) {
		return gameRenderables.remove(e);
	}

	public void register(Element e) {
		guiRenderables.add(0, e);
	}

	public void unregister(Element e) {
		guiRenderables.remove(e);
	}

	public void render(GLDisplay display) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		gameRenderables.forEach(this::render);
//		System.out.println("There are " + gameRenderables.size() + " entities currently being rendered.");
		guiRenderables.sort((e1, e2) -> e2.getDepth() - e1.getDepth());
		guiRenderables.forEach(this::render);

		display.update();
	}

	public void setClearColor(Color clearColor) {
		glClearColor(clearColor.getRed(), clearColor.getGreen(), clearColor.getBlue(), clearColor.getAlpha());
	}

	@Override
	public void initSystem() {
		glActiveTexture(GL_TEXTURE0);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	// TODO: Replace projection matrix loading with better implementation
	public static void setProjectionMatrix(Matrix4f m) {
		GLRenderingEngine.projectionMatrix = m;
	}

	public static Matrix4f getProjectionMatrix() {
		return GLRenderingEngine.projectionMatrix;
	}

	public void setClearColor(float r, float g, float b, float a) {
		glClearColor(r, g, b, a);
	}

	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponents(RenderData.class, Transform.class);
	}
}
