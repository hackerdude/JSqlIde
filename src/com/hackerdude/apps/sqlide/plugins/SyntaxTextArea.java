package com.hackerdude.apps.sqlide.plugins;

//import textarea.JEditTextArea;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import com.hackerdude.apps.sqlide.pluginapi.*;
import java.awt.Dimension;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import com.hackerdude.apps.sqlide.*;
//import textarea.TextAreaDefaults;
//import textarea.syntax.SyntaxDocument;

/**
 * Extension of JEditTextArea that supports dropping of Browser Node Items.
 *
 * @copyright (C) 1998-2002 Hackerdude (David Martinez). All Rights Reserved.
 * @author David Martinez
 * @version 1.0
 */
public class SyntaxTextArea extends JPanel implements DropTargetListener {

	JTextArea textArea = new JTextArea();
	DropTarget dropTarget = null;
	Properties configuration = new Properties();
	BorderLayout layout = new BorderLayout();
	JScrollPane scroller = new JScrollPane(textArea);

	public SyntaxTextArea() {
		super();
		jbInit();
	}

	public void jbInit() {
		this.setLayout(layout);
		this.add(scroller, BorderLayout.CENTER);
	}

	public String getText() {
		return textArea.getText();
	}

	public void cut() {
		textArea.cut();
	}

	public void copy() {
		textArea.copy();
	}

	public void paste() {
		textArea.paste();
	}

	public SyntaxTextArea(Properties configuration) {//TextAreaDefaults defaults) {
		super();
		this.configuration = configuration;
		dropTarget = new DropTarget(this, this);
		jbInit();
//		setPreferredSize(new Dimension(300,100));
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
		if ( dropped != null ) setText(textArea.getText()+dropped);

	}


	public void fireConfigurationChanged() {
		Font theFont = ProgramConfig.getFont(configuration, ProgramConfig.PROP_FONT_FOR_EDITOR);
		textArea.setFont(theFont);
	}


	public void setText(String text) {
		textArea.setText(text);
	}

	/**
	 * is invoked when you are exit the DropSite without dropping
	 */

	public void dragExit (DropTargetEvent event) {
//    System.out.println( "dragExit");
	}

	public void setEditable(boolean isEditable) {
		textArea.setEditable(isEditable);
	}

	/**
	 * is invoked if the use modifies the current drop gesture
	 */
	public void dropActionChanged ( DropTargetDragEvent event ) {
	}

	/**
	 * is invoked when a drag operation is going on
	 */
	public void dragOver (DropTargetDragEvent event) {
//    System.out.println( "dragOver");
	}

	/**
	 * is invoked when you are dragging over the DropSite
	 */
	public void dragEnter (DropTargetDragEvent event) {
		event.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
	}


}