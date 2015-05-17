package org.kuhi.visualscan;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class ScanPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> names;
	private ArrayList<Integer> shapes;
	
	// Monster name text color
	private Color nameColor = new Color(0xd3d3d3);
	// Shape bar color
	private Color shapeColor = new Color(0xff3300);
	// Target identifier color
	private Color targetColor = new Color(0x07de00);
	
	private String target = new String();
	
	public ScanPanel(ArrayList<String> n, ArrayList<Integer> s) {
		names = n;
		shapes = s;
	}
	
	public void setTarget(String t) {
		target = t;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int y = 10;
		int index = 0;
		g.setColor(new Color(0xd3d3d3));
		boolean tg = true;
		for( String name : names ) {
		  if( tg && name.equalsIgnoreCase(target)) {
			  g.setColor(targetColor);
			  g.fillRoundRect(1, y-6, 204, 24, 8, 4);
			  tg = false;
		  }
		  g.setColor(shapeColor);
		  g.fillRoundRect(3, y-4, shapes.get(index)*2, 20, 8, 4);
		  g.drawRoundRect(3, y-4, 200, 20, 8, 4);
		  g.setColor(nameColor);
		  g.drawString(name, 10, y+10);
		  y+=25;
		  index++;
		}
	}

}
