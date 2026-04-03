package com.legends.model;

public class Hero extends Unit {

    private final HeroClass heroClass;//defines scaling + abilities

    public Hero(HeroClass heroClass, int partyLevel) {
        super(heroClass.getDisplayName(), 2, 30, 0);//base stats
        this.heroClass = heroClass;
        applyLevel(partyLevel);//apply level-based stat scaling
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    //Scales stats based on party level
    private void applyLevel(int partyLevel) {

        setAttack(getAttack() + heroClass.getAttackPerLevel() * partyLevel);
        setMaxHealth(getMaxHealth() + heroClass.getMaxHealthPerLevel() * partyLevel);
        setDefense(getDefense() + heroClass.getDefensePerLevel() * partyLevel);

        fullyRestore();
    }

    public String getSpell1Name() {
        return heroClass.getSpell1();
    }

    public String getSpell2Name() {
        return heroClass.getSpell2();
    }
}