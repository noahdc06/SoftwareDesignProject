package com.legends.model;

public class Enemy {
    private String type;
    private int hitCount = 0;
    private boolean isAlive = true;

    public Enemy(String type) {
        this.type = type;
    }

    public void takeHit() {
        hitCount++;
        if (hitCount >= 3) {
            isAlive = false;
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getType() {
        return type;
    }
}
