package com.mycompany.mathgame;

public enum OperationType {
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

    public char getSymbol() {
        switch (this) {
            case ADDITION: return '+';
            case SUBTRACTION: return '-';
            case MULTIPLICATION: return '×';
            case DIVISION: return '÷';
            default: return '?';
        }
    }
}
