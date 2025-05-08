package lii.hospitaltrial.databasecrud;

import lii.hospitaltrial.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientCRUD {

    public boolean insertPatient(Patient patient) {
        String sql = "INSERT INTO patient (patient_id, first_name, surname, address, telephone_no) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, patient.getPatientId());
                statement.setString(2, patient.getFirstName());
                statement.setString(3, patient.getSurname());
                statement.setString(4, patient.getAddress());
                statement.setLong(5, patient.getTelephoneNo());
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getLong("patient_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("surname"),
                        resultSet.getString("address"),
                        resultSet.getLong("telephone_no")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patient SET first_name = ?, surname = ?, address = ?, telephone_no = ? WHERE patient_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, patient.getFirstName());
                statement.setString(2, patient.getSurname());
                statement.setString(3, patient.getAddress());
                statement.setLong(4, patient.getTelephoneNo());
                statement.setLong(5, patient.getPatientId());
                boolean result = statement.executeUpdate() > 0;
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePatient(long patientId) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                // Delete associated admission records
                String deleteAdmissionsSql = "DELETE FROM patientadmission WHERE patient_id = ?";
                try (PreparedStatement deleteAdmissionsStmt = connection.prepareStatement(deleteAdmissionsSql)) {
                    deleteAdmissionsStmt.setLong(1, patientId);
                    deleteAdmissionsStmt.executeUpdate();
                }

                // Delete the patient
                String deletePatientSql = "DELETE FROM patient WHERE patient_id = ?";
                try (PreparedStatement deletePatientStmt = connection.prepareStatement(deletePatientSql)) {
                    deletePatientStmt.setLong(1, patientId);
                    boolean result = deletePatientStmt.executeUpdate() > 0;
                    connection.commit();
                    return result;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public long getNextPatientId() {
        String sql = "SELECT MAX(patient_id) FROM patient";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                long maxId = rs.getLong(1);
                return maxId > 0 ? maxId + 1 : 1001; // Start from 1001 if no existing records
            }
            return 1001; // Default starting ID
        } catch (SQLException e) {
            e.printStackTrace();
            return 1001;
        }
    }
}