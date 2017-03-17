package com.morph.engine.graphics.shaders;

import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.Uniforms;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.physics.components.Transform;

/**
 * Created by Fernando on 3/8/2017.
 */
public class TintShaderUniforms extends Uniforms {
    private Matrix4f mvp;
    private Texture diffuse;

    @Override
    public void defineUniforms(int shader) {
        addUniform("mvp", shader);
        addUniform("diffuse", shader);
        addUniform("diffuseColor", shader);
    }

    @Override
    public void setUniforms(Transform t, RenderData data) {
        mvp = t.getTransformationMatrix();
        diffuse = data.getTexture(0);

        setUniformMatrix4fv("mvp", mvp.mul(GLRenderingEngine.projectionMatrix).getTranspose());
        setUniform1i("diffuse", 0);
        setUniform4f("diffuseColor", data.getTint());

        diffuse.bind();
    }

    @Override
    public void unbind(Transform t, RenderData data) {
        diffuse.unbind();
    }
}
