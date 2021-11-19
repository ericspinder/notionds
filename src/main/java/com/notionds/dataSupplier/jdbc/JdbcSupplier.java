package com.notionds.dataSupplier.jdbc;

import com.notionds.dataSupplier.NotionDs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcSupplier implements NotionDs.Supplier_I {

    private String name;
    private String url;
    private String username;
    private String password;

    public JdbcSupplier(String name, String driverClassName, String url, String username, String password) {
        this.name = name;
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



    @Override
    public String getName() {
        return this.name;
    }

    public static class H2 extends JdbcSupplier {

        public H2(String name, String url, String userName, String password) {
            super(name, "org.h2.Driver", url, userName, password);
        }
    }
}
