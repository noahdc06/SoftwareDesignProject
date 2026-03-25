package com.legends.spell;

import com.legends.model.Hero;
import com.legends.model.Party;

import java.util.Random;

public interface SpellStrategy {
    String useSpell1(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel);
    String useSpell2(Hero hero, Party actorParty, Party targetParty, Random random, int partyLevel);
    String getSpell1Description();
    String getSpell2Description();
}