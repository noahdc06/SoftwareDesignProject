package com.legends.database;

import com.legends.model.Profile;
import java.sql.*;

public class UserDAO {

    public void createUser(Profile profile) throws Exception {
        Connection conn = DatabaseConnection.getConnection();

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, profile.getUsername());
        stmt.setString(2, profile.getPassword());
        stmt.executeUpdate();

        conn.close();
        System.out.println("User saved to database.");
    }

    public Profile login(String username, String password) throws Exception {
        Connection conn = DatabaseConnection.getConnection();

        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            conn.close();
            System.out.println("Login successful.");
            return new Profile(username, password);
        }

        conn.close();
        return null;
    }
}