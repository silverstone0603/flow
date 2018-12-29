package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import module.modCoreWindow;
import module.modFileList;
import module.modSysData;
import module.modSysTheme.flowButton;
import module.modSysTheme.flowTable;
import module.modSysTheme.flowTextField;

public class fraFileExplorer extends JPanel implements ActionListener, ComponentListener, ListSelectionListener, MouseListener, KeyListener {

	private modCoreWindow clsParent;
	private modFileList clsFileList;
	private modSysData clsSysData;
	public int chooseFileName;

	File f; // 파일 (암호화, 복구화, 삭제시)담을 파일 변수.

	Border tmpBorder, tmpButtonBorder;

	JLabel lblTitle = new JLabel("파일을 암호화 또는 복호화하거나 다른 사용자에게 공유할 수 있습니다.");
	flowButton btnChkFileAll = new flowButton("목록 반전 선택");
	flowButton btnSendFile = new flowButton("파일 공유");
	flowButton btnEncrypt = new flowButton("암호화 설정/해제");
	flowTextField txtAddress = new flowTextField();
	
	// #####################################################################################
	String[] columnTitles = {"", "파일 이름", "수정된 날짜", "파일 크기"};
	Object[][] dataEntries = null;

	flowTable table = new flowTable(columnTitles, dataEntries);
	// #####################################################################################
	
	JScrollPane scrFileList = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

	String[][] tmpFileList;

	String fileName;
	File enFile;

	public fraFileExplorer(modCoreWindow clsParent, modFileList clsFileList, modSysData clsSysData) {
		this.clsParent = clsParent;
		this.clsFileList = clsFileList;
		this.clsSysData = clsSysData;
		clsParent.getRootPane().addComponentListener(this);

		makeComponent();

		// setRefreshList();
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
		lblTitle.setFont(new Font("Malgun Gothic", 0, 14));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setBounds(80, 0, 200, 30);
		lblTitle.setVisible(true);

		txtAddress.setFont(new Font("Malgun Gothic", 1, 12));
		txtAddress.setHorizontalAlignment(SwingConstants.LEFT);
		txtAddress.setBounds(80, 70, 400, 30);
		txtAddress.setVisible(true);
		
		btnChkFileAll.setFont(new Font("Malgun Gothic", 1, 14));
		btnChkFileAll.setHorizontalAlignment(SwingConstants.CENTER);
		btnChkFileAll.setBounds(80, 105, 125, 30);
		btnChkFileAll.setVisible(true);
		btnChkFileAll.setActionCommand("Button_CheckFileAll");
		btnChkFileAll.addActionListener(this);
		
		btnSendFile.setFont(new Font("Malgun Gothic", 1, 14));
		btnSendFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnSendFile.setBounds(215, 105, 125, 30);
		btnSendFile.setVisible(true);
		btnSendFile.setActionCommand("Button_ShareFile");
		btnSendFile.addActionListener(this);
		
		btnEncrypt.setFont(new Font("Malgun Gothic", 1, 14));
		btnEncrypt.setHorizontalAlignment(SwingConstants.CENTER);
		btnEncrypt.setBounds(80, 105, 150, 30);
		btnEncrypt.setVisible(true);
		btnEncrypt.setActionCommand("Button_Encrypt");
		btnEncrypt.addActionListener(this);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 파일 다중 선택 가능하도록 함.
		table.setBorder(tmpBorder);
		table.setFont(new Font("Malgun Gothic", 0, 12));
		// table.setForeground(Color.WHITE);
		// table.setCellRenderer(new WhiteYellowCellRenderer());      //호출.
		table.setBackground(new Color(255,255,255));
		table.setForeground(new Color(0,0,0));
		table.getTableHeader().setReorderingAllowed(false); // 행이 이동 안되도록 만듦
		table.addMouseListener((MouseListener) this);
		table.addKeyListener(this);
		// table.getColumn("선택").setPreferredWidth(10); // 선택 행 크기 지정
		table.setCellEditable(0, true); // 파일 선택 되도록 설정
		table.getColumnModel().getSelectionModel().addListSelectionListener(this); // 리스트 선택.
		
		scrFileList.setBounds(80, 150, 400, 300);
		scrFileList.setBorder(null);
		scrFileList.addKeyListener(this);

		// 패널에 추가
		this.add(lblTitle);
		this.add(txtAddress);
		this.add(btnChkFileAll);
		this.add(btnSendFile);
		this.add(btnEncrypt);
		this.add(scrFileList);

		this.setVisible(true);
		
		setRefreshList();
	}

	public void setRefreshList() {
		tmpFileList = clsFileList.getList();
		dataEntries = null;
		dataEntries = new Object[tmpFileList.length][5];

		for (int i = 0; i < tmpFileList.length; i++) {
			dataEntries[i][0] = new Boolean(false);
			dataEntries[i][1] = new String(tmpFileList[i][0]);
			dataEntries[i][2] = new String(tmpFileList[i][1]);
			dataEntries[i][3] = new String(
					String.valueOf( (Integer.parseInt(tmpFileList[i][2]) / 1024) )
					+" KB"
					);
			dataEntries[i][4] = new String(tmpFileList[i][3]);
		}
		
		table.setFlowTableModel(columnTitles, dataEntries);
		table.repaint();
		tmpFileList = null;

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
		Component c = (Component)evt.getSource();
		this.setBounds(0,0,c.getWidth(),c.getHeight());
		
		lblTitle.setBounds(80, 0, this.getWidth()-160, 30);
		txtAddress.setBounds(80, txtAddress.getY(), this.getWidth() - 160, txtAddress.getHeight());
		btnChkFileAll.setBounds(80, 105, 125, 30);
		btnSendFile.setBounds(215, 105, 125, 30);
		btnEncrypt.setBounds(this.getWidth() - (80+150), 105, 150, 30);
		scrFileList.setBounds(80, 150, this.getWidth() - 160, this.getHeight() - 250);
		table.setBounds(0, 0, scrFileList.getWidth(), scrFileList.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	// 리스트 항목 선택.
	@Override
	public void valueChanged(ListSelectionEvent e) {

		
		//chooseFileName = table.getSelectedIndex();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "table":
				break;
			case "Button_CheckFileAll":
				if(table.getRowCount()>0) {
					for(int i=0; i<table.getRowCount();i++) {
						Object tmpValCheck = table.getFlowTableModel().getValueAt(i, 0);
						table.getFlowTableModel().setValueAt(!(boolean)tmpValCheck, i, 0);
						table.repaint();
					}
				}
				break;
			case "Button_ShareFile":
				toSendFile();
				break;
			case "Button_Encrypt":
				//int row = table.getSelectedRow();
				setEncrypt();
				setRefreshList();
				break;
		}
	} // action

	public void toSendFile() {
		if(table.getRowCount()>0) {
			int tmpNoChk=0;
			for(int i=0; i<table.getRowCount();i++) {
				// #############################################################
				boolean tmpChk = (Boolean)table.getFlowTableModel().getValueAt(i, 0);
				if(tmpChk==true) {
				// #############################################################					
					// clsFileList.getEncryptStatus("");
					String tmpFilePath = (String) table.getFlowTableModel().getValueAt(i, 4) + "\\" + table.getFlowTableModel().getValueAt(i, 1) ;
					clsParent.pnlShareFile.setSendFile(tmpFilePath);
					break;
				// #############################################################
				}else {
				// #############################################################
					tmpNoChk++;
				// #############################################################
				}
				// #############################################################
			}
			
			if(table.getRowCount()==tmpNoChk) clsParent.clsTrayIcon.showMessage("파일 선택", "공유할 파일을 먼저 선택 하세요.", 1);
			
		}
	}
	
	public void setEncrypt() {
		if(table.getRowCount()>0) {
			for(int i=0; i<table.getRowCount();i++) {
				// #############################################################
				boolean tmpChk = (Boolean)table.getFlowTableModel().getValueAt(i, 0);
				if(tmpChk==true) {
				// #############################################################
					String tmpPath = (String) table.getFlowTableModel().getValueAt(i, 4) + "\\"; //clsFileList.getPath()+"\\";
					String tmpFileName = (String) table.getFlowTableModel().getValueAt(i, 1);
					String tmpFileParameter = tmpFileName.substring(tmpFileName.lastIndexOf(".")+1);
					tmpFileParameter = tmpFileName.substring(tmpFileName.lastIndexOf("_")+1);
					
					if (!tmpFileParameter.equals("E")) {
						try {
								// 암호화 시키기
								boolean tmpVal = clsParent.clsEncry.encrypt(new File(tmpPath+tmpFileName), new File(tmpPath+tmpFileName + "_E"));
								
								if(tmpVal==true) {
									System.out.print("[Flow _ File Explorer Frame] 암호화 작업 시작 ▶ ");
				
									// 암호화시 원본 파일 지우기.
									f = new File(tmpPath+tmpFileName);
									System.out.print("암호화된 파일 생성 ▶ ");
									
									f.delete();
									f = null;
									System.out.print("원본 파일 삭제 ["+(i+1)+" / "+table.getRowCount()+"]\n");
							}else {
								System.out.println("[Flow _ File Explorer Frame] 암호화 작업에 실패 했습니다.");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						// 복구화 시키기.
						try {
							
							int tmpFileNameStart = tmpFileName.indexOf("_E");
							String tmpFileNameDecrypt = tmpFileName.substring(0, tmpFileNameStart);
							
							// 암호화 된 파일 복구 시키기.
							boolean tmpVal = clsParent.clsEncry.decrypt(new File(tmpPath+tmpFileName), new File(tmpPath+tmpFileNameDecrypt));
							if(tmpVal==true) {
								System.out.print("[Flow _ File Explorer Frame] 복호화 작업 시작 ▶ ");
				
								f = new File(tmpPath+tmpFileName);
								System.out.print("원본 파일 생성 ▶ ");
								
								f.delete();
								f = null;
								System.out.print("암호화된 파일 삭제 ["+(i+1)+" / "+table.getRowCount()+"]\n");
							}else {
								System.out.println("[Flow _ File Explorer Frame] 복호화 작업에 실패 했습니다.");
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				// #############################################################
				}
				// #############################################################
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getSource() == table) {

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		/*
		 *  116 : F5
		 *  10 : Enter
		 *  17 : Ctrl
		 *  65 : A
		 */
		if(e.getKeyCode() == 116) {
			// 파일 리스트 새로고침 동작
			setRefreshList();
		}else if(e.getKeyCode() == 65) {
			// 전체 선택
			if(table.getRowCount()>0) {
				for(int i=0; i<table.getRowCount();i++) {
					Object tmpValCheck = table.getFlowTableModel().getValueAt(i, 0);
					table.getFlowTableModel().setValueAt(!(boolean)tmpValCheck, i, 0);
					table.repaint();
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}