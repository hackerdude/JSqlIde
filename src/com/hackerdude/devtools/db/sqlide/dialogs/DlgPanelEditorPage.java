/*
 * DlgPanelEditorPage.java - "Editor" Page of the
 * configuration dialog.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Revision: $Revision$
 * Id      : $Id$
 *
*/

package com.hackerdude.devtools.db.sqlide.dialogs;

import com.hackerdude.devtools.db.sqlide.ProgramConfig;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import java.util.*;
import java.lang.Object.*;
import java.lang.Exception.*;
import java.io.*;

public class DlgPanelEditorPage extends JPanel {

   JComboBox  cbSQLFontName  = new JComboBox();
   JTextField cbSQLFontSize  = new JTextField();
   JLabel lblSQLFontName;
   JLabel lblSQLFontSize;
   JPanel pnTopPane;
   JPanel pnTopPane2;
   static String[] fontFamilyNames;

    public DlgPanelEditorPage() {

      setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
      jbInit();

   }

   public void jbInit() {
      lblSQLFontName = new JLabel("SQL Code Font Name: ");
      lblSQLFontSize  = new JLabel("SQL Code Font Size: ");
      pnTopPane = new JPanel();
      this.addFocusListener(new java.awt.event.FocusAdapter() {
      });
      pnTopPane.add(lblSQLFontName);
      pnTopPane.add(cbSQLFontName);
      pnTopPane2 = new JPanel();
      pnTopPane2.add(lblSQLFontSize);
      pnTopPane2.add(cbSQLFontSize);

      add(pnTopPane, BorderLayout.SOUTH);
      add(pnTopPane2, BorderLayout.SOUTH);

    };

	public void updateFonts() {
		boolean shouldUpdateConfig = false;
		if ( fontFamilyNames == null ) {
		 shouldUpdateConfig = true;
		 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		 fontFamilyNames = ge.getAvailableFontFamilyNames();
		}
		if ( cbSQLFontName.getItemCount() < 1 ) for ( int f=0; f<fontFamilyNames.length; f++ ) cbSQLFontName.addItem(fontFamilyNames[f]);
		if ( shouldUpdateConfig ) updateFromConfig();

	}


    public void updateFromConfig() {
	int i;
	for ( i=0; i<cbSQLFontName.getItemCount(); i++)
	    if ( cbSQLFontName.getItemAt(i).equals(ProgramConfig.getInstance().getSQLFontName()) )
		cbSQLFontName.setSelectedIndex(i);
	//for ( i=0; i<cbSQLFontSize.getItemCount(); i++)
	  //  if ( cbSQLFontSize.getItemAt(i).equals(new Integer( ProgramConfig.getInstance().getSQLFontSize()) ) )
		//cbSQLFontSize.setSelectedIndex(i);
		cbSQLFontSize.setText(Integer.toString(ProgramConfig.getInstance().getSQLFontSize()));
    }

}

/*

  $Log$
  Revision 1.1  2001/09/07 02:51:20  davidmartinez
  Initial revision

  Revision 1.2  2000/05/05 22:51:33  david
  Implement a connection pool; Completely reworked DatabaseSpec and the
  DlgDbSpecConfig to work with a user defined set of name/value pairs;
  Added a history feature to the Interactive SQL window; Promoted
  refreshConfiguration to required by the Panel Interface.

  Revision 1.1.1.1  2000/04/27 11:55:02  david
  Initial Import

  Revision 1.4  1999/10/25 15:20:37  david
  Moved files to a flat JSqlIde package, in preparation for the
  makefile and package organization.

  Revision 1.3  1999/10/19 00:45:05  david
  Added Log


 */
