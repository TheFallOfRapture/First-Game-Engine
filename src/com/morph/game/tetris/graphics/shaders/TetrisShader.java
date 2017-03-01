package com.fate.game.tetris.graphics.shaders;

import com.fate.engine.graphics.Shader;

public class TetrisShader extends Shader<TetrisShaderUniforms> {
	public TetrisShader() {
		super("shaders/tetrisSV");
	}

	protected void initUniforms(Shader<TetrisShaderUniforms> shader) {
		uniforms = new TetrisShaderUniforms();
		uniforms.init(shader);
	}
}
