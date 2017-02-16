package com.fate.engine.core;

import com.fate.engine.entities.Entity;
import com.fate.engine.entities.EntityGrid;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Fernando on 1/19/2017.
 */
public class TileWorld extends World {
    private int width;
    private int height;
    private float xOffset;
    private float yOffset;
    private float tileSize;
    private Entity[] entities;

    public TileWorld(Game game, int width, int height, float tileSize) {
        super(game);
        this.width = width;
        this.height = height;
        this.xOffset = 0;
        this.yOffset = 0;
        this.tileSize = tileSize;
        this.entities = new Entity[width*height];
    }

    @Override
    public List<Entity> getEntities() {
        return Arrays.asList(entities);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public boolean addEntity(Entity e, int tileX, int tileY) {
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height)
            return false;

        game.renderingEngine.register(e);

        Entity tmp = entities[tileX + tileY * width];
        if (tmp != null)
            game.renderingEngine.unregister(tmp);

        entities[tileX + tileY * width] = e;
        e.getComponent(Transform2D.class).setPosition(new Vector2f(xOffset + (tileX + 0.5f) * tileSize, yOffset + (height * tileSize) - (tileY + 0.5f) * tileSize));
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

    public boolean moveEntity(int startX, int startY, int endX, int endY) {
        if (startX < 0 || startX >= width || startY < 0 || startY >= height
                || endX < 0 || endX >= width || endY < 0 || endY >= height)
            return false;

        if (entities[endX + endY * width] != null)
            game.renderingEngine.unregister(entities[endX + endY * width]);

        entities[endX + endY * width] = entities[startX + startY * width];
        entities[startX + startY * width] = null;

        entities[endX + endY * width].getComponent(Transform2D.class).setPosition(new Vector2f(xOffset + (endX + 0.5f) * tileSize, yOffset + (height * tileSize) - (endY + 0.5f) * tileSize));

        return true;
    }

    public boolean moveEntity(Entity e, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        int[] location = findMatch(e);

        if (location[0] == -1)
            return false;

        System.out.println(new Vector2f(x, y));

        return moveEntity(location[0], location[1], x, y);
    }

    public boolean translateEntity(Entity e, int dx, int dy) {
        int[] location = findMatch(e);

        if (location[0] == -1)
            return false;

        int x = location[0] + dx;
        int y = location[1] + dy;

        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        return moveEntity(location[0], location[1], x, y);
    }

    public boolean translateEntity(int tileX, int tileY, int dx, int dy) {
        int x = tileX + dx;
        int y = tileY + dy;

        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        return moveEntity(tileX, tileY, x, y);
    }

    private int[] findMatch(Entity e) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                if (entities[x + y * width] != null && e != null && e.equals(entities[x + y * width]))
                    return new int[]{x, y};

        return new int[]{-1, -1};
    }

    public Entity getEntity(int tileX, int tileY) {
        if (tileX >= width || tileY >= height)
            return null;

        return entities[tileX + tileY * width];
    }

    public boolean removeEntity(int tileX, int tileY) {
        if (tileX + tileY * width >= entities.length || entities[tileX + tileY * width] == null)
            return false;

        Entity temp = entities[tileX + tileY * width];
        game.renderingEngine.unregister(temp);

        entities[tileX + tileY * width] = null;

        return true;
    }

    public boolean removeEntity(Entity e) {
        int[] location = findMatch(e);
        if (location[0] == -1)
            return false;

        return removeEntity(location[0], location[1]);
    }

    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        return entities[x + y * width] == null;
    }

    public boolean areEmpty(int[]... positions) {
        boolean result = true;
        for (int[] p : positions) {
            result &= isEmpty(p[0], p[1]);
        }

        return result;
    }
}
