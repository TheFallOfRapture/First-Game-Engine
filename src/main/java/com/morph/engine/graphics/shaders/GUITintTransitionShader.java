package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/8/2017.
 */
public class GUITintTransitionShader extends Shader<GUITintTransitionShaderUniforms> {
    public GUITintTransitionShader() {
        super("shaders/tintTransition");
    }

    @Override
    protected void initUniforms(Shader<GUITintTransitionShaderUniforms> shader) {
        uniforms = new GUITintTransitionShaderUniforms();
        uniforms.init(shader);
    }
}
