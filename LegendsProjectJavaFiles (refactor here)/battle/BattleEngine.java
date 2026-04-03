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

        createParties(partyLevel);   //build hero + enemy parties
        buildTurnOrder();            //define turn sequence

        this.turnIndex = 0;
        advanceToNextActiveTurn();   //skip downed units' turns
    }

    //Create hero party and enemy party based on mode (PVE or PVP)
    private void createParties(int partyLevel) {
        List<Hero> heroes = new ArrayList<>();
        heroes.add(new Hero(HeroClass.ROGUE, partyLevel));
        heroes.add(new Hero(HeroClass.WARRIOR, partyLevel));
        heroes.add(new Hero(HeroClass.MAGE, partyLevel));
        heroes.add(new Hero(HeroClass.CLERIC, partyLevel));

        if (mode == BattleMode.PVP) {
            renamePvPHeroes(heroes, "1"); // label Player 1 heroes
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

            renamePvPHeroes(enemies, "2"); // label Player 2 heroes
            enemiesParty = new Party(enemies);
        }
    }

    //Rename heroes during PvP (Team 1 and Team 2)
    private void renamePvPHeroes(List<Hero> heroes, String playerNumber) {
        for (Hero hero : heroes) {
            hero.setName(hero.getHeroClass().getDisplayName() + " " + playerNumber);
        }
    }

    //Turn order is based on hero class
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

    public Party getHeroesParty() { return heroesParty; }
    public Party getEnemiesParty() { return enemiesParty; }
    public String getBattleLog() { return battleLog; }
    public boolean isFinished() { return finished; }
    public String getWinnerText() { return winnerText; }

    public Unit getCurrentUnit() {
        return turnOrder.get(turnIndex);
    }

    //Only heroes are player-controlled
    public boolean isCurrentTurnPlayerControlled() {
        return !finished && getCurrentUnit() instanceof Hero;
    }

    public boolean isCurrentTurnLeftSide() {
        return heroesParty.getUnits().contains(getCurrentUnit());
    }

    public String getCurrentTurnLabel() {
        return getCurrentUnit().getName() + "'s turn";
    }

    //Return action labels based on hero
    public String[] getCurrentActionLabels() {
        Unit unit = getCurrentUnit();
        if (unit instanceof Hero hero) {
            return new String[]{"Attack", "Recover", hero.getSpell1Name(), hero.getSpell2Name()};
        }
        return new String[]{"Attack", "Recover", "Spell 1", "Spell 2"};
    }

    //Gets description from Spell package
    public String getSpellDescription(Hero hero, boolean spell1) {
        SpellStrategy strategy = getSpellStrategy(hero);
        return spell1 ? strategy.getSpell1Description() : strategy.getSpell2Description();
    }

    //Handles player input
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

        String message = resolvePlayerAction(hero, actorParty, targetParty, action);
        nextTurn(message);
    }

    private String resolvePlayerAction(Hero hero, Party actorParty, Party targetParty, AttackCommand action) {
        return switch (action.getCommand()) {
            case ATTACK -> handleAttack(hero, targetParty, action.getTargetIndex());
            case RECOVER -> handleRecover(hero);
            case SPELL_1 -> useSpell1(hero, actorParty, targetParty);
            case SPELL_2 -> useSpell2(hero, actorParty, targetParty);
            default -> hero.getName() + " did nothing.";
        };
    }

    private String handleAttack(Hero hero, Party targetParty, int targetIndex) {
        Unit target = targetParty.getByIndex(targetIndex);
        if (target == null) {
            return hero.getName() + " could not find a target.";
        }
        int damage = hero.dealDamageTo(target);
        return hero.getName() + " attacked " + target.getName() + " for " + damage + " damage.";
    }

    private String handleRecover(Hero hero) {
        int healAmount = hero.getAttack();
        hero.heal(healAmount);
        return hero.getName() + " recovered " + healAmount + " health.";
    }

    //Spells uses the Spell package
    private String useSpell1(Hero hero, Party actorParty, Party targetParty) {
        return getSpellStrategy(hero).useSpell1(hero, actorParty, targetParty, random, getPartyLevel(actorParty));
    }

    private String useSpell2(Hero hero, Party actorParty, Party targetParty) {
        return getSpellStrategy(hero).useSpell2(hero, actorParty, targetParty, random, getPartyLevel(actorParty));
    }

    //Hero stats is not stored in BattleEngine so party level is derived from Rogue's attack
    private int getPartyLevel(Party party) {
        Hero firstHero = party.getHeroes().get(0);
        int baseAttack = 2;
        int perLevel = firstHero.getHeroClass().getAttackPerLevel();
        return (firstHero.getAttack() - baseAttack) / Math.max(1, perLevel);
    }

    //Random targeting (can include downed units intentionally)
    private Unit randomTarget(List<Unit> units) {
        return units.get(random.nextInt(units.size()));
    }

    //Runs enemy turns until player input is needed
    public void performAutomaticTurns() {
        while (!finished && !isCurrentTurnPlayerControlled()) {
            performEnemyAction();
        }
    }

    //Enemy random target selection
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

    //Move to next turn and update battle state
    private void nextTurn(String message) {
        battleLog = message;
        evaluateBattleState();

        if (finished) return;

        turnIndex = (turnIndex + 1) % turnOrder.size();
        advanceToNextActiveTurn();
    }

    //Skip downed units automatically
    private void advanceToNextActiveTurn() {
        if (finished) return;

        int checked = 0;

        while (checked < turnOrder.size()) {
            Unit current = turnOrder.get(turnIndex);

            if (!current.isDowned()) return;

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

    //Checks if battle is complete
    private void evaluateBattleState() {
        if (heroesParty.allDowned()) {
            finished = true;
            winnerText = mode == BattleMode.PVE
                    ? "Game over. The enemies won."
                    : "Player 2 wins.";
            return;
        }

        if (enemiesParty.allDowned()) {
            finished = true;
            winnerText = mode == BattleMode.PVE
                    ? "Victory! You won the battle."
                    : "Player 1 wins.";
        }
    }

    //Restore all heroes upon reaching an inn
    public void fullyRestoreHeroesAtInn() {
        for (Unit unit : heroesParty.getUnits()) {
            unit.fullyRestore();
        }
    }

    //Strategy spell selection based on hero class
    private SpellStrategy getSpellStrategy(Hero hero) {
        return switch (hero.getHeroClass()) {
            case ROGUE -> new RogueSpellStrategy();
            case WARRIOR -> new WarriorSpellStrategy();
            case MAGE -> new MageSpellStrategy();
            case CLERIC -> new ClericSpellStrategy();
        };
    }
}
