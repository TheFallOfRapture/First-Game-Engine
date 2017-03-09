package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/8/2017.
 */
public class TintShader extends Shader<TintShaderUniforms> {
    public TintShader() {
        super("shaders/tint");
    }

    @Override
    protected void initUniforms(Shader<TintShaderUniforms> shader) {
        uniforms = new TintShaderUniforms();
        uniforms.init(shader);
    }
}
