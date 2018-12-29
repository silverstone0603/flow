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

	JLabel lblTitle = new JLabel("로그인하면 파일 암호화 및 스토리지 동기화 기능을 사용할 수 있습니다.");
	JLabel lblDesc[] = new JLabel [3];
	flowButton btnRegister = new flowButton("> 아직 계정이 없으신가요?");
	flowButton btnSubmit = new flowButton("로그인");
	flowTextField txtID = new flowTextField();
	flowPasswordField txtPW = new flowPasswordField();
	flowPasswordField txtPWVerify = new flowPasswordField();

	public fraLogin(modCoreWindow clsParent, modSysData clsSysData){
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		clsParent.getRootPane().addComponentListener(this);

		this.setLayout(null);
		this.setBackground(new Color(255,255,255));

		// 로그인 프레임
		lblTitle.setOpaque(false);
		lblTitle.setFont(new Font("Malgun Gothic",0,14));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setBounds(80, 0, 200, 30);
		lblTitle.setVisible(true);

		lblDesc[0] = new JLabel("아이디 (ID)");
		lblDesc[0].setOpaque(false);
		lblDesc[0].setFont(new Font("Malgun Gothic",1,14));
		lblDesc[0].setHorizontalAlignment(SwingConstants.LEFT);
		lblDesc[0].setBounds(80, 45, 300, 30);
		lblDesc[0].setVisible(true);

		lblDesc[1] = new JLabel("비밀번호 (Password)");
		lblDesc[1].setOpaque(false);
		lblDesc[1].setFont(new Font("Malgun Gothic",1,14));
		lblDesc[1].setHorizontalAlignment(SwingConstants.LEFT);
		lblDesc[1].setBounds(80, 115, 300, 30);
		lblDesc[1].setVisible(true);

		lblDesc[2] = new JLabel("비밀번호 다시 입력 (Verify Password)");
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

		// 패널에 추가
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
				btnRegister.setText("> 로그인 창 돌아가기");
				btnSubmit.setText("계정 등록");
			}else {
				txtID.setText("");
				txtPW.setText("");
				txtPWVerify.setText("");
				lblDesc[2].setVisible(false);
				txtPWVerify.setVisible(false);
				btnRegister.setText("> 아직 계정이 없으신가요?");
				btnSubmit.setText("로그인");
			}
			break;
		case "submit":
			if(modeReg==false) {
				// 로그인 모드
				if(txtID.getText().length()>=5 && txtPW.getText().length()>=8) {
					int Login_chk = clsSysData.setLogin(txtID.getText(), txtPW.getText());
					if(Login_chk == 1) {
						clsSysData.setEncryInfo(txtID.getText()); // 696d697373796f7568616e6765656e6A
						txtID.setText("");
						txtPW.setText("");
						clsParent.pnlMenu.setMenuNum(0);
						clsParent.clsTrayIcon.showAlert("Flow Companion", "Flow 계정으로 로그인하였습니다.", MessageType.INFO);
					}else if(Login_chk == 2){
						clsParent.clsTrayIcon.showMessage("로그인 실패", "이미 로그인되어 있는 아이디입니다.\n확인 후 다시 시도 해주십시오.", 1);
					}else {
						clsParent.clsTrayIcon.showMessage("로그인 실패", "ID 또는 PW가 올바르지 않습니다.\n확인 후 다시 시도 해주십시오.", 2);
					}
				}else{
					clsParent.clsTrayIcon.showMessage("로그인 실패", "아이디는 5자 이상 및 비밀번호는 8자 이상으로 입력 해주십시오.", 0);
				}
			}else {
				if(txtID.getText().length()>=5 && txtPW.getText().length()>=8) {
					int Reg_chk = clsSysData.setRegister(txtID.getText(), txtPW.getText(), txtPWVerify.getText());
					if(Reg_chk == 0) {
						// 회원 가입 모드
						modeReg = false;
						txtID.setText("");
						txtPW.setText("");
						txtPWVerify.setText("");
						lblDesc[2].setVisible(false);
						txtPWVerify.setVisible(false);
						btnRegister.setText("> 아직 계정이 없으신가요?");
						btnSubmit.setText("로그인");
						
						clsParent.clsTrayIcon.showMessage("계정 등록", "계정을 만들었습니다.\n새로운 계정으로 로그인 해주세요.", 0);
					}else if (Reg_chk == 1){
						// 존재하는 아이디
						clsParent.clsTrayIcon.showMessage("계정 등록 실패", "이미 등록된 아이디입니다.\n다른 아이디를 입력 해주십시오.", 1);
					}else if (Reg_chk == 2) {
						// 비밀번호가 틀림
						clsParent.clsTrayIcon.showMessage("계정 등록 실패", "기존에 입력한 비밀번호와 확인용 비밀번호가 다릅니다.", 1);
					}else {
						// DB 접속 오류 or 구문오류 확인
						clsParent.clsTrayIcon.showMessage("계정 등록 실패","알수없는 오류입니다. 관리자에게 문의해주세요.", 2);
					}
				}else {
					clsParent.clsTrayIcon.showMessage("계정 등록 실패", "아이디는 5자 이상 및 비밀번호는 8자 이상으로 입력 해주십시오.", 1);
				}
			}
			break;
		}
	}
}