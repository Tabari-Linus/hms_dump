//package lii.hospitaltrial;
//
//import lii.hospitaltrial.model.Speciality;
//import lii.hospitaltrial.model.Employee;
//import lii.hospitaltrial.model.Doctor;
//import lii.hospitaltrial.model.Department;
//import lii.hospitaltrial.model.Nurse;
//import lii.hospitaltrial.model.Patient;
//import lii.hospitaltrial.model.PatientAdmission;
//import lii.hospitaltrial.model.PatientTransfer;
//import lii.hospitaltrial.model.PatientTreatment;
//import lii.hospitaltrial.model.UserAccount;
//import lii.hospitaltrial.model.Ward;
//
//import lii.hospitaltrial.databasedao.EmployeeDAO;
//import lii.hospitaltrial.databasedao.SpecialityDAO;
//import lii.hospitaltrial.databasedao.DoctorDAO;
//import lii.hospitaltrial.databasedao.DepartmentDAO;
//import lii.hospitaltrial.databasedao.NurseDAO;
//import lii.hospitaltrial.databasedao.PatientDAO;
//import lii.hospitaltrial.databasedao.PatientAdmissionDAO;
//import lii.hospitaltrial.databasedao.PatientTransferDAO;
//import lii.hospitaltrial.databasedao.PatientTreatmentDAO;
//import lii.hospitaltrial.databasedao.UserAccountDAO;
//import lii.hospitaltrial.databasedao.WardDAO;
//import lii.hospitaltrial.databasedao.DBConnection;
//
//
//
//public class Main {
//
//
//    public static void main(String[] args) {
//
//        // Example usage of the DAO classes
//        Speciality speciality = new Speciality(1L, "Cardiology");
//        SpecialityDAO.insertSpeciality(speciality);
//
//        // Inserting a new employees
//        Employee doc1 = new Employee(1001L, "John", "Doe", "123 Elm St", 1234567890L);
//        EmployeeDAO.insertEmployee(doc1);
//
//        Employee nurse1 = new Employee(1003L, "Alice", "Smith", "456 Oak St", 1234567891L);
//        employeeDAO.insertEmployee(nurse1);
//
//
//        Doctor doctor = new Doctor(1001L, 1L);  // employee_id, speciality_id
//        doctorDAO.insertDoctor(doctor);
//
//        Department dept = new Department(10L, "Cardiology", "Building A", 1001L);
//        departmentDAO.insertDepartment(dept);
//
//        Nurse nurse = new Nurse(1003L, "Night", new BigDecimal("50000.00"), 10L);
//        nurseDAO.insertNurse(nurse);
//
//        Ward ward = new Ward(200L, 10L, 1, 1003L, 12);
//        wardDAO.insertWard(ward);
//
//        Patient patient = new Patient(3001L, "Michael", "Jordan", "789 Pine St", 9876543210L);
//        patientDAO.insertPatient(patient);
//
//        PatientAdmission admission = new PatientAdmission(
//                1L, 3001L, 200L, 1, "Heart Condition", LocalDate.of(2025, 5, 7), null
//        );
//        patientAdmissionDAO.insertAdmission(admission);
//
//        PatientTreatment treatment = new PatientTreatment(
//                1L, 3001L, 1001L, LocalDate.of(2025, 5, 7), "Initial checkup and ECG", 1L
//        );
//        patientTreatmentDAO.insertTreatment(treatment);
//
//
//        Ward ward2 = new Ward(201L, 10L, 2, 1003L, 10);
//        wardDAO.insertWard(ward2);
//
//        PatientTransfer transfer = new PatientTransfer(
//                1L, 3001L, 200L, 201L, "Required neurological observation", 1L
//        );
//        patientTransferDAO.insertTransfer(transfer);
//
//        patientAdmissionDAO.updateDischargeDate(1L, LocalDate.of(2025, 5, 9));
//
//        UserAccount user = new UserAccount(1L, "jdoe", "securePass123", true, 1001L);
//        userAccountDAO.insertUser(user);
//
//        Patient retrieved = patientDAO.getAll(3001L);
//        System.out.println("Patient: " + retrieved.getFirstName());
//
//    }
//}