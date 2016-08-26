package com.fate.engine.physics.components;

import org.jbox2d.dynamics.Body;

import com.fate.engine.entities.Component;
import com.fate.engine.math.Vector2f;

public abstract class RigidBody2D extends Component {
	protected Body body;
	public abstract Vector2f getPosition();
}
