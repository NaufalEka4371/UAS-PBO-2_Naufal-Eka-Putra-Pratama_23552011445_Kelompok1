package com.mycompany.mathgame;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LevelData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<GameMode, Map<Integer, Integer>> highscoreMap;

    public LevelData() {
        highscoreMap = new HashMap<>();
        for (GameMode mode : GameMode.values()) {
            Map<Integer, Integer> levelScores = new HashMap<>();
            for (int i = 1; i <= 15; i++) {
                levelScores.put(i, 0); // default skor awal = 0
            }
            highscoreMap.put(mode, levelScores);
        }
    }

    public int getHighscore(GameMode mode, int level) {
        return highscoreMap.getOrDefault(mode, new HashMap<>()).getOrDefault(level, 0);
    }

    public void setHighscore(GameMode mode, int level, int score) {
        if (!highscoreMap.containsKey(mode)) {
            highscoreMap.put(mode, new HashMap<>());
        }
        Map<Integer, Integer> levelScores = highscoreMap.get(mode);
        int current = levelScores.getOrDefault(level, 0);
        if (score > current) {
            levelScores.put(level, score);
        }
    }

    public Map<Integer, Integer> getAllHighscores(GameMode mode) {
        return highscoreMap.getOrDefault(mode, new HashMap<>());
    }

    public boolean isLevelUnlocked(GameMode mode, int level) {
        if (level == 1) return true;
        Map<Integer, Integer> scores = highscoreMap.getOrDefault(mode, new HashMap<>());
        return scores.getOrDefault(level - 1, 0) > 0;
    }
}
