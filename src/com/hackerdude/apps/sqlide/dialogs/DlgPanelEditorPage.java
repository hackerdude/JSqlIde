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

package com.hackerdude.apps.sqlide.dialogs;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.hackerdude.apps.sqlide.ProgramConfig;
import com.hackerdude.apps.sqlide.SqlIdeApplication;
import com.hackerdude.swing.picklist.font.FontPickListDialog;

/**
 * This panel allows the user to specify the fonts for different on-screen
 * elements.
 * @author David Martinez
 * @version 1.0
 */
public class DlgPanelEditorPage extends JPanel {


	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jPanel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JLabel lblAppFonts = new JLabel();


	ActionChangeSQLFont[] actions;
    private JScrollPane jScrollPane1 = new JScrollPane();

	public DlgPanelEditorPage() {

		setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
		jbInit();

	}

	public void jbInit() {
		this.setLayout(borderLayout1);
		jPanel1.setLayout(gridBagLayout1);
		lblAppFonts.setHorizontalAlignment(SwingConstants.CENTER);
		lblAppFonts.setHorizontalTextPosition(SwingConstants.LEFT);
		lblAppFonts.setText("Application Fonts: ");
		this.add(jPanel1,  BorderLayout.CENTER);
        jPanel1.add(jScrollPane1,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(lblAppFonts, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));


	}


	public void readFromModel() {
		jScrollPane1.getViewport().removeAll();

		JPanel fontContainer = new JPanel(new GridBagLayout());

		JPanel[] panels = getFontPanels();
		for ( int i=0; i<panels.length; i++ ) {
			fontContainer.add(panels[i],new GridBagConstraints(0, i, 1,1, 1,1,GridBagConstraints.CENTER,GridBagConstraints.REMAINDER, new Insets(0,0,0,0), 0,0));
		}
		jScrollPane1.getViewport().add(fontContainer);
	}


	public void applyToModel() {
		for ( int i=0; i<ProgramConfig.FONT_NAMES.length; i++ ) {
			Font newFont = actions[i].getFont();
			ProgramConfig.getInstance().setFont(ProgramConfig.FONT_NAMES[i], newFont);
		}
	}

	public ActionChangeSQLFont[] initializeFontActions() {
		ArrayList arrayList = new ArrayList();
		for ( int i=0; i<ProgramConfig.FONT_NAMES.length; i++ ) {
			String currentFontName = ProgramConfig.FONT_NAMES[i];
			String title = "Change Font for "+currentFontName;
			String instructions = "Compose a new Font for "+currentFontName;
			ActionChangeSQLFont changeAction = new ActionChangeSQLFont(SqlIdeApplication.getFrame(),
			title, instructions,
			ProgramConfig.getInstance().getFont(currentFontName), new FontDescriptionLabel(currentFontName+": "));
			arrayList.add(changeAction);
		}
		ActionChangeSQLFont[] result = new ActionChangeSQLFont[arrayList.size()];
		result = (ActionChangeSQLFont[])arrayList.toArray((ActionChangeSQLFont[])result);
		return result;
	}

	public JPanel[] getFontPanels() {
		actions = initializeFontActions();
		JPanel[] result = new JPanel[actions.length];
		for ( int i=0; i<actions.length; i++) {
			FontDescriptionLabel label = actions[i].getDescriptionLabel();
			JButton button = new JButton(actions[i]);
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(label, BorderLayout.CENTER);
			panel.add(button, BorderLayout.EAST);
			result[i] = panel;
		}
		return result;

	}

	/**
	 * This is a subclassing of JLabel to show the font description as well
	 * as the specified text.
	 */
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
			text = text+getFontDescription()+" ";
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

	/**
	 * This action allows you to change the Font for an element.
	 * @author David Martinez
	 * @version 1.0
	 */
	class ActionChangeSQLFont extends AbstractAction {

		JFrame owner;
		String title;
		String instructions;
		Font currentFont;
		FontDescriptionLabel fontLabel;

		public ActionChangeSQLFont(JFrame owner, String title, String instructions, Font currentFont, FontDescriptionLabel descriptionLabel) {
			super("..");
			this.owner = owner;
			this.title = title;
			this.instructions = instructions;
			this.currentFont = currentFont;
			this.fontLabel = descriptionLabel;
			fontLabel.setFontToDescribe(currentFont);
		}

		public void actionPerformed(ActionEvent evt) {
			Font newFont = FontPickListDialog.showFontSelectionDialog(owner, title, instructions, currentFont);
			if ( newFont != null ) {
				currentFont = newFont;
				fontLabel.setFontToDescribe(newFont);
			}
		}

		public Font getFont() {
			return currentFont;
		}
		public FontDescriptionLabel getDescriptionLabel() {
			return fontLabel;
		}
	}


}
