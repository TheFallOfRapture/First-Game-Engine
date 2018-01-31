package com.morph.engine.util;

import java.util.HashMap;

public class StateMachine {
    private State currentState;
    private HashMap<String, Runnable> transitions;
    private HashMap<String, State> possibleStates;

    public StateMachine(State currentState) {
        this.currentState = currentState;
        transitions = new HashMap<>();
        possibleStates = new HashMap<>();
    }

    public void addPossibility(String stateName) {
        possibleStates.put(stateName, new State(stateName));
    }

    public void addPossibilities(String...stateNames) {
        for (String s : stateNames) {
            addPossibility(s);
        }
    }

    public void addTransition(String stateBegin, String stateEnd, Runnable behavior) {
        transitions.put(stateBegin + " > " + stateEnd, behavior);
    }

    public void copyTransition(String stateBegin, String stateEnd, String copyBegin, String copyEnd) {
        transitions.put(stateBegin + " > " + stateEnd, transitions.get(copyBegin + " > " + copyEnd));
    }

    public void changeState(String endState) {
        if (currentState.getName().equals(endState)) return;

        Runnable transition = transitions.get(currentState.getName() + " > " + endState);

        if (transition == null) {
            transition = transitions.get("* > " + endState);
        }

        if (transition != null) {
            transition.run();
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
}