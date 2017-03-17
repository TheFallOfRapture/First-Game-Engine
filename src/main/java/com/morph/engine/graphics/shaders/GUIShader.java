package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/6/2017.
 */
public class GUIShader extends Shader<GUIShaderUniforms> {
    public GUIShader() {
        super("shaders/basicTextured");
    }

    @Override
    protected void initUniforms(Shader<GUIShaderUniforms> shader) {
        uniforms = new GUIShaderUniforms();
        uniforms.init(shader);
    }
}
