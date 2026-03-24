package com.legends.Design_Patterns;

import com.legends.model.Profile;
import com.legends.model.Enemy;

public class AttackAction implements PlayerAction {
    public void execute(Profile player, Enemy enemy) {
        System.out.println("Player attacks!");
        enemy.takeHit();
    }
}
