package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import module.modCoreWindow;
import module.modSysData;
import module.modSysTheme.flowButton;
import module.modSysTheme.flowProgressbar;

public class fraSetting extends JPanel implements ActionListener, ComponentListener{
	private static modCoreWindow clsParent;
	private static modSysData clsSysData;
	
	private boolean blAbout = false;
	
	JLabel lblCredit = new JLabel();
	JLabel lblTitle = new JLabel("Flow�� ���� �� ���ۿ� ���� ���� ���� �����մϴ�.");
	JLabel lblMemInfo = new JLabel("Memory Information");
	JLabel lblProgramInfo = new JLabel("Flow Companion Information");
	flowButton btnAbout = new flowButton("���α׷� ����");
	flowButton btnClean = new flowButton("�޸� ����");
	flowProgressbar pgbMemInfo = new flowProgressbar();
	
	public fraSetting(modCoreWindow clsParent, modSysData clsSysData){
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		clsParent.getRootPane().addComponentListener(this);

		this.setLayout(null);
		this.setBackground(new Color(255,255,255));

		// ������
		lblCredit.setOpaque(false);
		lblCredit.setFont(new Font("Malgun Gothic",0,14));
		lblCredit.setIcon(new ImageIcon(getClass().getClassLoader().getResource("flow_sys_credit_img.png")));
		lblCredit.setBounds(0, 0, 684, 382);
		lblCredit.setVisible(false);
		
		lblTitle.setOpaque(false);
		lblTitle.setFont(new Font("Malgun Gothic",0,14));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setBounds(80, 0, 200, 30);
		lblTitle.setVisible(true);

		lblMemInfo.setOpaque(false);
		lblMemInfo.setFont(new Font("Malgun Gothic",0,14));
		lblMemInfo.setHorizontalAlignment(SwingConstants.LEFT);
		lblMemInfo.setBounds(80, 50, 200, 30);
		lblMemInfo.setVisible(true);

		btnAbout.setFont(new Font("Malgun Gothic",1,14));
		btnAbout.setHorizontalAlignment(SwingConstants.CENTER);
		btnAbout.setBounds(280,  0, 125, 30);
		btnAbout.setActionCommand("about");
		btnAbout.addActionListener(this);
		btnAbout.setVisible(true);
		
		btnClean.setFont(new Font("Malgun Gothic",1,14));
		btnClean.setHorizontalAlignment(SwingConstants.CENTER);
		btnClean.setBounds(280,  50, 100, 30);
		btnClean.setActionCommand("memclean");
		btnClean.addActionListener(this);
		btnClean.setVisible(true);
		
		lblProgramInfo.setText("Flow Companion for Windows [Version "+clsSysData.sysVer+"]");
		lblProgramInfo.setOpaque(false);
		lblProgramInfo.setFont(new Font("Malgun Gothic",1,14));
		lblProgramInfo.setHorizontalAlignment(SwingConstants.LEFT);
		lblProgramInfo.setBounds(80, 300, 200, 30);
		lblProgramInfo.setVisible(true);
		
		pgbMemInfo.setText("�׽�Ʈ ���α׷�����");
		pgbMemInfo.setBounds(80,  100, 200, 30);
		pgbMemInfo.setVisible(true);
		
		// �гο� �߰�
		this.add(lblCredit);
		this.add(lblTitle);
		this.add(lblMemInfo);
		this.add(btnClean);
		this.add(pgbMemInfo);
		this.add(lblProgramInfo);
		this.add(btnAbout);
		
		this.setVisible(true);
	}

	public void setMemInfo() {
		double tmpValue = ((double)Runtime.getRuntime().freeMemory()/Runtime.getRuntime().totalMemory())*100;
		lblMemInfo.setText("�Ҵ�� ��ü �޸� : "+(Runtime.getRuntime().totalMemory()/1024/1024)+"MB / ���� �޸� : "+(Runtime.getRuntime().freeMemory()/1024/1024)+"MB");
		pgbMemInfo.setProgressValue( (int)tmpValue );
		this.repaint();
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
		
		setComponentZOrder(btnAbout, 2);
		lblCredit.setBounds((this.getWidth()-684)/2, this.getHeight()-(382+80), 684, 382);
		lblTitle.setBounds(80, 0, this.getWidth()-80, 30);
		lblMemInfo.setBounds(80, 50, this.getWidth()-(80+100), 30);
		btnClean.setBounds(this.getWidth()-(80+100), 50, 100, 30);
		pgbMemInfo.setBounds(80, 90, this.getWidth()-(80+80), 30);
		lblProgramInfo.setBounds(80, this.getHeight()-(100+30), this.getWidth()-(80+125), 30);
		btnAbout.setBounds(this.getWidth()-(80+125), this.getHeight()-(100+30), 125, 30);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch(e.getActionCommand()) {
			case "about":
				if(blAbout==true) {
					blAbout = false;
					btnAbout.setText("���α׷� ����");
					lblCredit.setVisible(false);
					lblTitle.setVisible(true);
					lblMemInfo.setVisible(true);
					btnClean.setVisible(true);
					pgbMemInfo.setVisible(true);
					lblProgramInfo.setVisible(true);
				}else {
					blAbout = true;
					btnAbout.setText("��������");
					lblCredit.setVisible(true);
					lblTitle.setVisible(false);
					lblMemInfo.setVisible(false);
					btnClean.setVisible(false);
					pgbMemInfo.setVisible(false);
					lblProgramInfo.setVisible(false);
				}
				break;
			case "memclean":
				clsParent.clsTrayIcon.showMessage("Memory Clean", "�� Runtime GC ����� �����ӿ�ũ �� JVM ���ۿ� ū ������ �ʷ��մϴ�.\n�������̸� �� ����� ������� ���ʽÿ�.", 2);
				Runtime.getRuntime().gc();
				clsParent.clsTrayIcon.showMessage("Memory Clean",
						"Finished the memory clean for the framework and JVM.\n\nTotal Memory : "+Runtime.getRuntime().totalMemory()+"\nFree Memory : "+Runtime.getRuntime().freeMemory(),
						0);
				break;
		}
	}
}
