package com.fate.game.testing;

import com.fate.engine.graphics.Uniforms;
import com.fate.engine.graphics.components.RenderData;
import com.fate.engine.math.Matrix4f;
import com.fate.engine.physics.components.Transform;

public class BasicShaderUniforms extends Uniforms {
	private Matrix4f mvp;

	@Override
	public void defineUniforms(int shader) {
		addUniform("mvp", shader);
	}
	
	public void setUniforms(Transform t, RenderData data) {
		mvp = t.getTransformationMatrix();
	}

	@Override
	public void unbind(Transform t, RenderData data) {
		// TODO Auto-generated method stub
		
	}
}
