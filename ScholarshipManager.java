import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ScholarshipManager {
    public static ArrayList<Applicant> applicantList = new ArrayList<>();
    public static ArrayList<ScholarshipApplication> applicationList = new ArrayList<>();
    public static ArrayList<Disbursement> disbursementList = new ArrayList<>();

    private static final String APPLICANTS_FILE = "applicants_data.dat";
    private static final String APPLICATIONS_FILE = "applications_data.dat";
    private static final String DISBURSEMENTS_FILE = "disbursements_data.dat";

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "admin123";

    public static final double MAXIMUM_GWA = 3.00;
    public static final double MAXIMUM_ANNUAL_INCOME = 250000.0;
    public static final double DEFAULT_STIPEND_AMOUNT = 15000.0;

    public static void initializeSystem() {
        loadAllData();
    }

    @SuppressWarnings("unchecked")
    public static void loadAllData() {
        File f1 = new File(APPLICANTS_FILE);
        if (f1.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f1))) {
                applicantList = (ArrayList<Applicant>) ois.readObject();
            } catch (Exception e) {
                applicantList = new ArrayList<>();
            }
        }

        File f2 = new File(APPLICATIONS_FILE);
        if (f2.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f2))) {
                applicationList = (ArrayList<ScholarshipApplication>) ois.readObject();
            } catch (Exception e) {
                applicationList = new ArrayList<>();
            }
        }

        File f3 = new File(DISBURSEMENTS_FILE);
        if (f3.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f3))) {
                disbursementList = (ArrayList<Disbursement>) ois.readObject();
            } catch (Exception e) {
                disbursementList = new ArrayList<>();
            }
        }
    }

    public static void saveApplicants() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(APPLICANTS_FILE))) {
            oos.writeObject(applicantList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveApplications() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(APPLICATIONS_FILE))) {
            oos.writeObject(applicationList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveDisbursements() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DISBURSEMENTS_FILE))) {
            oos.writeObject(disbursementList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveAllData() {
        saveApplicants();
        saveApplications();
        saveDisbursements();
    }

    public static void registerApplicant(Applicant applicant) {
        applicantList.add(applicant);
        saveApplicants();
    }

    public static Applicant authenticateApplicant(String username, String password) {
        for (Applicant app : applicantList) {
            if (app.getUsername().equals(username) && app.getPassword().equals(password)) {
                return app;
            }
        }
        return null;
    }

    // Method Overloading: search by ID or by Name
    public static Applicant searchApplicant(String applicantID) {
        for (Applicant app : applicantList) {
            if (app.getApplicantID().equalsIgnoreCase(applicantID)) {
                return app;
            }
        }
        return null;
    }

    public static ArrayList<Applicant> searchApplicantByName(String name) {
        ArrayList<Applicant> matches = new ArrayList<>();
        for (Applicant app : applicantList) {
            if (app.getName().toLowerCase().contains(name.toLowerCase())) {
                matches.add(app);
            }
        }
        return matches;
    }

    public static ScholarshipApplication findApplicationByApplicantID(String applicantID) {
        for (ScholarshipApplication sa : applicationList) {
            if (sa.getApplicantID().equalsIgnoreCase(applicantID)) {
                return sa;
            }
        }
        return null;
    }

    public static Disbursement findDisbursementByApplicationID(String applicationID) {
        for (Disbursement d : disbursementList) {
            if (d.getApplicationID().equalsIgnoreCase(applicationID)) {
                return d;
            }
        }
        return null;
    }

    public static void reviewApplication(ScholarshipApplication sa, boolean approve) {
        if (sa == null) return;
        if (approve) {
            sa.updateStatus("Approved");
            Disbursement existing = findDisbursementByApplicationID(sa.getApplicationID());
            if (existing == null) {
                Disbursement d = new Disbursement(
                    "DISB-" + (disbursementList.size() + 101),
                    sa.getApplicationID(),
                    sa.getApplicantID(),
                    sa.getApplicantName(),
                    DEFAULT_STIPEND_AMOUNT,
                    LocalDate.now(),
                    "Pending"
                );
                d.recordDisbursement();
                disbursementList.add(d);
            }
        } else {
            sa.updateStatus("Rejected");
        }
        saveAllData();
    }

    public static void updateDisbursementStatus(ScholarshipApplication sa, String newStatus) {
        if (sa == null) return;
        Disbursement d = findDisbursementByApplicationID(sa.getApplicationID());
        if (d == null) {
            d = new Disbursement(
                "DISB-" + (disbursementList.size() + 101),
                sa.getApplicationID(),
                sa.getApplicantID(),
                sa.getApplicantName(),
                DEFAULT_STIPEND_AMOUNT,
                LocalDate.now(),
                newStatus
            );
            disbursementList.add(d);
        } else {
            d.updatePaymentStatus(newStatus, LocalDate.now());
        }
        saveDisbursements();
    }

    public static String generateSummary() {
        int total = applicationList.size();
        int pending = 0, approved = 0, rejected = 0, ineligible = 0, incomplete = 0, released = 0;

        for (ScholarshipApplication sa : applicationList) {
            switch (sa.getStatus()) {
                case "Pending Admin Review": pending++; break;
                case "Approved": approved++; break;
                case "Rejected": rejected++; break;
                case "Ineligible": ineligible++; break;
                case "Incomplete": incomplete++; break;
            }
        }

        for (Disbursement d : disbursementList) {
            if ("Released".equalsIgnoreCase(d.getPaymentStatus())) {
                released++;
            }
        }

        return String.format(
            "========== SCHOLARSHIP SUMMARY REPORT ==========\n\n" +
            "Registered Applicants  : %d\n" +
            "Total Applications     : %d\n" +
            "Pending Admin Review   : %d\n" +
            "Approved               : %d\n" +
            "Rejected               : %d\n" +
            "Ineligible             : %d\n" +
            "Incomplete             : %d\n" +
            "Disbursements Released : %d\n",
            applicantList.size(), total, pending, approved, rejected, ineligible, incomplete, released
        );
    }
}