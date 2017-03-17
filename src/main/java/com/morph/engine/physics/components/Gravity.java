package com.morph.engine.physics.components;

import com.morph.engine.entities.Component;

/**
 * Created by Fernando on 1/9/2017.
 */
public class Gravity extends Component {
    @Override
    public Component clone() {
        return new Gravity();
    }
}
