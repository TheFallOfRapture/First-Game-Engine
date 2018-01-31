package com.morph.engine.graphics.components;

import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import com.morph.engine.entities.Component;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.shaders.Shader;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.shaders.Uniforms;
import com.morph.engine.graphics.Vertex;
import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector3f;
import com.morph.engine.util.RenderDataUtilsKt;
import kotlin.Unit;

public class RenderData extends Component {
	protected List<Vertex> vertices;
	protected List<Integer> indices;
	protected List<Texture> textures;
	
	protected Shader<?> shader;
	protected Color tint = new Color(1, 1, 1);

	protected float lerpFactor;

	protected int vbo, cbo, tbo, ibo;
	protected int vao;

	private boolean initialized = false;
	
	public RenderData(Shader<?> shader, Texture texture) {
		this.vertices = new ArrayList<>();
		this.indices = new ArrayList<>();
		this.textures = new ArrayList<>();

		this.shader = shader;

		this.textures.add(texture);

		shader.init();
	}
	
	public RenderData(Shader<?> shader, Texture texture, List<Vertex> vertices, List<Integer> indices) {
		this.vertices = vertices;
		this.indices = indices;
		this.textures = new ArrayList<>();

		this.shader = shader;
		this.textures.add(texture);

		shader.init();
	}

	public RenderData(List<Vertex> vertices, List<Integer> indices, Shader<?> shader, List<Texture> textures, int vao) {
		this.vertices = vertices;
		this.indices = indices;
		this.textures = textures;

		this.shader = shader;

		this.vao = vao;

		initialized = true;
	}

	public void init() {
		vbo = glGenBuffers();
		cbo = glGenBuffers();
		tbo = glGenBuffers();
		ibo = glGenBuffers();
		
		vao = glGenVertexArrays();

		double[] pointData = getPointDataArray();
		double[] colorData = getColorDataArray();
		double[] texCoordData = getTexCoordDataArray();
		int[] indexData = getIndexDataArray();

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, pointData, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, texCoordData, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
		
		glBindVertexArray(vao);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_DOUBLE, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, cbo);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_DOUBLE, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, tbo);
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 2, GL_DOUBLE, false, 0, 0);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBindVertexArray(0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		initialized = true;
	}

	public void refreshVertices() {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, getPointDataArray(), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void refreshColors() {
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, getColorDataArray(), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void refreshTexCoords() {
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, getTexCoordDataArray(), GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void refreshIndices() {
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, getIndexDataArray(), GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void refreshData() {
		refreshVertices();
		refreshColors();
		refreshTexCoords();
		refreshIndices();
	}
	
	public void destroy() {
		shader.removeReference();
	}

	private double[] getPointDataArray() {
		return vertices
				.stream()
				.map(Vertex::getPosition)
				.flatMapToDouble(
						p -> DoubleStream.of(
								(double)p.getX(),
								(double)p.getY(),
								(double)p.getZ()))
				.toArray();
	}

	private double[] getColorDataArray() {
		return vertices
				.stream()
				.map(Vertex::getColor)
				.flatMapToDouble(
						c -> DoubleStream.of(
								(double)c.getRed(),
								(double)c.getGreen(),
								(double)c.getBlue(),
								(double)c.getAlpha()))
				.toArray();
	}

	private double[] getTexCoordDataArray() {
		return vertices
				.stream()
				.map(Vertex::getTexCoord)
				.flatMapToDouble(
						t -> DoubleStream.of(
								(double)t.getX(),
								(double)t.getY()))
				.toArray();
	}

	private int[] getIndexDataArray() {
		return indices.stream().mapToInt(Integer::intValue).toArray();
	}

	public void addVertices(Vertex... vertices) {
		updateAll(data -> data.vertices.addAll(Arrays.asList(vertices)));
	}
	
	public void addIndices(int... indices) {
		updateIndices(data -> data.loadIndices(indices));
	}

	public void loadIndices(int... indices) {
		this.indices.addAll(Arrays.stream(indices).boxed().collect(Collectors.toList()));
	}
	
	public void addVertex(Vertex v, int index) {
		vertices.add(v);
		indices.add(index);
	}
	
	public void addVertex(Vertex v) {
		vertices.add(v);
	}
	
	public void addVertex(Vector2f position, Color color, Vector2f texCoord) {
		vertices.add(new Vertex(position, color, texCoord));
	}
	
	public void addVertex(Vector2f position, Color color, int index) {
		vertices.add(new Vertex(position, color));
		indices.add(index);
	}

	public void addVertex(Vector2f position, Vector2f texCoord) {
		vertices.add(new Vertex(position, texCoord));
	}
	
	public void addVertex(Vector2f position, Vector2f texCoord, int index) {
		vertices.add(new Vertex(position, texCoord));
		indices.add(index);
	}
	
	public void addVertex(Vector3f position, Color color) {
		vertices.add(new Vertex(position, color));
	}
	
	public void addVertex(Vector2f position, Color color) {
		vertices.add(new Vertex(position, color));
	}
	
	public void addVertex(Vector3f position, int index) {
		vertices.add(new Vertex(position, new Color(1, 1, 1)));
		indices.add(index);
	}
	
	public void addVertex(Vector2f position, int index) {
		vertices.add(new Vertex(position, new Color(1, 1, 1)));
		indices.add(index);
	}
	
	public void addVertex(Vector3f position) {
		vertices.add(new Vertex(position, new Color(1, 1, 1)));
	}
	
	public void addVertex(Vector2f position) {
		vertices.add(new Vertex(position, new Color(1, 1, 1)));
	}

	public void removeVertexAtPosition(int i) {
		vertices.remove(i);
	}
	
	public void setVertex(int index, Vertex v) {
		vertices.set(index, v);
	}

	public void setColor(int index, Color c) {
		vertices.get(index).setColor(c);
	}

	public void setTint(Color tint) {
		this.tint = tint;
	}
	
	public void setPosition(int index, Vector3f v) {
		vertices.get(index).setPosition(v);
	}
	
	public void addIndex(int index) {
		indices.add(index);
	}

	public void removeIndexAtPosition(int i) {
		indices.remove(i);
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	
	public List<Integer> getIndices() {
		return indices;
	}
	
	public void setVertexArrayObject(int buffer) {
		this.vao = buffer;
	}

	public int getVertexArrayObject() {
		return vao;
	}
	
	public Shader<? extends Uniforms> getShader() {
		return shader;
	}
	
	public Texture getTexture(int index) {
		return textures.get(index);
	}
	
	public void setTexture(Texture texture, int index) {
		if (textures.size() < index + 1) {
			for (int i = 0; i < index; i++) {
				textures.add(null);
			}
		}

		textures.set(index, texture);
	}

	public float getLerpFactor() {
		return lerpFactor;
	}

	public void setLerpFactor(float lerpFactor) {
		this.lerpFactor = lerpFactor;
	}

	public Color getTint() {
		return tint;
	}

	public void resetAllColors(Color c) {
		vertices.stream().map(Vertex::getColor).forEach(color -> color.setRGB(c));
		init();
	}

	public void updateAll(Consumer<? super RenderData> body) {
		RenderDataUtilsKt.updateAll(this, data -> {
			body.accept(data);
			return Unit.INSTANCE;
		});
	}

	public void updateVertices(Consumer<? super RenderData> body) {
		RenderDataUtilsKt.updateVertices(this, data -> {
			body.accept(data);
			return Unit.INSTANCE;
		});
	}

	public void updateColors(Consumer<? super RenderData> body) {
		RenderDataUtilsKt.updateColors(this, data -> {
			body.accept(data);
			return Unit.INSTANCE;
		});
	}

	public void updateTexCoords(Consumer<? super RenderData> body) {
		RenderDataUtilsKt.updateTexCoords(this, data -> {
			body.accept(data);
			return Unit.INSTANCE;
		});
	}

	public void updateIndices(Consumer<? super RenderData> body) {
		RenderDataUtilsKt.updateIndices(this, data -> {
			body.accept(data);
			return Unit.INSTANCE;
		});
	}

	@Override
	public Component clone() {
		return new RenderData(vertices, indices, shader, textures, vao);
	}
}
