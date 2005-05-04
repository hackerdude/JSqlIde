package com.hackerdude.apps.sqlide.plugins.pgsql.storedproc;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.hackerdude.apps.sqlide.ProgramIcons;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;
import com.hackerdude.apps.sqlide.nodes.ItemServerNode;
import com.hackerdude.apps.sqlide.pluginapi.BrowserExtensionPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.IDENodeContextPluginIF;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.ItemCatalogNode;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.ItemSchemaNode;

/**
 * This is a stored procedure browser extension plugin.
 */
public class ProcedureBrowserExtension implements BrowserExtensionPluginIF, IDENodeContextPluginIF {

	public static final String PGSQL_PROCEDURE_SQL = "storedprocedures.pgsql.properties";

	public static final String SQL_RETRIEVE_PROCS_PROPERTY = "myprocs.retrieve";
	public static final String SQL_RETRIEVE_PROC_SRC_PROPERTY = "myprocs.retrieve.src";
	public static final String SQL_CREATE_PROC_PROPERTY = "myprocs.create.procedure";
	public static final String SQL_REPLACE_PROC_PROPERTY = "myprocs.replace.procedure";
	public static final String SQL_DELETE_PROC_PROPERTY  = "myproc.delete.procedure";

	public static final String SQL_TYPES_SELECT  = "myproc.select.types";

	public static final Properties sqlCalls = new Properties();

	public ProcedureBrowserExtension() {
	}

	public void requestAddSubNodes(NodeIDEBase parentNode) {
		DatabaseProcess proc = parentNode.getDatabaseProcess();
		if ( parentNode instanceof ItemSchemaNode ) {
			ItemSchemaNode schemaNode = (ItemSchemaNode)parentNode;
			String containerName = schemaNode.toString();
			parentNode.add( new CategoryStoredProcedureNode(null, containerName, proc));

		}

		if ( parentNode instanceof ItemCatalogNode ) {
			ItemCatalogNode catalogNode = (ItemCatalogNode)parentNode;
			String containerName = catalogNode.toString();
			parentNode.add(new CategoryStoredProcedureNode(containerName, null, proc));
		}

		if ( parentNode instanceof  ItemServerNode ) {
			parentNode.add(new CategoryStoredProcedureNode(null, null, proc));
		}

		if ( parentNode instanceof CategoryStoredProcedureNode ) {
			populateStoredProcedures((CategoryStoredProcedureNode)parentNode);
		}

	}

	public void initPlugin() {
		InputStream is = getClass().getResourceAsStream(PGSQL_PROCEDURE_SQL);
		if ( is == null ) throw new IllegalArgumentException("Installation Error: Could not find "+PGSQL_PROCEDURE_SQL);
		try {
			sqlCalls.load(is);
		} catch (IOException ex) {
			throw new IllegalArgumentException("Could not initialize plugin - could not read "+PGSQL_PROCEDURE_SQL+" - "+ex.toString());
		}
	}

	public String getPluginName() { return "Stored Procedure Browser"; }

	public String getPluginVersion() {
		return "Version 0.0";
	}

	public void freePlugin() {
	}

	public String getPluginShortName() {
		return "Procedure Browser";
	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getStoredProcIcon();
	}

	private void populateStoredProcedures(CategoryStoredProcedureNode storedProcCategory) {
		ItemStoredProcedureNode dbItem = null;
		DatabaseProcess db = storedProcCategory.getDatabaseProcess();
		String catalogName =  storedProcCategory.getCatalogName();
		String schemaName  = storedProcCategory.getSchemaName();
		if ( db.getHostConfiguration().getJdbc().getDriver().toLowerCase().indexOf("postgres")> -1) {
			populatePostgreSQLStoredProcedures(storedProcCategory, dbItem, db, catalogName, schemaName);
		} else {
			populateGenericStoredProcs(storedProcCategory, dbItem, db, catalogName, schemaName);
		}

	}

	/** @todo Hackish, but it should work. Use the properties file to retrieve the stored procedures list and start going through it. */
	private void populatePostgreSQLStoredProcedures(CategoryStoredProcedureNode storedProcCategory, ItemStoredProcedureNode dbItem, DatabaseProcess db, String catalogName, String schemaName) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if ( catalogName  != null ) db.changeCatalog(catalogName);
			conn = db.getConnection();
			String sqlStatement = sqlCalls.getProperty(SQL_RETRIEVE_PROCS_PROPERTY);
			preparedStatement = conn.prepareStatement(sqlStatement);
//			if ( preparedStatement.getParameterMetaData().getParameterCount() > 0 ) {
//				preparedStatement.setString(1, databaseProcess.getUserName());
//			}
			resultSet = preparedStatement.executeQuery();
			while ( resultSet.next() ) {
				String procName = resultSet.getString(1);
				int numArgs     = resultSet.getInt(2);
				ArrayList argumentTypes = new ArrayList();
				for ( int i=3; i<8; i++ ) {
					int thisType = resultSet.getInt(i);
					if ( thisType > 0 ) argumentTypes.add(new Integer(thisType));
				}
				ItemStoredProcedureNode storedProcedure = new ItemStoredProcedureNode(procName, argumentTypes, null, db);
				storedProcCategory.add(storedProcedure);
			}
//			resultSet.close();
//			preparedStatement.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			if ( resultSet!=null ) try { resultSet.close(); } catch ( Throwable th ) {}
			if ( preparedStatement!=null ) try { preparedStatement.close(); } catch ( Throwable th ) {}
			if ( conn!=null ) db.returnConnection(conn);
		}


	}

	private void populateGenericStoredProcs(CategoryStoredProcedureNode storedProcCategory, ItemStoredProcedureNode dbItem, DatabaseProcess db, String catalogName, String schemaName) {
		try {
				  Connection conn = db.getPool().getConnection();
				  try {
					  ResultSet rs = conn.getMetaData().getProcedures(catalogName, schemaName, null );
					  while(rs.next()) {
						  dbItem = new ItemStoredProcedureNode(rs.getString("PROCEDURE_NAME"),  db);
						  String remarks = "No remarks";
						  try {
							  remarks = rs.getString("REMARKS");
						  } catch ( Throwable th ) {}
						  dbItem.setRemarks(remarks);
						  storedProcCategory.add( dbItem );

					  }
				  } finally { db.getPool().releaseConnection(conn); }
			  } catch( SQLException sqle ) {
				  sqle.printStackTrace();
				  JOptionPane.showMessageDialog(null, sqle,
												"SQL Error when getting procedures",
												JOptionPane.ERROR_MESSAGE);
			  }
	}
	public Action[] getActionsFor(NodeIDEBase[] selectedNodes) {
		if ( selectedNodes!= null & selectedNodes.length == 1 ) {
			if ( selectedNodes[0] instanceof ItemStoredProcedureNode ) {
				Action[] action = new Action[1];
				action[0] = new ActionEditStoredProcedure((ItemStoredProcedureNode)selectedNodes[0]);
				return action;
			}
			if ( selectedNodes[0] instanceof CategoryStoredProcedureNode ) {
				Action[] action = new Action[1];
				action[0] = new ActionCreateStoredProcedure(selectedNodes[0].getDatabaseProcess());
				return action;
			}
		}
		return NULL_ACTIONS;
	}

	class ActionEditStoredProcedure extends AbstractAction {
		ItemStoredProcedureNode storedProcedure;
		public ActionEditStoredProcedure(ItemStoredProcedureNode storedProcedure) {
			super("Open "+storedProcedure.toString(), ProgramIcons.getInstance().findIcon("images/Open.gif"));
			this.storedProcedure = storedProcedure;
		}
		public void actionPerformed(ActionEvent evt) {
			StoredProcedureEditor editor = new StoredProcedureEditor();
			editor.setDatabaseProcess(storedProcedure.getDatabaseProcess());
			editor.setStoredProcedure(storedProcedure);
			editor.readStoredProcedureSource();
			editor.initPlugin();
			SqlIdeApplication.getInstance().setRightPanel(editor);
		}
	}

	class ActionCreateStoredProcedure extends AbstractAction {
		DatabaseProcess proc;
		public ActionCreateStoredProcedure(DatabaseProcess proc) {
			super("Create new Function", ProgramIcons.getInstance().findIcon("images/storedproc.gif"));
			this.proc = proc;
		}
		public void actionPerformed(ActionEvent evt) {
			/** @todo Implement */
		}
	}

}