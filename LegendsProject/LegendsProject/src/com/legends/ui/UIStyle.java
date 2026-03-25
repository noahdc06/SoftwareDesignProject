package com.legends.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;

public class UIStyle {
    public static final Font TITLE_FONT = new Font("Serif", Font.BOLD, 28);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 16);
    public static final Color BG = new Color(245, 239, 227);

    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BG);
    }

    public static void styleButton(JButton button) {
        button.setFont(BODY_FONT);
        button.setFocusPainted(false);
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(BG);
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(BODY_FONT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }
}
