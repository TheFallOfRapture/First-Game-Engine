package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/8/2017.
 */
public class GUITintShader extends Shader<GUITintShaderUniforms> {
    public GUITintShader() {
        super("shaders/tint");
    }

    @Override
    protected void initUniforms(Shader<GUITintShaderUniforms> shader) {
        uniforms = new GUITintShaderUniforms();
        uniforms.init(shader);
    }
}
