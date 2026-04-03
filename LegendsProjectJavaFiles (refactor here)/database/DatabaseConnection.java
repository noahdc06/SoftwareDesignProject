package com.legends.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    //MySQL database
    private static final String URL = "jdbc:mysql://localhost:3306/legends_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "thepassword";

    //Database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}