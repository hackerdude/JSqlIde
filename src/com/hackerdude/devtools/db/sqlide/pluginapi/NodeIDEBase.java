
/**
 * Title:        JSqlIde<p>
 * Description:  A Java SQL Integrated Development Environment
 * <p>
 * Copyright:    Copyright (c) David Martinez<p>
 * Company:      <p>
 * @author David Martinez
 * @version 1.0
 */
package com.hackerdude.devtools.db.sqlide.pluginapi;
import com.hackerdude.devtools.db.sqlide.dataaccess.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;

/**
 * SQLIDE Base Node. All Nodes inherit from this base node.
 */
public abstract class NodeIDEBase extends DefaultMutableTreeNode implements Transferable  {

	 protected DatabaseProcess db;
	 protected String itemName;

	 protected static DataFlavor localObjectFlavor;
	 protected DataFlavor stringDataFlavor = DataFlavor.stringFlavor;

	 public NodeIDEBase( String name, DatabaseProcess db ) {
	   super(name);
	   try {
			localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
	   } catch (ClassNotFoundException exc ) { exc.printStackTrace(); }
	   itemName = name;
	   setDatabaseProcess(db);
	   // If this node might have children, add a dummy so the selection listener can call reacChildren on us and read on expansion.
	   if ( canHaveChildren() ) add(new DefaultMutableTreeNode("Dummy"));
	 };

	 public void setDatabaseProcess(DatabaseProcess process) {
	   db = process;
	 };

	 public DatabaseProcess getDatabaseProcess(){
	 return (db);
	 };

	 public abstract String getInfo();

	 public abstract void readChildren();

	 public abstract boolean canHaveChildren();

	public void removeChildren() {
		if ( canHaveChildren() ) {
			removeAllChildren();
			add(new DefaultMutableTreeNode("Dummy"));
		}
	}
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] result = new DataFlavor[2];
		result[0] = localObjectFlavor;
		result[1] = stringDataFlavor;
		return result;

	}
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(localObjectFlavor) || flavor.equals(stringDataFlavor);

	}
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return this;
	}


	public static DataFlavor localDataFlavor() { return localObjectFlavor; }


}