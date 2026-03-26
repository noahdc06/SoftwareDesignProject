package com.legends.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Party {
    private final List<Unit> units;//stores all units in the party

    public Party(List<? extends Unit> units) {
        this.units = new ArrayList<>(units);
    }

    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);//party cannot be changed externally
    }

    public List<Hero> getHeroes() {
        List<Hero> heroes = new ArrayList<>();
        for (Unit unit : units) {
            if (unit instanceof Hero hero) {
                heroes.add(hero);//collect heroes in party
            }
        }
        return heroes;
    }

    public boolean allDowned() {
        for (Unit unit : units) {
            if (!unit.isDowned()) {
                return false;//at least one unit still alive
            }
        }
        return true;//all units are downed, game over/victory occurs
    }

    public Unit getByIndex(int index) {
        if (index < 0 || index >= units.size()) {
            return null;
        }
        return units.get(index);//return unit at target position
    }
}