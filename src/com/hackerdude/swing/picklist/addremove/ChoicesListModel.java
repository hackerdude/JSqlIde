package com.hackerdude.swing.picklist.addremove;

import java.util.Vector;

import javax.swing.AbstractListModel;

/**
 * This list model represents the picklist choices.
 * @author
 * @version 1.0
 * @copyright
 */
public class ChoicesListModel extends AbstractListModel {

	Vector items = new Vector();

	public ChoicesListModel(Vector items) {
		this.items.addAll(items);
	}


	public int getSize() {
		return items.size();
	}

	public Object getElementAt(int index) {
		Object item = items.elementAt(index);
		return item;
	}


	public void addElement(Object element) {
		items.add(element);
		fireIntervalAdded(this, 0,getSize());
	}

	public Object removeItemAt(int index) {
		Object result = items.remove(index);
		fireContentsChanged(this, index-1, index+1);
		return result;
	}

	public Vector removeAllItems() {
		Vector result = new Vector(items);
		items.clear();
		fireContentsChanged(this, 0,0);
		return result;
	}


	public void addAllItems(Vector items) {
		int sizeBefore = this.items.size();
		this.items.addAll(items);
		fireContentsChanged(this, sizeBefore,this.items.size());
	}

}
