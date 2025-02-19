package org.drive.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "lb_user_db";
    private static final String USER = "root";
    private static final String PASS = "12345678";

    public static Connection getConnection() throws SQLException {

        // TODO: Check If Table ans Database exist
//        createDatabaseAndTableIfNotExists();
        return DriverManager.getConnection(DB_URL + DB_NAME, USER, PASS);
    }
}
