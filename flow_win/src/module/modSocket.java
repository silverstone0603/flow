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
			System.out.println("[Flow _ Share Manager] �ٸ� ����ڰ� ������ ������ �ޱ� ���� ������Դϴ�.");
			Share_Timer = new Timer();
			Share_Task = new TimerTask() {
				@Override
				public void run() {
					try {
						if(clsSysData.getLoginValue()==true) {
							Connection con = clsParent.clsDBMS.Connecter();
							Statement stmt = con.createStatement();
							ResultSet rsMain = stmt.executeQuery("select u_id, issuc from share_list where issuc = 0 and rcv_id ='"
									+ clsSysData.getUserID() + "';"); // �����̵� share_list �� �ִ��� Ȯ��
							if (rsMain.next()) {
								if (rsMain.getInt("issuc") == 0) {
									// ������ ���� ������ �� ������ ����
									int valChoice = JOptionPane.showConfirmDialog(null, rsMain.getInt("u_id")+"�Բ��� ������ �����ϼ̽��ϴ�.\n������ ������ �����ðڽ��ϱ�?", "���� ����", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
									if(valChoice == 0) {
										System.out.println("[Flow _ Share Manager] ������ ������ �ޱ� ���� �غ��մϴ�.");
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

	// Server = ���ӵ� Client�κ��� Server�� ������ �޴� ����
	public void receiveFile(){
		try {
			socServer = new ServerSocket(7777);  //��Ʈ 7777�� ������ �����մϴ�.
			System.out.println("[Flow _ Share Manager] ������ �ޱ� ���� Server�� ���ϴ�.");
			socClient = socServer.accept();                       //Ŭ���̾�Ʈ�� ������ �޽��ϴ�.
			System.out.println("[Flow _ Share Manager] Client�� �����Ͽ� Client�� ������ ������ �����մϴ�.");
			InputStream in = null;                       
			FileOutputStream out = null;
			in = socClient.getInputStream();                //Ŭ���̾�Ʈ�� ���� ����Ʈ ������ �Է��� �޴� InputStream�� ���� �����մϴ�.
			DataInputStream din = new DataInputStream(in);  //InputStream�� �̿��� ������ ������ �Է��� �޴� DataInputStream�� �����մϴ�.

			while(true) {
				int data = din.readInt();           // Int�� �����͸� ���۹޽��ϴ�.
				String filename = din.readUTF();            // String�� �����͸� ���۹޾� filename(������ �̸����� ����)�� �����մϴ�.
				String filepath = "";

				String[] tmpPath = filename.split("\\\\");
				for(int i=0; i<(tmpPath.length-1); i++) {
					filepath += tmpPath[i];
					if(i<tmpPath.length-2) filepath+="\\";
				}

				File file = new File(filepath);             // �Է¹��� File�� �̸����� �����Ͽ� �����մϴ�.
				if(!file.exists()){
					file.mkdirs(); // ���丮 ���� �޼ҵ�
					System.out.println("[Flow _ Share Manager] �ش� ��ο� ������ ��� ��ο� �°� �����߽��ϴ�.");
				}

				file = new File(filename);             // �Է¹��� File�� �̸����� �����Ͽ� �����մϴ�.
				out = new FileOutputStream(file);           //������ ������ Ŭ���̾�Ʈ�κ��� ���۹޾� �ϼ���Ű�� FileOutputStream�� �����մϴ�.

				int datas = data;                            //����Ƚ��, �뷮�� �����ϴ� �����Դϴ�.
				byte[] buffer = new byte[4];        //����Ʈ������ �ӽ������ϴ� ���۸� �����մϴ�.
				int len;                               //������ �������� ���̸� �����ϴ� �����Դϴ�.


				for(;data>0;data--){                   //���۹��� data�� Ƚ����ŭ ���۹޾Ƽ� FileOutputStream�� �̿��Ͽ� File�� �ϼ���ŵ�ϴ�.
					len = in.read(buffer);
					out.write(buffer,0,len);

					double tmpPer = (double)datas-data;
					tmpPer = (tmpPer/datas)*100;
					setPercent(tmpPer);
					clsParent.pnlShareFile.setProgressValue((int)tmpPer);

					String tmpStrPer = String.format("%.2f" , tmpPer);
					System.out.println("[Flow _ Share Manager] ��ü ���� ũ�� �� "+tmpStrPer+"% ���ŵ�");
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
				
				System.out.println("[Flow _ Share Manager] ���� ������ �Ϸ�Ǿ����ϴ�.");
			}
		}catch (Exception e) {
		}
	}

	// Client = ������ �����ؼ� Server�� ������ ������ ����
	public boolean sendFile(String tmpIP, String tmpFile){
		OutputStream out;
		FileInputStream fin;

		try{
			socClient = new Socket(tmpIP, 7777); //127.0.0.1�� ������ �����Ƿ� �ڽ��� �����Ǹ� ��ȯ���ְ�,
			System.out.println("[Flow _ Share Manager] Server�� ���� �߽��ϴ�.");        //11111�� �������� ��Ʈ�Դϴ�.
			out = socClient.getOutputStream();                 //������ ����Ʈ������ �����͸� ������ ��Ʈ���� �����մϴ�.
			DataOutputStream dout = new DataOutputStream(out); //OutputStream�� �̿��� ������ ������ ������ ��Ʈ���� �����մϴ�.

			while(true){
				fin = new FileInputStream(new File(tmpFile)); //FileInputStream - ���Ͽ��� �Է¹޴� ��Ʈ���� �����մϴ�.

				byte[] buffer = new byte[4];        //����Ʈ������ �ӽ������ϴ� ���۸� �����մϴ�.
				int len;                               //������ �������� ���̸� �����ϴ� �����Դϴ�.
				int data=0;                            //����Ƚ��, �뷮�� �����ϴ� �����Դϴ�.

				while( (len = fin.read(buffer))>0 ){     //FileInputStream�� ���� ���Ͽ��� �Է¹��� �����͸� ���ۿ� �ӽ������ϰ� �� ���̸� �����մϴ�.
					data++;                        //�������� ���� �����մϴ�.
				}

				int datas = data;                      //�Ʒ� for���� ���� data�� 0�̵Ǳ⶧���� �ӽ������Ѵ�.

				fin.close();
				fin = new FileInputStream(tmpFile);   //FileInputStream�� ����Ǿ����� ���Ӱ� �����մϴ�.
				dout.writeInt(data);                   //������ ����Ƚ���� ������ �����ϰ�,
				dout.writeUTF(tmpFile);               //������ �̸��� ������ �����մϴ�.

				len = 0;

				for(; data>0; data--){                   // �����͸� �о�� Ƚ����ŭ FileInputStream���� ������ ������ �о�ɴϴ�.
					len = fin.read(buffer);        // FileInputStream�� ���� ���Ͽ��� �Է¹��� �����͸� ���ۿ� �ӽ������ϰ� �� ���̸� �����մϴ�.
					out.write(buffer,0,len);       // �������� ������ ����(1kbyte��ŭ������, �� ���̸� �����ϴ�.

					double tmpPer = (double)datas-data;
					tmpPer = (tmpPer/datas)*100;
					setPercent(tmpPer);
					clsParent.pnlShareFile.setProgressValue((int)tmpPer);

					String tmpStrPer = String.format("%.2f" , tmpPer);
					System.out.println("[Flow _ Share Manager] ��ü ���� ũ�� �� "+tmpStrPer+"% ���۵�");
				}

				setPercent((double)100.0);

				out.flush();
				out.close();
				System.out.println("[Flow _ Share Manager] ���� ������ �Ϸ�Ǿ����ϴ�.");
				return true;
			}
		}catch(Exception e){
			// e.printStackTrace();
			return false;
		}
	}
}