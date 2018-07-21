package com.morph.engine.core

import com.morph.engine.core.Game.GameAction.*
import com.morph.engine.entities.Entity
import io.reactivex.disposables.Disposable

abstract class GameSystem(val game: Game) {
    private var gameLinkHandle : Disposable? = null

    fun link(game: Game) {
        gameLinkHandle = game.events.subscribe {
            when (it) {
                PRE_UPDATE -> preUpdate(game)
                UPDATE -> update(game)
                FIXED_UPDATE -> fixedUpdate(game, game.dt)
                POST_UPDATE -> postUpdate(game)
                else -> {}
            }
        }
    }

    fun unlink() {
        gameLinkHandle?.dispose()
    }

    protected abstract fun acceptEntity(e: Entity): Boolean

    abstract fun initSystem()

    private fun preUpdate(game: Game) {
        game.getWorld().entities.filter(this::acceptEntity).forEach(this::preUpdate)
        systemPreUpdate()
    }

    private fun update(game: Game) {
        game.getWorld().entities.filter(this::acceptEntity).forEach(this::update)
        systemUpdate()
    }

    private fun fixedUpdate(game: Game, dt: Float) {
        game.getWorld().entities.filter(this::acceptEntity).forEach { fixedUpdate(it, dt) }
        systemFixedUpdate(dt)
    }

    private fun postUpdate(game: Game) {
        game.getWorld().entities.filter(this::acceptEntity).forEach(this::postUpdate)
        systemPostUpdate()
    }

    protected open fun preUpdate(e: Entity) {}
    protected open fun update(e: Entity) {}
    protected open fun fixedUpdate(e: Entity, dt: Float) {}
    protected open fun postUpdate(e: Entity) {}

    protected open fun systemPreUpdate() {}
    protected open fun systemUpdate() {}
    protected open fun systemFixedUpdate(dt: Float) {}
    protected open fun systemPostUpdate() {}
}
