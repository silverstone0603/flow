package module;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.fraLogin;
import main.fraToolbar;
import main.fraUserList;


/*
 *************************************
 * ���� �ڷ�
 ************************************* 
 * setBounds ���� _ setBounds(x, y, w, h)
 * 
 * 
 */

public class modCoreWindow extends JFrame implements Runnable, ActionListener, ListSelectionListener, ComponentListener{
	private final int SIZE_MENU = 80;

	// �ý��� �ٽ� ���
	private static modSysData clsSysData;
	public static modSysNotificator clsTrayIcon;
	public static modSysTheme clsSysTheme;

	// �޴� ���
	public CardLayout layCards = new CardLayout();
	public fraToolbar pnlMenu;

	// �ϴ� ������ ���
	public JPanel pnlContents = new JPanel();
	public fraLogin pnlLogin;
	public fraUserList pnlUserList;

	public modCoreWindow() {
		// ���ۿ� �ʿ��� ��ü ����
		makeComponent();
	}

	private void makeComponent() {
		System.out.println("[Flow _ Window Frame] �����ӿ�ũ �ʱ�ȭ�� �����մϴ�.");

		// Windows �׸� ��� ����
		System.out.print("[Flow _ Window Frame] Windows �⺻ �׸��� ����ϵ��� �����մϴ�...");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultLookAndFeelDecorated(true);
		System.out.println("����");

		clsSysData = new modSysData();
		System.out.println("[Flow _ Window Frame] Flow Companion on Java Virtual Machine [Version "+clsSysData.sysVer+"]");
		
		clsTrayIcon = new modSysNotificator(this, this.clsSysData);
		clsSysTheme = new modSysTheme();

		// DBMS ���� Ȯ��
		if(clsSysData.getDBConnect()==0) {
			clsTrayIcon.showMessage("���� ���� ����", "Flow ���� ���ӿ� �����߽��ϴ�.\n������� ��Ʈ��ũ �Ǵ� �����ڿ��� �����Ͻʽÿ�,", 2);
			this.dispose();
			System.exit(0);
		}
		
		pnlLogin = new fraLogin(this, this.clsSysData);
		pnlUserList = new fraUserList(this, this.clsSysData);

		this.getRootPane().addComponentListener(this);
		setTitle("Flow Session Management Program [Version "+clsSysData.sysVer+"]");

		// ������ ����
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getClassLoader().getResource("flow_sys_trayicon_img.png")
				));

		setResizable(true);
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// â�� ������ ���α׷��� ������ ��� : setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		// ������ �г� ����
		pnlContents.setBounds(0, this.SIZE_MENU, 684, 382);
		getContentPane().add(pnlContents);
		pnlContents.setLayout(layCards);

		pnlContents.add(pnlLogin, "FormMain");
		pnlContents.add(pnlUserList, "FormUserList");
		System.out.println("[Flow _ Window Frame] ������ ������ �г��� �ε��մϴ�.");

		// �޴� ����
		pnlMenu = new fraToolbar(this, this.clsSysData);

		addWindowListener(new ExitHandler(this));
		setLocationRelativeTo(null);
		setMinimumSize(this.getSize());
		setVisible(true);

		System.out.println("[Flow _ Notification Manager] �˸� �����ڸ� �����մϴ�.");
		clsTrayIcon.showAlert("Flow Session Manager", "����ȭ�� ���� ���α׷��� �׻� ����˴ϴ�.", MessageType.INFO);

		System.out.println("[Flow _ Window Frame] �����ӿ�ũ �ʱ�ȭ�� �����߽��ϴ�.");

		// Runnable Thread ����
		run();
	}

	public int getSizeMenu(){
		return this.SIZE_MENU;
	}

	public int getMenuNum(){
		return pnlMenu.getMenuNum();
	}

	public void ListSelectionListener(ListSelectionEvent e) {

	}
	public void ActionListener(ActionEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	class ExitHandler extends WindowAdapter{
		private modCoreWindow tmpFrame;
		private int tmpExitSign;

		public ExitHandler(modCoreWindow tmpFrame) {
			this.tmpFrame = tmpFrame;
		}

		public void windowClosing(WindowEvent e){
			JFrame frame = (JFrame)e.getWindow();

			// ���� â������ ���� �˾��� ǥ���ϵ��� ����
			if((tmpFrame.getMenuNum() == 0 && tmpExitSign != 0) || clsSysData.getLoginValue()==false) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Flow Companion�� �����ұ��?", "Flow Companion", JOptionPane.YES_NO_OPTION);
				tmpExitSign = dialogResult;
				if(dialogResult == 0) {
					System.out.println("[Flow _ Account Manager] ���� �α׾ƿ��� �����մϴ�.");
					frame.dispose();
					System.out.println("[Flow _ Window Frame] �����ӿ�ũ�� �����մϴ�.");
					System.exit(0);
				} else {
					frame.setVisible(false);
					System.out.println("[Flow _ Window Frame] �ý��� Ʈ���� �������� ��ȯ�մϴ�.");
				}
			}else {
				// ���� �˾����� �����ϵ��� �������� ���� ���
				frame.setVisible(false);
				System.out.println("[Flow _ Window Frame] �ý��� Ʈ���� �������� ��ȯ�մϴ�.");
			}
		}
	}

	static String getTime() {
		// ����ð��� ���ڿ��� ��ȯ�ϴ� �Լ�
		SimpleDateFormat f = new SimpleDateFormat("[HH:mm:ss]");
		return f.format(new Date());
	}

	@Override
	public void componentResized(ComponentEvent evt) {
		// ��Ʈ�� ������ ����
		Component c = (Component)evt.getSource();
		pnlMenu.setBounds(0, 0, c.getWidth(), this.SIZE_MENU);
		pnlContents.setBounds(0, 80, c.getWidth(), c.getHeight()-this.SIZE_MENU);
		// System.out.println("[Flow _ Window Frame] â ũ�� : " + c.getWidth() + " x " + c.getHeight());
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		this.componentResized(arg0);
	}

	@Override
	public void componentMoved(ComponentEvent evt) {
		// TODO Auto-generated method stub
		this.componentResized(evt);
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		this.componentResized(arg0);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(true) {
				if(clsSysData.getLoginValue()==true) {

				}
				
				Thread.sleep(1000);
			}
		}catch (Exception e) {
			System.out.println("[Flow Runtime Error] "+e);
		}
	}
}
