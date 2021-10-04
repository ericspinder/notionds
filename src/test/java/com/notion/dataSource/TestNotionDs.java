package com.notion.dataSource;

import com.notionds.dataSource.DatabasePool;
import com.notionds.dataSource.Options;
import com.notionds.dataSource.NotionDs;
import com.notionds.dataSource.connection.delegation.jdbcProxy.ConnectionWrapperFactory;
import com.notionds.dataSource.exceptions.ExceptionAdvice;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class TestNotionDs {

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
			Options options = new Options.Defaults()
			NotionDs vendorMain = new NotionDs(options, "test", new ConnectionWrapperFactory(options), new DatabasePool.Default(options), new ExceptionAdvice.KillExceptionOnException(options), null) {
				@Override
				protected Connection buildConnection() throws SQLException {
					return DriverManager.getConnection("jdbc:h2:~/test", "", "");
				}
			};
			vendorMain.c

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
