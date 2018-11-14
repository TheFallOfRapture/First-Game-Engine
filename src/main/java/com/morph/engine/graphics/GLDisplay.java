package com.morph.engine.graphics;

import com.morph.engine.core.Camera;
import com.morph.engine.core.Game;
import com.morph.engine.input.Keyboard;
import com.morph.engine.input.Mouse;
import com.morph.engine.math.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

// TODO: Generalize to platform agnostic version
// TODO: Migrate to Kotlin

public class GLDisplay {
	private long window;
	private int width, height;
	private String title;
	
	public GLDisplay(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void init(Game game) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failure to create the GLFW window");

		glfwSetKeyCallback(window, Keyboard.INSTANCE::handleKeyEvent);
		
		glfwSetCursorPosCallback(window, (window, x, y) -> Mouse.INSTANCE.setMousePosition(window, new Vector2f((float) x, (float) y), game.getCamera()));
		
		glfwSetMouseButtonCallback(window, Mouse.INSTANCE::handleMouseEvent);

		glfwSetWindowCloseCallback(window, (window) -> game.handleExitEvent());
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		GL.createCapabilities();
	}
	
	public void update() {
		glfwSwapBuffers(window);
	}
	
	public void pollEvents() {
		glfwPollEvents();
	}
	
	public void destroy() {
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public void setFullscreen(int resX, int resY) {
		long newWindow = glfwCreateWindow(resX, resY, title, glfwGetPrimaryMonitor(), window);
		glfwDestroyWindow(window);
		this.window = newWindow;
	}
	
	public void show() {
		glfwShowWindow(window);
	}
	
	public void enableVSync() {
		glfwSwapInterval(1);
	}
	
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}
	
	public void setSize(int width, int height) {
		glfwSetWindowSize(window, width, height);
	}
	
	public long getWindow() {
		return window;
	}
}
