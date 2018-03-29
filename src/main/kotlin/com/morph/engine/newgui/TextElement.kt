package com.morph.engine.newgui

import com.morph.engine.graphics.Color
import com.morph.engine.graphics.FontManager
import com.morph.engine.graphics.LoadedFont
import com.morph.engine.graphics.components.TextRenderData
import com.morph.engine.graphics.shaders.GUITextShader
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D

/**
 * Created on 7/30/2017.
 */
open class TextElement @JvmOverloads constructor(
        name: String,
        open var text: String,
        font: String,
        var size: Int,
        position: Vector2f,
        color: Color = Color(0f, 0f, 0f),
        depth: Int = 0
) : Element(
        name,
        Transform2D(position + (Vector2f(0f, 0f)), 0f, Vector2f(size.toFloat() / LoadedFont.SIZE.toFloat(), size.toFloat() / LoadedFont.SIZE.toFloat())),
        TextRenderData(GUITextShader(), text, FontManager.loadFont(font).font, color),
        depth
) {
    val font: LoadedFont = LoadedFont(font)

    var bottomLeft: Vector2f? = null
        private set
    var topRight: Vector2f? = null
        private set

    val fontName: String
        get() = font.fontName

    final override val renderData: TextRenderData
        get() = super.renderData as TextRenderData

    init {
        if (text != "") {
            this.bottomLeft = renderData.vertices[0].position.xy
            this.topRight = renderData.vertices[renderData.vertices.size - 2].position.xy
        }
    }
}
