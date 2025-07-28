package com.mycompany.mathgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; // Import MouseEvent dan MouseAdapter

public class LevelScreen extends JFrame {
    public LevelScreen() {
        GameMode mode = MathGame.getCurrentGameMode();

        setTitle("Pilih Operasi - " + mode.getDisplayName());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // setLayout(null); // Ini akan diatur oleh setContentPane

        // Set background dan pastikan layout null untuk content pane
        setContentPane(new JLabel(ResourceUtil.getScaledBackgroundIcon(800, 600)));
        getContentPane().setLayout(null); // Penting untuk setBounds

        // ResourceUtil.applyCustomFont(this); // applyCustomFont(this) akan menerapkan ke semua komponen anak secara rekursif,
                                            // yang mungkin menimpa setFont manual. Lebih baik terapkan per komponen.
        ResourceUtil.playBackgroundMusic(); // Memanggil tanpa argumen untuk menggunakan MUSIC_PATH

        // Judul
        JLabel title = new JLabel("Pilih Operasi - " + mode.getDisplayName(), SwingConstants.CENTER);
        title.setBounds(150, 60, 500, 60); // Posisi dan ukuran disesuaikan
        ResourceUtil.applyCustomFont(title, 40f); // Ukuran font judul disesuaikan
        title.setForeground(new Color(255, 255, 255, 230)); // Warna putih transparan
        getContentPane().add(title); // Tambahkan ke content pane

        // Tombol-tombol Operasi
        OperationMode[] ops = OperationMode.values();
        int buttonWidth = 220; // Lebar tombol operasi
        int buttonHeight = 70; // Tinggi tombol operasi
        int startX = (getWidth() - (buttonWidth * 2 + 50)) / 2; // Hitung X agar grup tombol di tengah (2 tombol + 50px spacing)
        int startY = 180; // Posisi Y awal tombol

        for (int i = 0; i < ops.length; i++) {
            OperationMode operation = ops[i];
            
            // Perhitungan posisi X dan Y untuk 2 kolom
            int x = startX + (i % 2) * (buttonWidth + 50); // 50px jarak antar kolom
            int y = startY + (i / 2) * (buttonHeight + 30); // 30px jarak antar baris

            JButton btn = createStyledOperationButton(operation.getDisplayName(), x, y, buttonWidth, buttonHeight);
            btn.addActionListener(e -> {
                ResourceUtil.playClickSound();
                dispose();
                new SubLevelScreen(mode, operation).setVisible(true);
            });
            getContentPane().add(btn); // Tambahkan ke content pane
        }

        // Back Button: Posisinya sudah di atas kiri, tetap konsisten
        JButton backButton = ResourceUtil.createBackButton(e -> {
            ResourceUtil.playClickSound();
            dispose();
            new StartScreen().setVisible(true);
        });
        backButton.setBounds(20, 20, 48, 48);
        getContentPane().add(backButton); // Tambahkan ke content pane
    }

    // Metode baru untuk membuat tombol operasi dengan style kustom
    private JButton createStyledOperationButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        ResourceUtil.applyCustomFont(button, 24f); // Ukuran font untuk tombol operasi
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Warna dasar tombol operasi (bisa berbeda dari StartScreen jika diinginkan)
        Color baseColor = new Color(150, 70, 200, 180); // Ungu yang sedikit lebih cerah transparan
        Color pressedColor = new Color(120, 50, 170, 220); // Lebih gelap saat ditekan
        Color hoverColor = new Color(180, 90, 230, 220); // Lebih terang saat di-hover

        button.setForeground(Color.WHITE); // Warna teks putih
        // Border yang konsisten dengan StartScreen atau disesuaikan
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2)); // Border putih transparan tipis

        button.setBackground(baseColor);
        button.setContentAreaFilled(true);
        button.setOpaque(true);

        // Efek Hover dan Pressed
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255, 200), 3)); // Border Cyan terang saat hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2));
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

        return button;
    }
}