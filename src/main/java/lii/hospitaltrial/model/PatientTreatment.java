package lii.hospitaltrial.model;

import java.sql.Date;

public class PatientTreatment {
    private long id;
    private long patientId;
    private long doctorId;
    private Date treatmentDate;
    private String remarks;
    private long patientAdmissionId;

    // Constructor
    public PatientTreatment(long id, long patientId, long doctorId, Date treatmentDate, String remarks, long patientAdmissionId) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.treatmentDate = treatmentDate;
        this.remarks = remarks;
        this.patientAdmissionId = patientAdmissionId;
    }

    // Default constructor
    public PatientTreatment() {}

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getPatientAdmissionId() {
        return patientAdmissionId;
    }

    public void setPatientAdmissionId(long patientAdmissionId) {
        this.patientAdmissionId = patientAdmissionId;
    }
}