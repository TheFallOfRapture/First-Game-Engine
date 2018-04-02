package com.morph.engine.script

import com.morph.engine.core.Game
import com.morph.engine.entities.Entity

/**
 * Created on 7/3/2017.
 */
abstract class GameBehavior {
    private var game: Game? = null
    var name: String? = null

    fun setGame(game: Game) {
        this.game = game
    }

    // Scripting API

    // Exposes all Entity methods
    fun getEntityByName(name: String): Entity? {
        return game?.world?.getEntityByName(name)
    }

    open fun init() {} // Runs only once - on script initialization.
    open fun start() {} // Runs on script initialization, and every time the script is modified.
    fun preUpdate() {}
    fun update() {}
    fun fixedUpdate(dt: Float) {}
    fun postUpdate() {}
    fun destroy() {}
}
