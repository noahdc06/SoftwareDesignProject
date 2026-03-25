package com.legends.main;

import com.legends.ui.HomePage;

import javax.swing.SwingUtilities;

public class GameMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage().setVisible(true));
    }
}
