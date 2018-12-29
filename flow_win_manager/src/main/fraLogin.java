package main;

import module.*;
import module.modSysTheme.*;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.*;

public class fraLogin extends JPanel implements ActionListener, ComponentListener{
	private static modCoreWindow clsParent;
	private static modSysData clsSysData;

	boolean modeReg = false;

	JLabel lblTitle = new JLabel("�����ڷ� �α����ϸ� ���� ����� ���� ���� ����� ����� �� �ֽ��ϴ�.");
	JLabel lblDesc[] = new JLabel [2];
	flowButton btnSubmit = new flowButton("�α���");
	flowTextField txtID = new flowTextField();
	flowPasswordField txtPW = new flowPasswordField();

	public fraLogin(modCoreWindow clsParent, modSysData clsSysData){
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		clsParent.getRootPane().addComponentListener(this);

		this.setLayout(null);
		this.setBackground(new Color(255,255,255));

		// �α��� ������
		lblTitle.setOpaque(false);
		lblTitle.setFont(new Font("Malgun Gothic",0,14));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setBounds(80, 0, 200, 30);
		lblTitle.setVisible(true);

		lblDesc[0] = new JLabel("���̵� (ID)");
		lblDesc[0].setOpaque(false);
		lblDesc[0].setFont(new Font("Malgun Gothic",1,14));
		lblDesc[0].setHorizontalAlignment(SwingConstants.LEFT);
		lblDesc[0].setBounds(80, 45, 300, 30);
		lblDesc[0].setVisible(true);

		lblDesc[1] = new JLabel("��й�ȣ (Password)");
		lblDesc[1].setOpaque(false);
		lblDesc[1].setFont(new Font("Malgun Gothic",1,14));
		lblDesc[1].setHorizontalAlignment(SwingConstants.LEFT);
		lblDesc[1].setBounds(80, 115, 300, 30);
		lblDesc[1].setVisible(true);

		txtID.setFont(new Font("Malgun Gothic",1,12));
		txtID.setHorizontalAlignment(SwingConstants.LEFT);
		txtID.setBounds(80,70,400,30);
		txtID.registerKeyboardAction(this, "submit", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), JComponent.WHEN_FOCUSED);
		txtID.setFocusable(true);
		txtID.setVisible(true);

		txtPW.setFont(new Font("Malgun Gothic",1,12));
		txtPW.setHorizontalAlignment(SwingConstants.LEFT);
		txtPW.setBounds(80,145,400,30);
		txtPW.registerKeyboardAction(this, "submit", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), JComponent.WHEN_FOCUSED);
		txtPW.setVisible(true);

		btnSubmit.setFont(new Font("Malgun Gothic",1,14));
		btnSubmit.setHorizontalAlignment(SwingConstants.CENTER);
		btnSubmit.setBounds(80, 190, 100, 40);
		btnSubmit.setActionCommand("submit");
		btnSubmit.addActionListener(this);
		btnSubmit.setVisible(true);

		// �гο� �߰�
		this.add(lblTitle);
		this.add(lblDesc[0]);
		this.add(lblDesc[1]);
		this.add(txtID);
		this.add(txtPW);
		this.add(btnSubmit);

		this.setVisible(true);
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
		Component c = (Component)evt.getSource();
		this.setBounds(0,0,c.getWidth(),c.getHeight());
		lblTitle.setBounds(80, 0, this.getWidth()-160, 30);
		for(int i=0; i<lblDesc.length; i++) {
			lblDesc[i].setBounds(80, 40+(70*i), this.getWidth()-160, 30);
		}
		txtID.setBounds(80, 70, this.getWidth()-160, 30);
		txtPW.setBounds(80, 145, this.getWidth()-160, 30);     
		btnSubmit.setBounds(this.getWidth()-180, this.getHeight()-(190), 100, 40);       
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch(e.getActionCommand()) {
		case "submit":
			// �α��� ���
			int Login_chk = clsSysData.setLogin(txtID.getText(), txtPW.getText());
			if(Login_chk == 1) {
				clsSysData.setLoginValue(true);
				txtID.setText("");
				txtPW.setText("");
				clsParent.pnlMenu.setMenuNum(1);
				clsParent.clsTrayIcon.showAlert("Flow Session Manager", "Flow �������� �α����Ͽ����ϴ�.", MessageType.INFO);
			}else if(Login_chk == 2){
				clsParent.clsTrayIcon.showMessage("�α��� ����", "�̹� �α��εǾ� �ִ� ���̵��Դϴ�.\nȮ�� �� �ٽ� �õ� ���ֽʽÿ�.", 1);
			}else {
				clsParent.clsTrayIcon.showMessage("�α��� ����", "ID �Ǵ� PW�� �ùٸ��� �ʽ��ϴ�.\nȮ�� �� �ٽ� �õ� ���ֽʽÿ�.", 2);
			}
			break;
		}
	}
}