package com.mycompany.mathgame;

public enum OperationMode {
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION;

    public String getDisplayName() {
        switch (this) {
            case ADDITION: return "Penjumlahan";
            case SUBTRACTION: return "Pengurangan";
            case MULTIPLICATION: return "Perkalian";
            case DIVISION: return "Pembagian";
            default: return "Tidak Dikenal";
        }
    }
}
