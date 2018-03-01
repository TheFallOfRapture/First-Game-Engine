package com.morph.engine.graphics.components.light

import com.morph.engine.entities.Component
import com.morph.engine.graphics.Color
import com.morph.engine.math.Quaternion
import com.morph.engine.math.Vector3f
import com.morph.engine.physics.components.Transform

sealed class Light(var brightness: Float, var color: Color)
class DirectionalLight(brightness: Float, color: Color) : Light(brightness, color)

sealed class SceneLight(brightness: Float, color: Color, var parentTransform: Transform) : Light(brightness, color), Component

class PointLight @JvmOverloads constructor(
        brightness: Float,
        color: Color,
        parentTransform: Transform = Transform.identity(),
        var localPosition: Vector3f
) : SceneLight(brightness, color, parentTransform)

class SpotLight @JvmOverloads constructor(
        brightness: Float,
        color: Color,
        parentTransform: Transform = Transform.identity(),
        var localPosition: Vector3f,
        var localRotation: Quaternion,
        var angle: Float
) : SceneLight(brightness, color, parentTransform)