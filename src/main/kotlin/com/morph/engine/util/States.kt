package com.morph.engine.util

import java.util.*

class State(val name: String)

class StateMachine(private var currentState: State) {
    private val transitions: HashMap<String, () -> Unit> = HashMap()
    private val possibleStates: HashMap<String, State> = HashMap()

    val currentStateName: String
        get() = currentState.name

    fun addPossibility(stateName: String) {
        possibleStates[stateName] = State(stateName)
    }

    fun addPossibilities(vararg stateNames: String) {
        for (s in stateNames) {
            addPossibility(s)
        }
    }

    fun addTransition(stateBegin: String, stateEnd: String, behavior: () -> Unit) {
        transitions["$stateBegin > $stateEnd"] = behavior
    }

    fun copyTransition(stateBegin: String, stateEnd: String, copyBegin: String, copyEnd: String) {
        transitions["$stateBegin > $stateEnd"] = transitions["$copyBegin > $copyEnd"] ?: throw IllegalArgumentException("End state does not exist")
    }

    fun changeState(endState: String) {
        if (currentState.name == endState) return

        val transition: (() -> Unit)? = transitions[currentState.name + " > " + endState]

        if (transition != null) {
            transition()
        } else {
            transitions["* > $endState"]?.invoke()
        }

        currentState = possibleStates[endState] ?: throw IllegalArgumentException("End state does not exist")
    }

    fun isCurrentState(name: String): Boolean {
        return currentState.name == name
    }
}