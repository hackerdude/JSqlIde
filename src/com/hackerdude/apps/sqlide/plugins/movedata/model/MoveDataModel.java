package com.hackerdude.apps.sqlide.plugins.movedata.model;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

/**
 * MoveDataModel represents the script that is going to be executed.
 */
public class MoveDataModel extends AbstractListModel {

	ArrayList al = new ArrayList();  // The storage container for the instructions.

	public MoveDataModel() {
	}

	public void addInstruction(AbstractScriptNode instruction) {
		al.add(instruction);
		fireIntervalAdded(this, al.size()-1, al.size());
	}

	public void addInstruction(int index, MoveDataNode instruction) {
		al.add(index, instruction);
		fireIntervalAdded(this, index, index);
	}

	public void removeInstruction(MoveDataNode instruction) {
		int iRemoved = al.indexOf(instruction);
		al.remove(instruction);
		fireIntervalRemoved(this, iRemoved, iRemoved);
	}

	// Methods to satisfy abstracts in AbstractListModel

	public int getSize() { return al.size(); }

	public Object getElementAt(int index) { return al.get(index); }

	/**
	 * Returns an array with pointers to all the instructions at this
	 * point in time.
	 */
	public synchronized AbstractScriptNode[] getInstructions() {
		AbstractScriptNode[] result = new AbstractScriptNode[al.size()];
		result = (AbstractScriptNode[])al.toArray(result);
		return result;
	}


}
