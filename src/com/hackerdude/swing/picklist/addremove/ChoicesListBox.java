package com.hackerdude.swing.picklist.addremove;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * A Box with a list of choices.
 */
public class ChoicesListBox extends JPanel {
	BorderLayout blChoicesListbox = new BorderLayout();
	JScrollPane spScroller = new JScrollPane();
	JList lstChoiceList = new JList();
	JPanel pnlTopPanel = new JPanel();
	JLabel lblTitle = new JLabel();
	ChoicesListModel listModel = new ChoicesListModel(new Vector());
    private BorderLayout blTitleBorderLayout = new BorderLayout();

	public ChoicesListBox() {
		try {
			jbInit();
			lstChoiceList.setModel(listModel);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public int getChoiceCount() {
		return lstChoiceList.getModel().getSize();
	}

	void jbInit() throws Exception {
		this.setLayout(blChoicesListbox);
		lblTitle.setToolTipText("");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTitle.setText("Choices");
		pnlTopPanel.setLayout(blTitleBorderLayout);
        this.add(spScroller, BorderLayout.CENTER);
		this.add(pnlTopPanel, BorderLayout.NORTH);
		pnlTopPanel.add(lblTitle, BorderLayout.CENTER);
		spScroller.getViewport().add(lstChoiceList, null);
	}


	public void loadWithChoiceCache(Vector choices) {
		listModel = new ChoicesListModel(choices);
		lstChoiceList.setModel(listModel);
	}

	public ChoicesListModel getModel() { return listModel; }


	public void setTitle(String title) {
		lblTitle.setText(title);
	}


}
