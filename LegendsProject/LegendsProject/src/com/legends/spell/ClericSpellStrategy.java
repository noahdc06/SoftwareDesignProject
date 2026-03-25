package com.legends.spell;

import com.legends.model.Hero;
import com.legends.model.Party;
import com.legends.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClericSpellStrategy implements SpellStrategy {
    public String useSpell1(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        for (Unit unit : actorParty.getUnits()) {
            if (!unit.isDowned()) {
                int healAmount = (int) Math.ceil(unit.getMaxHealth() * 0.20);
                unit.heal(healAmount);
            }
        }
        return hero.getName() + " used SALVE and healed all non-downed allies for 20% of their max health.";
    }

    public String useSpell2(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        List<Unit> validTargets = new ArrayList<>();
        for (Unit unit : actorParty.getUnits()) {
            if (!unit.isDowned()) {
                validTargets.add(unit);
            }
        }

        if (validTargets.isEmpty()) {
            return hero.getName() + " used RENEW but no valid ally could be restored.";
        }

        Unit target = validTargets.get(random.nextInt(validTargets.size()));
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