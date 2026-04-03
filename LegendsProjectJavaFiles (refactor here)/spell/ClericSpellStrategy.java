package com.legends.spell;

import com.legends.model.Hero;
import com.legends.model.Party;
import com.legends.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClericSpellStrategy implements SpellStrategy {
    //Spell 1 is a 20% max health heal for the whole party. It has no effect on downed allies.
    public String useSpell1(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        for (Unit unit : actorParty.getUnits()) {
            if (!unit.isDowned()) {
                int healAmount = (int) Math.ceil(unit.getMaxHealth() * 0.20);//20% heal rounded up
                unit.heal(healAmount);
            }
        }
        return hero.getName() + " used SALVE and healed all non-downed allies for 20% of their max health.";
    }
    //Spell 2 is a full health restoration for a random friendly. It can be wasted if a party member is downed.
    public String useSpell2(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        List<Unit> targets = actorParty.getUnits();

        Unit target = targets.get(random.nextInt(targets.size()));//random chosen ally

        if (target.isDowned()) {
            return hero.getName() + " used RENEW but it was wasted on " + target.getName() + " (downed).";
        }

        target.fullyRestore();
        return hero.getName() + " used RENEW and restored " + target.getName() + " to full health.";
    }

    public String getSpell1Description() {
        return "SALVE: Heal all non-downed allies for 20% of their max health.";
    }

    public String getSpell2Description() {
        return "RENEW: Fully restore 1 random non-downed ally.";
    }
}