package com.fate.game.testing;

import com.fate.engine.graphics.Shader;

public class BasicShader extends Shader<BasicShaderUniforms> {
	public BasicShader() {
		super("shaders/basic");
	}
	
	protected void initUniforms(Shader<BasicShaderUniforms> shader) {
		uniforms = new BasicShaderUniforms();
		uniforms.init(shader);
	}
}
