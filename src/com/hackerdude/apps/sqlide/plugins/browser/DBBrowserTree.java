package com.hackerdude.apps.sqlide.plugins.browser;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;

import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;

/**
 * The browser tree.
 * 
 * @author David Martinez
 * @version 1.0
 */
public class DBBrowserTree extends JTree implements DragSourceListener, DragGestureListener {

	DragSource dragSource = null;

	public DBBrowserTree(TreeNode root) {
		super(root);
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging
	 * has ended
	 *
	 */

	public void dragDropEnd (DragSourceDropEvent event) {
//    if ( event.getDropSuccess()){
//	System.out.println("DragDropEnd");
//    }
	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging
	 * has entered the DropSite
	 *
	 */

	public void dragEnter (DragSourceDragEvent event) {
//    System.out.println( " dragEnter");
	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging
	 * has exited the DropSite
	 *
	 */

	public void dragExit (DragSourceEvent event) {
//    System.out.println( "dragExit");

	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging is currently
	 * ocurring over the DropSite
	 *
	 */

	public void dragOver (DragSourceDragEvent event) {
//    System.out.println( "dragExit");

	}

	/**
	 * is invoked when the user changes the dropAction
	 *
	 */

	public void dropActionChanged ( DragSourceDragEvent event) {
//    System.out.println( "dropActionChanged");
	}

	/**
	 * a drag gesture has been initiated
	 *
	 */

	public void dragGestureRecognized( DragGestureEvent event) {

		if ( getSelectionPath() != null ) {
			Object selected = getSelectionPath().getLastPathComponent();
			if ( selected != null && selected instanceof NodeIDEBase ){
				NodeIDEBase transferableNode = (NodeIDEBase)selected;
				dragSource.startDrag(event, DragSource.DefaultMoveDrop, transferableNode, this);
			}
		}
	}



}