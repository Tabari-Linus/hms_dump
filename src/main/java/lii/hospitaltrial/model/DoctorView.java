package lii.hospitaltrial.model;

public class DoctorView {
    private long employeeId;
    private String firstName;
    private String surname;
    private String address;
    private long telephoneNo;
    private long specialityId;

    public DoctorView(Employee employee, Doctor doctor) {
        this.employeeId = employee.getEmployeeId();
        this.firstName = employee.getFirstName();
        this.surname = employee.getSurname();
        this.address = employee.getAddress();
        this.telephoneNo = employee.getTelephoneNo();
        this.specialityId = doctor.getSpecialityId();
    }

    // Getters only since this is a view model
    public long getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getFullName() { return firstName + " " + surname; }
    public String getAddress() { return address; }
    public long getTelephoneNo() { return telephoneNo; }
    public long getSpecialityId() { return specialityId; }
}