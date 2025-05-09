package lii.hospitaltrial;

import lii.hospitaltrial.databasecrud.*;
import lii.hospitaltrial.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    private static void createInitialData() throws Exception {
        System.out.println("\n--- Creating Initial Data ---");

        // Insert specialities first (no dependencies)
        Speciality cardiology = new Speciality( null,"Cardiology");
        Speciality neurology = new Speciality(null, "Neurology");
        Speciality surgery = new Speciality(null, "General Surgery");

        specialityCRUD.insertSpeciality(cardiology);
        specialityCRUD.insertSpeciality(neurology);
        specialityCRUD.insertSpeciality(surgery);

        // Insert employees (no dependencies)
        Employee emp1 = new Employee(null, "John", "Smith", "123 Main St", 5551234567L);
        Employee emp2 = new Employee(null, "Emily", "Johnson", "456 Oak Ave", 5552345678L);
        Employee emp3 = new Employee(null, "Michael", "Williams", "789 Pine Rd", 5553456789L);
        Employee emp4 = new Employee(null, "Sarah", "Brown", "321 Elm St", 5554567890L);
        Employee emp5 = new Employee(null, "David", "Jones", "654 Maple Dr", 5555678901L);

        employeeCRUD.insertEmployee(emp1);
        employeeCRUD.insertEmployee(emp2);
        employeeCRUD.insertEmployee(emp3);
        employeeCRUD.insertEmployee(emp4);
        employeeCRUD.insertEmployee(emp5);

        // Insert doctors (depends on employee and speciality)
        doctorCRUD.insertDoctor(new Doctor(emp1.getEmployeeId(), cardiology.getSpecialityId()));
        doctorCRUD.insertDoctor(new Doctor(emp2.getEmployeeId(), neurology.getSpecialityId()));
        doctorCRUD.insertDoctor(new Doctor(emp3.getEmployeeId(), surgery.getSpecialityId()));

        // Insert departments (depends on doctors)
        Department dept1 = new Department(null, "Cardiology", "Building A", emp1.getEmployeeId());
        Department dept2 = new Department(null, "Neurology", "Building B", emp2.getEmployeeId());
        Department dept3 = new Department(null, "Surgery", "Building C", emp3.getEmployeeId());

        departmentCRUD.insertDepartment(dept1);
        departmentCRUD.insertDepartment(dept2);
        departmentCRUD.insertDepartment(dept3);

        // Insert nurses (depends on employee and department)
        nurseCRUD.insertNurse(new Nurse(emp4.getEmployeeId(), "Day", 65000.00, dept1.getDepartmentCode()));
        nurseCRUD.insertNurse(new Nurse(emp5.getEmployeeId(), "Night", 68000.00, dept2.getDepartmentCode()));

        // Insert wards (depends on department and nurse)
        Ward ward1 = new Ward(null, dept1.getDepartmentCode(), 101, emp4.getEmployeeId(), 20);
        Ward ward2 = new Ward(null, dept2.getDepartmentCode(), 201, emp5.getEmployeeId(), 15);
        Ward ward3 = new Ward(null, dept1.getDepartmentCode(), 102, emp4.getEmployeeId(), 10);

        wardCRUD.insertWard(ward1);
        wardCRUD.insertWard(ward2);
        wardCRUD.insertWard(ward3);

        System.out.println("Initial data created successfully.");
    }

    private static void patientAdmissionWorkflow() throws Exception {
        System.out.println("\n--- Patient Admission Workflow ---");

        // Get first ward for admission
        List<Ward> wards = wardCRUD.getAllWards();
        if (wards.isEmpty()) {
            throw new RuntimeException("No wards available for admission");
        }
        Ward firstWard = wards.get(0);

        // Create patient
        Patient patient = new Patient(null, "Robert", "Wilson", "987 Cedar Ln", 5556789012L);
        patientCRUD.insertPatient(patient);
        System.out.println("Patient created: " + patient);

        // Create admission
        PatientAdmission admission = new PatientAdmission(
                null,
                patient.getPatientId(),
                firstWard.getWardId(),
                5,
                "Severe chest pain, suspected myocardial infarction",
                LocalDate.of(2023, 11, 15),
                LocalDate.of(2023, 11, 30)
        );

        if (admissionCRUD.insertPatientAdmission(admission)) {
            System.out.println("Patient admitted successfully:");
            System.out.println(admission);
        } else {
            System.err.println("Failed to admit patient");
        }
    }

    private static void patientTreatmentProcess() throws Exception {
        System.out.println("\n--- Patient Treatment Process ---");

        // Get the first patient and doctor
        List<Patient> patients = patientCRUD.getAllPatients();
        if (patients.isEmpty()) {
            throw new RuntimeException("No patients available for treatment");
        }
        Patient patient = patients.get(0);

        List<Doctor> doctors = doctorCRUD.getAllDoctors();
        if (doctors.isEmpty()) {
            throw new RuntimeException("No doctors available");
        }
        Doctor doctor = doctors.get(0);

        List<PatientAdmission> admissions = admissionCRUD.getAdmissionsByPatient(patient.getPatientId());
        if (admissions.isEmpty()) {
            throw new RuntimeException("No admissions found for patient");
        }
        PatientAdmission admission = admissions.get(0);

        // Create treatments
        PatientTreatment treatment1 = new PatientTreatment(
                null,
                patient.getPatientId(),
                doctor.getEmployeeId(),
                LocalDate.of(2023, 11, 15),
                "Initial assessment, ordered ECG and blood tests",
                admission.getId()
        );

        PatientTreatment treatment2 = new PatientTreatment(
                null,
                patient.getPatientId(),
                doctor.getEmployeeId(),
                LocalDate.of(2023, 12, 15),
                "Confirmed myocardial infarction, started medication",
                admission.getId()
        );

        PatientTreatment treatment3 = new PatientTreatment(
                null,
                patient.getPatientId(),
                doctor.getEmployeeId(),
                LocalDate.of(2022, 11, 15),
                "Performed angioplasty, patient responding well",
                admission.getId()
        );

        treatmentCRUD.insertPatientTreatment(treatment1);
        treatmentCRUD.insertPatientTreatment(treatment2);
        treatmentCRUD.insertPatientTreatment(treatment3);

        System.out.println("Treatments recorded for patient " + patient.getPatientId() + ":");
        List<PatientTreatment> treatments = treatmentCRUD.getTreatmentsByPatient(patient.getPatientId());
        treatments.forEach(System.out::println);
    }

    private static void patientTransferProcess() throws Exception {
        System.out.println("\n--- Patient Transfer Process ---");

        // Get patient and current admission
        List<Patient> patients = patientCRUD.getAllPatients();
        if (patients.isEmpty()) {
            throw new RuntimeException("No patients available for transfer");
        }
        Patient patient = patients.get(0);

        List<PatientAdmission> admissions = admissionCRUD.getAdmissionsByPatient(patient.getPatientId());
        if (admissions.isEmpty()) {
            throw new RuntimeException("No admissions found for patient");
        }
        PatientAdmission admission = admissions.get(0);

        // Get available wards for transfer (excluding current ward)
        List<Ward> allWards = wardCRUD.getAllWards();
        if (allWards.size() < 2) {
            throw new RuntimeException("Not enough wards for transfer");
        }
        Ward fromWard = wardCRUD.getWardById(admission.getWardId());
        // In patientTransferProcess method
        Ward toWard = allWards.stream()
                .filter(w -> !Objects.equals(w.getWardId(), fromWard.getWardId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No suitable ward found for transfer"));
        // Create transfer record
        PatientTransfer transfer = new PatientTransfer(
                null,
                patient.getPatientId(),
                fromWard.getWardId(),
                toWard.getWardId(),
                "Neurological symptoms observed, consult with neurologist required",
                admission.getId()
        );

        if (transferCRUD.insertTransfer(transfer)) {
            System.out.println("Transfer recorded successfully:");
            System.out.println(transfer);

            // Update admission to new ward
            admission.setWardId(toWard.getWardId());
            admission.setBedNumber(3); // Assign new bed number

            if (admissionCRUD.updateAdmission(admission)) {
                System.out.println("Admission updated to new ward:");
                System.out.println(admission);

                // Get neurologist for new treatments
                List<Doctor> neurologists = doctorCRUD.getDoctorsBySpeciality(2L); // Assuming 2 is neurology
                if (neurologists.isEmpty()) {
                    throw new RuntimeException("No neurologists available");
                }
                Doctor neurologist = neurologists.get(0);

                // Add new treatments after transfer
                PatientTreatment treatment4 = new PatientTreatment(
                        null,
                        patient.getPatientId(),
                        neurologist.getEmployeeId(),
                        LocalDate.of(2023, 11, 20),
                        "Neurological consultation, ordered MRI",
                        admission.getId()
                );

                PatientTreatment treatment5 = new PatientTreatment(
                        null,
                        patient.getPatientId(),
                        neurologist.getEmployeeId(),
                        LocalDate.of(2023, 11, 21),
                        "MRI results reviewed, no neurological damage found",
                        admission.getId()
                );

                treatmentCRUD.insertPatientTreatment(treatment4);
                treatmentCRUD.insertPatientTreatment(treatment5);

                System.out.println("Additional treatments after transfer:");
                treatmentCRUD.getTreatmentsByPatient(patient.getPatientId()).forEach(System.out::println);
            }
        }
    }

    private static void dischargePatient() throws Exception {
        System.out.println("\n--- Patient Discharge Process ---");

        // Get first patient admission
        List<PatientAdmission> admissions = admissionCRUD.getAllAdmissions();
        if (admissions.isEmpty()) {
            throw new RuntimeException("No admissions found");
        }
        PatientAdmission admission = admissions.get(0);

        // Update discharge date
        admission.setDateDischarged(LocalDate.of(2023, 11, 25));

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
            try {
                System.out.println(dept);
                Employee director = employeeCRUD.getEmployeeById(dept.getDirectorId());
                System.out.println("  Director: " + director.getFirstName() + " " + director.getSurname());
            } catch (Exception e) {
                System.err.println("Error getting department info: " + e.getMessage());
            }
        });

        System.out.println("\nAll Patients:");
        patientCRUD.getAllPatients().forEach(System.out::println);

        System.out.println("\nPatient Admission History:");
        admissionCRUD.getAllAdmissions().forEach(admission -> {
            try {
                Patient patient = patientCRUD.getPatientById(admission.getPatientId());
                Ward ward = wardCRUD.getWardById(admission.getWardId());
                System.out.printf("Patient: %s %s, Ward: %d, Admitted: %s, Discharged: %s%n",
                        patient.getFirstName(), patient.getSurname(),
                        ward.getWardNumber(),
                        admission.getDateAdmitted(),
                        admission.getDateDischarged());
            } catch (Exception e) {
                System.err.println("Error getting admission info: " + e.getMessage());
            }
        });

        System.out.println("\nAll Treatments:");
        treatmentCRUD.getAllTreatments().forEach(treatment -> {
            try {
                Patient patient = patientCRUD.getPatientById(treatment.getPatientId());
                Employee doctor = employeeCRUD.getEmployeeById(treatment.getDoctorId());
                System.out.printf("%s - %s %s treated by Dr. %s: %s%n",
                        treatment.getTreatmentDate(),
                        patient.getFirstName(), patient.getSurname(),
                        doctor.getSurname(),
                        treatment.getRemarks());
            } catch (Exception e) {
                System.err.println("Error getting treatment info: " + e.getMessage());
            }
        });

        System.out.println("\nCurrent Ward Occupancy:");
        wardCRUD.getAllWards().forEach(ward -> {
            try {
                Department dept = departmentCRUD.getDepartmentById(ward.getDepartmentId());
                long occupiedBeds = admissionCRUD.getCurrentAdmissionsByWard(ward.getWardId()).size();
                System.out.printf("%s Ward %d: %d/%d beds occupied%n",
                        dept.getDepartmentName(),
                        ward.getWardNumber(),
                        occupiedBeds,
                        ward.getBedCount());
            } catch (Exception e) {
                System.err.println("Error getting ward info: " + e.getMessage());
            }
        });
    }
}