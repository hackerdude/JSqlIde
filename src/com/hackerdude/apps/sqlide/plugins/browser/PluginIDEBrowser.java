/**
 *   PanelIDEBrowser.java - a SQL Connection Browser.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
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
 * @author David Martinez <david@hackerdude.com>
 * Revision: $Revision$
 * Id      : $Id$
 *
 */
package com.hackerdude.apps.sqlide.plugins.browser;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.tree.*;

import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.nodes.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;
import com.hackerdude.lib.*;

/**
 *   Based on the sqlIDE Config, this browser
 *   creates as many server nodes as necessary, and
 *   it initializes all the default nodes. Eventually
 *   we'll use these nodes and their DatabaseProcess'
 *   classes to get metadata and server information.
 *
 *   @version $Id$
 *
 */
public class PluginIDEBrowser extends JPanel
	  implements IDEVisualPluginIF {

	DatabaseProcess currentProcess;
	BrowserModel serverBrowser;
	DBBrowserTree browserTree;
	DefaultMutableTreeNode rootNode;
	JScrollPane scroll;

	BorderLayout mainBorderLayout = new BorderLayout();
	JPanel browserPanel = new JPanel();
	BorderLayout browserPanelBorderLayout = new BorderLayout();
	JPanel bottomPanel = new JPanel();
	JLabel lblInfo = new JLabel();

	SqlIdeApplication ide;

	BorderLayout borderLayout3 = new BorderLayout();

        public final Action ACTION_POPUP_MENU = new PopupMenuAction();

	private final BrowserRenderer BROWSER_RENDERER = new BrowserRenderer();
	private final TreeSelectionListener BROWSER_SELECTION_LISTENER = new IDEBrowserSelectionListener();
	private final TreeWillExpandListener BROWSER_WILL_EXPAND_LISTENER = new BrowserWillExpandListener();

	private Action[] contextActions = NULL_ACTIONS;

	public static final String PROPERTY_ELEMENT_SELECTED = "nodeSelected";

	/**
	 * JBuilder likes this
	 */
	public void jbInit() {
		rootNode = new DefaultMutableTreeNode("My Environment");
		// Override the drawing for the tree with our own class, below.
		this.setLayout(mainBorderLayout);
		browserPanel.setLayout(browserPanelBorderLayout);
		bottomPanel.setLayout(borderLayout3);
		lblInfo.setText("Info:");
		lblInfo.setIcon(ProgramIcons.getInstance().findIcon("images/Inform.gif"));
		this.setMinimumSize(new Dimension(200, 58));
		this.add(browserPanel, BorderLayout.CENTER);
		browserPanel.add(bottomPanel, BorderLayout.SOUTH);
		createBrowserTree();
	}

	public void createBrowserTree() {
		JScrollPane oldScroll = null;
		if (scroll != null)
			oldScroll = scroll;
		browserTree = new DBBrowserTree(rootNode);
		scroll = new JScrollPane(browserTree);
		browserTree.setShowsRootHandles(false);
		browserTree.addMouseListener(new BrowserTreePopupAdapter());
		browserTree.putClientProperty("JTree.lineStyle", "Angled");
		browserTree.addTreeWillExpandListener(BROWSER_WILL_EXPAND_LISTENER);
		browserTree.addTreeSelectionListener(BROWSER_SELECTION_LISTENER);
		browserPanel.add(scroll, BorderLayout.CENTER);
		scroll.getViewport().add(browserTree, null);
		bottomPanel.add(lblInfo, BorderLayout.CENTER);
		BROWSER_RENDERER.setOpenIcon(ProgramIcons.getInstance().getExpandIcon());
		BROWSER_RENDERER.setClosedIcon(ProgramIcons.getInstance().getCollapseIcon());
		browserTree.setCellRenderer(BROWSER_RENDERER);
		browserTree.setVisible(true);
		browserTree.setFont(ProgramConfig.getInstance().getFont(ProgramConfig.PROP_FONT_FOR_BROWSER));

		if (oldScroll != null)
			browserPanel.remove(oldScroll);
		if (ide != null)
			ide.pack();

	}

	/**
	 * A basic selection listener for the browser tree. It changes the current
	 * process for this plugin and it also changes the information label in the
	 * bottom to reflect what has been selected.
	 */
	class IDEBrowserSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			determineContextActions();

			TreePath leadSelectionPath = e.getNewLeadSelectionPath();
			Object[] objs;
			if (leadSelectionPath == null)
				return;
			objs = leadSelectionPath.getPath();
			TreeNode myNode;
			for (int i = 0; i < objs.length; i++) {
				myNode = (TreeNode) objs[i];
				if (myNode != null && myNode instanceof ItemServerNode) {
					currentProcess = ( (ItemServerNode) myNode).getDatabaseProcess();
				}
			}

			TreePath selectionPath = browserTree.getSelectionPath();
			if (selectionPath == null) {
				lblInfo.setText("");
				return;
			}
			TreeNode tn = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();

			NodeIDEBase bn = null;
			if (tn instanceof NodeIDEBase) {
				bn = (NodeIDEBase) tn;
				firePropertyChange(PROPERTY_ELEMENT_SELECTED, null, bn);
			}
			if (bn != null)
				lblInfo.setText(bn.getInfo());
		}
	}

	/**
	 * Constructor.
	 */
	public PluginIDEBrowser() {
		super();
	}

	public void initPlugin() {
		jbInit();
		serverBrowser = new BrowserModel(rootNode);
		UIManager.put("Tree.exp andedIcon", new IconUIResource(ProgramIcons.getInstance().getExpandIcon()));
		UIManager.put("Tree.collapsedIcon", new IconUIResource(ProgramIcons.getInstance().getCollapseIcon()));
		if (currentProcess == null)
			currentProcess = DatabaseService.getInstance().getDatabaseProcess(
						   HostConfigRegistry.getInstance().getSqlideHostConfig(0)
						   );
	}

	public void freePlugin() {
		//
	}

	public void setSQLIDE(SqlIdeApplication ide) {
		this.ide = ide;
	}

	/**
	 *  Creates the IdeBrowser-specific menu.
	 */
	public JMenu createPanelMenu(JMenu parent) {
		return (null);
	}

/**
	 *  Shows/hides the menus.
	 */
	public void setVisibleMenus(boolean value, JMenu menu) {
	}

/**
	 * Returns the Currently selected Database Process.
	 */
	public DatabaseProcess getDatabaseProcess() {
		System.out.println("[PanelIDEBrowser] " + currentProcess);
		return (currentProcess);

	}

/**
	 * Since this is a collection panel and doesn't have
	 * one database process (it can potentially have many,
	 * or even zero), this method is empty. It is not very
	 * common to have this method empty, but it does happen
	 * in "Browser tree" situations that span across multiple
	 * servers.
	 */
	public void setDatabaseProcess(DatabaseProcess proc) {}

/**
	 * Returns the "short name" for this plugin (to be put on
	 * the tabs on a TabbedPane, for instance).
	 */
	public String getPluginShortName() {
		return ("Browser");
	}

	public String getPluginName() {
		return "SQL-IDE Browser";
	}

/**
	 * Returns the version of this plugin as a string.
	 */
	public String getPluginVersion() {
		return ("$Revision$");
	}

	/**
	 * The listener for the expansion of the nodes.
	 *
	 * see treeWillExpand call.
	 */
	class BrowserWillExpandListener
		  implements TreeWillExpandListener {

		/**
		 * This method is called by the tree when the node is about to expand
		 * and before it actually does. This node
		 */
		public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {

			setCursor(new Cursor(Cursor.WAIT_CURSOR));

			TreePath path = e.getPath();
			Object comp = path.getLastPathComponent();
			if (comp == null)
				return; // Odd... ignore.
			NodeIDEBase node = null;

			try {
				node = (NodeIDEBase) comp;
			}
			catch (ClassCastException exc) {
				// Don't do anything
			}

			if (node == null) {
				setCursor(Cursor.getDefaultCursor());
				return;
			}
			// Remove all the children, and re-read them.
			node.removeAllChildren();
			try {
				/* Read the standard children for this node, and also ask
				  any running  browser extension plugins to put their
				  contributions to this node (if any). */
				node.readChildren();
				ide.requestAddSubNodes(node);
			}
			catch (Exception exc) {
				node.removeAllChildren();
				node.add(new DefaultMutableTreeNode("Dummy"));
				setCursor(Cursor.getDefaultCursor());
				throw new ExpandVetoException(e);
			}
			setCursor(Cursor.getDefaultCursor());

		}

		// Required by TreeWillExpandListener interface.
		public void treeWillCollapse(TreeExpansionEvent e) {
			TreePath path = e.getPath();
			Object comp = path.getLastPathComponent();
			try {
				NodeIDEBase node = (NodeIDEBase) comp;
				node.removeChildren();
			}
			catch (ClassCastException exc) {}
		}

	}

	/**
	 * The Renderer for the different Icons
	 */
	class BrowserRenderer extends DefaultTreeCellRenderer {

		final ImageIcon folderIcon = ProgramIcons.getInstance().findIcon("images/Folder.gif");
		final ImageIcon catalogIcon = ProgramIcons.getInstance().getDatabaseIcon();
		final ImageIcon schemaIcon = ProgramIcons.getInstance().findIcon("images/Data.gif");
		final ImageIcon serverIcon = ProgramIcons.getInstance().getServerIcon();
		final ImageIcon loginsIcon = ProgramIcons.getInstance().getLoginsIcon();
		final ImageIcon storedProcIcon = ProgramIcons.getInstance().getStoredProcIcon();
		final ImageIcon triggerIcon = ProgramIcons.getInstance().getTriggerIcon();
		final ImageIcon expandIcon = ProgramIcons.getInstance().getExpandIcon();
		final ImageIcon collapseIcon = ProgramIcons.getInstance().getCollapseIcon();
		final ImageIcon columnIcon = ProgramIcons.getInstance().findIcon("images/Column.gif");
		final ImageIcon tableIcon = ProgramIcons.getInstance().findIcon("images/Sheet.gif");
		final ImageIcon indexIcon = ProgramIcons.getInstance().findIcon("images/BCard.gif");
		final ImageIcon usersIcon = ProgramIcons.getInstance().findIcon("images/Users.gif");
		final ImageIcon userIcon = ProgramIcons.getInstance().findIcon("images/User.gif");

		public BrowserRenderer() {

			setOpenIcon(expandIcon);
			setClosedIcon(collapseIcon);

		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
			  boolean sel, boolean expanded,
			  boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			// Get this node.
			TreeNode node = (TreeNode) value;
			ImageIcon icon = null;
			TreeNode parent = node.getParent();
			icon = getIconfor(node);
			if (tree.getModel().getRoot() == value) {
				icon = ProgramIcons.getInstance().getRootIcon();
			}
			if (icon == null) {
				System.out.println("[PanelIDEBrowser] Couldn't find icon for class " + node.getClass().getName());
			}

			// Still No icon?? Put a default on it.
			if (icon == null) {
				icon = folderIcon;
			}
			;
			setIcon(icon);

			return this;

		}

		protected ImageIcon getIconfor(TreeNode treeNode) {
			Class nodeClass = treeNode.getClass();
			ImageIcon icon = null;
			if ( treeNode instanceof NodeIDEBase ) {
				icon = ((NodeIDEBase)treeNode).getIcon();
			}
			if ( icon != null ) return icon;

			if (nodeClass == ItemCatalogNode.class
				|| nodeClass == CategoryCatalogsNode.class) {
				icon = catalogIcon;
			}
			if (nodeClass == ItemSchemaNode.class
				|| nodeClass == CategorySchemaNode.class) {
				icon = schemaIcon;
			}

			if (nodeClass == ItemTableNode.class) {
				icon = tableIcon;
			}
			if (nodeClass == CategoryTableNode.class) {
				icon = folderIcon;
			}

			if (nodeClass == CategoryTriggerNode.class) {
				icon = triggerIcon;
			}
			if (nodeClass == ItemIndexNode.class) {
				icon = indexIcon;
			}
			if (nodeClass == ItemServerNode.class) {
				icon = serverIcon;
			}
			if (nodeClass == CategoryIndexesNode.class) {
				icon = indexIcon;
			}

			if (nodeClass == CategoryColumnsNode.class) {
				icon = columnIcon;
			}
			if (nodeClass == ItemTableColumnNode.class) {
				icon = columnIcon;
			}
			if (nodeClass == CategoryUsersNode.class) {
				icon = usersIcon;
			}
			if (nodeClass == CategoryGeneralUsersNode.class) {
				icon = usersIcon;
			}
			if (treeNode instanceof ItemUserNode) {
				icon = userIcon;
			}
			if (nodeClass == CategoryDbUsersNode.class) {
				icon = catalogIcon;
			}

			return icon;
		}

	}

	public boolean isActionPossible(String action) {
		boolean theResult = false;
		if (action.equals("Cut")) {}
		;
		if (action.equals("Copy")) {}
		;
		if (action.equals("Paste")) {}
		;
		return (theResult);
	}

	public boolean executeAction(String action) {
		/** @todo Implement */
		return false;
	}

	public void showAboutBox() {
		GPLAboutDialog gpl = new GPLAboutDialog(this, "IDE Browser Panel", getPluginVersion(),
												"A Tree-Based Multiple Database browser panel for SQLIDE.",
												"(C) 1999 by David Martinez.");
		gpl.actionPerformed(null);
	}

	public void refreshPanel() {
//		serverBrowser = new BrowserModel(rootNode);
//		createBrowserTree();
		DefaultTreeModel model = (DefaultTreeModel)browserTree.getModel();
		serverBrowser.createDBNodes();
		model.nodeStructureChanged(rootNode);
		browserTree.setFont(ProgramConfig.getInstance().getFont(ProgramConfig.PROP_FONT_FOR_BROWSER));
	}


	/**
	 * This is a Popup Adapter for the browser tree. It actually checks for the
	 * right-click because some JDKs don't have a registered gesture (at least
	 * as of JDK1.2).
	 */
	public class BrowserTreePopupAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				/** @todo This does not work on MacOS */
				if (e.getModifiers() == e.BUTTON3_MASK || e.getModifiers() == e.CTRL_MASK) {
					if (contextActions == null || contextActions.length > 0) {
						JPopupMenu thisMenu = new JPopupMenu("Actions");
						for (int i = 0; i < contextActions.length; i++)
							thisMenu.add(contextActions[i]);
						if (thisMenu != null) {
							Double theX = new Double(e.getPoint().getX());
							Double theY = new Double(e.getPoint().getY());
							thisMenu.show(browserTree, theX.intValue(), theY.intValue());
						}
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * Determines the available context actions for this panel. These actions
	 * change when the current selection changes on the browser.
	 */
	public void determineContextActions() {
		TreePath[] selectedPaths = browserTree.getSelectionModel().getSelectionPaths();
		if (selectedPaths == null) return;
		NodeIDEBase[] selectedNodes = new NodeIDEBase[selectedPaths.length];
		for (int i = 0; i < selectedPaths.length; i++) {
			Object lastPathComponent = selectedPaths[i].getLastPathComponent();
			Object selectedNode = lastPathComponent;
			if (selectedNode instanceof NodeIDEBase) {
				selectedNodes[i] = (NodeIDEBase) selectedNode;
			}
			else return;
		}
		contextActions = ide.getActionsFor(selectedNodes);
	}

	public JPopupMenu getPopupMenuFor(NodeIDEBase node) {
		return null;
	}


	public Action[] getPossibleActions() {
		return contextActions;
	}

	public void receivePluginFocus() {
		browserTree.requestFocus();
	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().findIcon("images/World2.gif");
	}

	public boolean executeStandardAction(ActionEvent evt) {
		/** @todo Implement. */
		return false;
	}


        public class PopupMenuAction extends AbstractAction {
          public PopupMenuAction() {
                  super("Display Popup Menu");
                  putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F12, Event.CTRL_MASK, false));
//                  putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_A));
          }
          public void actionPerformed(ActionEvent e) {
//                  if ( about == null ) {
//                          about = new AboutDialog(frame);
//                          about.pack();
//                          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//                          double x = ( screenSize.getWidth() - about.getWidth() ) / 2;
//                          double y = ( screenSize.getHeight() - about.getHeight() ) / 2;
//                          about.setBounds(new Double(x).intValue(), new Double(y).intValue(), about.getWidth(), about.getHeight());
//                  }
//                  about.show();
          }

        }

}