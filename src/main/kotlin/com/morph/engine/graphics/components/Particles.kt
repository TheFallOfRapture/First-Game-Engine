package com.morph.engine.graphics.components

import com.morph.engine.entities.Component
import com.morph.engine.graphics.Color
import com.morph.engine.graphics.shaders.Shader
import com.morph.engine.math.Vector3f

class Particle(val color : Color, val emitter : Emitter) : Component() {
    var age = 0f
}
class Emitter(
        val color : Color,
        val spawnRate : Float,
        val velocity : Vector3f,
        val lifetime : Float,
        val shader : Shader<*>,
        private val particles: MutableList<Particle> = mutableListOf()
) : Component(), MutableList<Particle> by particles {
    var age = 0f
}