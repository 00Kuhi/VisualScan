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
	private static final Integer GS_PERCENT = new Integer(85);
	private static final Integer SH_PERCENT = new Integer(70);
	private static final Integer NH_PERCENT = new Integer(50);
	private static final Integer NIGS_PERCENT = new Integer(35);
	private static final Integer BS_PERCENT = new Integer(20);
	private static final Integer VBS_PERCENT = new Integer(10);
	private static final Integer ND_PERCENT = new Integer(5); 
	
	private static final String ES = "ES";
	private static final String GS = "GS";
	private static final String SH = "SH";
	private static final String NH = "NH";
	private static final String NIGS = "NIGS";
	private static final String BS = "BS";
	private static final String VBS = "VBS";
	private static final String ND = "ND"; 

	// Scanned monster names and shapes
	private ArrayList<ScanShape> shapes = new ArrayList<>();
	
	private ScanPanel scan_panel;
	private SettingsPanel settings_panel;
	
	public VisualScanPlugin() {
		scan_panel = new ScanPanel(shapes);
	}
	
	public void loadPlugin() {
		ClientGUI cgui = getClientGUI();
		
		//Create window
		BatWindow window = cgui.createBatWindow("Scan:", 300, 600, 300, 140);
		window.removeTabAt(0);
		window.setAlphaValue(0);
		window.setVisible(true);
		
		//Create scan panel
		scan_panel = new ScanPanel(shapes);
		scan_panel.setVisible(true);		
		scan_panel.setOpaque(false);
		window.newTab("Scan", scan_panel);
		
		//Set 'target' on clicking a scanned monster
		scan_panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int click_target = (int)((e.getY()-4)/25);
				if( shapes.size() > click_target ) {
					getClientGUI().doCommand("target "+shapes.get(click_target).getName());
				}
			}		
		});
		
		// create settings panel
		settings_panel = new SettingsPanel();
		settings_panel.init();
		settings_panel.setVisible(true);
		window.newTab("Settings", settings_panel);
		
		cgui.printText("general", "Initialized Scan Panel");
		
	}
	
	@Override
	public ParsedResult trigger(ParsedResult line) {
		String original = line.getStrippedText();
		if( original == null ) return line;
		
		// Clear scan info on start of the round
		Matcher m = round_pattern.matcher(original);
		if( m.matches() ) {
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
			return addScanResult(m.group(1),ES_PERCENT,ES,line);
		}
				
		m = gs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),GS_PERCENT,GS,line);
		}

		m = sh_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),SH_PERCENT,SH,line);
		}

		m = nh_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),NH_PERCENT,NH,line);
		}
		
		m = nigs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),NIGS_PERCENT,NIGS,line);
		}

		m = bs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),BS_PERCENT,BS,line);
		}

		m = vbs_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),VBS_PERCENT,VBS,line);
		}
		
		m = nd_pattern.matcher(original);
		if( m.matches() ) {
			return addScanResult(m.group(1),ND_PERCENT,ND,line);
		}

		return null;
	}
	
	private ParsedResult addScanResult(String name, Integer shape, String shapeName, ParsedResult result) {
		ScanShape scanShape = new ScanShape(name,shape,shapeName);
		shapes.add(scanShape);
		scan_panel.repaint();
		// Gag scan text from 'general' if hide rounds scan is set
		return (settings_panel.hideRoundsScans())?new ParsedResult(""):result;
	}
	
	@Override
	public String getName() {
		return "Visual Scan";
	}
	

}
