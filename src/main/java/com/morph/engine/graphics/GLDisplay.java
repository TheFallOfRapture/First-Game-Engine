package com.morph.engine.graphics;

import com.morph.engine.events.ExitEvent;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import com.morph.engine.events.EventDispatcher;
import com.morph.engine.events.ResizeEvent;
import com.morph.engine.input.Keyboard;
import com.morph.engine.input.Mouse;
import com.morph.engine.math.Vector2f;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GLDisplay {
	private long window;
	private int width, height;
	private String title;
	
	public GLDisplay(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failure to create the GLFW window");
		
		glfwSetKeyCallback(window, Keyboard::handleKeyEvent);
		
		glfwSetWindowSizeCallback(window, (window, x, y) -> EventDispatcher.INSTANCE.dispatchEvent(new ResizeEvent(this, x, y, false)));
		
		glfwSetCursorPosCallback(window, (window, x, y) -> Mouse.setMousePosition(window, new Vector2f(x, y)));
		
		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if (action == GLFW_PRESS)
				Mouse.mousePressed(button);
			if (action == GLFW_RELEASE)
				Mouse.mouseReleased(button);
		});

		glfwSetWindowCloseCallback(window, (window) -> EventDispatcher.INSTANCE.dispatchEvent(new ExitEvent(this)));
		
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
	
	public boolean isCloseRequested() {
		return glfwWindowShouldClose(window);
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
