package com.legends.spell;

import com.legends.model.Hero;
import com.legends.model.Party;
import com.legends.model.Unit;

import java.util.List;
import java.util.Random;

public class RogueSpellStrategy implements SpellStrategy {
    public String useSpell1(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        Unit target = randomTarget(targetParty.getUnits(), random);
        int multiplier = random.nextInt(4);
        int damage = Math.max(0, hero.getAttack() * multiplier - target.getDefense());
        target.takeDamageRaw(damage);
        return hero.getName() + " used SLASH on " + target.getName() + " for " + damage + " damage. Multiplier: " + multiplier + ".";
    }

    public String useSpell2(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        for (Unit unit : targetParty.getUnits()) {
            unit.setDefense(Math.max(0, unit.getDefense() - 1));
        }
        return hero.getName() + " used TRICK and reduced all enemy defense by 1.";
    }

    public String getSpell1Description() {
        return "SLASH: Hits 1 random enemy for attack x random multiplier 0-3.";
    }

    public String getSpell2Description() {
        return "TRICK: Reduce all enemy defense by 1.";
    }

    private Unit randomTarget(List<Unit> units, Random random) {
        return units.get(random.nextInt(units.size()));
    }
}