package com.notion.dataSource;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestVendorMain {

	@Test
	public void test() {
		Connection conn = null;
		Statement stmt = null;

		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:~/test", "", "");
			stmt = conn.createStatement();
			//stmt.execute("drop table user");
			stmt.execute("create table user(id int primary key, name varchar(100))");
			stmt.execute("insert into user values(1, 'hello')");
			stmt.execute("insert into user values(2, 'world')");
			ResultSet rs = stmt.executeQuery("select * from user");

			while (rs.next()) {
				System.out.println("id " + rs.getInt("id") + " name " + rs.getString("name"));
			}
			stmt.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
