package com.morph.engine.core;

import com.morph.engine.entities.Entity;

/**
 * Created by Fernando on 1/19/2017.
 */
public abstract class TiledGame extends Game {
    public TiledGame(int width, int height, int tilesX, int tilesY, float tileSize, String title, float fps, boolean fullscreen) {
        super(width, height, title, fps, fullscreen);
        this.world = new TileWorld(this, tilesX, tilesY, tileSize);
    }

    public boolean addEntity(Entity e, int tileX, int tileY) {
        return getWorld().addEntity(e, tileX, tileY);
    }

    public final TileWorld getWorld() {
        return (TileWorld) world;
    }
}
