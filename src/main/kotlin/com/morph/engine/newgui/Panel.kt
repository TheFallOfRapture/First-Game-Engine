package com.morph.engine.newgui

import com.morph.engine.graphics.Color
import com.morph.engine.graphics.Texture
import com.morph.engine.graphics.shaders.GUITintShader
import com.morph.engine.math.Vector2f
import com.morph.engine.physics.components.Transform2D
import com.morph.engine.util.RenderDataUtils

class Panel(
        name: String,
        position: Vector2f,
        size: Vector2f,
        color: Color = Color(0f, 0f, 0f),
        texture: Texture
) : Container(
        name,
        Transform2D(position + (size * 0.5f), scale = size),
        RenderDataUtils.createTintedSquare(color, GUITintShader(), texture)
)
