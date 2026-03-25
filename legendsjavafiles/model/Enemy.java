package com.legends.model;

import java.util.Random;

public class Enemy extends Unit {
    public Enemy(String name, int partyLevel, Random random) {
        super(name, 2, 20, 0);
        applyScaling(partyLevel, random);
        fullyRestore();
    }

    private void applyScaling(int partyLevel, Random random) {
        for (int i = 0; i < partyLevel * 3; i++) {
            int roll = random.nextInt(3);
            if (roll == 0) {
                setAttack(getAttack() + 3);
            } else if (roll == 1) {
                setMaxHealth(getMaxHealth() + 6);
            } else {
                setDefense(getDefense() + 1);
            }
        }
    }
}
