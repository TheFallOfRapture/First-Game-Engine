package com.fate.game.shooting.graphics.shaders;

import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.graphics.Texture;
import com.fate.engine.graphics.Uniforms;
import com.fate.engine.graphics.components.RenderData;
import com.fate.engine.math.Matrix4f;
import com.fate.engine.physics.components.Transform;

public class BasicTexturedShaderUniforms extends Uniforms {
	private Matrix4f mvp;
	private Texture diffuse;
	
	@Override
	public void defineUniforms(int shader) {
		addUniform("mvp", shader);
		addUniform("diffuse", shader);
	}

	public void setUniforms(Transform t, RenderData data) {
		mvp = t.getTransformationMatrix();
		diffuse = data.getTexture();
		
		setUniformMatrix4fv("mvp", mvp.mul(GLRenderingEngine.projectionMatrix).getTranspose());
		setUniform1i("diffuse", 0);
		
		diffuse.bind();
	}
	
	public void unbind(Transform t, RenderData data) {
		diffuse.unbind();
	}
}
