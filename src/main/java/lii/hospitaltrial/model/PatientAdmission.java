package lii.hospitaltrial.model;

import java.util.Date;

public class PatientAdmission {
    private long id;
    private long patientId;
    private long wardId;
    private int bedNumber;
    private String diagnosis;
    private Date dateAdmitted;
    private Date dateDischarged;

    public PatientAdmission() {}

    public PatientAdmission(long id, long patientId, long wardId, int bedNumber, String diagnosis, Date dateAdmitted, Date dateDischarged) {
        this.id = id;
        this.patientId = patientId;
        this.wardId = wardId;
        this.bedNumber = bedNumber;
        this.diagnosis = diagnosis;
        this.dateAdmitted = dateAdmitted;
        this.dateDischarged = dateDischarged;
    }

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

    public long getWardId() {
        return wardId;
    }

    public void setWardId(long wardId) {
        this.wardId = wardId;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Date getDateAdmitted() {
        return dateAdmitted;
    }

    public void setDateAdmitted(Date dateAdmitted) {
        this.dateAdmitted = dateAdmitted;
    }

    public Date getDateDischarged() {
        return dateDischarged;
    }

    public void setDateDischarged(Date dateDischarged) {
        this.dateDischarged = dateDischarged;
    }

    @Override
    public String toString() {
        return "PatientAdmission{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", wardId=" + wardId +
                ", bedNumber=" + bedNumber +
                ", diagnosis='" + diagnosis + '\'' +
                ", dateAdmitted=" + dateAdmitted +
                ", dateDischarged=" + dateDischarged +
                '}';
    }
}