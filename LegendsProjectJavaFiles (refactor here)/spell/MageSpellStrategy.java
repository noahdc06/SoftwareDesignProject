package com.legends.spell;

import com.legends.model.Hero;
import com.legends.model.Party;
import com.legends.model.Unit;

import java.util.List;
import java.util.Random;

public class MageSpellStrategy implements SpellStrategy {
    //Spell 1 is a high-damage random target attack. Uses 2x attack stat but can be wasted on downed enemies.
    public String useSpell1(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        Unit target = randomTarget(targetParty.getUnits(), random);
        int damage = Math.max(0, hero.getAttack() * 2 - target.getDefense());
        target.takeDamageRaw(damage);
        return hero.getName() + " used BLAST on " + target.getName() + " for " + damage + " damage.";
    }
    //Spell 2 is a room-wide attack. Hits everyone, including self.
    public String useSpell2(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel) {
        for (Unit unit : actorParty.getUnits()) {
            hero.dealDamageTo(unit);
        }//hits all allies
        for (Unit unit : targetParty.getUnits()) {
            hero.dealDamageTo(unit);
        }//hits all enemies
        return hero.getName() + " used BURST. Everyone took damage.";
    }

    public String getSpell1Description() {
        return "BLAST: Deal double attack damage to 1 random enemy.";
    }

    public String getSpell2Description() {
        return "BURST: Deal attack damage to all units, including allies and self.";
    }

    private Unit randomTarget(List<Unit> units, Random random) {
        return units.get(random.nextInt(units.size()));
    }
}