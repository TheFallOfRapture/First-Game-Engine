package com.morph.engine.graphics

import com.morph.engine.math.Vector2f
import com.morph.engine.math.Vector3f

data class Vertex @JvmOverloads constructor(var position: Vector3f, var color: Color = Color(1f, 1f, 1f), var texCoord: Vector2f = Vector2f(0f, 0f)) {
    @JvmOverloads constructor(position: Vector2f, color: Color = Color(1f, 1f, 1f), texCoord: Vector2f = Vector2f(0f, 0f))
        : this(Vector3f(position, 0f), color, texCoord)

    constructor(position: Vector2f, texCoord: Vector2f) : this(position, Color(1f, 1f, 1f), texCoord)
}
