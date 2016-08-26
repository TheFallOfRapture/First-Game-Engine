package com.fate.game.shooting.entities.components;

import com.fate.engine.entities.Component;

public class Health extends Component {
	private int currentHealth;
	private int maxHealth;
	
	public Health(int currentHealth, int maxHealth) {
		this.currentHealth = currentHealth > maxHealth ? maxHealth : currentHealth;
		this.maxHealth = maxHealth;
	}
	
	public Health(int maxHealth) {
		this.maxHealth = maxHealth;
		this.currentHealth = maxHealth;
	}
	
	public void addCurrentHealth(int amount) {
		currentHealth = currentHealth + amount >= maxHealth ? maxHealth : currentHealth + amount;
	}
	
	public void removeCurrentHealth(int amount) {
		currentHealth = currentHealth - amount <= 0 ? 0 : currentHealth - amount;
	}
	
	public void addMaxHealth(int amount) {
		maxHealth += amount;
	}
	
	public void removeMaxHealth(int amount) {
		maxHealth = maxHealth - amount <= 0 ? 0 : maxHealth - amount;
	}
	
	public void setCurrentHealth(int amount) {
		this.currentHealth = amount > maxHealth ? maxHealth : amount;
	}
	
	public void setMaxHealth(int amount) {
		this.maxHealth = amount < 0 ? 0 : amount;
		if (currentHealth > maxHealth)
			currentHealth = maxHealth;
	}
	
	public int getCurrentHealth() {
		return currentHealth;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public Component clone() {
		return new Health(currentHealth, maxHealth);
	}
}
