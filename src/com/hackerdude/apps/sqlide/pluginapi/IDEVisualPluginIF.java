package com.hackerdude.apps.sqlide.pluginapi;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;

/**
 * Visual Plugin Interface
 *
 * <P>Classes implementing this interface are treated as visual plugins and
 * can be embedded on the right hand side of the SQLIDE frame.
 */
 public interface IDEVisualPluginIF extends IDEPluginIF {

	 /**
	  * This method will be called at various times, most notably when the panel is receiving the focus.
	  *
	  * The method should return all the possible panel-specific actions.
	  */
	 public Action[] getPossibleActions();

	 /**
	  * This method is called after the user has requested to switch focus to
	  * this panel using the keyboard. Implement this method by calling the
	  * grabFocus() method on the component that is most appropriate.
	  */
	 public void receivePluginFocus();

	 /**
	 * SQLIDE will call this method after creating the plugin for execution.
	 * <P>You will receive the databaseprocess this plugin is intended to be
	 * associated with. Use this databaseprocess to execute your queries.
	 *
	 */
	 public void setDatabaseProcess(DatabaseProcess proc);

	 /**
	  * Implement this method by returning the DatabaseProcess object you received
	  * with setDatabaseProcess().
	  */
	 public DatabaseProcess getDatabaseProcess();

	 /**
	  * This method will be called whenever standard actions (such as
	  * cut, copy and paste) are called.
	  * <P>Your program should return true if the action was executed, or
	  * false otherwise.
	  */
	 public boolean executeStandardAction(ActionEvent evt);

 }
