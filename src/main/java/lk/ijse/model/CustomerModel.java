package lk.ijse.model;

import lk.ijse.DB.DbConnection;
import lk.ijse.dto.CustomerDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    private String splitCustomerID(String currentCustomerID){
        if (currentCustomerID != null){
            String [] split = currentCustomerID.split("00");

            int id = Integer.parseInt(split[1]);
            id++;
            return "C00" + id;
        }else {
            return "C001";
        }
    }

    public String generateNextCustomer() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT customerId FROM Customer ORDER BY customerId DESC LIMIT 1";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();
        if (resultSet.next()){
            return splitCustomerID(resultSet.getString(1));
        }
        return splitCustomerID(null);
    }

    public boolean save(CustomerDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO Customer VALUES (?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);

        ptsm.setString(1,dto.getCustomerId());
        ptsm.setString(2,dto.getName());
        ptsm.setString(3, String.valueOf(dto.getContact()));

        return ptsm.executeUpdate() > 0;
    }

    public List<CustomerDto> getAllCustomer() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql ="SELECT * FROM Customer";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        ArrayList<CustomerDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new CustomerDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getInt(3)
                            )
            );
        }
        return dtoList;
    }

    public boolean updateCustomer(CustomerDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE Customer SET CustomerName = ?, CustomerContact = ? WHERE CustomerId =?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getName());
        ptsm.setString(2, dto.getCustomerId());
        ptsm.setInt(3, dto.getContact());

        return ptsm.executeUpdate() > 0;
    }

    public CustomerDto searchCustomer(String searchId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM Customer WHERE CustomerId = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchId);
        ResultSet resultSet = ptsm.executeQuery();

        CustomerDto dto = null;
        if (resultSet.next()){
            String customer_id = resultSet.getString(1);
            String customer_name = resultSet.getString(2);
            int customer_contact = resultSet.getInt(3);

            dto = new CustomerDto(customer_id,customer_name,customer_contact);
        }
        return dto;
    }

    public CustomerDto searchCustomerByPhoneNumber(String phoneNumber) throws SQLException {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "SELECT * FROM Customer WHERE CustomerContact = ?";
            PreparedStatement ptsm = connection.prepareStatement(sql);
            ptsm.setString(1, phoneNumber);
            ResultSet resultSet = ptsm.executeQuery();

            CustomerDto dto = null;
            if (resultSet.next()) {
                String customer_id = resultSet.getString(1);
                String customer_name = resultSet.getString(2);
                int customer_phoneNumber = Integer.parseInt(resultSet.getString(3));

                dto = new CustomerDto(customer_id,customer_name,customer_phoneNumber);
            }
            return dto;
    }

    public boolean deleteCustomers(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM Customer WHERE CustomerId = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,id);
        return ptsm.executeUpdate() > 0;
    }
}
