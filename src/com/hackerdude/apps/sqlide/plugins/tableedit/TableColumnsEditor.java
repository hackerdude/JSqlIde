package com.hackerdude.apps.sqlide.plugins.tableedit;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JTable;

import com.hackerdude.apps.sqlide.pluginapi.NodeIDEBase;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDECategory;
import com.hackerdude.apps.sqlide.pluginapi.NodeIDEItem;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.CategoryColumnsNode;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.ItemTableColumnNode;

/**
 * Table Column Editor.
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class TableColumnsEditor extends JTable implements DropTargetListener {

	FieldCollectionModel dbColumnsModel;

	public TableColumnsEditor(FieldCollectionModel model) {
		super(model);
		dbColumnsModel = model;
	}


  /**
   * a drop has occurred
   *
   */
  public void drop (DropTargetDropEvent event) {
	ArrayList alColumns = new ArrayList();
	try {
		Transferable transferable = event.getTransferable();
		// we accept NodeIDEItems
		if ( transferable.isDataFlavorSupported(NodeIDECategory.localDataFlavor()) ){
			event.acceptDrop(DnDConstants.ACTION_MOVE);
			NodeIDEBase item = (NodeIDEBase)transferable.getTransferData(NodeIDEItem.localDataFlavor());
			if ( item instanceof CategoryColumnsNode ) {
				CategoryColumnsNode columns = (CategoryColumnsNode)item;
				Enumeration columnsEnum = columns.children();
				while ( columnsEnum.hasMoreElements() ) {
					Object element = columnsEnum.nextElement();
					if ( element instanceof ItemTableColumnNode ) {
						ItemTableColumnNode node = (ItemTableColumnNode)element;
						alColumns.add(node);
					}
				}
			}
			else if (item instanceof ItemTableColumnNode ) {
				alColumns.add(item);
			}
			addToModel(alColumns);
			event.getDropTargetContext().dropComplete(true);
			this.grabFocus();
		}
		else{
			event.rejectDrop();
		}
	} catch (IOException exception) {
		exception.printStackTrace();
		System.err.println( "Exception" + exception.getMessage());
		event.rejectDrop();
	}
	catch (UnsupportedFlavorException ufException ) {
	  ufException.printStackTrace();
	  System.err.println( "Exception" + ufException.getMessage());
	  event.rejectDrop();
	}
	addToModel(alColumns);


  }


  public void addToModel(ArrayList al)  {
	Iterator it = al.iterator();
	while (it.hasNext()) {
		ItemTableColumnNode node = (ItemTableColumnNode)it.next();
		TableField newField = new TableField();
		newField.fieldName = node.getColumnName();
		newField.fieldType = new TableFieldType(node.getColumnType());
		newField.fieldLen  = new Integer(node.getColumnSize());
		dbColumnsModel.insertField(newField);
	}

  }

  /**
   * is invoked when you are exit the DropSite without dropping
   *
   */

  public void dragExit (DropTargetEvent event) {
	System.out.println( "dragExit");

  }

  /**
   * is invoked if the use modifies the current drop gesture
   *
   */
  public void dropActionChanged ( DropTargetDragEvent event ) {
  }

  /**
   * is invoked when a drag operation is going on
   *
   */

  public void dragOver (DropTargetDragEvent event) {
	System.out.println( "dragOver");
  }

  /**
   * is invoked when you are dragging over the DropSite
   *
   */

  public void dragEnter (DropTargetDragEvent event) {

	// debug messages for diagnostics
	System.out.println( "dragEnter");
	event.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
  }

}
