package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.hackerdude.apps.sqlide.ProgramConfig;

/**
 * A panel with a sql CLOB editor.
 */
public class ClobEditorPanel  extends JPanel {

	private Clob clob;
	private Blob blob;

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

	public void setFieldName(String fieldName) {
		lblFieldName.setText("Field: "+fieldName);
	}

	/**
	 * Tries to read the blob as a string.
	 * @param blob The blob to set.
	 */
	public void setBlob(Blob blob) {
		edtEditor.setFont(ProgramConfig.getInstance().getResultSetFont());
		this.blob = blob;
		final Blob ourBlob = blob;
		edtEditor.setText("Reading CLOB. Please wait...");
		Thread clobReader = new Thread() {
			public void run() {
				String result = "";
				try {
					result = readBlob(ourBlob);
				}
				catch (Exception ex) {
					result = ex.toString();
				}
				finally {
					final String text = result;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							edtEditor.setText(text);
							edtEditor.setSelectionStart(0);
							edtEditor.setSelectionEnd(0);
						}
					});
				}

			}
		};
		clobReader.start();

	}

	/**
	 * Tries to read a blob into a Stringbuffer
	 * @param blob The blob to read
	 * @return The string with the contents
	 * @throws SQLException If a SQL error ocurrs
	 * @throws IOException If an i/o error ocurrs
	 */
	public static String readBlob(Blob blob) throws SQLException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(blob.getBinaryStream()));
		String line = null;
		StringBuffer buffer = new StringBuffer( (int)(blob.length()*1.5) );
		while ( ( line = reader.readLine()  )!=null ) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	/**
	 * Reads the clob into the field.
	 * @param clob The clob to read.
	 */
	public void setClob(Clob clob) {
		edtEditor.setFont(ProgramConfig.getInstance().getResultSetFont());
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
							edtEditor.setSelectionStart(0);
							edtEditor.setSelectionEnd(0);
						}
					});
				}

			}
		};
		clobReader.start();
	}

	/**
	 * Tries to read a blob into a Stringbuffer
	 *
	 * @param clob The clob field to read.
	 * @return The string with the contents
	 * @throws SQLException If a SQL error ocurrs
	 * @throws IOException If an i/o error ocurrs
	 */
	public static String readClob(Clob clob) throws SQLException, IOException {

		BufferedReader reader = new BufferedReader(clob.getCharacterStream());
		String line = null;
		StringBuffer buffer = new StringBuffer( (int)(clob.length()*1.5) );
		while ( ( line = reader.readLine()  )!=null ) {
			buffer.append(line);
		}
		return buffer.toString();

	}

}

