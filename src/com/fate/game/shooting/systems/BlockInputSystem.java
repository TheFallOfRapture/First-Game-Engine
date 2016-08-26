package com.fate.game.shooting.systems;

import com.fate.engine.collision.components.BoundingBox2D;
import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.GLRenderingEngine;
import com.fate.engine.graphics.components.RenderData;
import com.fate.engine.input.Mouse;
import com.fate.engine.math.Vector2f;
import com.fate.engine.math.Vector4f;
import com.fate.game.shooting.entities.components.Block;

public class BlockInputSystem extends GameSystem {
	public BlockInputSystem(Game game) {
		super(game);
	}
	
	protected void update(Entity e) {
		BoundingBox2D bounds = e.getComponent(BoundingBox2D.class);
		Block block = e.getComponent(Block.class);
		
		Vector2f mousePos = Mouse.getWorldMousePosition();
		
		switch (block.getCurrentStateName()) {
			case "BlockInactive":
				if (mousePos != null && bounds.contains(mousePos)) {
					if (Mouse.isMouseButtonPressed(0)) {
						block.setState("BlockClick");
					} else {
						block.setState("BlockHover");
					}
				}
				break;
			case "BlockHover":
				if (mousePos != null && bounds.contains(mousePos)) {
					if (Mouse.isMouseButtonPressed(0)) {
						block.setState("BlockClick");
					}
				} else {
					block.setState("BlockInactive");
				}
				break;
			case "BlockClick":
				if (!Mouse.isMouseButtonDown(0)) {
					block.setState("BlockDestroy");
				}
				break;
		}
	}

	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponents(Block.class, BoundingBox2D.class);
	}

	@Override
	public void initSystem() {}
}
