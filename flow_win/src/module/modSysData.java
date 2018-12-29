package module;

import java.awt.TrayIcon.MessageType;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.spec.SecretKeySpec;

public class modSysData {
	public final double sysVer = 1.01;

	private static modCoreWindow clsParent;
	private static modDBMS clsDBMS;

	private static boolean sysLogin = false;
	private static String sysUserID;
	private static String sysUserKey;

	private static Timer Session_Timer = null;
	private static TimerTask Session_task = null;

	public modSysData(modCoreWindow clsParent, modDBMS clsDBMS) {
		this.clsParent = clsParent;
		this.clsDBMS = clsDBMS;
		System.out.println("[Flow _ System Data Module] 시스템 데이터 모듈이 로드되었습니다.");
	}

	public void setEncryInfo(String userid) { //String token
		this.sysLogin = true;
		this.sysUserID = userid;
		this.sysUserKey = this.sysUserKey;
	}

	public boolean getLoginValue() {
		return sysLogin;
	}

	public void setLogout() {
		try {
			Connection con = clsDBMS.Connecter();
			if (con != null) {
				Statement stmt = con.createStatement();
				String use_session = "DELETE FROM `session` where u_id = '" + sysUserID + "';";
				stmt.executeUpdate(use_session);

				this.sysLogin = false;
				this.sysUserID = null;
				this.sysUserKey = null;
				Session_Timer.cancel();
				Session_Timer = null;
				Session_task = null;

				System.out.println("[Flow _ Session Manager] 서버로부터 로그아웃 되었습니다.");
			}
		} catch (Exception e) {

		}
	}

	public String getUserID() {
		return this.sysUserID;
	}

	public String getUserKey() {
		return this.sysUserKey;
	}

	public int getDBConnect() {
		return clsDBMS.getConnect();
	}

	public static String getMacAddr() {
		String macAddr = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();

			/* NetworkInterface를 이용하여 현재 로컬 서버에 대한 하드웨어 어드레스를 가져오기 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			macAddr = "";
			for (int i = 0; i < mac.length; i++) {
				macAddr += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macAddr;

	}

	public static String getIPAddr() {
		String ipAddr = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ipAddr = addr.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipAddr;
	}

	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat date_f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return String.format(date_f.format(date).toString());
	}

	// 로그인 메소드 0이면 실패 1이면 성공
	public int setLogin(String id, String pw) {
		Connection con = null;
		con = clsDBMS.Connecter();
		if (con != null) {
			String user_id = id;
			String user_pw = pw;
			String user_mac = getMacAddr();
			String user_ip = getIPAddr();
			String time = getTime();

			try {
				String use_id = "select u_id,u_pw from user_info where u_id ='" + user_id + "';";
				Statement[] stmt = { con.createStatement(), con.createStatement(), con.createStatement(),
						con.createStatement(), con.createStatement(), con.createStatement()};

				ResultSet result = stmt[0].executeQuery(use_id);

				String use_session = "select count(*) from session where u_id ='" + user_id + "';";
				ResultSet result2 = stmt[1].executeQuery(use_session);

				if (result2.next()) {
					if (result2.getInt(1) == 0) {
						if (result.next()) {
							if (!result.getString("u_pw").equals(user_pw)) {
								return 0;
							} else {
								stmt[2].executeUpdate("insert into login_log(u_id,mac,ip,time) values('" + user_id
										+ "','" + user_mac + "','" + user_ip + "','" + time + "');");
								stmt[3].executeUpdate("insert into session(u_id,ip,time) values('" + user_id + "','"
										+ user_ip + "','" + time + "');");
								ResultSet Hashset = stmt[4]
										.executeQuery("select hashset from user_info where u_id = '" + user_id + "';");

								sysLogin = true;
								sysUserID = id;
								if (Hashset.next()) sysUserKey = Hashset.getString(1);

								System.out.println("[Flow _ Session Manager] 서버에 로그인 되었습니다.");

								Session_Timer = new Timer();
								Session_task = new TimerTask() {
									@Override
									public void run() {
										try {
											ResultSet rs = stmt[5].executeQuery(
													"select count(*) from session where u_id = '" + getUserID() + "';");
											if (rs.next()) {
												if (rs.getInt(1) == 0) {
													setLogout();
													clsParent.pnlMenu.setMenuNum(1);
													clsParent.clsTrayIcon.showAlert("Flow Companion", "세션이 종료되어 로그아웃 되었습니다. 다시 로그인해주세요.", MessageType.INFO);
												}
											} else {
												Connection con = clsDBMS.Connecter();
												Statement stmt = con.createStatement();
												String time = getTime();
												String user_session = "UPDATE `session` SET time ='" + time+"' WHERE u_id ='"+sysUserID+"';";
												stmt.executeUpdate(user_session);
												System.out.println("[Flow _ Session Manager] "+getTime()+" 기준으로 사용자 접속 세션이 갱신되었습니다.");
											}
										}catch(Exception e) {}
									}
								};
								Session_Timer.schedule(Session_task, 30000, 30000);
							}
							return 1;
						} else {
							con.close();
							return 0;
						}
					} else
						return 2; // 2면 세션이 있는거임
				}

			} catch (SQLException e) {
				System.out.println(e);
				return 0;
			}
		} // if문
		return 0;
	}

	public static int setRegister(String id, String pw, String pw2) {
		Connection con = null;
		con = clsDBMS.Connecter();

		// ---- if
		if (con != null) {

			String user_id = id;
			String user_pw = pw;
			String user_pw2 = pw2;
			String user_dedir = "C:/Flow/Files";
			String sync = "1";
			String hashset = "";

			// 랜덤 10자리 해쉬값 생성
			for (int i = 0; i < 8; i++) {
				Random rnd = new Random();
				String randomStr = String.valueOf((char) ((int) (rnd.nextInt(26)) + 97));
				hashset += randomStr;
			}
			try {
				String use_id = "select count(*) from user_info where u_id ='" + user_id + "';";
				Statement stmt = con.createStatement();
				ResultSet result = stmt.executeQuery(use_id);

				if (result.next()) {
					if (result.getInt(1) != 0) {
						return 1;
					} else if (!user_pw.equals(user_pw2)) {
						return 2;
					} else {
						stmt.executeUpdate("insert into user_info(u_id,u_pw,defaultdir,sync,hashset) values('" + user_id
								+ "','" + user_pw + "','" + user_dedir + "','" + sync + "','" + hashset + "');");
						return 0;
					}
				} // 첫번째 if

				con.close();

			} catch (SQLException e) {
				System.out.println("[Flow _ Session Manager] 스크립트를 처리하는 동안 오류가 발생 했습니다.");
				return 3;
			}
		} // if문
		return 3;
	}

}
