package com.hackerdude.swing.picklist.search;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;


/**
 * Results panel. The results page.
 *
 */
public class ResultsPanel extends JPanel {
	BorderLayout borderLayout1 = new BorderLayout();
	JScrollPane resultScroll = new JScrollPane();
	JTable resultsTable = new JTable();
	JPanel resultHeaderPanel = new JPanel();
	JLabel searchResultsLabel = new JLabel();
	BorderLayout borderLayout2 = new BorderLayout();

	public ResultsPanel() {
		try {
			jbInit();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		searchResultsLabel.setText("Search Results:");
		resultHeaderPanel.setLayout(borderLayout2);
		this.add(resultScroll, BorderLayout.CENTER);
		this.add(resultHeaderPanel, BorderLayout.NORTH);
		resultHeaderPanel.add(searchResultsLabel, BorderLayout.CENTER);
		resultScroll.getViewport().add(resultsTable, null);
	}



	public void setTableModel(TableModel newTableModel) {
		resultsTable.setModel(newTableModel);
	}


}