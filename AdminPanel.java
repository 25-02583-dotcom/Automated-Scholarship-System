import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminPanel {
    public static final List<String[]> customPrograms = new ArrayList<>();
    private static final String CUSTOM_PROGRAMS_FILE = "custom_scholarships.txt";

    static {
        loadCustomProgramsFromFile();
    }

    public static void loadCustomProgramsFromFile() {
        File file = new File(CUSTOM_PROGRAMS_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            customPrograms.clear();
            for (String line : lines) {
                String[] parts = line.split("\\|", 3);
                if (parts.length == 3) {
                    customPrograms.add(new String[]{parts[0], parts[1], parts[2]});
                }
            }
        } catch (IOException ignored) {
        }
    }

    public static void saveCustomProgramsToFile() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(CUSTOM_PROGRAMS_FILE, false),
                    StandardCharsets.UTF_8))) {
            for (String[] prog : customPrograms) {
                String name = prog[0].replace("|", "/").trim();
                String elig = prog[1].replace("|", "/").trim();
                String ben = prog[2].replace("|", "/").trim();
                bw.write(name + "|" + elig + "|" + ben);
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    public static void createAndShowAdminDashboard() {
        loadCustomProgramsFromFile();

        JFrame frame = new JFrame("Administrator Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1020, 700));
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

        JLabel userLbl = new JLabel("Administrator (" + ScholarshipManager.ADMIN_USERNAME + ")");
        userLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLbl.setForeground(new Color(203, 213, 225));

        topBar.add(brandLbl, BorderLayout.WEST);
        topBar.add(userLbl, BorderLayout.EAST);
        frame.add(topBar, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(AutomatedApplication.BG_SOFT);
        body.setBorder(new EmptyBorder(22, 26, 22, 26));

        JPanel banner = AutomatedApplication.createRoundedPanel(AutomatedApplication.PRIMARY_DARK, null, 18);
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(20, 24, 20, 24));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeLbl = new JLabel("Welcome, Administrator");
        welcomeLbl.setFont(new Font("Segoe UI", Font.BOLD, 21));
        welcomeLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel("Review applications, manage scholarship programs, monitor disbursements, and view summaries.");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLbl.setForeground(new Color(203, 213, 225));

        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.add(welcomeLbl);
        textCol.add(Box.createRigidArea(new Dimension(0, 4)));
        textCol.add(subLbl);
        banner.add(textCol, BorderLayout.CENTER);

        JLabel secTitle = new JLabel("Administrator Actions");
        secTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        secTitle.setForeground(AutomatedApplication.TEXT_DARK);
        secTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cardsRow = new JPanel(new GridLayout(2, 2, 20, 18));
        cardsRow.setOpaque(false);
        cardsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));

        cardsRow.add(createActionCard(
            "REVIEW",
            new Color(224, 231, 255),
            AutomatedApplication.PRIMARY_ACCENT,
            "Review Applications",
            "Inspect submitted applications, browse uploaded files, and approve or reject.",
            "Open Review  ->",
            AutomatedApplication.PRIMARY_ACCENT,
            e -> openReviewApplicationsDialog(frame)
        ));

        cardsRow.add(createActionCard(
            "PROGRAMS",
            new Color(254, 243, 199),
            new Color(180, 83, 9),
            "Add Scholarship Program",
            "Publish new scholarship programs or delete custom programs permanently.",
            "Manage Programs  ->",
            new Color(180, 83, 9),
            e -> openAddScholarshipDialog(frame)
        ));

        cardsRow.add(createActionCard(
            "DISBURSEMENT",
            new Color(220, 252, 231),
            new Color(21, 128, 61),
            "Disbursement Monitoring",
            "All applicants appear automatically; mark payouts as Pending or Released.",
            "Open Disbursement  ->",
            new Color(21, 128, 61),
            e -> openDisbursementMonitoringDialog(frame)
        ));

        cardsRow.add(createActionCard(
            "REPORTS",
            new Color(241, 245, 249),
            AutomatedApplication.PRIMARY_DARK,
            "Summary Report",
            "View totals of applicants, applications, statuses, and released disbursements.",
            "View Summary  ->",
            AutomatedApplication.PRIMARY_DARK,
            e -> openSummaryReportDialog(frame)
        ));

        body.add(banner);
        body.add(Box.createRigidArea(new Dimension(0, 18)));
        body.add(secTitle);
        body.add(Box.createRigidArea(new Dimension(0, 14)));
        body.add(cardsRow);
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
        AutomatedApplication.applyButtonHover(logoutBtn, AutomatedApplication.BTN_RED,
            new Color(220, 38, 38));
        logoutBtn.setPreferredSize(new Dimension(96, 30));
        logoutBtn.addActionListener(e -> {
            saveCustomProgramsToFile();
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
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel badgeLbl = new JLabel(" " + badgeText + " ");
        badgeLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badgeLbl.setForeground(badgeFg);
        badgeLbl.setBackground(badgeBg);
        badgeLbl.setOpaque(true);
        badgeLbl.setBorder(new EmptyBorder(3, 8, 3, 8));
        badgeLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(AutomatedApplication.TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><div style='color:#64748b; font-size:12px; line-height:1.35;'>" + desc + "</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(AutomatedApplication.TEXT_MUTED);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topText = new JPanel();
        topText.setLayout(new BoxLayout(topText, BoxLayout.Y_AXIS));
        topText.setOpaque(false);
        topText.add(badgeLbl);
        topText.add(Box.createRigidArea(new Dimension(0, 8)));
        topText.add(lblTitle);
        topText.add(Box.createRigidArea(new Dimension(0, 4)));
        topText.add(lblDesc);

        JButton actionBtn = AutomatedApplication.createRoundedButton(btnText, btnColor, Color.WHITE, 8);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionBtn.setPreferredSize(new Dimension(155, 34));
        actionBtn.addActionListener(action);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomRow.setOpaque(false);
        bottomRow.add(actionBtn);

        card.add(topText, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);
        return card;
    }

    public static void openAddScholarshipDialog(JFrame parent) {
        loadCustomProgramsFromFile();

        JDialog dialog = new JDialog(parent, "Add Scholarship Program", true);
        dialog.setSize(780, 520);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formCard = AutomatedApplication.createRoundedPanel(Color.WHITE, new Color(199, 210, 254), 14);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField txtName = new JTextField();
        txtName.setPreferredSize(new Dimension(0, 30));

        JTextField txtElig = new JTextField();
        txtElig.setPreferredSize(new Dimension(0, 30));

        JTextField txtBen = new JTextField();
        txtBen.setPreferredSize(new Dimension(0, 30));

        JButton saveBtn = AutomatedApplication.createRoundedButton(
            "Save & Publish Scholarship", AutomatedApplication.BTN_GREEN, Color.WHITE, 8
        );
        saveBtn.setPreferredSize(new Dimension(0, 36));

        int r = 0;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.gridx = 0; gbc.gridy = r++; formCard.add(new JLabel("Scholarship Program Name:"), gbc);
        gbc.gridy = r++; formCard.add(txtName, gbc);

        gbc.gridy = r++; formCard.add(new JLabel("Eligibility Requirements:"), gbc);
        gbc.gridy = r++; formCard.add(txtElig, gbc);

        gbc.gridy = r++; formCard.add(new JLabel("Scholarship Benefits / Allowance:"), gbc);
        gbc.gridy = r++; formCard.add(txtBen, gbc);

        gbc.gridy = r++; gbc.insets = new Insets(8, 0, 0, 0);
        formCard.add(saveBtn, gbc);

        String[] cols = {"Scholarship Program Name", "Eligibility Requirements", "Scholarship Benefits / Allowance"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Runnable reloadTable = () -> {
            model.setRowCount(0);
            for (String[] prog : customPrograms) {
                model.addRow(new Object[]{prog[0], prog[1], prog[2]});
            }
        };
        reloadTable.run();

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        saveBtn.addActionListener(e -> {
            String name = txtName.getText().trim();
            String elig = txtElig.getText().trim();
            String ben = txtBen.getText().trim();

            if (name.isEmpty() || elig.isEmpty() || ben.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please complete all scholarship fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            customPrograms.add(new String[]{name, elig, ben});
            saveCustomProgramsToFile();
            reloadTable.run();

            txtName.setText("");
            txtElig.setText("");
            txtBen.setText("");

            JOptionPane.showMessageDialog(dialog, "Scholarship program saved and published!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton deleteBtn = AutomatedApplication.createActionButton(
            "Delete Selected", AutomatedApplication.BTN_RED, Color.WHITE, 140, 34
        );
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0 || row >= customPrograms.size()) {
                JOptionPane.showMessageDialog(dialog, "Select a custom scholarship program to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            customPrograms.remove(row);
            saveCustomProgramsToFile();
            reloadTable.run();
            JOptionPane.showMessageDialog(dialog, "Selected scholarship program deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(deleteBtn);
        bottomPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(formCard, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void openReviewApplicationsDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Review Applications", true);
        dialog.setSize(980, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        String[] cols = {
            "App ID", "Applicant ID", "Name", "Program",
            "GWA", "Annual Income", "Documents", "Status"
        };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Runnable reloadApps = () -> {
            model.setRowCount(0);
            for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
                String docName = (sa.getDocument() != null && sa.getDocument().getFileName() != null)
                    ? sa.getDocument().getFileName()
                    : "None";
                model.addRow(new Object[]{
                    sa.getApplicationID(),
                    sa.getApplicantID(),
                    sa.getApplicantName(),
                    sa.getScholarshipType(),
                    String.format("%.2f", sa.getGwa()),
                    String.format("P%.2f", sa.getAnnualIncome()),
                    docName,
                    sa.getStatus()
                });
            }
        };
        reloadApps.run();

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton viewFilesBtn = AutomatedApplication.createActionButton(
            "View Files", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 125, 34
        );
        viewFilesBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0 || row >= ScholarshipManager.applicationList.size()) {
                JOptionPane.showMessageDialog(dialog, "Select an application first.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ScholarshipApplication sa = ScholarshipManager.applicationList.get(row);
            openUploadedFilesChooserDialog(dialog, sa);
        });

        JButton approveBtn = AutomatedApplication.createActionButton(
            "Approve", AutomatedApplication.BTN_GREEN, Color.WHITE, 120, 34
        );
        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0 || row >= ScholarshipManager.applicationList.size()) {
                JOptionPane.showMessageDialog(dialog, "Select an application to approve.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ScholarshipApplication sa = ScholarshipManager.applicationList.get(row);
            sa.updateStatus("Approved");
            ScholarshipManager.saveAllData();
            ApplicantPanel.appendDisbursementNotice(
                sa.getApplicantID(),
                "Application Approved: " + sa.getApplicationID(),
                "Your scholarship application (" + sa.getApplicationID() + " - " + sa.getScholarshipType() + ") has been Approved by Admin."
            );
            reloadApps.run();
            JOptionPane.showMessageDialog(dialog, "Application approved and notice sent to applicant.", "Approved", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton rejectBtn = AutomatedApplication.createActionButton(
            "Reject", AutomatedApplication.BTN_RED, Color.WHITE, 120, 34
        );
        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0 || row >= ScholarshipManager.applicationList.size()) {
                JOptionPane.showMessageDialog(dialog, "Select an application to reject.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            ScholarshipApplication sa = ScholarshipManager.applicationList.get(row);
            sa.updateStatus("Rejected");
            ScholarshipManager.saveAllData();
            ApplicantPanel.appendDisbursementNotice(
                sa.getApplicantID(),
                "Application Rejected: " + sa.getApplicationID(),
                "Your scholarship application (" + sa.getApplicationID() + " - " + sa.getScholarshipType() + ") has been Rejected by Admin."
            );
            reloadApps.run();
            JOptionPane.showMessageDialog(dialog, "Application rejected and notice sent to applicant.", "Rejected", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(viewFilesBtn);
        bottomPanel.add(approveBtn);
        bottomPanel.add(rejectBtn);
        bottomPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private static List<String> collectCandidateFiles(ScholarshipApplication sa) {
        Map<String, String> entries = new LinkedHashMap<>();

        String savedRaw = ApplicantPanel.uploadedFilePathsByAppId.get(sa.getApplicationID());
        if (savedRaw != null && !savedRaw.trim().isEmpty()) {
            for (String p : savedRaw.split("\\|\\|")) {
                String trimmed = p.trim();
                if (!trimmed.isEmpty()) {
                    File f = new File(trimmed);
                    entries.put(f.getName(), f.getAbsolutePath());
                }
            }
        }

        if (sa.getDocument() != null && sa.getDocument().getFileName() != null) {
            String[] names = sa.getDocument().getFileName().split(",\\s*");
            for (String rawName : names) {
                String name = rawName.trim();
                if (!name.isEmpty() && !entries.containsKey(name)) {
                    File local = new File(name);
                    entries.put(name, local.exists() ? local.getAbsolutePath() : name);
                }
            }
        }

        return new ArrayList<>(entries.values());
    }

    private static void launchSingleFile(Component parent, String pathOrName) {
        File fileToOpen = new File(pathOrName);
        if (!fileToOpen.exists()) {
            File local = new File(fileToOpen.getName());
            if (local.exists()) {
                fileToOpen = local;
            }
        }
        if (!fileToOpen.exists()) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Locate File: " + fileToOpen.getName());
            int res = chooser.showOpenDialog(parent);
            if (res == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
                fileToOpen = chooser.getSelectedFile();
            }
        }
        if (!fileToOpen.exists()) {
            JOptionPane.showMessageDialog(parent, "Unable to locate file: " + pathOrName, "File Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fileToOpen);
            } else {
                JOptionPane.showMessageDialog(parent, "Desktop file opening is not supported on this system.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Could not open file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void openUploadedFilesChooserDialog(Component parent, ScholarshipApplication sa) {
        List<String> files = collectCandidateFiles(sa);
        if (files.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No uploaded files found for this application.", "No Files", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog fileDialog = new JDialog(
            SwingUtilities.getWindowAncestor(parent),
            "Uploaded Files — " + sa.getApplicationID() + " (" + sa.getApplicantName() + ")",
            Dialog.ModalityType.APPLICATION_MODAL
        );
        fileDialog.setSize(640, 340);
        fileDialog.setLocationRelativeTo(parent);
        fileDialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        fileDialog.setLayout(new BorderLayout(10, 10));

        String[] cols = {"#", "File Name", "Path / Status"};
        DefaultTableModel fileModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < files.size(); i++) {
            File f = new File(files.get(i));
            boolean exists = f.exists() || new File(f.getName()).exists();
            fileModel.addRow(new Object[]{
                i + 1,
                f.getName(),
                exists ? f.getAbsolutePath() : "Not found on disk (select to locate)"
            });
        }

        JTable fileTable = new JTable(fileModel);
        fileTable.setRowHeight(28);
        fileTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fileTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        fileTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        fileTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        fileTable.getColumnModel().getColumn(2).setPreferredWidth(360);
        if (!files.isEmpty()) {
            fileTable.setRowSelectionInterval(0, 0);
        }

        Runnable openSelected = () -> {
            int sel = fileTable.getSelectedRow();
            if (sel < 0 || sel >= files.size()) {
                JOptionPane.showMessageDialog(fileDialog, "Select a file from the list to view.", "No File Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            launchSingleFile(fileDialog, files.get(sel));
        };

        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openSelected.run();
                }
            }
        });

        JButton openSelectedBtn = AutomatedApplication.createActionButton(
            "Open Selected", AutomatedApplication.BTN_GREEN, Color.WHITE, 140, 34
        );
        openSelectedBtn.addActionListener(e -> openSelected.run());

        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );
        closeBtn.addActionListener(e -> fileDialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(openSelectedBtn);
        btnPanel.add(closeBtn);

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(14, 14, 14, 14));
        wrapper.add(new JScrollPane(fileTable), BorderLayout.CENTER);
        wrapper.add(btnPanel, BorderLayout.SOUTH);

        fileDialog.add(wrapper, BorderLayout.CENTER);
        fileDialog.setVisible(true);
    }

    private static Disbursement ensureDisbursementForApplication(ScholarshipApplication sa) {
        if (sa == null) {
            return null;
        }
        Disbursement existing = ScholarshipManager.findDisbursementByApplicationID(sa.getApplicationID());
        if (existing != null) {
            return existing;
        }
        int nextNum = 101;
        for (Disbursement d : ScholarshipManager.disbursementList) {
            if (d.getDisbursementID() != null && d.getDisbursementID().startsWith("DISB-")) {
                try {
                    int n = Integer.parseInt(d.getDisbursementID().substring(5).trim());
                    if (n >= nextNum) {
                        nextNum = n + 1;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        String disbId = "DISB-" + nextNum;
        Disbursement disb = new Disbursement(
            disbId,
            sa.getApplicationID(),
            sa.getApplicantID(),
            sa.getApplicantName(),
            0.0,
            LocalDate.now(),
            "Pending"
        );
        disb.recordDisbursement();
        ScholarshipManager.disbursementList.add(disb);
        return disb;
    }

    private static void syncAllApplicationsToDisbursements() {
        int beforeSize = ScholarshipManager.disbursementList.size();
        for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
            ensureDisbursementForApplication(sa);
        }
        if (ScholarshipManager.disbursementList.size() != beforeSize) {
            ScholarshipManager.saveAllData();
        }
    }

    public static void openDisbursementMonitoringDialog(JFrame parent) {
        syncAllApplicationsToDisbursements();

        JDialog dialog = new JDialog(parent, "Disbursement Monitoring", true);
        dialog.setSize(980, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        String[] cols = {
            "Disbursement ID", "App ID", "Applicant ID", "Applicant Name",
            "Cash Grant Amount", "Status", "Date"
        };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        final List<Disbursement> rowDisbursements = new ArrayList<>();

        Runnable reloadDisb = () -> {
            syncAllApplicationsToDisbursements();
            model.setRowCount(0);
            rowDisbursements.clear();

            for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
                Disbursement d = ensureDisbursementForApplication(sa);
                rowDisbursements.add(d);
                String applicantName = (d.getApplicantName() != null && !d.getApplicantName().trim().isEmpty())
                    ? d.getApplicantName()
                    : sa.getApplicantName();
                model.addRow(new Object[]{
                    d.getDisbursementID(),
                    sa.getApplicationID(),
                    sa.getApplicantID(),
                    applicantName,
                    String.format("P%.2f", d.getAmount()),
                    d.getPaymentStatus(),
                    d.getDate()
                });
            }
        };
        reloadDisb.run();

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton pendingBtn = AutomatedApplication.createActionButton(
            "Mark Pending", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 140, 34
        );
        pendingBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0 || row >= rowDisbursements.size()) {
                JOptionPane.showMessageDialog(dialog, "Select a disbursement record to set as Pending.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Disbursement d = rowDisbursements.get(row);
            String amtRaw = JOptionPane.showInputDialog(
                dialog,
                "Confirm or enter Cash Grant Amount for Pending status (in P):",
                String.format("%.2f", d.getAmount())
            );
            if (amtRaw == null || amtRaw.trim().isEmpty()) {
                return;
            }
            double pendingAmount;
            try {
                pendingAmount = Double.parseDouble(amtRaw.trim());
                if (pendingAmount < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid cash grant amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            d.updatePaymentStatus("Pending", LocalDate.now());
            ScholarshipManager.saveAllData();
            ApplicantPanel.appendDisbursementNotice(
                d.getApplicantID(),
                "Cash Grant Pending: " + String.format("P%.2f", pendingAmount),
                String.format(
                    "Your cash grant of P%.2f (Disbursement ID: %s, App ID: %s, Date: %s) is currently marked as Pending.",
                    pendingAmount, d.getDisbursementID(), d.getApplicationID(), d.getDate()
                )
            );
            reloadDisb.run();
            JOptionPane.showMessageDialog(dialog, "Cash grant marked as Pending and notice sent to applicant.", "Pending", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton releaseBtn = AutomatedApplication.createActionButton(
            "Mark Released", AutomatedApplication.BTN_GREEN, Color.WHITE, 140, 34
        );
        releaseBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0 || row >= rowDisbursements.size()) {
                JOptionPane.showMessageDialog(dialog, "Select a disbursement record to release.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Disbursement d = rowDisbursements.get(row);
            String amtRaw = JOptionPane.showInputDialog(
                dialog,
                "Confirm or enter Cash Grant Amount to Release (in P):",
                String.format("%.2f", d.getAmount())
            );
            if (amtRaw == null || amtRaw.trim().isEmpty()) {
                return;
            }
            double finalAmount;
            try {
                finalAmount = Double.parseDouble(amtRaw.trim());
                if (finalAmount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid cash grant amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            d.updatePaymentStatus("Released", LocalDate.now());
            ScholarshipManager.saveAllData();
            ApplicantPanel.appendDisbursementNotice(
                d.getApplicantID(),
                "Cash Grant Released: " + String.format("P%.2f", finalAmount),
                String.format(
                    "Your grant of P%.2f has been released (Disbursement ID: %s, App ID: %s, Date: %s).",
                    finalAmount, d.getDisbursementID(), d.getApplicationID(), d.getDate()
                )
            );
            reloadDisb.run();
            JOptionPane.showMessageDialog(dialog, "Cash grant marked as Released and notice sent to applicant.", "Released", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(pendingBtn);
        bottomPanel.add(releaseBtn);
        bottomPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void openSummaryReportDialog(JFrame parent) {
        int totalApplicants = ScholarshipManager.applicantList.size();
        int totalApps = ScholarshipManager.applicationList.size();
        int approved = 0;
        int rejected = 0;
        int pending = 0;

        for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
            String st = sa.getStatus();
            if ("Approved".equalsIgnoreCase(st)) {
                approved++;
            } else if ("Rejected".equalsIgnoreCase(st)) {
                rejected++;
            } else {
                pending++;
            }
        }

        double totalReleased = 0.0;
        for (Disbursement d : ScholarshipManager.disbursementList) {
            if ("Released".equalsIgnoreCase(d.getPaymentStatus())) {
                totalReleased += d.getAmount();
            }
        }

        String summaryText = "System Summary Report\n\n"
            + "Registered Applicants: " + totalApplicants + "\n"
            + "Total Applications: " + totalApps + "\n"
            + "Approved Applications: " + approved + "\n"
            + "Rejected Applications: " + rejected + "\n"
            + "Pending / Other Applications: " + pending + "\n"
            + "Total Released Disbursements: P" + String.format("%.2f", totalReleased);

        JOptionPane.showMessageDialog(parent, summaryText, "Summary Report", JOptionPane.INFORMATION_MESSAGE);
    }
}