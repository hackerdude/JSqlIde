/*
 *   DlgPanelGeneralPage.java - "General" Page of the
 *   configuration dialog.
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
package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.hackerdude.apps.sqlide.ProgramConfig;


/**
 * "General" page of the Configuration dialog.
 *
 * @version $Id$
 */
public class DlgPanelGeneralPage extends JPanel {

    public JComboBox  cbLookFeel = new JComboBox();

    public DlgPanelGeneralPage( ) {
		JLabel lblLookFeel = new JLabel("Look and Feel: ");

		// Get the available Look and Feels
		LookAndFeelInfo[] theLooks = UIManager.getInstalledLookAndFeels();
		for ( int lf=0; lf<theLooks.length; lf++ ) cbLookFeel.addItem(theLooks[lf].getName());

		JPanel pnMidPane = new JPanel();
		pnMidPane.setLayout( new BorderLayout() );

		JPanel pnTopPane = new JPanel();

		pnTopPane.add(lblLookFeel);
		pnTopPane.add(cbLookFeel);
		setLayout( new BorderLayout());
		add(pnTopPane, BorderLayout.NORTH);
		add(pnMidPane, BorderLayout.CENTER);
    }

	public void readFromModel() {
		String lkName = ProgramConfig.getInstance().getUILookandFeel();
		cbLookFeel.setSelectedItem(UIManager.getLookAndFeel().getName());
	}


}

