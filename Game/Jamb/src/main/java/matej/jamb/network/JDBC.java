package matej.jamb.network;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBC {

	static final String JDBC_DRIVER = "org.postgresql.Driver";  
	static final String DB_URL = "jdbc:postgresql://localhost:5432/jambDB";
	static final String USER = "postgres";
	static final String PASS = "pg1234567890";

	static Connection conn;

	public static void connectToDatabase() {
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int executeStatement(String statement) {
		Statement stmt = null;
		int input = 0;
		try {
			stmt = conn.createStatement();
			String sql = statement;
			if (statement.startsWith("SELECT")) {
				ResultSet rs = stmt.executeQuery(sql);
				if(rs.next()) {
					input = Integer.valueOf(rs.getString("option"));
				}
			} else {
				stmt.executeUpdate(sql);
			}
			//			System.out.println("Statement executed successfully...");
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
				try {
					if(conn!=null)
						conn.close();
				} catch(SQLException se) {
					se.printStackTrace();
				}
			}
			//			System.out.println("Goodbye!");
		}
		return input;
	}
}