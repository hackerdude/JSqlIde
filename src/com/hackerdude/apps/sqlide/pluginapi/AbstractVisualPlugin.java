package com.hackerdude.apps.sqlide.pluginapi;

import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import com.hackerdude.apps.sqlide.ProgramIcons;

/**
 * Abstract Implementation of the Visual Plugin API Version 2.0
 *
 * <P>People implementing Visual Plugins are advised to extend this
 * abstract to save them some time from having to implement a
 * lot of functionality on their own.
 *
 * <P>This abstract provides basic functionality to make it easy
 * to implement a plugin with standard cut, copy and paste
 * actions, as well as File Open, File Close, etcetera.
 */
public abstract class AbstractVisualPlugin extends JPanel implements IDEVisualPluginIF {


	/**
	 * This is the standard File Open action. It comes with an Icon from the
	 * sqlide ProgramIcons cache and calls the abstract doOpenFile().
	 */
	private Action FILE_OPEN = new SqlIdeAction("Open", ProgramIcons.getInstance().findIcon("images/Open.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK, false), KeyEvent.VK_O) {
		public void actionPerformed(ActionEvent ev) {
			doOpenFile();
		}
	};

	/**
	 * This is the standard File Save Action. It comes with an Icon from the
	 * SQLIDE ProgramIcons cache and calls the abstract doSaveFile.
	 */
	private Action FILE_SAVE  = new SqlIdeAction("Save", ProgramIcons.getInstance().findIcon("images/Save.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK, false), KeyEvent.VK_S) {
		public void actionPerformed(ActionEvent ev) {
			doSaveFile();
		}
	};

	/**
	 * This is the standard File Save As... Action. It comes with an Icon from the
	 * SQLIDE ProgramIcons cache and calls the abstract doSaveFileAs.
	 */
	private Action FILE_SAVE_AS  = new SqlIdeAction("Save As", ProgramIcons.getInstance().findIcon("images/Save.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK, false), KeyEvent.VK_A) {
		public void actionPerformed(ActionEvent ev) {
			doSaveFileAs();
		}
	};

	/**
	 * This is the standard Edit Cut action. It comes with an Icon from the
	 * SQLIDE ProgramIcons cache and calls the abstract doCut.
	 */
	private Action EDIT_CUT   = new SqlIdeAction("Cut", ProgramIcons.getInstance().findIcon("images/Cut.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK, false), KeyEvent.VK_T){
		public void actionPerformed(ActionEvent ev) {
			doCut();
		}
	};

	/**
	 * This is the standard Edit Copy action. It comes with an Icon from the
	 * SQLIDE ProgramIcons cache and calls the abstract doCopy
	 */
	private Action EDIT_COPY  = new SqlIdeAction("Copy", ProgramIcons.getInstance().findIcon("images/Copy.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK, false), KeyEvent.VK_C){
		public void actionPerformed(ActionEvent ev) {
			doCopy();
		}
	};

	/**
	 * This is the standard Edit Paste. It comes with an Icon from the
	 * SQLIDE ProgramIcons cache and calls the abstract doPaste.
	 */
	private Action EDIT_PASTE = new SqlIdeAction("Paste", ProgramIcons.getInstance().findIcon("images/Paste.gif"), KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK, false), KeyEvent.VK_P){
		public void actionPerformed(ActionEvent ev) {
			doPaste();
		}
	};

	/**
	 * This array list contains all the actions
	 * for the plugin. If you add your own actions and leave the default
	 * implementation of getAvailableActions() they will automatically be
	 * passed to all the context menus.
	 */
	protected ArrayList pluginActions;

	/**
	 * Creates a new Visual Plugin.
	 */
	public AbstractVisualPlugin() {
	}

	/**
	 * Initializes the plugin for execution.
	 * For this abstract this means to initialize
	 * its base standardActions member.
	 */
	public void initPlugin() {
		pluginActions = new ArrayList();
		// Add all the standard actions.
		pluginActions.add(getActionFileOpen());
		pluginActions.add(getActionFileSave());
		pluginActions.add(getActionFileSaveAs());

		pluginActions.add(getActionEditCut());
		pluginActions.add(getActionEditCopy());
		pluginActions.add(getActionEditPaste());

	}

	/**
	* Standard "Open File" action.
	*/
	protected Action getActionFileOpen() { return FILE_OPEN; }

	/**
	* Standard "Save File" action.
	*/
	protected Action getActionFileSave() { return FILE_SAVE; }

	/**
	 * Standard "Save As" action.
	 */
	protected Action getActionFileSaveAs() { return FILE_SAVE_AS; }

	/**
	 * Returns the standard "Cut" Action.
	 */
	protected Action getActionEditCut() { return EDIT_CUT; }

	protected Action getActionEditCopy() { return EDIT_COPY; }

	protected Action getActionEditPaste() { return EDIT_PASTE; }

	protected abstract void doOpenFile();
	protected abstract void doSaveFile();
	protected abstract void doSaveFileAs();

	protected abstract void doCut();
	protected abstract void doCopy();
	protected abstract void doPaste();


}
