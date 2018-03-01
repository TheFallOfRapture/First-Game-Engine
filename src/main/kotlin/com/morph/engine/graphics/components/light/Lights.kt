package com.morph.engine.graphics.components.light

import com.morph.engine.entities.Component
import com.morph.engine.graphics.Color
import com.morph.engine.math.Vector3f
import com.morph.engine.math.Quaternion

sealed class Light(val brightness: Float, val color: Color)
class DirectionalLight(brightness: Float, color: Color) : Light(brightness, color)
class PointLight(brightness: Float, color: Color, val localPosition: Vector3f) : Light(brightness, color), Component
class SpotLight(brightness: Float, color: Color, val localPosition: Vector3f, val localRotation: Quaternion, val angle: Float) : Light(brightness, color), Component