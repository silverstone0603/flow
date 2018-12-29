package main;

import module.*;
import module.modSysTheme.*;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.*;
import javax.swing.*;

public class fraToolbar extends JPanel implements ActionListener, ComponentListener {
	private static modCoreWindow clsParent;
	private static modSysData clsSysData;

	public int numMenu;

	public JLabel lblTitle = new JLabel();
	public flowButton btn_Login = new flowButton("�α���");
	
	public fraToolbar(modCoreWindow clsParent, modSysData clsSysData){
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;

		clsParent.getRootPane().addComponentListener(this);

		setLayout(null);
		setBackground(new Color(255,255,255));
		setFont(new Font("Malgun Gothic",1,18));
		setBounds(0, 0, 684, clsParent.getSizeMenu());

		clsParent.addComponentListener(this);
		clsParent.getContentPane().add(this);

		lblTitle.setFont(new Font("Malgun Gothic",1,15));
		lblTitle.setText("Flow Companion");
		lblTitle.setVisible(true);

		// ��ư ����
		btn_Login.setFont(new Font("Malgun Gothic",1,12));
		btn_Login.addActionListener(this);
		btn_Login.setVisible(false);

		add(lblTitle);
		add(btn_Login);

		setMenuNum(0);
		System.out.println("[Flow _ Window Frame] ��� ��ư�� �����Ǿ����ϴ�.");

	}

	public String toString() {
		return String.format("%d",numMenu);
	}

	public int getMenuNum() {
		return this.numMenu;
	}

	public void setMenuNum(int num) {
		if(clsSysData.getLoginValue() == true) {
			lblTitle.setText(clsSysData.getUserID()+"��, �������.");
			lblTitle.repaint();
			btn_Login.setText("�α׾ƿ�");
		}else {
			lblTitle.setText("Flow�� �̿��Ϸ��� �α��� �ϼ���.");
			btn_Login.setText("�α���");
		}

		switch(num) {
		case 0:
			if(clsSysData.getLoginValue() == false) {
				this.numMenu = 0;
				clsParent.layCards.show(clsParent.pnlContents, "FormMain");
			}else {
				clsSysData.setLogout();
				btn_Login.setText("�α���");
				clsParent.clsTrayIcon.showAlert("Flow Companion", "Flow �������� �α׾ƿ� �Ǿ����ϴ�.", MessageType.INFO);
				
				btn_Login.setVisible(false);
				setMenuNum(1);
			}
			break;
		case 1:
			if(clsSysData.getLoginValue() == true) {
				this.numMenu = 1;
				clsParent.layCards.show(clsParent.pnlContents, "FormUserList");

				btn_Login.setText("�α׾ƿ�");
				btn_Login.setVisible(true);
			}else setMenuNum(0);
			break;
		}
		this.frameResized();
	}

	public void frameResized() {
		this.setBounds(0, 0, this.getWidth(), this.getHeight());
		
		lblTitle.setBounds(20, 20, 250, 40);
		
		btn_Login.setBounds(this.getWidth()-(10+100), 10, 100, 60);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		frameResized();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//actinoPerformed�� �Լ��� ����Ѵ�
		if(e.getSource() == this.btn_Login) {
			setMenuNum(0);        	
		}
	}

}
