package lii.hospitaltrial.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lii.hospitaltrial.model.Doctor;
import lii.hospitaltrial.model.Employee;

public class DoctorDialogController {
    @FXML private TextField employeeIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField addressField;
    @FXML private TextField telephoneNoField;
    @FXML private TextField specialityIdField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private boolean saveClicked = false;
    private Employee employee;
    private Doctor doctor;

    @FXML
    private void initialize() {
        cancelButton.setOnAction(e -> closeDialog());
        saveButton.setOnAction(e -> handleSave());
    }

    public void setDoctorData(Employee employee, Doctor doctor) {
        this.employee = employee;
        this.doctor = doctor;

        if (employee != null) {
            employeeIdField.setText(String.valueOf(employee.getEmployeeId()));
            firstNameField.setText(employee.getFirstName());
            surnameField.setText(employee.getSurname());
            addressField.setText(employee.getAddress());
            telephoneNoField.setText(String.valueOf(employee.getTelephoneNo()));
        }

        if (doctor != null) {
            specialityIdField.setText(String.valueOf(doctor.getSpecialityId()));
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    private void handleSave() {
        try {
            if (employeeIdField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                    surnameField.getText().isEmpty() || addressField.getText().isEmpty() ||
                    telephoneNoField.getText().isEmpty() || specialityIdField.getText().isEmpty()) {
                throw new IllegalArgumentException("All fields are required.");
            }

            long employeeId = Long.parseLong(employeeIdField.getText());
            long telephoneNo = Long.parseLong(telephoneNoField.getText());
            long specialityId = Long.parseLong(specialityIdField.getText());

            if (employee == null) {
                employee = new Employee();
            }
            if (doctor == null) {
                doctor = new Doctor();
            }

            employee.setEmployeeId(employeeId);
            employee.setFirstName(firstNameField.getText());
            employee.setSurname(surnameField.getText());
            employee.setAddress(addressField.getText());
            employee.setTelephoneNo(telephoneNo);

            doctor.setEmployeeId(employeeId);
            doctor.setSpecialityId(specialityId);

            saveClicked = true;
            closeDialog();
        } catch (NumberFormatException e) {
            showError("Employee ID, Telephone No, and Speciality ID must be numeric.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Employee getEmployee() {
        return employee;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}