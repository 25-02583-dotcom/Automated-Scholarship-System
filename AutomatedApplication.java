import javax.swing.*;
<<<<<<< HEAD
import javax.swing.plaf.basic.BasicButtonUI;
=======
>>>>>>> 20a6e8d98ef8f75e99ae593282529920ffb68f48
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
<<<<<<< HEAD
            private float hoverProgress = 0f;
            private float hoverTarget = 0f;
            private final Timer hoverTimer = new Timer(15, e -> {
                float difference = hoverTarget - hoverProgress;
                if (Math.abs(difference) < 0.035f) {
                    hoverProgress = hoverTarget;
                    ((Timer) e.getSource()).stop();
                } else {
                    hoverProgress += difference * 0.28f;
                }
                repaint();
            });

            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                        if (isEnabled()) {
                            hoverTarget = 1f;
                            hoverTimer.start();
                        }
                    }
                    @Override public void mouseExited(java.awt.event.MouseEvent e) {
                        hoverTarget = 0f;
                        hoverTimer.start();
                    }
                });
            }

=======
>>>>>>> 20a6e8d98ef8f75e99ae593282529920ffb68f48
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
<<<<<<< HEAD
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                Color hoverColor = (bg.getRed() > 235 && bg.getGreen() > 235 && bg.getBlue() > 235)
                    ? new Color(224, 231, 255) : brighten(bg, 0.22f);
                Color current = blend(bg, hoverColor, hoverProgress);
                boolean pressed = getModel().isPressed() && getModel().isArmed();
                if (!isEnabled()) current = new Color(148, 163, 184);
                if (pressed) current = current.darker();

                int shadowOffset = Math.round(4f * hoverProgress);
                if (isEnabled() && !pressed && hoverProgress > 0.02f) {
                    g2.setColor(new Color(15, 23, 42, Math.round(24 + 34 * hoverProgress)));
                    g2.fill(new RoundRectangle2D.Float(1, 3 + shadowOffset, getWidth() - 2,
                        getHeight() - 3, arc, arc));
                }

                GradientPaint gradient = new GradientPaint(0, 0, current.brighter(),
                    0, getHeight(), current);
                g2.setPaint(gradient);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1,
                    getHeight() - 1, arc, arc));

                if (hoverProgress > 0.05f) {
                    g2.setColor(new Color(255, 255, 255, Math.round(28 * hoverProgress)));
                    g2.setStroke(new BasicStroke(1f));
                    g2.draw(new RoundRectangle2D.Float(0.8f, 0.8f, getWidth() - 2.6f,
                        getHeight() - 2.6f, arc, arc));
                }
=======
                g2.setColor(getModel().isPressed() ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
>>>>>>> 20a6e8d98ef8f75e99ae593282529920ffb68f48
                g2.dispose();
                super.paintComponent(g);
            }
        };
<<<<<<< HEAD

        btn.setUI(new BasicButtonUI());
=======
>>>>>>> 20a6e8d98ef8f75e99ae593282529920ffb68f48
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
<<<<<<< HEAD
        btn.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        btn.setRolloverEnabled(true);
=======
>>>>>>> 20a6e8d98ef8f75e99ae593282529920ffb68f48
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton createActionButton(String text, Color bg, Color fg, int w, int h) {
        JButton btn = createRoundedButton(text, bg, fg, 10);
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }
<<<<<<< HEAD

    // Reusable hover styling for ordinary Swing buttons (Exit, Logout, Show/Hide).
    public static void applyButtonHover(JButton button, Color normal, Color hover) {
        // A consistent BasicButtonUI prevents the system look and feel from masking hover colors.
        button.setUI(new BasicButtonUI());
        button.setBackground(normal);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hover);
                    button.repaint();
                }
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normal);
                button.repaint();
            }
        });
    }

    private static Color blend(Color start, Color end, float amount) {
        float t = Math.max(0f, Math.min(1f, amount));
        int r = Math.round(start.getRed() + (end.getRed() - start.getRed()) * t);
        int g = Math.round(start.getGreen() + (end.getGreen() - start.getGreen()) * t);
        int b = Math.round(start.getBlue() + (end.getBlue() - start.getBlue()) * t);
        return new Color(r, g, b);
    }

    private static Color brighten(Color color, float amount) {
        int r = Math.min(255, Math.round(color.getRed() + (255 - color.getRed()) * amount));
        int g = Math.min(255, Math.round(color.getGreen() + (255 - color.getGreen()) * amount));
        int b = Math.min(255, Math.round(color.getBlue() + (255 - color.getBlue()) * amount));
        return new Color(r, g, b);
    }
}
=======
}
>>>>>>> 20a6e8d98ef8f75e99ae593282529920ffb68f48
