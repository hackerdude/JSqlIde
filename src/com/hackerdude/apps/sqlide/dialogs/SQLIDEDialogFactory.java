package com.hackerdude.apps.sqlide.dialogs;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.*;

/**
 * The dialog factory creates common dialogs with SQLIDE
 * application-specific features.
 */
public class SQLIDEDialogFactory {

	/**
	 * Creates a SQL file chooser that allows you to select SQL files with
	 * a preview pane.
	 * @param curDir The directory you want the dialog to start on.
	 * @return The file chooser.
	 */
	public static JFileChooser createSQLFileChooser(String curDir) {
		JFileChooser chooser = new JFileChooser(curDir);
		final SQLFilePreviewPanel panel = new SQLFilePreviewPanel();
		chooser.setAccessory(panel);
		chooser.setFileView(new SQLIDEFileView());
		chooser.addPropertyChangeListener("SelectedFileChangedProperty",new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				File newFile = (File)evt.getNewValue();
				try {
					panel.setCurrentFile(newFile);
				} catch ( Exception exc ) {
					panel.clearContents();
				}
			}
		});
		return chooser;
	}



}
