package com.legends.database;

import com.legends.model.Profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Profile createUser(String username, String password) {
        String sql = "INSERT INTO users (username, password, party_level, room_count) VALUES (?, ?, 1, 1)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    return new Profile(id, username, 1, 1);
                }
            }
            throw new RuntimeException("Could not create user.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Profile login(String username, String password) {
        String sql = "SELECT id, username, party_level, room_count FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Profile(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getInt("party_level"),
                            resultSet.getInt("room_count")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProgress(Profile profile) {
        String sql = "UPDATE users SET party_level = ?, room_count = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, profile.getPartyLevel());
            statement.setInt(2, profile.getRoomCount());
            statement.setInt(3, profile.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetProgress(Profile profile) {
        profile.setPartyLevel(1);
        profile.setRoomCount(1);
        updateProgress(profile);
    }
}
