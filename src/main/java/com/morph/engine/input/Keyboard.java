package com.morph.engine.input;

import static com.morph.engine.input.Keyboard.StdKeyAction.PRESS;
import static com.morph.engine.input.Keyboard.StdKeyAction.RELEASE;
import static org.lwjgl.glfw.GLFW.*;

import com.morph.engine.events.KeyEvent;
import com.morph.engine.util.Feed;
import com.morph.engine.util.Listener;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Keyboard {
	private final static int keys = GLFW_KEY_LAST;
	private final static Keyboard INSTANCE = new Keyboard();
	private static boolean keysPressed[] = new boolean[keys];
	private static boolean keysDown[] = new boolean[keys];
	private static boolean keysReleased[] = new boolean[keys];

	private static PublishSubject<KeyEvent> keyPresses = PublishSubject.create();
	private static PublishSubject<KeyEvent> keyReleases = PublishSubject.create();

	private static Feed<StdKeyEvent> keyEventFeed = new Feed<>();

	// PRESS, REPEAT, RELEASE
	private static Flowable<StdKeyEvent> standardKeyEvents = Flowable.create(emitter -> {
		Listener<StdKeyEvent> listener = new Listener<StdKeyEvent>() {
			@Override
			public void onNext(StdKeyEvent e) {
				emitter.onNext(e);
			}

			@Override
			public void onError(Throwable t) {
				emitter.onError(t);
			}
		};
		keyEventFeed.register(listener);
	}, BackpressureStrategy.BUFFER);

	// UP, DOWN
	private static Flowable<BinKeyEvent> binaryKeyEvents = Flowable.create(emitter -> {
		Listener<StdKeyEvent> listener = new Listener<StdKeyEvent>() {
			@Override
			public void onNext(StdKeyEvent stdKeyEvent) {
				switch(stdKeyEvent.action) {
					case PRESS:
						emitter.onNext(new BinKeyEvent(BinKeyAction.DOWN, stdKeyEvent.getKey(), stdKeyEvent.getMods()));
						break;
					case RELEASE:
						emitter.onNext(new BinKeyEvent(BinKeyAction.UP, stdKeyEvent.getKey(), stdKeyEvent.getMods()));
						break;
					case REPEAT:
						break;
				}
			}

			@Override
			public void onError(Throwable t) {
				emitter.onError(t);
			}
		};
		keyEventFeed.register(listener);
	}, BackpressureStrategy.BUFFER);

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

	public enum StdKeyAction {
		PRESS, REPEAT, RELEASE
	}

	enum BinKeyAction {
		UP, DOWN
	}

	public static class GenericKeyEvent {
		private int key;
		private int mods;

		public GenericKeyEvent(int key, int mods) {
			this.key = key;
			this.mods = mods;
		}

		public int getKey() {
			return key;
		}

		public int getMods() {
			return mods;
		}

		public boolean hasMod(int modCheck) {
			return (mods & modCheck) != 0;
		}
	}

	public static class StdKeyEvent extends GenericKeyEvent {
		private StdKeyAction action;

		public StdKeyEvent(StdKeyAction action, int key, int mods) {
			super(key, mods);
			this.action = action;
		}

		public StdKeyAction getAction() {
			return action;
		}
	}

	public static class BinKeyEvent extends GenericKeyEvent {
		private BinKeyAction action;

		public BinKeyEvent(BinKeyAction action, int key, int mods) {
			super(key, mods);
			this.action = action;
		}

		public BinKeyAction getAction() {
			return action;
		}
	}
	
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
		if (keycode < 0 || keycode >= keys) {
			System.err.println("Key event outside of handled keys");
			return;
		}

		keysPressed[keycode] = true;
		keyPresses.onNext(new KeyEvent(INSTANCE, keycode, GLFW_PRESS, 0));
	}

	public static void keyReleased(int keycode) {
		if (keycode < 0 || keycode >= keys) {
			System.err.println("Key event outside of handled keys");
			return;
		}

		keysReleased[keycode] = true;
		keysDown[keycode] = false;
		keyReleases.onNext(new KeyEvent(INSTANCE, keycode, GLFW_RELEASE, 0));
	}

	public static void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public static void handleKeyEvent(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS)
			Keyboard.keyPressed(key);
		if (action == GLFW_RELEASE)
			Keyboard.keyReleased(key);

		handleKeyEventRx(key, action, mods); // TODO: Remove from this method and deprecate/remove the enclosing method.
	}

	public static void handleKeyEventRx(int key, int action, int mods) {
		keyEventFeed.onNext(new StdKeyEvent(getKeyAction(action), key, mods));
	}

	private static StdKeyAction getKeyAction(int action) {
		switch (action) {
			case GLFW_PRESS:
				return PRESS;
			case GLFW_REPEAT:
				return StdKeyAction.REPEAT;
			case GLFW_RELEASE:
				return RELEASE;
			default:
				return null;
		}
	}

	public static Flowable<StdKeyEvent> getStandardKeyEvents() {
		return standardKeyEvents;
	}

	public static Flowable<BinKeyEvent> getBinaryKeyEvents() {
		return binaryKeyEvents;
	}

	public static Observable<KeyEvent> getKeyPresses() {
		return keyPresses;
	}

	public static Observable<KeyEvent> getKeyReleases() {
		return keyReleases;
	}
}
