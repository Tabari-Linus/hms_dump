package lii.hospitaltrial.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.application.Platform;

public class DashboardController {
    @FXML private StackPane contentArea;
    @FXML private Button dashboardBtn;
    @FXML private Button doctorsBtn;
    @FXML private Button nursesBtn;
    @FXML private Button patientsBtn;
    @FXML private Button departmentsBtn;
    @FXML private Button operationsBtn;

    @FXML
    private void initialize() {
        // Wait for FXML components to be fully initialized
        Platform.runLater(() -> {
            // Setup navigation first
            setupNavigation();
            // Then load the dashboard
            loadDashboard();
        });
    }

    private void setupNavigation() {
        if (dashboardBtn != null) {
            dashboardBtn.setOnAction(e -> loadDashboard());
        }
        if (doctorsBtn != null) {
            doctorsBtn.setOnAction(e -> loadPage("Doctors"));
        }
        if (nursesBtn != null) {
            nursesBtn.setOnAction(e -> loadPage("Nurses"));
        }
        if (patientsBtn != null) {
            patientsBtn.setOnAction(e -> loadPage("Patients"));
        }
        if (departmentsBtn != null) {
            departmentsBtn.setOnAction(e -> loadPage("Departments"));
        }
        if (operationsBtn != null) {
            operationsBtn.setOnAction(e -> loadPage("Operations"));
        }
    }

    private void loadDashboard() {
        try {
            if (contentArea != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitaltrial/view/Dashboard.fxml"));
                Node dashboard = loader.load();
                contentArea.getChildren().setAll(dashboard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String page) {
        try {
            if (contentArea != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lii/hospitaltrial/view/" + page + ".fxml"));
                Node pageContent = loader.load();
                contentArea.getChildren().setAll(pageContent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}