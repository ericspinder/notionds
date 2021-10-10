package com.notionds.dataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSupplier implements NotionDs.ConnectionSupplier_I {

    private String url;
    private String username;
    private String password;

    public ConnectionSupplier(String driverClassName, String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static class H2 extends ConnectionSupplier {

        public H2(String url, String userName, String password) {
            super("org.h2.Driver", url, userName, password);
        }
    }
}
