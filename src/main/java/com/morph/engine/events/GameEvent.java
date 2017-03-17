package com.morph.engine.events;

import com.morph.engine.core.Game;

public class GameEvent extends Event {
	public GameEvent(Game game) {
		super(game);
	}
	
	public Game getGame() {
		return (Game) source;
	}
}
