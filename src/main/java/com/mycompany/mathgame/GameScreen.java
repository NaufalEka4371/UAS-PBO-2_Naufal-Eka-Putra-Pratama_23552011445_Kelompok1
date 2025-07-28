package com.mycompany.mathgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen extends JFrame {
    private final GameMode mode;
    private final OperationMode operation;
    private final int level;
    private int score = 0;
    private int questionCount = 0;
    private int lives = 3;
    private int totalTime = 30; // Untuk mode Time Attack

    private JLabel questionLabel, scoreLabel, timerLabel;
    private JPanel livesPanel;
    private List<JLabel> heartIcons;

    private JTextField answerField;
    private Timer questionTimer, globalTimer;
    private int correctAnswer;
    private int timeLeft = 5; // Timer per pertanyaan untuk mode selain Normal, Time Attack, Daily Challenge

    public GameScreen(GameMode mode, OperationMode operation, int level) {
        this.mode = mode;
        this.operation = operation;
        this.level = level;

        setTitle("MathGame - Level " + level + " (" + operation.getDisplayName() + ")");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new JLabel(ResourceUtil.getScaledBackgroundIcon(800, 600)));
        getContentPane().setLayout(null);

        // Header Panel (untuk back button, score, lives, timer)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setOpaque(false);
        headerPanel.setBounds(0, 0, 800, 80);
        getContentPane().add(headerPanel);

        // Back Button: Posisikan di dalam headerPanel
        JButton backButton = ResourceUtil.createBackButton((ActionEvent e) -> {
            ResourceUtil.playClickSound();
            if (questionTimer != null) questionTimer.stop();
            if (globalTimer != null) globalTimer.stop();
            dispose();
            new SubLevelScreen(mode, operation).setVisible(true);
        });
        backButton.setBounds(20, 15, 48, 48);
        headerPanel.add(backButton);

        // Score Label: Posisikan di dalam headerPanel
        scoreLabel = new JLabel("Skor: 0", SwingConstants.LEFT);
        scoreLabel.setBounds(90, 25, 180, 30);
        ResourceUtil.applyCustomFont(scoreLabel, 24f);
        scoreLabel.setForeground(new Color(255, 255, 255, 220));
        headerPanel.add(scoreLabel);

        // Lives Panel (untuk ikon hati): Posisikan di dalam headerPanel
        livesPanel = new JPanel();
        livesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        livesPanel.setOpaque(false);
        livesPanel.setBounds(280, 20, 240, 40);
        headerPanel.add(livesPanel);
        
        heartIcons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            JLabel heart = new JLabel();
            // Pastikan path dan nama file gambar hati benar
            // Ini akan memicu error jika resource tidak ditemukan
            heart.setIcon(ResourceUtil.getScaledImage("/images/hearth.png", 35, 35));
            heart.setVisible(false);
            heartIcons.add(heart);
            livesPanel.add(heart);
        }

        // Timer Label: Posisikan di dalam headerPanel
        timerLabel = new JLabel("", SwingConstants.RIGHT);
        timerLabel.setBounds(600, 25, 170, 30);
        ResourceUtil.applyCustomFont(timerLabel, 24f);
        timerLabel.setForeground(new Color(255, 255, 0, 220));
        headerPanel.add(timerLabel);
        
        // Title Label: Posisinya di bawah headerPanel
        JLabel titleLabel = new JLabel("Level " + level + " - " + operation.getDisplayName(), SwingConstants.CENTER);
        titleLabel.setBounds(150, 100, 500, 50);
        ResourceUtil.applyCustomFont(titleLabel, 36f);
        titleLabel.setForeground(new Color(255, 255, 255, 230));
        getContentPane().add(titleLabel);

        // Question Label: Di tengah layar
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setBounds(100, 200, 600, 80);
        ResourceUtil.applyCustomFont(questionLabel, 48f);
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(questionLabel);

        // Answer Field: Di bawah soal
        answerField = new JTextField();
        answerField.setBounds(275, 320, 250, 50);
        ResourceUtil.applyCustomFont(answerField, 30f);
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.setBackground(new Color(255, 255, 255, 200));
        answerField.setBorder(BorderFactory.createLineBorder(new Color(150, 100, 200), 2));
        getContentPane().add(answerField);
        answerField.addActionListener(e -> checkAnswer());

        // Submit Button: Di bawah answer field
        JButton submitButton = createStyledSubmitButton("Jawab", 325, 400, 150, 55);
        submitButton.addActionListener(e -> checkAnswer());
        getContentPane().add(submitButton);

        if (mode == GameMode.NORMAL || mode == GameMode.DAILY_CHALLENGE) {
            timerLabel.setVisible(false);
            livesPanel.setVisible(false);
        } else if (mode == GameMode.SURVIVAL) {
            lives = 5;
            livesPanel.setVisible(true);
            updateLivesDisplay();
            timerLabel.setVisible(true);
            timerLabel.setText("⏱ " + timeLeft);
        } else if (mode == GameMode.TIME_ATTACK) {
            livesPanel.setVisible(false);
            timerLabel.setVisible(true);
            startGlobalTimer();
        }

        updateScore();
        nextQuestion();
    }

    private JButton createStyledSubmitButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        ResourceUtil.applyCustomFont(button, 24f);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color baseColor = new Color(50, 150, 200, 180);
        Color pressedColor = new Color(40, 120, 160, 220);
        Color hoverColor = new Color(70, 180, 230, 220);

        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));
        
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
                button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));
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

    private void nextQuestion() {
        boolean gameOverConditionMet = false;
        if (mode == GameMode.NORMAL && questionCount >= 10) {
            gameOverConditionMet = true;
        } else if (mode == GameMode.TIME_ATTACK && totalTime <= 0) {
            gameOverConditionMet = true;
        } else if (mode == GameMode.SURVIVAL && questionCount >= 10) {
            gameOverConditionMet = true;
        } else if (mode == GameMode.DAILY_CHALLENGE && questionCount >= 10) {
            gameOverConditionMet = true;
        }

        if (gameOverConditionMet) {
            if (questionTimer != null) questionTimer.stop();
            if (globalTimer != null) globalTimer.stop();
            endGame("Permainan selesai!");
            return;
        }

        if (mode == GameMode.SURVIVAL) {
            timeLeft = 5;
            timerLabel.setText("⏱ " + timeLeft);
            if (questionTimer != null) questionTimer.stop();

            questionTimer = new Timer(1000, e -> {
                timeLeft--;
                timerLabel.setText("⏱ " + timeLeft);
                if (timeLeft <= 0) {
                    questionTimer.stop();
                    ResourceUtil.playWrongSound();
                    
                    lives--;
                    updateLivesDisplay();
                    if (lives <= 0) {
                        endGame("Game Over! Nyawa habis.");
                        return;
                    }
                    nextQuestion();
                }
            });
            questionTimer.start();
        }

        answerField.setText("");
        generateQuestion();
        questionCount++;
    }

    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(answerField.getText().trim());
            if (questionTimer != null) questionTimer.stop();

            if (userAnswer == correctAnswer) {
                ResourceUtil.playCorrectSound();
                score += 10;
            } else {
                ResourceUtil.playWrongSound();
                if (mode == GameMode.SURVIVAL) {
                    lives--;
                    updateLivesDisplay();
                    if (lives <= 0) {
                        endGame("Game Over! Nyawa habis.");
                        return;
                    }
                } else {
                    score -= 5;
                }
            }
            updateScore();
            nextQuestion();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid!");
            answerField.setText("");
        }
    }
    
    private void updateScore() {
        scoreLabel.setText("Skor: " + score);
    }

    private void updateLivesDisplay() {
        for (int i = 0; i < heartIcons.size(); i++) {
            if (i < lives) {
                heartIcons.get(i).setVisible(true);
            } else {
                heartIcons.get(i).setVisible(false);
            }
        }
        livesPanel.revalidate();
        livesPanel.repaint();
    }

    private void startGlobalTimer() {
        globalTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalTime--;
                timerLabel.setText("⏱ " + totalTime);
                if (totalTime <= 0) {
                    globalTimer.stop();
                    endGame("Waktu habis!");
                }
            }
        });
        globalTimer.start();
    }

    private void endGame(String message) {
        if (questionTimer != null) questionTimer.stop();
        if (globalTimer != null) globalTimer.stop();

        ProgressManager.unlockNextLevel(mode, level);
        ProgressManager.saveHighscore(mode, level, score);

        JOptionPane.showMessageDialog(this, message + "\nSkor Akhir: " + score);
        dispose();
        new HighscoreScreen().setVisible(true);
    }

    private void generateQuestion() {
        Random rand = new Random();
        int a, b;

        int maxNum = 10 * level;
        if (level > 5) maxNum = 50 + (level - 5) * 10;
        if (maxNum > 100) maxNum = 100;

        a = rand.nextInt(maxNum) + 1;
        b = rand.nextInt(maxNum) + 1;

        switch (operation) {
            case ADDITION:
                correctAnswer = a + b;
                questionLabel.setText(a + " + " + b + " = ?");
                break;
            case SUBTRACTION:
                if (a < b) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                correctAnswer = a - b;
                questionLabel.setText(a + " - " + b + " = ?");
                break;
            case MULTIPLICATION:
                int multLimit = 12;
                a = rand.nextInt(multLimit) + 1;
                b = rand.nextInt(multLimit) + 1;
                correctAnswer = a * b;
                questionLabel.setText(a + " × " + b + " = ?");
                break;
            case DIVISION:
                b = rand.nextInt(10) + 1;
                correctAnswer = rand.nextInt(maxNum / b) + 1;
                int dividend = correctAnswer * b;
                questionLabel.setText(dividend + " ÷ " + b + " = ?");
                break;
        }
    }
}