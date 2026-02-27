package com.legends.model;

public class Hero {
    private String name;
    private int attack = 5;
    private int defense = 5;
    private int hp = 100;
    private int mana = 50;

    public Hero(String name) {
        this.name = name;
    }

    public void attack(Hero target) {
        int damage = Math.max(0, this.attack - target.defense);
        target.hp -= damage;
        System.out.println(name + " attacks " + target.name + " for " + damage + " damage.");
    }

    public void defend() {
        hp += 10;
        mana += 5;
        System.out.println(name + " defends and restores 10 HP + 5 Mana.");
    }

    public boolean isAlive() {
        return hp > 0;
    }
}