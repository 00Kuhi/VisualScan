package org.kuhi.visualscan;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ScanPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<ScanShape> shapes;
	
	// Monster name text color
	private Color nameColor = new Color(0xd3d3d3);
	// Shape bar color
	private Color shapeColor = new Color(0xd4354f);
	// Target identifier color
	private Color targetColor = new Color(0xbaa9ac);
	
	private Font nameFont = null;
	
	private String target = new String();
	
	public ScanPanel(ArrayList<ScanShape> s) {
		shapes = s;
	}
	
	public void setTarget(String t) {
		target = t;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if( nameFont == null ) nameFont = g.getFont().deriveFont(Font.BOLD);
		
		int y = 4;
		boolean tg = true;
		for( ScanShape shape : shapes ) {
		  if( tg && shape.getName().equalsIgnoreCase(target)) {
			  g.setColor(shapeColor);
			  tg = false;
		  } else {
			  g.setColor(targetColor);
		  }
		  g.drawRoundRect(23, y-4, 200, 20, 4, 4);
		  g.setColor(shapeColor);
		  g.fillRoundRect(25, y-2, (shape.getShape()*2-4), 16, 4, 4);
		  g.setColor(nameColor);
		  g.setFont(nameFont);
		  g.drawString(shape.getName(), 35, y+10);
		  g.drawString(shape.getShapeName(), 240, y+10);
		  y+=25;
		}
	}

}
