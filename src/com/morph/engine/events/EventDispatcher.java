package com.fate.engine.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class EventDispatcher {
	private List<Object> eventListeners;
	public static final EventDispatcher INSTANCE = new EventDispatcher();
	
	private EventDispatcher() {
		eventListeners = new ArrayList<Object>();
	}
	
	public void addEventHandler(Object listener) {
		eventListeners.add(listener);
	}
	
	public <T extends Event> void dispatchEvent(T event) {
		for (int i = 0; i < eventListeners.size(); i++) {
			Object listener = eventListeners.get(i);
			List<Method> handlers = Arrays.asList(listener.getClass().getMethods())
					.stream()
					.filter(e -> {
						EventListener l = e.getAnnotation(EventListener.class);
						return l != null && l.value().isInstance(event);
					}).collect(Collectors.toCollection(ArrayList::new));
			
			for (Method m : handlers) {
				try {
					if (m.getParameterCount() == 0)
						m.invoke(listener);
					else if (m.getParameterCount() == 1)
						m.invoke(listener, event);
				} catch (InvocationTargetException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
