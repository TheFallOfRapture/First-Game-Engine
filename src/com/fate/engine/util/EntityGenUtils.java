package com.fate.engine.util;

import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.entities.Component;
import com.fate.engine.entities.Entity;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Texture;
import com.fate.engine.graphics.shaders.BasicTexturedShader;
import com.fate.engine.math.Vector2f;
import com.fate.engine.physics.components.Transform2D;

import java.util.Arrays;

/**
 * Created by Fernando on 1/16/2017.
 */
public class EntityGenUtils {
    public static Entity createEntity(Component... components) {
        Entity e = new Entity();
        Arrays.stream(components).forEach(e::addComponent);
        return e;
    }

    public static Entity createEntityRectangle(float width, float height, boolean isTrigger, Component... additionalComponents) {
        Entity e = createEntity();
        Vector2f halfSize = new Vector2f(width / 2.0f, height / 2.0f);

        e.addComponent(new Transform2D(new Vector2f(0, 0), new Vector2f(width, height)));
        e.addComponent(new BoundingBox2D(new Vector2f(0, 0), halfSize, isTrigger));
        e.addComponent(RenderDataUtils.createSquare(new Color(1, 1, 1), new BasicTexturedShader(), new Texture(null)));

        return e;
    }
}
