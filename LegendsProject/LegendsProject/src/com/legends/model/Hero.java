package com.legends.model;

public class Hero extends Unit {
    private final HeroClass heroClass;

    public Hero(HeroClass heroClass, int partyLevel) {
        super(heroClass.getDisplayName(), 2, 30, 0);
        this.heroClass = heroClass;
        applyLevel(partyLevel);
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    private void applyLevel(int partyLevel) {
        int extraLevels = Math.max(0, partyLevel);
        setAttack(getAttack() + heroClass.getAttackPerLevel() * extraLevels);
        setMaxHealth(getMaxHealth() + heroClass.getMaxHealthPerLevel() * extraLevels);
        setDefense(getDefense() + heroClass.getDefensePerLevel() * extraLevels);
        fullyRestore();
    }

    public String getSpell1Name() {
        return heroClass.getSpell1();
    }

    public String getSpell2Name() {
        return heroClass.getSpell2();
    }
}
