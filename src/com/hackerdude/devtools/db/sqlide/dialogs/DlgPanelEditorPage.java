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
import java.awt.event.*;

import javax.swing.*;

import com.hackerdude.devtools.db.sqlide.*;
import com.hackerdude.swing.picklist.font.FontPickListDialog;

public class DlgPanelEditorPage extends JPanel {

	final Action ACTION_CHANGE_SQLCODEFONT = new ActionChangeSQLFont();
	final Action ACTION_CHANGE_RESULTSETFONT = new ActionChangeResultSetFont();

    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JPanel pnTopPane2;
    FontDescriptionLabel lblResultSetFontDescription;
    JPanel pnTopPane;
    FontDescriptionLabel lblSqlFontDescription;
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    private JButton btnChangeSQLCodeFont = new JButton(ACTION_CHANGE_SQLCODEFONT);
    private JButton btnChange = new JButton(ACTION_CHANGE_RESULTSETFONT);
    private JLabel lblAppFonts = new JLabel();

	private Font currentResultSetFont;
	private Font currentSQLFont;

    public DlgPanelEditorPage() {

      setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
      jbInit();

   }

   public void jbInit() {
      pnTopPane2 = new JPanel();
        lblResultSetFontDescription = new FontDescriptionLabel("ResultSet Font: ");
        pnTopPane = new JPanel();
        lblSqlFontDescription = new FontDescriptionLabel("SQL Code Font: ");
        this.addFocusListener(new java.awt.event.FocusAdapter() {
      });
      this.setLayout(borderLayout1);
        jPanel1.setLayout(gridBagLayout1);
        pnTopPane2.setLayout(borderLayout2);
        pnTopPane.setLayout(borderLayout3);
        btnChangeSQLCodeFont.setText("Change...");
        btnChange.setText("Change...");
        lblResultSetFontDescription.setText("ResultSet: ");
        lblSqlFontDescription.setText("SQL Code: ");
        lblAppFonts.setHorizontalAlignment(SwingConstants.CENTER);
        lblAppFonts.setHorizontalTextPosition(SwingConstants.LEFT);
        lblAppFonts.setText("Application Fonts: ");
        this.add(jPanel1,  BorderLayout.CENTER);
        pnTopPane2.add(lblResultSetFontDescription,  BorderLayout.WEST);
        jPanel1.add(pnTopPane,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnTopPane.add(lblSqlFontDescription, BorderLayout.CENTER);
        jPanel1.add(pnTopPane2,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        pnTopPane.add(btnChangeSQLCodeFont,  BorderLayout.EAST);
        pnTopPane2.add(btnChange, BorderLayout.EAST);
        jPanel1.add(lblAppFonts,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


    }


    public void readFromModel() {
		currentSQLFont = ProgramConfig.getInstance().getSQLFont();
		currentResultSetFont = ProgramConfig.getInstance().getResultSetFont();
		lblSqlFontDescription.setFontToDescribe(currentSQLFont);
		lblResultSetFontDescription.setFontToDescribe(currentResultSetFont);

	}


	public void applyToModel() {
		if ( currentSQLFont != null ) ProgramConfig.getInstance().setSQLFont(currentSQLFont);
		if ( currentResultSetFont != null ) ProgramConfig.getInstance().setResultSetFont(currentResultSetFont);
	}

	class FontDescriptionLabel extends JLabel {

		Font fontToDescribe;

		public FontDescriptionLabel(String text) {
			super(text);
		}

		public void setFontToDescribe(Font newFont) {
			fontToDescribe = newFont;
			updateUI();
		}

		public Font getFontToDescribe() { return fontToDescribe; }

		public String getText() {
			String text = super.getText();
			text = text+getFontDescription();
			return text;
		}

		public String getFontDescription() {
			if ( fontToDescribe == null ) return "<No font defined>";
			String	strStyle;

			if (fontToDescribe.isBold()) {
				strStyle = fontToDescribe.isItalic() ? "bold+italic" : "bold";
			} else {
				strStyle = fontToDescribe.isItalic() ? "italic" : "plain";
			}

			String result = fontToDescribe.getName() +", "+strStyle + ", " + fontToDescribe.getSize()+" pt";
			return result;
		}
	}

	class ActionChangeSQLFont extends AbstractAction {
		public ActionChangeSQLFont() {
		}

		public void actionPerformed(ActionEvent evt) {
			Font newFont = FontPickListDialog.showFontSelectionDialog(sqlide.getFrame(), "Change SQL Editor Font", "Compose a new Font for the SQL Editor", currentSQLFont);
			if ( newFont != null ) {
				currentSQLFont = newFont;
				lblSqlFontDescription.setFontToDescribe(newFont);
			}
		}
	}


	class ActionChangeResultSetFont extends AbstractAction {
		public ActionChangeResultSetFont() {
		}

		public void actionPerformed(ActionEvent evt) {
			Font newFont = FontPickListDialog.showFontSelectionDialog(sqlide.getFrame(), "Change ResultSet Font", "Compose a new font for the ResultSet", currentResultSetFont);
			if ( newFont != null ) {
				currentResultSetFont = newFont;
				lblResultSetFontDescription.setFontToDescribe(newFont);
			}
		}
	}

}

