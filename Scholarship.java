import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Scholarship {
    static ArrayList<Application> applications = new ArrayList<>();
    static ArrayList<UserAccount> registeredApplicants = new ArrayList<>();

    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin123";

    static final double MAXIMUM_GWA = 1.75; 
    static final double MAXIMUM_ANNUAL_INCOME = 250000.0;

    static class UserAccount {
        String fullName;
        String studentId;
        String username;
        String password;

        UserAccount(String fullName, String studentId, String username, String password) {
            this.fullName = fullName;
            this.studentId = studentId;
            this.username = username;
            this.password = password;
        }
    }

    static class Application {
        String studentId;
        String name;
        String program;
        double gwa;
        double annualIncome;
        String documentName;
        LocalDate documentExpiry;
        String applicationStatus;
        String disbursementStatus;

        Application(String studentId, String name, String program,
                    double gwa, double annualIncome, String documentName,
                    LocalDate documentExpiry) {
            this.studentId = studentId;
            this.name = name;
            this.program = program;
            this.gwa = gwa;
            this.annualIncome = annualIncome;
            this.documentName = documentName;
            this.documentExpiry = documentExpiry;
            this.applicationStatus = "Pending Admin Review";
            this.disbursementStatus = "Not Yet Approved";
        }
    }

    public static void main(String[] args) {
        registeredApplicants.add(new UserAccount("Default Student", "25-00000", "applicant", "1234"));

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(Scholarship::createAndShowLoginWindow);
    }

    private static JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(44, 62, 80));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static JPanel createWebCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        return panel;
    }

    static void createAndShowLoginWindow() {
        JFrame frame = new JFrame("Scholarship System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(900, 650));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(24, 43, 73));
        navBar.setPreferredSize(new Dimension(0, 65));
        navBar.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JLabel brandLabel = new JLabel("🎓 SCHOLARSHIP SYSTEM PORTAL");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandLabel.setForeground(Color.WHITE);
        navBar.add(brandLabel, BorderLayout.WEST);

        frame.add(navBar, BorderLayout.NORTH);

        JPanel mainContainer = new JPanel(new GridBagLayout());
        mainContainer.setBackground(new Color(240, 244, 248));

        JPanel loginCard = createWebCardPanel();
        loginCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Scholarship System Login", JLabel.CENTER);
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        loginTitle.setForeground(new Color(24, 43, 73));

        JLabel roleLabel = new JLabel("Portal Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Applicant", "Admin"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleCombo.setPreferredSize(new Dimension(240, 35));

        JLabel userLabel = new JLabel("Username / ID:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userField.setPreferredSize(new Dimension(240, 35));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passField.setPreferredSize(new Dimension(240, 35));

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(39, 174, 96));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setPreferredSize(new Dimension(240, 40));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton signUpBtn = new JButton("Don't have an account? Sign Up");
        signUpBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        signUpBtn.setBackground(new Color(41, 128, 185));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setPreferredSize(new Dimension(240, 35));
        signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton exitBtn = new JButton("Close System");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exitBtn.setBackground(new Color(192, 57, 43));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setPreferredSize(new Dimension(240, 35));
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; loginCard.add(loginTitle, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; loginCard.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; loginCard.add(roleCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; loginCard.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; loginCard.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; loginCard.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; loginCard.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 12, 4, 12);
        loginCard.add(loginBtn, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(4, 12, 4, 12);
        loginCard.add(signUpBtn, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(4, 12, 8, 12);
        loginCard.add(exitBtn, gbc);

        mainContainer.add(loginCard);
        frame.add(mainContainer, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String role = (String) roleCombo.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if ("Applicant".equals(role)) {
                UserAccount account = authenticateApplicant(username, password);
                if (account != null) {
                    frame.dispose();
                    createAndShowApplicantWebDashboard(account);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Applicant Credentials or Account does not exist.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                    frame.dispose();
                    createAndShowAdminWebDashboard();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Admin Credentials.", "Authentication Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpBtn.addActionListener(e -> openApplicantSignUpDialog(frame));
        exitBtn.addActionListener(e -> System.exit(0));
        frame.setVisible(true);
    }

    static void openApplicantSignUpDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Applicant Registration", true);
        dialog.setSize(420, 380);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Applicant Account", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(24, 43, 73));

        JTextField txtFullName = new JTextField(15);
        JTextField txtStudentId = new JTextField(15);
        JTextField txtUsername = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2; dialog.add(title, gbc);
        gbc.gridwidth = 1;
        r++; gbc.gridx = 0; gbc.gridy = r; dialog.add(new JLabel("Full Name:"), gbc); gbc.gridx = 1; dialog.add(txtFullName, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; dialog.add(new JLabel("Student ID:"), gbc); gbc.gridx = 1; dialog.add(txtStudentId, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; dialog.add(new JLabel("Username:"), gbc); gbc.gridx = 1; dialog.add(txtUsername, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; dialog.add(new JLabel("Password:"), gbc); gbc.gridx = 1; dialog.add(txtPassword, gbc);

        JButton registerBtn = new JButton("Register Account");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setBackground(new Color(39, 174, 96));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        r++; gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 8, 10);
        dialog.add(registerBtn, gbc);

        registerBtn.addActionListener(e -> {
            String name = txtFullName.getText().trim();
            String sid = txtStudentId.getText().trim();
            String user = txtUsername.getText().trim();
            String pass = new String(txtPassword.getPassword());

            if (name.isEmpty() || sid.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (UserAccount acc : registeredApplicants) {
                if (acc.username.equalsIgnoreCase(user)) {
                    JOptionPane.showMessageDialog(dialog, "Username already taken.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            registeredApplicants.add(new UserAccount(name, sid, user, pass));
            JOptionPane.showMessageDialog(dialog, "Registration Successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    static UserAccount authenticateApplicant(String username, String password) {
        for (UserAccount acc : registeredApplicants) {
            if (acc.username.equals(username) && acc.password.equals(password)) {
                return acc;
            }
        }
        return null;
    }

    static void createAndShowApplicantWebDashboard(UserAccount currentUser) {
        JFrame frame = new JFrame("Scholarship System - Applicant Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1000, 700));
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(24, 43, 73));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        JLabel logo = new JLabel("🎓 Scholarship System - Applicant Dashboard");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        header.add(logo, BorderLayout.WEST);

        JLabel userBadge = new JLabel("Logged as: " + currentUser.fullName + " (" + currentUser.studentId + ")");
        userBadge.setForeground(new Color(189, 195, 199));
        userBadge.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.add(userBadge, BorderLayout.EAST);
        frame.add(header, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton navPrograms = createSidebarButton("📌 View Programs");
        JButton navSubmit = createSidebarButton("📝 Apply Online");
        JButton navStatus = createSidebarButton("🔍 Application Status");
        JButton navLogout = createSidebarButton("🚪 Logout");
        navLogout.setBackground(new Color(192, 57, 43));

        sidebar.add(navPrograms);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(navSubmit);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(navStatus);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(navLogout);

        frame.add(sidebar, BorderLayout.WEST);

        CardLayout cardLayout = new CardLayout();
        JPanel contentArea = new JPanel(cardLayout);
        contentArea.setBackground(new Color(240, 244, 248));
        contentArea.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel programsCard = createWebCardPanel();
        programsCard.setLayout(new BorderLayout(15, 15));
        JLabel progTitle = new JLabel("Available Scholarship Programs", JLabel.LEFT);
        progTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        progTitle.setForeground(new Color(24, 43, 73));
        programsCard.add(progTitle, BorderLayout.NORTH);

        JTextArea progList = new JTextArea(
            "1. Academic Excellence Scholarship\n" +
            "   - Eligibility: Maximum GWA of " + MAXIMUM_GWA + " or better (e.g., 1.00 - 1.75)\n" +
            "   - Target: Outstanding academic achievers\n\n" +
            "2. Financial Assistance Support Program\n" +
            "   - Eligibility: Annual Household Income ≤ ₱" + MAXIMUM_ANNUAL_INCOME + "\n" +
            "   - Target: Underprivileged students needing financial aid\n\n" +
            "3. Government Institutional Grant\n" +
            "   - Eligibility: Complete valid document submissions and verification"
        );
        progList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        progList.setEditable(false);
        progList.setBackground(new Color(248, 249, 250));
        progList.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        programsCard.add(new JScrollPane(progList), BorderLayout.CENTER);

        JPanel applyCard = createWebCardPanel();
        applyCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel formHeader = new JLabel("Online Scholarship Application Form", JLabel.CENTER);
        formHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formHeader.setForeground(new Color(39, 174, 96));

        JTextField txtStudentId = new JTextField(currentUser.studentId, 15);
        JTextField txtName = new JTextField(currentUser.fullName, 15);
        JComboBox<String> cmbProgram = new JComboBox<>(new String[]{
            "Academic Excellence Scholarship", 
            "Financial Assistance Support Program", 
            "Government Institutional Grant"
        });
        JTextField txtGwa = new JTextField(15);
        JTextField txtIncome = new JTextField(15);
        JTextField txtDocName = new JTextField(15);
        JTextField txtExpiry = new JTextField(15);

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2; applyCard.add(formHeader, gbc);
        gbc.gridwidth = 1;
        r++; gbc.gridx = 0; gbc.gridy = r; applyCard.add(new JLabel("Student ID:"), gbc); gbc.gridx = 1; applyCard.add(txtStudentId, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; applyCard.add(new JLabel("Full Name:"), gbc); gbc.gridx = 1; applyCard.add(txtName, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; applyCard.add(new JLabel("Scholarship Program:"), gbc); gbc.gridx = 1; applyCard.add(cmbProgram, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; applyCard.add(new JLabel("GWA (e.g. 1.25, 1.50):"), gbc); gbc.gridx = 1; applyCard.add(txtGwa, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; applyCard.add(new JLabel("Annual Income (₱):"), gbc); gbc.gridx = 1; applyCard.add(txtIncome, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; applyCard.add(new JLabel("Requirement Doc (.pdf/.jpg/.png):"), gbc); gbc.gridx = 1; applyCard.add(txtDocName, gbc);
        r++; gbc.gridx = 0; gbc.gridy = r; applyCard.add(new JLabel("Doc Expiry (YYYY-MM-DD):"), gbc); gbc.gridx = 1; applyCard.add(txtExpiry, gbc);

        JButton btnSubmit = new JButton("Submit Application Now");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setBackground(new Color(39, 174, 96));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        r++; gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 6, 10);
        applyCard.add(btnSubmit, gbc);

        JPanel statusCard = createWebCardPanel();
        statusCard.setLayout(new BorderLayout(15, 15));
        
        JPanel statusSearchBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        statusSearchBox.setBackground(Color.WHITE);
        JLabel lblSearch = new JLabel("Enter Student ID:");
        JTextField txtSearchId = new JTextField(currentUser.studentId, 12);
        JButton btnSearch = new JButton("Search Status");
        btnSearch.setBackground(new Color(41, 128, 185));
        btnSearch.setForeground(Color.WHITE);
        statusSearchBox.add(lblSearch);
        statusSearchBox.add(txtSearchId);
        statusSearchBox.add(btnSearch);

        JLabel statusResultLabel = new JLabel("<html><div style='text-align: center; color: #7f8c8d;'>" +
                "<h3>Click Search Status to view your real-time status.</h3></div></html>", SwingConstants.CENTER);

        statusCard.add(statusSearchBox, BorderLayout.NORTH);
        statusCard.add(statusResultLabel, BorderLayout.CENTER);

        contentArea.add(programsCard, "PROGRAMS");
        contentArea.add(applyCard, "APPLY");
        contentArea.add(statusCard, "STATUS");

        frame.add(contentArea, BorderLayout.CENTER);

        navPrograms.addActionListener(e -> cardLayout.show(contentArea, "PROGRAMS"));
        navSubmit.addActionListener(e -> cardLayout.show(contentArea, "APPLY"));
        navStatus.addActionListener(e -> cardLayout.show(contentArea, "STATUS"));

        btnSubmit.addActionListener(e -> {
            String studentId = txtStudentId.getText().trim();
            String name = txtName.getText().trim();
            String program = (String) cmbProgram.getSelectedItem();

            if (studentId.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Student ID and Name are required fields.", "Form Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (findApplication(studentId) != null) {
                JOptionPane.showMessageDialog(frame, "Application with this Student ID already exists.", "Duplicate Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double gwa, income;
            try {
                gwa = Double.parseDouble(txtGwa.getText().trim());
                if (gwa < 1.0 || gwa > 5.0) throw new NumberFormatException();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid GWA. Please enter a valid grade average between 1.00 and 5.00.", "Form Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                income = Double.parseDouble(txtIncome.getText().trim());
                if (income < 0) throw new NumberFormatException();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Income value.", "Form Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String docName = txtDocName.getText().trim().toLowerCase();
            if (!(docName.endsWith(".pdf") || docName.endsWith(".jpg") || docName.endsWith(".jpeg") || docName.endsWith(".png"))) {
                JOptionPane.showMessageDialog(frame, "Invalid Document format (.pdf, .jpg, .png allowed).", "Form Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate expiryDate;
            try {
                expiryDate = LocalDate.parse(txtExpiry.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format. Use YYYY-MM-DD.", "Form Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (expiryDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(frame, "Submitted Document is already expired.", "Form Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Application app = new Application(studentId, name, program, gwa, income, txtDocName.getText().trim(), expiryDate);
            if (gwa <= MAXIMUM_GWA && income <= MAXIMUM_ANNUAL_INCOME) {
                app.applicationStatus = "Pending Admin Review";
                JOptionPane.showMessageDialog(frame, "Application submitted successfully! Status: Pending Admin Review.", "Application Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                app.applicationStatus = "Ineligible";
                JOptionPane.showMessageDialog(frame, "Submitted! Status: Ineligible (Does not satisfy GWA/Income criteria).", "Notice", JOptionPane.WARNING_MESSAGE);
            }

            applications.add(app);
            txtGwa.setText(""); txtIncome.setText(""); txtDocName.setText(""); txtExpiry.setText("");
        });

        btnSearch.addActionListener(e -> {
            String sid = txtSearchId.getText().trim();
            Application app = findApplication(sid);
            if (app == null) {
                statusResultLabel.setText("<html><div style='text-align: center; color: #c0392b;'><h3>No record found for Student ID: " + sid + "</h3></div></html>");
            } else {
                statusResultLabel.setText(String.format(
                    "<html><div style='font-family: Segoe UI; padding: 20px; background: #f8f9fa; border: 1px solid #dcdde1; border-radius: 8px;'>" +
                    "<h2 style='color: #16a085;'>Application Result</h2>" +
                    "<b>Student ID:</b> %s<br>" +
                    "<b>Applicant Name:</b> %s<br>" +
                    "<b>Program:</b> %s<br>" +
                    "<b>GWA:</b> %.2f<br>" +
                    "<b>Annual Income:</b> ₱%.2f<br>" +
                    "<b>Document:</b> %s (Expires: %s)<br><br>" +
                    "<b>Application Status:</b> <span style='color: #2980b9;'>%s</span><br>" +
                    "<b>Disbursement Status:</b> <span style='color: #27ae60;'>%s</span></div></html>",
                    app.studentId, app.name, app.program, app.gwa, app.annualIncome,
                    app.documentName, app.documentExpiry, app.applicationStatus, app.disbursementStatus
                ));
            }
        });

        navLogout.addActionListener(e -> {
            frame.dispose();
            createAndShowLoginWindow();
        });

        frame.setVisible(true);
    }

    static void createAndShowAdminWebDashboard() {
        JFrame frame = new JFrame("Scholarship System - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1000, 700));
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(192, 57, 43));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));

        JLabel logo = new JLabel("🛡️ Scholarship System - Administrator Portal");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        header.add(logo, BorderLayout.WEST);

        JLabel userBadge = new JLabel("Logged as: System Administrator");
        userBadge.setForeground(Color.WHITE);
        userBadge.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.add(userBadge, BorderLayout.EAST);
        frame.add(header, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JButton navReview = createSidebarButton("📑 Review Applications");
        JButton navDisburse = createSidebarButton("💰 Disbursement Tracking");
        JButton navReports = createSidebarButton("📊 System Reports");
        JButton navLogout = createSidebarButton("🚪 Logout System");
        navLogout.setBackground(new Color(192, 57, 43));

        sidebar.add(navReview);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(navDisburse);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebar.add(navReports);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(navLogout);

        frame.add(sidebar, BorderLayout.WEST);

        CardLayout cardLayout = new CardLayout();
        JPanel contentArea = new JPanel(cardLayout);
        contentArea.setBackground(new Color(240, 244, 248));
        contentArea.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel reviewCard = createWebCardPanel();
        reviewCard.setLayout(new BorderLayout(15, 15));
        JLabel lblReviewTitle = new JLabel("Pending Applications Review", JLabel.LEFT);
        lblReviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        String[] revCols = {"Student ID", "Name", "Program", "GWA", "Income", "Status"};
        DefaultTableModel reviewModel = new DefaultTableModel(revCols, 0);
        JTable reviewTable = new JTable(reviewModel);
        reviewTable.setRowHeight(28);
        reviewTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel reviewBtnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnApprove = new JButton("Approve Application");
        btnApprove.setBackground(new Color(39, 174, 96));
        btnApprove.setForeground(Color.WHITE);
        JButton btnReject = new JButton("Reject Application");
        btnReject.setBackground(new Color(192, 57, 43));
        btnReject.setForeground(Color.WHITE);
        reviewBtnBar.add(btnApprove);
        reviewBtnBar.add(btnReject);

        reviewCard.add(lblReviewTitle, BorderLayout.NORTH);
        reviewCard.add(new JScrollPane(reviewTable), BorderLayout.CENTER);
        reviewCard.add(reviewBtnBar, BorderLayout.SOUTH);

        JPanel disburseCard = createWebCardPanel();
        disburseCard.setLayout(new BorderLayout(15, 15));
        JLabel lblDisburseTitle = new JLabel("Approved Grants & Disbursement Monitoring", JLabel.LEFT);
        lblDisburseTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        String[] disCols = {"Student ID", "Name", "Program", "App Status", "Disbursement Status"};
        DefaultTableModel disburseModel = new DefaultTableModel(disCols, 0);
        JTable disburseTable = new JTable(disburseModel);
        disburseTable.setRowHeight(28);
        disburseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel disburseBtnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnMarkPending = new JButton("Mark Pending");
        btnMarkPending.setBackground(new Color(241, 196, 15));
        JButton btnMarkReleased = new JButton("Mark Released");
        btnMarkReleased.setBackground(new Color(39, 174, 96));
        btnMarkReleased.setForeground(Color.WHITE);
        disburseBtnBar.add(btnMarkPending);
        disburseBtnBar.add(btnMarkReleased);

        disburseCard.add(lblDisburseTitle, BorderLayout.NORTH);
        disburseCard.add(new JScrollPane(disburseTable), BorderLayout.CENTER);
        disburseCard.add(disburseBtnBar, BorderLayout.SOUTH);

        JPanel reportCard = createWebCardPanel();
        reportCard.setLayout(new BorderLayout(15, 15));
        JLabel lblReportTitle = new JLabel("System Analytics & Report Summary", JLabel.LEFT);
        lblReportTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTextArea reportText = new JTextArea();
        reportText.setFont(new Font("Consolas", Font.PLAIN, 14));
        reportText.setEditable(false);
        reportText.setBackground(new Color(248, 249, 250));
        reportText.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        reportCard.add(lblReportTitle, BorderLayout.NORTH);
        reportCard.add(new JScrollPane(reportText), BorderLayout.CENTER);

        contentArea.add(reviewCard, "REVIEW");
        contentArea.add(disburseCard, "DISBURSE");
        contentArea.add(reportCard, "REPORTS");

        frame.add(contentArea, BorderLayout.CENTER);

        Runnable refreshTables = () -> {
            reviewModel.setRowCount(0);
            disburseModel.setRowCount(0);
            int total = applications.size(), pending = 0, approved = 0, rejected = 0, ineligible = 0, released = 0;

            for (Application app : applications) {
                if ("Pending Admin Review".equals(app.applicationStatus)) {
                    reviewModel.addRow(new Object[]{app.studentId, app.name, app.program, app.gwa, app.annualIncome, app.applicationStatus});
                    pending++;
                } else if ("Approved".equals(app.applicationStatus)) {
                    approved++;
                    disburseModel.addRow(new Object[]{app.studentId, app.name, app.program, app.applicationStatus, app.disbursementStatus});
                } else if ("Rejected".equals(app.applicationStatus)) {
                    rejected++;
                } else if ("Ineligible".equals(app.applicationStatus)) {
                    ineligible++;
                }

                if ("Released".equals(app.disbursementStatus)) {
                    released++;
                }
            }

            reportText.setText(String.format(
                "==========================================================\n" +
                "               SCHOLARSHIP SYSTEM REPORT METRICS          \n" +
                "==========================================================\n\n" +
                "  Total Applications Submitted  : %d\n" +
                "  Applications Pending Review   : %d\n" +
                "  Applications Approved         : %d\n" +
                "  Applications Rejected         : %d\n" +
                "  Applications Ineligible       : %d\n\n" +
                "  Total Funds/Grants Released   : %d\n" +
                "==========================================================",
                total, pending, approved, rejected, ineligible, released
            ));
        };

        refreshTables.run();

        navReview.addActionListener(e -> {
            refreshTables.run();
            cardLayout.show(contentArea, "REVIEW");
        });
        navDisburse.addActionListener(e -> {
            refreshTables.run();
            cardLayout.show(contentArea, "DISBURSE");
        });
        navReports.addActionListener(e -> {
            refreshTables.run();
            cardLayout.show(contentArea, "REPORTS");
        });

        btnApprove.addActionListener(e -> {
            int sel = reviewTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an application to approve.", "Selection Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sid = (String) reviewModel.getValueAt(sel, 0);
            Application app = findApplication(sid);
            if (app != null) {
                app.applicationStatus = "Approved";
                app.disbursementStatus = "Pending";
                JOptionPane.showMessageDialog(frame, "Application Approved successfully.");
                refreshTables.run();
            }
        });

        btnReject.addActionListener(e -> {
            int sel = reviewTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(frame, "Please select an application to reject.", "Selection Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sid = (String) reviewModel.getValueAt(sel, 0);
            Application app = findApplication(sid);
            if (app != null) {
                app.applicationStatus = "Rejected";
                app.disbursementStatus = "Not Applicable";
                JOptionPane.showMessageDialog(frame, "Application Rejected.");
                refreshTables.run();
            }
        });

        btnMarkPending.addActionListener(e -> {
            int sel = disburseTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(frame, "Select a record first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sid = (String) disburseModel.getValueAt(sel, 0);
            Application app = findApplication(sid);
            if (app != null) {
                app.disbursementStatus = "Pending";
                refreshTables.run();
            }
        });

        btnMarkReleased.addActionListener(e -> {
            int sel = disburseTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(frame, "Select a record first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sid = (String) disburseModel.getValueAt(sel, 0);
            Application app = findApplication(sid);
            if (app != null) {
                app.disbursementStatus = "Released";
                refreshTables.run();
            }
        });

        navLogout.addActionListener(e -> {
            frame.dispose();
            createAndShowLoginWindow();
        });

        frame.setVisible(true);
    }

    static Application findApplication(String studentId) {
        for (Application app : applications) {
            if (app.studentId.equalsIgnoreCase(studentId)) {
                return app;
            }
        }
        return null;
    }
}