package com.hackerdude.apps.sqlide.dialogs;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.*;

public class ClobEditorPanel  extends JPanel {

	private Clob clob;

    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel lblFieldName = new JLabel();
    private JScrollPane spScroller = new JScrollPane();
    private JEditorPane edtEditor = new JEditorPane();

    public ClobEditorPanel() {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        lblFieldName.setText("Field: ");
        this.setLayout(borderLayout1);
        edtEditor.setText("jEditorPane1");
        this.add(lblFieldName, BorderLayout.NORTH);
        this.add(spScroller, BorderLayout.CENTER);
        spScroller.getViewport().add(edtEditor, null);
    }

	public void setClob(Clob clob) {
		this.clob = clob;
		final Clob ourClob = clob;
		edtEditor.setText("Reading CLOB. Please wait...");
		Thread clobReader = new Thread() {
			public void run() {
				String result = "";
				try {
					result = readClob(ourClob);
				}
				catch (Exception ex) {
					result = ex.toString();
				}
				finally {
					final String text = result;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							edtEditor.setText(text);
						}
					});
				}

			}
		};
		clobReader.start();

	}

	public static String readClob(Clob clob) throws SQLException, IOException {

		BufferedReader reader = new BufferedReader(clob.getCharacterStream());
		String line = null;
		StringBuffer buffer = new StringBuffer( (int)(clob.length()*1.5) );
		while ( ( line = reader.readLine()  )!=null ) {


		}
		return buffer.toString();

	}

}

