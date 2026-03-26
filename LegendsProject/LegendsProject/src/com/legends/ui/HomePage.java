package com.legends.ui;

import com.legends.campaign.PvECampaign;
import com.legends.model.Profile;
import com.legends.service.SessionManager;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

public class HomePage extends JFrame {
    private final SessionManager sessionManager;//login session
    private final JLabel infoLabel;//login and progress info

    public HomePage() {
        sessionManager = SessionManager.getInstance();

        setTitle("Legends of Sword and Wand");
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        UIStyle.styleFrame(this);

        JPanel root = new JPanel(new BorderLayout());
        UIStyle.stylePanel(root);

        JLabel title = new JLabel("Legends of Sword and Wand", JLabel.CENTER);
        title.setFont(UIStyle.TITLE_FONT);
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        UIStyle.stylePanel(center);

        infoLabel = new JLabel("Not logged in", JLabel.CENTER);
        infoLabel.setFont(UIStyle.BODY_FONT);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        JButton createButton = new JButton("Create Profile");
        JButton pveButton = new JButton("Enter PvE");
        JButton pvpButton = new JButton("Enter PvP");
        JButton logoutButton = new JButton("Logout");

        for (JButton button : new JButton[]{loginButton, createButton, pveButton, pvpButton, logoutButton}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            UIStyle.styleButton(button);
            button.setMaximumSize(new java.awt.Dimension(220, 40));
        }

        loginButton.addActionListener(e -> {
            new LoginPage(this).setVisible(true);
            setVisible(false);//hide home while login page is open
        });

        createButton.addActionListener(e -> {
            new CreateProfilePage(this).setVisible(true);
            setVisible(false);//hide home while create page is open
        });

        pveButton.addActionListener(e -> startPvE());
        pvpButton.addActionListener(e -> new PvPSetupPage(this).setVisible(true));

        logoutButton.addActionListener(e -> {
            sessionManager.logout();//log out of session
            refreshInfo();
        });

        center.add(Box.createVerticalStrut(20));
        center.add(infoLabel);
        center.add(Box.createVerticalStrut(20));
        center.add(loginButton);
        center.add(Box.createVerticalStrut(12));
        center.add(createButton);
        center.add(Box.createVerticalStrut(12));
        center.add(pveButton);
        center.add(Box.createVerticalStrut(12));
        center.add(pvpButton);
        center.add(Box.createVerticalStrut(12));
        center.add(logoutButton);

        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
        refreshInfo();//load current login state
    }

    public void refreshInfo() {
        if (sessionManager.isLoggedIn()) {
            Profile profile = sessionManager.getCurrentProfile();
            infoLabel.setText("Logged in as " + profile.getUsername() + " | Level " + profile.getPartyLevel() + " | Room " + profile.getRoomCount());//show saved progress
        } else {
            infoLabel.setText("Not logged in");
        }
    }

    private void startPvE() {
        if (!sessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Login first to play PvE.");//block PvE without account
            return;
        }

        Profile profile = sessionManager.getCurrentProfile();
        PvECampaign campaign = new PvECampaign(profile);
        BattlePage.startPvEFlow(this, campaign);//start campaign
    }
}