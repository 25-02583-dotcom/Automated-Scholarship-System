# Automated Scholarship Application, Document Validation, and Disbursement Monitoring System

**Course:** OOP101 – Object-Oriented Programming (AY 2026–2027)  
**Section:** IT-2112 | **Team:** J³ (J Cubed) – Group 1  
**Instructor:** Mr. Juriel U. Comia  

---

## 1. Project Description
The **Automated Scholarship Application, Document Validation, and Disbursement Monitoring System** is a structured Java-based desktop application designed to streamline scholarship application processing, automate requirement validation against predefined eligibility criteria, and maintain systematic logs of stipend disbursements.

---

## 2. Problem Being Addressed
Manual processing of scholarship applications is labor-intensive and prone to human error. Relying on paper-based records and unorganized digital folders often leads to:
- Misplaced or lost supporting documents
- Duplicate scholarship submissions
- Delayed verification of applicant eligibility
- Inaccurate or imprecise tracking of stipend disbursements

---

## 3. Project Objectives
### General Objective
To develop a Java-based application that automates scholarship application management, validates submitted supporting documents, and monitors allowance disbursements to maintain transparent and efficient scholarship records.

### Specific Objectives
- Record and organize student applicant profiles and scholarship application details.
- Build an automated document validation engine that evaluates academic credentials (GWA), income thresholds, and requirement completeness.
- Implement a smart completeness checker that flags missing or invalid submissions prior to administrator review.
- Maintain systematic application tracking across all stages (`Pending`, `Auto-Verified`, `Approved`, `Rejected`).
- Log and track scholarship disbursement details, including payout amounts, dates, and balance statuses.
- Generate structured summaries and reports for applications, document verification statuses, and financial disbursements.

---

## 4. Target Users
| Target User | System Usage |
| :--- | :--- |
| **Scholarship Applicants** | Submit personal details, log requirement submissions, track application validation progress, and view disbursement schedules. |
| **Scholarship Administrators / Staff** | Review automatically validated documents, confirm application approvals, generate summary reports, and record stipend disbursements. |

---

## 5. Main Features
- **Applicant & Application Management:** Create, view, search, and update applicant details and scholarship categories.
- **Automated Document Validation:** Programmatically checks GWA, household income caps, and document validity against eligibility thresholds without manual calculation.
- **Smart Completeness Checker:** Automatically flags incomplete requirement sets before final review, preventing the submission of invalid applications.
- **Application Progress Tracker:** Monitors real-time status transitions (`Submitted` → `Pending Verification` → `Verified` → `Approved` / `Rejected`).
- **Automated Disbursement Monitoring:** Logs stipend payout amounts, dates, and balance statuses while highlighting pending or delayed releases.
- **Summary & Reporting Module:** Generates formatted summaries of applicants, document verification logs, and disbursement histories.

---

## 6. Technologies Used
- **Programming Language:** Java (JDK 17+)
- **Core Concepts:** Object-Oriented Programming (OOP), Java Collections Framework (`ArrayList`), File I/O
- **Tools & Version Control:** Git & GitHub, *[Ilagay ang IDE ninyo, hal. NetBeans / IntelliJ IDEA / VS Code]*

---

## 7. Team Members and Roles
| No. | SR Code | Name | Role / Assigned Tasks |
| :---: | :---: | :--- | :--- |
| 1 | 25-05743 | **Babao, John Robert M.** | *[Ilagay ang assigned task, hal. System Architecture & Validation Logic]* |
| 2 | 25-04523 | **Driz, Jelie May M.** | *[Ilagay ang assigned task, hal. UI/Menu Flow & Application Tracking]* |
| 3 | 25-02583 | **Luna, Jovelyn** | *[Ilagay ang assigned task, hal. Disbursement Module, Reports & Documentation]* |

---

## 8. OOP Concepts Demonstrated
- **Classes and Objects:** Entities such as `Applicant`, `ScholarshipApplication`, `GovernmentDocument`, `Disbursement`, `ScholarshipManager`, and `Main` encapsulate individual records and system operations.
- **Methods:** Operational logic is modularized into methods like `validateDocument()`, `updateStatus()`, `recordDisbursement()`, and `generateSummary()`.
- **Encapsulation:** Sensitive fields (such as student `gwa`, household income, and disbursement `amount`) are declared `private` and accessed strictly through getters and setters.
- **Inheritance:** *[Ilagay ang parent-child classes ninyo, hal. `Applicant` at `Admin` ay nag-i-inherit mula sa base class na `User`]*
- **Polymorphism:** *[Ilagay ang method overriding/overloading na ginamit ninyo, hal. overridden `displayDetails()` o `validate()` methods]*
- **Abstraction:** Internal document checking and tracking algorithms are hidden behind clean, menu-driven user interfaces.
- **Exception Handling:** Uses `try-catch` blocks to catch invalid user inputs, non-numeric entries, and out-of-bounds menu selections.
- **Arrays / Data Handling:** Utilizes `ArrayList` to dynamically store, search, filter, and generate reports from records.

---

## 9. Instructions for Running the Program
1. Ensure **Java Development Kit (JDK)** is installed on your computer.
2. Clone this repository:
   ```bash
   git clone [https://github.com/](https://github.com/)[username]/[repository-name].git
