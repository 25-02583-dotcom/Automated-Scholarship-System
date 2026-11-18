import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class AutomatedScholarshipApplication {
    static ArrayList<Application> applications = new ArrayList<>();
    static ArrayList<UserAccount> registeredApplicants = new ArrayList<>();

    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin123";

    static final double MAXIMUM_GWA = 2.00; 
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
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(AutomatedScholarshipApplication::createAndShowLoginWindow);
    }

    private static JButton createCenteredButton(String text, Color bg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); 
        button.setPreferredSize(new Dimension(350, 50));
        button.setMaximumSize(new Dimension(350, 50));
        return button;
    }

    // LOGIN WINDOW
    static void createAndShowLoginWindow() {
        JFrame frame = new JFrame("Scholarship System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        
        JLabel titleLabel = new JLabel("AUTOMATED SCHOLARSHIP APPLICATION SYSTEM");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(titleLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(new Color(245, 247, 250));
        
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 225, 233), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel loginHeader = new JLabel("SYSTEM LOGIN", JLabel.CENTER);
        loginHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginHeader.setForeground(new Color(41, 128, 185));
        
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleLabel.setForeground(Color.DARK_GRAY);
        
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Applicant", "Admin"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleCombo.setPreferredSize(new Dimension(220, 32));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.DARK_GRAY);
        
        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setPreferredSize(new Dimension(220, 32));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(Color.DARK_GRAY);
        
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setPreferredSize(new Dimension(220, 32));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; cardPanel.add(loginHeader, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; cardPanel.add(roleLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; cardPanel.add(roleCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; cardPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; cardPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; cardPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; cardPanel.add(passField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(Color.WHITE);
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setContentAreaFilled(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setPreferredSize(new Dimension(100, 35));
        loginBtn.setFocusPainted(false);

        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signUpBtn.setBackground(new Color(39, 174, 96));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setOpaque(true);
        signUpBtn.setContentAreaFilled(true);
        signUpBtn.setBorderPainted(false);
        signUpBtn.setPreferredSize(new Dimension(100, 35));
        signUpBtn.setFocusPainted(false);
        
        JButton exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exitBtn.setBackground(new Color(149, 165, 166));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setOpaque(true);
        exitBtn.setContentAreaFilled(true);
        exitBtn.setBorderPainted(false);
        exitBtn.setPreferredSize(new Dimension(100, 35));
        exitBtn.setFocusPainted(false);
        
        btnPanel.add(loginBtn);
        btnPanel.add(signUpBtn);
        btnPanel.add(exitBtn);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; cardPanel.add(btnPanel, gbc);
        centerContainer.add(cardPanel);
        frame.add(centerContainer, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String role = (String) roleCombo.getSelectedItem();
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if ("Applicant".equals(role)) {
                UserAccount account = authenticateApplicant(username, password);
                if (account != null) {
                    JOptionPane.showMessageDialog(frame, "Applicant login successful!");
                    frame.dispose();
                    createAndShowApplicantDashboard(account);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                    JOptionPane.showMessageDialog(frame, "Admin login successful!");
                    frame.dispose();
                    createAndShowAdminDashboard();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpBtn.addActionListener(e -> openApplicantSignUpDialog(frame));
        exitBtn.addActionListener(e -> System.exit(0));
        frame.setVisible(true);
    }

    // SIGN UP DIALOG
    static void openApplicantSignUpDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Applicant Registration", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtFullName = new JTextField(15);
        JTextField txtStudentId = new JTextField(15);
        JTextField txtUsername = new JTextField(15);
        JPasswordField txtPassword = new JPasswordField(15);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; dialog.add(txtFullName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; dialog.add(txtStudentId, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; dialog.add(txtUsername, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; dialog.add(txtPassword, gbc);

        row++;
        JButton registerBtn = new JButton("Register");
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setBackground(new Color(39, 174, 96));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setOpaque(true);
        registerBtn.setContentAreaFilled(true);
        registerBtn.setBorderPainted(false);
        registerBtn.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 6, 10);
        dialog.add(registerBtn, gbc);

        registerBtn.addActionListener(e -> {
            String name = txtFullName.getText().trim();
            String studentId = txtStudentId.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (name.isEmpty() || studentId.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (UserAccount acc : registeredApplicants) {
                if (acc.username.equalsIgnoreCase(username)) {
                    JOptionPane.showMessageDialog(dialog, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            registeredApplicants.add(new UserAccount(name, studentId, username, password));
            JOptionPane.showMessageDialog(dialog, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
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

    // APPLICANT DASHBOARD
    static void createAndShowApplicantDashboard(UserAccount currentUser) {
        JFrame frame = new JFrame("Applicant Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(39, 174, 96));
        headerPanel.setPreferredSize(new Dimension(0, 70));

        JLabel headerLabel = new JLabel("APPLICANT DASHBOARD (" + currentUser.fullName + ")");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(headerLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(new Color(245, 247, 250));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 225, 233), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel menuTitle = new JLabel("Welcome, " + currentUser.fullName);
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuTitle.setForeground(new Color(39, 174, 96));
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnSelectProgram = createCenteredButton("View Scholarship Programs", new Color(41, 128, 185));
        JButton btnSubmitApp = createCenteredButton("Submit Application", new Color(39, 174, 96));
        JButton btnViewStatus = createCenteredButton("View My Application Status", new Color(142, 68, 173));
        JButton btnLogout = createCenteredButton("Logout", new Color(192, 57, 43));

        menuPanel.add(menuTitle);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(btnSelectProgram);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(btnSubmitApp);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(btnViewStatus);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(btnLogout);

        centerContainer.add(menuPanel);
        frame.add(centerContainer, BorderLayout.CENTER);

        btnSelectProgram.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, 
                "Available Scholarship Programs:\n\n" +
                "1. Academic Scholarship (Max GWA: " + MAXIMUM_GWA + " or better)\n" +
                "2. Financial Assistance Scholarship (Max Income: ₱" + MAXIMUM_ANNUAL_INCOME + ")\n" +
                "3. Government Scholarship", 
                "Scholarship Programs", JOptionPane.INFORMATION_MESSAGE);
        });

        btnSubmitApp.addActionListener(e -> openSubmitApplicationDialog(frame, currentUser));
        btnViewStatus.addActionListener(e -> openViewStatusDialog(frame, currentUser));
        btnLogout.addActionListener(e -> {
            frame.dispose();
            createAndShowLoginWindow();
        });

        frame.setVisible(true);
    }

    // ADMIN DASHBOARD
    static void createAndShowAdminDashboard() {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(192, 57, 43));
        headerPanel.setPreferredSize(new Dimension(0, 70));

        JLabel headerLabel = new JLabel("ADMINISTRATOR DASHBOARD");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(headerLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(new Color(245, 247, 250));

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 225, 233), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel menuTitle = new JLabel("Welcome, Administrator");
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        menuTitle.setForeground(new Color(192, 57, 43));
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnReview = createCenteredButton("Review Applications", new Color(41, 128, 185));
        JButton btnDisburse = createCenteredButton("Disbursement Monitoring", new Color(39, 174, 96));
        JButton btnReports = createCenteredButton("Generate Reports", new Color(142, 68, 173));
        JButton btnLogout = createCenteredButton("Logout", new Color(192, 57, 43));

        menuPanel.add(menuTitle);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(btnReview);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(btnDisburse);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(btnReports);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        menuPanel.add(btnLogout);

        centerContainer.add(menuPanel);
        frame.add(centerContainer, BorderLayout.CENTER);

        btnReview.addActionListener(e -> openReviewApplicationsDialog(frame));
        btnDisburse.addActionListener(e -> openDisbursementDialog(frame));
        btnReports.addActionListener(e -> openReportsDialog(frame));
        btnLogout.addActionListener(e -> {
            frame.dispose();
            createAndShowLoginWindow();
        });

        frame.setVisible(true);
    }

    static void openSubmitApplicationDialog(JFrame parent, UserAccount currentUser) {
        JDialog dialog = new JDialog(parent, "Submit Application", true);
        dialog.setSize(450, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtStudentId = new JTextField(currentUser.studentId, 15);
        txtStudentId.setEditable(false);
        JTextField txtName = new JTextField(currentUser.fullName, 15);
        txtName.setEditable(false);
        JComboBox<String> cmbProgram = new JComboBox<>(new String[]{
            "Academic Scholarship", 
            "Financial Assistance Scholarship", 
            "Government Scholarship"
        });
        JTextField txtGwa = new JTextField(15);
        JTextField txtIncome = new JTextField(15);
        JTextField txtDocName = new JTextField(15);
        JTextField txtExpiry = new JTextField(15);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; dialog.add(txtStudentId, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; dialog.add(txtName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Program:"), gbc);
        gbc.gridx = 1; dialog.add(cmbProgram, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("GWA (1.00 - 2.00):"), gbc);
        gbc.gridx = 1; dialog.add(txtGwa, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Annual Income:"), gbc);
        gbc.gridx = 1; dialog.add(txtIncome, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Document (.pdf/.jpg/.png):"), gbc);
        gbc.gridx = 1; dialog.add(txtDocName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; dialog.add(new JLabel("Expiry (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; dialog.add(txtExpiry, gbc);

        row++;
        JButton submitBtn = new JButton("Submit Application");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submitBtn.setBackground(new Color(39, 174, 96));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setOpaque(true);
        submitBtn.setContentAreaFilled(true);
        submitBtn.setBorderPainted(false);
        submitBtn.setFocusPainted(false);
        submitBtn.setPreferredSize(new Dimension(200, 35));
        
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 6, 10);
        dialog.add(submitBtn, gbc);

        submitBtn.addActionListener(e -> {
            String studentId = txtStudentId.getText().trim();
            String name = txtName.getText().trim();
            String program = (String) cmbProgram.getSelectedItem();

            if (findApplication(studentId) != null) {
                JOptionPane.showMessageDialog(dialog, "An application with this Student ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double gwa, income;
            try {
                gwa = Double.parseDouble(txtGwa.getText().trim());
                if (gwa < 1.0 || gwa > 5.0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid GWA. Must be between 1.00 and 5.00.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                income = Double.parseDouble(txtIncome.getText().trim());
                if (income < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid income.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String docName = txtDocName.getText().trim().toLowerCase();
            if (!(docName.endsWith(".pdf") || docName.endsWith(".jpg") || docName.endsWith(".jpeg") || docName.endsWith(".png"))) {
                JOptionPane.showMessageDialog(dialog, "Invalid document format (.pdf/.jpg/.png).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate expiryDate;
            try {
                expiryDate = LocalDate.parse(txtExpiry.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format (YYYY-MM-DD).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (expiryDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(dialog, "Document is expired.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Application app = new Application(studentId, name, program, gwa, income, txtDocName.getText().trim(), expiryDate);

            if (gwa <= MAXIMUM_GWA && income <= MAXIMUM_ANNUAL_INCOME) {
                app.applicationStatus = "Pending Admin Review";
                JOptionPane.showMessageDialog(dialog, "Requirements met! Forwarded for review.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                app.applicationStatus = "Ineligible";
                JOptionPane.showMessageDialog(dialog, "Does not meet initial GWA or Income requirements.", "Notice", JOptionPane.WARNING_MESSAGE);
            }

            applications.add(app);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    static void openViewStatusDialog(JFrame parent, UserAccount currentUser) {
        Application app = findApplication(currentUser.studentId);
        if (app == null) {
            JOptionPane.showMessageDialog(parent, "No application found for your Student ID yet.", "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String details = String.format(
            "<html><div style='font-family: Segoe UI; padding: 6px;'>" +
            "<b>Student ID:</b> %s<br>" +
            "<b>Name:</b> %s<br>" +
            "<b>Program:</b> %s<br>" +
            "<b>GWA:</b> %.2f<br>" +
            "<b>Annual Income:</b> ₱%.2f<br>" +
            "<b>Document:</b> %s<br>" +
            "<b>Expiry:</b> %s<br>" +
            "<b>Application Status:</b> <font color='#2980b9'>%s</font><br>" +
            "<b>Disbursement Status:</b> <font color='#27ae60'>%s</font></div></html>",
            app.studentId, app.name, app.program, app.gwa, app.annualIncome,
            app.documentName, app.documentExpiry, app.applicationStatus, app.disbursementStatus
        );

        JOptionPane.showMessageDialog(parent, new JLabel(details), "Application Status", JOptionPane.INFORMATION_MESSAGE);
    }

    static void openReviewApplicationsDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Review Pending Applications", true);
        dialog.setSize(750, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        String[] columns = {"Student ID", "Name", "Program", "GWA", "Income", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Application app : applications) {
            if ("Pending Admin Review".equals(app.applicationStatus)) {
                model.addRow(new Object[]{app.studentId, app.name, app.program, app.gwa, app.annualIncome, app.applicationStatus});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton approveBtn = new JButton("Approve Selected");
        approveBtn.setBackground(new Color(39, 174, 96));
        approveBtn.setForeground(Color.WHITE);
        approveBtn.setOpaque(true);
        approveBtn.setContentAreaFilled(true);
        approveBtn.setBorderPainted(false);
        approveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JButton rejectBtn = new JButton("Reject Selected");
        rejectBtn.setBackground(new Color(192, 57, 43));
        rejectBtn.setForeground(Color.WHITE);
        rejectBtn.setOpaque(true);
        rejectBtn.setContentAreaFilled(true);
        rejectBtn.setBorderPainted(false);
        rejectBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        bottomPanel.add(approveBtn);
        bottomPanel.add(rejectBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String studentId = (String) model.getValueAt(row, 0);
            Application app = findApplication(studentId);
            if (app != null) {
                app.applicationStatus = "Approved";
                app.disbursementStatus = "Pending";
                JOptionPane.showMessageDialog(dialog, "Application approved.");
                dialog.dispose();
            }
        });

        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String studentId = (String) model.getValueAt(row, 0);
            Application app = findApplication(studentId);
            if (app != null) {
                app.applicationStatus = "Rejected";
                app.disbursementStatus = "Not Applicable";
                JOptionPane.showMessageDialog(dialog, "Application rejected.");
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    static void openDisbursementDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Disbursement Monitoring", true);
        dialog.setSize(750, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        String[] columns = {"Student ID", "Name", "Program", "App Status", "Disbursement Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Application app : applications) {
            if ("Approved".equals(app.applicationStatus)) {
                model.addRow(new Object[]{app.studentId, app.name, app.program, app.applicationStatus, app.disbursementStatus});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton setPendingBtn = new JButton("Mark as Pending");
        setPendingBtn.setBackground(new Color(241, 196, 15));
        setPendingBtn.setForeground(Color.DARK_GRAY);
        setPendingBtn.setOpaque(true);
        setPendingBtn.setContentAreaFilled(true);
        setPendingBtn.setBorderPainted(false);
        setPendingBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JButton setReleasedBtn = new JButton("Mark as Released");
        setReleasedBtn.setBackground(new Color(39, 174, 96));
        setReleasedBtn.setForeground(Color.WHITE);
        setReleasedBtn.setOpaque(true);
        setReleasedBtn.setContentAreaFilled(true);
        setReleasedBtn.setBorderPainted(false);
        setReleasedBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        bottomPanel.add(setPendingBtn);
        bottomPanel.add(setReleasedBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        setPendingBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String studentId = (String) model.getValueAt(row, 0);
            Application app = findApplication(studentId);
            if (app != null) {
                app.disbursementStatus = "Pending";
                JOptionPane.showMessageDialog(dialog, "Status updated to Pending.");
                dialog.dispose();
            }
        });

        setReleasedBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String studentId = (String) model.getValueAt(row, 0);
            Application app = findApplication(studentId);
            if (app != null) {
                app.disbursementStatus = "Released";
                JOptionPane.showMessageDialog(dialog, "Status updated to Released.");
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    static void openReportsDialog(JFrame parent) {
        int total = applications.size();
        int pending = 0, approved = 0, rejected = 0, ineligible = 0, released = 0;

        for (Application app : applications) {
            switch (app.applicationStatus) {
                case "Pending Admin Review": pending++; break;
                case "Approved": approved++; break;
                case "Rejected": rejected++; break;
                case "Ineligible": ineligible++; break;
            }
            if ("Released".equals(app.disbursementStatus)) {
                released++;
            }
        }

        String report = String.format(
            " SCHOLARSHIP REPORT \n\n" +
            "Total Applications: %d\n" +
            "Pending Admin Review: %d\n" +
            "Approved: %d\n" +
            "Rejected: %d\n" +
            "Ineligible: %d\n" +
            "Disbursements Released: %d\n",
            total, pending, approved, rejected, ineligible, released
        );

        JTextArea textArea = new JTextArea(report, 10, 30);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(parent, new JScrollPane(textArea), "System Report", JOptionPane.INFORMATION_MESSAGE);
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