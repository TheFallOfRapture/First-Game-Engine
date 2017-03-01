package com.morph.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;

import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector4f;

public class Mouse {
	private static Vector2f screenMousePosition;
	private static Vector2f worldMousePosition;
	
	private static Matrix4f screenToWorld;
	
	private static boolean[] buttonsPressed = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private static boolean[] buttonsDown = new boolean[GLFW_MOUSE_BUTTON_LAST];
	private static boolean[] buttonsReleased = new boolean[GLFW_MOUSE_BUTTON_LAST];
	
	public static void clear() {
		for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
			if (buttonsPressed[i])
				buttonsDown[i] = true;
			
			buttonsPressed[i] = false;
			buttonsReleased[i] = false;
		}
	}
	
	public static void setMousePosition(long window, Vector2f v) {
		screenMousePosition = v;
		
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, widthBuffer, heightBuffer);
		
		int width = widthBuffer.get();
		int height = heightBuffer.get();

		if (screenToWorld == null) {
			screenToWorld = GLRenderingEngine.getProjectionMatrix().getInverse();
		}

		Vector2f normalizedMousePos = screenMousePosition.div(new Vector2f(width / 2f, height / 2f)).sub(new Vector2f(1, 1)).mul(new Vector2f(1, -1));
		worldMousePosition = screenToWorld.mul(new Vector4f(normalizedMousePos, 0, 1)).getXY();
	}
	
	public static Vector2f getScreenMousePosition() {
		return screenMousePosition;
	}
	
	public static Vector2f getWorldMousePosition() {
		return worldMousePosition;
	}
	
	public static void mousePressed(int button) {
		buttonsPressed[button] = true;
	}
	
	public static void mouseReleased(int button) {
		buttonsReleased[button] = true;
		buttonsDown[button] = false;
	}
	
	public static boolean isMouseButtonPressed(int button) {
		return buttonsPressed[button];
	}
	
	public static boolean isMouseButtonDown(int button) {
		return buttonsDown[button];
	}
	
	public static boolean isMouseButtonReleased(int button) {
		return buttonsReleased[button];
	}
}
