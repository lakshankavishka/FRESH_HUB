package lk.ijse.model;

import lk.ijse.DB.DbConnection;
import lk.ijse.dto.EmployeeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    private String splitEmployeeID(String currentEmployeeID) {
        if (currentEmployeeID != null) {
            String[] split = currentEmployeeID.split("00");
            if (split.length > 1) {

                int id = Integer.parseInt(split[1]);
                id++;
                return "E00" + id;
            } else {
                return "E001";
            }
        } else {
            return "Invalid Employee Id";
        }
    }
    public String generateNextEmployeeID() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT Employee_id FROM Employee ORDER BY Employee_id DESC LIMIT 1";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();
        if (resultSet.next()){
            return splitEmployeeID(resultSet.getString(1));
        }
        return splitEmployeeID(null);
    }

    public boolean saveEmployee(EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO Employee VALUES (?,?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getEmployee_id());
        ptsm.setString(2, dto.getName());
        ptsm.setString(3,dto.getAddress());
        ptsm.setString(4, dto.getPhone_number());

        return ptsm.executeUpdate() > 0;
    }

    public EmployeeDto searchEmployeeByID(String searchID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM Employee WHERE employee_id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchID);
        ResultSet resultSet = ptsm.executeQuery();

        EmployeeDto dto = null;
        if (resultSet.next()){
            String Employee_id = resultSet.getString(1);
            String Employee_name = resultSet.getString(2);
            String Employee_address = resultSet.getString(3);
            String Employee_phone_number = resultSet.getString(4);

            dto = new EmployeeDto(Employee_id, Employee_name,  Employee_address,
                    Employee_phone_number);
        }
        return dto;
    }

    public EmployeeDto searchEmployeeByPhoneNumber(String searchPhoneNumber) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM Employee WHERE phone_number = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchPhoneNumber);
        ResultSet resultSet = ptsm.executeQuery();

        EmployeeDto dto = null;
        if (resultSet.next()){
            String Employee_id = resultSet.getString(1);
            String Employee_Name = resultSet.getString(2);
            String Employee_address = resultSet.getString(4);
            String Employee_phone_number = resultSet.getString(5);

            dto = new EmployeeDto(Employee_id, Employee_Name, Employee_address,
                    Employee_phone_number);
        }
        return dto;
    }

    public boolean updateEmployee(EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql ="UPDATE Employee SET  name= ?,address = ?,phone_number = ? WHERE employee_id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);

        ptsm.setString(1, dto.getName());
        ptsm.setString(2, dto.getAddress());
        ptsm.setString(2,dto.getPhone_number());
        ptsm.setString(4, dto.getEmployee_id());

        return ptsm.executeUpdate() > 0;
    }

    public boolean deleteEmployee(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM employee WHERE employee_id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,id);
        return ptsm.executeUpdate() > 0;
    }

    public List<EmployeeDto> getAllEmployees() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM Employee";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        ArrayList<EmployeeDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new EmployeeDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return dtoList;
    }
}
