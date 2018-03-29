package com.morph.engine.newgui

import com.morph.engine.graphics.Color
import com.morph.engine.graphics.LoadedFont
import com.morph.engine.graphics.Texture
import com.morph.engine.graphics.shaders.GUITintTransitionShader
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D
import com.morph.engine.util.RenderDataUtils

class Button(
        name: String,
        val text: String,
        val font: String,
        protected var size: Int,
        color: Color,
        buttonColor: Color,
        texture: Texture,
        altTexture: Texture,
        transform: Transform2D,
        depth: Int
) : Container(name, transform, RenderDataUtils.createTintedSquare(buttonColor, GUITintTransitionShader(), texture, altTexture, 0f), depth) {
    init {
        val textObj = TextElement(name + "-innerText", text, font, size,
                transform.position - (transform.scale * (Vector2f(0.5f, 0.5f))), color, depth - 1)

        val scaleRatio = size.toFloat() / LoadedFont.SIZE.toFloat()

        val xShift = (transform.scale.x - textObj.renderData.width * scaleRatio) * 0.5f
        val yShift = transform.scale.y * 0.5f - textObj.font.ascent * textObj.font.scale * 2.0f

        println(textObj.font.ascent * textObj.font.scale * scaleRatio)

        textObj.transform.translate(Vector2f(xShift, yShift))

        this.addElement(textObj)
    }
}
