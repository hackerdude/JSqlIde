/*
 * sqlIDE - a Pure Java Database Development environment
 *  Copyright (C) 1999 by David Martinez <david@hackerdude.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Please read hacking.html for overview information if you want
 * to modify.
 *
 * @author David Martinez <david@hackerdude.com>
 * @version $Id$
*/
package com.hackerdude.devtools.db.sqlide;
import com.hackerdude.lib.*;
import com.hackerdude.devtools.db.sqlide.dbspecific.*;
import com.hackerdude.devtools.db.sqlide.dialogs.*;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import com.hackerdude.devtools.db.sqlide.plugins.*;
import com.hackerdude.devtools.db.sqlide.plugins.isql.PluginInteractiveSQL;
import com.hackerdude.devtools.db.sqlide.plugins.definitions.*;
import com.hackerdude.devtools.db.sqlide.plugins.browser.*;
import com.hackerdude.devtools.db.sqlide.plugins.browser.browsejdbc.BasicJDBCIntrospector;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.Cursor;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.event.*;
import java.util.*;
import java.lang.Object.*;
import java.io.*;

/**
 * Main class for the sql ide program.
 *
 *
 * <P>The program starts by doing the following:
 *
 *  <ul>
 *    <li>Creates a Frame for the application
 *    <li>Splits the screen
 *    <li>Creates a global ProgramConfig configuration object.
 *    <li>Places an IDEBrowser on the left side of the screen.
 *    <li>Places an Interactive SQL Window on the right hand side.
 *    <li>Creates a menu and a toolbar.
 *  </ul>
 * @version $Id$
 */
public class sqlide  {

	private final static JFrame frame = new JFrame("sqlide Main Window");

	JMenuBar  menubar;
	private static sqlide instance;

	private final ImageIcon sqlideIcon = ProgramIcons.getInstance().getAppIcon();

	private IDEVisualPluginIF rightIdePanel;

	public final Action FILE_OPEN = new ActionSQLIDE("Open", ProgramIcons.getInstance().findIcon("images/Open.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, false), KeyEvent.VK_O);
	public final Action FILE_SAVE = new ActionSQLIDE("Save", ProgramIcons.getInstance().findIcon("images/Save.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, false), KeyEvent.VK_S);
	public final Action FILE_EXIT = new ActionProgramExit();
	public final Action EDIT_CUT  = new ActionSQLIDE("Cut", ProgramIcons.getInstance().findIcon("images/Cut.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, false), KeyEvent.VK_T);
	public final Action EDIT_COPY = new ActionSQLIDE("Copy", ProgramIcons.getInstance().findIcon("images/Copy.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, false), KeyEvent.VK_C);
	public final Action EDIT_PASTE = new ActionSQLIDE("Paste", ProgramIcons.getInstance().findIcon("images/Paste.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK, false), KeyEvent.VK_P);
	public final Action TOOLS_CONFIGURE = new ActionToolsConfigure();
	public final Action VIEW_BROWSER = new ActionSQLIDE("Browser", ProgramIcons.getInstance().getServerIcon(), KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK, false), KeyEvent.VK_B);
	public final Action PLUGIN_CLOSE = new ActionPluginClose();
	public final Action SELECT_LEFT = new ActionSelectLeftPanel();
	public final Action SELECT_RIGHT = new ActionSelectRightPanel();

	public final Action HELP_CHANGES   = new ActionHelpChanges();
	public final Action HELP_ABOUT     = new ActionHelpAbout();
	public final Action HELP_README    = new ActionHelpReadme();
	public final Action HELP_PAYING    = new ActionHelpPaying();
	public final Action HELP_TO_DO     = new ActionHelpTodo();
	public final Action HELP_KNOWNBUGS = new ActionHelpBugs();
	public final Action PLUGIN_CONTROL = new ActionPluginControl();

	public final Action HELP_LICENSE = new ActionHelpLicense();

	JMenu mnuPluginControl = new JMenu(PLUGIN_CONTROL);
	AboutDialog about = null;

	private ProgressFrame mainProgress;
	private int mainProgressValue;
	private JToolBar tbToolbar = new JToolBar();
	private JButton btnOpen = new JButton(FILE_OPEN);
	private JButton btnSave = new JButton(FILE_SAVE);
	JPanel mainPanel = new JPanel();
	BorderLayout mainBorderLayout = new BorderLayout();
	JTabbedPane jTabbedPane1 = new JTabbedPane();
	PluginIDEBrowser idebrowser;
	JSplitPane jSplitPane1 = new JSplitPane();
	JPopupMenu jPopupMenu1 = new JPopupMenu();

	private RunningPlugins runningPlugins = new RunningPlugins();


	/**
	 * JBuilder likes to see a "jbInit" method and
	 * runs this method to show the program in
	 * design-time. Since I use JBuilder ocasionally,
	 * I have this code to make it happy.
	 */
	public void jbInit() {

		idebrowser = new PluginIDEBrowser();
		idebrowser.setSQLIDE(this);

		mainPanel.setLayout(mainBorderLayout);
		jTabbedPane1.setTabPlacement(JTabbedPane.BOTTOM);
		jTabbedPane1.setToolTipText("");
		jTabbedPane1.addChangeListener(new TabChangeListener());
		jTabbedPane1.addMouseListener(new PluginPagesPopupAdapter());
		jPopupMenu1.setInvoker(jTabbedPane1);
		frame.getContentPane().add(menubar, BorderLayout.NORTH);
		frame.getContentPane().add(tbToolbar, BorderLayout.SOUTH);
		tbToolbar.add(btnOpen, null);
		tbToolbar.add(btnSave, null);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(jSplitPane1, BorderLayout.CENTER);
		jSplitPane1.add(jTabbedPane1, "right");
		jSplitPane1.add(idebrowser, "left");
		JMenuItem mnuClosePlugin = jPopupMenu1.add( PLUGIN_CLOSE );
		KeyStroke theAccel = (KeyStroke)PLUGIN_CLOSE.getValue(Action.ACCELERATOR_KEY);
		if ( theAccel != null ) mnuClosePlugin.setAccelerator(theAccel);


	}

	/**
	 * Contructor
	 */
	public sqlide() {

		mainProgress = ProgressFrame.createProgressFrame("SQLIDE Loading...", 0, 6);
		mainProgressValue = 0;
		mainProgress.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Read the configuration
		mainProgress.changeMessage("Reading Configuration...");

		mainProgress.setIconImage(sqlideIcon.getImage());
		mainProgress.setValue(++mainProgressValue);

		mainProgress.changeMessage("Creating Menus...");
		createMenus();

		mainProgress.changeMessage("Initializing UI...");
		jbInit();

		// Add the IDE browser.
		mainProgress.changeMessage("Creating Browser...");
		idebrowser.setVisible(true);
		idebrowser.initPlugin();


		mainProgress.changeMessage("Bootstrapping plugins...");
		try {
			bootstrapPlugins();
		} catch ( IOException exc ) {
			exc.printStackTrace();
		}

		mainProgress.changeMessage("Creating Default Interactive SQL");
		DatabaseProcess ideserver = new DatabaseProcess(ProgramConfig.getInstance().getDefaultDatabaseSpec());
		mainProgress.setValue(++mainProgressValue);

//
//		if ( idebrowser.getDatabaseProcess().doConnect() ) {
//		IDEVisualPluginIF plugin = (IDEVisualPluginIF)PluginFactory.createPlugin("Interactive SQL");
//		runningPlugins.startPlugin(plugin);
//		plugin.setDatabaseProcess(idebrowser.getDatabaseProcess());
//		setRightPanel(plugin);
//		}

		mainProgress.setCursor(Cursor.getDefaultCursor());

	}

	/**
	 * JMenuItem has a bug where it does not set its Accelerator key. This
	 * workaround makes sure it gets set using the action's accel key.
	 */
	private void _addMenu(JMenu menu, Action theAction) {
		JMenuItem newItem = menu.add( theAction );
		KeyStroke theAccel = (KeyStroke)theAction.getValue(Action.ACCELERATOR_KEY);
		if ( theAccel != null ) newItem.setAccelerator(theAccel);
	}

	public void createMenus() {
		JMenu fileMenu;
		JMenu editMenu;
		JMenu toolsMenu;
		JMenu pluginMenu;
		JMenu helpMenu;
		menubar = new JMenuBar();
		fileMenu = new JMenu("File");
		editMenu = new JMenu("Edit");;
		toolsMenu = new JMenu("Tools");;
		pluginMenu = new JMenu("Plug-In");;
		helpMenu = new JMenu("Help");;
		fileMenu.setMnemonic('F');
		editMenu.setMnemonic('E');
		toolsMenu.setMnemonic('T');
		pluginMenu.setMnemonic('P');
		helpMenu.setMnemonic('H');

		_addMenu(fileMenu, FILE_OPEN );
		_addMenu(fileMenu, FILE_SAVE );
		fileMenu.addSeparator();
		_addMenu(fileMenu, FILE_EXIT );
//		_addMenu(fileMenu, FILE_OPEN );
		_addMenu(editMenu, EDIT_CUT );
		_addMenu(editMenu, EDIT_COPY );
		_addMenu(editMenu, EDIT_PASTE );
		_addMenu(toolsMenu, TOOLS_CONFIGURE);

		_addMenu(pluginMenu, PLUGIN_CLOSE);
		_addMenu(pluginMenu,SELECT_LEFT);
		_addMenu(pluginMenu,SELECT_RIGHT);
		pluginMenu.addSeparator();
		pluginMenu.add(mnuPluginControl);
		pluginMenu.addSeparator();

		// Add all the visual plugins to the plugin menu.
		IDEVisualPluginIF[] availableVisualPlugins = PluginRegistry.getInstance().getAllVisualPlugins();
		for ( int i=0; i<availableVisualPlugins.length; i++) {
			ActionCreatePlugin cp = new ActionCreatePlugin(availableVisualPlugins[i].getPluginName(), availableVisualPlugins[i].getPluginIcon());
			pluginMenu.add(cp);
		}

		_addMenu(helpMenu,HELP_ABOUT);
		_addMenu(helpMenu,HELP_KNOWNBUGS);
		_addMenu(helpMenu,HELP_PAYING);
		_addMenu(helpMenu,HELP_README);
		_addMenu(helpMenu,HELP_TO_DO);
		_addMenu(helpMenu,HELP_CHANGES);

		menubar.add( fileMenu   );
		menubar.add( editMenu   );
		menubar.add( toolsMenu  );
		menubar.add( pluginMenu );
		menubar.add(Box.createHorizontalGlue());
		menubar.add( helpMenu );

	}


  /**
   * This initializes the user interface according to the
   * whatever the configuration class says (look and feel)
   */
	public static void initializeUI() {
		// Set the Look-and-Feel
		try {
			String lookAndFeelClass = ProgramConfig.getInstance().getUILookandFeelClass();
			UIManager.setLookAndFeel(lookAndFeelClass);
			if ( frame != null ) {
				SwingUtilities.updateComponentTreeUI(frame);
				frame.pack();
			}

		} catch (Exception exc) {
			System.err.println("Error: Could not load LookAndFeel: " + ProgramConfig.getInstance().getUILookandFeelClass());
		}
	}


	/**
	 * Main Function.
	 * Run the sqlide class to start the application.
	 */
	public static void main(String s[]) {

		initializeUI();
		instance = new sqlide();
		// Create the sqlide and put it on a frame.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { System.exit(0); }
		});
		frame.setIconImage(instance.sqlideIcon.getImage());

		instance.mainProgress.setValue(++instance.mainProgressValue);

		frame.pack();

		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		instance.mainProgress.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		instance.mainProgress.changeMessage("Showing IDE...");
		instance.mainProgress.setValue(++instance.mainProgressValue);
		instance.mainProgress.setVisible(false);
		frame.setCursor(Cursor.getDefaultCursor());
		instance.mainProgress.setCursor(Cursor.getDefaultCursor());

		// In 20% from the corners.
		int inset = 20;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Double insetD = new Double(screenSize.width * (inset*0.01));
		int insetx = insetD.intValue();
		insetD = new Double(screenSize.height * (inset*0.01));
		int insety = insetD.intValue();

		frame.setBounds( insetx, insety, screenSize.width - insetx, screenSize.height - insety);
		frame.setVisible(true);

	}

	public static sqlide getInstance() { return instance; }

	private class TabChangeListener implements ChangeListener{

		public void stateChanged( ChangeEvent e ) {
			/** @todo Ask panels to hide their menus when this event fires. */
			  if ( rightIdePanel != null ) {
				//rightIdsePanel.setVisibleMenus(false, mnuPluginControl );
				rightIdePanel = (IDEVisualPluginIF)jTabbedPane1.getSelectedComponent();
//				if ( rightIdePanel != null ) rightIdePanel.setVisibleMenus(true, mnuPluginControl);
			  }
		};

	}

	public void refreshPanels() {
	/** @todo Figure out if it's still needed, then reimplement. */
/*		if ( rightIdePanel != null ) rightIdePanel.refreshPanel();
		runningPlugins.refreshPlugins();
		idebrowser.refreshPanel();
*/
	}

	/**
	 * Returns true if the current right panel is an Interactive SQL
	 */
	public boolean isRightanISQL() {
		if ( rightIdePanel == null ) return false;
		return rightIdePanel instanceof PluginInteractiveSQL;
	}

	/** @todo Todo: Use this if the plugin is also an instance of contextprovider plugins */
	public Action[] getActionsFor(NodeIDEBase[] nodes) {
		return runningPlugins.getActionsFor(nodes);
	}

	private void createPanelMenu(IDEVisualPluginIF plugin) {
		mnuPluginControl.removeAll();
		Action[] actions = plugin.getPossibleActions();
		for ( int i=0; i<actions.length; i++) { _addMenu(mnuPluginControl,actions[i]); }
		// Now re-compose the popup menu with all the actions.
		jPopupMenu1.removeAll();
		JMenuItem mnuClosePlugin = jPopupMenu1.add( PLUGIN_CLOSE );
		KeyStroke theAccel = (KeyStroke)PLUGIN_CLOSE.getValue(Action.ACCELERATOR_KEY);
		if ( theAccel != null ) mnuClosePlugin.setAccelerator(theAccel);
		jPopupMenu1.addSeparator();
		for ( int i=0; i<actions.length; i++) {
			JMenuItem newMenu = jPopupMenu1.add( actions[i] );
			KeyStroke newAccel = (KeyStroke)actions[i].getValue(Action.ACCELERATOR_KEY);
			if ( newAccel != null ) mnuClosePlugin.setAccelerator(theAccel);
		}

	}

	/**
	 * Installs the Right-hand panel.
	 */
	public void setRightPanel(IDEVisualPluginIF plugin) {
		rightIdePanel = plugin;
		createPanelMenu(plugin);
		jTabbedPane1.add((Component)plugin, plugin.getPluginShortName()+" ("+plugin.getDatabaseProcess().getSpec().getPoliteName()+")" );
	}

	public void pack() {
		if ( frame != null ) frame.pack();
	}

	public class PluginPagesPopupAdapter extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			browserTree_mouseClicked(e);
		}

		void browserTree_mouseClicked(MouseEvent e) {
			if ( e.getModifiers() == e.BUTTON3_MASK ) {
					Double theX = new Double(e.getPoint().getX());
					Double theY = new Double(e.getPoint().getY());
					jPopupMenu1.show(jTabbedPane1, theX.intValue(), theY.intValue());
			}
		}
	}


	/**
	 * This class returns all the possible actions of visible panels
	 */
	public ArrayList getAllActions() {
		ArrayList result = getActionSQLIDEs();
		Action[] actions = idebrowser.getPossibleActions();
		for ( int i=0; i<actions.length; i++) result.add(actions[i]);
		actions = rightIdePanel.getPossibleActions();
		for ( int i=0; i<actions.length; i++) result.add(actions[i]);

		return result;

	}

	public ArrayList getActionSQLIDEs() {
		ArrayList result = new ArrayList(800);
		result.add(FILE_OPEN);
		result.add(FILE_SAVE);
		result.add(FILE_EXIT);

		result.add(EDIT_CUT);
		result.add(EDIT_COPY);
		result.add(EDIT_PASTE);

		result.add(TOOLS_CONFIGURE);
		result.add(VIEW_BROWSER);
		result.add(PLUGIN_CLOSE);

		result.add(HELP_ABOUT);
		result.add(HELP_KNOWNBUGS);
		result.add(HELP_PAYING);
		result.add(HELP_TO_DO);
		result.add(HELP_README);

		return result;
	}

	public class ActionHelpAbout extends AbstractAction {
		public ActionHelpAbout() {
			super("About SQLIDE...", ProgramIcons.getInstance().findIcon("images/About.gif"));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.CTRL_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
		}
		public void actionPerformed(ActionEvent e) {
/*			GPLAboutDialog gpl = new GPLAboutDialog(frame, "sqlide",
											ProgramConfig.getVersionNumber(),
											"A SQL Integrated Development Environment written in Java.",
											"(C) 1999-2001 by David Martinez.");
			//gpl.getContentPane().
			gpl.actionPerformed(null);*/
			if ( about == null ) {
				about = new AboutDialog(frame);
				about.pack();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				double x = ( screenSize.getWidth() - about.getWidth() ) / 2;
				double y = ( screenSize.getHeight() - about.getHeight() ) / 2;
				about.setBounds(new Double(x).intValue(), new Double(y).intValue(), about.getWidth(), about.getHeight());
			}
			about.show();
		}
	}

	public class ActionPluginControl extends AbstractAction {

		public ActionPluginControl() {
		super("Control Plugin", ProgramIcons.getInstance().findIcon("images/server.gif"));
		}
		public void actionPerformed(ActionEvent e) {
			Component comp = jTabbedPane1.getSelectedComponent();
			IDEPluginIF plugin = (IDEPluginIF)comp;
			jTabbedPane1.remove(comp);
			runningPlugins.endPlugin(plugin);
		}

	}

	public class ActionPluginClose extends AbstractAction {

		public ActionPluginClose() {
			super("Close Plugin", ProgramIcons.getInstance().findIcon("images/Folder.gif"));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
		}
		public void actionPerformed(ActionEvent e) {
			Component comp = jTabbedPane1.getSelectedComponent();
			IDEPluginIF plugin = (IDEPluginIF)comp;
			jTabbedPane1.remove(comp);
			runningPlugins.endPlugin(plugin);
		}

	}
	public class ActionToolsConfigure extends AbstractAction {
		public ActionToolsConfigure() {
			super("Configure...",ProgramIcons.getInstance().findIcon("images/List.gif") );
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F7, Event.CTRL_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
		}
		public void actionPerformed(ActionEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				String currentLF = ProgramConfig.getInstance().getUILookandFeel();
				if ( DlgIDEConfigure.showConfigurationDialog(frame) ) {
					refreshPanels();
					String newLF = ProgramConfig.getInstance().getUILookandFeel();
					if (  ! currentLF.equals(newLF) ) {
						try {
							LookAndFeel newLookAndFeel = sqlide.changeLookAndFeel(newLF);
							SwingUtilities.updateComponentTreeUI(sqlide.getFrame());
						} catch ( UnsupportedLookAndFeelException exc ) {
							exc.printStackTrace();
						}
					}
				}
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}

	}

	public class ActionProgramExit extends AbstractAction {
		public ActionProgramExit() {
			super("Exit SQLIDE", ProgramIcons.getInstance().findIcon("images/Exit.gif"));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
		}
		public void actionPerformed(ActionEvent e) {
			runningPlugins.terminatePlugins();
			System.exit(0);
		}
	}


	public class ActionHelpPaying extends AbstractAction {
		public ActionHelpPaying() {
			super("Paying for SQLIDE", ProgramIcons.getInstance().findIcon("images/Favorite.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_P));
		}
		public void actionPerformed(ActionEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				new ShowDocumentDialog().showPayDialog();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public class ActionHelpLicense extends AbstractAction {
		public ActionHelpLicense() {
			super("License", ProgramIcons.getInstance().findIcon("images/List.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_T));
		}
		public void actionPerformed(ActionEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				new ShowDocumentDialog().showLicenseDialog();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public class ActionHelpTodo extends AbstractAction {
		public ActionHelpTodo() {
			super("To-Do List", ProgramIcons.getInstance().findIcon("images/List.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_T));
		}
		public void actionPerformed(ActionEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				new ShowDocumentDialog().showTodoDialog();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}
	}
	public class ActionHelpReadme extends AbstractAction {
		public ActionHelpReadme() {
			super("Readme", ProgramIcons.getInstance().findIcon("images/Document.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));

		}
		public void actionPerformed(ActionEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				new ShowDocumentDialog().showReadmeDialog();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}
	}
	public class ActionHelpBugs extends AbstractAction {
		public ActionHelpBugs() {
			super("Known Bugs", ProgramIcons.getInstance().findIcon("images/Error.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
		}
		public void actionPerformed(ActionEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				new ShowDocumentDialog().showKnownBugsDialog();
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public class ActionHelpChanges extends AbstractAction {
		public ActionHelpChanges() {
			super("Changes", ProgramIcons.getInstance().findIcon("images/CheckAll.gif"));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_H));
		}
		public void actionPerformed(ActionEvent e) {
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) );
			try {
				new ShowDocumentDialog().showDialog("Changes", "com.hackerdude.devtools.db.sqlide.CHANGES.html", "../docs/CHANGES.html");

			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public class ActionCreatePlugin extends AbstractAction {
		public ActionCreatePlugin(String name, Icon img, KeyStroke keyStroke, int mnemonicKey) {
			super(name, img);
			putValue(MNEMONIC_KEY, new Integer(mnemonicKey));
			putValue(ACCELERATOR_KEY, keyStroke);

		}
		public ActionCreatePlugin(String name, Icon img) {
			super(name, img);
		}
		public void actionPerformed(ActionEvent e) {
			String politeName = e.getActionCommand();
			try {
				frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				IDEVisualPluginIF plugin = (IDEVisualPluginIF)PluginFactory.createPlugin(politeName);
				runningPlugins.startPlugin(plugin);
				plugin.setDatabaseProcess( idebrowser.getDatabaseProcess() );
				setRightPanel(plugin);
				JPanel pluginPanel = (JPanel)plugin;
				jTabbedPane1.setSelectedComponent(pluginPanel);
			 } finally {
				frame.setCursor(Cursor.getDefaultCursor());
			 }
		}
	}

	public class ActionSQLIDE extends AbstractAction {
		public ActionSQLIDE(String name, ImageIcon img, KeyStroke keyStroke, int mnemonicKey) {
			super(name, img);
			putValue(MNEMONIC_KEY, new Integer(mnemonicKey));
			putValue(ACCELERATOR_KEY, keyStroke);

		}

		public ActionSQLIDE(String name, ImageIcon img) {
			super(name, img);
		}

		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			System.out.println("[sqlide] Defering execution of action "+command+" to right panel");
			if ( rightIdePanel != null ) {
			   if ( rightIdePanel.executeStandardAction(e) ) return;
			}
		}

	}



	public class ActionSelectRightPanel extends AbstractAction {
		public ActionSelectRightPanel() {
			super("Select Right Panel", null);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Event.CTRL_MASK|Event.ALT_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
		}
		public void actionPerformed(ActionEvent e) {
			if ( rightIdePanel!= null ) {
				rightIdePanel.receivePluginFocus();
//				((JComponent)rightIdePanel).grabFocus();
			}
		}
	}

	public class ActionSelectLeftPanel extends AbstractAction {
		public ActionSelectLeftPanel() {
			super("Select Left Panel", null);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Event.CTRL_MASK|Event.ALT_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
		}
		public void actionPerformed(ActionEvent e) {
			if ( idebrowser!= null ) {
				idebrowser.receivePluginFocus();

//				((JComponent)idebrowser).grabFocus();
			}
		}
	}

	public void requestAddSubNodes(NodeIDEBase parentNode) {
		runningPlugins.requestAddSubNodes(parentNode);
	}


	private void bootstrapPlugins() throws IOException {

		InputStream is = sqlide.class.getResourceAsStream("autoexec.plugins.properties");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String newPlugin = reader.readLine();
		while ( newPlugin != null ) {
			if (! newPlugin.trim().startsWith("#") && ! newPlugin.trim().equals("")) {
				PluginDefinition def = PluginRegistry.getInstance().getPluginByClassName(newPlugin);
				if ( def == null ) {
					System.out.println("[SQLIDE] Plugin "+newPlugin+" not found.");
				} else {
					mainProgress.changeMessage("[SQLIDE] Starting "+def.pluginInstance.getPluginName()+"...");
					runningPlugins.startPlugin(PluginFactory.createPlugin(def));
					System.out.println("Plugin "+def.pluginInstance.getPluginName()+" started.");
				}
			}
			newPlugin = reader.readLine();
		}

	}

	public static JFrame getFrame() {
		return frame;
	}


	/**
	 * This method changes the look and feel on the UI manager and returns
	 * the new look and feel.
	 * @param lfName The name of the look and feel
	 * @return The new look and feel, or the old look and feel if the lfName is not found.
	 */
	public static LookAndFeel changeLookAndFeel(String lfName) throws UnsupportedLookAndFeelException {
		// Iterate down all the installed l&fs looking for the one with the lfName.
		UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
		for ( int i=0; i<info.length; i++ ) {
			if ( info[i].getName().equals(lfName) ) {
				try {
					String className = info[i].getClassName();
					UIManager.installLookAndFeel(info[i]);
					UIManager.setLookAndFeel(className);
					break;
				} catch ( Throwable err ) {
					JOptionPane.showMessageDialog(sqlide.getFrame(), "Could not change the look and feel due to the following error: "+err.getMessage(), "Could not change look and feel", JOptionPane.ERROR_MESSAGE);
					err.printStackTrace();
				}
			}
		}
		return UIManager.getLookAndFeel();
	}


}
