package com.fate.game.shooting.graphics.shaders;

import com.fate.engine.graphics.Shader;

public class BasicTexturedShader extends Shader<BasicTexturedShaderUniforms> {
	public BasicTexturedShader() {
		super("shaders/basicTextured");
	}
	
	protected void initUniforms(Shader<BasicTexturedShaderUniforms> shader) {
		uniforms = new BasicTexturedShaderUniforms();
		uniforms.init(shader);
	}
}
