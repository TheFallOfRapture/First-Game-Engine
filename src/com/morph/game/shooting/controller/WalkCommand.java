package com.fate.game.shooting.controller;

import com.fate.engine.entities.Entity;
import com.fate.engine.math.Vector2f;

public class WalkCommand extends MovementCommand {
	public static enum Direction {
		LEFT, RIGHT, UP, DOWN;
	}
	
	private Vector2f direction;
	private float velocity;
	
	public WalkCommand(Direction direction, float velocity) {
		switch (direction) {
			case LEFT:
				this.direction = new Vector2f(-1, 0);
				break;
			case RIGHT:
				this.direction = new Vector2f(1, 0);
				break;
			case UP:
				this.direction = new Vector2f(0, 1);
				break;
			case DOWN:
				this.direction = new Vector2f(0, -1);
				break;
		}

	
		this.velocity = velocity;
	}

	@Override
	public Vector2f addMovement(Entity e) {
		return direction.scale(velocity);
	}
	
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
}
