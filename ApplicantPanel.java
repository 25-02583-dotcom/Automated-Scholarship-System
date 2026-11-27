import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApplicantPanel {
    public static final Map<String, String> uploadedFilePathsByAppId = new HashMap<>();
    private static final String NOTICES_FILE = "disbursement_notices.txt";
    private static final String DRAFTS_FILE = "draft_applications.txt";

    private static String sanitizeField(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "/").replace("\r", " ").replace("\n", " ");
    }

    private static String joinFilePaths(List<String> paths) {
        if (paths == null || paths.isEmpty()) {
            return "";
        }
        List<String> clean = new ArrayList<>();
        for (String p : paths) {
            if (p != null && !p.trim().isEmpty()) {
                clean.add(p.trim().replace("|", "/"));
            }
        }
        return String.join("||", clean);
    }

    private static List<String> splitFilePaths(String raw) {
        List<String> result = new ArrayList<>();
        if (raw == null || raw.trim().isEmpty()) {
            return result;
        }
        String[] parts = raw.split("\\|\\|");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty() && !result.contains(trimmed)) {
                result.add(trimmed);
            }
        }
        return result;
    }

    private static String formatFileNamesDisplay(List<String> paths) {
        if (paths == null || paths.isEmpty()) {
            return "";
        }
        List<String> names = new ArrayList<>();
        for (String p : paths) {
            names.add(new File(p).getName());
        }
        return String.join(", ", names);
    }

    private static Map<String, String[]> loadAllDrafts() {
        Map<String, String[]> drafts = new LinkedHashMap<>();
        File file = new File(DRAFTS_FILE);
        if (!file.exists()) {
            return drafts;
        }
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|", 6);
                if (parts.length >= 2) {
                    String keyId = parts[0].trim();
                    String prog = parts.length > 1 ? parts[1] : "";
                    String docCat = parts.length > 2 ? parts[2] : "Academic Record";
                    String gwa = parts.length > 3 ? parts[3] : "";
                    String income = parts.length > 4 ? parts[4] : "";
                    String filePaths = parts.length > 5 ? parts[5] : "";
                    drafts.put(keyId, new String[]{prog, docCat, gwa, income, filePaths});
                }
            }
        } catch (IOException ignored) {
        }
        return drafts;
    }

    private static void writeAllDrafts(Map<String, String[]> drafts) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(DRAFTS_FILE, false), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, String[]> entry : drafts.entrySet()) {
                String[] d = entry.getValue();
                String line = String.join("|",
                    sanitizeField(entry.getKey()),
                    sanitizeField(d.length > 0 ? d[0] : ""),
                    sanitizeField(d.length > 1 ? d[1] : ""),
                    sanitizeField(d.length > 2 ? d[2] : ""),
                    sanitizeField(d.length > 3 ? d[3] : ""),
                    sanitizeField(d.length > 4 ? d[4] : "")
                );
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ignored) {
        }
    }

    private static String buildDraftKey(String applicantId, String program) {
        String cleanApp = applicantId != null ? applicantId.trim() : "";
        String cleanProg = program != null ? program.trim() : "";
        return cleanProg.isEmpty() ? cleanApp : cleanApp + "::" + cleanProg;
    }

    private static void saveDraftForApplicant(
            String applicantId,
            String program,
            String docCategory,
            String gwa,
            String income,
            String joinedFilePaths
    ) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return;
        }
        Map<String, String[]> drafts = loadAllDrafts();
        String[] payload = new String[]{
            program != null ? program : "",
            docCategory != null ? docCategory : "Academic Record",
            gwa != null ? gwa : "",
            income != null ? income : "",
            joinedFilePaths != null ? joinedFilePaths : ""
        };
        drafts.put(buildDraftKey(applicantId, program), payload);
        drafts.put(applicantId.trim(), payload);
        writeAllDrafts(drafts);
    }

    private static String[] getDraftForApplicant(String applicantId, String program) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return null;
        }
        Map<String, String[]> drafts = loadAllDrafts();
        if (program != null && !program.trim().isEmpty()) {
            String[] byProg = drafts.get(buildDraftKey(applicantId, program));
            if (byProg != null) {
                return byProg;
            }
        }
        return drafts.get(applicantId.trim());
    }

    private static void clearDraftForApplicant(String applicantId, String program) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return;
        }
        Map<String, String[]> drafts = loadAllDrafts();
        boolean changed = false;
        if (program != null && !program.trim().isEmpty()) {
            if (drafts.remove(buildDraftKey(applicantId, program)) != null) {
                changed = true;
            }
        }
        if (drafts.remove(applicantId.trim()) != null) {
            changed = true;
        }
        if (changed) {
            writeAllDrafts(drafts);
        }
    }

    private static List<ScholarshipApplication> getApplicationsForApplicant(String applicantId) {
        List<ScholarshipApplication> matches = new ArrayList<>();
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return matches;
        }
        for (ScholarshipApplication sa : ScholarshipManager.applicationList) {
            if (applicantId.trim().equalsIgnoreCase(sa.getApplicantID())) {
                matches.add(sa);
            }
        }
        return matches;
    }

    public static void appendDisbursementNotice(String applicantId, String subject, String message) {
        if (applicantId == null || applicantId.trim().isEmpty()) {
            return;
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String cleanSubject = subject.replace("|", "/").replace("\n", " ").trim();
        String cleanMessage = message.replace("|", "/").replace("\n", " ").trim();
        String line = applicantId.trim() + "|" + timestamp + "|" + cleanSubject + "|" + cleanMessage;

        List<String> existing = loadRawNoticeLines();
        if (!existing.isEmpty()) {
            String last = existing.get(existing.size() - 1);
            String[] parts = last.split("\\|", 4);
            if (parts.length == 4 && parts[0].equals(applicantId.trim())
                    && parts[2].equals(cleanSubject) && parts[3].equals(cleanMessage)) {
                return;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(NOTICES_FILE, true), StandardCharsets.UTF_8))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException ignored) {
        }
    }

    private static List<String> loadRawNoticeLines() {
        List<String> lines = new ArrayList<>();
        File file = new File(NOTICES_FILE);
        if (!file.exists()) {
            return lines;
        }
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
        return lines;
    }

    private static List<String[]> getNoticesForApplicant(String applicantId) {
        List<String[]> result = new ArrayList<>();
        for (String line : loadRawNoticeLines()) {
            String[] parts = line.split("\\|", 4);
            if (parts.length == 4 && parts[0].equals(applicantId)) {
                result.add(new String[]{parts[1], parts[2], parts[3]});
            }
        }
        return result;
    }

    private static void syncCurrentStatusToInbox(Applicant currentUser) {
        List<ScholarshipApplication> apps = getApplicationsForApplicant(currentUser.getApplicantID());
        if (apps.isEmpty()) {
            return;
        }
        for (ScholarshipApplication sa : apps) {
            Disbursement disb = ScholarshipManager.findDisbursementByApplicationID(sa.getApplicationID());
            String appStat = (sa.getStatus() != null) ? sa.getStatus() : "Pending";

            if (disb != null && "Released".equalsIgnoreCase(disb.getPaymentStatus())) {
                appendDisbursementNotice(
                    currentUser.getApplicantID(),
                    "Cash Grant Released: " + String.format("P%.2f", disb.getAmount()),
                    String.format(
                        "Your grant of P%.2f has been released (Disbursement ID: %s, App ID: %s, Program: %s, Date: %s).",
                        disb.getAmount(), disb.getDisbursementID(), sa.getApplicationID(), sa.getScholarshipType(), disb.getDate()
                    )
                );
            } else if (disb != null) {
                appendDisbursementNotice(
                    currentUser.getApplicantID(),
                    "Cash Grant Update (" + disb.getPaymentStatus() + "): " + String.format("P%.2f", disb.getAmount()),
                    String.format(
                        "Your cash grant of P%.2f (Disbursement ID: %s, App ID: %s, Program: %s) is currently %s | Application Status: %s.",
                        disb.getAmount(), disb.getDisbursementID(), sa.getApplicationID(), sa.getScholarshipType(), disb.getPaymentStatus(), appStat
                    )
                );
            } else {
                appendDisbursementNotice(
                    currentUser.getApplicantID(),
                    "Application Status: " + appStat,
                    String.format(
                        "App ID: %s (%s) — Application Status: %s | Disbursement Status: Pending",
                        sa.getApplicationID(), sa.getScholarshipType(), appStat
                    )
                );
            }
        }
    }

    private static List<String[]> getAllScholarshipPrograms() {
        AdminPanel.loadCustomProgramsFromFile();
        List<String[]> all = new ArrayList<>();
        all.add(new String[]{
            "DOST Scholarship (Department of Science and Technology)",
            "STEM / IT track & DOST-SEI qualifying exam",
            "Tuition, living & book allowance"
        });
        all.add(new String[]{
            "Academic Scholarship",
            "Max GWA of " + ScholarshipManager.MAXIMUM_GWA + " or better",
            "Tuition coverage & academic grant"
        });
        all.add(new String[]{
            "Financial Assistance Scholarship",
            "Annual Income <= P" + String.format("%.2f", ScholarshipManager.MAXIMUM_ANNUAL_INCOME),
            "Financial stipend & fee subsidy"
        });
        all.add(new String[]{
            "Government Scholarship",
            "Valid documents & active enrollment",
            "Semesterly educational grant"
        });
        all.add(new String[]{
            "Science and Technology (S&T) Scholarship",
            "GWA of " + ScholarshipManager.MAXIMUM_GWA + " or better & complete docs",
            "Monthly allowance & study grant"
        });
        all.add(new String[]{
            "Cultural and Arts Achievement Grant",
            "Campus group endorsement & good standing",
            "Tuition fee discount"
        });
        all.addAll(AdminPanel.customPrograms);
        return all;
    }

    public static void createAndShowApplicantDashboard(Applicant currentUser) {
        syncCurrentStatusToInbox(currentUser);
        List<String[]> notices = getNoticesForApplicant(currentUser.getApplicantID());

        JFrame frame = new JFrame("Applicant Portal - " + currentUser.getName());
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

        JLabel brandLbl = new JLabel("Applicant Portal");
        brandLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        brandLbl.setForeground(Color.WHITE);

        JLabel userLbl = new JLabel(currentUser.getName() + " (" + currentUser.getApplicantID() + ")");
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

        JLabel welcomeLbl = new JLabel("Welcome, " + currentUser.getName());
        welcomeLbl.setFont(new Font("Segoe UI", Font.BOLD, 21));
        welcomeLbl.setForeground(Color.WHITE);

        JLabel subLbl = new JLabel("Select a scholarship program, submit requirements, and track your status.");
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLbl.setForeground(new Color(203, 213, 225));

        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.add(welcomeLbl);
        textCol.add(Box.createRigidArea(new Dimension(0, 4)));
        textCol.add(subLbl);
        banner.add(textCol, BorderLayout.CENTER);

        JLabel secTitle = new JLabel("Applicant Actions");
        secTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        secTitle.setForeground(AutomatedApplication.TEXT_DARK);
        secTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        String grantBadge = notices.isEmpty() ? "GRANT INBOX" : "GRANT INBOX (" + notices.size() + ")";
        String grantDesc = notices.isEmpty()
            ? "View your saved email-style disbursement notices, cash grant amount, and status updates."
            : "Latest Notice: " + notices.get(notices.size() - 1)[2];

        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsRow.setOpaque(false);
        cardsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardsRow.setPreferredSize(new Dimension(0, 210));
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        cardsRow.add(createActionCard(
            "PROGRAMS",
            new Color(224, 231, 255),
            AutomatedApplication.PRIMARY_ACCENT,
            "Select Scholarship Program",
            "Browse available scholarship programs, check requirements, and apply.",
            "Browse Programs  ->",
            AutomatedApplication.PRIMARY_ACCENT,
            e -> openScholarshipProgramsDialog(frame, currentUser)
        ));

        cardsRow.add(createActionCard(
            "TRACKING",
            new Color(220, 252, 231),
            new Color(21, 128, 61),
            "View My Application Status",
            "Track your real-time application status, document check, and disbursement.",
            "Check Status  ->",
            new Color(21, 128, 61),
            e -> openViewStatusDialog(frame, currentUser)
        ));

        cardsRow.add(createActionCard(
            grantBadge,
            new Color(254, 243, 199),
            new Color(180, 83, 9),
            "Cash Disbursement Grant",
            grantDesc,
            "Open Inbox  ->",
            new Color(180, 83, 9),
            e -> openCashDisbursementGrantDialog(frame, currentUser)
        ));

        body.add(banner);
        body.add(Box.createVerticalGlue());
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

        JLabel footLbl = new JLabel("Automated Scholarship Application System  •  Applicant Portal");
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

        if (!notices.isEmpty()) {
            String[] latest = notices.get(notices.size() - 1);
            JOptionPane.showMessageDialog(
                frame,
                "Disbursement Notice (" + latest[0] + ")\n" + latest[1] + "\n" + latest[2],
                "Disbursement Notice Inbox",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
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

    public static void openCashDisbursementGrantDialog(JFrame parent, Applicant currentUser) {
        syncCurrentStatusToInbox(currentUser);
        List<String[]> notices = getNoticesForApplicant(currentUser.getApplicantID());

        List<ScholarshipApplication> apps = getApplicationsForApplicant(currentUser.getApplicantID());
        ScholarshipApplication sa = apps.isEmpty() ? null : apps.get(apps.size() - 1);
        Disbursement disb = (sa != null) ? ScholarshipManager.findDisbursementByApplicationID(sa.getApplicationID()) : null;

        JDialog dialog = new JDialog(parent, "Cash Disbursement Grant — Notice Inbox", true);
        dialog.setSize(860, 480);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel summaryCard = AutomatedApplication.createRoundedPanel(Color.WHITE, new Color(199, 210, 254), 14);
        summaryCard.setLayout(new BoxLayout(summaryCard, BoxLayout.Y_AXIS));
        summaryCard.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel titleLbl = new JLabel("Cash Disbursement Grant & Saved Notices");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(AutomatedApplication.PRIMARY_DARK);

        String programText = (sa != null) ? sa.getScholarshipType() : "No application submitted yet";
        String appStatusText = (sa != null && sa.getStatus() != null) ? sa.getStatus() : "N/A";
        String disbIdText = (disb != null) ? disb.getDisbursementID() : "N/A";
        String grantAmountText = (disb != null) ? String.format("P%.2f", disb.getAmount()) : "P0.00";
        String grantStatusText = (disb != null) ? disb.getPaymentStatus() : "Pending";
        String grantDateText = (disb != null && disb.getDate() != null) ? disb.getDate().toString() : "N/A";

        String headlineNotice = (disb != null && "Released".equalsIgnoreCase(grantStatusText))
            ? "Your grant of " + grantAmountText + " has been released."
            : "Program: " + programText + "  |  App Status: " + appStatusText + "  |  Cash Grant: " + grantAmountText + " (" + grantStatusText + ")";

        JLabel lblSummary = new JLabel(headlineNotice);
        lblSummary.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSummary.setForeground(AutomatedApplication.PRIMARY_ACCENT);

        JLabel lblMeta = new JLabel(
            "Program: " + programText + "  |  App Status: " + appStatusText +
            "  |  Disbursement ID: " + disbIdText + "  |  Amount: " + grantAmountText +
            "  |  Disbursement: " + grantStatusText + " (" + grantDateText + ")"
        );
        lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMeta.setForeground(AutomatedApplication.TEXT_DARK);

        summaryCard.add(titleLbl);
        summaryCard.add(Box.createRigidArea(new Dimension(0, 6)));
        summaryCard.add(lblSummary);
        summaryCard.add(Box.createRigidArea(new Dimension(0, 4)));
        summaryCard.add(lblMeta);

        String[] cols = {"Date & Time", "Subject", "Notice Details"};
        DefaultTableModel inboxModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = notices.size() - 1; i >= 0; i--) {
            String[] n = notices.get(i);
            inboxModel.addRow(new Object[]{n[0], n[1], n[2]});
        }

        JTable inboxTable = new JTable(inboxModel);
        inboxTable.setRowHeight(28);
        inboxTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        inboxTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        inboxTable.getColumnModel().getColumn(0).setPreferredWidth(130);
        inboxTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        inboxTable.getColumnModel().getColumn(2).setPreferredWidth(450);

        JTextArea messagePreview = new JTextArea(4, 40);
        messagePreview.setEditable(false);
        messagePreview.setLineWrap(true);
        messagePreview.setWrapStyleWord(true);
        messagePreview.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messagePreview.setBorder(new EmptyBorder(8, 10, 8, 10));
        if (!notices.isEmpty()) {
            String[] latest = notices.get(notices.size() - 1);
            messagePreview.setText("Date: " + latest[0] + "\nSubject: " + latest[1] + "\n\n" + latest[2]);
            inboxTable.setRowSelectionInterval(0, 0);
        } else {
            messagePreview.setText("No saved notices in your inbox yet.");
        }

        inboxTable.getSelectionModel().addListSelectionListener(e -> {
            int row = inboxTable.getSelectedRow();
            if (row >= 0) {
                String dt = String.valueOf(inboxModel.getValueAt(row, 0));
                String subj = String.valueOf(inboxModel.getValueAt(row, 1));
                String det = String.valueOf(inboxModel.getValueAt(row, 2));
                messagePreview.setText("Date: " + dt + "\nSubject: " + subj + "\n\n" + det);
            }
        });

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(inboxTable),
            new JScrollPane(messagePreview)
        );
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);

        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(summaryCard, BorderLayout.NORTH);
        body.add(splitPane, BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void openScholarshipProgramsDialog(JFrame parent, Applicant currentUser) {
        JDialog dialog = new JDialog(parent, "Scholarship Programs", true);
        dialog.setSize(860, 650);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        List<String[]> programs = getAllScholarshipPrograms();

        JPanel topCard = AutomatedApplication.createRoundedPanel(
            Color.WHITE, new Color(199, 210, 254), 14
        );
        topCard.setLayout(new BorderLayout(12, 0));
        topCard.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel titleLbl = new JLabel("Available Scholarship Programs");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(AutomatedApplication.TEXT_DARK);

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBox.setOpaque(false);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(AutomatedApplication.TEXT_DARK);

        JTextField txtSearch = new JTextField(22);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(220, 32));

        searchBox.add(lblSearch);
        searchBox.add(txtSearch);

        topCard.add(titleLbl, BorderLayout.WEST);
        topCard.add(searchBox, BorderLayout.EAST);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(AutomatedApplication.BG_SOFT);
        listContainer.setBorder(new EmptyBorder(4, 4, 4, 4));

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(AutomatedApplication.BG_SOFT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        Runnable rebuildCards = () -> {
            String keyword = txtSearch.getText().trim().toLowerCase();
            listContainer.removeAll();

            int shown = 0;
            for (String[] prog : programs) {
                String combined = (prog[0] + " " + prog[1] + " " + prog[2]).toLowerCase();
                if (keyword.isEmpty() || combined.contains(keyword)) {
                    shown++;
                    JPanel itemCard = AutomatedApplication.createRoundedPanel(
                        Color.WHITE, new Color(199, 210, 254), 14
                    );
                    itemCard.setLayout(new BorderLayout(14, 0));
                    itemCard.setBorder(new EmptyBorder(14, 18, 14, 18));
                    itemCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

                    JPanel infoCol = new JPanel();
                    infoCol.setOpaque(false);
                    infoCol.setLayout(new BoxLayout(infoCol, BoxLayout.Y_AXIS));

                    JLabel nameLbl = new JLabel(prog[0]);
                    nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    nameLbl.setForeground(AutomatedApplication.PRIMARY_DARK);

                    JLabel eligLbl = new JLabel("Eligibility: " + prog[1]);
                    eligLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    eligLbl.setForeground(AutomatedApplication.TEXT_MUTED);

                    JLabel benLbl = new JLabel("Benefits: " + prog[2]);
                    benLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    benLbl.setForeground(AutomatedApplication.PRIMARY_ACCENT);

                    infoCol.add(nameLbl);
                    infoCol.add(Box.createRigidArea(new Dimension(0, 4)));
                    infoCol.add(eligLbl);
                    infoCol.add(Box.createRigidArea(new Dimension(0, 2)));
                    infoCol.add(benLbl);

                    JButton applyBtn = AutomatedApplication.createActionButton(
                        "Apply Program", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 130, 34
                    );
                    applyBtn.addActionListener(ev -> {
                        dialog.dispose();
                        openSubmitApplicationDialog(parent, currentUser, prog[0]);
                    });

                    JPanel btnWrap = new JPanel(new GridBagLayout());
                    btnWrap.setOpaque(false);
                    btnWrap.add(applyBtn);

                    itemCard.add(infoCol, BorderLayout.CENTER);
                    itemCard.add(btnWrap, BorderLayout.EAST);

                    listContainer.add(itemCard);
                    listContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            if (shown == 0) {
                JLabel emptyLbl = new JLabel("No matching scholarship program found.");
                emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                emptyLbl.setForeground(AutomatedApplication.TEXT_MUTED);
                emptyLbl.setBorder(new EmptyBorder(20, 10, 20, 10));
                listContainer.add(emptyLbl);
            }

            listContainer.revalidate();
            listContainer.repaint();
        };

        rebuildCards.run();

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                rebuildCards.run();
            }
        });

        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.BTN_RED, Color.WHITE, 130, 34
        );
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(topCard, BorderLayout.NORTH);
        body.add(scrollPane, BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public static void openSubmitApplicationDialog(JFrame parent, Applicant currentUser) {
        openSubmitApplicationDialog(parent, currentUser, null);
    }

    public static void openSubmitApplicationDialog(JFrame parent, Applicant currentUser, String preselectedProgram) {
        JDialog dialog = new JDialog(parent, "Submit Application", true);
        dialog.setSize(700, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);

        JPanel card = AutomatedApplication.createRoundedPanel(Color.WHITE, new Color(199, 210, 254), 16);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(18, 24, 18, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;

        JTextField txtStudentId = new JTextField(currentUser.getApplicantID());
        txtStudentId.setEditable(false);
        txtStudentId.setPreferredSize(new Dimension(0, 32));

        JTextField txtName = new JTextField(currentUser.getName());
        txtName.setEditable(false);
        txtName.setPreferredSize(new Dimension(0, 32));

        List<String[]> allPrograms = getAllScholarshipPrograms();
        String[] programNames = new String[allPrograms.size()];
        for (int i = 0; i < allPrograms.size(); i++) {
            programNames[i] = allPrograms.get(i)[0];
        }

        JComboBox<String> cmbProgram = new JComboBox<>(programNames);
        cmbProgram.setPreferredSize(new Dimension(0, 32));

        JComboBox<String> cmbDocType = new JComboBox<>(new String[]{
            "Academic Record",
            "Income Certificate",
            "Identification Document"
        });
        cmbDocType.setPreferredSize(new Dimension(0, 32));

        JTextField txtGwa = new JTextField();
        txtGwa.setPreferredSize(new Dimension(0, 32));

        JTextField txtIncome = new JTextField();
        txtIncome.setPreferredSize(new Dimension(0, 32));

        JTextField txtDocName = new JTextField();
        txtDocName.setEditable(false);
        txtDocName.setPreferredSize(new Dimension(0, 32));

        final List<String> selectedFilePaths = new ArrayList<>();
        final boolean[] submittedSuccessfully = {false};
        final boolean[] exitHandled = {false};

        String initialProgram = (preselectedProgram != null && !preselectedProgram.isEmpty())
            ? preselectedProgram
            : (programNames.length > 0 ? programNames[0] : "");
        if (!initialProgram.isEmpty()) {
            cmbProgram.setSelectedItem(initialProgram);
        }

        String[] savedDraft = getDraftForApplicant(currentUser.getApplicantID(), (String) cmbProgram.getSelectedItem());
        if (savedDraft != null) {
            if (savedDraft.length > 1 && !savedDraft[1].isEmpty()) {
                cmbDocType.setSelectedItem(savedDraft[1]);
            }
            if (savedDraft.length > 2) {
                txtGwa.setText(savedDraft[2]);
            }
            if (savedDraft.length > 3) {
                txtIncome.setText(savedDraft[3]);
            }
            if (savedDraft.length > 4 && !savedDraft[4].isEmpty()) {
                selectedFilePaths.addAll(splitFilePaths(savedDraft[4]));
                txtDocName.setText(formatFileNamesDisplay(selectedFilePaths));
            }
        }

        Runnable persistCurrentDraft = () -> saveDraftForApplicant(
            currentUser.getApplicantID(),
            (String) cmbProgram.getSelectedItem(),
            (String) cmbDocType.getSelectedItem(),
            txtGwa.getText(),
            txtIncome.getText(),
            joinFilePaths(selectedFilePaths)
        );

        Runnable handleIncompleteClose = () -> {
            if (exitHandled[0]) {
                return;
            }
            exitHandled[0] = true;
            persistCurrentDraft.run();
            if (!submittedSuccessfully[0]) {
                boolean hasAnyInput = !txtGwa.getText().trim().isEmpty()
                    || !txtIncome.getText().trim().isEmpty()
                    || !selectedFilePaths.isEmpty();
                boolean isIncomplete = txtGwa.getText().trim().isEmpty()
                    || txtIncome.getText().trim().isEmpty()
                    || selectedFilePaths.isEmpty();
                if (isIncomplete) {
                    String msg = hasAnyInput
                        ? "Your input is not yet complete. Your progress has been saved as a draft so you can continue later."
                        : "Your input is not yet complete. No application has been submitted.";
                    JOptionPane.showMessageDialog(
                        dialog,
                        msg,
                        "Incomplete Input",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
            dialog.dispose();
        };

        cmbProgram.addActionListener(e -> {
            persistCurrentDraft.run();
            String[] progDraft = getDraftForApplicant(currentUser.getApplicantID(), (String) cmbProgram.getSelectedItem());
            if (progDraft != null) {
                if (progDraft.length > 1 && !progDraft[1].isEmpty()) {
                    cmbDocType.setSelectedItem(progDraft[1]);
                }
                txtGwa.setText(progDraft.length > 2 ? progDraft[2] : "");
                txtIncome.setText(progDraft.length > 3 ? progDraft[3] : "");
                selectedFilePaths.clear();
                if (progDraft.length > 4 && !progDraft[4].isEmpty()) {
                    selectedFilePaths.addAll(splitFilePaths(progDraft[4]));
                }
                txtDocName.setText(formatFileNamesDisplay(selectedFilePaths));
            }
        });
        cmbDocType.addActionListener(e -> persistCurrentDraft.run());

        KeyAdapter autoSaveKeyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                persistCurrentDraft.run();
            }
        };
        txtGwa.addKeyListener(autoSaveKeyAdapter);
        txtIncome.addKeyListener(autoSaveKeyAdapter);

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleIncompleteClose.run();
            }
        });

        JButton browseBtn = AutomatedApplication.createRoundedButton(
            "Upload Files", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 8
        );
        browseBtn.setPreferredSize(new Dimension(115, 32));

        JPanel fileUploadPanel = new JPanel(new BorderLayout(6, 0));
        fileUploadPanel.setOpaque(false);
        fileUploadPanel.add(txtDocName, BorderLayout.CENTER);
        fileUploadPanel.add(browseBtn, BorderLayout.EAST);

        JButton saveDraftBtn = AutomatedApplication.createRoundedButton(
            "Save Draft", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 10
        );
        saveDraftBtn.setPreferredSize(new Dimension(0, 40));
        saveDraftBtn.addActionListener(e -> handleIncompleteClose.run());

        JButton submitBtn = AutomatedApplication.createRoundedButton(
            "Submit Application", AutomatedApplication.BTN_GREEN, Color.WHITE, 10
        );
        submitBtn.setPreferredSize(new Dimension(0, 40));

        JPanel actionBtnsRow = new JPanel(new GridLayout(1, 2, 10, 0));
        actionBtnsRow.setOpaque(false);
        actionBtnsRow.add(saveDraftBtn);
        actionBtnsRow.add(submitBtn);

        gbc.insets = new Insets(4, 0, 2, 10);
        gbc.gridx = 0; gbc.gridy = 0; card.add(new JLabel("Applicant ID:"), gbc);
        gbc.insets = new Insets(4, 10, 2, 0);
        gbc.gridx = 1; gbc.gridy = 0; card.add(new JLabel("Full Name:"), gbc);

        gbc.insets = new Insets(0, 0, 8, 10);
        gbc.gridx = 0; gbc.gridy = 1; card.add(txtStudentId, gbc);
        gbc.insets = new Insets(0, 10, 8, 0);
        gbc.gridx = 1; gbc.gridy = 1; card.add(txtName, gbc);

        gbc.insets = new Insets(4, 0, 2, 10);
        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("Scholarship Program:"), gbc);
        gbc.insets = new Insets(4, 10, 2, 0);
        gbc.gridx = 1; gbc.gridy = 2; card.add(new JLabel("Document Category:"), gbc);

        gbc.insets = new Insets(0, 0, 8, 10);
        gbc.gridx = 0; gbc.gridy = 3; card.add(cmbProgram, gbc);
        gbc.insets = new Insets(0, 10, 8, 0);
        gbc.gridx = 1; gbc.gridy = 3; card.add(cmbDocType, gbc);

        gbc.insets = new Insets(4, 0, 2, 10);
        gbc.gridx = 0; gbc.gridy = 4; card.add(new JLabel("GWA (1.00 - 3.00):"), gbc);
        gbc.insets = new Insets(4, 10, 2, 0);
        gbc.gridx = 1; gbc.gridy = 4; card.add(new JLabel("Annual Income:"), gbc);

        gbc.insets = new Insets(0, 0, 8, 10);
        gbc.gridx = 0; gbc.gridy = 5; card.add(txtGwa, gbc);
        gbc.insets = new Insets(0, 10, 8, 0);
        gbc.gridx = 1; gbc.gridy = 5; card.add(txtIncome, gbc);

        gbc.insets = new Insets(4, 0, 2, 0);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        card.add(new JLabel("Upload Documents (.pdf/.jpg/.png):"), gbc);

        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        card.add(fileUploadPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(6, 0, 0, 0);
        card.add(actionBtnsRow, gbc);

        GridBagConstraints outer = new GridBagConstraints();
        outer.fill = GridBagConstraints.BOTH;
        outer.weightx = 1.0;
        outer.weighty = 1.0;
        outer.insets = new Insets(14, 14, 14, 14);
        dialog.add(card, outer);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Select Document File(s)");
            chooser.setMultiSelectionEnabled(true);
            int result = chooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] chosenFiles = chooser.getSelectedFiles();
                if ((chosenFiles == null || chosenFiles.length == 0) && chooser.getSelectedFile() != null) {
                    chosenFiles = new File[]{chooser.getSelectedFile()};
                }
                if (chosenFiles != null && chosenFiles.length > 0) {
                    for (File f : chosenFiles) {
                        if (f != null && !selectedFilePaths.contains(f.getAbsolutePath())) {
                            selectedFilePaths.add(f.getAbsolutePath());
                        }
                    }
                    txtDocName.setText(formatFileNamesDisplay(selectedFilePaths));
                    persistCurrentDraft.run();
                }
            }
        });

        submitBtn.addActionListener(e -> {
            persistCurrentDraft.run();

            String studentId = txtStudentId.getText().trim();
            String name = txtName.getText().trim();
            String program = (String) cmbProgram.getSelectedItem();
            String docType = (String) cmbDocType.getSelectedItem();
            String gwaRaw = txtGwa.getText().trim();
            String incomeRaw = txtIncome.getText().trim();
            String docRaw = txtDocName.getText().trim();

            if (studentId.isEmpty() || name.isEmpty() || program == null ||
                gwaRaw.isEmpty() || incomeRaw.isEmpty() || (docRaw.isEmpty() && selectedFilePaths.isEmpty())) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "Your input is not yet complete. Your progress has been saved as a draft.",
                    "Incomplete Input",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            double gwa, income;
            try {
                gwa = Double.parseDouble(gwaRaw);
                if (gwa < 1.0 || gwa > 5.0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid GWA. Must be between 1.00 and 5.00.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                income = Double.parseDouble(incomeRaw);
                if (income < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid annual income.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setGwa(gwa);

            String combinedFileNames = !selectedFilePaths.isEmpty()
                ? formatFileNamesDisplay(selectedFilePaths)
                : docRaw;

            LocalDate defaultExpiry = LocalDate.now().plusYears(1);
            String docId = "DOC-" + (ScholarshipManager.applicationList.size() + 101);
            GovernmentDocument doc;
            if ("Academic Record".equals(docType)) {
                doc = new GovernmentDocument.AcademicRecord(docId, combinedFileNames, LocalDate.now(), defaultExpiry, gwa);
            } else if ("Income Certificate".equals(docType)) {
                doc = new GovernmentDocument.IncomeCertificate(docId, combinedFileNames, LocalDate.now(), defaultExpiry, income);
            } else {
                doc = new GovernmentDocument.IdentificationDocument(docId, combinedFileNames, LocalDate.now(), defaultExpiry);
            }

            String appId = "APP-" + (ScholarshipManager.applicationList.size() + 101);
            if (!selectedFilePaths.isEmpty()) {
                uploadedFilePathsByAppId.put(appId, joinFilePaths(selectedFilePaths));
            }

            ScholarshipApplication sa = new ScholarshipApplication(
                appId, studentId, name, program, gwa, income, doc
            );
            sa.submitApplication();

            ScholarshipManager.applicationList.add(sa);
            ScholarshipManager.saveAllData();
            clearDraftForApplicant(studentId, program);
            submittedSuccessfully[0] = true;

            appendDisbursementNotice(
                studentId,
                "Application Submitted: " + sa.getStatus(),
                "App ID: " + appId + " (" + program + ") submitted. Initial Status: " + sa.getStatus() + " | Disbursement: Pending"
            );

            JOptionPane.showMessageDialog(
                dialog,
                "Application processed and saved!\nStatus: " + sa.getStatus(),
                "Submission Result",
                JOptionPane.INFORMATION_MESSAGE
            );
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    private static List<String> collectCandidateFiles(ScholarshipApplication sa) {
        Map<String, String> entries = new LinkedHashMap<>();
        if (sa == null) {
            return new ArrayList<>();
        }

        String savedRaw = uploadedFilePathsByAppId.get(sa.getApplicationID());
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
            chooser.setDialogTitle("Locate Uploaded File: " + fileToOpen.getName());
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
            JOptionPane.showMessageDialog(parent, "No uploaded files found.", "File Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog fileDialog = new JDialog(
            SwingUtilities.getWindowAncestor(parent),
            "Uploaded Files — " + sa.getApplicationID(),
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

    public static void openViewStatusDialog(JFrame parent, Applicant currentUser) {
        List<ScholarshipApplication> apps = getApplicationsForApplicant(currentUser.getApplicantID());
        if (apps.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No application found for your Applicant ID yet.", "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(parent, "My Application Status", true);
        dialog.setSize(980, 360);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        String[] cols = {
            "App ID", "Program", "GWA", "Annual Income",
            "Uploaded Files", "App Status", "Disbursement"
        };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (ScholarshipApplication sa : apps) {
            Disbursement disb = ScholarshipManager.findDisbursementByApplicationID(sa.getApplicationID());
            String disbStatus = (disb != null) ? disb.getPaymentStatus() : "Not Yet Approved";
            String disbAmount = (disb != null) ? String.format("P%.2f", disb.getAmount()) : "N/A";
            String uploadedFile = (sa.getDocument() != null && sa.getDocument().getFileName() != null)
                ? sa.getDocument().getFileName()
                : "None";

            model.addRow(new Object[]{
                sa.getApplicationID(),
                sa.getScholarshipType(),
                String.format("%.2f", sa.getGwa()),
                String.format("P%.2f", sa.getAnnualIncome()),
                uploadedFile,
                sa.getStatus(),
                disbStatus + " (" + disbAmount + ")"
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        if (!apps.isEmpty()) {
            table.setRowSelectionInterval(0, 0);
        }

        JButton viewFilesBtn = AutomatedApplication.createActionButton(
            "View Files", AutomatedApplication.PRIMARY_ACCENT, Color.WHITE, 125, 34
        );
        viewFilesBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            ScholarshipApplication target = (sel >= 0 && sel < apps.size()) ? apps.get(sel) : apps.get(0);
            openUploadedFilesChooserDialog(dialog, target);
        });

        JButton closeBtn = AutomatedApplication.createActionButton(
            "Close Window", AutomatedApplication.PRIMARY_DARK, Color.WHITE, 130, 34
        );
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(viewFilesBtn);
        bottomPanel.add(closeBtn);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(body, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}