package org.kuhi.visualscan;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

public class VisualScanPlugin extends BatClientPlugin implements BatClientPluginTrigger {
	
	// REGEX patterns
	private static Pattern round_pattern = Pattern.compile("^\\*{5,} Round .*\n");
	private static Pattern es_pattern = Pattern.compile("(.*) is in excellent shape \\(.*\n");
	private static Pattern gs_pattern = Pattern.compile("(.*) is in a good shape \\(.*\n");
	private static Pattern sh_pattern = Pattern.compile("(.*) is slightly hurt \\(.*\n");
	private static Pattern nh_pattern = Pattern.compile("(.*) is noticeably hurt \\(.*\n");
	private static Pattern nigs_pattern = Pattern.compile("(.*) is not in a good shape \\(.*\n");
	private static Pattern bs_pattern = Pattern.compile("(.*) is in bad shape \\(.*\n");
	private static Pattern vbs_pattern = Pattern.compile("(.*) is in very bad shape \\(.*\n");
	private static Pattern nd_pattern = Pattern.compile("(.*) is near death \\(.*\n");
	private static Pattern target_pattern = Pattern.compile("^You are now targetting (.*).\n");

	// SHAPE percentages
	private static final Integer ES_PERCENT = new Integer(100);
	private static final Integer GS_PERCENT = new Integer(90);
	private static final Integer SH_PERCENT = new Integer(80);
	private static final Integer NH_PERCENT = new Integer(50);
	private static final Integer NIGS_PERCENT = new Integer(35);
	private static final Integer BS_PERCENT = new Integer(20);
	private static final Integer VBS_PERCENT = new Integer(10);
	private static final Integer ND_PERCENT = new Integer(5); 
	
	// Scanned monster names and shapes
	private ArrayList<String> names = new ArrayList<>();
	private ArrayList<Integer> shapes = new ArrayList<>();
	
	private ScanPanel scan_panel;
	
	// Gag rounds scan lines
	private boolean GAG = true;
	
	public VisualScanPlugin() {
		scan_panel = new ScanPanel(names,shapes);
	}
	
	public void loadPlugin() {
		ClientGUI cgui = getClientGUI();
		
		//Create window
		BatWindow window = cgui.createBatWindow("Scan:", 300, 600, 300, 140);
		window.removeTabAt(0);
		window.setAlphaValue(0);
		window.setVisible(true);
		
		//Create scan panel
		scan_panel = new ScanPanel(names, shapes);
		scan_panel.setVisible(true);		
		scan_panel.setOpaque(false);
		window.newTab("Scan", scan_panel);
		
		//Set 'target' on clicking a scanned monster
		scan_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int click_target = (int)((e.getY()-10)/25);
				if( names.size() > click_target ) {
					getClientGUI().doCommand("target "+names.get(click_target));
				}
			}		
		});
		
		cgui.printText("general", "Initialized Scan Panel");
		
	}
	
	@Override
	public ParsedResult trigger(ParsedResult line) {
		String original = line.getStrippedText();
		if( original == null ) return line;
		
		// Clear scan info on start of the round
		Matcher m = round_pattern.matcher(original);
		if( m.matches() ) {
			names.clear();
			shapes.clear();
		}
		
		// Set 'target' when clicked on
		m = target_pattern.matcher(original);
		if( m.matches() ) {
			scan_panel.setTarget(m.group(1));
			scan_panel.repaint();
		}
		
		m = es_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),ES_PERCENT,line);
		}
				
		m = gs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),GS_PERCENT,line);
		}

		m = sh_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),SH_PERCENT,line);
		}

		m = nh_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),NH_PERCENT,line);
		}
		
		m = nigs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),NIGS_PERCENT,line);
		}

		m = bs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),BS_PERCENT,line);
		}

		m = vbs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),VBS_PERCENT,line);
		}
		
		m = nd_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),ND_PERCENT,line);
		}

		return null;
	}
	
	private ParsedResult addScanResult(String name, Integer shape, ParsedResult result) {
		names.add(name);
		shapes.add(shape);
		scan_panel.repaint();
		// Gag scan text from 'general' if set
		return (GAG)?new ParsedResult(""):result;
	}
	
	@Override
	public String getName() {
		return "Visual Scan";
	}
	

}
