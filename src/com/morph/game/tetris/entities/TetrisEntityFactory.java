package com.fate.game.tetris.entities;

import com.fate.engine.entities.Entity;
import com.fate.engine.entities.EntityFactory;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Texture;
import com.fate.engine.graphics.components.RenderData;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;
import com.fate.game.tetris.graphics.shaders.TetrisShader;

/**
 * Created by Fernando on 2/11/2017.
 */
public class TetrisEntityFactory {
    public static Entity getBlock(String name, Color c, float size) {
        Entity block = EntityFactory.getCustomTintRectangle(name, size, size, c, new TetrisShader());
        block.getComponent(RenderData.class).setTexture(new Texture("textures/tetrisBlockSV.png"));
        return block;
    }

    public static Entity getBlockAt(String name, Vector2f position, Color c, float size) {
        Entity block = getBlock(name, c, size);
        block.getComponent(Transform2D.class).setPosition(position);
        return block;
    }
}
