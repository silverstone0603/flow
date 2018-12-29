package module;

import main.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

public class modChartPanel extends JPanel{
	static modFileList clsFileList;
	static fraMain pnlMain;

	Color colorBackground = new Color(255,255,255);
	Color[] color = {new Color(126,153,00), new Color(00,110,75)};
	public int[] arcAngle = new int[2];
	public int[] cntData = new int[2]; //0: de, 1: en;

	public modChartPanel(modFileList clsFileList, fraMain pnlMain) {
		this.clsFileList = clsFileList;
		this.pnlMain = pnlMain;

		setFont(new Font("Malgun Gothic",1,15));
		setVisible(true);
		setBounds(50,60,130,130);
		setBackground(colorBackground);
	}

	public void setData(int data1, int data2) {
		cntData[0] = data1;
		cntData[1] = data2;
	}

	public void paintComponent(Graphics g){
		Graphics2D g1 = (Graphics2D)g.create();
		
		super.paintComponent(g);//부모 Paint호출
		
		g1.setPaint(
        		new GradientPaint(
        				new Point(0, getHeight()),
        				new Color(126,153,0), //색상 지정
        				new Point(getWidth(), getHeight()),
        				new Color(0,110,117) //색상 지정
        		)
        );
        
		int startAngle = 0;
		// g.drawString("총 파일 : 100% 00개", 170,25 );
		for(int i=0;i<cntData.length;i++){
			// System.out.println("for문 1 _ "+i);
			g.setColor(color[i]);
		}
		
		for(int i=0;i<cntData.length;i++){
			// System.out.println("for문 2 _ [StartAngle"+startAngle+"] _ "+i);
			g.setColor(color[i]);
			g.fillArc(0, 0, 125, 125, startAngle, arcAngle[i]);
			
			startAngle = startAngle + arcAngle[i];
		}
		g.dispose();
		g1.dispose();
	}

	public void drawChart(){ // 차트를 그림
		int sum=0; // 초기값 0

		if(cntData[0]<=0 && cntData[1]<=0) {
			System.out.println("[Flow _ Chart Panel Module] setData 메소드로 값을 지정한 후 사용 가능합니다.");
			return;
		}

		for(int i=0;i<cntData.length;i++){ // 데이터 값만큼 루프
			sum+=cntData[i];
		}
		if(sum == 0) 
			return;

		for(int i=0;i<cntData.length;i++){ 
			arcAngle[i] = (int)Math.round((double)cntData[i]/(double)sum*360);
			this.repaint(); // 차트 패널의 Paint 호출
		}
	} 
}