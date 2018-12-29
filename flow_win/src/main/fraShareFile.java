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

	// ���� �ְų� �޴� �����α� ǥ��
	private boolean blSharingMode = false;
	public static String tmpFilePath = "";

	JLabel lblTitle = new JLabel("Flow ID�� ������ �ִ� ����ڿ� ������ �ְ� ���� �� �ֽ��ϴ�.");
	JLabel lblShareTitle = new JLabel("������ �ְ� ���� Flow ID�� �Է��ϼ���.");
	flowTextField txtUserID = new flowTextField();
	flowButton btnSubmit = new flowButton("������");
	flowProgressbar pgbFile = new flowProgressbar();

	private static Timer Share_Timer = null;
	private static TimerTask Share_Task = null;

	public fraShareFile(modCoreWindow clsParent, modSysData clsSysData) {
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		clsParent.getRootPane().addComponentListener(this);

		this.setLayout(null);
		this.setBackground(new Color(255, 255, 255));

		// ������
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

		pgbFile.setText("���� ���� ���� ����");
		pgbFile.setBounds(80, 100, 200, 30);
		pgbFile.setVisible(false);

		// �гο� �߰�
		this.add(lblTitle);
		this.add(lblShareTitle);
		this.add(txtUserID);
		this.add(btnSubmit);
		this.add(pgbFile);

		this.setVisible(true);
	}

	public void setSendFile(String tmpPath) {
		// ��� ���� �ޱ�
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
				clsParent.clsTrayIcon.showMessage("���� ����", "������ ������ ������� Flow ID�� �Է����ּ���.", 1);
			}else {
				Connection con = clsParent.clsDBMS.Connecter();
				Statement stmt = con.createStatement();
				String tmpQuery = "select u_id, ip from session where u_id='"+txtUserID.getText()+"';";
				ResultSet rsMain = stmt.executeQuery(tmpQuery);
				
				if(rsMain.next()) {
					// �ش� ����ڰ� ���� ���̶��
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
							System.out.println("[Flow _ Share Manager] ���� ��Ͽ� ���� ������ �߰��Ͽ����ϴ�.");
							
							lblShareTitle.setText("���� ������ ���� �غ� ���̴� ��ø� ��ٷ��ּ���...");
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
										lblShareTitle.setText("��û�Ͻ� ����ڿ��� ������ �����մϴ�.");
										while(true) {
											boolean blSend = clsParent.clsSocket.sendFile(tmpRcvIP, tmpFilePath);
											if(blSend == true) {
												clsParent.clsTrayIcon.showAlert("Flow Companion", "���� ������ �Ϸ��Ͽ����ϴ�.", MessageType.INFO);
												break;
											}else {
												clsParent.clsTrayIcon.showAlert("Flow Companion", "���� ������ �����߽��ϴ�. �ٽ� �ѹ� �õ����ּ���.", MessageType.ERROR);
												break;
											}
										}
										
										clsParent.pnlMenu.setButtonEnable(true);
										lblShareTitle.setText("������ �ְ� ���� Flow ID�� �Է��ϼ���.");
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
					clsParent.clsTrayIcon.showMessage("�ش� ����� ����", "�Է��Ͻ� ����ڰ� ���ų� Flow Companion�� ������� ���°� �ƴմϴ�.\nȮ�� �� �ٽ� �Է����ּ���.", 1);
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
				lblShareTitle.setText("������ �ְ� ���� Flow ID�� �Է��ϼ���.");
				txtUserID.setText("");
				txtUserID.setVisible(true);
				btnSubmit.setVisible(true);
				pgbFile.setProgressValue(0);
				pgbFile.setVisible(false);
				
				break;
			case 1:
				clsParent.pnlMenu.setButtonEnable(false);
				lblShareTitle.setText("������ ������ �����޴� �۾��� �������Դϴ�...");
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
