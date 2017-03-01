package com.fate.game.shooting.entities.components.attacks;

/**
 * Created by Fernando on 1/11/2017.
 */
public class AttackData {
    private int damage;
    private float cooldown;

    public AttackData(int damage, float cooldown) {
        this.damage = damage;
        this.cooldown = cooldown;
    }

    public int getDamage() {
        return damage;
    }

    public float getCooldown() {
        return cooldown;
    }
}
