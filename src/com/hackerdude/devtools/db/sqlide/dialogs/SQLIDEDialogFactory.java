package com.hackerdude.devtools.db.sqlide.dialogs;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class SQLIDEDialogFactory {

	public SQLIDEDialogFactory() {
	}

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