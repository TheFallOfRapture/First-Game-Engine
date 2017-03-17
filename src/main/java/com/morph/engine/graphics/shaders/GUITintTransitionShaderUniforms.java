package com.morph.engine.graphics.shaders;

import com.morph.engine.core.Game;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.Uniforms;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.physics.components.Transform;

/**
 * Created by Fernando on 3/8/2017.
 */
public class GUITintTransitionShaderUniforms extends Uniforms {
    private Matrix4f mvp;
    private Texture diff1;
    private Texture diff2;
    private Color diffuseColor;
    private float lerpFactor;

    @Override
    public void defineUniforms(int shader) {
        addUniform("mvp", shader);
        addUniform("diff1", shader);
        addUniform("diff2", shader);
        addUniform("diffuseColor", shader);
        addUniform("lerpFactor", shader);
    }

    @Override
    public void setUniforms(Transform t, RenderData data) {
        this.mvp = t.getTransformationMatrix();
        this.diff1 = data.getTexture(0);
        this.diff2 = data.getTexture(1);
        this.diffuseColor = data.getTint();
        this.lerpFactor = data.getLerpFactor();

        setUniformMatrix4fv("mvp", mvp.mul(Game.screenOrtho).getTranspose());
        setUniform1i("diff1", 0);
        setUniform1i("diff2", 1);
        setUniform4f("diffuseColor", diffuseColor);
        setUniform1f("lerpFactor", lerpFactor);

        diff1.bind(0);
        diff2.bind(1);
    }

    @Override
    public void unbind(Transform t, RenderData data) {
        diff1.unbind();
        diff2.unbind();
    }
}
