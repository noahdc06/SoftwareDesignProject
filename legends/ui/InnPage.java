package com.legends.ui;

import com.legends.campaign.PvECampaign;
import com.legends.model.Hero;
import com.legends.model.HeroClass;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;

//Inn page shows hero info and allows user to exit campaign or progress to next room
public class InnPage extends JFrame {
    private final HomePage homePage;
    private final PvECampaign campaign;
    private final JTextArea heroArea;
    private final JLabel infoLabel;

    public InnPage(HomePage homePage, PvECampaign campaign, java.util.List<Hero> heroes) {
        this.homePage = homePage;
        this.campaign = campaign;

        setTitle("Inn");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        UIStyle.styleFrame(this);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        UIStyle.stylePanel(root);

        infoLabel = new JLabel("", JLabel.CENTER);
        infoLabel.setFont(UIStyle.BODY_FONT);
        root.add(infoLabel, BorderLayout.NORTH);

        heroArea = new JTextArea();
        UIStyle.styleTextArea(heroArea);
        root.add(heroArea, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 2, 8, 8));
        UIStyle.stylePanel(bottom);

        JButton exitButton = new JButton("Exit");
        JButton nextButton = new JButton("Next");

        UIStyle.styleButton(exitButton);
        UIStyle.styleButton(nextButton);

        bottom.add(exitButton);
        bottom.add(nextButton);
        root.add(bottom, BorderLayout.SOUTH);

        exitButton.addActionListener(e -> {
            homePage.refreshInfo();
            homePage.setVisible(true);
            dispose();//close inn page
        });

        nextButton.addActionListener(e -> {
            campaign.completeInnRoomAndSave();//advance campaign after inn

            if (campaign.getProfile().getRoomCount() >= 10) {
                javax.swing.JOptionPane.showMessageDialog(this, "You have completed the campaign! Your heroes may now retire.");
                campaign.resetCampaign();
                homePage.refreshInfo();
                homePage.setVisible(true);
                dispose();
            } else {
                BattlePage.startPvEFlow(homePage, campaign);//go to next room
                dispose();
            }
        });

        setContentPane(root);
        renderHeroes(heroes);
    }

    private void renderHeroes(java.util.List<Hero> heroes) {
        infoLabel.setText("Inn | Level " + campaign.getProfile().getPartyLevel() + " | Room " + campaign.getProfile().getRoomCount());

        StringBuilder builder = new StringBuilder();
        for (Hero hero : heroes) {
            builder.append(hero.getStatLine()).append("\n");
            builder.append(hero.getSpell1Name()).append(": ").append(getAbilityDescription(hero, true)).append("\n");
            builder.append(hero.getSpell2Name()).append(": ").append(getAbilityDescription(hero, false)).append("\n\n");
        }

        heroArea.setText(builder.toString().trim());//show hero info & abilities in text area
    }

    private String getAbilityDescription(Hero hero, boolean spell1) {
        HeroClass heroClass = hero.getHeroClass();

        if (heroClass == HeroClass.ROGUE) {
            return spell1
                    ? "Attack a random enemy 0-3 times. The attack may be wasted on downed enemies."
                    : "Reduce all enemy defense.";
        }

        if (heroClass == HeroClass.WARRIOR) {
            return spell1
                    ? "Attack a random target, twice. The attacks may be wasted on downed enemies."
                    : "Increase own defense by 4.";
        }

        if (heroClass == HeroClass.MAGE) {
            return spell1
                    ? "Deal double attack damage to a random enemy. This spell can be wasted on downed enemies."
                    : "Deal attack damage to all units, including allies and self.";
        }

        return spell1
                ? "Heal all non-downed allies for 20% of their max health. This has no effect on downed allies."
                : "Choose a random ally and heal them to full. This spell is wasted if it targets a downed ally.";
    }
}