package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.Shader;

/**
 * Created by Fernando on 3/6/2017.
 */
public class GUITextShader extends Shader<GUITextShaderUniforms> {
    public GUITextShader() {
        super("shaders/text");
    }

    protected void initUniforms(Shader<GUITextShaderUniforms> shader) {
        uniforms = new GUITextShaderUniforms();
        uniforms.init(shader);
    }
}
