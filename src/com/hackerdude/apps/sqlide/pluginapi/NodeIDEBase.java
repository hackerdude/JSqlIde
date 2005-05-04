package com.hackerdude.apps.sqlide.pluginapi;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.hackerdude.apps.sqlide.dataaccess.DatabaseProcess;

/**
 * SQLIDE Base Node. All tree Nodes inherit from this base node.
 * 
 * @author David Martinez <a
 *         href="mailto:david@hackerdude.com">david@hackerdude.com</a>
 */
public abstract class NodeIDEBase extends DefaultMutableTreeNode implements
		Transferable {

	/**
	 * The database process for this node.
	 */
	protected DatabaseProcess databaseProcess;

	/**
	 * The name of this item.
	 */
	protected String itemName;

	/**
	 * The data flavor for transfering local objects through drag and drop
	 */
	protected static DataFlavor localObjectFlavor;

	/**
	 * The data flavor for transferring strings through drag and drop.
	 */
	protected DataFlavor stringDataFlavor = DataFlavor.stringFlavor;

	/**
	 * Creates a new IDE base node.
	 * 
	 * @param name The string name of the node
	 * @param databaseProcess The database process to use
	 */
	public NodeIDEBase(String name, DatabaseProcess databaseProcess) {
		super(name);
		try {
			localObjectFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
		} catch (ClassNotFoundException exc) {
			exc.printStackTrace();
		}
		itemName = name;
		setDatabaseProcess(databaseProcess);
		// If this type of node *might* have children, add a dummy so the selection listener
		// can call reacChildren on us and read on expansion.
		if (canHaveChildren()) add(new DefaultMutableTreeNode("Dummy"));
	}

	/**
	 * Sets the current database process for this node.
	 * @param process
	 */
	private void setDatabaseProcess(DatabaseProcess process) {
		databaseProcess = process;
	}

	/**
	 * Accesor for the database process.
	 * @return The database process currently associated with this node.
	 */
	public DatabaseProcess getDatabaseProcess() {
		return (databaseProcess);
	}

	/**
	 * Implementers of this method shall return a short 
	 * information string for this node.
	 * 
	 * @return A short information string for this node.
	 */
	public abstract String getInfo();

	/**
	 * Implementers of this method shall
	 * read the children of this node.
	 */
	public abstract void readChildren();

	/**
	 * Implementers return true if the concrete 
	 * type of node may have children, or false 
	 * otherwise.
	 * 
	 * @return True if this type of node may have children, false otherwise.
	 */
	public abstract boolean canHaveChildren();

	/**
	 * Removes all the children of this node. Ocurrs 
	 * when collapsing the nodes.
	 */
	public void removeChildren() {
		if (canHaveChildren()) {
			removeAllChildren();
			add(new DefaultMutableTreeNode("Dummy"));
		}
	}

	/**
	 * Returns the data flavors for drag-and-drop.
	 *
	 * @return The data flavors 
	 */
	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] result = new DataFlavor[2];
		result[0] = localObjectFlavor;
		result[1] = stringDataFlavor;
		return result;

	}

	/**
	 * Returns true if the data flavor supplied is compatible with the supported data flavors.
	 * 
	 * @return true if the data flavor was supported, false otherwise.
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(localObjectFlavor) || flavor.equals(stringDataFlavor);

	}

	/**
	 * Returns the transfer data for the data flavor.
	 * 
	 * @param The transfer data for the object.
	 * @return The object for the transfer data.
	 */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return this;
	}

	public static DataFlavor localDataFlavor() {
		return localObjectFlavor;
	}

	/**
	 * Returns the icon for this node.
	 * 
	 * @return The image for this node.
	 */
	public ImageIcon getIcon() {
		return null;
	}

}
