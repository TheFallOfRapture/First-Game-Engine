package com.morph.demos.test.particles

import com.morph.engine.core.GameApplication

fun main(args: Array<String>) {
    val launcher = GameApplication(ParticleGame())
    launcher.launchGame()
}