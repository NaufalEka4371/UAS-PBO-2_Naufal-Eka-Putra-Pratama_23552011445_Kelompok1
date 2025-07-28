// File: D:\andwi\Belajar ngoding\Belajar Java\MathGame\src\main\java\com\mycompany\mathgame\ProgressManager.java
package com.mycompany.mathgame;

import java.util.HashMap;
import java.util.Map;

public class ProgressManager {
    private static Map<GameMode, Integer> unlockedLevels = new HashMap<>();
    private static Map<String, Integer> highscores = new HashMap<>();

    /**
     * Unlock level berikutnya jika currentLevel sudah selesai.
     */
    public static void unlockNextLevel(GameMode mode, int currentLevel) {
        int currentUnlocked = unlockedLevels.getOrDefault(mode, 1);
        if (currentLevel >= currentUnlocked) {
            unlockedLevels.put(mode, currentLevel + 1);
        }
    }

    /**
     * Simpan skor tertinggi untuk suatu mode dan level.
     */
    public static void saveHighscore(GameMode mode, int level, int score) {
        String key = mode.name() + "_LEVEL_" + level;
        int currentHigh = highscores.getOrDefault(key, 0);
        if (score > currentHigh) {
            highscores.put(key, score);
        }
    }

    /**
     * Ambil skor tertinggi per level untuk setiap mode.
     */
    public static Map<GameMode, Map<Integer, Integer>> getBestHighscoresPerMode() {
        Map<GameMode, Map<Integer, Integer>> result = new HashMap<>();
        for (String key : highscores.keySet()) {
            String[] parts = key.split("_LEVEL_");
            if (parts.length != 2) continue; // jaga-jaga error
            try {
                GameMode mode = GameMode.valueOf(parts[0]);
                int level = Integer.parseInt(parts[1]);
                int score = highscores.get(key);

                result.putIfAbsent(mode, new HashMap<>());
                result.get(mode).put(level, score);
            } catch (Exception e) {
                e.printStackTrace(); // log error parsing jika ada
            }
        }
        return result;
    }

    /**
     * Ambil level tertinggi yang telah dibuka untuk suatu mode.
     */
    public static int getUnlockedLevel(GameMode mode) {
        return unlockedLevels.getOrDefault(mode, 1);
    }
}