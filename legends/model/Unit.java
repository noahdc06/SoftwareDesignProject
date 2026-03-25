package com.legends.model;

public abstract class Unit {
    private String name;
    private int attack;
    private int maxHealth;
    private int currentHealth;
    private int defense;
    private boolean downed;

    protected Unit(String name, int attack, int maxHealth, int defense) {
        this.name = name;
        this.attack = attack;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.defense = defense;
        this.downed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = Math.max(0, attack);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(1, maxHealth);
        if (currentHealth > this.maxHealth) {
            currentHealth = this.maxHealth;
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
        if (this.currentHealth <= 0) {
            this.currentHealth = 0;
            this.downed = true;
        }
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);
    }

    public boolean isDowned() {
        return downed;
    }

    public void setDowned(boolean downed) {
        this.downed = downed;
        if (downed && currentHealth > 0) {
            currentHealth = 0;
        }
    }

    public void heal(int amount) {
        if (downed || amount <= 0) {
            return;
        }
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    public void fullyRestore() {
        downed = false;
        currentHealth = maxHealth;
    }

    public int dealDamageTo(Unit target) {
        if (target.isDowned()) {
            return 0;
        }
        int damage = Math.max(0, attack - target.getDefense());
        target.takeDamageRaw(damage);
        return damage;
    }

    public void takeDamageRaw(int damage) {
        int actual = Math.max(0, damage);
        currentHealth -= actual;
        if (currentHealth <= 0) {
            currentHealth = 0;
            downed = true;
        }
    }

    public String getStatLine() {
        return name + "  ATK:" + attack + "  HP:" + currentHealth + "/" + maxHealth + "  DEF:" + defense + (downed ? "  DOWN" : "");
    }
}
