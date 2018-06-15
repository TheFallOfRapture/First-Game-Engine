package com.morph.engine.core;

import com.morph.engine.entities.Entity;
import com.morph.engine.entities.EntityGrid;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Fernando on 1/19/2017.
 */
public abstract class TileWorld extends EntityGrid implements IWorld {
    private Game game;
    private float xOffset;
    private float yOffset;
    private float tileSize;
    private Entity[] entities;

    public TileWorld(Game game, int width, int height, float tileSize) {
        super(width, height);
        this.game = game;
        this.xOffset = 0;
        this.yOffset = 0;
        this.tileSize = tileSize;
        this.entities = new Entity[getWidth()*getHeight()];
    }

    @Override
    public Game getGame() {
        return game;
    }

    // TODO: Strict implementation = return false
    @Override
    public boolean addEntity(Entity e) {
        Vector2f tilePos = e.getComponent(Transform2D.class).getPosition().invScale(tileSize).map(x -> (float) Math.floor(x));
        return addEntity(e, (int) tilePos.getX(), (int) tilePos.getY());
    }

    public float getTileSize() {
        return tileSize;
    }

    public void setXOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(float yOffset) {
        this.yOffset = yOffset;
    }

    @Override
    public boolean addEntity(@NotNull Entity e, int tileX, int tileY) {
        if (tileX < 0 || tileX >= getWidth() || tileY < 0 || tileY >= getHeight())
            return false;

        game.renderingEngine.register(e);

        Entity tmp = entities[tileX + tileY * getWidth()];
        if (tmp != null)
            game.renderingEngine.unregister(tmp);

        entities[tileX + tileY * getWidth()] = e;
        e.getComponent(Transform2D.class).setPosition(new Vector2f(xOffset + (tileX + 0.5f) * tileSize, yOffset + (getHeight() * tileSize) - (tileY + 0.5f) * tileSize));
        e.getComponent(Transform2D.class).setScale(new Vector2f(tileSize, tileSize));

        return true;
    }

    public boolean addEntityGrid(EntityGrid entities, int startX, int startY) {
        for (int y = 0; y < entities.getHeight(); y++) {
            for (int x = 0; x < entities.getWidth(); x++) {
                Entity e = entities.getEntity(x, y);
                if (e != null) {
                    boolean success = addEntity(e, startX + x, startY + y);
                    if (!success) return false;
                }
            }
        }

        return true;
    }

    public boolean removeEntityGrid(EntityGrid entities) {
        for (Entity e : this.entities)
            if (entities.getEntities().contains(e))
                removeEntity(e);

        return true;
    }

    public boolean lazyMoveEntityGrid(EntityGrid entities, int x, int y) {
        removeEntityGrid(entities);
        return addEntityGrid(entities, x, y);
    }

    @Override
    public boolean moveEntity(int startX, int startY, int endX, int endY) {
        if (startX < 0 || startX >= getWidth() || startY < 0 || startY >= getHeight()
                || endX < 0 || endX >= getWidth() || endY < 0 || endY >= getHeight())
            return false;

        if (entities[endX + endY * getWidth()] != null)
            game.renderingEngine.unregister(entities[endX + endY * getWidth()]);

        entities[endX + endY * getWidth()] = entities[startX + startY * getWidth()];
        entities[startX + startY * getWidth()] = null;

        entities[endX + endY * getWidth()].getComponent(Transform2D.class).setPosition(new Vector2f(xOffset + (endX + 0.5f) * tileSize, yOffset + (getHeight() * tileSize) - (endY + 0.5f) * tileSize));

        return true;
    }

    private int[] findMatch(Entity e) {
        for (int y = 0; y < getHeight(); y++)
            for (int x = 0; x < getWidth(); x++)
                if (entities[x + y * getWidth()] != null && e != null && e.equals(entities[x + y * getWidth()]))
                    return new int[]{x, y};

        return new int[]{-1, -1};
    }

    @Override
    public boolean removeEntity(int tileX, int tileY) {
        if (tileX + tileY * getWidth() >= entities.length || entities[tileX + tileY * getWidth()] == null)
            return false;

        Entity temp = entities[tileX + tileY * getWidth()];
        game.renderingEngine.unregister(temp);

        entities[tileX + tileY * getWidth()] = null;

        return true;
    }

    public boolean removeEntity(Entity e) {
        int[] location = findMatch(e);
        if (location[0] == -1)
            return false;

        return removeEntity(location[0], location[1]);
    }

    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
            return false;

        return entities[x + y * getWidth()] == null;
    }

    public boolean areEmpty(int[]... positions) {
        boolean result = true;
        for (int[] p : positions) {
            result &= isEmpty(p[0], p[1]);
        }

        return result;
    }
}
