package module;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;

public class modSysTheme{

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