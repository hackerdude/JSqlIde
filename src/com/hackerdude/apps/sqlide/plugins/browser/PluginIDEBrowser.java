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

import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.*;
import com.hackerdude.apps.sqlide.nodes.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;
import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.lib.GPLAboutDialog;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.*;
import java.util.*;
import java.lang.Object.*;
import java.io.*;
import java.sql.*;

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
public class PluginIDEBrowser extends JPanel implements IDEVisualPluginIF {  // JInternalFrame {

	DatabaseProcess currentProcess;
	BrowserModel serverBrowser;
	DBBrowserTree browserTree;
	DefaultMutableTreeNode top;
	JScrollPane scroll;
	BrowserRenderer brenderer;
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel BrowserPanel = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	JLabel lblInfo = new JLabel();
	JMenuItem mnuCopyName = new JMenuItem();
	SqlIdeApplication ide;
	BorderLayout borderLayout3 = new BorderLayout();

	/**
	 * JBuilder likes this
	 */
	public void jbInit() {

		top = new DefaultMutableTreeNode("My Environment");
		// Override the drawing for the tree with our own class, below.
		this.setLayout(borderLayout1);
		BrowserPanel.setLayout(borderLayout2);
		jPanel1.setLayout(borderLayout3);
		lblInfo.setText("Info:");
		lblInfo.setIcon(ProgramIcons.getInstance().findIcon("images/Inform.gif"));
		this.setMinimumSize(new Dimension(200, 58));
		mnuCopyName.setText("Copy Name");
		this.add(BrowserPanel, BorderLayout.CENTER);
		BrowserPanel.add(jPanel1, BorderLayout.SOUTH);
		createBrowserTree();
	}

	public void createBrowserTree() {
		JScrollPane oldScroll = null;
		if ( scroll != null ) oldScroll = scroll;
		browserTree = new DBBrowserTree(top);
		scroll =    new JScrollPane(browserTree);
		TreeWillExpandListener willExpand = new BrowserWillExpandListener();
		browserTree.addTreeWillExpandListener(willExpand);
		browserTree.setShowsRootHandles(false);
		browserTree.addMouseListener(new BrowserTreePopupAdapter());
		browserTree.putClientProperty("JTree.lineStyle", "Angled");
		browserTree.addTreeSelectionListener(
				new TreeSelectionListener() {
			public void valueChanged( TreeSelectionEvent e ) {
				TreePath tp = e.getNewLeadSelectionPath();
				Object[] objs;
				if ( tp == null )  return;
				objs = tp.getPath();
				TreeNode myNode;
				for ( int i=0; i<objs.length; i++) {
					myNode = (TreeNode)objs[i];
					if ( myNode != null && myNode instanceof ItemServerNode ) {
						currentProcess = ((ItemServerNode)myNode).getDatabaseProcess();
					}
/*				   myNode = (DefaultMutableTreeNode)objs[i];
	   Object userObject = myNode.getUserObject();
	   if ( userObject != null && userObject.getClass() == DatabaseProcess.class ) {
	  currentProcess = (DatabaseProcess)myNode.getUserObject();
	   }*/
				}
				browserTree_valueChanged(e);
			}
		});
				BrowserPanel.add(scroll, BorderLayout.CENTER);
				scroll.getViewport().add(browserTree, null);
				jPanel1.add(lblInfo, BorderLayout.CENTER);
				brenderer = new BrowserRenderer();
				brenderer.setOpenIcon(ProgramIcons.getInstance().getExpandIcon());
				brenderer.setClosedIcon(ProgramIcons.getInstance().getCollapseIcon());
				browserTree.setCellRenderer(brenderer);
				browserTree.setVisible(true);
				if ( oldScroll!= null ) BrowserPanel.remove(oldScroll);
				if ( ide != null ) ide.pack();

	}

	/**
	 * Constructor.
	 */
	public PluginIDEBrowser() {
		super();
	}

	public void initPlugin() {
		jbInit();
		serverBrowser = new BrowserModel(top);
		UIManager.put("Tree.expandedIcon", new IconUIResource(ProgramIcons.getInstance().getExpandIcon()));
		UIManager.put("Tree.collapsedIcon", new IconUIResource(ProgramIcons.getInstance().getCollapseIcon()));
		if (currentProcess == null) currentProcess = new DatabaseProcess(ProgramConfig.getInstance().getSqlideHostConfig(0));
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
	public JMenu createPanelMenu( JMenu parent ) {
		return(null);
	}


	/**
	 *  Shows/hides the menus.
	 */
	public void setVisibleMenus( boolean value, JMenu menu ){
	}

	/**
	 * Returns the Currently selected Database Process.
	 */
	public DatabaseProcess getDatabaseProcess() {
		System.out.println("[PanelIDEBrowser] "+currentProcess);
		return(currentProcess);

	}

	/**
	 * Since this is a collection panel and doesn't have
	 * one database process (it can potentially have many,
	 * or even zero), this method is empty. It is not very
	 * common to have this method empty, but it does happen
	 * in "Browser tree" situations that span across multiple
	 * servers.
	 */
	public void setDatabaseProcess(DatabaseProcess proc) {  }

	/**
	 * Returns the "short name" for this plugin (to be put on
	 * the tabs on a TabbedPane, for instance).
	 */
	public String getPluginShortName() { return("Browser"); }

	public String getPluginName() { return "SQL-IDE Browser"; }

	/**
	 * Returns the version of this plugin as a string.
	 */
	public String getPluginVersion() { return("$Revision$"); }


	/**
	 * The listener for the expansion of the nodes.
	 *
	 * see treeWillExpand call.
	 */
	class BrowserWillExpandListener implements TreeWillExpandListener {


		/**
		 * This method is called by the tree when the node is about to expand
		 * and before it actually does. This node
		 */
		public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {

			setCursor(new Cursor(Cursor.WAIT_CURSOR));

			TreePath path = e.getPath();
			Object comp = path.getLastPathComponent();
			if ( comp == null ) return;  // Odd... ignore.
			NodeIDEBase node = null;

			try {
				node = (NodeIDEBase)comp;
			} catch( ClassCastException exc ) {
				// Don't do anything
			}

			if ( node == null ) {
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
			} catch( Exception exc ) {
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
				NodeIDEBase node = (NodeIDEBase)comp;
				node.removeChildren();
				} catch ( ClassCastException exc ) {}
		}


	}

	/**
	 * The Renderer for the different Icons
	 */
	class BrowserRenderer extends DefaultTreeCellRenderer {

		ImageIcon folderIcon;
		ImageIcon catalogIcon;
		ImageIcon schemaIcon;
		ImageIcon serverIcon;
		ImageIcon loginsIcon;
		ImageIcon storedProcIcon;
		ImageIcon triggerIcon;
		ImageIcon expandIcon;
		ImageIcon collapseIcon;
		ImageIcon columnIcon;
		ImageIcon tableIcon;
		ImageIcon indexIcon;
		ImageIcon usersIcon;
		ImageIcon userIcon;

		public BrowserRenderer() {

			indexIcon      = ProgramIcons.getInstance().findIcon("images/BCard.gif");
			tableIcon      = ProgramIcons.getInstance().findIcon("images/Sheet.gif");
			folderIcon     = ProgramIcons.getInstance().findIcon("images/Folder.gif");
			columnIcon     = ProgramIcons.getInstance().findIcon("images/Column.gif");
			serverIcon     = ProgramIcons.getInstance().getServerIcon();
			catalogIcon    = ProgramIcons.getInstance().getDatabaseIcon();
			schemaIcon     = ProgramIcons.getInstance().findIcon("images/Data.gif");
			loginsIcon     = ProgramIcons.getInstance().getLoginsIcon();
			storedProcIcon = ProgramIcons.getInstance().getStoredProcIcon();
			triggerIcon    = ProgramIcons.getInstance().getTriggerIcon();
			expandIcon     = ProgramIcons.getInstance().getExpandIcon();
			collapseIcon   = ProgramIcons.getInstance().getCollapseIcon();
			usersIcon      = ProgramIcons.getInstance().findIcon("images/Users.gif");
			userIcon      = ProgramIcons.getInstance().findIcon("images/User.gif");
			setOpenIcon(expandIcon);
			setClosedIcon(collapseIcon);

		}

		public Component getTreeCellRendererComponent(  JTree tree,   Object value,
				boolean sel,  boolean expanded,
	boolean leaf, int row, boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			// Get this node.
			TreeNode node = (TreeNode)value;
			ImageIcon icon = null;
			TreeNode parent = node.getParent();
			icon = getIconfor(node);
			if ( tree.getModel().getRoot() == value ) { icon = ProgramIcons.getInstance().getRootIcon(); }
			if ( icon == null ) { System.out.println("[PanelIDEBrowser] Couldn't find icon for class "+node.getClass().getName()); }

			// Still No icon?? Put a default on it.
			if ( icon == null ) { icon = folderIcon; };
			setIcon(icon);

			return this;

		}

		protected ImageIcon getIconfor(TreeNode treeNode) {
			Class nodeClass = treeNode.getClass();
			ImageIcon icon = null;
			if ( nodeClass == ItemCatalogNode.class
				|| nodeClass == CategoryCatalogsNode.class ) { icon = catalogIcon; }
			if ( nodeClass == ItemSchemaNode.class
				|| nodeClass == CategorySchemaNode.class ) { icon = schemaIcon; }

			if ( nodeClass == ItemTableNode.class  )    { icon = tableIcon; }
			if ( nodeClass == CategoryTableNode.class ) { icon = folderIcon; }

			if ( nodeClass == CategoryStoredProcedureNode.class ) { icon = storedProcIcon; }
			if ( nodeClass == CategoryTriggerNode.class ) { icon = triggerIcon; }
			if ( nodeClass == ItemIndexNode.class ) { icon = indexIcon; }
			if ( nodeClass == ItemServerNode.class ) { icon = serverIcon; }
			if ( nodeClass == CategoryIndexesNode.class ) { icon = indexIcon; }

			if ( nodeClass == CategoryColumnsNode.class ) { icon = columnIcon; }
			if ( nodeClass == ItemTableColumnNode.class ) { icon = columnIcon; }
			if ( nodeClass == CategoryUsersNode.class ) { icon = usersIcon; }
			if ( nodeClass == CategoryGeneralUsersNode.class ) { icon = usersIcon; }
			if ( treeNode instanceof ItemUserNode ) { icon = userIcon; }
			if ( nodeClass == CategoryDbUsersNode.class ) { icon = catalogIcon; }
			if ( nodeClass == ItemStoredProcedureNode.class ) { icon = storedProcIcon; }

			return icon;
		}


	}

	public boolean isActionPossible( String action ) {
		boolean theResult = false;
		if ( action.equals("Cut") ) {  };
		if ( action.equals("Copy") ) {  };
		if ( action.equals("Paste") ) {  };
		return(theResult);
	}

	public boolean executeAction( String action ) {
		/** @todo Implement */
		return false;
	}

	public void showAboutBox() {
		GPLAboutDialog gpl = new GPLAboutDialog(this, "IDE Browser Panel",getPluginVersion(),
				"A Tree-Based Multiple Database browser panel for SQLIDE.",
	"(C) 1999 by David Martinez.");
		gpl.actionPerformed(null);
	}

	public void refreshPanel() {
		serverBrowser = new BrowserModel(top);
		createBrowserTree();
	}

	void browserTree_valueChanged(TreeSelectionEvent e) {
		TreePath tp = browserTree.getSelectionPath();
		if ( tp == null ) {
			lblInfo.setText("");
			return;
		}
		TreeNode tn = (DefaultMutableTreeNode)tp.getLastPathComponent();
		NodeIDEBase bn = null;
		if ( tn instanceof NodeIDEBase ) { bn = (NodeIDEBase)tn; }
		if ( bn != null ) lblInfo.setText(bn.getInfo());

	}

	public class BrowserTreePopupAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			try {
				/** @todo This does not work on MacOS */
				if ( e.getModifiers() == e.BUTTON3_MASK || e.getModifiers() == e.CTRL_MASK ) {
					TreePath[] selectedPaths = browserTree.getSelectionModel().getSelectionPaths();
					if ( selectedPaths == null ) return;
					NodeIDEBase[] selectedNodes = new NodeIDEBase[selectedPaths.length];
					for ( int i=0; i<selectedPaths.length; i++ ) {
						Object lastPathComponent = selectedPaths[i].getLastPathComponent();
						Object selectedNode = lastPathComponent;
						if ( selectedNode instanceof NodeIDEBase ) {
							selectedNodes[i] = (NodeIDEBase)selectedNode;
						} else return;
					}
					Action[] actions = ide.getActionsFor(selectedNodes);
					if ( actions == null || actions.length > 0 ) {
						JPopupMenu thisMenu = new JPopupMenu("Actions");
						for ( int i=0; i<actions.length; i++) thisMenu.add(actions[i]);
						if ( thisMenu != null ) {
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

	public JPopupMenu getPopupMenuFor(NodeIDEBase node) { return null; }

	public Action[] getActionsFor(NodeIDEBase node) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Action[] getPossibleActions() {
		/** @todo Implement */
		throw new UnsupportedOperationException("Not Implemented");
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

}
