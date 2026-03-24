package com.legends.Design_Patterns;

import com.legends.model.Profile;
import com.legends.model.Enemy;

public interface PlayerAction {
    void execute(Profile player, Enemy enemy);
}
