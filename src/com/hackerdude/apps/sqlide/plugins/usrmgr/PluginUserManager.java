package com.hackerdude.apps.sqlide.plugins.usrmgr;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.pluginapi.*;
import java.awt.*;
import javax.swing.*;
import com.hackerdude.apps.sqlide.ProgramIcons;
import java.awt.event.ActionEvent;

/**
 * A User Manager Panel
 */

public class PluginUserManager extends AbstractVisualPlugin {
	/**
	 * BorderLayout borderLayout1
	 */
	BorderLayout borderLayout1 = new BorderLayout();
	/**
	 * JPanel jPanel1
	 */
	JPanel jPanel1 = new JPanel();
	/**
	 * JLabel jLabel1
	 */
	JLabel jLabel1 = new JLabel();
	/**
	 * JPanel jPanel2
	 */
	JPanel jPanel2 = new JPanel();
	/**
	 * JPanel jPanel3
	 */
	JPanel jPanel3 = new JPanel();
	/**
	 * BorderLayout borderLayout2
	 */
	BorderLayout borderLayout2 = new BorderLayout();

	DatabaseProcess proc;

	/**
	 * Creates a new user manager.
	 *
	 */
	public PluginUserManager() {
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Changes the database process to the specified database process.
	 *
	 */
	public void setDatabaseProcess(DatabaseProcess proc) {
		this.proc = proc;
	}
	/**
	 * getDatabaseProcess
	 *
	 * @return the returned DatabaseProcess
	 */
	public DatabaseProcess getDatabaseProcess() {
		return proc;
	}

	public String getPluginName()  {
		return "User Manager";
	}
	/**
	 * getShortName
	 *
	 * @return the returned String
	 */
	public String getPluginShortName() {
		return "UserMgr";
	}
	/**
	 * getPluginVersion
	 *
	 * @return the returned String
	 */
	public String getPluginVersion() {
		return "0.0";
	}
	/**
	 * @param value parameter for setVisibleMenus
	 *
	 */
	public void setVisibleMenus(boolean value, JMenu menu) {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.IDEPanelInterface method*/
		throw new java.lang.UnsupportedOperationException("Method setVisibleMenus() not yet implemented.");
	}
	/**
	 * This method returns true if the action is possible at this point in time, false otherwise.
	 *
	 * @param action Action you want to find out if it is possible.
	 */
	public boolean isActionPossible(String action) {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.IDEPanelInterface method*/
		throw new java.lang.UnsupportedOperationException("Method isActionPossible() not yet implemented.");
	}
	/**
	 * executeAction
	 *
	 * @param actionName parameter for executeAction
	 * @return the returned boolean
	 */
	public boolean executeStandardAction(ActionEvent evt) {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.IDEPanelInterface method*/
		throw new java.lang.UnsupportedOperationException("Method executeAction() not yet implemented.");
	}
	/**
	 * showAboutBox
	 *
	 */
	public void showAboutBox() {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.IDEPanelInterface method*/
		throw new java.lang.UnsupportedOperationException("Method showAboutBox() not yet implemented.");
	}
	/**
	 * refreshPanel
	 *
	 */
	public void refreshPanel() {
	}

	/**
	 * getAvailableActions
	 *
	 * @return the returned Action[]
	 */
	public Action[] getPossibleActions() {
		return null;
	}
	/**
	 * getActionsFor
	 *
	 * @param node parameter for getActionsFor
	 * @return the returned Action[]
	 */
	public Action[] getActionsFor(NodeIDEBase[] node) {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.IDEPanelInterface method*/
		throw new java.lang.UnsupportedOperationException("Method getActionsFor() not yet implemented.");
	}

	/**
	 * jbInit
	 *
	 * @throws Exception -
	 */
	private void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		jLabel1.setText("Manage Users By");
		jPanel3.setBorder(BorderFactory.createEtchedBorder());
		jPanel3.setLayout(borderLayout2);
		this.add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(jLabel1, null);
		this.add(jPanel2, BorderLayout.SOUTH);
		this.add(jPanel3, BorderLayout.CENTER);
	}

	/**
	 * requestIDEFocus
	 *
	 */
	public void receivePluginFocus() {
		throw new java.lang.UnsupportedOperationException("Method requestIDEFocus() not yet implemented.");
	}

	public void freePlugin() {

	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getDatabaseIcon();

	}
	public void doCut() {

	}

	public void doCopy() {

	}

	public void doPaste() {

	}

	public void doSaveFileAs() {

	}

	public void doSaveFile() {

	}

	public void doOpenFile() {

	}

}
