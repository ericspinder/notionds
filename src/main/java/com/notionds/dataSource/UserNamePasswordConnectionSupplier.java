package com.notionds.dataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class UserNamePasswordConnectionSupplier implements NotionDs.ConnectionSupplier_I {

    private final UUID uuid = UUID.randomUUID();
    private final String url;
    private final String username;
    private final String password;
    private final String testSQL;

    public UserNamePasswordConnectionSupplier(String driverClassName, String url, String username, String password, String testSQL) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.testSQL = testSQL;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException(classNotFoundException);
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public String getTestSQL() {
        return testSQL;
    }

}
