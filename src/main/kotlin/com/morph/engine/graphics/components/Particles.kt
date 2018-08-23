package com.morph.engine.graphics.components

import com.morph.engine.entities.Component
import com.morph.engine.graphics.Color
import com.morph.engine.math.Vector3f

class Particle(val color : Color, val age : Int) : Component()
class Emitter(val spawnRate : Float, val velocity : Vector3f, val lifetime : Float) : Component()