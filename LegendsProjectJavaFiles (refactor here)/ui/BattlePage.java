package com.legends.ui;

import com.legends.battle.AttackCommand;
import com.legends.battle.BattleEngine;
import com.legends.battle.BattleMode;
import com.legends.battle.Command;
import com.legends.campaign.PvECampaign;
import com.legends.database.User;
import com.legends.model.Hero;
import com.legends.model.Party;
import com.legends.model.Unit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedList;

public class BattlePage extends JFrame {
    //Final variables for Java Swing GUI
    private final HomePage homePage;
    private final PvECampaign campaign;
    private final BattleMode mode;
    private final BattleEngine engine;

    private final JLabel topLabel;
    private final JTextArea leftArea;
    private final JTextArea rightArea;
    private final JTextArea logArea;
    private final JTextArea spellInfoArea;

    private final JComboBox<String> targetBox;

    private final JButton attackButton;
    private final JButton recoverButton;
    private final JButton spell1Button;
    private final JButton spell2Button;

    private final LinkedList<String> battleHistory;//stores recent battle messages

    public BattlePage(HomePage homePage, PvECampaign campaign, BattleMode mode, int partyLevel) {
        this.homePage = homePage;
        this.campaign = campaign;
        this.mode = mode;
        this.engine = new BattleEngine(partyLevel, mode);
        this.battleHistory = new LinkedList<>();

        //Window creation
        setTitle(mode == BattleMode.PVE ? "PvE Battle" : "PvP Battle");
        setSize(980, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        UIStyle.styleFrame(this);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        UIStyle.stylePanel(root);
        root.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        topLabel = new JLabel("", JLabel.CENTER);
        topLabel.setFont(UIStyle.BODY_FONT);
        root.add(topLabel, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 8, 8));
        UIStyle.stylePanel(center);

        leftArea = new JTextArea();
        rightArea = new JTextArea();
        UIStyle.styleTextArea(leftArea);
        UIStyle.styleTextArea(rightArea);

        center.add(new JScrollPane(leftArea));
        center.add(new JScrollPane(rightArea));
        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        UIStyle.stylePanel(bottom);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        UIStyle.stylePanel(infoPanel);

        logArea = new JTextArea(5, 30);
        spellInfoArea = new JTextArea(2, 30);

        UIStyle.styleTextArea(logArea);
        UIStyle.styleTextArea(spellInfoArea);

        logArea.setEditable(false);
        spellInfoArea.setEditable(false);

        JPanel logPanel = new JPanel(new BorderLayout());
        UIStyle.stylePanel(logPanel);
        logPanel.add(new JLabel("Battle Log"), BorderLayout.NORTH);
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        UIStyle.stylePanel(descriptionPanel);
        descriptionPanel.add(new JLabel("Description"), BorderLayout.NORTH);
        descriptionPanel.add(new JScrollPane(spellInfoArea), BorderLayout.CENTER);

        infoPanel.add(logPanel);
        infoPanel.add(descriptionPanel);
        bottom.add(infoPanel, BorderLayout.CENTER);

        JPanel controls = new JPanel(new GridLayout(2, 3, 8, 8));
        UIStyle.stylePanel(controls);

        targetBox = new JComboBox<>();

        attackButton = new JButton("Attack");
        recoverButton = new JButton("Recover");
        spell1Button = new JButton("Spell 1");
        spell2Button = new JButton("Spell 2");

        JButton menuButton = new JButton(mode == BattleMode.PVE ? "Exit to Menu" : "Close");

        for (JButton b : new JButton[]{attackButton, recoverButton, spell1Button, spell2Button, menuButton}) {
            UIStyle.styleButton(b);
        }

        attackButton.addActionListener(e -> doAction(Command.ATTACK));
        recoverButton.addActionListener(e -> doAction(Command.RECOVER));
        spell1Button.addActionListener(e -> doAction(Command.SPELL_1));
        spell2Button.addActionListener(e -> doAction(Command.SPELL_2));

        menuButton.addActionListener(e -> {
            homePage.refreshInfo();
            homePage.setVisible(true);
            dispose();//close battle screen
        });

        controls.add(new JLabel("Target:"));
        controls.add(targetBox);
        controls.add(menuButton);
        controls.add(new JLabel());

        controls.add(attackButton);
        controls.add(recoverButton);
        controls.add(spell1Button);
        controls.add(spell2Button);

        bottom.add(controls, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);

        processAutoTurns();//run enemy turns if needed
        updateBattleHistory(engine.getBattleLog());
        refreshUI();
    }

    public static void startPvEFlow(HomePage homePage, PvECampaign campaign) {
        PvECampaign.RoomType roomType = campaign.determineCurrentRoomType();

        if (roomType == PvECampaign.RoomType.CAMPAIGN_COMPLETE) {
            JOptionPane.showMessageDialog(homePage, "You have completed the campaign! Your heroes may now retire.");
            campaign.resetCampaign();
            homePage.refreshInfo();
            homePage.setVisible(true);
            return;
        }

        if (roomType == PvECampaign.RoomType.INN) {
            BattleEngine tempEngine = new BattleEngine(campaign.getProfile().getPartyLevel(), BattleMode.PVE);
            tempEngine.fullyRestoreHeroesAtInn();
            campaign.saveAtInnWithoutAdvancing();

            new InnPage(homePage, campaign, tempEngine.getHeroesParty().getHeroes()).setVisible(true);
            homePage.setVisible(false);
            return;
        }

        new BattlePage(homePage, campaign, BattleMode.PVE, campaign.getProfile().getPartyLevel()).setVisible(true);
        homePage.setVisible(false);
    }

    private void doAction(Command command) {
        int targetIndex = targetBox.getSelectedIndex();

        if (command == Command.ATTACK && targetIndex < 0) {
            JOptionPane.showMessageDialog(this, "Choose a target.");
            return;
        }

        engine.performPlayerAction(new AttackCommand(command, targetIndex));//Command pattern usage
        handlePostAction();
    }

    private void handlePostAction() {
        updateBattleHistory(engine.getBattleLog());

        if (engine.isFinished()) {
            refreshUI();
            finishBattle();
            return;
        }

        processAutoTurns();//run enemy turns
        updateBattleHistory(engine.getBattleLog());
        refreshUI();

        if (engine.isFinished()) {
            finishBattle();
        }
    }

    private void processAutoTurns() {
        engine.performAutomaticTurns();
        //Enemy turns
    }

    private void updateBattleHistory(String message) {
        if (message != null && !message.isBlank()) {
            if (battleHistory.isEmpty() || !battleHistory.getLast().equals(message)) {
                battleHistory.addLast(message);

                while (battleHistory.size() > 50) {
                    battleHistory.removeFirst();//limit log size to 50
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        for (String entry : battleHistory) {
            builder.append(entry).append("\n");
        }

        logArea.setText(builder.toString().trim());
        logArea.setCaretPosition(logArea.getDocument().getLength());
        //auto scroll in UI
    }

    private void refreshUI() {
        if (mode == BattleMode.PVE) {
            topLabel.setText("PvE | Level " + campaign.getProfile().getPartyLevel() + " | Room " + campaign.getProfile().getRoomCount() + " | " + engine.getCurrentTurnLabel());
        } else {
            topLabel.setText("PvP | " + engine.getCurrentTurnLabel());
        }

        leftArea.setText(buildPartyText(engine.getHeroesParty(), mode == BattleMode.PVP ? "Player 1 Heroes" : "Heroes"));
        rightArea.setText(buildPartyText(engine.getEnemiesParty(), mode == BattleMode.PVP ? "Player 2 Heroes" : "Enemies"));

        String[] labels = engine.getCurrentActionLabels();
        attackButton.setText(labels[0]);
        recoverButton.setText(labels[1]);
        spell1Button.setText(labels[2]);
        spell2Button.setText(labels[3]);

        if (engine.getCurrentUnit() instanceof Hero hero) {
            String stats = hero.getName() +
                    " | ATK:" + hero.getAttack() +
                    " DEF:" + hero.getDefense() +
                    " HP:" + hero.getCurrentHealth() + "/" + hero.getMaxHealth();

            spellInfoArea.setText(
                    stats + "\n\n" +
                            hero.getSpell1Name() + ": " + engine.getSpellDescription(hero, true) + "\n" +
                            hero.getSpell2Name() + ": " + engine.getSpellDescription(hero, false)
            );
        } else {
            spellInfoArea.setText("Enemy turn.");
        }

        targetBox.removeAllItems();
        for (Unit unit : getTargetParty().getUnits()) {
            targetBox.addItem(unit.getName());//target enemy party
        }

        boolean enableControls = engine.isCurrentTurnPlayerControlled() && !engine.isFinished();

        attackButton.setEnabled(enableControls);
        recoverButton.setEnabled(enableControls);
        spell1Button.setEnabled(enableControls);
        spell2Button.setEnabled(enableControls);
        targetBox.setEnabled(enableControls);
    }

    private Party getTargetParty() {
        return engine.isCurrentTurnLeftSide() ? engine.getEnemiesParty() : engine.getHeroesParty();//determine target side
    }

    private String buildPartyText(Party party, String title) {
        StringBuilder builder = new StringBuilder(title).append("\n\n");

        for (Unit unit : party.getUnits()) {
            builder.append(unit.getStatLine()).append("\n");
        }

        return builder.toString();
    }

    private void finishBattle() {
        JOptionPane.showMessageDialog(this, engine.getWinnerText());

        if (mode == BattleMode.PVP) {
            homePage.setVisible(true);
            dispose();
            return;
        }

        if (engine.getEnemiesParty().allDowned()) {
            campaign.completeBattle();
            new User().updateProgress(campaign.getProfile());
            //Victory message on campaign complete (10 rooms)
            if (campaign.getProfile().getRoomCount() >= 10) {
                JOptionPane.showMessageDialog(this, "You have completed the campaign! Your heroes may now retire.");
                campaign.resetCampaign();
                homePage.refreshInfo();
                homePage.setVisible(true);
                dispose();
                return;
            }

            BattlePage.startPvEFlow(homePage, campaign);
            dispose();
        } else {
            new User().resetProgress(campaign.getProfile());
            homePage.refreshInfo();
            homePage.setVisible(true);
            dispose();
        }
    }
}