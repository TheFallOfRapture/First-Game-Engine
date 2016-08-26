package com.fate.game.shooting.entities.components;

import com.fate.engine.entities.Component;
import com.fate.engine.graphics.Color;
import com.fate.engine.graphics.components.RenderData;
import com.fate.game.shooting.block.State;
import com.fate.game.shooting.block.StateMachine;

public class Block extends Component {
	private StateMachine stateMachine;
	private Color inactive;
	private Color hover;
	private Color click;
	
	public Block(Color inactive, Color hover, Color click) {
		stateMachine = new StateMachine(new State("BlockInactive"));
		stateMachine.addPossibilities("BlockInactive", "BlockHover", "BlockClick", "BlockDestroy");
		
		stateMachine.addTransition("*", "BlockClick", (e) -> {
			RenderData data = e.getComponent(RenderData.class);
			for (int i = 0; i < data.getVertices().size(); i++) {
				data.getVertices().get(i).setColor(click);
			}
			data.init();
		});
		
		stateMachine.addTransition("*", "BlockHover", (e) -> {
			RenderData data = e.getComponent(RenderData.class);
			for (int i = 0; i < data.getVertices().size(); i++) {
				data.getVertices().get(i).setColor(hover);
			}
			data.init();
		});
		
		stateMachine.addTransition("*", "BlockInactive", (e) -> {
			RenderData data = e.getComponent(RenderData.class);
			for (int i = 0; i < data.getVertices().size(); i++) {
				data.getVertices().get(i).setColor(inactive);
			}
			data.init();
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
