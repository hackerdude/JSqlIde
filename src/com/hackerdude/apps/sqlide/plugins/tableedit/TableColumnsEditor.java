package com.hackerdude.apps.sqlide.plugins.tableedit;

import javax.swing.JTable;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import com.hackerdude.apps.sqlide.plugins.browser.browsejdbc.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
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
				Enumeration enum = columns.children();
				while ( enum.hasMoreElements() ) {
					Object element = enum.nextElement();
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
