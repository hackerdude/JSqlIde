package com.hackerdude.apps.sqlide.plugins.movedata;

import com.hackerdude.apps.sqlide.dataaccess.*;
import com.hackerdude.apps.sqlide.pluginapi.*;
import javax.swing.*;
import java.awt.*;
import java.beans.*;
import javax.swing.event.*;
import com.hackerdude.apps.sqlide.nodes.*;
import com.hackerdude.apps.sqlide.ProgramIcons;
import java.awt.event.ActionEvent;

/**
 * Data Mover plugin. The Data Mover can create a script
 * to move data from one place to another.
 * <P>A DataMover interpreter back-end actually uses the
 * SQLIDE connection pools to execute the script generated
 * by this UI.
 */
public class DataMoverPanel extends AbstractVisualPlugin implements IDENodeContextPluginIF {

	DatabaseProcess proc;
	BorderLayout borderLayout4 = new BorderLayout();
	JTabbedPane dataMoverPages = new JTabbedPane();
	JPanel pnlVisual = new JPanel();
	JPanel sourcePane = new JPanel();
	VisualInstructionPanel visualInstructions = new VisualInstructionPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	InstructionSourceEditor instructionSourceEditor = new InstructionSourceEditor();
	JPanel jPanel1 = new JPanel();
	JButton btnExecute = new JButton();


	public DataMoverPanel() {
		jbInit();
	}

	public void jbInit() {
		this.setLayout(borderLayout4);
		pnlVisual.setLayout(borderLayout1);
		sourcePane.setLayout(borderLayout2);
		btnExecute.setText("Execute!");
		this.add(dataMoverPages, BorderLayout.CENTER);
		dataMoverPages.add(pnlVisual, "Visual");
		pnlVisual.add(visualInstructions, BorderLayout.CENTER);
		dataMoverPages.add(sourcePane, "Source");
		sourcePane.add(instructionSourceEditor, BorderLayout.CENTER);
		this.add(jPanel1, BorderLayout.SOUTH);
		jPanel1.add(btnExecute, null);
		refreshPanel();
	}

	public void setDatabaseProcess(DatabaseProcess proc) { this.proc = proc; }

	public DatabaseProcess getDatabaseProcess() { return proc; }

	public String getPluginName() { return "DataMover"; }

	public String getPluginShortName() { return "DataMover"; }

	public String getPluginVersion() { return "0.0.1"; }

	public JMenu createPanelMenu(JMenu parent) { return null; }

	public void setVisibleMenus(boolean value, JMenu menu) { }

	public boolean isActionPossible(String action) {
		boolean theResult = false;
		if ( action.equals("Cut") ) {  };
		if ( action.equals("Copy") ) {  };
		if ( action.equals("Paste") ) {  };
		return(theResult);
	}

	public boolean executeStandardAction(ActionEvent evt) {
		boolean theResult = false;
//		if ( action.equals("Cut") ) {  };
//		if ( action.equals("Copy") ) {  };
//		if ( action.equals("Paste") ) {  };
		return(theResult);
	}

	public void showAboutBox() {
		javax.swing.JOptionPane.showMessageDialog(this, "Data Mover Panel\nVersion 0.0", "About Data Mover",JOptionPane.INFORMATION_MESSAGE);
	}

	public void refreshPanel() {
		/**@todo: Implement this com.hackerdude.apps.sqlide.intf.IDEPanelInterface method*/
//		DataMoveParser parser = new DataMoveParser(visualInstructions.instructionModel);
//		System.out.println("[DataMoverPanel] TODO: Add toXML here");
//		instructionSourceEditor.editor.setText(parser.toXML());

	}

	void dataMoverPages_stateChanged(ChangeEvent e) {
		System.out.println("[DataMoverPanel] State Changed");
		Component comp = dataMoverPages.getSelectedComponent();
		if ( comp instanceof InstructionSourceEditor ) {
		} else {
			DataMoveParser parser = new DataMoveParser(visualInstructions.instructionModel);
			System.out.println("[DataMoverPanel] TODO: Add toXML here");
			instructionSourceEditor.editor.setText(parser.toXML());
		}

	}
	public JPopupMenu getPopupMenuFor(NodeIDEBase node) { return null; }

	public Action[] getActionsFor(NodeIDEBase[] node) {
		return NULL_ACTIONS;
	}

	public Action[] getPossibleActions() {
	  return null;
	}

	public void receivePluginFocus() {
		dataMoverPages.requestFocus();
	}

	public Icon getPluginIcon() {
		return ProgramIcons.getInstance().getDatabaseIcon();

	}

	public void freePlugin() {}

	public void doCut() {

	}

	public void doCopy() {

	}

	public void doPaste() {

	}

	public void doSaveFileAs() {

	}

	public void doSaveFile() {

	}

	public void doOpenFile() {

	}

}
