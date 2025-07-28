package com.mycompany.mathgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class HighscoreScreen extends JFrame {

    public HighscoreScreen() {
        setTitle("Highscore");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        // Set background
        setContentPane(new JLabel(ResourceUtil.getScaledBackgroundIcon(800, 600)));
        getContentPane().setLayout(null); // ulangi layout agar layout manual tetap aktif

        // Judul
        JLabel title = new JLabel("Highscore", SwingConstants.CENTER);
        title.setBounds(250, 30, 300, 50);
        ResourceUtil.applyCustomFont(title, 36f);
        title.setForeground(Color.WHITE);
        getContentPane().add(title);

        // Ambil semua skor per mode dan level
        Map<GameMode, Map<Integer, Integer>> allHighscores = ProgressManager.getBestHighscoresPerMode();

        int y = 100;
        for (GameMode mode : GameMode.values()) {
            Map<Integer, Integer> modeScores = allHighscores.getOrDefault(mode, new java.util.HashMap<>());

            int maxScore = modeScores.values().stream()
                .mapToInt(score -> score)
                .max()
                .orElse(0); // Jika kosong, default 0

            String displayText = mode.getDisplayName() + ": " + maxScore;

            JLabel label = new JLabel(displayText, SwingConstants.CENTER);
            label.setBounds(250, y, 300, 40);
            ResourceUtil.applyCustomFont(label, 22f);
            label.setForeground(Color.WHITE);
            getContentPane().add(label);
            y += 50;
        }

        // Tombol kembali
        JButton backButton = ResourceUtil.createBackButton(e -> {
            ResourceUtil.playClickSound();
            dispose();
            new StartScreen().setVisible(true);
        });
        backButton.setBounds(20, 20, 48, 48);
        getContentPane().add(backButton);
    }
}
