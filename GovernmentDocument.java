import java.io.Serializable;
import java.time.LocalDate;

public class GovernmentDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String documentID;
    protected String documentType;
    protected String fileName;
    protected LocalDate submissionDate;
    protected LocalDate expiryDate;
    protected String verificationStatus;

    public GovernmentDocument(String documentID, String documentType, String fileName,
                              LocalDate submissionDate, LocalDate expiryDate) {
        this.documentID = documentID;
        this.documentType = documentType;
        this.fileName = fileName;
        this.submissionDate = submissionDate;
        this.expiryDate = expiryDate;
        this.verificationStatus = "Unverified";
    }

    public void recordDocument() {
        if (this.submissionDate == null) {
            this.submissionDate = LocalDate.now();
        }
        if (this.verificationStatus == null) {
            this.verificationStatus = "Recorded";
        }
    }

    public boolean validateDocument() {
        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }
        String lower = fileName.trim().toLowerCase();
        boolean validExt = lower.endsWith(".pdf") || lower.endsWith(".jpg") ||
                           lower.endsWith(".jpeg") || lower.endsWith(".png");
        boolean notExpired = expiryDate != null && !expiryDate.isBefore(LocalDate.now());
        return validExt && notExpired;
    }

    public boolean verifyDocument() {
        boolean ok = validateDocument();
        updateDocumentStatus(ok ? "Verified" : "Invalid / Expired");
        return ok;
    }

    public void updateDocumentStatus(String newStatus) {
        this.verificationStatus = newStatus;
    }

    public String getDocumentID() { return documentID; }
    public String getDocumentType() { return documentType; }
    public String getFileName() { return fileName; }
    public LocalDate getSubmissionDate() { return submissionDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getVerificationStatus() { return verificationStatus; }

    public static class AcademicRecord extends GovernmentDocument {
        private static final long serialVersionUID = 1L;
        private double gwa;

        public AcademicRecord(String documentID, String fileName, LocalDate submissionDate,
                              LocalDate expiryDate, double gwa) {
            super(documentID, "Academic Record", fileName, submissionDate, expiryDate);
            this.gwa = gwa;
        }

        @Override
        public boolean validateDocument() {
            return super.validateDocument() && gwa >= 1.0 && gwa <= 5.0;
        }

        public double getGwa() { return gwa; }
    }

    public static class IncomeCertificate extends GovernmentDocument {
        private static final long serialVersionUID = 1L;
        private double annualIncome;

        public IncomeCertificate(String documentID, String fileName, LocalDate submissionDate,
                                 LocalDate expiryDate, double annualIncome) {
            super(documentID, "Income Certificate", fileName, submissionDate, expiryDate);
            this.annualIncome = annualIncome;
        }

        @Override
        public boolean validateDocument() {
            return super.validateDocument() && annualIncome >= 0;
        }

        public double getAnnualIncome() { return annualIncome; }
    }

    public static class IdentificationDocument extends GovernmentDocument {
        private static final long serialVersionUID = 1L;

        public IdentificationDocument(String documentID, String fileName,
                                      LocalDate submissionDate, LocalDate expiryDate) {
            super(documentID, "Identification Document", fileName, submissionDate, expiryDate);
        }
    }
}