package com.fate.engine.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Entity implements Cloneable {
	private List<Component> components;
	private boolean destroyed;
	
	public Entity() {
		this.components = new ArrayList<Component>();
	}

	public void addComponent(Component c) {
		c.setParent(this);
		components.add(c);
		c.init();
	}
	
	public void removeComponent(Component c) {
		c.setParent(null);
		components.remove(c);
		c.destroy();
	}
	
	public void clearComponents(Class<? extends Component> type) {
		for (int i = components.size() - 1; i >= 0; i--) {
			Component c = components.get(i);
			if (type.isInstance(c))
				removeComponent(c);
		}
	}
	
	public void clearAllComponents() {
		for (int i = components.size() - 1; i >= 0; i--) {
			removeComponent(components.get(i));
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> type) {
		if (!hasComponent(type))
			return null;
		
		return (T) components.stream().filter(type::isInstance).collect(Collectors.toCollection(ArrayList::new)).get(0);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> List<T> getComponents(Class<T> type) {
		if (!hasComponent(type))
			return null;
		
		return (List<T>) components.stream().filter(type::isInstance).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public <T extends Component> boolean hasComponent(Class<T> type) {
		return components.stream().filter(type::isInstance).collect(Collectors.toCollection(ArrayList::new)).size() != 0;
	}
	
	public void destroy() {
		clearAllComponents();
		destroyed = true;
	}

	public List<? extends Component> getAllComponents() {
		return components;
	}
	
	@Override
	public Entity clone() {
		Entity e = new Entity();
		for (Component c : components) {
			e.addComponent(c.clone());
		}
		
		return e;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	@SafeVarargs
	public final boolean hasComponents(Class<? extends Component>... requiredTypes) {
		boolean result = true;
		
		for (Class<? extends Component> type : requiredTypes) {
			result &= hasComponent(type);
		}
		
		return result;
	}
}
