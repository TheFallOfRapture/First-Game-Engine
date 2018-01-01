package com.morph.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import com.morph.engine.util.Feed;
import com.morph.engine.util.Listener;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.lwjgl.BufferUtils;

import com.morph.engine.graphics.GLRenderingEngine;
import com.morph.engine.math.Matrix4f;
import com.morph.engine.math.Vector2f;
import com.morph.engine.math.Vector4f;

public class Mouse {
	private static Feed<StdMouseEvent> mouseEventFeed = new Feed<>();

	private static Observable<StdMouseEvent> standardMouseEvents = Observable.create(mouseEventFeed::emit);

	private static Observable<BinMouseEvent> binaryMouseEvents = Observable.create(emitter -> {
		Listener<StdMouseEvent> listener = new Listener<StdMouseEvent>() {
			@Override
			public void onNext(StdMouseEvent stdMouseEvent) {
				switch(stdMouseEvent.action) {
					case PRESS:
						emitter.onNext(new BinMouseEvent(BinMouseAction.DOWN, stdMouseEvent.getButton(), stdMouseEvent.getMods()));
						break;
					case RELEASE:
						emitter.onNext(new BinMouseEvent(BinMouseAction.UP, stdMouseEvent.getButton(), stdMouseEvent.getMods()));
						break;
				}
			}

			@Override
			public void onError(Throwable t) {
				emitter.onError(t);
			}

			@Override
			public void onComplete() {
				emitter.onComplete();
			}
		};
		mouseEventFeed.register(listener);
	});

	// TODO: Convert mouse position into Observable/Flowable.
	private static Vector2f screenMousePosition;
	private static Vector2f worldMousePosition;
	
	private static Matrix4f screenToWorld;

	public enum StdMouseAction {
		PRESS, RELEASE
	}

	public enum BinMouseAction {
		UP, DOWN
	}

	public static class GenericMouseEvent {
		private int button;
		private int mods;

		public GenericMouseEvent(int button, int mods) {
			this.button = button;
			this.mods = mods;
		}

		public int getButton() {
			return button;
		}

		public int getMods() {
			return mods;
		}

		public boolean hasMod(int modCheck) {
			return (mods & modCheck) != 0;
		}
	}

	public static class StdMouseEvent extends GenericMouseEvent {
		private StdMouseAction action;

		public StdMouseEvent(StdMouseAction action, int button, int mods) {
			super(button, mods);
			this.action = action;
		}

		public StdMouseAction getAction() {
			return action;
		}
	}

	public static class BinMouseEvent extends GenericMouseEvent {
		private BinMouseAction action;

		public BinMouseEvent(BinMouseAction action, int button, int mods) {
			super(button, mods);
			this.action = action;
		}

		public BinMouseAction getAction() {
			return action;
		}
	}

	public static void handleMouseEvent(long window, int button, int action, int mods) {
		mouseEventFeed.onNext(new StdMouseEvent(getAction(action), button, mods));
	}

	private static StdMouseAction getAction(int action) {
		switch (action) {
			case GLFW_PRESS:
				return StdMouseAction.PRESS;
			case GLFW_RELEASE:
				return StdMouseAction.RELEASE;
			default:
				return null;
		}
	}
	
	public static void setMousePosition(long window, Vector2f v) {
		IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, widthBuffer, heightBuffer);

		int width = widthBuffer.get();
		int height = heightBuffer.get();

		screenMousePosition = new Vector2f(v.getX(), height - v.getY());

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

	public static boolean queryUpDown(int button, BinMouseAction action) {
		Maybe<BinMouseEvent> e = binaryMouseEvents.filter(event -> event.getButton() == button).lastElement();
		return !e.isEmpty().blockingGet() && e.blockingGet().action == action;
	}

	public static Observable<StdMouseEvent> getStandardMouseEvents() {
		return standardMouseEvents;
	}
}
