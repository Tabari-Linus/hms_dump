package lii.hospitaltrial.model;

public class Patient {
    private long patientId;
    private String firstName;
    private String surname;
    private String address;
    private long telephoneNo;

    public Patient() {}

    public Patient(long patientId, String firstName, String surname, String address, long telephoneNo) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.telephoneNo = telephoneNo;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(long telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", telephoneNo=" + telephoneNo +
                '}';
    }
}