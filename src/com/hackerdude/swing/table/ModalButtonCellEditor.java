package com.hackerdude.swing.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * This is a cell editor that you provide a JButton to. It executes the click
 * event when the button is pressed, at which point you are expected to show
 * a modal dialog to show your own "drill-down capability". Extend from this and
 * override the setCellEditorValue() in order to show your dialog box (I think) :-)
 */
public class ModalButtonCellEditor extends AbstractCellEditor implements TableCellEditor {

	Object currentValue;
	JLabel editorLabel;
	JTable ownerTable;
	int currentRow;
	int currentColumn;
	JLabel cellEditorLabel = new JLabel();
	JComponent cellEditorComponent;
	JPanel editorPanel;
	JButton cellEditorButton;

	/**
	 * This is the constructor for the cell editor.
	 * @param actionForButton The modal action you want the system to execute when they click the button.
	 */
	public ModalButtonCellEditor( Action actionForButton ) {
		cellEditorButton = new JButton(actionForButton);
		cellEditorComponent = createEditorComponent();
	}


	public Object getCellEditorValue() {
		return currentValue;
	}

	public void setCellEditorValue(Object obj) {
		currentValue = obj;
	}

	/**
	 * Starts editing of the Choice group reference Cell.
	 *
	 * <P>This editing operation shows our custom editor component which
	 * contains a label and a button. Pressing the button will show a
	 * selection box with all the available choice groups so that the
	 * user can modify the selected choice group.
	 *
	 * @param owner The owner table.
	 * @param value The current value.
	 * @param isSelected True if the cell is selected.
	 * @param row The table row being edited
	 * @param column The table column being edited.
	 * @return The properly initialized custom editor component.
	 */
	public Component getTableCellEditorComponent(JTable owner, Object value, boolean isSelected, int row, int column) {
		currentValue = value;
		ownerTable = owner;
		currentRow = row;
		currentColumn = column;
		cellEditorLabel.setText(currentValue.toString());
		return cellEditorComponent;
	}

	/**
	 * This method actually creates the component.
	 * <P>The component is a panel with a JLabel and a button.
	 * @return a new Instance of the editor panel.
	 */
	private JComponent createEditorComponent() {
		editorPanel = new JPanel();
		cellEditorLabel.setForeground(SystemColor.menuText);
		editorPanel.setLayout(new BorderLayout());
		editorPanel.add(cellEditorLabel, BorderLayout.CENTER);
		editorPanel.add(cellEditorButton, BorderLayout.EAST);
		editorPanel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if ( evt.getKeyChar() == ' ' ) {
					cellEditorButton.doClick();
				}
			}
		});
		cellEditorButton.setToolTipText("Select...");
		return editorPanel;
	}


}
