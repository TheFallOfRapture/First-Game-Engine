package com.fate.engine.graphics.components;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.fate.engine.entities.Component;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Shader;
import com.fate.engine.graphics.Texture;
import com.fate.engine.graphics.Uniforms;
import com.fate.engine.graphics.Vertex;
import com.fate.engine.math.Vector2f;
import com.fate.engine.math.Vector3f;

public class RenderData extends Component {
	protected List<Vertex> vertices;
	protected List<Integer> indices;
	
	protected Shader<?> shader;
	protected Texture texture;
	
	protected int vao;
	
	public RenderData(Shader<?> shader, Texture texture) {
		this.vertices = new ArrayList<Vertex>();
		this.indices = new ArrayList<Integer>();
		this.shader = shader;
		this.texture = texture;
		
		shader.init();
	}
	
	public RenderData(Shader<?> shader, Texture texture, List<Vertex> vertices, List<Integer> indices) {
		this.vertices = vertices;
		this.indices = indices;
		this.shader = shader;
		this.texture = texture;
		
		shader.init();
	}
	
	public void init() {
		int vbo = glGenBuffers();
		int cbo = glGenBuffers();
		int tbo = glGenBuffers();
		int ibo = glGenBuffers();
		
		vao = glGenVertexArrays();
		
		IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.size());
		indices.forEach(indexBuffer::put);
		indexBuffer.flip();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.size() * 3);
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(vertices.size() * 4);
		FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(vertices.size() * 2);
		for (Vertex v : vertices) {
			Vector3f pos = v.getPosition();
			Color c = v.getColor();
			Vector2f tex = v.getTexCoord();
			
			vertexBuffer.put(pos.getX());
			vertexBuffer.put(pos.getY());
			vertexBuffer.put(pos.getZ());
			
			colorBuffer.put(c.getRed());
			colorBuffer.put(c.getGreen());
			colorBuffer.put(c.getBlue());
			colorBuffer.put(c.getAlpha());
			
			texCoordBuffer.put(tex.getX());
			texCoordBuffer.put(tex.getY());
		}
		
		vertexBuffer.flip();
		colorBuffer.flip();
		texCoordBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, cbo);
		glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, tbo);
		glBufferData(GL_ARRAY_BUFFER, texCoordBuffer, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
		
		glBindVertexArray(vao);
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, cbo);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, tbo);
			glEnableVertexAttribArray(2);
			glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBindVertexArray(0);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void destroy() {
		shader.removeReference();
	}
	
	public void addVertices(Vertex... vertices) {
		this.vertices.addAll(Arrays.asList(vertices));
	}
	
	public void addIndices(int... indices) {
		for (int i : indices) {
			this.indices.add(i);
		}
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
	
	public void setVertex(int index, Vertex v) {
		vertices.set(index, v);
	}

	public void setColor(int index, Color c) {
		vertices.get(index).setColor(c);
	}
	
	public void setPosition(int index, Vector3f v) {
		vertices.get(index).setPosition(v);
	}
	
	public void addIndex(int index) {
		indices.add(index);
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
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	public Component clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
