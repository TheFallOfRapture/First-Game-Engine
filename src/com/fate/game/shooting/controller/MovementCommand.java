package com.fate.game.shooting.controller;

import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;

public abstract class MovementCommand {
	public abstract Vector2f addMovement(Entity e);
}
