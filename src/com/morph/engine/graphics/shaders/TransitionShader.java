package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/2/2017.
 */
public class TransitionShader extends Shader<TransitionShaderUniforms> {
    public TransitionShader() {
        super("shaders/transition");
    }

    @Override
    protected void initUniforms(Shader<TransitionShaderUniforms> shader) {
        uniforms = new TransitionShaderUniforms();
        uniforms.init(shader);
    }
}
