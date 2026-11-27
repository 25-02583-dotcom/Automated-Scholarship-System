import java.io.Serializable;
import java.time.LocalDate;

public class Disbursement implements Serializable {
    private static final long serialVersionUID = 1L;

    private String disbursementID;
    private String applicationID;
    private String applicantID;
    private String applicantName;
    private double amount;
    private LocalDate date;
    private String paymentStatus;

    public Disbursement(String disbursementID, String applicationID, String applicantID,
                        String applicantName, double amount, LocalDate date, String paymentStatus) {
        this.disbursementID = disbursementID;
        this.applicationID = applicationID;
        this.applicantID = applicantID;
        this.applicantName = applicantName;
        this.amount = amount;
        this.date = date;
        this.paymentStatus = paymentStatus;
    }

    public void recordDisbursement() {
        if (this.date == null) {
            this.date = LocalDate.now();
        }
        if (this.paymentStatus == null || this.paymentStatus.trim().isEmpty()) {
            this.paymentStatus = "Pending";
        }
    }

    public void updatePaymentStatus(String status, LocalDate payoutDate) {
        this.paymentStatus = status;
        if (payoutDate != null) {
            this.date = payoutDate;
        }
    }

    public String displayDisbursement() {
        return String.format(
            "Disb ID: %s | App ID: %s | Student: %s | Amount: P%.2f | Date: %s | Status: %s",
            disbursementID, applicationID, applicantName, amount, date, paymentStatus
        );
    }

    public String getDisbursementID() { return disbursementID; }
    public String getApplicationID() { return applicationID; }
    public String getApplicantID() { return applicantID; }
    public String getApplicantName() { return applicantName; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getPaymentStatus() { return paymentStatus; }
}