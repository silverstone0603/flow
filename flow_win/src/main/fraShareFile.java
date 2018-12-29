package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import module.modCoreWindow;
import module.modSysData;
import module.modSysTheme.flowButton;
import module.modSysTheme.flowProgressbar;
import module.modSysTheme.flowTextField;

public class fraShareFile extends JPanel implements ActionListener, ComponentListener {
	private static modCoreWindow clsParent;
	private static modSysData clsSysData;

	// 파일 주거나 받는 상태인기 표시
	private boolean blSharingMode = false;
	public static String tmpFilePath = "";

	JLabel lblTitle = new JLabel("Flow ID를 가지고 있는 사용자와 파일을 주고 받을 수 있습니다.");
	JLabel lblShareTitle = new JLabel("파일을 주고 받을 Flow ID를 입력하세요.");
	flowTextField txtUserID = new flowTextField();
	flowButton btnSubmit = new flowButton("보내기");
	flowProgressbar pgbFile = new flowProgressbar();

	private static Timer Share_Timer = null;
	private static TimerTask Share_Task = null;

	public fraShareFile(modCoreWindow clsParent, modSysData clsSysData) {
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		clsParent.getRootPane().addComponentListener(this);

		this.setLayout(null);
		this.setBackground(new Color(255, 255, 255));

		// 프레임
		lblTitle.setOpaque(false);
		lblTitle.setFont(new Font("Malgun Gothic", 0, 14));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setBounds(80, 0, 200, 30);
		lblTitle.setVisible(true);

		lblShareTitle.setOpaque(false);
		lblShareTitle.setFont(new Font("Malgun Gothic", 0, 14));
		lblShareTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblShareTitle.setBounds(80, 50, 200, 30);
		lblShareTitle.setVisible(true);

		txtUserID.setOpaque(false);
		txtUserID.setFont(new Font("Malgun Gothic", 0, 14));
		txtUserID.setHorizontalAlignment(SwingConstants.LEFT);
		txtUserID.setBounds(80, 90, 200, 30);
		txtUserID.setVisible(true);

		btnSubmit.setFont(new Font("Malgun Gothic", 1, 14));
		btnSubmit.setHorizontalAlignment(SwingConstants.CENTER);
		btnSubmit.setBounds(280, 90, 100, 30);
		btnSubmit.setActionCommand("sendfile");
		btnSubmit.addActionListener(this);
		btnSubmit.setVisible(true);

		pgbFile.setText("파일 공유 상태 정보");
		pgbFile.setBounds(80, 100, 200, 30);
		pgbFile.setVisible(false);

		// 패널에 추가
		this.add(lblTitle);
		this.add(lblShareTitle);
		this.add(txtUserID);
		this.add(btnSubmit);
		this.add(pgbFile);

		this.setVisible(true);
	}

	public void setSendFile(String tmpPath) {
		// 경로 전달 받기
		this.tmpFilePath = tmpPath;
		clsParent.pnlMenu.setMenuNum(3);
		this.repaint();
	}
	
	public void setProgressValue(int value) {
		pgbFile.setProgressValue(value);
	}
	
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat date_f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return String.format(date_f.format(date).toString());
	}
	
	public void setSubmitUserID() {
		try{
			if(txtUserID.getText().length()<=0) {
				clsParent.clsTrayIcon.showMessage("공유 오류", "파일을 전송할 사용자의 Flow ID를 입력해주세요.", 1);
			}else {
				Connection con = clsParent.clsDBMS.Connecter();
				Statement stmt = con.createStatement();
				String tmpQuery = "select u_id, ip from session where u_id='"+txtUserID.getText()+"';";
				ResultSet rsMain = stmt.executeQuery(tmpQuery);
				
				if(rsMain.next()) {
					// 해당 사용자가 접속 중이라면
					if(rsMain.getString("u_id").equals(txtUserID.getText())) {
						clsParent.pnlMenu.setButtonEnable(false);

						String tmpID = clsSysData.getUserID();
						String tmpRcvID = rsMain.getString("u_id");
						String tmpIP = clsSysData.getIPAddr();
						String tmpRcvIP = rsMain.getString("ip");
						String tmpTime = getTime(); 
						String tmpIssuc = "0";

						stmt = null;
						stmt = con.createStatement();
						tmpQuery = "insert into share_list (u_id, rcv_id, u_ip, rcv_ip, sndtime, issuc) values('"+tmpID+"','"+tmpRcvID+"','"+tmpIP+"','"+tmpRcvIP+"','"+tmpTime+"',"+tmpIssuc+");";
						int tmpValue = stmt.executeUpdate(tmpQuery);
						
						if(tmpValue > 0) {
							System.out.println("[Flow _ Share Manager] 공유 목록에 공유 파일을 추가하였습니다.");
							
							lblShareTitle.setText("파일 공유를 위해 준비 중이니 잠시만 기다려주세요...");
							txtUserID.setText("");
							txtUserID.setVisible(false);
							btnSubmit.setVisible(false);
							pgbFile.setProgressValue(0);
							pgbFile.setVisible(true);
							
							Share_Timer = new Timer();
							Share_Task = new TimerTask() {
								@Override
								public void run() {
									try {
										lblShareTitle.setText("요청하신 사용자에게 파일을 공유합니다.");
										while(true) {
											boolean blSend = clsParent.clsSocket.sendFile(tmpRcvIP, tmpFilePath);
											if(blSend == true) {
												clsParent.clsTrayIcon.showAlert("Flow Companion", "파일 공유를 완료하였습니다.", MessageType.INFO);
												break;
											}else {
												clsParent.clsTrayIcon.showAlert("Flow Companion", "파일 공유에 실패했습니다. 다시 한번 시도해주세요.", MessageType.ERROR);
												break;
											}
										}
										
										clsParent.pnlMenu.setButtonEnable(true);
										lblShareTitle.setText("파일을 주고 받을 Flow ID를 입력하세요.");
										txtUserID.setVisible(true);
										btnSubmit.setVisible(true);
										pgbFile.setVisible(false);
										
										clsParent.pnlMenu.setMenuNum(2);
										Share_Task.cancel();
									}catch(Exception e) {}
								}
							};
							Share_Timer.schedule(Share_Task, 30000, 30000);
						}
					}
				}else {
					clsParent.clsTrayIcon.showMessage("해당 사용자 없음", "입력하신 사용자가 없거나 Flow Companion을 사용중인 상태가 아닙니다.\n확인 후 다시 입력해주세요.", 1);
				}
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void setReceiveMode(int value) {
		switch(value) {
			case 0:
				clsParent.pnlMenu.setButtonEnable(true);
				lblShareTitle.setText("파일을 주고 받을 Flow ID를 입력하세요.");
				txtUserID.setText("");
				txtUserID.setVisible(true);
				btnSubmit.setVisible(true);
				pgbFile.setProgressValue(0);
				pgbFile.setVisible(false);
				
				break;
			case 1:
				clsParent.pnlMenu.setButtonEnable(false);
				lblShareTitle.setText("공유한 파일을 내려받는 작업을 진행중입니다...");
				txtUserID.setText("");
				txtUserID.setVisible(false);
				btnSubmit.setVisible(false);
				pgbFile.setProgressValue(0);
				pgbFile.setVisible(true);
				
				break;
		}

	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent evt) {
		// TODO Auto-generated method stub
		Component c = (Component) evt.getSource();
		this.setBounds(0, 0, c.getWidth(), c.getHeight());

		lblTitle.setBounds(80, 0, this.getWidth() - 80, 30);
		lblShareTitle.setBounds(80, 50, this.getWidth() - (80 + 100), 30);
		txtUserID.setBounds(80, 90, this.getWidth() - (80 + 190), 30);
		btnSubmit.setBounds(this.getWidth() - (80 + 100), 90, 100, 30);
		pgbFile.setBounds(80, 90, this.getWidth() - (80 + 80), 30);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
		case "sendfile":
			setSubmitUserID();
			break;
		case "cancel":
			if (blSharingMode == true) {
				lblTitle.setVisible(true);
				lblShareTitle.setVisible(true);
				btnSubmit.setVisible(true);
				pgbFile.setVisible(true);
			} else {
				lblTitle.setVisible(true);
				lblShareTitle.setVisible(true);
				btnSubmit.setVisible(true);
				pgbFile.setVisible(true);
			}
			break;
		}
	}
}
