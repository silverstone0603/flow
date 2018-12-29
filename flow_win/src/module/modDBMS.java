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
			String driverName = "com.mysql.cj.jdbc.Driver"; // 드라이버 이름 지정
			String dbURL = "jdbc:mysql://" + DB_Host + ":3306/" + DB_Name + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"; // URL

			// 지정
			Class.forName(driverName); // 드라이버 로드

			con = DriverManager.getConnection(dbURL, DB_Id, DB_Pw); // 연결
			// System.out.println("[Flow _ DBMS Manager] DB 접속에 성공하였습니다.");
		} catch (Exception e) {
			System.out.println("[Flow _ DBMS Manager] DB 접속에 실패하였습니다.");
			System.out.println("[Flow _ DBMS Manager] 오류 내용 : "+e+"]\n");
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
			String driverName = "com.mysql.cj.jdbc.Driver"; // 드라이버 이름 지정
			String dbURL = "jdbc:mysql://" + DB_Host + ":3306/" + DB_Name + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC"; // URL

			// 지정
			Class.forName(driverName); // 드라이버 로드

			con = DriverManager.getConnection(dbURL, DB_Id, DB_Pw); // 연결
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
}
