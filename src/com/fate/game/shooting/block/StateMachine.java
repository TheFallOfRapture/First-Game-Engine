package com.fate.game.shooting.block;

import java.util.HashMap;
import java.util.function.Consumer;

import com.fate.engine.entities.Component;
import com.fate.engine.entities.Entity;

public class StateMachine {
	private State currentState;
	private Entity entity;
	private HashMap<String, Consumer<Entity>> transitions;
	private HashMap<String, State> possibleStates;
	
	public StateMachine(State currentState) {
		this.currentState = currentState;
		transitions = new HashMap<String, Consumer<Entity>>();
		possibleStates = new HashMap<String, State>();
	}
	
	public void setEntity(Entity e) {
		this.entity = e;
	}
	
	public void addPossibility(String stateName) {
		possibleStates.put(stateName, new State(stateName));
	}
	
	public void addPossibilities(String...stateNames) {
		for (String s : stateNames) {
			addPossibility(s);
		}
	}
	
	public void addTransition(String stateBegin, String stateEnd, Consumer<Entity> behavior) {
		transitions.put(stateBegin + " > " + stateEnd, behavior);
	}
	
	public void copyTransition(String stateBegin, String stateEnd, String copyBegin, String copyEnd) {
		transitions.put(stateBegin + " > " + stateEnd, transitions.get(copyBegin + " > " + copyEnd));
	}
	
	public void changeState(String endState) {
		Consumer<Entity> transition = transitions.get(currentState.getName() + " > " + endState);
		
		if (transition == null) {
			transition = transitions.get("* > " + endState);
		}
		
		if (transition != null) {
			transition.accept(entity);
		}
		
		currentState = possibleStates.get(endState);
	}
	
	public boolean isCurrentState(String name) {
		return currentState.getName().equals(name);
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public String getCurrentStateName() {
		return currentState.getName();
	}

	@Override
	public Component clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
