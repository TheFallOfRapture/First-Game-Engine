package com.morph.engine.entities;

public interface Component {
	default void init() {}
	default void destroy() {}
}
