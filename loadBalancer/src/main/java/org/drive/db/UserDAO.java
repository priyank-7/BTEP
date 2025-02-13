package org.drive.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserDAO {

    private static Connection connection;

    public UserDAO(Connection con) {
        connection = con;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (_id, username, password_hash, role, used_storage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPasswordHash());
            stmt.setString(4, user.getRole());
            stmt.setFloat(5, user.getUsedStorage());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to insert user: " + e.getMessage());
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return User.builder()
                        ._id(rs.getString("_id"))
                        .username(rs.getString("username"))
                        .passwordHash(rs.getString("password_hash"))
                        .role(rs.getString("role"))
                        .usedStorage(rs.getFloat("used_storage"))
                        .build();
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve user: " + e.getMessage());
            return null;
        }
        return null;
    }
}
