package com.notion.dataSource;

import com.notionds.dataSource.NotionDs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

class MutableConnectionSupplier implements NotionDs.ConnectionSupplier_I {

    private final UUID uuid = UUID.randomUUID();
    private String url;
    private String username;
    private String password;
    private String testSQL;

    public MutableConnectionSupplier(String driverClassName, String url, String username, String password, String testSQL) {
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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTestSQL(String testSQL) {
        this.testSQL = testSQL;
    }
}
