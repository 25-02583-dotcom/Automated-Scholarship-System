import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdminPanel {
    public static final List<String[]> customPrograms = new ArrayList<>();

    private static ScholarshipApplication findAppById(String appId) {
        for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
            if (sa.getApplicationID().equalsIgnoreCase(appId)) {
                return sa;
            }
        }
        return null;
    }

    private static void openUploadedFileForApplication(Component parent, String appId, String fileName) {
        if (fileName == null || fileName.trim().isEmpty() || "None".equalsIgnoreCase(fileName.trim())) {
            JOptionPane.showMessageDialog(parent, "No uploaded file found for this application.", "No File", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String savedPath = ApplicantPanel.uploadedFilePathsByAppId.get(appId);
        File targetFile = null;

        if (savedPath != null && !savedPath.trim().isEmpty()) {
            File f = new File(savedPath);
            if (f.exists()) {
                targetFile = f;
            }
        }

        if (targetFile == null) {
            File localFile = new File(fileName);
            if (localFile.exists()) {
                targetFile = localFile;
            }
        }

        if (targetFile == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Uploaded File: " + fileName);
            int res = chooser.showOpenDialog(parent);
            if (res == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
                targetFile = chooser.getSelectedFile();
                ApplicantPanel.uploadedFilePathsByAppId.put(appId, targetFile.getAbsolutePath());
            }
        }

        if (targetFile == null || !targetFile.exists()) {
            JOptionPane.showMessageDialog(parent, "File not found: " + fileName, "File Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(targetFile);
            } else {
                JOptionPane.showMessageDialog(parent, "Opening files is not supported on this device.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Unable to open file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void createAndShowAdminDashboard() {
        JFrame frame = new JFrame("Administrator Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(980, 680));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(AutomatedApplication.BG_SOFT);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AutomatedApplication.PRIMARY_DARK);
        topBar.setPreferredSize(new Dimension(0, 54));
        topBar.setBorder(new EmptyBorder(0, 26, 0, 26));

        JLabel brandLbl = new JLabel("Administrator Portal");
        brandLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        brandLbl.setForeground(Color.WHITE);

        JLabel roleLbl = new JLabel("System Administrator");
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        roleLbl.setForeground(new Color(203, 213, 225));

        topBar.add(brandLbl, BorderLayout.WEST);
        topBar.add(roleLbl, BorderLayout.EAST);
        frame.add(topBar, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(AutomatedApplication.BG_SOFT);
        body.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel banner = AutomatedApplication.createRoundedPanel(AutomatedApplication.PRIMARY_DARK, null, 18);
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(18, 24, 18, 24));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeLbl = new JLabel("Welcome, Administrator");
        welcomeLbl.setFont(new Font("Segoe UI", Font.BOLD, 21));
        welcomeLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel("Review applications, monitor disbursements, add scholarships, and generate reports.");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLbl.setForeground(new Color(203, 213, 225));

        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.add(welcomeLbl);
        textCol.add(Box.createRigidArea(new Dimension(0, 4)));
        textCol.add(subLbl);
        banner.add(textCol, BorderLayout.CENTER);

        JLabel secTitle = new JLabel("Admin Management");
        secTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        secTitle.setForeground(AutomatedApplication.TEXT_DARK);
        secTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 20, 16));
        cardsGrid.setOpaque(false);
        cardsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsGrid.setPreferredSize(new Dimension(0, 340));
        cardsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 370));

        cardsGrid.add(createActionCard(
            "APPLICATIONS",
            new Color(224, 231, 255),
            AutomatedApplication.PRIMARY_ACCENT,
            "Review Applications",
            "Inspect pending applications, view uploaded files, and approve or reject entries.",
            "Review Entries  ->",
            AutomatedApplication.PRIMARY_ACCENT,
            e -> openReviewApplicationsDialog(frame)
        ));

        cardsGrid.add(createActionCard(
            "DISBURSEMENT",
            new Color(220, 252, 231),
            new Color(21, 128, 61),
            "Disbursement Monitoring",
            "Record disbursement details and mark allowances as Pending or Released.",
            "Manage Funds  ->",
            new Color(21, 128, 61),
            e -> openDisbursementMonitoringDialog(frame)
        ));

        cardsGrid.add(createActionCard(
            "SCHOLARSHIPS",
            new Color(224, 242, 254),
            new Color(3, 105, 161),
            "Add Scholarship",
            "Create and publish new scholarship programs visible to applicants.",
            "Add Scholarship  ->",
            new Color(3, 105, 161),
            e -> openAddScholarshipDialog(frame)
        ));

        cardsGrid.add(createActionCard(
            "REPORTS",
            new Color(254, 243, 199),
            new Color(180, 83, 9),
            "Generate Reports",
            "View summary reports of applications, eligibility, and disbursement statuses.",
            "View Report  ->",
            new Color(180, 83, 9),
            e -> openReportsDialog(frame)
        ));

        body.add(banner);
        body.add(Box.createVerticalGlue());
        body.add(Box.createRigidArea(new Dimension(0, 12)));
        body.add(secTitle);
        body.add(Box.createRigidArea(new Dimension(0, 12)));
        body.add(cardsGrid);
        body.add(Box.createVerticalGlue());
        frame.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(AutomatedApplication.PRIMARY_DARK);
        footer.setPreferredSize(new Dimension(0, 46));
        footer.setBorder(new EmptyBorder(0, 26, 0, 26));

        JLabel footLbl = new JLabel("Automated Scholarship Application System  •  Administrator Portal");
        footLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footLbl.setForeground(new Color(203, 213, 225));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(AutomatedApplication.BTN_RED);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(96, 30));
        logoutBtn.addActionListener(e -> {
            ScholarshipManager.saveAllData();
            frame.dispose();
            LoginPanel.createAndShowLoginWindow();
        });

        footer.add(footLbl, BorderLayout.WEST);
        footer.add(logoutBtn, BorderLayout.EAST);
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JPanel createActionCard(
            String badgeText,
            Color badgeBg,
            Color badgeFg,
            String title,
            String desc,
            String btnText,
            Color btnColor,
            java.awt.event.ActionListener action
    ) {
        JPanel card = AutomatedApplication.createRoundedPanel(
            Color.WHITE, new Color(199, 210, 254), 16
        );
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel badgeLbl = new JLabel(" " + badgeText + " ");
        badgeLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badgeLbl.setForeground(badgeFg);
        badgeLbl.setBackground(badgeBg);
        badgeLbl.setOpaque(true);
        badgeLbl.setBorder(new EmptyBorder(3, 8, 3, 8));
        badgeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(AutomatedApplication.TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><div style='color:#64748b; font-size:12px; line-height:1.3;'>" + desc + "</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(AutomatedApplication.TEXT_MUTED);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topText = new JPanel();
        topText.setLayout(new BoxLayout(topText, BoxLayout.Y_AXIS));
        topText.setOpaque(false);
        topText.add(badgeLbl);
        topText.add(Box.createRigidArea(new Dimension(0, 7)));
        topText.add(lblTitle);
        topText.add(Box.createRigidArea(new Dimension(0, 4)));
        topText.add(lblDesc);

        JButton actionBtn = AutomatedApplication.createRoundedButton(btnText, btnColor, Color.WHITE, 8);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionBtn.setPreferredSize(new Dimension(155, 32));
        actionBtn.addActionListener(action);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomRow.setOpaque(false);
        bottomRow.add(actionBtn);

        card.add(topText, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);
        return card;
    }

    public static void openAddScholarshipDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add Scholarship Program", true);
        dialog.setSize(760, 540);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formCard = AutomatedApplication.createRoundedPanel(Color.WHITE, new Color(199, 210, 254), 16);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(16, 20, 16, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 0, 3, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField txtProgName = new JTextField();
        txtProgName.setPreferredSize(new Dimension(0, 30));

        JTextField txtEligibility = new JTextField();
        txtEligibility.setPreferredSize(new Dimension(0, 30));

        JTextField txtBenefits = new JTextField();
        txtBenefits.setPreferredSize(new Dimension(0, 30));

        JButton saveBtn = AutomatedApplication.createRoundedButton(
            "Save & Publish Scholarship", AutomatedApplication.BTN_GREEN, Color.WHITE, 10
        );
        saveBtn.setPreferredSize(new Dimension(0, 36));

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r++; formCard.add(new JLabel("Scholarship Program Name:"), gbc);
        gbc.gridy = r++; formCard.add(txtProgName, gbc);

        gbc.gridy = r++; formCard.add(new JLabel("Eligibility Requirements:"), gbc);
        gbc.gridy = r++; formCard.add(txtEligibility, gbc);

        gbc.gridy = r++; formCard.add(new JLabel("Scholarship Benefits / Allowance:"), gbc);
        gbc.gridy = r++; formCard.add(txtBenefits, gbc);

        gbc.gridy = r++; gbc.insets = new Insets(10, 0, 0, 0);
        formCard.add(saveBtn, gbc);

        String[] cols = {"Scholarship Program Name", "Eligibility Requirements", "Scholarship Benefits / Allowance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        Runnable refreshTable = () -> {
            model.setRowCount(0);
            for (String[] prog : customPrograms) {
                model.addRow(new Object[]{prog[0], prog[1], prog[2]});
            }
        };
        refreshTable.run();

        JButton deleteBtn = AutomatedApplication.createActionButton(
            "Delete Selected", AutomatedApplication.BTN_RED, Color.WHITE, 145, 34
        );
        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );

        saveBtn.addActionListener(e -> {
            String name = txtProgName.getText().trim();
            String elig = txtEligibility.getText().trim();
            String ben = txtBenefits.getText().trim();

            if (name.isEmpty() || elig.isEmpty() || ben.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill out all scholarship fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            customPrograms.add(new String[]{name, elig, ben});
            ScholarshipManager.saveAllData();
            refreshTable.run();
            txtProgName.setText("");
            txtEligibility.setText("");
            txtBenefits.setText("");
            JOptionPane.showMessageDialog(dialog, "Scholarship program added! It is now visible in Applicant Portal.", "Added", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a scholarship program to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            customPrograms.remove(row);
            ScholarshipManager.saveAllData();
            refreshTable.run();
            JOptionPane.showMessageDialog(dialog, "Selected scholarship program deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(deleteBtn);
        btnPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(formCard, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void openReviewApplicationsDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Review Applications", true);
        dialog.setSize(1120, 520);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        String[] cols = {
            "App ID", "Applicant ID", "Name", "Program",
            "GWA", "Income", "Uploaded File", "Doc Status", "App Status"
        };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        Runnable refreshTable = () -> {
            model.setRowCount(0);
            for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
                String uploadedFile = (sa.getDocument() != null && sa.getDocument().getFileName() != null)
                    ? sa.getDocument().getFileName()
                    : "None";
                String docStatus = (sa.getDocument() != null && sa.getDocument().getVerificationStatus() != null)
                    ? sa.getDocument().getVerificationStatus()
                    : "Pending";

                model.addRow(new Object[]{
                    sa.getApplicationID(),
                    sa.getApplicantID(),
                    sa.getApplicantName(),
                    sa.getScholarshipType(),
                    String.format("%.2f", sa.getGwa()),
                    String.format("P%.2f", sa.getAnnualIncome()),
                    uploadedFile,
                    docStatus,
                    sa.getStatus()
                });
            }
        };
        refreshTable.run();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String appId = (String) model.getValueAt(row, 0);
                        String fileName = (String) model.getValueAt(row, 6);
                        openUploadedFileForApplication(dialog, appId, fileName);
                    }
                }
            }
        });

        JButton openFileBtn = AutomatedApplication.createActionButton(
            "Open File", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 120, 34
        );
        JButton approveBtn = AutomatedApplication.createActionButton(
            "Approve Selected", AutomatedApplication.BTN_GREEN, Color.WHITE, 145, 34
        );
        JButton rejectBtn = AutomatedApplication.createActionButton(
            "Reject Selected", AutomatedApplication.BTN_RED, Color.WHITE, 145, 34
        );
        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );

        openFileBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String appId = (String) model.getValueAt(row, 0);
            String fileName = (String) model.getValueAt(row, 6);
            openUploadedFileForApplication(dialog, appId, fileName);
        });

        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String appId = (String) model.getValueAt(row, 0);
            ScholarshipApplication sa = findAppById(appId);
            if (sa != null) {
                sa.setStatus("Approved");
                ScholarshipManager.saveAllData();
                refreshTable.run();
                JOptionPane.showMessageDialog(dialog, "Application " + appId + " marked as Approved.");
            }
        });

        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String appId = (String) model.getValueAt(row, 0);
            ScholarshipApplication sa = findAppById(appId);
            if (sa != null) {
                sa.setStatus("Rejected");
                ScholarshipManager.saveAllData();
                refreshTable.run();
                JOptionPane.showMessageDialog(dialog, "Application " + appId + " marked as Rejected.");
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(openFileBtn);
        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);
        btnPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void openDisbursementMonitoringDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Disbursement Monitoring", true);
        dialog.setSize(920, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        String[] cols = {"Disbursement ID", "Application ID", "Applicant ID", "Applicant Name", "Amount", "Status", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        Runnable refreshTable = () -> {
            model.setRowCount(0);
            for (Disbursement d : ScholarshipManager.disbursementList) {
                model.addRow(new Object[]{
                    d.getDisbursementID(),
                    d.getApplicationID(),
                    d.getApplicantID(),
                    d.getApplicantName(),
                    String.format("P%.2f", d.getAmount()),
                    d.getPaymentStatus(),
                    d.getDate()
                });
            }
        };
        refreshTable.run();

        JButton addDisbBtn = AutomatedApplication.createActionButton(
            "Record Disbursement", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 165, 34
        );
        JButton releaseBtn = AutomatedApplication.createActionButton(
            "Mark Released", AutomatedApplication.BTN_GREEN, Color.WHITE, 140, 34
        );
        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.BTN_RED, Color.WHITE, 130, 34
        );

        addDisbBtn.addActionListener(e -> {
            String appId = JOptionPane.showInputDialog(dialog, "Enter Application ID (e.g., APP-101):");
            if (appId == null || appId.trim().isEmpty()) return;

            ScholarshipApplication sa = findAppById(appId.trim());
            if (sa == null) {
                JOptionPane.showMessageDialog(dialog, "Application ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String amtRaw = JOptionPane.showInputDialog(dialog, "Enter Disbursement Amount (e.g., 5000):");
            if (amtRaw == null || amtRaw.trim().isEmpty()) return;

            try {
                double amt = Double.parseDouble(amtRaw.trim());
                if (amt <= 0) throw new NumberFormatException();

                String disbId = "DSB-" + (ScholarshipManager.disbursementList.size() + 101);
                Disbursement disb = new Disbursement(
                    disbId,
                    sa.getApplicationID(),
                    sa.getApplicantID(),
                    sa.getApplicantName(),
                    amt,
                    LocalDate.now(),
                    "Pending"
                );
                disb.recordDisbursement();
                ScholarshipManager.disbursementList.add(disb);
                ScholarshipManager.saveAllData();
                refreshTable.run();
                JOptionPane.showMessageDialog(dialog, "Disbursement recorded as Pending.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        releaseBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a disbursement entry first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String disbId = (String) model.getValueAt(row, 0);
            for (Disbursement d : ScholarshipManager.disbursementList) {
                if (d.getDisbursementID().equals(disbId)) {
                    d.updatePaymentStatus("Released", LocalDate.now());
                    ScholarshipManager.saveAllData();
                    refreshTable.run();
                    JOptionPane.showMessageDialog(dialog, "Disbursement " + disbId + " marked as Released.");
                    return;
                }
            }
        });

        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(addDisbBtn);
        btnPanel.add(releaseBtn);
        btnPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void openReportsDialog(JFrame parent) {
        int totalApps = ScholarshipManager.applicationList.size();
        int approved = 0;
        int rejected = 0;
        int pending = 0;
        double totalDisbursed = 0.0;

        for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
            String st = sa.getStatus();
            if ("Approved".equalsIgnoreCase(st) || "Eligible".equalsIgnoreCase(st)) {
                approved++;
            } else if ("Rejected".equalsIgnoreCase(st) || "Not Eligible".equalsIgnoreCase(st) || "Incomplete".equalsIgnoreCase(st)) {
                rejected++;
            } else {
                pending++;
            }
        }

        for (Disbursement d : ScholarshipManager.disbursementList) {
            if ("Released".equalsIgnoreCase(d.getPaymentStatus())) {
                totalDisbursed += d.getAmount();
            }
        }

        String summary = String.format(
            "<html><div style='font-family: Segoe UI; padding: 8px; line-height: 1.5;'>" +
            "<h3 style='margin:0 0 8px 0;'>Scholarship & Disbursement Summary Report</h3>" +
            "<b>Total Registered Applicants:</b> %d<br>" +
            "<b>Total Applications Submitted:</b> %d<br>" +
            "<b>Approved / Eligible:</b> <font color='#15803d'>%d</font><br>" +
            "<b>Pending Review:</b> <font color='#b45309'>%d</font><br>" +
            "<b>Rejected / Incomplete:</b> <font color='#dc2626'>%d</font><br>" +
            "<b>Total Released Disbursements:</b> <font color='#4338ca'>P%.2f</font></div></html>",
            ScholarshipManager.applicantList.size(), totalApps, approved, pending, rejected, totalDisbursed
        );

        JOptionPane.showMessageDialog(parent, new JLabel(summary), "Summary Report", JOptionPane.INFORMATION_MESSAGE);
    }
}