package com.hackerdude.apps.sqlide.plugins.pgsql.storedproc;

import javax.swing.Action;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.sql.*;

/**
 * A stored procedure editor.
 */
public class StoredProcedureEditor extends AbstractVisualPlugin {

	DatabaseProcess process;
	ItemStoredProcedureNode storedProcedure;
	EditorPanel editorPanel;
	BorderLayout layout;

	public StoredProcedureEditor() {
		super();
		layout = new BorderLayout();
		setLayout(layout);
		editorPanel  = new EditorPanel();
		add(editorPanel, BorderLayout.CENTER);
	}

	public String getPluginName() { return "Procedure Editor"; }
	public String getPluginVersion() { return "0.0"; }
	public String getPluginShortName() { return "Function "+(storedProcedure==null?"":" - "+storedProcedure.toString()); }

	public void initPlugin() {
		super.initPlugin();
	}

	public void freePlugin() {

	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().findIcon("images/storedproc.gif");
	}

	public Action[] getPossibleActions() {
		return NULL_ACTIONS;
	}

	public void receivePluginFocus() {

	}

	public void setDatabaseProcess(DatabaseProcess process) {
		this.process = process;
	}

	public DatabaseProcess getDatabaseProcess() {
		return process;
	}
	public boolean executeStandardAction(ActionEvent evt) {
		return false;
	}

	public void setStoredProcedure(ItemStoredProcedureNode storedProcedure) {
		this.storedProcedure = storedProcedure;

	}

	public void doCopy() {}

	public void doCut() {

	}

	public void doPaste() {

	}

	public void doSaveFileAs() {

	}
	public void doSaveFile() {
		/** @todo Save procedure to the server. */
	}

	public void doOpenFile() {

	}

	public void readStoredProcedureSource() {
		Connection conn = null;
		ResultSet result = null;
		PreparedStatement statement = null;
		String sqlCall = ProcedureBrowserExtension.sqlCalls.getProperty(ProcedureBrowserExtension.SQL_RETRIEVE_PROC_SRC_PROPERTY);
		try {
			conn = process.getConnection();
			statement = conn.prepareStatement(sqlCall);
			statement.setString(1, storedProcedure.toString());
			result = statement.executeQuery();
			while ( result.next() ) {
				String procedureSource = result.getString("prosrc");
				editorPanel.area.setText(procedureSource);
//				editorPanel.area.setSelectionStart(0);
//				editorPanel.area.setSelectionEnd(0);
			}

		}
		catch (Exception ex) {

		}

	}


}