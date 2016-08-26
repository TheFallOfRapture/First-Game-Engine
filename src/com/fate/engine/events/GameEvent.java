package com.fate.engine.events;

import com.fate.engine.core.Game;

public class GameEvent extends Event {
	public GameEvent(Game game) {
		super(game);
	}
	
	public Game getGame() {
		return (Game) source;
	}
}
