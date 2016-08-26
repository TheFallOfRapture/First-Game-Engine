package com.fate.game.shooting.entities.components.powerups;

import com.fate.engine.entities.Entity;
import com.fate.game.shooting.controller.KeyboardController;

public class JumpBoost extends PowerUp {
	private float jumpModifier;
	
	public JumpBoost(float jumpModifier) {
		this.jumpModifier = jumpModifier;
	}
	
	@Override
	public void onPickup(Entity e) {
		KeyboardController kc = e.getComponent(KeyboardController.class);
		float jump = kc.getJumpHeight();
		kc.setJumpHeight(jump * jumpModifier);
	}
	
	public float getJumpModifier() {
		return jumpModifier;
	}
}
