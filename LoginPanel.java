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

        JButton topRoleBtn = new JButton("Roles");
        JButton topSignUpBtn = new JButton("Sign Up");
        JButton topExitBtn = new JButton("Exit");

        for (JButton b : new JButton[]{topRoleBtn, topSignUpBtn, topExitBtn}) {
            b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            b.setForeground(Color.WHITE);
            b.setBackground(AutomatedApplication.PRIMARY_DARK);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
            b.setContentAreaFilled(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            topRight.add(b);
        }

        topBar.add(brandLbl, BorderLayout.WEST);
        topBar.add(topRight, BorderLayout.EAST);
        frame.add(topBar, BorderLayout.NORTH);

        // Main Non-Scrollable Content (Hero + Login only)
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
        gbc.gridy = r++; signInCard.add(passField, gbc);
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
        topSignUpBtn.addActionListener(e -> openApplicantSignUpDialog(frame));
        topRoleBtn.addActionListener(e -> roleCombo.requestFocus());
        topExitBtn.addActionListener(e -> {
            ScholarshipManager.saveAllData();
            System.exit(0);
        });

        frame.setVisible(true);
    }

    public static void openApplicantSignUpDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Applicant Registration", true);
        dialog.setSize(430, 440);
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
        JPasswordField txtPassword = new JPasswordField();
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
        gbc.gridy = row++; card.add(txtPassword, gbc);

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
}