package com.morph.engine.graphics.components.light

import com.morph.engine.entities.Component
import com.morph.engine.graphics.Color
import com.morph.engine.math.Quaternion
import com.morph.engine.math.Vector3f

sealed class Light(var brightness: Float, var color: Color)
class DirectionalLight(brightness: Float, color: Color) : Light(brightness, color)
class PointLight(brightness: Float, color: Color, var localPosition: Vector3f) : Light(brightness, color), Component
class SpotLight(brightness: Float, color: Color, var localPosition: Vector3f, var localRotation: Quaternion, var angle: Float) : Light(brightness, color), Component