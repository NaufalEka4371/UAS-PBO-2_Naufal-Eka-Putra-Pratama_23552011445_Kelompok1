package com.mycompany.mathgame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

public class ResourceUtil {

    private static Font pixelFont;
    private static Clip backgroundClip;

    private static final Map<String, Clip> soundCache = new HashMap<>();

    // Lokasi file dari resources - SESUAIKAN DENGAN STRUKTUR FOLDER ANDA
    // Berdasarkan image_762f4a.png:
    private static final String FONT_PATH = "/fonts/real_font.ttf"; // Ubah ini ke nama font yang benar (misal: pixel.ttf atau pixel2.ttf)
    
    // Perhatikan folder "sound" (tanpa 's') seperti di image_762f4a.png
    private static final String BACKGROUND_IMG = "/images/backgroundungu.png";
    private static final String SOUND_CORRECT = "/sound/benar.wav";
    private static final String SOUND_WRONG = "/sound/salah.wav";
    private static final String SOUND_CLICK = "/sound/click.wav";
    private static final String MUSIC_PATH = "/sound/backgroundmusic.wav";
    private static final String BACK_ICON = "/images/back_icon.png";
    private static final String HEART_ICON = "/images/hearth.png";

    static {
        loadFont();
        preloadSound(SOUND_CORRECT);
        preloadSound(SOUND_WRONG);
        preloadSound(SOUND_CLICK);
        // Musik latar biasanya diputar saat aplikasi dimulai, bukan di preload static block
        // preloadSound(MUSIC_PATH); // Komentar ini sudah benar
    }

    public static void loadFont() {
        try (InputStream is = ResourceUtil.class.getResourceAsStream(FONT_PATH)) {
            if (is == null) {
                System.err.println("ERROR: Font file not found in classpath! Check path: " + FONT_PATH);
                pixelFont = new Font("Arial", Font.PLAIN, 18);
            } else {
                pixelFont = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pixelFont);
                System.out.println("Custom font loaded successfully: " + pixelFont.getFontName());
            }
        } catch (Exception e) {
            System.err.println("ERROR loading custom font from " + FONT_PATH + ": " + e.getMessage());
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.PLAIN, 18);
        }
    }

    public static void applyCustomFont(Component component) {
        applyCustomFont(component, 18f);
    }

    public static void applyCustomFont(Component component, float size) {
        if (pixelFont == null) {
            System.err.println("Pixel font is null, cannot apply custom font. Using default.");
            if (component != null) {
                component.setFont(new Font("Arial", Font.PLAIN, (int) size));
            }
            return;
        }
        if (component != null) {
            component.setFont(pixelFont.deriveFont(size));
        } else {
            System.err.println("Cannot apply font to a null component.");
        }
    }

    public static Font getPixelFont(int size) {
        if (pixelFont == null) {
            return new Font("Arial", Font.PLAIN, size);
        }
        return pixelFont.deriveFont((float) size);
    }

    public static Font getCustomFont(float size) {
        return getPixelFont((int)size);
    }

    // --- Audio Methods ---
    public static void playBackgroundMusic(String path) {
        stopBackgroundMusic();
        try (InputStream is = ResourceUtil.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("ERROR: Background music file not found! Check path: " + path);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new java.io.BufferedInputStream(is));
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing background music from " + path + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playBackgroundMusic() {
        playBackgroundMusic(MUSIC_PATH);
    }

    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    // --- Image Methods ---
    public static Image getScaledBackground(int width, int height) {
        try (InputStream is = ResourceUtil.class.getResourceAsStream(BACKGROUND_IMG)) {
            if (is == null) {
                System.err.println("Background image not found (InputStream): " + BACKGROUND_IMG);
                return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            }
            BufferedImage originalImage = ImageIO.read(is);
            return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.err.println("Error loading or scaling background image: " + e.getMessage());
            e.printStackTrace();
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public static ImageIcon getScaledBackgroundIcon(int width, int height) {
        Image img = getScaledBackground(width, height);
        return img != null ? new ImageIcon(img) : null;
    }

    public static void applyBackground(JFrame frame) {
        ImageIcon bg = getScaledBackgroundIcon(frame.getWidth(), frame.getHeight());
        if (bg != null) {
            JLabel backgroundLabel = new JLabel(bg);
            backgroundLabel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
            JPanel contentPane = (JPanel) frame.getContentPane();
            contentPane.add(backgroundLabel, JLayeredPane.FRAME_CONTENT_LAYER);
            contentPane.setLayout(null);
        } else {
             frame.getContentPane().setBackground(Color.DARK_GRAY);
             frame.getContentPane().setLayout(null);
        }
    }

    public static ImageIcon getScaledImage(String path, int width, int height) {
        try (InputStream is = ResourceUtil.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Image not found (InputStream): " + path);
                return null;
            }
            BufferedImage originalImage = ImageIO.read(is);
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            System.err.println("Error loading or scaling image from " + path + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static ImageIcon getImage(String path) {
        try (InputStream is = ResourceUtil.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Image not found (InputStream, raw): " + path);
                return null;
            }
            return new ImageIcon(ImageIO.read(is));
        } catch (IOException e) {
            System.err.println("Error loading raw image from " + path + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // --- Button Methods ---
    public static JButton createIconButton(String iconPath) {
        return createIconButton(iconPath, 50, 50);
    }

    public static JButton createIconButton(String iconPath, int width, int height) {
        JButton button = new JButton();
        ImageIcon icon = getScaledImage(iconPath, width, height);
        if (icon != null) {
            button.setIcon(icon);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setOpaque(false);
            button.setFocusPainted(false);
        } else {
            button.setText("ICON");
            applyCustomFont(button, 14f);
        }
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createBackButton(ActionListener backAction) {
        JButton backButton = createIconButton(BACK_ICON, 48, 48);
        backButton.addActionListener(backAction);
        backButton.setToolTipText("Kembali ke Menu");

        Color hoverBorderColor = new Color(255, 255, 0, 200);
        Color defaultBorderColor = new Color(255, 255, 255, 150);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // backButton.setBorder(BorderFactory.createLineBorder(hoverBorderColor, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // backButton.setBorder(BorderFactory.createLineBorder(defaultBorderColor, 2));
            }
        });
        return backButton;
    }
    
    public static ImageIcon getHeartIcon(int width, int height) {
        return getScaledImage(HEART_ICON, width, height);
    }

    // --- Sound Methods ---
    public static void playCorrectSound() {
        playSound(SOUND_CORRECT);
    }

    public static void playWrongSound() {
        playSound(SOUND_WRONG);
    }

    public static void playClickSound() {
        playSound(SOUND_CLICK);
    }

    public static void playSound(String path) {
        try {
            Clip clip = soundCache.get(path);
            if (clip == null) {
                preloadSound(path); // Coba preload jika belum ada di cache
                clip = soundCache.get(path);
            }
            if (clip != null) {
                // Hentikan dan tutup clip jika sedang berjalan atau terbuka,
                // lalu buka kembali untuk memastikan buffer bersih.
                // Ini mungkin sedikit overhead, tapi bisa mengatasi masalah delay/potongan.
                if (clip.isRunning()) {
                    clip.stop();
                }
                if (clip.isOpen()) { // Pastikan clip tertutup sebelum dibuka lagi
                    clip.close();
                }
                // Dapatkan ulang InputStream dan buka clip
                InputStream is = ResourceUtil.class.getResourceAsStream(path);
                if (is == null) {
                     System.err.println("Sound file not found for playback (InputStream): " + path);
                     return;
                }
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new java.io.BufferedInputStream(is));
                clip.open(audioIn); // Buka kembali clip
                clip.setFramePosition(0); // Selalu mulai dari awal
                clip.start();
            } else {
                 System.err.println("Failed to play sound: Clip is null for path " + path);
            }
        } catch (Exception e) {
            System.err.println("Error playing sound (cached): " + path + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void preloadSound(String path) {
        try {
            InputStream is = ResourceUtil.class.getResourceAsStream(path);
            if (is == null) {
                System.err.println("Sound file not found for preload (InputStream): " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new java.io.BufferedInputStream(is));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            soundCache.put(path, clip);
            System.out.println("Sound preloaded: " + path);
        } catch (Exception e) {
            System.err.println("Preload sound error: " + path + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}