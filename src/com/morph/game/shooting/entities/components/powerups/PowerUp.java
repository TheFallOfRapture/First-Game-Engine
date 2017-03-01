package com.fate.game.shooting.entities.components.powerups;

import com.fate.engine.entities.Component;
import com.fate.engine.entities.Entity;

public abstract class PowerUp extends Component {
	@Override
	public Component clone() {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract void onPickup(Entity e);
}
