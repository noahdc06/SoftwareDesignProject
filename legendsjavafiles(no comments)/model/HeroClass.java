package com.legends.model;

public enum HeroClass {
    ROGUE("Rogue", 4, 4, 1, "SLASH", "TRICK"),
    WARRIOR("Warrior", 2, 8, 2, "SWING", "BLOCK"),
    MAGE("Mage", 3, 4, 2, "BLAST", "BURST"),
    CLERIC("Cleric", 2, 6, 3, "SALVE", "RENEW");

    private final String displayName;
    private final int attackPerLevel;
    private final int maxHealthPerLevel;
    private final int defensePerLevel;
    private final String spell1;
    private final String spell2;

    HeroClass(String displayName, int attackPerLevel, int maxHealthPerLevel, int defensePerLevel, String spell1, String spell2) {
        this.displayName = displayName;
        this.attackPerLevel = attackPerLevel;
        this.maxHealthPerLevel = maxHealthPerLevel;
        this.defensePerLevel = defensePerLevel;
        this.spell1 = spell1;
        this.spell2 = spell2;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAttackPerLevel() {
        return attackPerLevel;
    }

    public int getMaxHealthPerLevel() {
        return maxHealthPerLevel;
    }

    public int getDefensePerLevel() {
        return defensePerLevel;
    }

    public String getSpell1() {
        return spell1;
    }

    public String getSpell2() {
        return spell2;
    }
}
