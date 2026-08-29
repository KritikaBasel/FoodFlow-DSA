package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * UIUtils.java
 * 
 * Helper class providing a cute, bright, soft-pastel restaurant palette,
 * typography, and custom UI components (rounded panels, rounded buttons, badges, why-DSA cards).
 */
public class UIUtils {

    // 🎀 Cute, Soft Pastel Color Palette
    public static final Color COLOR_BG = new Color(254, 249, 247);          // Soft Cream White
    public static final Color COLOR_SIDEBAR = new Color(255, 235, 238);     // Soft Pastel Pink
    public static final Color COLOR_SIDEBAR_HOVER = new Color(255, 218, 225);
    public static final Color COLOR_PRIMARY = new Color(255, 107, 129);     // Vibrant Cute Rose
    public static final Color COLOR_PRIMARY_HOVER = new Color(238, 82, 102);
    public static final Color COLOR_SECONDARY = new Color(116, 185, 255);   // Soft Pastel Blue
    public static final Color COLOR_MINT = new Color(129, 236, 171);        // Cute Pastel Green
    public static final Color COLOR_MINT_DARK = new Color(38, 166, 91);
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_CARD_BORDER = new Color(245, 222, 226);
    public static final Color COLOR_TEXT_MAIN = new Color(45, 52, 54);      // Deep Charcoal Soft
    public static final Color COLOR_TEXT_MUTED = new Color(120, 111, 111);  // Muted Soft Brown
    public static final Color COLOR_ACCENT_YELLOW = new Color(254, 202, 87);// Cute Warm Yellow
    public static final Color COLOR_TAG_BG = new Color(255, 240, 243);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Creates a custom styled rounded panel.
     */
    public static JPanel createRoundedPanel(int radius, Color bgColor) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
                g2.setColor(COLOR_CARD_BORDER);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Creates a cute rounded button with hover effects.
     */
    public static JButton createRoundedButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color currentBg = getModel().isPressed() ? bgColor.darker() : 
                                 getModel().isRollover() ? bgColor.brighter() : bgColor;
                g2.setColor(currentBg);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BODY_BOLD);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        return button;
    }

    /**
     * Creates a stats card panel with emoji, value, and title.
     */
    public static JPanel createStatCard(String emoji, String title, String value, Color accentColor) {
        JPanel card = createRoundedPanel(18, COLOR_CARD_BG);
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(new EmptyBorder(15, 18, 15, 18));

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_SMALL);
        lblTitle.setForeground(COLOR_TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(accentColor);

        textPanel.add(lblTitle);
        textPanel.add(lblValue);

        card.add(lblEmoji, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Creates a dedicated "💡 Why Did We Use This?" educational card.
     */
    public static JPanel createWhyCard(String dsaTitle, String whyExplanation) {
        JPanel panel = createRoundedPanel(16, new Color(255, 248, 230)); // Warm pastel cream
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("💡 WHY " + dsaTitle.toUpperCase() + "?");
        title.setFont(FONT_SUBHEADER);
        title.setForeground(new Color(180, 83, 9)); // Warm amber

        JTextArea area = new JTextArea(whyExplanation);
        area.setFont(FONT_BODY);
        area.setForeground(COLOR_TEXT_MAIN);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setOpaque(false);

        panel.add(title, BorderLayout.NORTH);
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates a section header with cute title.
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_HEADER);
        lbl.setForeground(COLOR_TEXT_MAIN);
        return lbl;
    }
}
