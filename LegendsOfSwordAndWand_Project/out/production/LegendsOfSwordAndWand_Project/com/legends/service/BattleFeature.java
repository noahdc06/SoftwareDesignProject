package com.legends.service;

import com.legends.model.Profile;
import com.legends.model.Enemy;
import com.legends.patterns.*;
import com.legends.database.DatabaseConnection;

public class BattleFeature {

    private Profile player;
    private Enemy enemy;

    public BattleFeature(Profile player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public void startBattle() {
        System.out.println("Battle started against " + enemy.getType());

        while (player.isAlive() && enemy.isAlive()) {
            playerTurn();
            enemyTurn();
        }

        if (!player.isAlive()) {
            DatabaseConnection.getInstance().saveDeath(player.getUsername());
            System.out.println("Player died!");
        } else {
            System.out.println("Enemy defeated!");
        }
    }

    private void playerTurn() {
        PlayerAction action;

        int choice = 1; // change later to input

        if (choice == 1) {
            action = new AttackAction();
        } else {
            action = new DefendAction();
        }

        action.execute(player, enemy);
    }

    private void enemyTurn() {
        if (enemy.isAlive()) {
            System.out.println("Enemy attacks!");
            player.takeHit();
        }
    }
}
