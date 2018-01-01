package com.morph.engine.graphics;

import com.morph.engine.util.Feed;
import io.reactivex.Observable;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import com.morph.engine.input.Keyboard;
import com.morph.engine.input.Mouse;
import com.morph.engine.math.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GLDisplay {
	private long window;
	private int width, height;
	private String title;

	private Feed<GLDisplayAction> eventFeed = new Feed<>();
	private Observable<GLDisplayAction> events = Observable.create(eventFeed::emit);

	public enum GLDisplayAction {
		OPEN, CLOSE
	}
	
	public GLDisplay(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void init() {
		eventFeed.onNext(GLDisplayAction.OPEN);

		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit())
			throw new IllegalStateException("Failed to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failure to create the GLFW window");

		glfwSetKeyCallback(window, Keyboard::handleKeyEvent);
		
//		glfwSetWindowSizeCallback(window, (window, x, y) -> EventDispatcher.INSTANCE.dispatchEvent(new ResizeEvent(this, x, y, false)));
		
		glfwSetCursorPosCallback(window, (window, x, y) -> Mouse.setMousePosition(window, new Vector2f(x, y)));
		
		glfwSetMouseButtonCallback(window, Mouse::handleMouseEvent);

		glfwSetWindowCloseCallback(window, (window) -> {
			eventFeed.onNext(GLDisplayAction.CLOSE);
			eventFeed.onComplete();
		});
		
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

	public Observable<GLDisplayAction> getEvents() {
		return events;
	}
}
