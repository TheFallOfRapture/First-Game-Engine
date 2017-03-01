package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

public class BasicTexturedShader extends Shader<BasicTexturedShaderUniforms> {
	public BasicTexturedShader() {
		super("shaders/basicTextured");
	}

	protected void initUniforms(Shader<BasicTexturedShaderUniforms> shader) {
		uniforms = new BasicTexturedShaderUniforms();
		uniforms.init(shader);
	}
}
