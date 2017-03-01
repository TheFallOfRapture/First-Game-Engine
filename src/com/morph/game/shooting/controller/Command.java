package com.fate.game.shooting.controller;

import com.fate.engine.entities.Entity;

@FunctionalInterface
public interface Command {
	void execute(Entity e);
}
