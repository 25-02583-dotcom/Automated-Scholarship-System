import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AutomatedApplication {
    public static final Color PRIMARY_DARK = new Color(30, 41, 59);
    public static final Color PRIMARY_ACCENT = new Color(67, 56, 202);
    public static final Color BG_SOFT = new Color(238, 242, 255);
    public static final Color CARD_TINT = new Color(224, 231, 255);
    public static final Color CARD_WHITE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(15, 23, 42);
    public static final Color TEXT_MUTED = new Color(71, 85, 105);
    public static final Color BTN_GREEN = new Color(21, 128, 61);
    public static final Color BTN_RED = new Color(185, 28, 28);

    public static void main(String[] args) {
        ScholarshipManager.initializeSystem();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(LoginPanel::createAndShowLoginWindow);
    }

    public static JPanel createRoundedPanel(Color bgColor, Color borderColor, int arc) {
        return new JPanel() {
            {
                setOpaque(false);
                setBackground(bgColor);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape rr = new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);
                g2.setColor(getBackground());
                g2.fill(rr);
                if (borderColor != null) {
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.draw(rr);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
    }

    public static JButton createRoundedButton(String text, Color bg, Color fg, int arc) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createActionButton(String text, Color bg, Color fg, int w, int h) {
        JButton btn = createRoundedButton(text, bg, fg, 10);
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }
}