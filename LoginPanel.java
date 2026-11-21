import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel {
    public static void createAndShowLoginWindow() {
        JFrame frame = new JFrame("Automated Scholarship Application System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(980, 680));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(AutomatedApplication.BG_SOFT);

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AutomatedApplication.PRIMARY_DARK);
        topBar.setPreferredSize(new Dimension(0, 56));
        topBar.setBorder(new EmptyBorder(0, 28, 0, 28));

        JLabel brandLbl = new JLabel("AUTOMATED SCHOLARSHIP APPLICATION");
        brandLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        brandLbl.setForeground(Color.WHITE);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 13));
        topRight.setOpaque(false);

        JButton topExitBtn = new JButton("Exit");
        topExitBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        topExitBtn.setForeground(Color.WHITE);
        topExitBtn.setBackground(AutomatedApplication.PRIMARY_DARK);
        topExitBtn.setBorderPainted(false);
        topExitBtn.setFocusPainted(false);
        topExitBtn.setContentAreaFilled(false);
        topExitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        topRight.add(topExitBtn);

        topBar.add(brandLbl, BorderLayout.WEST);
        topBar.add(topRight, BorderLayout.EAST);
        frame.add(topBar, BorderLayout.NORTH);

        // Main Non-Scrollable Content
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(AutomatedApplication.BG_SOFT);
        centerWrapper.setBorder(new EmptyBorder(24, 28, 24, 28));

        JPanel heroRow = new JPanel(new GridLayout(1, 2, 20, 0));
        heroRow.setOpaque(false);
        heroRow.setPreferredSize(new Dimension(1120, 390));
        heroRow.setMaximumSize(new Dimension(1260, 420));

        JPanel heroBanner = AutomatedApplication.createRoundedPanel(
            AutomatedApplication.PRIMARY_DARK, null, 18
        );
        heroBanner.setLayout(new BoxLayout(heroBanner, BoxLayout.Y_AXIS));
        heroBanner.setBorder(new EmptyBorder(32, 32, 32, 32));

        JLabel dashLbl = new JLabel("—");
        dashLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        dashLbl.setForeground(new Color(165, 180, 252));
        dashLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heroText = new JLabel(
            "<html><div style='font-family: Segoe UI; color: white;'>" +
            "<div style='font-size: 28px; font-weight: bold; line-height: 1.25;'>Skip the wait.<br>Apply for grants<br>in minutes.</div>" +
            "<p style='font-size: 13px; color: #cbd5e1; margin-top: 12px;'>" +
            "Automated Scholarship Application, Document Validation, and Disbursement Monitoring System." +
            "</p></div></html>"
        );
        heroText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton heroCtaBtn = AutomatedApplication.createRoundedButton(
            "Create Applicant Account", Color.WHITE, AutomatedApplication.PRIMARY_DARK, 10
        );
        heroCtaBtn.setPreferredSize(new Dimension(200, 38));
        heroCtaBtn.setMaximumSize(new Dimension(200, 38));
        heroCtaBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        heroBanner.add(dashLbl);
        heroBanner.add(heroText);
        heroBanner.add(Box.createRigidArea(new Dimension(0, 18)));
        heroBanner.add(heroCtaBtn);

        JPanel signInCard = AutomatedApplication.createRoundedPanel(
            Color.WHITE, new Color(199, 210, 254), 18
        );
        signInCard.setLayout(new GridBagLayout());
        signInCard.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel signInTitle = new JLabel("Account Login");
        signInTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        signInTitle.setForeground(AutomatedApplication.TEXT_DARK);

        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Applicant", "Admin"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleCombo.setPreferredSize(new Dimension(0, 34));

        JTextField userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userField.setPreferredSize(new Dimension(0, 34));

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passField.setPreferredSize(new Dimension(0, 34));
        char defaultEchoChar = passField.getEchoChar();

        JButton togglePassBtn = new JButton("Show");
        togglePassBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        togglePassBtn.setPreferredSize(new Dimension(62, 34));
        togglePassBtn.setFocusPainted(false);
        togglePassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        togglePassBtn.setBackground(new Color(238, 242, 255));
        togglePassBtn.setForeground(AutomatedApplication.PRIMARY_ACCENT);
        togglePassBtn.setBorder(BorderFactory.createLineBorder(new Color(199, 210, 254), 1, true));
        togglePassBtn.addActionListener(e -> {
            if (passField.getEchoChar() == (char) 0) {
                passField.setEchoChar(defaultEchoChar);
                togglePassBtn.setText("Show");
            } else {
                passField.setEchoChar((char) 0);
                togglePassBtn.setText("Hide");
            }
        });

        JPanel passPanel = new JPanel(new BorderLayout(6, 0));
        passPanel.setOpaque(false);
        passPanel.add(passField, BorderLayout.CENTER);
        passPanel.add(togglePassBtn, BorderLayout.EAST);

        JButton loginBtn = AutomatedApplication.createRoundedButton(
            "Continue to Portal", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 10
        );
        loginBtn.setPreferredSize(new Dimension(0, 40));

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r++; signInCard.add(signInTitle, gbc);
        gbc.gridy = r++; signInCard.add(new JLabel("Role"), gbc);
        gbc.gridy = r++; signInCard.add(roleCombo, gbc);
        gbc.gridy = r++; signInCard.add(new JLabel("Username"), gbc);
        gbc.gridy = r++; signInCard.add(userField, gbc);
        gbc.gridy = r++; signInCard.add(new JLabel("Password"), gbc);
        gbc.gridy = r++; signInCard.add(passPanel, gbc);
        gbc.gridy = r++; gbc.insets = new Insets(12, 0, 0, 0); signInCard.add(loginBtn, gbc);

        heroRow.add(heroBanner);
        heroRow.add(signInCard);
        centerWrapper.add(heroRow);
        frame.add(centerWrapper, BorderLayout.CENTER);

        // Bottom Bar
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setBackground(AutomatedApplication.PRIMARY_DARK);
        bottomBar.setPreferredSize(new Dimension(0, 40));
        bottomBar.setBorder(new EmptyBorder(0, 28, 0, 28));
        JLabel footText = new JLabel("Automated Scholarship Application System  •  Team J³ (J Cubed)");
        footText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footText.setForeground(new Color(203, 213, 225));
        bottomBar.add(footText, BorderLayout.WEST);
        frame.add(bottomBar, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> {
            String role = (String) roleCombo.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Applicant".equals(role)) {
                Applicant account = ScholarshipManager.authenticateApplicant(username, password);
                if (account != null) {
                    ScholarshipManager.saveAllData();
                    JOptionPane.showMessageDialog(frame, "Applicant login successful! Session saved.");
                    frame.dispose();
                    ApplicantPanel.createAndShowApplicantDashboard(account);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid applicant credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (username.equals(ScholarshipManager.ADMIN_USERNAME) &&
                    password.equals(ScholarshipManager.ADMIN_PASSWORD)) {
                    ScholarshipManager.saveAllData();
                    JOptionPane.showMessageDialog(frame, "Admin login successful! Session saved.");
                    frame.dispose();
                    AdminPanel.createAndShowAdminDashboard();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid admin credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        heroCtaBtn.addActionListener(e -> openApplicantSignUpDialog(frame));
        topExitBtn.addActionListener(e -> {
            ScholarshipManager.saveAllData();
            System.exit(0);
        });

        frame.setVisible(true);
    }

    // sign-up
    public static void openApplicantSignUpDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Applicant Registration", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);

        JPanel card = AutomatedApplication.createRoundedPanel(Color.WHITE, new Color(199, 210, 254), 16);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField txtFullName = new JTextField();
        JTextField txtStudentId = new JTextField();
        JTextField txtContact = new JTextField();
        JTextField txtAddress = new JTextField();
        JTextField txtUsername = new JTextField();

        // new for unique password validation
        String placeholder = "Min. 8 chars, 1 uppercase, 1 number, 1 special";
        JPasswordField txtPassword = new JPasswordField(placeholder) {
            @Override
            public char[] getPassword() {
                String text = new String(super.getPassword());
                return text.equals(placeholder) ? new char[0] : super.getPassword();
            }
        };
        txtPassword.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        txtPassword.setForeground(Color.GRAY);
        txtPassword.setPreferredSize(new Dimension(0, 32));
        char defaultEcho = txtPassword.getEchoChar();
        txtPassword.setEchoChar((char) 0); // Para mabasa ang placeholder sa simula

        JButton toggleRegPassBtn = new JButton("Show");
        toggleRegPassBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        toggleRegPassBtn.setPreferredSize(new Dimension(62, 32));
        toggleRegPassBtn.setFocusPainted(false);
        toggleRegPassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleRegPassBtn.setBackground(new Color(238, 242, 255));
        toggleRegPassBtn.setForeground(AutomatedApplication.PRIMARY_ACCENT);
        toggleRegPassBtn.setBorder(BorderFactory.createLineBorder(new Color(199, 210, 254), 1, true));

        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(txtPassword.getPassword()).isEmpty()) {
                    txtPassword.setText("");
                    txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    txtPassword.setForeground(Color.BLACK);
                    if ("Show".equals(toggleRegPassBtn.getText())) {
                        txtPassword.setEchoChar(defaultEcho);
                    }
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (new String(txtPassword.getPassword()).isEmpty()) {
                    txtPassword.setEchoChar((char) 0);
                    txtPassword.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                    txtPassword.setForeground(Color.GRAY);
                    txtPassword.setText(placeholder);
                }
            }
        });

        toggleRegPassBtn.addActionListener(e -> {
            boolean isPlaceholder = new String(txtPassword.getPassword()).isEmpty() && txtPassword.getForeground() == Color.GRAY;
            if ("Show".equals(toggleRegPassBtn.getText())) {
                txtPassword.setEchoChar((char) 0);
                toggleRegPassBtn.setText("Hide");
            } else {
                if (!isPlaceholder) {
                    txtPassword.setEchoChar(defaultEcho);
                }
                toggleRegPassBtn.setText("Show");
            }
        });

        JPanel regPassPanel = new JPanel(new BorderLayout(6, 0));
        regPassPanel.setOpaque(false);
        regPassPanel.add(txtPassword, BorderLayout.CENTER);
        regPassPanel.add(toggleRegPassBtn, BorderLayout.EAST);

        JButton registerBtn = AutomatedApplication.createRoundedButton(
            "Register Applicant", AutomatedApplication.BTN_GREEN, Color.WHITE, 10
        );
        registerBtn.setPreferredSize(new Dimension(0, 36));

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++; card.add(new JLabel("Full Name:"), gbc);
        gbc.gridy = row++; card.add(txtFullName, gbc);

        gbc.gridy = row++; card.add(new JLabel("Student / Applicant ID:"), gbc);
        gbc.gridy = row++; card.add(txtStudentId, gbc);

        gbc.gridy = row++; card.add(new JLabel("Contact Details:"), gbc);
        gbc.gridy = row++; card.add(txtContact, gbc);

        gbc.gridy = row++; card.add(new JLabel("Address:"), gbc);
        gbc.gridy = row++; card.add(txtAddress, gbc);

        gbc.gridy = row++; card.add(new JLabel("Username:"), gbc);
        gbc.gridy = row++; card.add(txtUsername, gbc);

        gbc.gridy = row++; card.add(new JLabel("Password:"), gbc);
        gbc.gridy = row++; card.add(regPassPanel, gbc);

        gbc.gridy = row++; gbc.insets = new Insets(10, 0, 0, 0);
        card.add(registerBtn, gbc);

        GridBagConstraints outer = new GridBagConstraints();
        outer.fill = GridBagConstraints.BOTH;
        outer.weightx = 1.0;
        outer.weighty = 1.0;
        outer.insets = new Insets(14, 14, 14, 14);
        dialog.add(card, outer);

        registerBtn.addActionListener(e -> {
            String name = txtFullName.getText().trim();
            String studentId = txtStudentId.getText().trim();
            String contact = txtContact.getText().trim();
            String address = txtAddress.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (name.isEmpty() || studentId.isEmpty() || contact.isEmpty() ||
                address.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Password must be at least 8 characters long and contain at least 1 uppercase letter, 1 number, and 1 special character.",
                    "Invalid Password",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            for (Applicant acc : ScholarshipManager.applicantList) {
                if (acc.getUsername().equalsIgnoreCase(username)) {
                    JOptionPane.showMessageDialog(dialog, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Applicant newApplicant = new Applicant(studentId, name, contact, address, 0.0, username, password);
            newApplicant.addApplicant();
            JOptionPane.showMessageDialog(dialog, "Applicant profile registered and saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                hasSpecial = true;
            }
        }
        return hasUppercase && hasNumber && hasSpecial;
    }
}
