package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/6/2017.
 */
public class TextShader extends Shader<TextShaderUniforms> {
    public TextShader() {
        super("shaders/text");
    }

    protected void initUniforms(Shader<TextShaderUniforms> shader) {
        uniforms = new TextShaderUniforms();
        uniforms.init(shader);
    }
}
