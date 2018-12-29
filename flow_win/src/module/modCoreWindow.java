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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.fraFileExplorer;
import main.fraLogin;
import main.fraMain;
import main.fraSetting;
import main.fraShareFile;
import main.fraToolbar;


/*
 *************************************
 * 참고 자료
 ************************************* 
 * setBounds 순서 _ setBounds(x, y, w, h)
 * 
 * 
 */

public class modCoreWindow extends JFrame implements Runnable, ActionListener, ListSelectionListener, ComponentListener{
	private final int SIZE_MENU = 80;
	
	// 시스템 핵심 모듈
	private static modSysData clsSysData;
	private static modFileList clsFileList;
	public static modDBMS clsDBMS = new modDBMS();
	public static modSocket clsSocket;
	public static modEncryption clsEncry;
	public static modSysNotificator clsTrayIcon;
	public static modSysTheme clsSysTheme;
	
	// 메뉴 모듈
	public CardLayout layCards = new CardLayout();
	public fraToolbar pnlMenu;
	
	// 하단 컨텐츠 모듈
	public JPanel pnlContents = new JPanel();
	public fraMain pnlMain;
	public fraLogin pnlLogin;
	public fraFileExplorer pnlFileExplorer;
	public fraShareFile pnlShareFile;
	public fraSetting pnlSetting;
	
	public modCoreWindow() {
		// 동작에 필요한 개체 생성
		makeComponent();
	}

	private void makeComponent() {
		System.out.println("[Flow _ Window Frame] 프레임워크 초기화를 시작합니다.");

		// Windows 테마 사용 설정
		System.out.print("[Flow _ Window Frame] Windows 기본 테마를 사용하도록 설정합니다...");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultLookAndFeelDecorated(true);
		System.out.println("성공");

		clsSysData = new modSysData(this, this.clsDBMS);
		System.out.println("[Flow _ Window Frame] Flow Companion on Java Virtual Machine [Version "+clsSysData.sysVer+"]");
		
		// 경로 설정
		String tmpPath;
		tmpPath =JOptionPane.showInputDialog(null, "Flow 기본 경로를 입력 하십시오.");
		tmpPath = tmpPath.replace("\\", "\\\\");
		// tmpPath = "C:\\Flow";
		
		if(tmpPath.equals("")) {
			System.out.println("[Flow _ Window Frame] 경로가 존재하지 않습니다. 프레임 초기화를 종료합니다.");
			this.dispose();
			System.exit(0);
		}else {
			System.out.println("[Flow _ File Management Object] 설정된 경로는 " + tmpPath + " 입니다.");
			clsFileList = new modFileList(tmpPath);
		}

		// 암호화 객체 생성
		clsEncry = new modEncryption(this.clsSysData);

		// 소켓 통신 객체 생성
		clsSocket = new modSocket(this, this.clsSysData);
		
		clsTrayIcon = new modSysNotificator(this, this.clsSysData);
		clsSysTheme = new modSysTheme();

		// DBMS 연동 확인
		if(clsSysData.getDBConnect()==0) {
			clsTrayIcon.showMessage("서버 접속 실패", "Flow 서버 접속에 실패했습니다.\n사용자의 네트워크 관리자 또는 Flow 담당자에게 문의하십시오.", 2);
			this.dispose();
			System.exit(0);
		}
		
		pnlMain = new fraMain(this, this.clsSysData, this.clsFileList);
		pnlLogin = new fraLogin(this, this.clsSysData);
		pnlFileExplorer = new fraFileExplorer(this, this.clsFileList, this.clsSysData);
		pnlShareFile = new fraShareFile(this, this.clsSysData);
		pnlSetting = new fraSetting(this, this.clsSysData);

		this.getRootPane().addComponentListener(this);
		setTitle("Flow Companion");

		// 아이콘 설정
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getClassLoader().getResource("flow_sys_trayicon_img.png")
				));

		setResizable(true);
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// 창을 닫으면 프로그램을 종료할 경우 : setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		// 콘텐츠 패널 생성
		pnlContents.setBounds(0, this.SIZE_MENU, 684, 382);
		getContentPane().add(pnlContents);
		pnlContents.setLayout(layCards);

		pnlContents.add(pnlMain, "FormMain");
		pnlContents.add(pnlLogin, "FormLogin");
		pnlContents.add(pnlFileExplorer, "FormFileExplorer");
		pnlContents.add(pnlShareFile, "FormShareFile");
		pnlContents.add(pnlSetting, "FormSetting");
		System.out.println("[Flow _ Window Frame] 각각의 콘텐츠 패널을 로드합니다.");

		// 메뉴 생성
		pnlMenu = new fraToolbar(this, this.clsSysData);

		addWindowListener(new ExitHandler(this));
		setLocationRelativeTo(null);
		setMinimumSize(this.getSize());
		setVisible(true);

		System.out.println("[Flow _ Notification Manager] 알림 관리자를 시작합니다.");
		clsTrayIcon.showAlert("Flow Companion", "동기화를 위해 프로그램이 항상 실행됩니다.", MessageType.INFO);

		System.out.println("[Flow _ Window Frame] 프레임워크 초기화에 성공했습니다.");
		
		// 파일 공유 
		clsSocket.startReceive();
		
		// Runnable Thread 실행
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

			// 메인 창에서만 종료 팝업을 표시하도록 설정
			if((tmpFrame.getMenuNum() == 0 && tmpExitSign != 0) || clsSysData.getLoginValue()==false) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Flow Companion을 종료할까요?", "Flow Companion", JOptionPane.YES_NO_OPTION);
				tmpExitSign = dialogResult;
				if(dialogResult == 0) {
					System.out.println("[Flow _ Account Manager] 계정 로그아웃을 진행합니다.");
					frame.dispose();
					System.out.println("[Flow _ Window Frame] 프레임워크를 종료합니다.");
					System.exit(0);
				} else {
					frame.setVisible(false);
					System.out.println("[Flow _ Window Frame] 시스템 트레이 영역으로 전환합니다.");
				}
			}else {
				// 최초 팝업에서 종료하도록 설정하지 않은 경우
				frame.setVisible(false);
				System.out.println("[Flow _ Window Frame] 시스템 트레이 영역으로 전환합니다.");
			}
		}
	}

	static String getTime() {
		// 현재시간을 문자열로 반환하는 함수
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss]");
		return f.format(new Date());
	}

	@Override
	public void componentResized(ComponentEvent evt) {
		// 컨트롤 사이즈 조정
		Component c = (Component)evt.getSource();
		pnlMenu.setBounds(0, 0, c.getWidth(), this.SIZE_MENU);
		pnlContents.setBounds(0, 80, c.getWidth(), c.getHeight()-this.SIZE_MENU);
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
		File[] drvOrigin = null;
		File[] drvPrevious = null;
		try{
			while(true) {
				pnlSetting.setMemInfo();
				Runtime.getRuntime().gc();
				
				pnlMain.setRefreshChart();
				
				if(clsSysData.getLoginValue()==true) {
					drvOrigin = clsFileList.getDrive();
					if(drvPrevious == null) drvPrevious = clsFileList.getDrive();
					
					if(drvOrigin.length > drvPrevious.length) {
						// 드라이브 갯수가 늘었을 경우
						if(this.isVisible()==false) this.setVisible(true);
						clsTrayIcon.showAlert("Flow Companion", "새로운 장치가 인식되었습니다. 파일 탐색기 화면에서 선택해주세요.", MessageType.INFO);
					}else if(drvOrigin.length < drvPrevious.length) {
						// 드라이브 갯수가 줄어들었을 경우
						clsTrayIcon.showAlert("Flow Companion", "장치가 제거되었습니다. 동기화 목록을 다시 불러옵니다.", MessageType.INFO);
					}
					drvPrevious = drvOrigin;
				}
				
				Thread.sleep(1000);
			}
		}catch (Exception e) {
			System.out.println("[Flow Runtime Error] "+e);
		}
	}
}
