package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getConnection() throws SQLException {
        final String url = "jdbc:postgresql://192.168.27.66:5432/product_management_db";
        final String user = "product_manager_user";
        final String password = "123456";

        return DriverManager.getConnection(url, user, password);
    }

    public void closeConnection(Connection conn) {
        try{
            conn.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
