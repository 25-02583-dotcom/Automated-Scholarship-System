import java.io.Serializable;

public class Applicant implements Serializable {
    private static final long serialVersionUID = 1L;

    private String applicantID;
    private String name;
    private String contactDetails;
    private String address;
    private double gwa;
    private String username;
    private String password;

    public Applicant(String applicantID, String name, String contactDetails,
                     String address, double gwa, String username, String password) {
        this.applicantID = applicantID;
        this.name = name;
        this.contactDetails = contactDetails;
        this.address = address;
        this.gwa = gwa;
        this.username = username;
        this.password = password;
    }

    public void addApplicant() {
        ScholarshipManager.registerApplicant(this);
    }

    public void updateApplicant(String name, String contactDetails, String address, double gwa) {
        this.name = name;
        this.contactDetails = contactDetails;
        this.address = address;
        this.gwa = gwa;
        ScholarshipManager.saveAllData();
    }

    public String displayApplicant() {
        return String.format(
            "Applicant ID: %s | Name: %s | Contact: %s | Address: %s | GWA: %.2f",
            applicantID, name, contactDetails, address, gwa
        );
    }

    public String getApplicantID() { return applicantID; }
    public void setApplicantID(String applicantID) { this.applicantID = applicantID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactDetails() { return contactDetails; }
    public void setContactDetails(String contactDetails) { this.contactDetails = contactDetails; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getGwa() { return gwa; }
    public void setGwa(double gwa) { this.gwa = gwa; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}