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
	public flowButton btn_Main = new flowButton("��");
	public flowButton btn_Login = new flowButton("�α���");
	public flowButton btn_FileExplorer = new flowButton("���� Ž����");
	public flowButton btn_Setting = new flowButton("ȯ�� ����");
	
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
		btn_Main.setFont(new Font("Malgun Gothic",1,12));
		btn_Main.addActionListener(this);
		btn_Main.setVisible(false);
		
		btn_Login.setFont(new Font("Malgun Gothic",1,12));
		btn_Login.addActionListener(this);
		btn_Login.setVisible(false);
		
		btn_FileExplorer.setFont(new Font("Malgun Gothic",1,12));
		btn_FileExplorer.addActionListener(this);
		btn_FileExplorer.setVisible(false);
		
		btn_Setting.setFont(new Font("Malgun Gothic",1,12));
		btn_Setting.addActionListener(this);
		btn_Setting.setVisible(true);
		
		add(lblTitle);
		add(btn_Main);
		add(btn_Login);
		add(btn_FileExplorer);
		add(btn_Setting);
		
		setMenuNum(0);
		System.out.println("[Flow _ Window Frame] ��� ��ư�� �����Ǿ����ϴ�.");
		
	}
	
	public String toString() {
		return String.format("%d",numMenu);
	}
	
	public int getMenuNum() {
		return this.numMenu;
	}
	
	public void setButtonEnable(boolean tmpValue) {
		this.btn_Main.setEnabled(tmpValue);
	}
	
	public void setMenuNum(int num) {
		clsParent.pnlMain.setUserInfoArea(clsSysData.getLoginValue());
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
	        	if(this.numMenu==3) {
	        		setMenuNum(2);
	        	}else {
		        	this.numMenu = 0;
		        	clsParent.layCards.show(clsParent.pnlContents, "FormMain");
		        	
		        	btn_Main.setVisible(false);
		        	btn_Login.setVisible(true);
		        	btn_FileExplorer.setVisible(true);
		        	btn_Setting.setVisible(true);
	        	}
	        	break;
	        case 1:
	        	if(clsSysData.getLoginValue() == false) {
		        	this.numMenu = 1;
		        	clsParent.layCards.show(clsParent.pnlContents, "FormLogin");
		        	
		        	btn_Main.setVisible(true);
		        	btn_Login.setVisible(false);
		        	btn_FileExplorer.setVisible(false);
		        	btn_Setting.setVisible(false);
		        	lblTitle.setText("�α���");
	        	}else {
	        		clsSysData.setLogout();
	        		btn_Login.setText("�α���");
	        		clsParent.clsTrayIcon.showAlert("Flow Companion", "Flow �������� �α׾ƿ� �Ǿ����ϴ�.", MessageType.INFO);
	        		setMenuNum(0);
	        	}
	        	break;
	        case 2:
	        	if(clsSysData.getLoginValue() == true) {
		        	this.numMenu = 2;
		        	clsParent.layCards.show(clsParent.pnlContents, "FormFileExplorer");
		        	
		        	btn_Main.setVisible(true);
		        	btn_Login.setVisible(false);
		        	btn_FileExplorer.setVisible(false);
		        	btn_Setting.setVisible(false);
		        	lblTitle.setText("���� Ž����");
	        	}else setMenuNum(1);
	        	break;
	        case 3:
	        	this.numMenu = 3;
	        	clsParent.layCards.show(clsParent.pnlContents, "FormShareFile");
	        	
	        	btn_Main.setVisible(true);
	        	btn_Login.setVisible(false);
	        	btn_FileExplorer.setVisible(false);
	        	btn_Setting.setVisible(false);
	        	lblTitle.setText("���� ����");
	        	break;
	        case 4:
	        	this.numMenu = 4;
	        	clsParent.layCards.show(clsParent.pnlContents, "FormSetting");
	        	
	        	btn_Main.setVisible(true);
	        	btn_Login.setVisible(false);
	        	btn_FileExplorer.setVisible(false);
	        	btn_Setting.setVisible(false);
	        	lblTitle.setText("ȯ�� ����");
	        	break;
        }
        this.frameResized();
	}

	public void frameResized() {
	      this.setBounds(0, 0, this.getWidth(), this.getHeight());
	      btn_Main.setBounds(10, 10, 60, 60);
	      if(this.getMenuNum()==0) {
	    	  lblTitle.setBounds(20, 20, 250, 40);
	      }else {
	    	  lblTitle.setBounds(80, 20, 250, 40);
	      }

	      btn_Login.setBounds(this.getWidth()-(10+100+10+100+10+100), 10, 100, 60);
	      btn_FileExplorer.setBounds(this.getWidth()-(10+100+10+100), 10, 100, 60);
	      btn_Setting.setBounds(this.getWidth()-(10+100), 10, 100, 60);
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
        if(e.getSource() == this.btn_Main) {
        	setMenuNum(0);        	
        }else if(e.getSource() == this.btn_Login) {
        	setMenuNum(1);        	
        }else if(e.getSource() == this.btn_FileExplorer) {
        	setMenuNum(2);
        }else if(e.getSource() == this.btn_Setting) {
        	setMenuNum(4);
        }
	}

}
