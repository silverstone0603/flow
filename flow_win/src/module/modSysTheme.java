package module;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;

public class modSysTheme{

	/*****************************************************
	 *		
	 *		flowButton
	 *		일반 JButton과 동일하게 사용 가능
	 *		
	 *		사용법 :
	 *		flowButton btnTest = new flowButton("버튼 이름");
	 *		
	 *****************************************************/
	public static class flowProgressbar extends JLabel{
		private int btnBorderSize = 1;
		private int valProgress = 0;

		public flowProgressbar(){
			super.setFont(new Font("Malgun Gothic",1,14));
			super.setHorizontalAlignment(SwingConstants.CENTER);
			super.setForeground(Color.BLACK);
			super.setSize(100, 75);
		}
		
		public void setBorderSize(int tmpSize) {
			this.btnBorderSize = tmpSize;
			super.repaint();
		}

		public void setProgressValue(int tmpVal) {
			this.valProgress = tmpVal;
			super.setText(tmpVal+"%");
			super.repaint();
		}
		
		@Override
		protected void paintBorder(Graphics g) {
			Graphics2D g1 = (Graphics2D)g.create();
			Border sysBorder;

			sysBorder = BorderFactory.createLineBorder(new Color(41, 82, 161));
			sysBorder = BorderFactory.createEmptyBorder(10 , 10 , 10 , 10); //상하좌우 10씩 띄우기
			super.setBorder(sysBorder);
			sysBorder = null;

			// Border Color
			g1.setPaint(
					new GradientPaint(
							new Point(0, getHeight()),
							new Color(126,153,0), //색상 지정
							new Point(getWidth(), getHeight()),
							new Color(0,110,117) //색상 지정
							)
					);
			g1.fillRect(0, 0, getWidth(), btnBorderSize);
			g1.fillRect(0, 0, btnBorderSize, getHeight());
			g1.fillRect(getWidth()-btnBorderSize, 0, btnBorderSize, getHeight());
			g1.fillRect(0, getHeight()-btnBorderSize, getWidth(), btnBorderSize);
			g1.dispose();
			super.paintBorder(g);
		}

		@Override
		protected void paintComponent(Graphics g){
			Graphics2D g1 = (Graphics2D)g.create();
			double tmpPercent = ((double)this.valProgress/100)*getWidth();
			int tmpWidth = 0;
			
			// 퍼센트 구하는 규칙 : (입력값/전체값)*100	
			if(this.valProgress <= 0) {
				tmpWidth = 0;
			}else if(this.valProgress >= 100) {
				tmpWidth = getWidth();
			}else {
				tmpWidth = (int)tmpPercent;
			}
			
			if(this.valProgress>=50) {
				super.setForeground(Color.WHITE);
			}else {
				super.setForeground(Color.BLACK);
			}

			// Background Color
			g1.setPaint(
					new GradientPaint(
							new Point(0, getHeight()),
							new Color(126,153,0), //색상 지정
							new Point(tmpWidth, getHeight()),
							new Color(0,110,117) //색상 지정
							)
					);
			g1.fillRect(0, 0, tmpWidth, getHeight());
			
			g1.dispose();
			super.paintComponent(g);
		}
	}

	/*****************************************************
	 *		
	 *		flowTable
	 *		JTable과 동일하게 사용가능하나 Model이 이미 생성되어 있어서 값만 추가해주는 방식이면 됨.
	 *
	 *		사용법 :
	 *		flowTable tableTest = new flowTable(columnTitle, dataEntries);
	 *		
	 *		이동 안되게 할 때 :
	 *		tableTest.getTableHeader().setReorderingAllowed(false);
	 *
	 *		크기 조절이 안되도록 할 때 :
	 *		tableTest.getTableHeader().setResizingAllowed(false);
	 *
	 *
	 *		각종 컨트롤을 컬럼에 추가하고 싶을 때 사용법 :
	 *			String[] bloodGroups = { "A", "B", "AB", "O" };
	 *			JComboBox comboBox = new JComboBox(bloodGroups);
	 *			tableTest.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox));
	 *
	 *****************************************************/
	public static class flowTable extends JTable {
		private flowTableModel tableModel;

		public flowTable(String[] columnTitles, Object[][] dataEntries) {
			tableModel = new flowTableModel(columnTitles, dataEntries);
			super.setModel(tableModel);
			super.createDefaultColumnsFromModel();
		}

		// 특정 열의 편집 가능 여부 결과값 접근자
		public boolean isCellEditable(int row, int column) {
			return tableModel.isCellEditable(row, column);
		}

		// 특정 열을 편집 가능하게 만들 것인지 설정자
		public void setCellEditable(int column, boolean value) {
			//return true;
			tableModel.setCellEditable(column, value);
		}

		public flowTableModel getFlowTableModel() {
			return tableModel;
		}

		public void setFlowTableModel(String[] columnTitles, Object[][] dataEntries) {
			tableModel.columnTitles = columnTitles;
			tableModel.dataEntries = dataEntries;
		}

		public class flowTableModel extends AbstractTableModel{
			boolean[] columnEdited;
			String[] columnTitles;
			Object[][] dataEntries;

			int rowCount;

			public flowTableModel(String[] columnTitles, Object[][] dataEntries) {
				this.columnTitles = columnTitles;
				this.dataEntries = dataEntries;

				this.columnEdited = new boolean[columnTitles.length];
				Arrays.fill(columnEdited, false);
			}

			// 행 갯수가 변경 되었을 경우 초기화
			public void setColumnCount() {
				this.columnEdited = null;
				this.columnEdited = new boolean[columnTitles.length];
				Arrays.fill(columnEdited, false);
			}

			public int getRowCount() {
				return dataEntries.length;
			}

			public int getColumnCount() {
				return columnTitles.length;
			}

			public Object getValueAt(int row, int column) {
				return dataEntries[row][column];
			}

			public String getColumnName(int column) {
				return columnTitles[column];
			}

			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			public boolean isCellEditable(int row, int column) {
				//return true;
				return this.columnEdited[column];
			}

			public void setCellEditable(int column, boolean value) {
				//return true;
				this.columnEdited[column] = value;
			}

			public void setValueAt(Object value, int row, int column) {
				dataEntries[row][column] = value;
			}
		}
	}

	/*****************************************************
	 *		
	 *		flowTextField
	 *		일반 JTextField과 동일하게 사용 가능
	 *		
	 *		사용법 :
	 *		flowTextField txtTest = new flowTextField();
	 *		
	 *****************************************************/
	public static class flowPasswordField extends JPasswordField  implements FocusListener{
		private String strPlaceholder = "";

		public flowPasswordField(){
			super.setForeground(Color.BLACK);
			addFocusListener(this);
		}

		public void setPlaceHolder(String s) {
			this.strPlaceholder = s;
		}

		public void focusGained(FocusEvent e) {
			if (getText().equals(strPlaceholder)) {
				setText("");
				setForeground(Color.BLACK);
			}
		}

		public void focusLost(FocusEvent e) {
			if (getText().isEmpty()) {
				setForeground(Color.GRAY);
				setText(strPlaceholder);
			}
		}


		@Override
		protected void paintBorder(Graphics g) {
			Graphics2D g1 = (Graphics2D)g.create();
			Border sysBorder;
			final int BORDER_SIZE = 1;

			sysBorder = BorderFactory.createLineBorder(new Color(41, 82, 161));
			sysBorder = BorderFactory.createEmptyBorder(5 , 5 , 5 , 5); //상하좌우 10씩 띄우기
			super.setBorder(sysBorder);

			// Border Color
			g1.setPaint(
					new GradientPaint(
							new Point(0, getHeight()),
							new Color(126,153,0), //색상 지정
							new Point(getWidth(), getHeight()),
							new Color(0,110,117) //색상 지정
							)
					);
			g1.fillRect(0, 0, getWidth(), BORDER_SIZE);
			g1.fillRect(0, 0, BORDER_SIZE, getHeight());
			g1.fillRect(getWidth()-BORDER_SIZE, 0, BORDER_SIZE, getHeight());
			g1.fillRect(0, getHeight()-BORDER_SIZE, getWidth(), BORDER_SIZE);
			g1.dispose();
			super.paintBorder(g);
		}

		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
		}
	}
	/*****************************************************/


	/*****************************************************
	 *		
	 *		flowTextField
	 *		일반 JTextField과 동일하게 사용 가능
	 *		
	 *		사용법 :
	 *		flowTextField txtTest = new flowTextField();
	 *		
	 *****************************************************/
	public static class flowTextField extends JTextField implements FocusListener{
		private String strPlaceholder = "";

		public flowTextField(){
			super.setBounds(super.getX(), super.getY(), 200, 75);
			super.setForeground(Color.BLACK);
			addFocusListener(this);
			this.getActions();
		}

		public void setPlaceHolder(String s) {
			this.strPlaceholder = s;
			getFocusListeners();
		}

		@Override 
		public void focusGained(FocusEvent e) {
			if (getText().equals(strPlaceholder)) {
				setText("");
				setForeground(Color.BLACK);
			}
		}

		@Override 
		public void focusLost(FocusEvent e) {
			if (getText().isEmpty()) {
				setForeground(Color.GRAY);
				setText(strPlaceholder);
			}
		}

		@Override
		protected void paintBorder(Graphics g) {
			Graphics2D g1 = (Graphics2D)g.create();
			Border sysBorder;
			final int BORDER_SIZE = 1;

			sysBorder = BorderFactory.createLineBorder(new Color(41, 82, 161));
			sysBorder = BorderFactory.createEmptyBorder(5 , 5 , 5 , 5); //상하좌우 10씩 띄우기
			super.setBorder(sysBorder);
			sysBorder = null;

			// Border Color
			g1.setPaint(
					new GradientPaint(
							new Point(0, getHeight()),
							new Color(126,153,0), //색상 지정
							new Point(getWidth(), getHeight()),
							new Color(0,110,117) //색상 지정
							)
					);
			g1.fillRect(0, 0, getWidth(), BORDER_SIZE);
			g1.fillRect(0, 0, BORDER_SIZE, getHeight());
			g1.fillRect(getWidth()-BORDER_SIZE, 0, BORDER_SIZE, getHeight());
			g1.fillRect(0, getHeight()-BORDER_SIZE, getWidth(), BORDER_SIZE);
			g1.dispose();
			super.paintBorder(g);
		}

		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
		}
	}
	/*****************************************************/

	/*****************************************************
	 *		
	 *		flowButton
	 *		일반 JButton과 동일하게 사용 가능
	 *		
	 *		사용법 :
	 *		flowButton btnTest = new flowButton("버튼 이름");
	 *		
	 *****************************************************/
	public static class flowButton extends JButton implements MouseListener, MouseMotionListener{
		private int btnBorderSize = 1;
		private int btnState = 0;
		private boolean btnGradient = true; 

		public flowButton(String text){
			super(text);
			super.setForeground(Color.BLACK);
			super.setSize(100, 75);
			setContentAreaFilled(false);
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		public void setBorderSize(int tmpSize) {
			this.btnBorderSize = tmpSize;
			super.repaint();
		}

		public void setGraidentEffect(boolean val) {
			btnGradient = val;
			super.repaint();
		}

		@Override
		protected void paintBorder(Graphics g) {
			Graphics2D g1 = (Graphics2D)g.create();
			Border sysBorder;

			sysBorder = BorderFactory.createLineBorder(new Color(41, 82, 161));
			sysBorder = BorderFactory.createEmptyBorder(10 , 10 , 10 , 10); //상하좌우 10씩 띄우기
			super.setBorder(sysBorder);
			sysBorder = null;

			// Border Color
			g1.setPaint(
					new GradientPaint(
							new Point(0, getHeight()),
							new Color(126,153,0), //색상 지정
							new Point(getWidth(), getHeight()),
							new Color(0,110,117) //색상 지정
							)
					);
			g1.fillRect(0, 0, getWidth(), btnBorderSize);
			g1.fillRect(0, 0, btnBorderSize, getHeight());
			g1.fillRect(getWidth()-btnBorderSize, 0, btnBorderSize, getHeight());
			g1.fillRect(0, getHeight()-btnBorderSize, getWidth(), btnBorderSize);
			g1.dispose();
			super.paintBorder(g);
		}

		@Override
		protected void paintComponent(Graphics g){
			Graphics2D g1 = (Graphics2D)g.create();

			if(btnGradient == true) {
				switch(btnState) {
				case 0:
					this.setForeground(Color.BLACK);
					g1.setPaint(Color.WHITE);
					break;
				case 1:
					// Background Color
					g1.setPaint(
							new GradientPaint(
									new Point(0, getHeight()),
									new Color(126,153,0), //색상 지정
									new Point(getWidth(), getHeight()),
									new Color(0,110,117) //색상 지정
									)
							);
					g1.fillRect(0, 0, getWidth(), getHeight());
					this.setForeground(Color.WHITE);
					break;
				case 2:
					// Background Color
					g1.setPaint(
							new GradientPaint(
									new Point(0, getHeight()),
									new Color(86,113,0), //색상 지정
									new Point(getWidth(), getHeight()),
									new Color(0,70,77) //색상 지정
									)
							);
					g1.fillRect(0, 0, getWidth(), getHeight());
					this.setForeground(Color.WHITE);
					break;
				}
			}else {
				this.setForeground(Color.BLACK);
				g1.setPaint(Color.WHITE);
			}
			g1.dispose();
			super.paintComponent(g);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			if(btnBorderSize!=0) btnState = 0;
			this.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			this.repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// 마우스가 올라갈 때
			if(btnBorderSize!=0) btnState = 1;
			this.repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// 마우스가 빠져나올 때
			if(btnBorderSize!=0) btnState = 0;
			this.repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// 마우스가 눌릴 때
			if(btnBorderSize!=0) btnState = 2;
			this.repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// 마우스가 올라올 때
			if(btnBorderSize!=0) btnState = 1;
			this.repaint();
		}
	}
	/*****************************************************/


	/*****************************************************
	 *		
	 *		flowBorder
	 *		컨트롤에 GradientBorder를 넣을 경우 사용 가능
	 *		
	 *		사용법 :
	 *		setBorder (new flowBorder(10, 10, 10, 10));
	 *		
	 *****************************************************/
	public static class flowBorder implements Border{
		private Insets margin; 

		public flowBorder (int top, int left, int bottom, int right){ 
			super(); 
			margin = new Insets (top, left, bottom, right);
		} 

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height){ 
			Graphics2D g2d = (Graphics2D) g; 
			//g2d.setPaint (new GradientPaint (x, y, Color.RED, x + width, y, Color.BLUE)); 

			Area border = new Area (new Rectangle (x, y, width, height)); 
			border.subtract (new Area (new Rectangle (x + margin.left, y + margin.top, 
					width - margin.left - margin.right, height - margin.top - margin.bottom))); 
			g2d.fill (border); 
		} 

		@Override
		public Insets getBorderInsets (Component c){ 
			return margin; 
		} 

		public boolean isBorderOpaque(){ 
			return true; 
		}
	}
}


//	Paint gPaint1, gPaint2, gPaint3, gPaint4;   // Paint 속성을 위한 변수들
//
//	public modSysTheme(){              // 생성자
//		
//		// GradientPaint 객체를 생성한다.
//		gPaint1=new GradientPaint(10, 50, Color.white, 20, 60, Color.blue, true);
//		gPaint2=new GradientPaint(100, 50, Color.white, 100, 30, Color.blue, true);
//		gPaint3=new GradientPaint(190, 50, Color.white, 270, 250, Color.blue, false);
//		gPaint4=new GradientPaint(280, 50, new Color(126,153,0), 280, 250, new Color(0,110,117), false);
//	}
//
//	public Paint getColorFill(int tmpSelect) {
//		Paint tmpColor;
//		switch(tmpSelect) {
//		case 0:
//		default:
//			tmpColor=new GradientPaint(280, 50, new Color(126,153,0), 280, 250, new Color(0,110,117), false);
//			return tmpColor;
//		}
//	}
//	
//	public void paint(Graphics g){
//		Graphics2D g2=(Graphics2D)g;
//
//		// GradientPaint를 적용하여 사각형을 그린다.
//		g2.setPaint(gPaint1);
//		g2.fill(new Rectangle2D.Double(10,50,80,200));
//		g2.setPaint(gPaint2);
//		g2.fill(new Rectangle2D.Double(100,50,80,200));
//		g2.setPaint(gPaint3);
//		g2.fill(new Rectangle2D.Double(190,50,80,200));
//		g2.setPaint(gPaint4);
//		g2.fill(new Rectangle2D.Double(280,50,80,200));
//	}
//	
//	public static void main(String[] args){
//		Frame f=new modSysTheme("그라데이션 효과");
//		f.setSize(400,300);
//		f.setVisible(true);
//	}