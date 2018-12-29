package main;

import module.*;
import module.modSysTheme.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class fraMain extends JPanel implements ActionListener, ComponentListener{
	private modCoreWindow clsParent;
	private modSysData clsSysData;
	private modFileList clsFileList;
	private modSysTheme clsTheme = new modSysTheme();

	Container contentPane; // 컨테이너 생성

	JTextField[] tf  = new JTextField[2]; // 텍스트필드
	modChartPanel chrtPanel = new modChartPanel(clsFileList, this);
	JLabel lblGroupTitle1, lblGroupTitle12, lblGroupSubTitle1;

	JLabel lblGroup2, lblGroupTitle2, imgStatus;
	flowButton lblGroupSubTitle2;

	String[][] tmpFileList;

	public fraMain(modCoreWindow clsParent, modSysData clsSysData, modFileList clsFile){
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		this.clsFileList = clsFile;

		clsParent.getRootPane().addComponentListener(this);

		this.setLayout(null);
		this.setBackground(new Color(255,255,255));

		tmpFileList = clsFileList.getList();

		// 스토리지 사용량 프레임
		JLabel lblGroup1 = new JLabel("암호화된 파일 정보");
		lblGroup1.setOpaque(false);
		lblGroup1.setFont(new Font("Malgun Gothic",1,15));
		lblGroup1.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroup1.setBounds(50, 30, 200, 30);
		lblGroup1.setVisible(true);

		// 수정 해야될 부분
		lblGroupTitle1 = new JLabel("암호화 안된 파일 : "+clsFile.deCnt+"개 ("+Math.round(chrtPanel.arcAngle[0]*100/360)+"%)");
		lblGroupTitle12 = new JLabel("암호화된 파일 : "+clsFile.enCnt+"개 ("+Math.round(chrtPanel.arcAngle[1]*100/360)+"%)");

		lblGroupTitle1.setOpaque(false);
		lblGroupTitle1.setFont(new Font("Malgun Gothic",1,12));
		lblGroupTitle1.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupTitle1.setBounds(220, 75, 170, 30);
		lblGroupTitle1.setVisible(true);
		lblGroupTitle12.setOpaque(false);
		lblGroupTitle12.setFont(new Font("Malgun Gothic",1,12));
		lblGroupTitle12.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupTitle12.setBounds(220, 75, 170, 65);
		lblGroupTitle12.setVisible(true);

		lblGroupSubTitle1 = new JLabel("스토리지에 저장된 파일에 대한 정보가 여기에 표시됩니다.");
		lblGroupSubTitle1.setOpaque(false);
		lblGroupSubTitle1.setFont(new Font("Malgun Gothic",1,12));
		lblGroupSubTitle1.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupSubTitle1.setBounds(220, 110, 350, 30);
		lblGroupSubTitle1.setVisible(true);

		// 스토리지 데이터 동기화 상태 프레임
		lblGroup2 = new JLabel("Flow ID 사용자 정보");
		lblGroup2.setOpaque(false);
		lblGroup2.setFont(new Font("Malgun Gothic",1,15));
		lblGroup2.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroup2.setBounds(50, 210, 200, 30);
		lblGroup2.setVisible(true);
		
		imgStatus = new JLabel();
		imgStatus.setBounds(50, 240, 80, 80);
		imgStatus.setBorder(null);
		imgStatus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("flow_icon_notlogin.png")));
		imgStatus.setVisible(true);

		lblGroupTitle2 = new JLabel("계정 로그인 후 이용하실 수 있습니다.");
		lblGroupTitle2.setOpaque(false);
		lblGroupTitle2.setFont(new Font("Malgun Gothic",1,12));
		lblGroupTitle2.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupTitle2.setBounds(140, 260, 350, 20);
		lblGroupTitle2.setVisible(true);

		lblGroupSubTitle2 = new flowButton("> 계정 로그인");
		lblGroupSubTitle2.setOpaque(false);
		lblGroupSubTitle2.setFont(new Font("Malgun Gothic",1,12));
		lblGroupSubTitle2.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupSubTitle2.setBounds(140, 280, 350, 30);
		lblGroupSubTitle2.addActionListener(this);
		lblGroupSubTitle2.setBorderSize(0);
		lblGroupSubTitle2.setVisible(true);

		// 스토리지 사용량 프레임
		this.add(lblGroup1);
		this.add(chrtPanel);
		this.add(lblGroupTitle1);
		this.add(lblGroupTitle12);
		this.add(lblGroupSubTitle1);

		// 스토리지 데이터 동기화 상태 프레임
		this.add(lblGroup2);
		this.add(imgStatus);
		this.add(lblGroupTitle2);
		this.add(lblGroupSubTitle2);

		setRefreshChart();
		this.setVisible(true);
	}

	public void setRefreshChart() {
		tmpFileList = clsFileList.getList();
		chrtPanel.setData(clsFileList.deCnt, clsFileList.enCnt);
		chrtPanel.drawChart();
		lblGroupTitle1.setText("암호화 안된 파일 : "+clsFileList.deCnt+"개 ("+Math.round(chrtPanel.arcAngle[0]*100/360)+"%)");
		lblGroupTitle12.setText("암호화된 파일 : "+clsFileList.enCnt+"개 ("+Math.round(chrtPanel.arcAngle[1]*100/360)+"%)");
		lblGroupSubTitle1.setText("보안을 위해 파일 전체를 암호화하는 것을 권장합니다.");
		
		tmpFileList = null;
	}

	public void setUserInfoArea(boolean valLogin) {
		if(valLogin==true) {
			imgStatus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("flow_icon_login.png")));
			lblGroupTitle2.setText(clsSysData.getUserID()+"님의 Flow 정보가 동기화 중입니다.");
			lblGroupSubTitle2.setText("> 계정 로그아웃");
		}else {
			imgStatus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("flow_icon_notlogin.png")));
			lblGroupTitle2.setText("계정 로그인 후 이용하실 수 있습니다.");
			lblGroupSubTitle2.setText("> 계정 로그인");
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
		Component c = (Component)evt.getSource();
		this.setBounds(0, 0, c.getWidth(), c.getHeight() - clsParent.getSizeMenu());

	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Component c = (Component) e.getSource();
		if(e.getSource()==lblGroupSubTitle2) {
			clsParent.pnlMenu.setMenuNum(1);
		}
	}
}