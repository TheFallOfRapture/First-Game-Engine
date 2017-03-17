package com.morph.engine.core;

/**
 * Created by Fernando on 1/19/2017.
 */
public abstract class TiledGame extends Game {
    public TiledGame(int width, int height, int tilesX, int tilesY, float tileSize, String title, float fps, boolean fullscreen) {
        super(width, height, title, fps, fullscreen);
        this.world = new TileWorld(this, tilesX, tilesY, tileSize);
    }

    public final TileWorld getWorld() {
        return (TileWorld) world;
    }
}
