package com.hackerdude.swing.picklist.search;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import com.hackerdude.swing.widgets.FloatValueDocument;
import com.hackerdude.swing.widgets.IntegerValueDocument;

/**
 * The Search Criteria section of the generic search panel.
 *
 */
public class CriteriaPanel extends JPanel {

	JLabel lblSearchBy = new JLabel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel pnlSearchCriteria = new JPanel();
	JTextField fldSearchField = new JTextField();
	JPanel pnlFieldCriteria = new JPanel();
	public JButton btnFind = new JButton();
	JComboBox cbCriteria = new JComboBox();
	BorderLayout borderLayout3 = new BorderLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	JLabel lblByField = new JLabel();
	JPanel pnlSearchTerms = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JLabel lblSearchValue = new JLabel();

	SearchCriteria lastSelectedItem;

	ActionListener CRITERIA_CHANGE_HANDLER = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if  (lastSelectedItem != null ) {
				affectCriteriaWithTextField(lastSelectedItem);
			}
			if ( evt.getActionCommand().equals("comboBoxChanged") ) {
				SearchCriteria selectedCriteria = getSelectedCriteria();
				if ( selectedCriteria != null ) {
					Class dataType = selectedCriteria.getDataType();
					Object currentInput = selectedCriteria.getWhatToSearchFor();
					if ( dataType == Integer.class ) {
						fldSearchField.setDocument(new IntegerValueDocument());
					}
					else if ( dataType == Float.class  ) {
						fldSearchField.setDocument(new FloatValueDocument());
					} else {
						fldSearchField.setDocument(new PlainDocument());
					}
					if ( currentInput != null ) fldSearchField.setText(currentInput.toString());
					lastSelectedItem = selectedCriteria;
				}
			}
		}
	};

	public SearchCriteria getSelectedCriteria() {
		Object selectedItem = cbCriteria.getSelectedItem();
		SearchCriteria selectedCriteria = (SearchCriteria)selectedItem;
		return selectedCriteria;
	}

	public CriteriaPanel() {
		try {
			jbInit();
			cbCriteria.addActionListener(CRITERIA_CHANGE_HANDLER);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		lblSearchBy.setText("Enter the search Criteria");
		pnlSearchCriteria.setLayout(gridBagLayout2);
		pnlFieldCriteria.setLayout(borderLayout3);
		btnFind.setMnemonic('F');
		btnFind.setText("Find");
		lblByField.setDisplayedMnemonic('O');
		lblByField.setLabelFor(cbCriteria);
		lblByField.setText("Search On: ");
		pnlSearchTerms.setLayout(borderLayout2);
		lblSearchValue.setDisplayedMnemonic('S');
		lblSearchValue.setLabelFor(fldSearchField);
		lblSearchValue.setText("Search For: ");
        this.add(pnlSearchCriteria, BorderLayout.CENTER);
		pnlFieldCriteria.add(lblByField, BorderLayout.WEST);
		pnlFieldCriteria.add(cbCriteria, BorderLayout.CENTER);
		pnlSearchCriteria.add(pnlSearchTerms,              new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 250, 0));
		pnlSearchTerms.add(fldSearchField, BorderLayout.CENTER);
		pnlSearchTerms.add(lblSearchValue, BorderLayout.WEST);
		pnlSearchTerms.add(btnFind, BorderLayout.EAST);
		this.add(lblSearchBy, BorderLayout.NORTH);
		pnlSearchCriteria.add(pnlFieldCriteria,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
			,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void affectCriteriaWithTextField(SearchCriteria criteria) {
		String textTypedIn = fldSearchField.getText();
		Class dataType = criteria.getDataType();
		if ( dataType == Integer.class ) {
			if ( textTypedIn.equals("") ) textTypedIn = null;
			if ( textTypedIn != null ) criteria.whatToSearchFor = new Integer(textTypedIn);
			else criteria.whatToSearchFor = null;
		}
		else if ( dataType == Float.class  ) {
			if ( textTypedIn.equals("") ) textTypedIn = null;
			if ( textTypedIn != null ) criteria.whatToSearchFor = new Float(textTypedIn);
			else criteria.whatToSearchFor = null;
		}
		else criteria.whatToSearchFor = textTypedIn;
	}

	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		fldSearchField.setEnabled(isEnabled);
		cbCriteria.setEnabled(isEnabled);
		btnFind.setEnabled(isEnabled);
	}



}