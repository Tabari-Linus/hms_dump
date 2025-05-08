package lii.hospitaltrial.controller;

import javafx.fxml.FXML;
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

    private void handleSave() {
        if (isInputValid()) {
            patient = new Patient(
                    Long.parseLong(patientIdField.getText()),
                    firstNameField.getText(),
                    surnameField.getText(),
                    addressField.getText(),
                    Long.parseLong(telephoneNoField.getText())
            );
            saveClicked = true;
            closeDialog();
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

    private boolean isInputValid() {
        // Add validation logic here
        return true;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        patientIdField.setText(String.valueOf(patient.getPatientId()));
        firstNameField.setText(patient.getFirstName());
        surnameField.setText(patient.getSurname());
        addressField.setText(patient.getAddress());
        telephoneNoField.setText(String.valueOf(patient.getTelephoneNo()));
    }

    public Patient getPatient() {
        return patient;
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }
}