package com.fate.game.shooting.systems;

import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.engine.graphics.components.RenderData;
import com.fate.game.shooting.ShootingGame;
import com.fate.game.shooting.entities.components.Block;

public class BlockStateSystem extends GameSystem {
	public BlockStateSystem(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean acceptEntity(Entity e) {
		return e.hasComponents(Block.class, RenderData.class);
	}
	
	protected void update(Entity e) {
		Block block = e.getComponent(Block.class);
		
		switch (block.getCurrentStateName()) {
			case "BlockInactive":
				inactiveUpdate(e);
				break;
			case "BlockHover":
				hoverUpdate(e);
				break;
			case "BlockClick":
				clickUpdate(e);
				break;
			case "BlockDestroy":
				destroyUpdate(e);
				break;
		}
	}
	
	private void inactiveUpdate(Entity e) {}
	
	private void hoverUpdate(Entity e) {}
	
	private void clickUpdate(Entity e) {}
	
	private void destroyUpdate(Entity e) {
		((ShootingGame)game).getWorld().removeEntity(e);
	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
}
