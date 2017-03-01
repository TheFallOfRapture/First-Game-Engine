package com.fate.game.shooting.controller;

/**
 * Created by Fernando on 1/12/2017.
 */

@FunctionalInterface
public interface Actionable<T> {
    public void execute(T t);
}
