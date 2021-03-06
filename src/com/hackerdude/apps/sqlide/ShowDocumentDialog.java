package com.hackerdude.apps.sqlide;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLEditorKit;

/**
 * Dialog box that can show a document. Useful for
 * README's, TODO's, etcetera.
 * @author David Martinez
 * @version 1.0
 */
public class ShowDocumentDialog extends JDialog {
	JPanel panel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JLabel jLabel1 = new JLabel();
	JScrollPane jScrollPane1 = new JScrollPane();
	JEditorPane documentContents = new JEditorPane();
	JPanel jPanel2 = new JPanel();
	JButton jButton1 = new JButton();

	public ShowDocumentDialog(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			jbInit();
			pack();
			positionDialog();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public ShowDocumentDialog(JFrame owner) {
		this(owner, "", false);
	}

	void jbInit() throws Exception {
		panel1.setLayout(borderLayout1);
		jPanel1.setLayout(borderLayout2);
		jLabel1.setText("");
		jButton1.setMnemonic('C');
		jButton1.setText("Close");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton1_actionPerformed(e);
			}
		});
		getContentPane().add(panel1);
		panel1.add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(jLabel1, BorderLayout.WEST);
		panel1.add(jScrollPane1, BorderLayout.CENTER);
		panel1.add(jPanel2, BorderLayout.SOUTH);
		jPanel2.add(jButton1, null);
		jScrollPane1.getViewport().add(documentContents, null);
	}

	/**
	 * Loads text from an input stream.
	 */
	void loadText(InputStream is) throws IOException {
		if ( is != null ) {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String thisLine = null;
			thisLine = br.readLine();
			while ( thisLine != null ) {
				sb.append(thisLine).append(" \n");
				thisLine = br.readLine();
			}
			documentContents.setText(sb.toString());
			documentContents.setEditorKit(new HTMLEditorKit());
			documentContents.setText(sb.toString());
			documentContents.setSelectionStart(1);
			documentContents.setSelectionEnd(1);
		}

	}


	/**
	 * Show "Paying for SQLIDE" dialog.
	 */
//	public void showPayDialog() {
//		showDialog("Paying for SQLIDE", ShowDocumentDialog.class.getResourceAsStream("/docs/paying.html"));
//	}


	/**
	 * Show the TODO.HTML dialog.
	 */
//	public void showTodoDialog() {
//		showDialog("To do List for Project", ShowDocumentDialog.class.getResourceAsStream("/docs/TODO.html"));
//	}
//

	/**
	 * Show the readme dialog.
	 */
//	public void showReadmeDialog() {
//		showDialog("Project README", ShowDocumentDialog.class.getResourceAsStream("/docs/README.html"));
//	}

	/**
	 * Show the Known Bugs dialog.
	 */
//	public void showKnownBugsDialog() {
//		showDialog("Known Bugs in SQLIDE", ShowDocumentDialog.class.getResourceAsStream("/docs/BUGS.html"));
//	}


	/**
	 * Shows the dialog with the specified title, the resource name and the
	 * failover filename.
	 */
	public void showDialog(String title, InputStream inputStream) {
		if ( inputStream == null ) return;
		this.setTitle(title);

		this.setModal(true);
		this.documentContents.setEditable(false);
		try {
			loadText(inputStream);
		} catch ( IOException exc ) {
			exc.printStackTrace();
		}

		this.show();
	}

	private void positionDialog() throws HeadlessException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			  this.setSize(500, 300);
			  Dimension dim = this.getSize();

			  Double xPos = new Double((screenSize.getWidth()-dim.getWidth())/2);
			  Double yPos = new Double((screenSize.getHeight()-dim.getHeight())/2);
			  this.setBounds(xPos.intValue(), yPos.intValue(), new Double(dim.getWidth()).intValue(),new Double(dim.getHeight()).intValue());
	}

//	public void showLicenseDialog() {
//		showDialog("SQLIDE License Agreement", ShowDocumentDialog.class.getResourceAsStream("/docs/LICENSE.html"));
//	}

	void jButton1_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

}
