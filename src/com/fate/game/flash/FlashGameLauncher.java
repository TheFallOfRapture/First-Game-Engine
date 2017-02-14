package com.fate.game.flash;

import com.fate.engine.core.GameApplication;

/**
 * Created by Fernando on 1/19/2017.
 */
public class FlashGameLauncher {
    private static GameApplication gameLauncher;
    private static FlashGame game;

    public static void main(String[] args) {
        game = new FlashGame(800, 600, 60, false);
        gameLauncher = new GameApplication(game);

        gameLauncher.launch();
    }
}
