package com.hackerdude.devtools.db.sqlide.dialogs;

import javax.swing.filechooser.FileView;
import java.io.*;
import javax.swing.Icon;
import com.hackerdude.devtools.db.sqlide.ProgramIcons;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
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

