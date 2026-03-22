package com.legends.ui;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    public HomePage() {
        setTitle("⚔ Legends of Sword and Wand");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 15, 15));
        panel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("Legends of Sword and Wand", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton loginBtn = createButton("Login");
        JButton createBtn = createButton("Create Profile");
        JButton pveBtn = createButton("Enter PvE");
        JButton pvpBtn = createButton("Enter PvP");
        JButton exitBtn = createButton("Exit Game");

        panel.add(title);
        panel.add(loginBtn);
        panel.add(createBtn);
        panel.add(pveBtn);
        panel.add(pvpBtn);
        panel.add(exitBtn);

        add(panel);

        loginBtn.addActionListener(e -> new LoginPage());
        createBtn.addActionListener(e -> new CreateProfilePage());

        pveBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "PvE not implemented yet.")
        );

        pvpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "PvP not implemented yet.")
        );

        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }
}