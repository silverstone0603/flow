package module;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class modSysNotificator implements ActionListener {
   private modCoreWindow clsParent;
   private modSysData clsSysData;
   
	private SystemTray systemTray;
	private TrayIcon trayIcon;
	private PopupMenu mPopup;
	private MenuItem mItemNew, mItemOpen, mItemSave, mItemExit;

	public modSysNotificator(modCoreWindow clsParent, modSysData clsSysData) {
		this.clsParent = clsParent;
		this.clsSysData = clsSysData;
		
		try {
			initSystemTrayIcon();
		} catch (AWTException awte) {
			System.out.println("[Flow _ Notification Manager] Error occurred during create the Flow User Interface.");
			System.out.println(awte.toString()+"\n");
		}
	}

	public void initSystemTrayIcon() throws AWTException {
		if (SystemTray.isSupported()) {
			mPopup = new PopupMenu();
			mItemNew = new MenuItem("Flow ����");
			mItemOpen = new MenuItem("��ȣȭ�� ���� ����");
			mItemSave = new MenuItem("���� ��ȣȭ �ϱ�");
			mItemExit = new MenuItem("���α׷� ����");

			mItemNew.addActionListener(this);
			mItemOpen.addActionListener(this);
			mItemSave.addActionListener(this);
			mItemExit.addActionListener(this);

			mPopup.add(mItemNew);
			mPopup.addSeparator();
			/*
			mPopup.add(mItemOpen);
			mPopup.add(mItemSave);
			mPopup.addSeparator();
			*/
			mPopup.add(mItemExit);

			Image image = Toolkit.getDefaultToolkit().getImage(
					getClass().getClassLoader().getResource("flow_sys_trayicon_img.png")
					);
			
			trayIcon = new TrayIcon(image, "Flow Companion for Windows", mPopup);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(this);

			systemTray = SystemTray.getSystemTray();
			systemTray.add(trayIcon);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == trayIcon) {
			if(clsParent.isVisible() == false) {
				clsParent.setVisible(true);
				System.out.println("[Flow _ Window Frame] Windows�� â�� ǥ���մϴ�.");
			}
		} else if (ae.getSource() == mItemNew) {
			if(clsParent.isVisible() == false) {
				clsParent.setVisible(true);
				System.out.println("[Flow _ Window Frame] Windows�� â�� ǥ���մϴ�.");
			}
		} else if (ae.getSource() == mItemOpen) {
			showMessage("File Open...", "������ ���ϴ�.", 0);
		} else if (ae.getSource() == mItemSave) {
			showMessage("File Save...", "������ �����մϴ�.", 0);
		} else if (ae.getSource() == mItemExit) {
			if(clsSysData.getLoginValue()==true) {
				clsSysData.setLogout();
				JOptionPane.showMessageDialog(null, "���α׷��� ����Ǿ� �ڵ����� �α׾ƿ� �Ǿ����ϴ�.", "Flow Companion", JOptionPane.YES_NO_CANCEL_OPTION);
			}
			
			clsParent.dispose();
			System.out.println("[Flow _ Window Frame] �����ӿ�ũ�� �����մϴ�.");
			System.exit(0);
		}
	}

	public void showMessage(String title, String message, int messageType) {
		switch(messageType) {
		case 0:
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
			break;
		case 1:
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
			break;
		case 2:
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			break;
		}
	}
	
	public void showAlert(String title, String message, MessageType messageType) {
		trayIcon.displayMessage(title, message, messageType);
	}
}