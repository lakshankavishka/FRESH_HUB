package lk.ijse.model;

import lk.ijse.DB.DbConnection;
import lk.ijse.dto.RegistrationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationModel {

    public boolean registerUser(RegistrationDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO User VALUES (?, ?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getUser_name());
        ptsm.setString(2, dto.getPassword());

        return ptsm.executeUpdate() > 0;
    }

    public boolean isValidUser(String UserName, String Password) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM User WHERE UserName = ? AND Password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, UserName);
        ptsm.setString(2,Password);

        ResultSet resultSet = ptsm.executeQuery();

        return resultSet.next();
    }

    public RegistrationDto getUserInfo(String UserName) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM User WHERE UserName = ?";
        try (PreparedStatement ptsm = connection.prepareStatement(sql)) {
            ptsm.setString(1, UserName);

            try (ResultSet resultSet = ptsm.executeQuery()) {
                if (resultSet.next()) {
                    String retrievedUserName = resultSet.getString("UserName");
                    String retrievedPassword = resultSet.getString("Password");

                    return new RegistrationDto(retrievedUserName, retrievedPassword);
                }
            }
        }
        return null; // User isn't found
    }

    public boolean check(String UserName, String Password) throws SQLException {
        return isValidUser(UserName,Password);
    }
}
