package org.kuhi.visualscan;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class SettingsPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JCheckBox gag_box;
	private boolean GAG = true;

	public void init() {
		BoxLayout layout = new BoxLayout(this,BoxLayout.Y_AXIS);
		setLayout(layout);
		setBackground(Color.BLACK);
		gag_box = new JCheckBox("Hide rounds_scan");
		gag_box.setForeground(Color.WHITE);
		gag_box.setBackground(Color.BLACK);
		gag_box.setSelected(true);
		gag_box.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				GAG = gag_box.isSelected();
			}
		});
		add( gag_box );
		
		JSeparator s = new JSeparator();
		s.setForeground(Color.WHITE);
		add( s );
		
		JLabel help = new JLabel();
		help.setForeground(Color.WHITE);
		help.setBackground(Color.BLACK);
		help.setText("<html>Plugin needs following settings to work:<br>'battle rounds 1'<br>'battle round_scan on'</html>");
		add( help );
		
	}
	
	protected boolean hideRoundsScans() {
		return GAG;
	}
	

}
