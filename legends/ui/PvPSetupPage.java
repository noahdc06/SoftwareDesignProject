package com.legends.ui;

import com.legends.battle.BattleMode;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridLayout;
//PvP simply gives both players the same heroes at the same level to pit against each other.
public class PvPSetupPage extends JFrame {
    public PvPSetupPage(HomePage homePage) {
        setTitle("PvP Setup");
        setSize(320, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(homePage);
        UIStyle.styleFrame(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        UIStyle.stylePanel(panel);
        //Choose level all heroes will start at
        JSpinner levelSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JButton startButton = new JButton("Start PvP");
        JButton closeButton = new JButton("Close");

        UIStyle.styleButton(startButton);
        UIStyle.styleButton(closeButton);

        //Use bar or change number to set level
        startButton.addActionListener(e -> {
            int level = (int) levelSpinner.getValue();
            new BattlePage(homePage, null, BattleMode.PVP, level).setVisible(true);//start PvP battle
            dispose();
        });

        closeButton.addActionListener(e -> dispose());//close window

        panel.add(new JLabel("Party Level:"));
        panel.add(levelSpinner);
        panel.add(startButton);
        panel.add(closeButton);

        setContentPane(panel);
    }
}