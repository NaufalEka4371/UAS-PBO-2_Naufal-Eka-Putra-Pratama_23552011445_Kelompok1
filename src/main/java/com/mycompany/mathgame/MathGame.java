package com.mycompany.mathgame;

import javax.swing.*;

public class MathGame {
    private static GameMode currentGameMode = GameMode.NORMAL;

    public static GameMode getCurrentGameMode() {
        return currentGameMode;
    }

    public static void setCurrentGameMode(GameMode mode) {
        currentGameMode = mode;
    }

    public static void main(String[] args) {
        // Jalankan di thread UI
        SwingUtilities.invokeLater(() -> {
            // Pastikan background music hanya diputar sekali
            ResourceUtil.playBackgroundMusic("background_music.wav");
            new StartScreen().setVisible(true);
        });
    }
}
