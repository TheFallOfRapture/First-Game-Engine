package com.fate.engine.graphics;

import com.fate.engine.entities.Component;
import com.fate.engine.graphics.components.RenderData;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Fernando on 1/18/2017.
 */
public class TextureAnimation extends Component {
    private Texture[] frames;
    private float delay;
    private float accumulator;
    private int counter;
    private final int numFrames;
    private RenderData renderDataRef;

    public TextureAnimation(float delay, String... files) {
        Arrays.stream(files).map(Texture::new).collect(Collectors.toList()).toArray(frames);
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
        if (renderDataRef == null) {
            renderDataRef = parent.getComponent(RenderData.class);
        }

        if (accumulator >= delay) {
            renderDataRef.setTexture(frames[counter]);

            accumulator = 0;
            counter++;
        }

        if (counter >= numFrames) {
            counter = 0;
        }

        accumulator += dt;
    }

    @Override
    public TextureAnimation clone() {
        return new TextureAnimation(delay, frames);
    }
}
