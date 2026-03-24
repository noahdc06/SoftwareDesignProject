package com.legends.Design_Patterns;

import com.legends.model.Profile;
import com.legends.model.Enemy;

public class DefendAction implements PlayerAction {
    public void execute(Profile player, Enemy enemy) {
        System.out.println("Player defends!");
        player.setDefending(true);
    }
}
