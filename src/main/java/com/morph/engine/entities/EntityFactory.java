package com.morph.engine.entities;

import com.morph.engine.collision.components.BoundingBox2D;
import com.morph.engine.graphics.Color;
import com.morph.engine.graphics.shaders.Shader;
import com.morph.engine.graphics.Texture;
import com.morph.engine.graphics.components.RenderData;
import com.morph.engine.graphics.shaders.BasicTexturedShader;
import com.morph.engine.math.Vector2f;
import com.morph.engine.physics.components.Transform2D;
import com.morph.engine.util.RenderDataUtils;

/**
 * Created by Fernando on 1/15/2017.
 */
public class EntityFactory {
    private static int count = 0;

    public static Entity getEntity(String name) {
        count++;
        return new Entity(name, count);
    }

    public static Entity getRectangleAt(String name, float x, float y, float width, float height, Color c, boolean isTrigger) {
        Entity result = getEntity(name);
        Vector2f halfSize = new Vector2f(width / 2.0f, height / 2.0f);
        result.addComponent(new Transform2D(new Vector2f(x, y), new Vector2f(width, height)));
        result.addComponent(new BoundingBox2D(new Vector2f(x, y), halfSize, isTrigger));

        result.addComponent(RenderDataUtils.createSquare(c, new BasicTexturedShader(), new Texture(null)));

        return result;
    }

    public static Entity getRectangle(String name, float width, float height, Color c) {
        Entity result = getEntity(name);
        Vector2f halfSize = new Vector2f(width / 2.0f, height / 2.0f);
        result.addComponent(new Transform2D(new Vector2f(0, 0), new Vector2f(width, height)));
        result.addComponent(new BoundingBox2D(new Vector2f(0, 0), halfSize, false));

        result.addComponent(RenderDataUtils.createSquare(c, new BasicTexturedShader(), new Texture(null)));

        return result;
    }

    public static Entity getCustomRectangle(String name, float width, float height, Color c, Shader<?> shader) {
        Entity result = getEntity(name);
        Vector2f halfSize = new Vector2f(width / 2.0f, height / 2.0f);
        result.addComponent(new Transform2D(new Vector2f(0, 0), new Vector2f(width, height)));
        result.addComponent(new BoundingBox2D(new Vector2f(0, 0), halfSize, false));

        result.addComponent(RenderDataUtils.createSquare(c, shader, new Texture(null)));

        return result;
    }

    public static Entity getCustomTintRectangle(String name, float width, float height, Color c, Shader<?> shader) {
        Entity result = getEntity(name);
        Vector2f halfSize = new Vector2f(width / 2.0f, height / 2.0f);
        result.addComponent(new Transform2D(new Vector2f(0, 0), new Vector2f(width, height)));
        result.addComponent(new BoundingBox2D(new Vector2f(0, 0), halfSize, false));

        RenderData data = RenderDataUtils.createSquare(new Color(1, 1, 1), shader, new Texture(null));
        data.setTint(c);

        result.addComponent(data);

        return result;
    }
}
