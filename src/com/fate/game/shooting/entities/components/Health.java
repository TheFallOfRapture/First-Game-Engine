package com.fate.game.shooting.entities.components;

import com.fate.engine.entities.Component;
import com.fate.engine.events.EventDispatcher;
import com.fate.engine.events.EventListener;
import com.fate.game.shooting.ShootingGame;
import com.fate.game.shooting.events.DamageEvent;

public class Health extends Component {
	private int currentHealth;
	private int maxHealth;
	
	public Health(int currentHealth, int maxHealth) {
		this.currentHealth = currentHealth > maxHealth ? maxHealth : currentHealth;
		this.maxHealth = maxHealth;
		EventDispatcher.INSTANCE.addEventHandler(this);
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

	public float getPercentHealth() {
		return (float) currentHealth / maxHealth;
	}

	public void handleAttackEvent(DamageEvent e) {
		removeCurrentHealth(e.getAttackData().getDamage());

		System.out.println(this.parent + " was attacked! Health: " + currentHealth + "/" + maxHealth);
		if (currentHealth <= 0) {
			System.out.println(this.parent + " died!");
			((ShootingGame)e.getGame()).getWorld().removeEntity(this.parent);
		}
	}
	
	@Override
	public Component clone() {
		return new Health(currentHealth, maxHealth);
	}
}
