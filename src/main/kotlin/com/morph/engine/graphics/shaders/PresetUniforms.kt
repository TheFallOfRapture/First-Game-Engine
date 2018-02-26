package com.morph.engine.graphics.shaders

import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.graphics.components.RenderData
import com.morph.engine.math.Matrix4f
import com.morph.engine.physics.components.Transform

class BasicTexturedShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diffuse: Texture

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diffuse", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        mvp = t.transformationMatrix
        diffuse = data.getTexture(0)

        setUniformMatrix4fv("mvp", (mvp * world).transpose)
        setUniform1i("diffuse", 0)

        diffuse.bind()
    }

    override fun unbind(t: Transform, data: RenderData) {
        diffuse.unbind()
    }
}

class GUIShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diffuse: Texture

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diffuse", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        mvp = t.transformationMatrix
        diffuse = data.getTexture(0)

        setUniformMatrix4fv("mvp", (mvp * screen).transpose)
        setUniform1i("diffuse", 0)

        diffuse.bind()
    }

    override fun unbind(t: Transform, data: RenderData) {
        diffuse.unbind()
    }
}

class GUITextShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diffuse: Texture

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diffuse", shader)
        addUniform("diffuseColor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        mvp = t.transformationMatrix
        diffuse = data.getTexture(0)

        setUniformMatrix4fv("mvp", (mvp * screen).transpose)
        setUniform1i("diffuse", 0)
        setUniform3f("diffuseColor", data.tint)

        diffuse.bind()
    }

    override fun unbind(t: Transform, data: RenderData) {
        diffuse.unbind()
    }
}

class GUITintShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diffuse: Texture

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diffuse", shader)
        addUniform("diffuseColor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        mvp = t.transformationMatrix
        diffuse = data.getTexture(0)

        setUniformMatrix4fv("mvp", (mvp * screen).transpose)
        setUniform1i("diffuse", 0)
        setUniform4f("diffuseColor", data.tint)

        diffuse.bind()
    }

    override fun unbind(t: Transform, data: RenderData) {
        diffuse.unbind()
    }
}

class GUITintTransitionShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diff1: Texture
    private lateinit var diff2: Texture
    private lateinit var diffuseColor: Color
    private var lerpFactor: Float = 0f

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diff1", shader)
        addUniform("diff2", shader)
        addUniform("diffuseColor", shader)
        addUniform("lerpFactor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        this.mvp = t.transformationMatrix
        this.diff1 = data.getTexture(0)
        this.diff2 = data.getTexture(1)
        this.diffuseColor = data.tint
        this.lerpFactor = data.lerpFactor

        setUniformMatrix4fv("mvp", (mvp * screen).transpose)
        setUniform1i("diff1", 0)
        setUniform1i("diff2", 1)
        setUniform4f("diffuseColor", diffuseColor)
        setUniform1f("lerpFactor", lerpFactor)

        diff1.bind(0)
        diff2.bind(1)
    }

    override fun unbind(t: Transform, data: RenderData) {
        diff1.unbind()
        diff2.unbind()
    }
}

class GUITransitionShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diff1: Texture
    private lateinit var diff2: Texture
    private var lerpFactor: Float = 0f

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diff1", shader)
        addUniform("diff2", shader)
        addUniform("lerpFactor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        this.mvp = t.transformationMatrix
        this.diff1 = data.getTexture(0)
        this.diff2 = data.getTexture(1)
        this.lerpFactor = data.lerpFactor

        setUniformMatrix4fv("mvp", (mvp * screen).transpose)
        setUniform1i("diff1", 0)
        setUniform1i("diff2", 1)
        setUniform1f("lerpFactor", lerpFactor)

        diff1.bind(0)
        diff2.bind(1)
    }

    override fun unbind(t: Transform, data: RenderData) {
        diff1.unbind()
        diff2.unbind()
    }
}

class TextShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diffuse: Texture

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diffuse", shader)
        addUniform("diffuseColor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        mvp = t.transformationMatrix
        diffuse = data.getTexture(0)

        setUniformMatrix4fv("mvp", (mvp * world).transpose)
        setUniform1i("diffuse", 0)
        setUniform3f("diffuseColor", data.tint)

        diffuse.bind()
    }

    override fun unbind(t: Transform, data: RenderData) {
        diffuse.unbind()
    }
}

class TintShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diffuse: Texture

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diffuse", shader)
        addUniform("diffuseColor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        mvp = t.transformationMatrix
        diffuse = data.getTexture(0)

        setUniformMatrix4fv("mvp", (mvp * world).transpose)
        setUniform1i("diffuse", 0)
        setUniform4f("diffuseColor", data.tint)

        diffuse.bind()
    }

    override fun unbind(t: Transform, data: RenderData) {
        diffuse.unbind()
    }
}

class TransitionShaderUniforms : Uniforms() {
    private lateinit var mvp: Matrix4f
    private lateinit var diff1: Texture
    private lateinit var diff2: Texture
    private var lerpFactor: Float = 0f

    override fun defineUniforms(shader: Int) {
        addUniform("mvp", shader)
        addUniform("diff1", shader)
        addUniform("diff2", shader)
        addUniform("lerpFactor", shader)
    }

    override fun setUniforms(t: Transform, data: RenderData, world: Matrix4f, screen: Matrix4f) {
        this.mvp = t.transformationMatrix
        this.diff1 = data.getTexture(0)
        this.diff2 = data.getTexture(1)
        this.lerpFactor = data.lerpFactor

        setUniformMatrix4fv("mvp", (mvp * world).transpose)
        setUniform1i("diff1", 0)
        setUniform1i("diff2", 1)
        setUniform1f("lerpFactor", lerpFactor)

        diff1.bind(0)
        diff2.bind(1)
    }

    override fun unbind(t: Transform, data: RenderData) {
        diff1.unbind()
        diff2.unbind()
    }
}


