package com.morph.engine.core

import io.reactivex.rxkotlin.toCompletable

class GameApplication(val game: Game) {
    fun launch() {
        game::start.toCompletable().subscribe()
    }
}
