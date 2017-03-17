package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/6/2017.
 */
public class GUITransitionShader extends Shader<GUITransitionShaderUniforms> {
    public GUITransitionShader() {
        super("shaders/transition");
    }

    @Override
    protected void initUniforms(Shader<GUITransitionShaderUniforms> shader) {
        uniforms = new GUITransitionShaderUniforms();
        uniforms.init(shader);
    }
}

