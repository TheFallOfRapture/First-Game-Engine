package com.morph.engine.graphics;

import com.morph.engine.entities.Component;
import com.morph.engine.graphics.components.RenderData;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Fernando on 1/18/2017.
 */
public class TextureAnimation implements Component {
    private Texture[] frames;
    private float delay;
    private float accumulator;
    private int counter;
    private final int numFrames;
    private RenderData data;

    public TextureAnimation(RenderData data, float delay, String... files) {
        Arrays.stream(files).map(Texture::new).collect(Collectors.toList()).toArray(frames);
        this.data = data;
        this.delay = delay;
        this.accumulator = delay;
        this.counter = 0;
        this.numFrames = files.length;
    }

    private TextureAnimation(float delay, Texture... frames) {
        this.delay = delay;
        this.accumulator = delay;
        this.counter = 0;
        this.numFrames = frames.length;
        this.frames = frames;
    }

    public void update(float dt) {
        if (accumulator >= delay) {
            data.setTexture(frames[counter], 0);

            accumulator = 0;
            counter++;
        }

        if (counter >= numFrames) {
            counter = 0;
        }

        accumulator += dt;
    }
}
