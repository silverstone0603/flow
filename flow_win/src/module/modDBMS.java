package module;

import java.sql.Connection;
import java.sql.DriverManager;
import com.mysql.cj.*;
import com.mysql.cj.jdbc.*;


public class modDBMS {
	public static Connection Connecter() {
		String DB_Host = "14.45.100.205";
		String DB_Name = "java";
		String DB_Id = "java";
		String DB_Pw = "Flow_java123";

		Connection con = null;
		try {
			String driverName = "com.mysql.cj.jdbc.Driver"; // ����̹� �̸� ����
			String dbURL = "jdbc:mysql://" + DB_Host + ":3306/" + DB_Name + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"; // URL

			// ����
			Class.forName(driverName); // ����̹� �ε�

			con = DriverManager.getConnection(dbURL, DB_Id, DB_Pw); // ����
			// System.out.println("[Flow _ DBMS Manager] DB ���ӿ� �����Ͽ����ϴ�.");
		} catch (Exception e) {
			System.out.println("[Flow _ DBMS Manager] DB ���ӿ� �����Ͽ����ϴ�.");
			System.out.println("[Flow _ DBMS Manager] ���� ���� : "+e+"]\n");
			return con;
		}
		return con;
	}
	
	public static int getConnect() {
		String DB_Host = "14.45.100.205";
		String DB_Name = "java";
		String DB_Id = "java";
		String DB_Pw = "Flow_java123";

		Connection con = null;
		try {
			String driverName = "com.mysql.cj.jdbc.Driver"; // ����̹� �̸� ����
			String dbURL = "jdbc:mysql://" + DB_Host + ":3306/" + DB_Name + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"; // URL

			// ����
			Class.forName(driverName); // ����̹� �ε�

			con = DriverManager.getConnection(dbURL, DB_Id, DB_Pw); // ����
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
}
