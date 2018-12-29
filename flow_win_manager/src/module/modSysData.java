package module;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class modSysData {
	public final double sysVer = 0.51;
	private static modDBMS clsDBMS;
	private static boolean sysLogin = false;
	private static String sysUserID;
	private static String sysUserKey;

	private static Timer Session_Timer = null;
	private static TimerTask Session_task = null;

	public modSysData() {
		System.out.println("[Flow _ System Data Module] 시스템 데이터 모듈이 로드되었습니다.");
	}

	public void setLoginValue(boolean valLogin) {
		this.sysLogin = valLogin;
	}

	public static boolean getLoginValue() {
		return sysLogin;
	}

	public void setLogout() {
		try {
			this.sysLogin = false;
			this.sysUserID = null;
			this.sysUserKey = null;

			Session_Timer.cancel();
			Session_Timer = null;
			Session_task = null;
		}catch(Exception e) {}
	}

	public String getUserID() {
		return this.sysUserID;
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
	public static int setLogin(String id, String pw) {
		Connection con = null;
		con = clsDBMS.Connecter();

		try {
			String usrID = id, usrPW = pw;
			String query;
			String user_mac = getMacAddr();
			String user_ip = getIPAddr();
			String time = getTime();

			Scanner sc = new Scanner(System.in);

			con = clsDBMS.Connecter();
			Statement stmt = con.createStatement();

			if (!usrID.equals("admin"))
				return 0;

			query = "select u_id from user_info where (u_id='" + usrID + "') and (u_pw = '" + usrPW + "');";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				if (rs.getString("u_id").equals(usrID) == true) {

					String use_session = "select count(*) from session where u_id ='" + usrID + "';";
					ResultSet result2 = stmt.executeQuery(use_session);

					stmt.executeUpdate("insert into login_log(u_id,mac,ip,time) values('" + usrID + "','" + user_mac
							+ "','" + user_ip + "','" + time + "');");

					sysUserID = usrID;
					return 1;
				} else
					return 0;
			}

		} catch (Exception e) {
			System.out.println(e);
			return 0;
		}

		return 0;
	}

	// user List 불러오기
	public static String[][] loadUsrList() {

		try {
			if(getLoginValue() == true) {
				String query;
				String[][] sessionData = null;
				int tmpCount = 0;
	
				Connection con = clsDBMS.Connecter();
				Statement stmt = con.createStatement();
	
				ResultSet rs = stmt.executeQuery("select count(*) from session;");
				if(rs.next()) {
					tmpCount = rs.getInt(1);
					sessionData = new String[tmpCount][2]; // 가변 배열 만듬
				}
	
				rs = stmt.executeQuery("select u_id,time from session;");
				
				int tmpCnt=0;;
				while(rs.next()) {
					sessionData[tmpCnt][0] = new String(rs.getString("u_id"));
					sessionData[tmpCnt][1] = new String(rs.getString("time"));
					System.out.println("ID는 "+sessionData[tmpCnt][0]+", 시간은 "+sessionData[tmpCnt][1]);
					tmpCnt++;
				}
	
				return sessionData;
			}else return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

	}

	public static String loadUsrDeleteList() {
		try {
			if(getLoginValue() == true) {
				String query1, query2;
				String[][] lstSession;
				lstSession = loadUsrList();
				
				String[] sessionDelData = new String[lstSession.length];
				int delCnt = 0;
				
				ArrayList tmpList = new ArrayList();
	
				Connection con = clsDBMS.Connecter();
				Statement stmt = con.createStatement();
	
				ResultSet rs1;
				String sessionDelID="";
				
				for(int i=0; i < lstSession.length; i++) {
					long tmpSec = getDateToSecond(lstSession[i][1]);
					if (tmpSec >= 300) {
						sessionDelID+=lstSession[i][0];
						if(i<lstSession.length-1) sessionDelID+=", ";
						
						stmt.executeUpdate("delete from session where u_id='" + lstSession[i][0] + "';");
						delCnt++;
					}
				}


				if(sessionDelID.equals("") == true) {
					return String.format("삭제된 세션이 없습니다.");
				}				
				else {
					return String.format("삭제된 세션은 "+sessionDelID+" 입니다.");
				}
					
				
			}else{
				return String.format("삭제된 세션이 없습니다.");
			}
		} catch (Exception e) {
			return String.format("삭제된 세션이 없습니다.");
			
		}

	}

	public static long getDateToSecond(String s) {
		String start = s; // "2018-12-07 04:05:56";
		Calendar tempcal = Calendar.getInstance();

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startday = sf.parse(start, new ParsePosition(0));
		long startTime = startday.getTime(); // 현재의 시간 설정

		Calendar cal = Calendar.getInstance();
		Date endDate = cal.getTime();

		long endTime = endDate.getTime();
		long sec = (endTime - startTime) / 1000; // 초로 변환

		return sec;

	}	


}
