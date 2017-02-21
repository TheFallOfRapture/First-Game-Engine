package com.fate.engine.entities;

import com.fate.engine.core.Game;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Fernando on 2/11/2017.
 */
public class EntityGrid {
    private int width;
    private int height;
    private Entity[] entities;

    public EntityGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new Entity[width*height];
    }

    public List<Entity> getEntities() {
        return Arrays.asList(entities);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean addEntity(Entity e, int tileX, int tileY) {
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height)
            return false;

        Entity tmp = entities[tileX + tileY * width];

        entities[tileX + tileY * width] = e;
        return true;
    }

    public boolean moveEntity(int startX, int startY, int endX, int endY) {
        if (startX < 0 || startX >= width || startY < 0 || startY >= height
                || endX < 0 || endX >= width || endY < 0 || endY >= height)
            return false;

        entities[endX + endY * width] = entities[startX + startY * width];
        entities[startX + startY * width] = null;

        return true;
    }

    public boolean moveEntity(Entity e, int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return false;

        int[] location = findMatch(e);

        if (location[0] == -1)
            return false;

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

    private int[] findMatch(Entity e) {
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                if (e.equals(entities[x + y * width]))
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

        entities[tileX + tileY * width] = null;

        return true;
    }
}
