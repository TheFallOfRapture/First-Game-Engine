package com.fate.game.shooting.systems;

import com.fate.engine.core.Game;
import com.fate.engine.core.GameSystem;
import com.fate.engine.entities.Entity;
import com.fate.engine.graphics.components.RenderData;
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
		
		switch(block.getCurrentStateName()) {
			case "BlockInactive":
				break;
			case "BlockHover":
				break;
			case "BlockClick":
				break;
			case "BlockDestroy":
				game.removeEntity(e);
				break;
		}
	}

	@Override
	public void initSystem() {
		// TODO Auto-generated method stub
		
	}
}
