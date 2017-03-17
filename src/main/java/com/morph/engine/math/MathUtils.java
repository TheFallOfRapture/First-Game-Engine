package com.morph.engine.math;

public class MathUtils {
	public static final float clamp(float value, float min, float max) {
		if (value < min)
			return min;
		else if (value > max)
			return max;
		else
			return value;
	}
}
