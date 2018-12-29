package module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

public class modSocket {
	private static modCoreWindow clsParent;
	private static modSysData clsSysData;

	private static ServerSocket socServer = null;
	private static Socket socClient = null;

	private static Timer Share_Timer = null;
	private static TimerTask Share_Task = null;

	private double tmpPercent;

	public modSocket(modCoreWindow clsParent, modSysData clsSysData){
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
	}

	private void setPercent(double value) {
		this.tmpPercent = value;
	}

	public double getPercent() {
		double tmpValue = this.tmpPercent;
		if(this.tmpPercent>=100.0) this.tmpPercent = (double)0.0;
		return tmpValue;
	}

	public void startReceive() {
		try {
			System.out.println("[Flow _ Share Manager] 다른 사용자가 공유한 파일을 받기 위해 대기중입니다.");
			Share_Timer = new Timer();
			Share_Task = new TimerTask() {
				@Override
				public void run() {
					try {
						if(clsSysData.getLoginValue()==true) {
							Connection con = clsParent.clsDBMS.Connecter();
							Statement stmt = con.createStatement();
							ResultSet rsMain = stmt.executeQuery("select u_id, issuc from share_list where issuc = 0 and rcv_id ='"
									+ clsSysData.getUserID() + "';"); // 내아이디가 share_list 에 있는지 확인
							if (rsMain.next()) {
								if (rsMain.getInt("issuc") == 0) {
									// 파일을 받을 것인지 말 것인지 묻기
									int valChoice = JOptionPane.showConfirmDialog(null, rsMain.getInt("u_id")+"님께서 파일을 공유하셨습니다.\n공유한 파일을 받으시겠습니까?", "파일 공유", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
									if(valChoice == 0) {
										System.out.println("[Flow _ Share Manager] 공유한 파일을 받기 위해 준비합니다.");
										clsParent.pnlMenu.setMenuNum(3);
										clsParent.pnlShareFile.setReceiveMode(1);
										receiveFile();
										clsParent.pnlShareFile.setReceiveMode(0);
										clsParent.pnlMenu.setMenuNum(0);
										con.close();
									}else {
										stmt = null;
							            stmt = con.createStatement();
							            stmt.executeUpdate("update share_list set issuc = 2 where rcv_id ='" + clsSysData.getUserID() + "' and issuc = 0;");
									}
								}
							}
						}
					} catch (Exception e) { }
				}
			};
			Share_Timer.schedule(Share_Task, 1000, 1000);
		} catch (Exception e) {
		}
	}

	// Server = 접속된 Client로부터 Server가 파일을 받는 형식
	public void receiveFile(){
		try {
			socServer = new ServerSocket(7777);  //포트 7777로 서버를 개통합니다.
			System.out.println("[Flow _ Share Manager] 파일을 받기 위해 Server를 엽니다.");
			socClient = socServer.accept();                       //클라이언트의 접속을 받습니다.
			System.out.println("[Flow _ Share Manager] Client가 접속하여 Client가 보내는 파일을 수신합니다.");
			InputStream in = null;                       
			FileOutputStream out = null;
			in = socClient.getInputStream();                //클라이언트로 부터 바이트 단위로 입력을 받는 InputStream을 얻어와 개통합니다.
			DataInputStream din = new DataInputStream(in);  //InputStream을 이용해 데이터 단위로 입력을 받는 DataInputStream을 개통합니다.

			while(true) {
				int data = din.readInt();           // Int형 데이터를 전송받습니다.
				String filename = din.readUTF();            // String형 데이터를 전송받아 filename(파일의 이름으로 쓰일)에 저장합니다.
				String filepath = "";

				String[] tmpPath = filename.split("\\\\");
				for(int i=0; i<(tmpPath.length-1); i++) {
					filepath += tmpPath[i];
					if(i<tmpPath.length-2) filepath+="\\";
				}

				File file = new File(filepath);             // 입력받은 File의 이름으로 복사하여 생성합니다.
				if(!file.exists()){
					file.mkdirs(); // 디렉토리 생성 메소드
					System.out.println("[Flow _ Share Manager] 해당 경로에 파일이 없어서 경로에 맞게 생성했습니다.");
				}

				file = new File(filename);             // 입력받은 File의 이름으로 복사하여 생성합니다.
				out = new FileOutputStream(file);           //생성한 파일을 클라이언트로부터 전송받아 완성시키는 FileOutputStream을 개통합니다.

				int datas = data;                            //전송횟수, 용량을 측정하는 변수입니다.
				byte[] buffer = new byte[4];        //바이트단위로 임시저장하는 버퍼를 생성합니다.
				int len;                               //전송할 데이터의 길이를 측정하는 변수입니다.


				for(;data>0;data--){                   //전송받은 data의 횟수만큼 전송받아서 FileOutputStream을 이용하여 File을 완성시킵니다.
					len = in.read(buffer);
					out.write(buffer,0,len);

					double tmpPer = (double)datas-data;
					tmpPer = (tmpPer/datas)*100;
					setPercent(tmpPer);
					clsParent.pnlShareFile.setProgressValue((int)tmpPer);

					String tmpStrPer = String.format("%.2f" , tmpPer);
					System.out.println("[Flow _ Share Manager] 전체 파일 크기 중 "+tmpStrPer+"% 수신됨");
				}

				setPercent((double)100.0);

				out.flush();
				out.close();
				
				in.close();
	            socServer.close();
	            socClient.close();
	            
	            Connection con = clsParent.clsDBMS.Connecter();
	            Statement stmt = con.createStatement();
	            stmt.executeUpdate("update share_list set issuc = 1 where rcv_id ='" + clsSysData.getUserID() + "' and issuc = 0;");
				
				System.out.println("[Flow _ Share Manager] 파일 수신이 완료되었습니다.");
			}
		}catch (Exception e) {
		}
	}

	// Client = 서버로 접속해서 Server로 파일을 보내는 형식
	public boolean sendFile(String tmpIP, String tmpFile){
		OutputStream out;
		FileInputStream fin;

		try{
			socClient = new Socket(tmpIP, 7777); //127.0.0.1은 루프백 아이피로 자신의 아이피를 반환해주고,
			System.out.println("[Flow _ Share Manager] Server에 접속 했습니다.");        //11111은 서버접속 포트입니다.
			out = socClient.getOutputStream();                 //서버에 바이트단위로 데이터를 보내는 스트림을 개통합니다.
			DataOutputStream dout = new DataOutputStream(out); //OutputStream을 이용해 데이터 단위로 보내는 스트림을 개통합니다.

			while(true){
				fin = new FileInputStream(new File(tmpFile)); //FileInputStream - 파일에서 입력받는 스트림을 개통합니다.

				byte[] buffer = new byte[4];        //바이트단위로 임시저장하는 버퍼를 생성합니다.
				int len;                               //전송할 데이터의 길이를 측정하는 변수입니다.
				int data=0;                            //전송횟수, 용량을 측정하는 변수입니다.

				while( (len = fin.read(buffer))>0 ){     //FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
					data++;                        //데이터의 양을 측정합니다.
				}

				int datas = data;                      //아래 for문을 통해 data가 0이되기때문에 임시저장한다.

				fin.close();
				fin = new FileInputStream(tmpFile);   //FileInputStream이 만료되었으니 새롭게 개통합니다.
				dout.writeInt(data);                   //데이터 전송횟수를 서버에 전송하고,
				dout.writeUTF(tmpFile);               //파일의 이름을 서버에 전송합니다.

				len = 0;

				for(; data>0; data--){                   // 데이터를 읽어올 횟수만큼 FileInputStream에서 파일의 내용을 읽어옵니다.
					len = fin.read(buffer);        // FileInputStream을 통해 파일에서 입력받은 데이터를 버퍼에 임시저장하고 그 길이를 측정합니다.
					out.write(buffer,0,len);       // 서버에게 파일의 정보(1kbyte만큼보내고, 그 길이를 보냅니다.

					double tmpPer = (double)datas-data;
					tmpPer = (tmpPer/datas)*100;
					setPercent(tmpPer);
					clsParent.pnlShareFile.setProgressValue((int)tmpPer);

					String tmpStrPer = String.format("%.2f" , tmpPer);
					System.out.println("[Flow _ Share Manager] 전체 파일 크기 중 "+tmpStrPer+"% 전송됨");
				}

				setPercent((double)100.0);

				out.flush();
				out.close();
				System.out.println("[Flow _ Share Manager] 파일 전송이 완료되었습니다.");
				return true;
			}
		}catch(Exception e){
			// e.printStackTrace();
			return false;
		}
	}
}