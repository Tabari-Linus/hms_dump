package lii.hospitaltrial.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.util.Duration;
import lii.hospitaltrial.databasecrud.DBConnection;
import lii.hospitaltrial.util.getTotal;

import java.sql.Connection;

public class DashboardController {
    @FXML private StackPane contentArea;
    @FXML private Button dashboardBtn;
    @FXML private Button doctorsBtn;
    @FXML private Button nursesBtn;
    @FXML private Button patientsBtn;
    @FXML private Button departmentsBtn;
    @FXML private Button operationsBtn;

    @FXML private Label totalDoctors;
    @FXML private Label totalNurses;
    @FXML private Label totalPatients;
    @FXML private Label totalDepartments;
    @FXML private Label totalWards;
    @FXML private Label totalAdmissions;

    @FXML
    private void handleDoctorsClick(MouseEvent event) {
        loadPage("Doctors");
    }

    @FXML
    private void initialize() {
        // Initialize navigation first
        setupNavigation();


        Platform.runLater(this::loadDashboardData);
    }

    private void loadDashboardData() {
        // Add null checks for all labels
        if (totalDoctors == null || totalNurses == null || totalPatients == null ||
                totalDepartments == null || totalWards == null || totalAdmissions == null) {
            return;
        }

        try (Connection connection = DBConnection.getConnection()) {

            updateCard(totalDoctors, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM doctor"));


            updateCard(totalNurses, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM nurse"));


            updateCard(totalPatients, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM patient"));


            updateCard(totalDepartments, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM department"));


            updateCard(totalWards, getTotal.getTotalEntries(connection, "SELECT COUNT(*) FROM ward"));


            updateCard(totalAdmissions, getTotal.getTotalEntries(connection,
                    "SELECT COUNT(*) FROM patientadmission WHERE date_discharged IS NULL"));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to load dashboard data: " + e.getMessage());
        }
    }

    private void updateCard(Label label, int newValue) {
        if (label == null) return;
        try {
            int oldValue = Integer.parseInt(label.getText());
            animateCountChange(label, oldValue, newValue);
        } catch (NumberFormatException e) {
            label.setText(String.valueOf(newValue));
        }
    }

    private void animateCountChange(Label label, int oldValue, int newValue) {
        IntegerProperty count = new SimpleIntegerProperty(oldValue);
        label.textProperty().bind(count.asString());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(count, oldValue)),
                new KeyFrame(Duration.millis(500), new KeyValue(count, newValue))
        );

        ScaleTransition st = new ScaleTransition(Duration.millis(200), label);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);

        timeline.setOnFinished(e -> {
            label.textProperty().unbind();
            label.setText(String.valueOf(newValue));
        });

        timeline.play();
        st.play();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupNavigation() {
        if (dashboardBtn != null) dashboardBtn.setOnAction(e -> loadPage("Dashboard"));
        if (doctorsBtn != null) doctorsBtn.setOnAction(e -> loadPage("Doctors"));
        if (nursesBtn != null) nursesBtn.setOnAction(e -> loadPage("Nurses"));
        if (patientsBtn != null) patientsBtn.setOnAction(e -> loadPage("Patients"));
        if (departmentsBtn != null) departmentsBtn.setOnAction(e -> loadPage("Departments"));
        if (operationsBtn != null) operationsBtn.setOnAction(e -> loadPage("Operations"));
    }

    private void loadPage(String page) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitaltrial/view/" + page + ".fxml"));
            Node pageContent = loader.load();
            contentArea.getChildren().setAll(pageContent);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load page: " + page);
        }
    }
}