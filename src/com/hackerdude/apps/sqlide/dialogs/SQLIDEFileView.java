package com.hackerdude.apps.sqlide.dialogs;

import javax.swing.filechooser.FileView;
import java.io.*;
import javax.swing.Icon;
import com.hackerdude.apps.sqlide.ProgramIcons;

/**
 * A file view for dialog boxes that only shows SQL files.
 */
public class SQLIDEFileView extends FileView {

	public SQLIDEFileView() {
	}

	public Icon getIcon(File file) {
		String fileExt = file.getName();
		if ( fileExt.length() > 4 ) {
			fileExt = fileExt.substring(fileExt.length()-4);
		}
		if ( fileExt.equalsIgnoreCase(".sql") ) {
			return ProgramIcons.getInstance().getGoIcon();
		} else return null;
	}
}

