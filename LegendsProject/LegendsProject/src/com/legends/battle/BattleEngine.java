package com.legends.battle;

import com.legends.model.Enemy;
import com.legends.model.Hero;
import com.legends.model.HeroClass;
import com.legends.model.Party;
import com.legends.model.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        HeroClass heroClass = hero.getHeroClass();
        if (heroClass == HeroClass.ROGUE) {
            return spell1 ? " Attack a random enemy 0-3 times."
                    : " Reduce all enemy defense.";
        }
        if (heroClass == HeroClass.WARRIOR) {
            return spell1 ? " Attack two random enemies."
                    : " Increase own defense by 2.";
        }
        if (heroClass == HeroClass.MAGE) {
            return spell1 ? " Deal double attack damage to a random enemy."
                    : " Deal attack damage to all units, including allies and self.";
        }
        return spell1 ? " Heal all non-downed allies for 20% of their max health."
                : " Fully restore 1 random ally. Might be wasted if an ally is downed.";
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
        if (hero.getHeroClass() == HeroClass.ROGUE) {
            Unit target = randomTarget(targetParty.getUnits());
            int multiplier = random.nextInt(4);
            int damage = Math.max(0, hero.getAttack() * multiplier - target.getDefense());
            target.takeDamageRaw(damage);
            return hero.getName() + " used SLASH on " + target.getName() + " for " + damage + " damage. Multiplier: " + multiplier + ".";
        }

        if (hero.getHeroClass() == HeroClass.WARRIOR) {
            Unit target1 = randomTarget(targetParty.getUnits());
            Unit target2 = randomTarget(targetParty.getUnits());

            int damage1 = hero.dealDamageTo(target1);
            int damage2 = hero.dealDamageTo(target2);

            return hero.getName() + " used SWING on " +
                    target1.getName() + " for " + damage1 + " damage and " +
                    target2.getName() + " for " + damage2 + " damage.";
        }

        if (hero.getHeroClass() == HeroClass.MAGE) {
            Unit target = randomTarget(targetParty.getUnits());
            int damage = Math.max(0, hero.getAttack() * 2 - target.getDefense());
            target.takeDamageRaw(damage);
            return hero.getName() + " used BLAST on " + target.getName() + " for " + damage + " damage.";
        }

        for (Unit unit : actorParty.getUnits()) {
            if (!unit.isDowned()) {
                int healAmount = (int) Math.ceil(unit.getMaxHealth() * 0.20);
                unit.heal(healAmount);
            }
        }
        return hero.getName() + " used SALVE and healed allies for 20% health.";
    }

    private String useSpell2(Hero hero, Party actorParty, Party targetParty) {
        if (hero.getHeroClass() == HeroClass.ROGUE) {
            for (Unit unit : targetParty.getUnits()) {
                unit.setDefense(Math.max(0, unit.getDefense() - 1));
            }
            return hero.getName() + " used TRICK and reduced all enemy defense by 1.";
        }

        if (hero.getHeroClass() == HeroClass.WARRIOR) {
            hero.setDefense(hero.getDefense() + 4);
            return hero.getName() + " used BLOCK and gained 4 defense.";
        }

        if (hero.getHeroClass() == HeroClass.MAGE) {
            for (Unit unit : actorParty.getUnits()) {
                hero.dealDamageTo(unit);
            }
            for (Unit unit : targetParty.getUnits()) {
                hero.dealDamageTo(unit);
            }
            return hero.getName() + " used BURST. Everyone took damage.";
        }

        List<Unit> validTargets = new ArrayList<>();
        for (Unit unit : actorParty.getUnits()) {
            if (!unit.isDowned()) {
                validTargets.add(unit);
            }
        }

        if (validTargets.isEmpty()) {
            return hero.getName() + " used RENEW but no valid ally could be restored.";
        }

        Unit target = randomTarget(validTargets);
        target.fullyRestore();
        return hero.getName() + " used RENEW and restored " + target.getName() + " to full health.";
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

    public void restoreHeroesForNextBattle() {
        for (Unit unit : heroesParty.getUnits()) {
            if (!unit.isDowned()) {
                unit.fullyRestore();
            }
        }
    }

    public void fullyRestoreHeroesAtInn() {
        for (Unit unit : heroesParty.getUnits()) {
            unit.fullyRestore();
        }
    }
}