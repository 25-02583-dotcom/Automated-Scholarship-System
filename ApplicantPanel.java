import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ApplicantPanel {
    public static void createAndShowApplicantDashboard(Applicant currentUser) {
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
        body.setBorder(new EmptyBorder(18, 26, 18, 26));

        JPanel banner = AutomatedApplication.createRoundedPanel(AutomatedApplication.PRIMARY_DARK, null, 18);
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(20, 24, 20, 24));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 108));

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

        JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 16, 16));
        cardsGrid.setOpaque(false);
        cardsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));

        cardsGrid.add(createActionCard(
            "Select Scholarship Program",
            "Browse any available programs.",
            "Continue >",
            e -> openScholarshipProgramsDialog(frame, currentUser)
        ));

        cardsGrid.add(createActionCard(
            "Submit Application",
            "Enter student details, upload document metadata, validate requirements, and assess eligibility.",
            "Continue >",
            e -> openSubmitApplicationDialog(frame, currentUser)
        ));

        cardsGrid.add(createActionCard(
            "View My Application Status",
            "Track your real-time application status and disbursement progress.",
            "Continue >",
            e -> openViewStatusDialog(frame, currentUser)
        ));

        cardsGrid.add(createActionCard(
            "Logout",
            "Save changes and return to the login screen.",
            "Logout >",
            e -> {
                ScholarshipManager.saveAllData();
                frame.dispose();
                LoginPanel.createAndShowLoginWindow();
            }
        ));

        body.add(banner);
        body.add(Box.createRigidArea(new Dimension(0, 16)));
        body.add(secTitle);
        body.add(Box.createRigidArea(new Dimension(0, 10)));
        body.add(cardsGrid);
        frame.add(body, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(AutomatedApplication.PRIMARY_DARK);
        footer.setPreferredSize(new Dimension(0, 38));
        footer.setBorder(new EmptyBorder(0, 26, 0, 26));
        JLabel footLbl = new JLabel("Automated Scholarship Application System  •  Applicant Portal");
        footLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footLbl.setForeground(new Color(203, 213, 225));
        footer.add(footLbl, BorderLayout.WEST);
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JPanel createActionCard(String title, String desc, String linkText, java.awt.event.ActionListener action) {
        JPanel card = AutomatedApplication.createRoundedPanel(
            AutomatedApplication.CARD_TINT, new Color(165, 180, 252), 16
        );
        card.setLayout(new BorderLayout(0, 6));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel topText = new JPanel();
        topText.setLayout(new BoxLayout(topText, BoxLayout.Y_AXIS));
        topText.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(AutomatedApplication.TEXT_DARK);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(AutomatedApplication.TEXT_MUTED);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        topText.add(lblTitle);
        topText.add(Box.createRigidArea(new Dimension(0, 4)));
        topText.add(lblDesc);

        JButton linkBtn = new JButton(linkText);
        linkBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        linkBtn.setForeground(AutomatedApplication.PRIMARY_ACCENT);
        linkBtn.setBorderPainted(false);
        linkBtn.setContentAreaFilled(false);
        linkBtn.setFocusPainted(false);
        linkBtn.setMargin(new Insets(0, 0, 0, 0));
        linkBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkBtn.setHorizontalAlignment(SwingConstants.LEFT);
        linkBtn.addActionListener(action);

        card.add(topText, BorderLayout.NORTH);
        card.add(linkBtn, BorderLayout.SOUTH);
        return card;
    }

    public static void openScholarshipProgramsDialog(JFrame parent, Applicant currentUser) {
        JDialog dialog = new JDialog(parent, "Scholarship Programs", true);
        dialog.setSize(860, 650);
        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(AutomatedApplication.BG_SOFT);
        dialog.setLayout(new BorderLayout(10, 10));

        String[][] programs = {
            {
                "DOST Scholarship (Department of Science and Technology)",
                "STEM / IT track & DOST-SEI qualifying exam",
                "Tuition, living & book allowance"
            },
            {
                "Academic Scholarship",
                "Max GWA of " + ScholarshipManager.MAXIMUM_GWA + " or better",
                "Tuition coverage & academic grant"
            },
            {
                "Financial Assistance Scholarship",
                "Annual Income <= P" + String.format("%.2f", ScholarshipManager.MAXIMUM_ANNUAL_INCOME),
                "Financial stipend & fee subsidy"
            },
            {
                "Government Scholarship",
                "Valid documents & active enrollment",
                "Semesterly educational grant"
            },
            {
                "Science and Technology (S&T) Scholarship",
                "GWA of " + ScholarshipManager.MAXIMUM_GWA + " or better & complete docs",
                "Monthly allowance & study grant"
            },
            {
                "Cultural and Arts Achievement Grant",
                "Campus group endorsement & good standing",
                "Tuition fee discount"
            }
        };

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
        dialog.setSize(700, 440);
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

        JComboBox<String> cmbProgram = new JComboBox<>(new String[]{
            "DOST Scholarship (Department of Science and Technology)",
            "Academic Scholarship",
            "Financial Assistance Scholarship",
            "Government Scholarship",
            "Science and Technology (S&T) Scholarship",
            "Cultural and Arts Achievement Grant"
        });
        cmbProgram.setPreferredSize(new Dimension(0, 32));
        if (preselectedProgram != null) {
            cmbProgram.setSelectedItem(preselectedProgram);
        }

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
        txtDocName.setPreferredSize(new Dimension(0, 32));

        JTextField txtExpiry = new JTextField();
        txtExpiry.setPreferredSize(new Dimension(0, 32));

        JButton submitBtn = AutomatedApplication.createRoundedButton(
            "Submit Application", AutomatedApplication.BTN_GREEN, Color.WHITE, 10
        );
        submitBtn.setPreferredSize(new Dimension(0, 40));

        // Row 0 & 1: Applicant ID | Full Name
        gbc.insets = new Insets(4, 0, 2, 10);
        gbc.gridx = 0; gbc.gridy = 0; card.add(new JLabel("Applicant ID:"), gbc);
        gbc.insets = new Insets(4, 10, 2, 0);
        gbc.gridx = 1; gbc.gridy = 0; card.add(new JLabel("Full Name:"), gbc);

        gbc.insets = new Insets(0, 0, 8, 10);
        gbc.gridx = 0; gbc.gridy = 1; card.add(txtStudentId, gbc);
        gbc.insets = new Insets(0, 10, 8, 0);
        gbc.gridx = 1; gbc.gridy = 1; card.add(txtName, gbc);

        // Row 2 & 3: Scholarship Program | Document Category
        gbc.insets = new Insets(4, 0, 2, 10);
        gbc.gridx = 0; gbc.gridy = 2; card.add(new JLabel("Scholarship Program:"), gbc);
        gbc.insets = new Insets(4, 10, 2, 0);
        gbc.gridx = 1; gbc.gridy = 2; card.add(new JLabel("Document Category:"), gbc);

        gbc.insets = new Insets(0, 0, 8, 10);
        gbc.gridx = 0; gbc.gridy = 3; card.add(cmbProgram, gbc);
        gbc.insets = new Insets(0, 10, 8, 0);
        gbc.gridx = 1; gbc.gridy = 3; card.add(cmbDocType, gbc);

        // Row 4 & 5: GWA | Annual Income
        gbc.insets = new Insets(4, 0, 2, 10);
        gbc.gridx = 0; gbc.gridy = 4; card.add(new JLabel("GWA (1.00 - 3.00):"), gbc);
        gbc.insets = new Insets(4, 10, 2, 0);
        gbc.gridx = 1; gbc.gridy = 4; card.add(new JLabel("Annual Income:"), gbc);

        gbc.insets = new Insets(0, 0, 8, 10);
        gbc.gridx = 0; gbc.gridy = 5; card.add(txtGwa, gbc);
        gbc.insets = new Insets(0, 10, 8, 0);
        gbc.gridx = 1; gbc.gridy = 5; card.add(txtIncome, gbc);

        // Row 6 & 7: Document File | Expiry Date
        gbc.insets = new Insets(4, 0, 2, 10);
        gbc.gridx = 0; gbc.gridy = 6; card.add(new JLabel("Document File (.pdf/.jpg/.png):"), gbc);
        gbc.insets = new Insets(4, 10, 2, 0);
        gbc.gridx = 1; gbc.gridy = 6; card.add(new JLabel("Expiry Date (YYYY-MM-DD):"), gbc);

        gbc.insets = new Insets(0, 0, 12, 10);
        gbc.gridx = 0; gbc.gridy = 7; card.add(txtDocName, gbc);
        gbc.insets = new Insets(0, 10, 12, 0);
        gbc.gridx = 1; gbc.gridy = 7; card.add(txtExpiry, gbc);

        // Row 8: Full-width Submit Button
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(6, 0, 0, 0);
        card.add(submitBtn, gbc);

        GridBagConstraints outer = new GridBagConstraints();
        outer.fill = GridBagConstraints.BOTH;
        outer.weightx = 1.0;
        outer.weighty = 1.0;
        outer.insets = new Insets(14, 14, 14, 14);
        dialog.add(card, outer);

        submitBtn.addActionListener(e -> {
            String studentId = txtStudentId.getText().trim();
            String name = txtName.getText().trim();
            String program = (String) cmbProgram.getSelectedItem();
            String docType = (String) cmbDocType.getSelectedItem();
            String gwaRaw = txtGwa.getText().trim();
            String incomeRaw = txtIncome.getText().trim();
            String docRaw = txtDocName.getText().trim();
            String expiryRaw = txtExpiry.getText().trim();

            if (studentId.isEmpty() || name.isEmpty() || program == null ||
                gwaRaw.isEmpty() || incomeRaw.isEmpty() || docRaw.isEmpty() || expiryRaw.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Missing information. Please complete all required fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (ScholarshipManager.findApplicationByApplicantID(studentId) != null) {
                JOptionPane.showMessageDialog(dialog, "Duplicate application found for Applicant ID: " + studentId, "Duplicate Flagged", JOptionPane.WARNING_MESSAGE);
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

            LocalDate expiryDate;
            try {
                expiryDate = LocalDate.parse(expiryRaw);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setGwa(gwa);

            String docId = "DOC-" + (ScholarshipManager.applicationList.size() + 101);
            GovernmentDocument doc;
            if ("Academic Record".equals(docType)) {
                doc = new GovernmentDocument.AcademicRecord(docId, docRaw, LocalDate.now(), expiryDate, gwa);
            } else if ("Income Certificate".equals(docType)) {
                doc = new GovernmentDocument.IncomeCertificate(docId, docRaw, LocalDate.now(), expiryDate, income);
            } else {
                doc = new GovernmentDocument.IdentificationDocument(docId, docRaw, LocalDate.now(), expiryDate);
            }

            String appId = "APP-" + (ScholarshipManager.applicationList.size() + 101);
            ScholarshipApplication sa = new ScholarshipApplication(
                appId, studentId, name, program, gwa, income, doc
            );
            sa.submitApplication();

            ScholarshipManager.applicationList.add(sa);
            ScholarshipManager.saveAllData();

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

    public static void openViewStatusDialog(JFrame parent, Applicant currentUser) {
        ScholarshipApplication sa = ScholarshipManager.findApplicationByApplicantID(currentUser.getApplicantID());
        if (sa == null) {
            JOptionPane.showMessageDialog(parent, "No application found for your Applicant ID yet.", "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Disbursement disb = ScholarshipManager.findDisbursementByApplicationID(sa.getApplicationID());
        String disbStatus = (disb != null) ? disb.getPaymentStatus() : "Not Yet Approved";
        String disbAmount = (disb != null) ? String.format("P%.2f", disb.getAmount()) : "N/A";

        String details = String.format(
            "<html><div style='font-family: Segoe UI; padding: 6px;'>" +
            "<b>Application ID:</b> %s<br>" +
            "<b>Applicant ID:</b> %s<br>" +
            "<b>Name:</b> %s<br>" +
            "<b>Program:</b> %s<br>" +
            "<b>GWA:</b> %.2f<br>" +
            "<b>Annual Income:</b> P%.2f<br>" +
            "<b>Document:</b> %s (%s)<br>" +
            "<b>Application Status:</b> <font color='#4338ca'>%s</font><br>" +
            "<b>Disbursement Status:</b> <font color='#15803d'>%s (%s)</font></div></html>",
            sa.getApplicationID(), sa.getApplicantID(), sa.getApplicantName(), sa.getScholarshipType(),
            sa.getGwa(), sa.getAnnualIncome(), sa.getDocument().getFileName(),
            sa.getDocument().getVerificationStatus(), sa.getStatus(), disbStatus, disbAmount
        );

        JOptionPane.showMessageDialog(parent, new JLabel(details), "Application Status", JOptionPane.INFORMATION_MESSAGE);
    }
}