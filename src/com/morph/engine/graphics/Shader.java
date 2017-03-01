package com.fate.engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.fate.engine.math.Matrix4f;
import com.fate.engine.math.Vector3f;
import com.fate.engine.math.Vector4f;

public abstract class Shader<T extends Uniforms> {
	protected T uniforms;
	private ShaderResource resource;
	private static HashMap<String, ShaderResource> loadedShaders = new HashMap<String, ShaderResource>();
	
	public Shader(String shaderURL) {
		ShaderResource oldResource = loadedShaders.get(shaderURL);
		if (oldResource != null) {
			this.resource = oldResource;
			oldResource.addReference();
		} else {
			this.resource = Shader.loadShaderProgram(shaderURL);
			loadedShaders.put(shaderURL, resource);
		}
	}
	
	public void init() {
		initUniforms(this);
		uniforms.defineUniforms(resource.getID());
	}
	
	protected abstract void initUniforms(Shader<T> shader);
	
	public static ShaderResource loadShaderProgram(String shaderURL) {
		int shaderProgramID = 0;
		try {
			int vertexShaderID = loadVertexShader(shaderURL + ".vsh");
			int fragmentShaderID = loadFragmentShader(shaderURL + ".fsh");
			shaderProgramID = glCreateProgram();
			
			glAttachShader(shaderProgramID, vertexShaderID);
			glAttachShader(shaderProgramID, fragmentShaderID);
			
			glLinkProgram(shaderProgramID);
			
			if (glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE)
				throw new RuntimeException("Failed to link shader program: " + glGetProgramInfoLog(shaderProgramID, 1024));
			
			glValidateProgram(shaderProgramID);
			
			if (glGetProgrami(shaderProgramID, GL_VALIDATE_STATUS) == GL_FALSE)
				throw new RuntimeException("Failed to validate shader program: " + glGetProgramInfoLog(shaderProgramID, 1024));
			
			glDeleteShader(vertexShaderID);
			glDeleteShader(fragmentShaderID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ShaderResource(shaderProgramID);
	}
	
	public static int loadShader(String filename, int type) throws IOException {
		int shaderID = 0;
		
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(Shader.class.getClassLoader().getResourceAsStream(filename)));
		String line;
		
		while ((line = reader.readLine()) != null) {
			shaderSource.append(line).append("\n");
		}
		
		reader.close();
			
		shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		
		glCompileShader(shaderID);
		
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
			throw new RuntimeException("Failed to initialize shader of type " + type + ": " + glGetShaderInfoLog(shaderID, 1024));
		
		return shaderID;
	}
	
	public T getUniforms() {
		return uniforms;
	}
	
	public static int loadVertexShader(String filename) throws IOException {
		return loadShader(filename, GL_VERTEX_SHADER);
	}
	
	public static int loadFragmentShader(String filename) throws IOException {
		return loadShader(filename, GL_FRAGMENT_SHADER);
	}
	
	public void bind() {
		glUseProgram(resource.getID());
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void setUniform3f(String name, Vector3f value) {
		int location = glGetUniformLocation(resource.getID(), name);
		glUniform3f(location, value.getX(), value.getY(), value.getZ());
	}
	
	public void setUniform3f(String name, Color value) {
		int location = glGetUniformLocation(resource.getID(), name);
		glUniform3f(location, value.getRed(), value.getGreen(), value.getBlue());
	}
	
	public void setUniform4f(String name, Vector4f value) {
		int location = glGetUniformLocation(resource.getID(), name);
		glUniform4f(location, value.getX(), value.getY(), value.getZ(), value.getW());
	}
	
	public void setUniform4f(String name, Color value) {
		int location = glGetUniformLocation(resource.getID(), name);
		glUniform4f(location, value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
	}
	
	public void setUniformMatrix4fv(String name, Matrix4f value) {
		int location = glGetUniformLocation(resource.getID(), name);
		glUniformMatrix4fv(location, false, value.asArray());
	}

	public void removeReference() {
		resource.removeReference();
	}

	public void setUniform1i(String name, int value) {
		int location = glGetUniformLocation(resource.getID(), name);
		glUniform1i(location, value);
	}
}
