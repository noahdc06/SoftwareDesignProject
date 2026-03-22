package com.legends.ui;

import com.legends.database.UserDAO;
import com.legends.model.Profile;

import javax.swing.*;
import java.awt.*;

public class CreateProfilePage extends JFrame {

    public CreateProfilePage() {
        setTitle("Create Profile");
        setSize(350, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBackground(new Color(40, 40, 40));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        userLabel.setForeground(Color.WHITE);
        passLabel.setForeground(Color.WHITE);

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();

        JButton createBtn = new JButton("Create");
        createBtn.setBackground(new Color(70, 130, 180));
        createBtn.setForeground(Color.WHITE);

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(createBtn);

        add(panel);

        createBtn.addActionListener(e -> {
            try {
                UserDAO dao = new UserDAO();

                Profile profile = new Profile(
                        usernameField.getText(),
                        passwordField.getText()
                );

                dao.createUser(profile);

                JOptionPane.showMessageDialog(this, "Profile created!");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            }
        });

        setVisible(true);
    }
}