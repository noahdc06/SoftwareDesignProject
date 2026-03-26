package com.legends.model;

import java.util.Random;

public class Enemy extends Unit {

    public Enemy(String name, int partyLevel, Random random) {
        super(name, 2, 20, 0);//base enemy stats
        applyScaling(partyLevel, random);
        fullyRestore();//restore HP after max health changes
    }

    //Randomly increases enemy stats by three random bonuses as party level rises
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