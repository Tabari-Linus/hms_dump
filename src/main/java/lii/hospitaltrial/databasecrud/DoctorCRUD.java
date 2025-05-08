package lii.hospitaltrial.databasecrud;

import lii.hospitaltrial.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorCRUD {

    public boolean insertDoctor(Doctor doctor) {
        String sql = "INSERT INTO doctor (employee_id, speciality_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, doctor.getEmployeeId());
                statement.setLong(2, doctor.getSpecialityId());
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

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctor";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                        resultSet.getLong("employee_id"),
                        resultSet.getLong("speciality_id")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctor SET speciality_id = ? WHERE employee_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, doctor.getSpecialityId());
                statement.setLong(2, doctor.getEmployeeId());
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

    public boolean deleteDoctor(long employeeId) {
        String sql = "DELETE FROM doctor WHERE employee_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, employeeId);
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