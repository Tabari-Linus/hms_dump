package lii.hospitaltrial.databasecrud;

import lii.hospitaltrial.model.PatientAdmission;
import lii.hospitaltrial.model.PatientTreatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PatientAdmissionCRUD {

    public boolean insertAdmission(PatientAdmission admission) {
        String sql = "INSERT INTO patientadmission (id, patient_id, ward_id, bed_number, diagnosis, date_admitted, date_discharged) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, admission.getId());
                statement.setLong(2, admission.getPatientId());
                statement.setLong(3, admission.getWardId());
                statement.setInt(4, admission.getBedNumber());
                statement.setString(5, admission.getDiagnosis());
                statement.setDate(6, new java.sql.Date(admission.getDateAdmitted().getTime()));
                statement.setDate(7, admission.getDateDischarged() != null ? new java.sql.Date(admission.getDateDischarged().getTime()) : null);
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

    public List<PatientAdmission> getAllPatientAdmissions() {
        List<PatientAdmission> admissions = new ArrayList<>();
        String sql = "SELECT * FROM patientadmission";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                PatientAdmission admission = new PatientAdmission(
                        resultSet.getLong("id"),
                        resultSet.getLong("patient_id"),
                        resultSet.getLong("ward_id"),
                        resultSet.getInt("bed_number"),
                        resultSet.getString("diagnosis"),
                        resultSet.getDate("date_admitted"),
                        resultSet.getDate("date_discharged")
                );
                admissions.add(admission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admissions;
    }

    public boolean updateAdmission(PatientAdmission admission) {
        String sql = "UPDATE patientadmission SET patient_id = ?, ward_id = ?, bed_number = ?, diagnosis = ?, date_admitted = ?, date_discharged = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, admission.getPatientId());
                statement.setLong(2, admission.getWardId());
                statement.setInt(3, admission.getBedNumber());
                statement.setString(4, admission.getDiagnosis());
                statement.setDate(5, new java.sql.Date(admission.getDateAdmitted().getTime()));
                statement.setDate(6, admission.getDateDischarged() != null ? new java.sql.Date(admission.getDateDischarged().getTime()) : null);
                statement.setLong(7, admission.getId());
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

    public boolean deletePatientAdmission(long id) {
        String sql = "DELETE FROM patientadmission WHERE id = ?";
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

    public PatientAdmission getAdmissionById(Long patientId) throws Exception {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null");
        }

        final String sql = "SELECT * FROM patientadmission WHERE patient_id = ?";

        PatientAdmission admission = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    admission = new PatientAdmission(
                            resultSet.getLong("id"),
                            resultSet.getLong("patient_id"),
                            resultSet.getLong("ward_id"),
                            resultSet.getInt("bed_number"),
                            resultSet.getString("diagnosis"),
                            resultSet.getDate("date_admitted"),
                            resultSet.getDate("date_discharged")
                    );

                }
            }
        } catch (SQLException e) {
            throw new Exception("Error retrieving treatments for patient ID: " + patientId, e);
        }

        return admission;
    }

    public Collection<Object> getAdmissionsByWard(long wardId) {
        String sql = "SELECT * FROM patientadmission WHERE ward_id = ?";
        List<PatientAdmission> admissions = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, wardId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PatientAdmission admission = new PatientAdmission(
                            resultSet.getLong("id"),
                            resultSet.getLong("patient_id"),
                            resultSet.getLong("ward_id"),
                            resultSet.getInt("bed_number"),
                            resultSet.getString("diagnosis"),
                            resultSet.getDate("date_admitted"),
                            resultSet.getDate("date_discharged")
                    );
                    admissions.add(admission);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.singleton(admissions);
    }
}