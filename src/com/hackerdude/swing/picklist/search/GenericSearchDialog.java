package com.hackerdude.swing.picklist.search;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import com.hackerdude.swing.SwingUtils;

/**
 * This is a generic Search Dialog. See the generic search panel for details.
 * @author David Martinez
 * @version 1.0
 */
public class GenericSearchDialog extends JDialog {

	GenericSearchPanel searchPanel = new GenericSearchPanel();
	SearchInterface    searchInterface;
	SearchCriteria[]   availableCriteria;
	public Action FIND_ACTION = new FindAction();
	public Action OK_ACTION   = new OKAction();
	public Action CANCEL_ACTION = new CancelAction();

	Action[] itemLevelActions = null;
	Action[] groupLevelActions = null;

	int modalResult;

	public static final int MODAL_RESULT_OK     = 1;
	public static final int MODAL_RESULT_CANCEL = 0;

	public GenericSearchDialog() {
		super();
		initSearchDialog();
	}

	public GenericSearchDialog(Frame owner) {
		super(owner);
		initSearchDialog();
	}

	public GenericSearchDialog(Frame owner, boolean modal) {
		super(owner, modal);
		initSearchDialog();
	}

	public GenericSearchDialog(Frame owner, String title) {
		super(owner, title);
		initSearchDialog();
	}

	public GenericSearchDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		initSearchDialog();
	}

	private void initSearchDialog() {
		getContentPane().add(searchPanel);
		searchPanel.criteriaPanel.fldSearchField.grabFocus();
		getRootPane().setDefaultButton(searchPanel.criteriaPanel.btnFind);
		getRootPane().registerKeyboardAction(CANCEL_ACTION, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		pack();
		Point newPoint = SwingUtils.getCenteredWindowPoint(this);
		setLocation(newPoint);
		searchPanel.criteriaPanel.btnFind.setAction(FIND_ACTION);
		searchPanel.btnCancel.setAction(CANCEL_ACTION);
		searchPanel.btnOk.setAction(OK_ACTION);
		setItemLevelActionsEnabled(false);
		getResultsTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	}

	public int getSelectionMode() {
		return searchPanel.resultsPanel.resultsTable.getSelectionModel().getSelectionMode();
	}

	public void setSelectionMode(int newSelectionMode) {
		searchPanel.resultsPanel.resultsTable.setSelectionMode(newSelectionMode);
	}

	public void setSearchInterface(SearchInterface searchInterface) {
		this.searchInterface = searchInterface;
	}

	public void setTableModel(TableModel model) {
		searchPanel.resultsPanel.setTableModel(model);
		searchPanel.resultsPanel.resultsTable.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				System.out.println(event.getPropertyName());
			}
		});
	}

	/**
	 * This button sets the item-level actions to place as buttons on the
	 * bottom of the form.
	 * <P>Item level actions will only be active when there is a selected
	 * result on the search field.
	 *
	 * @param itemLevelActions Extra actions you want to include.
	 */
	public void setItemLevelActions(Action[] itemLevelActions) {
		this.itemLevelActions = itemLevelActions;
		searchPanel.setItemLevelActions(itemLevelActions);
		setItemLevelActionsEnabled(false);
	}

	public void setGroupLevelActions(Action[] groupActions) {
		this.groupLevelActions = groupActions;
		searchPanel.setGroupLevelActions(groupActions);
	}

	public void setAvailableCriteria(SearchCriteria[] criteria) {
		availableCriteria = criteria;
		searchPanel.criteriaPanel.cbCriteria.setModel(new DefaultComboBoxModel(criteria));
	}

	public void setCurrentAvailableCriteria(SearchCriteria currentCriteria) {
		searchPanel.criteriaPanel.cbCriteria.setSelectedItem(currentCriteria);
	}

	class OKAction extends AbstractAction {

		public OKAction() {
			super("OK");
		}

		public void actionPerformed(ActionEvent ev) {
			GenericSearchDialog.this.modalResult = MODAL_RESULT_OK;
			GenericSearchDialog.this.hide();
		}

	}

	class CancelAction extends AbstractAction {

		public CancelAction() {
			super("Cancel");
		}

		public void actionPerformed(ActionEvent ev) {
			GenericSearchDialog.this.modalResult = MODAL_RESULT_CANCEL;
			GenericSearchDialog.this.hide();
		}

	}

	/**
	 * Shows the search dialog as a modal box.
	 * @return The modal result (either MODAL_RESULT_OK or MODAL_RESULT_CANCEL);
	 */
	public int showModalSearch() {
		modalResult = MODAL_RESULT_CANCEL;
		setModal(true);
		searchPanel.criteriaPanel.fldSearchField.grabFocus();
		show();
		return modalResult;
	}

	class FindAction extends AbstractAction {

		public FindAction() {
			super("Find");
		}

		public void actionPerformed(ActionEvent ev) {
			if ( searchInterface == null )  {
				JOptionPane.showMessageDialog(GenericSearchDialog.this, "Search Interface not specified", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if ( ! searchInterface.okToBeginSearch() ) {
				JOptionPane.showMessageDialog(GenericSearchDialog.this, "Cannot begin a search.", "Cannot begin search",JOptionPane.ERROR_MESSAGE);
				return;
			}

			if ( searchPanel.criteriaPanel.cbCriteria.getSelectedIndex() > -1 &&
				( searchPanel.criteriaPanel.fldSearchField.getText() ==null  || ! searchPanel.criteriaPanel.fldSearchField.getText().equals("") ) ) {
				searchPanel.lblResultsMessage.setText("Searching...");

				SearchCriteria criteria = searchPanel.criteriaPanel.getSelectedCriteria();
				searchPanel.criteriaPanel.affectCriteriaWithTextField(criteria);
				setSearchEnabled(false);
				try {
					final SearchCriteria criteriaClone = (SearchCriteria)criteria.clone();
					Thread myThread = new Thread() {
						public void run() {

							try {
								searchInterface.beginSearch(criteriaClone);
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										int rowCount = searchPanel.resultsPanel.resultsTable.getRowCount();
										boolean enableItemLevelActions = rowCount > 0;
										setItemLevelActionsEnabled(enableItemLevelActions);
										if ( enableItemLevelActions ) searchPanel.resultsPanel.resultsTable.getSelectionModel().setSelectionInterval(0,0);
										setSearchEnabled(true);
										searchPanel.lblResultsMessage.setText("Search for "+criteriaClone.getWhatToSearchFor()+" finished.");
										/** @todo Do handling of jTable events for selection-time */
									}
								});
							} catch (Throwable ex) {

							}

						}

					};
					myThread.setName("Generic Search Thread");
					myThread.start();
				} catch (CloneNotSupportedException ex) {
				}
			} else {
				JOptionPane.showMessageDialog(GenericSearchDialog.this, "You didn't enter a search criteria.", "Nothing to Do!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}


	public JTable getResultsTable() {
		return searchPanel.resultsPanel.resultsTable;
	}

	public void setItemLevelActionsEnabled(boolean enabled) {
		// OK is also an item level action
		OK_ACTION.setEnabled(enabled);
		if ( itemLevelActions != null ) {
			for ( int i=0; i<itemLevelActions.length; i++ ) {
				itemLevelActions[i].setEnabled(enabled);
			}
		}

	}

	public void setSearchEnabled(boolean enabled) {
		FIND_ACTION.setEnabled(enabled);
		searchPanel.criteriaPanel.fldSearchField.setEnabled(enabled);
		searchPanel.criteriaPanel.cbCriteria.setEnabled(enabled);
		if ( groupLevelActions!= null ) {
			for ( int i=0; i<groupLevelActions.length; i++ ) {
				groupLevelActions[i].setEnabled(enabled);
			}
		}
	}

}