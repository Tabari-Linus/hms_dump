package lii.hospitaltrial.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lii.hospitaltrial.model.Patient;

public class PatientDialogController {
    @FXML private TextField patientIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField surnameField;
    @FXML private TextField addressField;
    @FXML private TextField telephoneNoField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private boolean saveClicked = false;
    private Patient patient;

    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        patientIdField.setText(String.valueOf(patient.getPatientId()));
        firstNameField.setText(patient.getFirstName());
        surnameField.setText(patient.getSurname());
        addressField.setText(patient.getAddress());
        telephoneNoField.setText(patient.getTelephoneNo() > 0 ?
                String.valueOf(patient.getTelephoneNo()) : "");
    }

    // Add this method to disable the patient ID field
    public void disablePatientIdField() {
        patientIdField.setEditable(false);
        patientIdField.setDisable(true);
        patientIdField.setStyle("-fx-opacity: 0.8;");
    }

    private void handleSave() {
        if (isInputValid()) {
            patient.setPatientId(Long.parseLong(patientIdField.getText()));
            patient.setFirstName(firstNameField.getText());
            patient.setSurname(surnameField.getText());
            patient.setAddress(addressField.getText());
            patient.setTelephoneNo(Long.parseLong(telephoneNoField.getText()));
            saveClicked = true;
            closeDialog();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            errorMessage += "First name is required!\n";
        }
        if (surnameField.getText() == null || surnameField.getText().trim().isEmpty()) {
            errorMessage += "Surname is required!\n";
        }
        if (addressField.getText() == null || addressField.getText().trim().isEmpty()) {
            errorMessage += "Address is required!\n";
        }
        if (telephoneNoField.getText() == null || telephoneNoField.getText().trim().isEmpty()) {
            errorMessage += "Telephone number is required!\n";
        } else {
            try {
                Long.parseLong(telephoneNoField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid telephone number format!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    private void handleCancel() {
        saveClicked = false;
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public Patient getPatient() {
        return patient;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}