package com.legends.battle;

import com.legends.model.Enemy;
import com.legends.model.Hero;
import com.legends.model.HeroClass;
import com.legends.model.Party;
import com.legends.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.legends.spell.ClericSpellStrategy;
import com.legends.spell.MageSpellStrategy;
import com.legends.spell.RogueSpellStrategy;
import com.legends.spell.SpellStrategy;
import com.legends.spell.WarriorSpellStrategy;

public class BattleEngine {
    private final BattleMode mode;
    private final Random random;
    private Party heroesParty;
    private Party enemiesParty;
    private int turnIndex;
    private final List<Unit> turnOrder;
    private String battleLog;
    private boolean finished;
    private String winnerText;

    public BattleEngine(int partyLevel, BattleMode mode) {
        this.mode = mode;
        this.random = new Random();
        this.turnOrder = new ArrayList<>();
        this.battleLog = "Battle started.";
        createParties(partyLevel);
        buildTurnOrder();
        this.turnIndex = 0;
        advanceToNextActiveTurn();
    }

    private void createParties(int partyLevel) {
        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero(HeroClass.ROGUE, partyLevel));
        heroes.add(new Hero(HeroClass.WARRIOR, partyLevel));
        heroes.add(new Hero(HeroClass.MAGE, partyLevel));
        heroes.add(new Hero(HeroClass.CLERIC, partyLevel));
        if (mode == BattleMode.PVP) {
            renamePvPHeroes(heroes, "1");
        }
        heroesParty = new Party(heroes);

        if (mode == BattleMode.PVE) {
            List<Enemy> enemies = new ArrayList<>();
            enemies.add(new Enemy("Enemy 1", partyLevel, random));
            enemies.add(new Enemy("Enemy 2", partyLevel, random));
            enemies.add(new Enemy("Enemy 3", partyLevel, random));
            enemies.add(new Enemy("Enemy 4", partyLevel, random));
            enemiesParty = new Party(enemies);
        } else {
            List<Hero> enemies = new ArrayList<>();
            enemies.add(new Hero(HeroClass.ROGUE, partyLevel));
            enemies.add(new Hero(HeroClass.WARRIOR, partyLevel));
            enemies.add(new Hero(HeroClass.MAGE, partyLevel));
            enemies.add(new Hero(HeroClass.CLERIC, partyLevel));
            renamePvPHeroes(enemies, "2");
            enemiesParty = new Party(enemies);
        }
    }

    private void renamePvPHeroes(List<Hero> heroes, String playerNumber) {
        for (Hero hero : heroes) {
            hero.setName(hero.getHeroClass().getDisplayName() + " " + playerNumber);
        }
    }

    private void buildTurnOrder() {
        turnOrder.clear();
        turnOrder.add(heroesParty.getByIndex(0));
        turnOrder.add(enemiesParty.getByIndex(0));
        turnOrder.add(heroesParty.getByIndex(1));
        turnOrder.add(enemiesParty.getByIndex(1));
        turnOrder.add(heroesParty.getByIndex(2));
        turnOrder.add(enemiesParty.getByIndex(2));
        turnOrder.add(heroesParty.getByIndex(3));
        turnOrder.add(enemiesParty.getByIndex(3));
    }

    public Party getHeroesParty() {
        return heroesParty;
    }

    public Party getEnemiesParty() {
        return enemiesParty;
    }

    public String getBattleLog() {
        return battleLog;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getWinnerText() {
        return winnerText;
    }

    public Unit getCurrentUnit() {
        return turnOrder.get(turnIndex);
    }

    public boolean isCurrentTurnPlayerControlled() {
        return !finished && getCurrentUnit() instanceof Hero;
    }

    public boolean isCurrentTurnLeftSide() {
        return heroesParty.getUnits().contains(getCurrentUnit());
    }

    public String getCurrentTurnLabel() {
        return getCurrentUnit().getName() + "'s turn";
    }

    public String[] getCurrentActionLabels() {
        Unit unit = getCurrentUnit();
        if (unit instanceof Hero hero) {
            return new String[]{"Attack", "Recover", hero.getSpell1Name(), hero.getSpell2Name()};
        }
        return new String[]{"Attack", "Recover", "Spell 1", "Spell 2"};
    }

    public String getSpellDescription(Hero hero, boolean spell1) {
        SpellStrategy strategy = getSpellStrategy(hero);
        return spell1 ? strategy.getSpell1Description() : strategy.getSpell2Description();
    }

    public void performPlayerAction(AttackCommand action) {
        if (finished || !isCurrentTurnPlayerControlled()) {
            return;
        }

        Unit actor = getCurrentUnit();
        if (actor.isDowned()) {
            nextTurn(actor.getName() + " is downed and skips the turn.");
            return;
        }

        if (!(actor instanceof Hero hero)) {
            return;
        }

        Party actorParty = heroesParty.getUnits().contains(actor) ? heroesParty : enemiesParty;
        Party targetParty = actorParty == heroesParty ? enemiesParty : heroesParty;
        String message;

        switch (action.getCommand()) {
            case ATTACK -> {
                Unit target = targetParty.getByIndex(action.getTargetIndex());
                if (target == null) {
                    message = hero.getName() + " could not find a target.";
                } else {
                    int damage = hero.dealDamageTo(target);
                    message = hero.getName() + " attacked " + target.getName() + " for " + damage + " damage.";
                }
            }
            case RECOVER -> {
                int healAmount = hero.getAttack();
                hero.heal(healAmount);
                message = hero.getName() + " recovered " + healAmount + " health.";
            }
            case SPELL_1 -> message = useSpell1(hero, actorParty, targetParty);
            case SPELL_2 -> message = useSpell2(hero, actorParty, targetParty);
            default -> message = hero.getName() + " did nothing.";
        }

        nextTurn(message);
    }

    private String useSpell1(Hero hero, Party actorParty, Party targetParty) {
        return getSpellStrategy(hero).useSpell1(hero, actorParty, targetParty, random, getPartyLevel(actorParty));
    }

    private String useSpell2(Hero hero, Party actorParty, Party targetParty) {
        return getSpellStrategy(hero).useSpell2(hero, actorParty, targetParty, random, getPartyLevel(actorParty));
    }

    private int getPartyLevel(Party party) {
        Hero firstHero = party.getHeroes().get(0);
        int baseAttack = 5;
        int perLevel = firstHero.getHeroClass().getAttackPerLevel();
        return ((firstHero.getAttack() - baseAttack) / Math.max(1, perLevel)) + 1;
    }


    private Unit randomTarget(List<Unit> units) {
        return units.get(random.nextInt(units.size()));
    }

    public void performAutomaticTurnsUntilPlayerNeeded() {
        while (!finished && !isCurrentTurnPlayerControlled()) {
            performEnemyAction();
        }
    }

    private void performEnemyAction() {
        Unit actor = getCurrentUnit();
        if (actor.isDowned()) {
            nextTurn(actor.getName() + " is downed and skips the turn.");
            return;
        }

        Party targetParty = isCurrentTurnLeftSide() ? enemiesParty : heroesParty;
        Unit target = randomTarget(targetParty.getUnits());
        int damage = actor.dealDamageTo(target);
        nextTurn(actor.getName() + " attacked " + target.getName() + " for " + damage + " damage.");
    }

    private void nextTurn(String message) {
        battleLog = message;
        evaluateBattleState();
        if (finished) {
            return;
        }
        turnIndex = (turnIndex + 1) % turnOrder.size();
        advanceToNextActiveTurn();
    }

    private void advanceToNextActiveTurn() {
        if (finished) {
            return;
        }

        int checked = 0;
        while (checked < turnOrder.size()) {
            Unit current = turnOrder.get(turnIndex);

            if (!current.isDowned()) {
                return;
            }

            if (battleLog == null || battleLog.isBlank()) {
                battleLog = current.getName() + " turn skipped (downed).";
            } else {
                battleLog += "\n" + current.getName() + " turn skipped (downed).";
            }

            turnIndex = (turnIndex + 1) % turnOrder.size();
            checked++;
        }

        evaluateBattleState();
    }

    private void evaluateBattleState() {
        if (heroesParty.allDowned()) {
            finished = true;
            winnerText = mode == BattleMode.PVE ? "Game over. The enemies won." : "Player 2 wins.";
            return;
        }

        if (enemiesParty.allDowned()) {
            finished = true;
            winnerText = mode == BattleMode.PVE ? "Victory! You won the battle." : "Player 1 wins.";
        }
    }

    public void fullyRestoreHeroesAtInn() {
        for (Unit unit : heroesParty.getUnits()) {
            unit.fullyRestore();
        }
    }

    private SpellStrategy getSpellStrategy(Hero hero) {
        return switch (hero.getHeroClass()) {
            case ROGUE -> new RogueSpellStrategy();
            case WARRIOR -> new WarriorSpellStrategy();
            case MAGE -> new MageSpellStrategy();
            case CLERIC -> new ClericSpellStrategy();
        };
    }
}