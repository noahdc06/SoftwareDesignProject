package com.legends.spell;

import com.legends.model.Hero;
import com.legends.model.Party;
import com.legends.model.Unit;

import java.util.List;
import java.util.Random;

public class WarriorSpellStrategy implements SpellStrategy {
    public String useSpell1(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        Unit target1 = randomTarget(targetParty.getUnits(), random);
        Unit target2 = randomTarget(targetParty.getUnits(), random);

        int damage1 = hero.dealDamageTo(target1);
        int damage2 = hero.dealDamageTo(target2);

        return hero.getName() + " used SWING on " +
                target1.getName() + " for " + damage1 + " damage and " +
                target2.getName() + " for " + damage2 + " damage.";
    }

    public String useSpell2(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        hero.setDefense(hero.getDefense() + 2);
        return hero.getName() + " used BLOCK and gained 2 defense.";
    }

    public String getSpell1Description() {
        return "SWING: Deal attack damage to 2 random enemies.";
    }

    public String getSpell2Description() {
        return "BLOCK: Increase own defense by 2.";
    }

    private Unit randomTarget(List<Unit> units, Random random) {
        return units.get(random.nextInt(units.size()));
    }
}