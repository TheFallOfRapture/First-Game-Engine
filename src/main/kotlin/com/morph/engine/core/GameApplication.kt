package com.morph.engine.core

import kotlin.concurrent.thread

class GameApplication(val game: Game) {
    fun launchGame() {
        thread {
            game.start()
        }
    }

    //        game::start.toCompletable().subscribe()
}
