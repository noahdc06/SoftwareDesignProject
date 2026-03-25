package com.legends.ui;

import com.legends.database.User;
import com.legends.model.Profile;
import com.legends.service.SessionManager;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class CreateProfilePage extends JFrame {
    public CreateProfilePage(HomePage homePage) {
        setTitle("Create Profile");
        setSize(420, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        UIStyle.styleFrame(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        UIStyle.stylePanel(panel);

        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JButton createButton = new JButton("Create");
        JButton backButton = new JButton("Back");
        UIStyle.styleButton(createButton);
        UIStyle.styleButton(backButton);

        createButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required.");
                return;
            }
            try {
                User userDAO = new User();
                if (userDAO.usernameExists(username)) {
                    JOptionPane.showMessageDialog(this, "That username already exists.");
                    return;
                }
                Profile profile = userDAO.createUser(username, password);
                SessionManager.getInstance().setCurrentProfile(profile);
                homePage.refreshInfo();
                homePage.setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();
        });

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(createButton);
        panel.add(backButton);
        setContentPane(panel);
    }
}
