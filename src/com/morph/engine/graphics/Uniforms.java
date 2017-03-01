package com.morph.engine.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import com.morph.engine.entities.Entity;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.Vector3f;
import com.morph.engine.math.Vector4f;
import com.morph.engine.physics.components.Transform;

public abstract class Uniforms {
	private HashMap<String, Integer> uniforms;
	protected Shader<?> shader;
	
	public void init(Shader<?> shader) {
		uniforms = new HashMap<String, Integer>();
		this.shader = shader;
	}
	
	public abstract void defineUniforms(int shader);
	public abstract void setUniforms(Transform t, RenderData data);
	public abstract void unbind(Transform t, RenderData data);
	
	protected void addUniform(String name, int shader) {
		int location = glGetUniformLocation(shader, name);
		uniforms.put(name, location);
	}
	
	public void setUniform1i(String name, int value) {
		int location = uniforms.get(name);
		glUniform1i(location, value);
	}
	
	public void setUniform3f(String name, Vector3f value) {
		int location = uniforms.get(name);
		glUniform3f(location, value.getX(), value.getY(), value.getZ());
	}
	
	public void setUniform3f(String name, Color value) {
		int location = uniforms.get(name);
		glUniform3f(location, value.getRed(), value.getGreen(), value.getBlue());
	}
	
	public void setUniform4f(String name, Vector4f value) {
		int location = uniforms.get(name);
		glUniform4f(location, value.getX(), value.getY(), value.getZ(), value.getW());
	}
	
	public void setUniform4f(String name, Color value) {
		int location = uniforms.get(name);
		glUniform4f(location, value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
	}
	
	public void setUniformMatrix4fv(String name, Matrix4f value) {
		int location = uniforms.get(name);
		glUniformMatrix4fv(location, false, value.asArray());
	}
}
