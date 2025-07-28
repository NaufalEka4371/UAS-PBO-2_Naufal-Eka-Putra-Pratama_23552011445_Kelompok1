package com.mycompany.mathgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter; // Import MouseAdapter
import java.awt.event.MouseEvent;   // Import MouseEvent

public class SubLevelScreen extends JFrame {
    private final GameMode mode;
    private final OperationMode operation;

    public SubLevelScreen(GameMode mode, OperationMode operation) {
        this.mode = mode;
        this.operation = operation;

        setTitle("Pilih Level - " + operation.getDisplayName());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // setLayout(null); // Ini akan diatur oleh setContentPane

        // Set background dan pastikan layout null untuk content pane
        setContentPane(new JLabel(ResourceUtil.getScaledBackgroundIcon(800, 600)));
        getContentPane().setLayout(null); // Penting untuk setBounds

        // Judul
        JLabel titleLabel = new JLabel("Level - " + operation.getDisplayName(), SwingConstants.CENTER);
        titleLabel.setBounds(150, 60, 500, 50); // Sesuaikan posisi dan ukuran judul
        ResourceUtil.applyCustomFont(titleLabel, 40f); // Ukuran font judul
        titleLabel.setForeground(new Color(255, 255, 255, 230)); // Warna putih transparan
        getContentPane().add(titleLabel); // Tambahkan ke content pane

        int maxUnlocked = ProgressManager.getUnlockedLevel(mode);

        int btnWidth = 100;
        int btnHeight = 50;
        int cols = 5;
        int spacingX = 30;
        int spacingY = 25;
        
        // Hitung startX dan startY agar grid tombol berada di tengah
        int totalGridWidth = cols * btnWidth + (cols - 1) * spacingX;
        int startX = (getWidth() - totalGridWidth) / 2;
        int startY = 150; // Sesuaikan posisi Y awal grid tombol

        for (int i = 1; i <= 15; i++) {
            int x = startX + ((i - 1) % cols) * (btnWidth + spacingX);
            int y = startY + ((i - 1) / cols) * (btnHeight + spacingY);

            JButton levelBtn = new JButton("Level " + i);
            levelBtn.setBounds(x, y, btnWidth, btnHeight);
            ResourceUtil.applyCustomFont(levelBtn, 22f); // Font tombol level, sedikit lebih besar
            levelBtn.setFocusPainted(false);
            levelBtn.setOpaque(true); // Penting agar warna background terlihat
            levelBtn.setContentAreaFilled(true); // Penting agar warna background terlihat

            // Warna untuk tombol yang unlocked
            Color unlockedBaseColor = new Color(100, 200, 100, 180); // Hijau cerah transparan
            Color unlockedPressedColor = new Color(80, 160, 80, 220); // Hijau lebih gelap
            Color unlockedHoverColor = new Color(120, 220, 120, 220); // Hijau lebih terang

            // Warna untuk tombol yang locked
            Color lockedBaseColor = new Color(80, 80, 80, 150); // Abu-abu gelap transparan
            Color lockedBorderColor = new Color(50, 50, 50, 100); // Border abu-abu gelap

            levelBtn.setForeground(Color.WHITE); // Warna teks putih untuk semua tombol

            if (i <= maxUnlocked) {
                levelBtn.setBackground(unlockedBaseColor);
                levelBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2)); // Border putih transparan
                levelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Kursor tangan saat dihover

                // Tambahkan efek hover/pressed untuk tombol yang unlocked
                int selectedLevel = i; // Perlu final atau effectively final untuk lambda
                levelBtn.addActionListener(e -> {
                    ResourceUtil.playClickSound();
                    dispose();
                    new GameScreen(mode, operation, selectedLevel).setVisible(true);
                });

                levelBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        levelBtn.setBackground(unlockedHoverColor);
                        levelBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0, 200), 3)); // Border kuning saat hover
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        levelBtn.setBackground(unlockedBaseColor);
                        levelBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2));
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        levelBtn.setBackground(unlockedPressedColor);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (levelBtn.contains(e.getPoint())) {
                            levelBtn.setBackground(unlockedHoverColor);
                        } else {
                            levelBtn.setBackground(unlockedBaseColor);
                        }
                    }
                });

            } else {
                levelBtn.setEnabled(false); // Nonaktifkan tombol
                levelBtn.setBackground(lockedBaseColor);
                levelBtn.setBorder(BorderFactory.createLineBorder(lockedBorderColor, 2));
                // Optional: set kursor default atau kursor "disallowed"
                levelBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
            }

            getContentPane().add(levelBtn); // Tambahkan tombol ke content pane
        }

        // Back Button
        JButton backButton = ResourceUtil.createBackButton((ActionEvent e) -> {
            ResourceUtil.playClickSound();
            dispose();
            new LevelScreen().setVisible(true);
        });
        backButton.setBounds(20, 20, 48, 48); // Posisi back button
        getContentPane().add(backButton); // Tambahkan back button ke content pane
    }
}