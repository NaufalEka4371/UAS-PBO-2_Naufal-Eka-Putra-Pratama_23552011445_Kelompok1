package com.mycompany.mathgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {

    public StartScreen() {
        setTitle("Math Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // setLayout(null); // Ini sudah dilakukan oleh setContentPane dengan null layout

        // Background
        setContentPane(new JLabel(ResourceUtil.getScaledBackgroundIcon(800, 600)));
        // Penting: Jika Anda mengatur content pane, layout manager-nya juga perlu diset.
        // Karena kita menggunakan setBounds, kita ingin layout null.
        getContentPane().setLayout(null); 

        // Musik latar (hanya dimainkan sekali jika belum aktif)
        ResourceUtil.playBackgroundMusic(); 

        // Judul
        JLabel title = new JLabel("Math Game", SwingConstants.CENTER);
        // Menggeser judul sedikit ke atas juga, dan mungkin sedikit kecilkan font
        title.setBounds(150, 60, 500, 60); // Y diubah dari 80 menjadi 60
        ResourceUtil.applyCustomFont(title, 50f); // Ukuran font mungkin sedikit diturunkan dari 52f ke 50f
        title.setForeground(new Color(255, 255, 255, 230));
        getContentPane().add(title); 

        // Tombol-tombol
        String[] labels = {
            "Normal", "Time Attack", "Survival", "Daily Challenge", "Highscore", "Keluar"
        };

        // Sesuaikan posisi Y awal tombol agar lebih ke atas
        int y = 170; // Y diubah dari 200 menjadi 170
        int buttonWidth = 280;
        int buttonHeight = 60;
        int spacing = 20;

        for (String label : labels) {
            int x = (getWidth() - buttonWidth) / 2;
            JButton button = createStyledButton(label, x, y, buttonWidth, buttonHeight);
            getContentPane().add(button);
            y += (buttonHeight + spacing);
        }
    }

    // Metode createStyledButton tidak berubah dari sebelumnya
    private JButton createStyledButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        ResourceUtil.applyCustomFont(button, 26f);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color baseColor = new Color(100, 50, 150, 180);
        Color pressedColor = new Color(80, 40, 120, 220);
        Color hoverColor = new Color(120, 60, 180, 220);

        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2));
        button.setBackground(baseColor);
        button.setContentAreaFilled(true); 
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0, 200), 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(hoverColor);
                } else {
                    button.setBackground(baseColor);
                }
            }
        });

        button.addActionListener(e -> {
            ResourceUtil.playClickSound();

            switch (text) {
                case "Normal":
                    MathGame.setCurrentGameMode(GameMode.NORMAL);
                    dispose();
                    new LevelScreen().setVisible(true);
                    break;
                case "Time Attack":
                    MathGame.setCurrentGameMode(GameMode.TIME_ATTACK);
                    dispose();
                    new LevelScreen().setVisible(true);
                    break;
                case "Survival":
                    MathGame.setCurrentGameMode(GameMode.SURVIVAL);
                    dispose();
                    new LevelScreen().setVisible(true);
                    break;
                case "Daily Challenge":
                    MathGame.setCurrentGameMode(GameMode.DAILY_CHALLENGE);
                    dispose();
                    new LevelScreen().setVisible(true);
                    break;
                case "Highscore":
                    dispose();
                    new HighscoreScreen().setVisible(true);
                    break;
                case "Keluar":
                    System.exit(0);
                    break;
            }
        });

        return button;
    }
}