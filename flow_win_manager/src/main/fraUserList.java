package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import module.modCoreWindow;
import module.modSysData;

public class fraUserList extends JPanel implements ActionListener, ComponentListener {

	private modCoreWindow clsParent;
	private modSysData clsSysData;

	Border tmpBorder, tmpButtonBorder;

	JLabel lblTitle = new JLabel("현재 접속자 정보를 불러오는 중입니다.");
	JTextArea txtLog = new JTextArea();
	JScrollPane scrTxtlog = new JScrollPane(txtLog, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);  //스크롤판 추가
	//txtLog.setCaretPosition(txtLog.getDocument().getLength());  // 



	JList<String> lstUserList = new JList<>(); // 파일리스트 창.
	DefaultListModel dlstUserList = new DefaultListModel();

	JScrollPane scrFileList = new JScrollPane(lstUserList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	private static Timer Session_Timer = null;
	private static TimerTask Session_task = null;

	public fraUserList(modCoreWindow clsParent, modSysData clsSysData) {
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		clsParent.getRootPane().addComponentListener(this);

		makeComponent();
		
		setRefreshList();
		
		Session_Timer = new Timer();
		Session_task = new TimerTask() {
			@Override
			public void run() {
				try{
					String tmpUsrDelList = clsSysData.loadUsrDeleteList();
					txtLog.append("["+clsSysData.getTime()+"] "+tmpUsrDelList+"\n");
					
					txtLog.setCaretPosition(txtLog.getDocument().getLength());
					setRefreshList();
				}catch(Exception e) {}
			}
		};
		Session_Timer.schedule(Session_task, 10000, 10000); // 5분 : 300,000
	}

	public void makeComponent() {
		this.setLayout(null);
		this.setBackground(new Color(255, 255, 255));

		tmpBorder = BorderFactory.createLineBorder(new Color(41, 82, 161));
		tmpBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5); // 상하좌우 10씩 띄우기

		tmpButtonBorder = BorderFactory.createLineBorder(new Color(88, 143, 249));
		tmpButtonBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5); // 상하좌우 10씩 띄우기

		// 로그인 프레임
		lblTitle.setOpaque(false);
		lblTitle.setFont(new Font("Malgun Gothic", 1, 18));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setBounds(50, 30, 500, 30);	//50, 30, 500, 30
		lblTitle.setVisible(true);

		/*
		txtLog.setFont(new Font("Malgun Gothic", 1, 12));
		txtLog.setBounds(50, 70, 400, 30);
		txtLog.setVisible(true);
		*/
		
		lstUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 파일 단일 선택만 가능하도록 함.
		lstUserList.setBorder(tmpBorder);
		lstUserList.setFont(new Font("Malgun Gothic", 1, 18));
		lstUserList.setForeground(Color.BLACK);
		//lstUserList.setBackground(new Color(0, 0, 0));
		
		scrTxtlog.setBounds(50, 130, 400, 60);	//(50, 130, 400, 60)
		//scrTxtlog.setBorder(null);

		scrFileList.setBounds(50, 150, 400, 300);	// 50, 150, 400, 300
		scrFileList.setBorder(null);
		
		

		// 패널에 추가
		this.add(lblTitle);
		this.add(scrTxtlog);
		this.add(scrFileList);

		this.setVisible(true);
	}

	public void setRefreshList() {
		dlstUserList.removeAllElements();
		
		if(clsSysData.getLoginValue()==true) {
			String[][] tmpUsrList = clsSysData.loadUsrList(); 
	
			lblTitle.setText("현재 세션 갯수는 "+tmpUsrList.length+"개 입니다.");
	
			for (int i=0; i<tmpUsrList.length; i++) {
				dlstUserList.addElement(tmpUsrList[i][0] + " (" +tmpUsrList[i][1] + ")");
			}
		}
		
		lstUserList.setModel(dlstUserList);
		lstUserList.repaint();
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
	public void componentResized(ComponentEvent evt) { // 창 사이즈 바뀔때 적용되는 소스
		// TODO Auto-generated method stub
		Component c = (Component) evt.getSource();
		this.setBounds(0, 0, c.getWidth(), c.getHeight() - clsParent.getSizeMenu());
		
		lblTitle.setBounds(50, 30, c.getWidth()-(50+50), 30);		//50, 30, c.getWidth()-(50+50), 30
		scrTxtlog.setBounds(50, 60, c.getWidth() - 100, 90);	//delUser 50, 60, c.getWidth() - 100, 90
		
		txtLog.setBounds(0, 0, scrTxtlog.getWidth(), scrTxtlog.getHeight() - 250);
		
		scrFileList.setBounds(50, 180, c.getWidth() - 100, c.getHeight() - 150);	//listUser 50, 150
		lstUserList.setBounds(0, 0, scrFileList.getWidth(), scrFileList.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}