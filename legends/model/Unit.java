package com.legends.model;

public abstract class Unit {
    private String name;
    //Unit stats
    private int attack;
    private int maxHealth;
    private int currentHealth;
    private int defense;
    private boolean downed;//downed status, unit can no longer act or heal

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
        this.attack = Math.max(0, attack);//attack must always be positive integer
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(1, maxHealth);//minimum health is 1
        if (currentHealth > this.maxHealth) {
            currentHealth = this.maxHealth;//cap current health
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
        if (this.currentHealth <= 0) {
            this.currentHealth = 0;
            this.downed = true;//mark as defeated
        }
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = Math.max(0, defense);//prevent negative defense
    }

    public boolean isDowned() {
        return downed;
    }

    public void setDowned(boolean downed) {
        this.downed = downed;
        if (downed && currentHealth > 0) {
            currentHealth = 0;//force health to 0 if downed
        }
    }

    public void heal(int amount) {
        if (downed || amount <= 0) {
            return;//cannot heal if downed or invalid amount
        }
        currentHealth = Math.min(maxHealth, currentHealth + amount);//cap at max health
    }

    public void fullyRestore() {
        downed = false;
        currentHealth = maxHealth;//restore to full health
    }

    public int dealDamageTo(Unit target) {
        if (target.isDowned()) {
            return 0;//no damage to downed targets (damage appears as "0" in logs, health does not change)
        }
        int damage = Math.max(0, attack - target.getDefense());//attack - damage = damage taken
        target.takeDamageRaw(damage);
        return damage;
    }

    public void takeDamageRaw(int damage) {
        int actual = Math.max(0, damage);
        currentHealth -= actual;
        if (currentHealth <= 0) {
            currentHealth = 0;
            downed = true;//mark as DOWN
        }
    }

    public String getStatLine() {
        return name + "  ATK:" + attack + "  HP:" + currentHealth + "/" + maxHealth + "  DEF:" + defense + (downed ? "  DOWN" : "");
    }
}