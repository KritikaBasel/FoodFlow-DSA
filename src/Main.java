import ui.MainFrame;

import javax.swing.*;

/**
 * Main.java
 * 
 * Application Entry Point for FoodFlow - Restaurant Management System Using DSA.
 * Sets the Swing Look and Feel, enables UI text antialiasing,
 * and launches the MainFrame window on the Event Dispatch Thread.
 */
public class Main {

    public static void main(String[] args) {
        // Enable anti-aliased font rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Set modern system look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }

        // Launch UI on Swing Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
