package com.morph.engine.events;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class EventDispatcher {
	private List<Object> eventListeners;
	public static final EventDispatcher INSTANCE = new EventDispatcher();
	
	private EventDispatcher() {
		eventListeners = new ArrayList<>();
	}
	
	public void addEventHandler(Object listener) {
		eventListeners.add(listener);
	}
	
	public <T extends Event> void dispatchEvent(T event) {
		eventListeners.forEach(listener -> Arrays.stream(listener.getClass().getMethods())
				.filter(e -> {
                    EventListener l = e.getAnnotation(EventListener.class);
                    return l != null && l.value().isInstance(event);
				}).forEach(m -> {
                    try {
                        if (m.getParameterCount() == 0) m.invoke(listener);
                        else if (m.getParameterCount() == 1) m.invoke(listener, event);
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
				}));
	}
}
