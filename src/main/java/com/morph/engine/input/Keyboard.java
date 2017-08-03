package com.morph.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import com.morph.engine.events.EventDispatcher;
import com.morph.engine.events.KeyEvent;

public class Keyboard {
	private final static int keys = GLFW_KEY_LAST;
	private final static Keyboard INSTANCE = new Keyboard();
	private static boolean keysPressed[] = new boolean[keys];
	private static boolean keysDown[] = new boolean[keys];
	private static boolean keysReleased[] = new boolean[keys];
	
	/**
	 * Key Pressed Event:
	 * Current Frame - Set keysPressed[keycode] to TRUE
	 * Next Frame - Set keysPressed[keycode] to FALSE
	 * 
	 * Current Frame: 
	 * keysPressed[keycode] = true
	 * clear():
	 *     keysPressedLastFrame[keycode] = true
	 *     keysPressed[keycode] = false
	 * 
	 * Next Frame:
	 * if (keysPressedLastFrame[keycode] && !keysReleasedLastFrame[keycode])
	 *     keysDown[keycode] = true
	 * 
	 * 
	 */
	
	public static void clear() {
		for (int i = 0; i < keys; i++) {
			if (keysPressed[i])
				keysDown[i] = true;
			
			keysPressed[i] = false;
			keysReleased[i] = false;
		}
	}
	
	public static boolean isKeyPressed(int keycode) {
		return keysPressed[keycode];
	}
	
	public static boolean isKeyDown(int keycode) {
		return keysDown[keycode];
	}
	
	public static boolean isKeyReleased(int keycode) {
		return keysReleased[keycode];
	}

	public static void keyPressed(int keycode) {
		keysPressed[keycode] = true;
		EventDispatcher.INSTANCE.dispatchEvent(new KeyEvent(INSTANCE, keycode, GLFW_PRESS, 0));
	}

	public static void keyReleased(int keycode) {
		keysReleased[keycode] = true;
		keysDown[keycode] = false;
		EventDispatcher.INSTANCE.dispatchEvent(new KeyEvent(INSTANCE, keycode, GLFW_RELEASE, 0));
	}

	public static void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
