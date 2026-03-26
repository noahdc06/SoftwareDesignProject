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

public class LoginPage extends JFrame {
    public LoginPage(HomePage homePage) {
        //Opens new window to log in
        setTitle("Login");
        setSize(380, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        UIStyle.styleFrame(this);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        UIStyle.stylePanel(panel);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        UIStyle.styleButton(loginButton);
        UIStyle.styleButton(backButton);

        loginButton.addActionListener(e -> {
            try {
                User userDAO = new User();
                Profile profile = userDAO.login(usernameField.getText().trim(), passwordField.getText());//check credentials

                if (profile == null) {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");//login failed
                    return;
                }

                SessionManager.getInstance().setCurrentProfile(profile);//store logged-in user
                homePage.refreshInfo();
                homePage.setVisible(true);
                dispose();//close login page
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());//show DB failure
            }
        });

        backButton.addActionListener(e -> {
            homePage.setVisible(true);
            dispose();//return to home page
        });

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(backButton);

        setContentPane(panel);
    }
}