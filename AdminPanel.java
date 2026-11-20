import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel {
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
        body.setBorder(new EmptyBorder(22, 26, 22, 26));

        JPanel banner = AutomatedApplication.createRoundedPanel(AutomatedApplication.PRIMARY_DARK, null, 18);
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(20, 24, 20, 24));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeLbl = new JLabel("Welcome, Administrator");
        welcomeLbl.setFont(new Font("Segoe UI", Font.BOLD, 21));
        welcomeLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel("Review applications, monitor disbursements, and generate reports.");
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

        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsRow.setOpaque(false);
        cardsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsRow.setPreferredSize(new Dimension(0, 205));
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 215));

        cardsRow.add(createActionCard(
            "APPLICATIONS",
            new Color(224, 231, 255),
            AutomatedApplication.PRIMARY_ACCENT,
            "Review Applications",
            "Inspect pending applications and approve or reject entries.",
            "Review Entries  ->",
            AutomatedApplication.PRIMARY_ACCENT,
            e -> openReviewApplicationsDialog(frame)
        ));

        cardsRow.add(createActionCard(
            "DISBURSEMENT",
            new Color(220, 252, 231),
            new Color(21, 128, 61),
            "Disbursement Monitoring",
            "Record disbursement details and mark allowances as Pending or Released.",
            "Manage Funds  ->",
            new Color(21, 128, 61),
            e -> openDisbursementDialog(frame)
        ));

        cardsRow.add(createActionCard(
            "REPORTS",
            new Color(254, 243, 199),
            new Color(180, 83, 9),
            "Generate Reports",
            "View summary reports of applications and disbursement statuses.",
            "View Report  ->",
            new Color(180, 83, 9),
            e -> openReportsDialog(frame)
        ));

        body.add(banner);
        body.add(Box.createVerticalGlue());
        body.add(Box.createRigidArea(new Dimension(0, 18)));
        body.add(secTitle);
        body.add(Box.createRigidArea(new Dimension(0, 14)));
        body.add(cardsRow);
        body.add(Box.createVerticalGlue());
        frame.add(body, BorderLayout.CENTER);

        // Bottom / Footer bar with Logout button on the right
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
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

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
        topText.add(Box.createRigidArea(new Dimension(0, 10)));
        topText.add(lblTitle);
        topText.add(Box.createRigidArea(new Dimension(0, 5)));
        topText.add(lblDesc);

        JButton actionBtn = AutomatedApplication.createRoundedButton(btnText, btnColor, Color.WHITE, 8);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionBtn.setPreferredSize(new Dimension(145, 34));
        actionBtn.addActionListener(action);

        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomRow.setOpaque(false);
        bottomRow.add(actionBtn);

        card.add(topText, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);
        return card;
    }

    public static void openReviewApplicationsDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Review Pending Applications", true);
        dialog.setSize(780, 410);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);

        String[] columns = {"App ID", "Applicant ID", "Name", "Program", "GWA", "Income", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
            if ("Pending Admin Review".equals(sa.getStatus())) {
                model.addRow(new Object[]{
                    sa.getApplicationID(), sa.getApplicantID(), sa.getApplicantName(),
                    sa.getScholarshipType(), sa.getGwa(), sa.getAnnualIncome(), sa.getStatus()
                });
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel tableCard = AutomatedApplication.createRoundedPanel(Color.WHITE, new Color(199, 210, 254), 14);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 12, 10, 12));
        tableCard.add(table.getTableHeader(), BorderLayout.NORTH);
        tableCard.add(table, BorderLayout.CENTER);

        JButton approveBtn = AutomatedApplication.createActionButton(
            "Approve Selected", AutomatedApplication.BTN_GREEN, Color.WHITE, 145, 34
        );
        JButton rejectBtn = AutomatedApplication.createActionButton(
            "Reject Selected", AutomatedApplication.BTN_RED, Color.WHITE, 145, 34
        );

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 6));
        bottomPanel.setOpaque(false);
        bottomPanel.add(approveBtn);
        bottomPanel.add(rejectBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(tableCard, BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);
        dialog.add(body, BorderLayout.CENTER);

        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String applicantID = (String) model.getValueAt(row, 1);
            ScholarshipApplication sa = ScholarshipManager.findApplicationByApplicantID(applicantID);
            if (sa != null) {
                ScholarshipManager.reviewApplication(sa, true);
                JOptionPane.showMessageDialog(dialog, "Application approved and disbursement record created.");
                dialog.dispose();
            }
        });

        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String applicantID = (String) model.getValueAt(row, 1);
            ScholarshipApplication sa = ScholarshipManager.findApplicationByApplicantID(applicantID);
            if (sa != null) {
                ScholarshipManager.reviewApplication(sa, false);
                JOptionPane.showMessageDialog(dialog, "Application rejected and saved.");
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    public static void openDisbursementDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Disbursement Monitoring", true);
        dialog.setSize(780, 410);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);

        String[] columns = {"App ID", "Applicant ID", "Name", "Program", "Disbursement Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
            if ("Approved".equals(sa.getStatus())) {
                Disbursement d = ScholarshipManager.findDisbursementByApplicationID(sa.getApplicationID());
                String dStatus = (d != null) ? d.getPaymentStatus() : "Pending";
                model.addRow(new Object[]{
                    sa.getApplicationID(), sa.getApplicantID(), sa.getApplicantName(),
                    sa.getScholarshipType(), dStatus
                });
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel tableCard = AutomatedApplication.createRoundedPanel(Color.WHITE, new Color(199, 210, 254), 14);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 12, 10, 12));
        tableCard.add(table.getTableHeader(), BorderLayout.NORTH);
        tableCard.add(table, BorderLayout.CENTER);

        JButton setPendingBtn = AutomatedApplication.createActionButton(
            "Mark as Pending", new Color(241, 196, 15), Color.DARK_GRAY, 145, 34
        );
        JButton setReleasedBtn = AutomatedApplication.createActionButton(
            "Mark as Released", AutomatedApplication.BTN_GREEN, Color.WHITE, 145, 34
        );

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 6));
        bottomPanel.setOpaque(false);
        bottomPanel.add(setPendingBtn);
        bottomPanel.add(setReleasedBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(tableCard, BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);
        dialog.add(body, BorderLayout.CENTER);

        setPendingBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String applicantID = (String) model.getValueAt(row, 1);
            ScholarshipApplication sa = ScholarshipManager.findApplicationByApplicantID(applicantID);
            if (sa != null) {
                ScholarshipManager.updateDisbursementStatus(sa, "Pending");
                JOptionPane.showMessageDialog(dialog, "Disbursement status updated to Pending and saved.");
                dialog.dispose();
            }
        });

        setReleasedBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select an application.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String applicantID = (String) model.getValueAt(row, 1);
            ScholarshipApplication sa = ScholarshipManager.findApplicationByApplicantID(applicantID);
            if (sa != null) {
                ScholarshipManager.updateDisbursementStatus(sa, "Released");
                JOptionPane.showMessageDialog(dialog, "Disbursement status updated to Released and saved.");
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    public static void openReportsDialog(JFrame parent) {
        String report = ScholarshipManager.generateSummary();
        JTextArea textArea = new JTextArea(report, 12, 36);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(parent, textArea, "System Report", JOptionPane.INFORMATION_MESSAGE);
    }
}