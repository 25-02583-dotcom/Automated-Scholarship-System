import java.io.Serializable;
import java.time.LocalDate;

public class ScholarshipApplication implements Serializable {
    private static final long serialVersionUID = 1L;

    private String applicationID;
    private String applicantID;
    private String applicantName;
    private String scholarshipType;
    private double gwa;
    private double annualIncome;
    private GovernmentDocument document;
    private String status;
    private LocalDate dateSubmitted;

    public ScholarshipApplication(String applicationID, String applicantID, String applicantName,
                                  String scholarshipType, double gwa, double annualIncome,
                                  GovernmentDocument document) {
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.applicantName = applicantName;
        this.scholarshipType = scholarshipType;
        this.gwa = gwa;
        this.annualIncome = annualIncome;
        this.document = document;
        this.dateSubmitted = LocalDate.now();
        this.status = "Submitted Pending Verification";
    }

    public void submitApplication() {
        if (this.dateSubmitted == null) {
            this.dateSubmitted = LocalDate.now();
        }
        if (this.document != null) {
            this.document.recordDocument();
            boolean docValid = this.document.verifyDocument();
            if (!docValid) {
                this.status = "Incomplete";
                return;
            }
        } else {
            this.status = "Incomplete";
            return;
        }

        if (this.gwa <= ScholarshipManager.MAXIMUM_GWA &&
            this.annualIncome <= ScholarshipManager.MAXIMUM_ANNUAL_INCOME) {
            this.status = "Pending Admin Review";
        } else {
            this.status = "Ineligible";
        }
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public String displayApplication() {
        return String.format(
            "App ID: %s | Applicant: %s (%s) | Program: %s | GWA: %.2f | Income: P%.2f | Status: %s",
            applicationID, applicantName, applicantID, scholarshipType, gwa, annualIncome, status
        );
    }

    public String getApplicationID() { return applicationID; }
    public String getApplicantID() { return applicantID; }
    public String getApplicantName() { return applicantName; }
    public String getScholarshipType() { return scholarshipType; }
    public double getGwa() { return gwa; }
    public double getAnnualIncome() { return annualIncome; }
    public GovernmentDocument getDocument() { return document; }
    public String getStatus() { return status; }
    public LocalDate getDateSubmitted() { return dateSubmitted; }
}