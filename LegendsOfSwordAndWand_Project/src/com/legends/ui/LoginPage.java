package com.legends.ui;

import com.legends.database.UserDAO;
import com.legends.model.Profile;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("Login");
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

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(34, 139, 34));
        loginBtn.setForeground(Color.WHITE);

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginBtn);

        add(panel);

        loginBtn.addActionListener(e -> {
            try {
                UserDAO dao = new UserDAO();
                Profile user = dao.login(
                        usernameField.getText(),
                        passwordField.getText()
                );

                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error.");
            }
        });

        setVisible(true);
    }
}