package lii.hospitaltrial.databasecrud;

import lii.hospitaltrial.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCRUD {

    public boolean insertEmployee(Employee employee) {
        String sql = "INSERT INTO employee (first_name, surname, address, telephone_no) " +
                "VALUES (?, ?, ?, ?) RETURNING employee_id";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, employee.getFirstName());
                statement.setString(2, employee.getSurname());
                statement.setString(3, employee.getAddress());
                statement.setLong(4, employee.getTelephoneNo());

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        employee.setEmployeeId(rs.getLong(1));
                        connection.commit();
                        return true;
                    }
                }
                connection.rollback();
                return false;
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
    public long getLastEmployeeId() {
        String sql = "SELECT MAX(employee_id) FROM employee";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return 100; // Starting ID if no employees exist
        } catch (SQLException e) {
            e.printStackTrace();
            return 100;
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getLong("employee_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("surname"),
                        resultSet.getString("address"),
                        resultSet.getLong("telephone_no")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Employee getEmployeeById(Long employeeId) throws Exception {
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        final String sql = "SELECT * FROM employee WHERE employee_id = ?";

        Employee employee = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, employeeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    employee = new Employee(
                            resultSet.getLong("employee_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("surname"),
                            resultSet.getString("address"),
                            resultSet.getLong("telephone_no")
                    );

                }
            }
        } catch (SQLException e) {
            throw new Exception("Error retrieving treatments for employee ID: " + employeeId, e);
        }

        return employee;
    }

    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employee SET first_name = ?, surname = ?, address = ?, telephone_no = ? WHERE employee_id = ?";
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, employee.getFirstName());
                statement.setString(2, employee.getSurname());
                statement.setString(3, employee.getAddress());
                statement.setLong(4, employee.getTelephoneNo());
                statement.setLong(5, employee.getEmployeeId());
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

    public boolean deleteEmployee(long employeeId) {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
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