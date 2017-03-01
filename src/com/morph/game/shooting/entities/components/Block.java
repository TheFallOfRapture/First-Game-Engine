package com.fate.game.shooting.entities.components;

import com.fate.engine.entities.Component;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.Texture;
import com.fate.engine.graphics.components.RenderData;
import com.fate.game.shooting.block.State;
import com.fate.game.shooting.block.StateMachine;

public class Block extends Component {
	private StateMachine stateMachine;
	
	public Block(Color inactive, Color hover, Color click) {
		stateMachine = new StateMachine(new State("BlockInactive"));
		stateMachine.addPossibilities("BlockInactive", "BlockHover", "BlockClick", "BlockDestroy");
		
		stateMachine.addTransition("*", "BlockClick", (e) -> {
			RenderData data = e.getComponent(RenderData.class);
			data.setTexture(new Texture("textures/tileClick.png"));
		});
		
		stateMachine.addTransition("*", "BlockHover", (e) -> {
			RenderData data = e.getComponent(RenderData.class);
			data.setTexture(new Texture("textures/tileHover.png"));
		});
		
		stateMachine.addTransition("*", "BlockInactive", (e) -> {
			RenderData data = e.getComponent(RenderData.class);
			data.setTexture(new Texture("textures/tileUnlit.png"));
		});
	}
	
	public void init() {
		stateMachine.setEntity(parent);
	}
	
	@Override
	public Component clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public State getCurrentState() {
		return stateMachine.getCurrentState();
	}
	
	public String getCurrentStateName() {
		return stateMachine.getCurrentStateName();
	}
	
	public void setState(String name) {
		stateMachine.changeState(name);
	}

	public boolean isCurrentState(String name) {
		return stateMachine.isCurrentState(name);
	}
}
