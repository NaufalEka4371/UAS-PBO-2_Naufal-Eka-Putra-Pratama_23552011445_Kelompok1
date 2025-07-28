package com.mycompany.mathgame;

/**
 * Enum yang merepresentasikan berbagai mode permainan dalam Math Game.
 */
public enum GameMode {
    NORMAL, TIME_ATTACK, SURVIVAL, DAILY_CHALLENGE;

    /**
     * Mengembalikan nama mode yang ditampilkan ke pengguna.
     * @return Nama mode dalam bahasa Indonesia.
     */
    public String getDisplayName() {
        switch (this) {
            case NORMAL:
                return "Normal";
            case TIME_ATTACK:
                return "Time Attack";
            case SURVIVAL:
                return "Survival";
            case DAILY_CHALLENGE:
                return "Tantangan Harian";
            default:
                return "Tidak Dikenal";
        }
    }
}
