package com.fate.game.shooting.controller;

import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;

public class JumpCommand extends MovementCommand {
	private float jumpHeight;

	public JumpCommand(float jumpHeight) {
		this.jumpHeight = jumpHeight;
	}

	@Override
	public Vector2f addMovement(Entity e) {
		// TODO Auto-generated method stub
		return new Vector2f(0, 0);
	}

}
