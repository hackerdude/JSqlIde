package com.hackerdude.swing.picklist.search;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is a Generic Search Panel. It shows a generic, event-aware search
 * dialog based on a series of search criteria. It does the search, displays
 * the data using the supplied tablemodel and it allows the user to select
 * an item from the table. It returns the index of the item that has been
 * selected.
 */
public class GenericSearchPanel extends JPanel {

	BorderLayout borderLayout1 = new BorderLayout();
	JPanel pnlBottomPanel = new JPanel();
	public CriteriaPanel criteriaPanel = new CriteriaPanel();
	ResultsPanel resultsPanel = new ResultsPanel();
	JLabel lblResultsMessage = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();

	public GenericSearchPanel() {
		try {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		lblResultsMessage.setText("Empty Results.");
		lblResultsMessage.setForeground(btnOk.getForeground());
		pnlBottomPanel.setLayout(borderLayout2);
		pnlOkCancel.setLayout(flowLayout1);
        pnlCustomActions.setLayout(borderLayout3);
		pnlOkCancel.add(btnOk, null);
        pnlOkCancel.add(btnCancel, null);
        this.add(criteriaPanel, BorderLayout.NORTH);
		this.add(resultsPanel, BorderLayout.CENTER);
		this.add(pnlBottomPanel, BorderLayout.SOUTH);
		pnlBottomPanel.add(lblResultsMessage,  BorderLayout.WEST);
		pnlBottomPanel.add(pnlOkCancel, BorderLayout.EAST);
        pnlBottomPanel.add(pnlCustomActions, BorderLayout.CENTER);
        pnlCustomActions.add(pnlGroupActions, BorderLayout.CENTER);
        pnlCustomActions.add(pnlItemLevelActions,  BorderLayout.WEST);
	}

	JPanel pnlOkCancel = new JPanel();
	JButton btnCancel = new JButton();
	FlowLayout flowLayout1 = new FlowLayout();
    JButton btnOk = new JButton();
    private JPanel pnlCustomActions = new JPanel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JPanel pnlGroupActions = new JPanel();
    private JPanel pnlItemLevelActions = new JPanel();


	public void setItemLevelActions(Action[] itemLevelActions) {
		pnlItemLevelActions.removeAll();
		for ( int i=0; i<itemLevelActions.length; i++ ) pnlItemLevelActions.add(new JButton(itemLevelActions[i]));
	}

	public void setGroupLevelActions(Action[] groupLevelActions) {
		pnlGroupActions.removeAll();
		for ( int i=0; i<groupLevelActions.length; i++ ) pnlGroupActions.add(new JButton(groupLevelActions[i]));

	}

}