package org.drive.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "lb_user_db";
    private static final String USER = "root";
    private static final String PASS = "12345678";
    private static final int POOLSIZE = 10;

    public static Connection getConnection() throws SQLException {
        createDatabaseAndTableIfNotExists();
        return DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS);
    }

    private static void createDatabaseAndTableIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database '" + DB_NAME + "' is ready.");

            stmt.executeUpdate("USE " + DB_NAME);

            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    _id VARCHAR(255) PRIMARY KEY,
                    username VARCHAR(255) UNIQUE NOT NULL,
                    password_hash VARCHAR(255) NOT NULL,
                    role VARCHAR(50),
                    used_storage FLOAT DEFAULT 0
                );
            """;
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'users' is ready.");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create database or table", e);
        }
    }
}
