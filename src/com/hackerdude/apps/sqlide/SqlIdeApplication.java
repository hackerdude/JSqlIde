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
package com.hackerdude.apps.sqlide;
import java.beans.*;
import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.hackerdude.apps.sqlide.dialogs.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.plugins.browser.*;
import com.hackerdude.apps.sqlide.plugins.definitions.*;
import com.hackerdude.apps.sqlide.plugins.isql.*;
import com.hackerdude.apps.sqlide.wizards.*;
import com.hackerdude.apps.sqlide.xml.*;
import com.hackerdude.apps.sqlide.xml.hostconfig.*;
import com.hackerdude.swing.*;
import com.hackerdude.apps.sqlide.dataaccess.*;

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
public class SqlIdeApplication  {

	private final static JFrame frame = new JFrame("sqlide Main Window");

	JMenuBar  menuBar;
	private static SqlIdeApplication instance;

	private final ImageIcon sqlideIcon = ProgramIcons.getInstance().getAppIcon();

	private IDEVisualPluginIF rightIdePanel;

	public final static Action SEPARATOR_ACTION = new AbstractAction() {
		public void actionPerformed(ActionEvent evt) {}
	};

	public final InteractiveCredentialsProvider credentialsProvider = new InteractiveCredentialsProvider();

	public final Action FILE_OPEN = new ActionSQLIDE("Open", ProgramIcons.getInstance().findIcon("images/Open.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, false), KeyEvent.VK_O);
	public final Action FILE_SAVE = new ActionSQLIDE("Save", ProgramIcons.getInstance().findIcon("images/Save.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, false), KeyEvent.VK_S);
	public final Action FILE_EXIT = new ActionProgramExit();
	public final Action EDIT_CUT  = new ActionSQLIDE("Cut", ProgramIcons.getInstance().findIcon("images/Cut.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, false), KeyEvent.VK_T);
	public final Action EDIT_COPY = new ActionSQLIDE("Copy", ProgramIcons.getInstance().findIcon("images/Copy.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, false), KeyEvent.VK_C);
	public final Action EDIT_PASTE = new ActionSQLIDE("Paste", ProgramIcons.getInstance().findIcon("images/Paste.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK, false), KeyEvent.VK_P);
	public final Action TOOLS_CONFIGURE = new ActionToolsConfigure();
	public final Action TOOLS_PLUGINS   = new ActionToolsPlugins();
	public final Action VIEW_BROWSER = new ActionSQLIDE("Browser", ProgramIcons.getInstance().getServerIcon(), KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK, false), KeyEvent.VK_B);
	public final Action PLUGIN_CLOSE = new ActionPluginClose();
	public final Action SELECT_LEFT = new ActionSelectLeftPanel();
	public final Action SELECT_RIGHT = new ActionSelectRightPanel();

	public final Action HELP_ABOUT     = new ActionHelpAbout();
	public final Action HELP_README    = new ActionHelpShowDocument("Read Me First!", ActionHelpShowDocument.DOCUMENT_README, ProgramIcons.getInstance().findIcon("images/Document.gif") );
	public final Action HELP_PAYING    = new ActionHelpShowDocument("Paying for SQLIDE", ActionHelpShowDocument.DOCUMENT_PAYING, ProgramIcons.getInstance().findIcon("images/Favorite.gif"));
	public final Action HELP_TO_DO     = new ActionHelpShowDocument("To Do List", ActionHelpShowDocument.DOUCMENT_TODO, ProgramIcons.getInstance().findIcon("images/List.gif"));
	public final Action HELP_KNOWNBUGS = new ActionHelpShowDocument("Known Bugs", ActionHelpShowDocument.DOCUMENT_BUGS, ProgramIcons.getInstance().findIcon("images/Error.gif"));

	public final Action HELP_LICENSE = new ActionHelpShowDocument("License Agreement", ActionHelpShowDocument.DOCUMENT_LICENSE, ProgramIcons.getInstance().findIcon("images/List.gif"));

	public final BrowserPropertyChangeListener BROWSER_LISTENER = new BrowserPropertyChangeListener();

	final JMenu mnuTasks = new JMenu("Tasks");
	AboutDialog about = null;

	private ProgressFrame mainProgress;
	private int mainProgressValue;
	JPanel mainPanel = new JPanel();
	BorderLayout mainBorderLayout = new BorderLayout();
	JTabbedPane pluginsTabbedPane = new JTabbedPane();
	PluginIDEBrowser idebrowser;
	JSplitPane jSplitPane1 = new JSplitPane();
	JPopupMenu pluginPopupMenu = new JPopupMenu();

	private RunningPlugins runningPlugins = new RunningPlugins();
	private JButton btnSave = new JButton(FILE_SAVE);
	private JToolBar tbToolbar = new JToolBar();
	private JButton btnOpen = new JButton(FILE_OPEN);


	/**
	 * JBuilder likes to see a "jbInit" method and runs this method to show
	 * the program in design-time. Since I use JBuilder, I have this code to
	 * make it happy.
	 */
	public void jbInit() {

		idebrowser = new PluginIDEBrowser();
		idebrowser.addPropertyChangeListener(PluginIDEBrowser.PROPERTY_ELEMENT_SELECTED, BROWSER_LISTENER);
		idebrowser.setSQLIDE(this);

		mainPanel.setLayout(mainBorderLayout);
		pluginsTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		pluginsTabbedPane.setToolTipText("");
		pluginsTabbedPane.addChangeListener(new TabChangeListener());
		pluginsTabbedPane.addMouseListener(new PluginPagesPopupAdapter());
		pluginPopupMenu.setInvoker(pluginsTabbedPane);
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(jSplitPane1, BorderLayout.CENTER);
		jSplitPane1.add(pluginsTabbedPane, "right");
		jSplitPane1.add(idebrowser, "left");
		mainPanel.add(tbToolbar,  BorderLayout.NORTH);
		JMenuItem mnuClosePlugin = pluginPopupMenu.add( PLUGIN_CLOSE );
		KeyStroke theAccel = (KeyStroke)PLUGIN_CLOSE.getValue(Action.ACCELERATOR_KEY);
		if ( theAccel != null ) mnuClosePlugin.setAccelerator(theAccel);
		tbToolbar.add(btnOpen, null);
		tbToolbar.add(btnSave, null);

	}

	/**
	 * Contructor
	 */
	public SqlIdeApplication() {

		mainProgress = ProgressFrame.createProgressFrame("SQLIDE Loading...", 0, 6);
		mainProgressValue = 0;
		mainProgress.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		DatabaseService.getInstance().setCredentialsProvider(credentialsProvider);

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
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
				idebrowser.initPlugin();
//			}
//		});


		mainProgress.changeMessage("Bootstrapping plugins...");
		try {
			bootstrapPlugins();
		} catch ( IOException exc ) {
			exc.printStackTrace();
		}

//		mainProgress.changeMessage("Creating Default Interactive SQL");
//		DatabaseProcess ideserver = DatabaseService.getInstance.getDatabaseProcess(ProgramConfig.getInstance().getDefaultDatabaseSpec());
//		mainProgress.setValue(++mainProgressValue);

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
		if ( theAction == SEPARATOR_ACTION ) {
			menu.addSeparator();
			return;
		}
		JMenuItem newItem = menu.add( theAction );
		KeyStroke theAccel = (KeyStroke)theAction.getValue(Action.ACCELERATOR_KEY);
		if ( theAccel != null ) newItem.setAccelerator(theAccel);
	}

	public void createMenus() {
		JMenu fileMenu;
		JMenu editMenu;
		JMenu toolsMenu;
		JMenu pluginMenu;
		JMenu activityMenu;
		JMenu helpMenu;

		menuBar = new JMenuBar();
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
		mnuTasks.setMnemonic('K');

		_addMenu(fileMenu, FILE_OPEN );
		_addMenu(fileMenu, FILE_SAVE );
		fileMenu.addSeparator();
		_addMenu(fileMenu, FILE_EXIT );
//		_addMenu(fileMenu, FILE_OPEN );
		_addMenu(editMenu, EDIT_CUT );
		_addMenu(editMenu, EDIT_COPY );
		_addMenu(editMenu, EDIT_PASTE );
		_addMenu(toolsMenu, TOOLS_CONFIGURE);
		_addMenu(toolsMenu, TOOLS_PLUGINS);


		_addMenu(pluginMenu, PLUGIN_CLOSE);
		_addMenu(pluginMenu,SELECT_LEFT);
		_addMenu(pluginMenu,SELECT_RIGHT);
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
//		_addMenu(helpMenu,HELP_CHANGES);

		menuBar.add( fileMenu   );
		menuBar.add( editMenu   );
		menuBar.add( toolsMenu  );
		menuBar.add( mnuTasks );
		menuBar.add( pluginMenu );
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add( helpMenu );

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

		ProgramConfig.getInstance();
		initializeUI();

		// If after this the vector is empty, show the wizard
		// for DBSpecs and save it (this will be useful to new users).
		if ( HostConfigRegistry.getInstance().getConnectionCount() == 0 ) {
			NewServerWizard wiz = NewServerWizard.showWizard(true);
			if ( wiz.result != NewServerWizard.OK ) System.exit(0);
			SqlideHostConfig config = wiz.getDBSpec();
			try {
				HostConfigFactory.saveSqlideHostConfig(config);

			}
			catch (Exception ex) {
				ex.printStackTrace();
			}

			HostConfigRegistry.getInstance().addSqlideHostConfig(config);
		}

		getInstance();
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

		frame.setSize(screenSize.width - insetx, screenSize.height - insety);
		Point point = SwingUtils.getCenteredWindowPoint(frame);
		frame.setLocation(point);
		frame.setVisible(true);

	}

	public static synchronized SqlIdeApplication getInstance() {
		if ( instance == null ) instance = new SqlIdeApplication();
		return instance;
	}

	private class TabChangeListener implements ChangeListener{

		public void stateChanged( ChangeEvent e ) {
			/** @todo Ask panels to hide their menus when this event fires. */
			  if ( rightIdePanel != null ) {
				//rightIdsePanel.setVisibleMenus(false, mnuPluginControl );
				rightIdePanel = (IDEVisualPluginIF)pluginsTabbedPane.getSelectedComponent();
//				if ( rightIdePanel != null ) rightIdePanel.setVisibleMenus(true, mnuPluginControl);
			  }
		};

	}

	public void refreshPanels() {
		idebrowser.refreshPanel();
//		if ( rightIdePanel != null ) rightIdePanel.refreshPanel();
		runningPlugins.refreshPlugins();
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

	public void createTasksMenu() {
		IDEVisualPluginIF plugin = getRightPanel();
		mnuTasks.removeAll();
		Action[] actions = null;
		if ( plugin != null ) {
			actions = plugin.getPossibleActions();
			if ( (actions != null) && (actions.length > 0)) {
				if (mnuTasks.getItemCount() > 0)
					mnuTasks.addSeparator();
				for (int i = 0; i < actions.length; i++) {
					_addMenu(mnuTasks, actions[i]);
				}
			}
		}
		actions = idebrowser.getPossibleActions();
		if ( (actions != null) && (actions.length > 0) ) {
			if ( mnuTasks.getItemCount() > 0 ) mnuTasks.addSeparator();
			for ( int i=0; i<actions.length; i++) { _addMenu(mnuTasks,actions[i]); }
		}
	}

	private void createPanelMenu(IDEVisualPluginIF plugin) {
		Action[] actions = plugin.getPossibleActions();
		// Now re-compose the popup menu with all the actions.
		pluginPopupMenu.removeAll();
		JMenuItem mnuClosePlugin = pluginPopupMenu.add( PLUGIN_CLOSE );
		KeyStroke theAccel = (KeyStroke)PLUGIN_CLOSE.getValue(Action.ACCELERATOR_KEY);
		if ( theAccel != null ) mnuClosePlugin.setAccelerator(theAccel);
		pluginPopupMenu.addSeparator();
		for ( int i=0; i<actions.length; i++) {
			JMenuItem newMenu = pluginPopupMenu.add( actions[i] );
			KeyStroke newAccel = (KeyStroke)actions[i].getValue(Action.ACCELERATOR_KEY);
			if ( newAccel != null ) mnuClosePlugin.setAccelerator(theAccel);
		}

	}

	/**
	 * Installs the Right-hand panel.
	 */
	public void setRightPanel(IDEVisualPluginIF plugin) {
		rightIdePanel = plugin;
		createTasksMenu();
		createPanelMenu(plugin);
		pluginsTabbedPane.add((Component)plugin, plugin.getPluginShortName()+" ("+plugin.getDatabaseProcess().getHostConfiguration().getName()+")" );
	}

	public IDEVisualPluginIF getRightPanel() {
		Component comp = pluginsTabbedPane.getSelectedComponent();
		IDEVisualPluginIF result = (IDEVisualPluginIF)comp;
		if ( result == null ) result = rightIdePanel;
		return result;
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
					pluginPopupMenu.show(pluginsTabbedPane, theX.intValue(), theY.intValue());
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

	public class ActionPluginClose extends AbstractAction {

		public ActionPluginClose() {
			super("Close Plugin", ProgramIcons.getInstance().findIcon("images/Folder.gif"));
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK, false));
			putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
		}

		public void actionPerformed(ActionEvent e) {
			Component comp = pluginsTabbedPane.getSelectedComponent();
			IDEPluginIF plugin = (IDEPluginIF)comp;
			pluginsTabbedPane.remove(comp);
			runningPlugins.endPlugin(plugin);
			createTasksMenu();
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
							LookAndFeel newLookAndFeel = SqlIdeApplication.changeLookAndFeel(newLF);
							SwingUtilities.updateComponentTreeUI(SqlIdeApplication.getFrame());
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

	public class ActionToolsPlugins extends AbstractAction {
		public ActionToolsPlugins() {
			super("Plugins...",ProgramIcons.getInstance().getServerIcon());
		}
		public void actionPerformed(ActionEvent e) {
			DlgPluginManager.showPluginManager(getInstance().getFrame(), "Plugin Manager");
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

	public class ActionHelpShowDocument extends AbstractAction {

		public final static String DOCUMENT_PAYING = "/docs/paying.html";
		public final static String DOCUMENT_README = "/docs/README.html";
		public final static String DOUCMENT_TODO   = "/docs/TODO.html";
		public final static String DOCUMENT_BUGS   = "/docs/BUGS.html";
		public final static String DOCUMENT_LICENSE = "/docs/LICENSE.html";

		public ShowDocumentDialog documentDialog = null;

		String documentName;
		String title;

		public ActionHelpShowDocument(String title, String documentName, Icon icon) {
			super(title,icon);
			this.documentName = documentName;
			this.title = title;
		}

		public void actionPerformed(ActionEvent evt) {
			synchronized ( ActionHelpShowDocument.class ) {
				if ( documentDialog == null ) documentDialog = new ShowDocumentDialog(SqlIdeApplication.getInstance().getFrame());
				documentDialog.showDialog(title, getClass().getResourceAsStream(documentName));
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
				pluginsTabbedPane.setSelectedComponent(pluginPanel);
			} finally {
				frame.setCursor(Cursor.getDefaultCursor());
				createTasksMenu();
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


	/**
	 * It loads and starts all the plugins that need to be initialized on
	 * startup.
	 * @throws IOException If the autoexec properties file is not found.
	 */
	private void bootstrapPlugins() throws IOException {

		InputStream is = SqlIdeApplication.class.getResourceAsStream("autoexec.plugins.properties");
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
//					System.out.println(def.pluginInstance.getPluginName()+" started.");
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
					JOptionPane.showMessageDialog(SqlIdeApplication.getFrame(), "Could not change the look and feel due to the following error: "+err.getMessage(), "Could not change look and feel", JOptionPane.ERROR_MESSAGE);
					err.printStackTrace();
				}
			}
		}
		return UIManager.getLookAndFeel();
	}

	public RunningPlugins getRunningPlugins() { return runningPlugins; }

	class BrowserPropertyChangeListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			createTasksMenu();
		}
	}

}
