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

import java.awt.*;

import javax.swing.*;

import com.hackerdude.devtools.db.sqlide.*;

public class DlgPanelEditorPage extends JPanel {

   static String[] fontFamilyNames;
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JPanel pnTopPane2;
    JTextField cbSQLFontSize = new JTextField();
    JLabel lblSQLFontSize;
    JComboBox cbSQLFontName = new JComboBox();
    JPanel pnTopPane;
    JLabel lblSQLFontName;
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();

    public DlgPanelEditorPage() {

      setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
      jbInit();

   }

   public void jbInit() {
      pnTopPane2 = new JPanel();
        lblSQLFontSize = new JLabel("SQL Code Font Size: ");
        pnTopPane = new JPanel();
        lblSQLFontName = new JLabel("SQL Code Font Name: ");
        this.addFocusListener(new java.awt.event.FocusAdapter() {
      });
      this.setLayout(borderLayout1);
        jPanel1.setLayout(gridBagLayout1);
        pnTopPane2.setLayout(borderLayout2);
        pnTopPane.setLayout(borderLayout3);
        cbSQLFontSize.setMinimumSize(new Dimension(15, 21));
        this.add(jPanel1,  BorderLayout.CENTER);
        pnTopPane2.add(lblSQLFontSize,  BorderLayout.WEST);
        pnTopPane2.add(cbSQLFontSize, BorderLayout.CENTER);
        jPanel1.add(pnTopPane,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnTopPane.add(lblSQLFontName, BorderLayout.CENTER);
        pnTopPane.add(cbSQLFontName, BorderLayout.EAST);
        jPanel1.add(pnTopPane2,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


    };

	public void updateFonts() {
		boolean shouldUpdateConfig = false;
		if ( fontFamilyNames == null ) {
		 shouldUpdateConfig = true;
		 GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		 fontFamilyNames = ge.getAvailableFontFamilyNames();
		}
		if ( cbSQLFontName.getItemCount() < 1 ) for ( int f=0; f<fontFamilyNames.length; f++ ) cbSQLFontName.addItem(fontFamilyNames[f]);
		if ( shouldUpdateConfig ) readFromModel();

	}


    public void readFromModel() {
		int i;
		for ( i=0; i<cbSQLFontName.getItemCount(); i++) {
			if ( cbSQLFontName.getItemAt(i).equals(ProgramConfig.getInstance().getSQLFontName()) ) {
				cbSQLFontName.setSelectedIndex(i);
			}
		}
		cbSQLFontSize.setText(Integer.toString(ProgramConfig.getInstance().getSQLFontSize()));
	}

}

