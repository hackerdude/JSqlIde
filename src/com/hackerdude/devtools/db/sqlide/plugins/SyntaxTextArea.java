package com.hackerdude.devtools.db.sqlide.plugins;

import textarea.JEditTextArea;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import com.hackerdude.devtools.db.sqlide.pluginapi.*;
import java.awt.Dimension;
import textarea.TextAreaDefaults;

/**
 * Extension of JEditTextArea that supports dropping of Browser Node Items.
 */
public class SyntaxTextArea extends JEditTextArea implements DropTargetListener {

	DropTarget dropTarget = null;

	public SyntaxTextArea(TextAreaDefaults defaults) {
		super(defaults);
		dropTarget = new DropTarget(painter, this);
		setPreferredSize(new Dimension(300,100));
	}


  /**
   * a drop has occurred
   *
   */
  public void drop (DropTargetDropEvent event) {
	/** @todo Accept targets by inserting at cursor location instead of replacing the whole text. */
	String dropped = null;
	try {
		Transferable transferable = event.getTransferable();
		// we accept Strings and local objects
		if ( transferable.isDataFlavorSupported(NodeIDEItem.localDataFlavor()) ){
			event.acceptDrop(DnDConstants.ACTION_MOVE);
			NodeIDEItem item = (NodeIDEItem)transferable.getTransferData(NodeIDEItem.localDataFlavor());
			System.out.println("[SyntaxTextArea] Got Item "+item.toString());
			dropped = item.toString();
			event.getDropTargetContext().dropComplete(true);
			this.grabFocus();
		} else if ( transferable.isDataFlavorSupported(DataFlavor.stringFlavor) ){
			event.acceptDrop(DnDConstants.ACTION_MOVE);
			dropped = (String)transferable.getTransferData (DataFlavor.stringFlavor);
			System.out.println("[SyntaxTextArea] Got String "+dropped);
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
	if ( dropped != null ) setText(getText()+dropped);

  }


  /**
   * is invoked when you are exit the DropSite without dropping
   *
   */

  public void dragExit (DropTargetEvent event) {
//    System.out.println( "dragExit");

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
//    System.out.println( "dragOver");
  }

  /**
   * is invoked when you are dragging over the DropSite
   *
   */

  public void dragEnter (DropTargetDragEvent event) {

	// debug messages for diagnostics
//    System.out.println( "dragEnter");
	event.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
  }


}