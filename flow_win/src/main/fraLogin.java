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

	JLabel lblTitle = new JLabel("�α����ϸ� ���� ��ȣȭ �� ���丮�� ����ȭ ����� ����� �� �ֽ��ϴ�.");
	JLabel lblDesc[] = new JLabel [3];
	flowButton btnRegister = new flowButton("> ���� ������ �����Ű���?");
	flowButton btnSubmit = new flowButton("�α���");
	flowTextField txtID = new flowTextField();
	flowPasswordField txtPW = new flowPasswordField();
	flowPasswordField txtPWVerify = new flowPasswordField();

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

		lblDesc[2] = new JLabel("��й�ȣ �ٽ� �Է� (Verify Password)");
		lblDesc[2].setOpaque(false);
		lblDesc[2].setFont(new Font("Malgun Gothic",1,14));
		lblDesc[2].setHorizontalAlignment(SwingConstants.LEFT);
		lblDesc[2].setBounds(80, 185, 300, 30);
		lblDesc[2].setVisible(false);

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

		txtPWVerify.setFont(new Font("Malgun Gothic",1,12));
		txtPWVerify.setHorizontalAlignment(SwingConstants.LEFT);
		txtPWVerify.setBounds(80,215,400,30);
		txtPWVerify.registerKeyboardAction(this, "submit", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), JComponent.WHEN_FOCUSED);
		txtPWVerify.setVisible(false);

		btnRegister.setFont(new Font("Malgun Gothic",1,14));
		btnRegister.setHorizontalAlignment(SwingConstants.LEFT);
		btnRegister.setBounds(80, 190, 200, 40);
		btnRegister.setBorderSize(0);
		btnRegister.setGraidentEffect(false);
		btnRegister.setActionCommand("register");
		btnRegister.addActionListener(this);
		btnRegister.setVisible(true);

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
		this.add(lblDesc[2]);
		this.add(txtID);
		this.add(txtPW);
		this.add(txtPWVerify);
		this.add(btnRegister);
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
		txtPWVerify.setBounds(80,215,this.getWidth()-160,30);
		btnRegister.setBounds(80, this.getHeight()-(190), 200, 40);       
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
		case "register":
			modeReg = !modeReg;
			if(modeReg==true) {
				txtID.setText("");
				txtPW.setText("");
				txtPWVerify.setText("");
				lblDesc[2].setVisible(true);
				txtPWVerify.setVisible(true);
				btnRegister.setText("> �α��� â ���ư���");
				btnSubmit.setText("���� ���");
			}else {
				txtID.setText("");
				txtPW.setText("");
				txtPWVerify.setText("");
				lblDesc[2].setVisible(false);
				txtPWVerify.setVisible(false);
				btnRegister.setText("> ���� ������ �����Ű���?");
				btnSubmit.setText("�α���");
			}
			break;
		case "submit":
			if(modeReg==false) {
				// �α��� ���
				if(txtID.getText().length()>=5 && txtPW.getText().length()>=8) {
					int Login_chk = clsSysData.setLogin(txtID.getText(), txtPW.getText());
					if(Login_chk == 1) {
						clsSysData.setEncryInfo(txtID.getText()); // 696d697373796f7568616e6765656e6A
						txtID.setText("");
						txtPW.setText("");
						clsParent.pnlMenu.setMenuNum(0);
						clsParent.clsTrayIcon.showAlert("Flow Companion", "Flow �������� �α����Ͽ����ϴ�.", MessageType.INFO);
					}else if(Login_chk == 2){
						clsParent.clsTrayIcon.showMessage("�α��� ����", "�̹� �α��εǾ� �ִ� ���̵��Դϴ�.\nȮ�� �� �ٽ� �õ� ���ֽʽÿ�.", 1);
					}else {
						clsParent.clsTrayIcon.showMessage("�α��� ����", "ID �Ǵ� PW�� �ùٸ��� �ʽ��ϴ�.\nȮ�� �� �ٽ� �õ� ���ֽʽÿ�.", 2);
					}
				}else{
					clsParent.clsTrayIcon.showMessage("�α��� ����", "���̵�� 5�� �̻� �� ��й�ȣ�� 8�� �̻����� �Է� ���ֽʽÿ�.", 0);
				}
			}else {
				if(txtID.getText().length()>=5 && txtPW.getText().length()>=8) {
					int Reg_chk = clsSysData.setRegister(txtID.getText(), txtPW.getText(), txtPWVerify.getText());
					if(Reg_chk == 0) {
						// ȸ�� ���� ���
						modeReg = false;
						txtID.setText("");
						txtPW.setText("");
						txtPWVerify.setText("");
						lblDesc[2].setVisible(false);
						txtPWVerify.setVisible(false);
						btnRegister.setText("> ���� ������ �����Ű���?");
						btnSubmit.setText("�α���");
						
						clsParent.clsTrayIcon.showMessage("���� ���", "������ ��������ϴ�.\n���ο� �������� �α��� ���ּ���.", 0);
					}else if (Reg_chk == 1){
						// �����ϴ� ���̵�
						clsParent.clsTrayIcon.showMessage("���� ��� ����", "�̹� ��ϵ� ���̵��Դϴ�.\n�ٸ� ���̵� �Է� ���ֽʽÿ�.", 1);
					}else if (Reg_chk == 2) {
						// ��й�ȣ�� Ʋ��
						clsParent.clsTrayIcon.showMessage("���� ��� ����", "������ �Է��� ��й�ȣ�� Ȯ�ο� ��й�ȣ�� �ٸ��ϴ�.", 1);
					}else {
						// DB ���� ���� or �������� Ȯ��
						clsParent.clsTrayIcon.showMessage("���� ��� ����","�˼����� �����Դϴ�. �����ڿ��� �������ּ���.", 2);
					}
				}else {
					clsParent.clsTrayIcon.showMessage("���� ��� ����", "���̵�� 5�� �̻� �� ��й�ȣ�� 8�� �̻����� �Է� ���ֽʽÿ�.", 1);
				}
			}
			break;
		}
	}
}