package com.hackerdude.swing.picklist.addremove;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.event.*;

/**
 * This is a pick list that allows you to add or remove items from
 * a fixed set of items to a selected set.
 */
public class AddRemovePickList extends JPanel {

	public final Action ACTION_REMOVE_ALL = new RemoveAllAction();
	public final Action ACTION_ADD_ALL    = new AddAllAction();
	public final Action ACTION_REMOVE     = new RemoveAction();
	public final Action ACTION_ADD        = new AddAction();

	BorderLayout blMainBorderLayout = new BorderLayout();

	JPanel pnlTopPanel = new JPanel();
	JLabel lblInstructions = new JLabel();
    private JSplitPane mainSplitter = new JSplitPane();
    private JSplitPane leftSplitter = new JSplitPane();
    private ChoicesListBox availableChoices = new ChoicesListBox();
    private ChoicesListBox selectedChoices = new ChoicesListBox();
    private BorderLayout blAddBufferLayout = new BorderLayout();
    private JPanel pnlCenterButtons = new JPanel();
    private JPanel pnlRemoveBuffer = new JPanel();
    private JButton btnAdd = new JButton(ACTION_ADD);
    private JButton btnAddAll = new JButton(ACTION_ADD_ALL);
    private BorderLayout blRemoveBufferLayout = new BorderLayout();
    private JPanel pnlCenterPanel = new JPanel();
    private GridBagLayout gbCenterPanelLayout = new GridBagLayout();
    private JButton btnRemoveAll = new JButton(ACTION_REMOVE_ALL);
    private GridBagLayout gridBagLayout2 = new GridBagLayout();
    private JPanel pnlAddBuffer = new JPanel();
    private JButton btnRemove = new JButton(ACTION_REMOVE);

	public AddRemovePickList() {
		try {
			jbInit();
			availableChoices.setTitle("Available");
			selectedChoices.setTitle("Selected");
			selectedChoices.lstChoiceList.addListSelectionListener(new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent evt) {
					availableChoices.lstChoiceList.clearSelection();
					updateButtonState();
				}
			});
			availableChoices.lstChoiceList.addListSelectionListener(new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent evt) {
					selectedChoices.lstChoiceList.clearSelection();
					updateButtonState();
				}
			});
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setLayout(blMainBorderLayout);
		lblInstructions.setText("Add and remove from the selected list by using the buttons");
        leftSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
        pnlCenterButtons.setLayout(gbCenterPanelLayout);
        pnlRemoveBuffer.setLayout(blRemoveBufferLayout);
        btnAdd.setMnemonic('A');
        btnAdd.setText("< Add");
        btnAddAll.setMnemonic('L');
        btnAddAll.setText("Add All");
        pnlCenterPanel.setLayout(gridBagLayout2);
        btnRemoveAll.setMnemonic('M');
        btnRemoveAll.setText("Remove All");
        pnlAddBuffer.setLayout(blAddBufferLayout);
        btnRemove.setMnemonic('R');
        btnRemove.setText("> Remove");
        this.add(pnlTopPanel,  BorderLayout.NORTH);
		pnlTopPanel.add(lblInstructions, null);
        this.add(mainSplitter,  BorderLayout.CENTER);

		mainSplitter.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		leftSplitter.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitter.add(leftSplitter, JSplitPane.LEFT);
		mainSplitter.add(availableChoices, JSplitPane.RIGHT);
        leftSplitter.add(selectedChoices, JSplitPane.LEFT);
        leftSplitter.add(pnlCenterPanel, JSplitPane.RIGHT);
        pnlCenterPanel.add(pnlCenterButtons, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(20, 0, 20, 0), 0, 123));
        pnlCenterButtons.add(btnAdd, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnlCenterButtons.add(btnRemove, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnlCenterButtons.add(pnlRemoveBuffer, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
			,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 30));
        pnlRemoveBuffer.add(btnRemoveAll, BorderLayout.SOUTH);
        pnlCenterButtons.add(pnlAddBuffer, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 30));
        pnlAddBuffer.add(btnAddAll, BorderLayout.NORTH);
	}

	public void setCenterButtonsVerbose(boolean verbose) {
		if ( verbose ) {
			btnAdd.setMnemonic('A');
			btnAdd.setText("< Add");
			btnAddAll.setMnemonic('L');
			btnAddAll.setText("Add All");
			btnRemoveAll.setMnemonic('M');
			btnRemoveAll.setText("Remove All");
			btnRemove.setMnemonic('R');
			btnRemove.setText("> Remove");
		} else {
			btnAdd.setMnemonic('<');
			btnAdd.setText("<");
			btnAddAll.setMnemonic('L');
			btnAddAll.setText("<<");
			btnRemoveAll.setMnemonic('A');
			btnRemoveAll.setText(">>");
			btnRemove.setMnemonic('>');
			btnRemove.setText(">");
		}

	}

	public void updateButtonState() {
		int availableChoiceCount = availableChoices.getChoiceCount();
		int selectedChoiceCount = selectedChoices.getChoiceCount();
		int selectedAvailable   = availableChoices.lstChoiceList.getSelectedIndex();
		int selectedChoice      = selectedChoices.lstChoiceList.getSelectedIndex();

		btnAddAll.setEnabled(availableChoiceCount>0);
		btnAdd.setEnabled(btnAddAll.isEnabled() && selectedAvailable > -1);
		btnRemove.setEnabled(selectedChoiceCount>0 && selectedChoice>-1);
		btnRemoveAll.setEnabled(selectedChoiceCount>0);
	}

	public void setAvailableChoices(Vector availableChoicesVector) {
		availableChoices.loadWithChoiceCache(availableChoicesVector);
		updateButtonState();
	}

	public void setSelectedChoices(Vector selectedChoicesVector) {
		selectedChoices.loadWithChoiceCache(selectedChoicesVector);
		updateButtonState();

	}

	public Vector getSelectedChoices(){
		return selectedChoices.getModel().items;
	}

	class AddAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			if ( availableChoices.lstChoiceList.getSelectedIndex() == -1 ) return;

			Object selectedValues[] = availableChoices.lstChoiceList.getSelectedValues();
			int[] selectedIndices = availableChoices.lstChoiceList.getSelectedIndices();
			for ( int i=selectedValues.length-1; i>-1; i--) {
				Object thisValue = selectedValues[i];
				int    thisIndex = selectedIndices[i];
				availableChoices.getModel().removeItemAt(thisIndex);
				selectedChoices.getModel().addElement(thisValue);
			}
			updateButtonState();
		}
	}

	class RemoveAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			if ( selectedChoices.lstChoiceList.getSelectedIndex() == -1 ) return;

			Object[] selectedValues = selectedChoices.lstChoiceList.getSelectedValues();
			int[] selectedIndices  = selectedChoices.lstChoiceList.getSelectedIndices();
			for (int i=selectedValues.length-1;i>-1; i-- ) {
				Object thisValue = selectedValues[i];
				int    thisIndex = selectedIndices[i];
				selectedChoices.getModel().removeItemAt(thisIndex);
				availableChoices.getModel().addElement(thisValue);
			}
			updateButtonState();
		}

	}

	class AddAllAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			Vector allAvailableItems = availableChoices.getModel().removeAllItems();
			selectedChoices.getModel().addAllItems(allAvailableItems);
			updateButtonState();
		}

	}

	class RemoveAllAction extends AbstractAction {

		public RemoveAllAction() {
			super("Remove All");
		}

		public void actionPerformed(ActionEvent evt) {
			Vector allSelected = selectedChoices.getModel().removeAllItems();
			availableChoices.getModel().addAllItems(allSelected);
			updateButtonState();
		}
	}

	public void setSelectedTitle(String title) {
		selectedChoices.setTitle(title);
	}

	public void setAvailableTitle(String title) {
		availableChoices.setTitle(title);
	}

}