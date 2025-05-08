package lii.hospitaltrial;

import lii.hospitaltrial.databasecrud.*;
import lii.hospitaltrial.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class HospitalSystemEndToEndTest {

    private static EmployeeCRUD employeeCRUD = new EmployeeCRUD();
    private static SpecialityCRUD specialityCRUD = new SpecialityCRUD();
    private static DoctorCRUD doctorCRUD = new DoctorCRUD();
    private static DepartmentCRUD departmentCRUD = new DepartmentCRUD();
    private static NurseCRUD nurseCRUD = new NurseCRUD();
    private static WardCRUD wardCRUD = new WardCRUD();
    private static PatientCRUD patientCRUD = new PatientCRUD();
    private static PatientAdmissionCRUD admissionCRUD = new PatientAdmissionCRUD();
    private static PatientTreatmentCRUD treatmentCRUD = new PatientTreatmentCRUD();
    private static PatientTransferCRUD transferCRUD = new PatientTransferCRUD();

    public static void main(String[] args) throws Exception {
        runTestScenario();
    }

    private static void runTestScenario() throws Exception {
        System.out.println("=== Starting Hospital System End-to-End Test ===");

        createInitialData();
        patientAdmissionWorkflow();
        try {
            patientTreatmentProcess();
        } catch (Exception e) {
            System.out.println("Error during treatment process: " + e.getMessage());
            throw new RuntimeException(e);
        }
        patientTransferProcess();
        try {
            dischargePatient();
        } catch (Exception e) {
            System.out.println("Error during discharge process: " + e.getMessage());
            throw new RuntimeException(e);
        }
        printFinalReports();

        System.out.println("=== Test Completed Successfully ===");
    }

    private static void createInitialData() {
        System.out.println("\n--- Creating Initial Data ---");

        specialityCRUD.insertSpeciality(new Speciality(1L, "Cardiology"));
        specialityCRUD.insertSpeciality(new Speciality(2L, "Neurology"));
        specialityCRUD.insertSpeciality(new Speciality(3L, "General Surgery"));

        employeeCRUD.insertEmployee(new Employee(101L, "John", "Smith", "123 Main St", 5551234567L));
        employeeCRUD.insertEmployee(new Employee(102L, "Emily", "Johnson", "456 Oak Ave", 5552345678L));
        employeeCRUD.insertEmployee(new Employee(103L, "Michael", "Williams", "789 Pine Rd", 5553456789L));
        employeeCRUD.insertEmployee(new Employee(104L, "Sarah", "Brown", "321 Elm St", 5554567890L));
        employeeCRUD.insertEmployee(new Employee(105L, "David", "Jones", "654 Maple Dr", 5555678901L));

        doctorCRUD.insertDoctor(new Doctor(101L, 1L));
        doctorCRUD.insertDoctor(new Doctor(102L, 2L));
        doctorCRUD.insertDoctor(new Doctor(103L, 3L));

        departmentCRUD.insertDepartment(new Department(1L, "Cardiology", "Building A", 101L));
        departmentCRUD.insertDepartment(new Department(2L, "Neurology", "Building B", 102L));
        departmentCRUD.insertDepartment(new Department(3L, "Surgery", "Building C", 103L));

        nurseCRUD.insertNurse(new Nurse(104L, "Day", 65000.00, 1L));
        nurseCRUD.insertNurse(new Nurse(105L, "Night", 68000.00, 2L));

        wardCRUD.insertWard(new Ward(1L, 1L, 101, 104L, 20));
        wardCRUD.insertWard(new Ward(2L, 2L, 201, 105L, 15));
        wardCRUD.insertWard(new Ward(3L, 1L, 102, 104L, 10));

        System.out.println("Initial data created successfully.");
    }

    private static void patientAdmissionWorkflow() {
        System.out.println("\n--- Patient Admission Workflow ---");

        Patient patient = new Patient(1001L, "Robert", "Wilson", "987 Cedar Ln", 5556789012L);
        patientCRUD.insertPatient(patient);
        System.out.println("Patient created: " + patient);

        PatientAdmission admission = new PatientAdmission(
                1L, 1001L, 1L, 5,
                "Severe chest pain, suspected myocardial infarction",
                Date.valueOf(LocalDate.of(2023, 11, 15)),
                Date.valueOf(LocalDate.of(2023, 11, 30))
        );

        if (admissionCRUD.insertAdmission(admission)) {
            System.out.println("Patient admitted successfully:");
            System.out.println(admission);
        } else {
            System.err.println("Failed to admit patient");
        }
    }

    private static void patientTreatmentProcess() throws Exception {
        System.out.println("\n--- Patient Treatment Process ---");

        PatientTreatment treatment1 = new PatientTreatment(
                1L, 1001L, 101L,
                Date.valueOf(LocalDate.of(2023, 11, 15)),
                "Initial assessment, ordered ECG and blood tests",
                1L
        );

        PatientTreatment treatment2 = new PatientTreatment(
                2L, 1001L, 101L,
                Date.valueOf(LocalDate.of(2023, 11, 16)),
                "Confirmed myocardial infarction, started medication",
                1L
        );

        PatientTreatment treatment3 = new PatientTreatment(
                3L, 1001L, 101L,
                Date.valueOf(LocalDate.of(2023, 11, 18)),
                "Performed angioplasty, patient responding well",
                1L
        );

        treatmentCRUD.insertTreatment(treatment1);
        treatmentCRUD.insertTreatment(treatment2);
        treatmentCRUD.insertTreatment(treatment3);

        System.out.println("Treatments recorded for patient 1001:");
        List<PatientTreatment> treatments = treatmentCRUD.getTreatmentsByPatient(1001L);
        treatments.forEach(System.out::println);
    }

    private static void patientTransferProcess() throws Exception {
        System.out.println("\n--- Patient Transfer Process ---");

        PatientTransfer transfer = new PatientTransfer(
                1L, 1001L, 1L, 2L,
                "Neurological symptoms observed, consult with neurologist required",
                1L
        );

        if (transferCRUD.insertTransfer(transfer)) {
            System.out.println("Transfer recorded successfully:");
            System.out.println(transfer);

            PatientAdmission admission = admissionCRUD.getAdmissionById(1001L);
            admission.setWardId(2L);
            admission.setBedNumber(3);

            if (admissionCRUD.updateAdmission(admission)) {
                System.out.println("Admission updated to new ward:");
                System.out.println(admission);

                PatientTreatment treatment4 = new PatientTreatment(
                        4L, 1001L, 102L,
                        Date.valueOf(LocalDate.of(2023, 11, 20)),
                        "Neurological consultation, ordered MRI",
                        1L
                );

                PatientTreatment treatment5 = new PatientTreatment(
                        5L, 1001L, 102L,
                        Date.valueOf(LocalDate.of(2023, 11, 21)),
                        "MRI results reviewed, no neurological damage found",
                        1L
                );

                treatmentCRUD.insertTreatment(treatment4);
                treatmentCRUD.insertTreatment(treatment5);

                System.out.println("Additional treatments after transfer:");
                treatmentCRUD.getTreatmentsByPatient(1001L).forEach(System.out::println);
            }
        }
    }

    private static void dischargePatient() throws Exception {
        System.out.println("\n--- Patient Discharge Process ---");

        PatientAdmission admission = admissionCRUD.getAdmissionById(1001L);
        admission.setDateDischarged(Date.valueOf(LocalDate.of(2023, 11, 25)));

        if (admissionCRUD.updateAdmission(admission)) {
            System.out.println("Patient discharged successfully. Final admission details:");
            System.out.println(admission);
        } else {
            System.err.println("Failed to update discharge date");
        }
    }

    private static void printFinalReports() throws Exception {
        System.out.println("\n--- Final Reports ---");

        System.out.println("\nAll Departments:");
        departmentCRUD.getAllDepartments().forEach(dept -> {
            System.out.println(dept);
            try {
                System.out.println("  Director: " + employeeCRUD.getEmployeeById(dept.getDirectorId()).getFirstName() +
                        " " + employeeCRUD.getEmployeeById(dept.getDirectorId()).getSurname());
            } catch (Exception e) {
                System.err.println("Error getting director info");
            }
        });

        System.out.println("\nPatient Admission History:");
        PatientAdmission admission = admissionCRUD.getAdmissionById(1L);
        System.out.println(admission);

        System.out.println("\nAll Treatments for Patient 1001:");
        treatmentCRUD.getTreatmentsByPatient(1001L).forEach(treatment -> {
            try {
                Employee doctor = employeeCRUD.getEmployeeById(treatment.getDoctorId());
                System.out.printf("%s - Dr. %s: %s%n",
                        treatment.getTreatmentDate(),
                        doctor.getSurname(),
                        treatment.getRemarks());
            } catch (Exception e) {
                System.err.println("Error getting doctor info");
            }
        });

        System.out.println("\nCurrent Ward Occupancy:");
        wardCRUD.getAllWards().forEach(ward -> {
            try {
                Department dept = departmentCRUD.getDepartmentById(ward.getDepartmentId());
                long occupiedBeds = admissionCRUD.getAdmissionsByWard(ward.getWardId()).size();
                System.out.printf("%s Ward %d: %d/%d beds occupied%n",
                        dept.getDepartmentName(),
                        ward.getWardNumber(),
                        occupiedBeds,
                        ward.getBedCount());
            } catch (Exception e) {
                System.err.println("Error getting ward info");
            }
        });
    }
}