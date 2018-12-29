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

	Container contentPane; // �����̳� ����

	JTextField[] tf  = new JTextField[2]; // �ؽ�Ʈ�ʵ�
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

		// ���丮�� ��뷮 ������
		JLabel lblGroup1 = new JLabel("��ȣȭ�� ���� ����");
		lblGroup1.setOpaque(false);
		lblGroup1.setFont(new Font("Malgun Gothic",1,15));
		lblGroup1.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroup1.setBounds(50, 30, 200, 30);
		lblGroup1.setVisible(true);

		// ���� �ؾߵ� �κ�
		lblGroupTitle1 = new JLabel("��ȣȭ �ȵ� ���� : "+clsFile.deCnt+"�� ("+Math.round(chrtPanel.arcAngle[0]*100/360)+"%)");
		lblGroupTitle12 = new JLabel("��ȣȭ�� ���� : "+clsFile.enCnt+"�� ("+Math.round(chrtPanel.arcAngle[1]*100/360)+"%)");

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

		lblGroupSubTitle1 = new JLabel("���丮���� ����� ���Ͽ� ���� ������ ���⿡ ǥ�õ˴ϴ�.");
		lblGroupSubTitle1.setOpaque(false);
		lblGroupSubTitle1.setFont(new Font("Malgun Gothic",1,12));
		lblGroupSubTitle1.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupSubTitle1.setBounds(220, 110, 350, 30);
		lblGroupSubTitle1.setVisible(true);

		// ���丮�� ������ ����ȭ ���� ������
		lblGroup2 = new JLabel("Flow ID ����� ����");
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

		lblGroupTitle2 = new JLabel("���� �α��� �� �̿��Ͻ� �� �ֽ��ϴ�.");
		lblGroupTitle2.setOpaque(false);
		lblGroupTitle2.setFont(new Font("Malgun Gothic",1,12));
		lblGroupTitle2.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupTitle2.setBounds(140, 260, 350, 20);
		lblGroupTitle2.setVisible(true);

		lblGroupSubTitle2 = new flowButton("> ���� �α���");
		lblGroupSubTitle2.setOpaque(false);
		lblGroupSubTitle2.setFont(new Font("Malgun Gothic",1,12));
		lblGroupSubTitle2.setHorizontalAlignment(SwingConstants.LEFT);
		lblGroupSubTitle2.setBounds(140, 280, 350, 30);
		lblGroupSubTitle2.addActionListener(this);
		lblGroupSubTitle2.setBorderSize(0);
		lblGroupSubTitle2.setVisible(true);

		// ���丮�� ��뷮 ������
		this.add(lblGroup1);
		this.add(chrtPanel);
		this.add(lblGroupTitle1);
		this.add(lblGroupTitle12);
		this.add(lblGroupSubTitle1);

		// ���丮�� ������ ����ȭ ���� ������
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
		lblGroupTitle1.setText("��ȣȭ �ȵ� ���� : "+clsFileList.deCnt+"�� ("+Math.round(chrtPanel.arcAngle[0]*100/360)+"%)");
		lblGroupTitle12.setText("��ȣȭ�� ���� : "+clsFileList.enCnt+"�� ("+Math.round(chrtPanel.arcAngle[1]*100/360)+"%)");
		lblGroupSubTitle1.setText("������ ���� ���� ��ü�� ��ȣȭ�ϴ� ���� �����մϴ�.");
		
		tmpFileList = null;
	}

	public void setUserInfoArea(boolean valLogin) {
		if(valLogin==true) {
			imgStatus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("flow_icon_login.png")));
			lblGroupTitle2.setText(clsSysData.getUserID()+"���� Flow ������ ����ȭ ���Դϴ�.");
			lblGroupSubTitle2.setText("> ���� �α׾ƿ�");
		}else {
			imgStatus.setIcon(new ImageIcon(getClass().getClassLoader().getResource("flow_icon_notlogin.png")));
			lblGroupTitle2.setText("���� �α��� �� �̿��Ͻ� �� �ֽ��ϴ�.");
			lblGroupSubTitle2.setText("> ���� �α���");
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