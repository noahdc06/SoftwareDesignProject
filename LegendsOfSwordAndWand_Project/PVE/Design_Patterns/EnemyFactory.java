package com.legends.Design_Patterns;

import com.legends.model.Enemy;

public class EnemyFactory {

    public static Enemy createEnemy(String type) {
        return new Enemy(type);
    }
}
