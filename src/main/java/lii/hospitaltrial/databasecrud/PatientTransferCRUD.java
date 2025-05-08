package lii.hospitaltrial.databasecrud;

import lii.hospitaltrial.model.PatientTransfer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientTransferCRUD {

    public boolean insertTransfer(PatientTransfer transfer) {
        String sql = "INSERT INTO patienttransfer (id, patient_id, from_ward, to_ward, reason, patient_admission_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, transfer.getId());
                statement.setLong(2, transfer.getPatientId());
                statement.setLong(3, transfer.getFromWard());
                statement.setLong(4, transfer.getToWard());
                statement.setString(5, transfer.getReason());
                statement.setLong(6, transfer.getPatientAdmissionId());
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

    public List<PatientTransfer> getAllPatientTransfers() {
        List<PatientTransfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM patienttransfer";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                PatientTransfer transfer = new PatientTransfer(
                        resultSet.getLong("id"),
                        resultSet.getLong("patient_id"),
                        resultSet.getLong("from_ward"),
                        resultSet.getLong("to_ward"),
                        resultSet.getString("reason"),
                        resultSet.getLong("patient_admission_id")
                );
                transfers.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transfers;
    }

    public boolean updatePatientTransfer(PatientTransfer transfer) {
        String sql = "UPDATE patienttransfer SET patient_id = ?, from_ward = ?, to_ward = ?, reason = ?, patient_admission_id = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, transfer.getPatientId());
                statement.setLong(2, transfer.getFromWard());
                statement.setLong(3, transfer.getToWard());
                statement.setString(4, transfer.getReason());
                statement.setLong(5, transfer.getPatientAdmissionId());
                statement.setLong(6, transfer.getId());
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

    public boolean deletePatientTransfer(long id) {
        String sql = "DELETE FROM patienttransfer WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
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
}