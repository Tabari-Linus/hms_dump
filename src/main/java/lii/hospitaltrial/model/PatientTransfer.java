package lii.hospitaltrial.model;

public class PatientTransfer {
    private long id;
    private long patientId;
    private long fromWard;
    private long toWard;
    private String reason;
    private long patientAdmissionId;

    public PatientTransfer(long id, long patientId, long fromWard, long toWard, String reason, long patientAdmissionId) {
        this.id = id;
        this.patientId = patientId;
        this.fromWard = fromWard;
        this.toWard = toWard;
        this.reason = reason;
        this.patientAdmissionId = patientAdmissionId;
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

    public long getFromWard() {
        return fromWard;
    }

    public void setFromWard(long fromWard) {
        this.fromWard = fromWard;
    }

    public long getToWard() {
        return toWard;
    }

    public void setToWard(long toWard) {
        this.toWard = toWard;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getPatientAdmissionId() {
        return patientAdmissionId;
    }

    public void setPatientAdmissionId(long patientAdmissionId) {
        this.patientAdmissionId = patientAdmissionId;
    }
}
