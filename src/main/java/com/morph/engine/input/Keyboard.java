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
	private static Feed<StdKeyEvent> keyEventFeed = new Feed<>();

	// PRESS, REPEAT, RELEASE
	private static Flowable<StdKeyEvent> standardKeyEvents = Flowable.create(keyEventFeed::emit, BackpressureStrategy.BUFFER);

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

			@Override
			public void onComplete() {
				emitter.onComplete();
			}
		};
		keyEventFeed.register(listener);
	}, BackpressureStrategy.BUFFER);

	public enum StdKeyAction {
		PRESS, REPEAT, RELEASE
	}

	public enum BinKeyAction {
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

	public static void handleKeyEvent(long window, int key, int scancode, int action, int mods) {
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
}
