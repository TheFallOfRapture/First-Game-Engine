package com.morph.engine.core

import com.morph.engine.entities.Entity

abstract class GameSystem(val game: Game) {
    protected abstract fun acceptEntity(e: Entity): Boolean

    abstract fun initSystem()

    fun preUpdate(game: Game) {
        game.getWorld().entities.filter(this::acceptEntity).forEach(this::preUpdate)
        systemPreUpdate()
    }

    fun update(game: Game) {
        game.getWorld().entities.filter(this::acceptEntity).forEach(this::update)
        systemUpdate()
    }

    fun fixedUpdate(game: Game, dt: Float) {
        game.getWorld().entities.filter(this::acceptEntity).forEach { fixedUpdate(it, dt) }
        systemFixedUpdate(dt)
    }

    fun postUpdate(game: Game) {
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
