package com.hackerdude.swing.picklist.font;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.hackerdude.swing.SwingUtils;

/**
 * A picklist dialog for fonts.
 */
public class FontPickListDialog extends JDialog {

	final Action ACTION_OK = new ActionOK();
	final Action ACTION_CANCEL = new ActionCancel();

    private BorderLayout blBorderLayout = new BorderLayout();
    private JPanel pnlButtonsPanel = new JPanel();
    private JButton btnCancel = new JButton(ACTION_CANCEL);
    private JButton btnOK = new JButton(ACTION_OK);


	FontPickListPanel pnlFontPickList = new FontPickListPanel();
    private JPanel pnlInstructions = new JPanel();
    private JLabel lblInstructions = new JLabel();

	int modalResult = JOptionPane.CANCEL_OPTION;

	public FontPickListDialog(JFrame owner) {
		super(owner);
		try {
			jbInit();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

    public FontPickListDialog()  {
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        this.getContentPane().setLayout(blBorderLayout);
        btnCancel.setText("Cancel");
        btnOK.setText("OK");
        lblInstructions.setText("Please select a font from the list.");
        this.getContentPane().add(pnlButtonsPanel, BorderLayout.SOUTH);
		this.getContentPane().add(pnlFontPickList, BorderLayout.CENTER);
        this.getContentPane().add(pnlInstructions, BorderLayout.NORTH);
        pnlButtonsPanel.add(btnOK, null);
        pnlButtonsPanel.add(btnCancel, null);
        pnlInstructions.add(lblInstructions, null);
    }

	public void setValue(Font font) {
		pnlFontPickList.setCurrentFont(font);
	}

	public Font getValue() {
		return pnlFontPickList.getCurrentFont();
	}


	public static void main(String[] args) {
		Font currentValue = new JLabel().getFont().deriveFont(48f);
		Font newValue = showFontSelectionDialog(null, "Select Font", "Please select a font, dude!", currentValue);
		if ( newValue == null ) System.out.println("No font selected");
		else System.out.println(newValue.toString());
	}



	public static Font showFontSelectionDialog(JFrame owner, String title, String instructions, Font currentFont) {
		FontPickListDialog dialog = new FontPickListDialog(owner);
		dialog.setTitle(title);
		dialog.setValue(currentFont);
		dialog.pack();
		Point point = SwingUtils.getCenteredWindowPoint(dialog);
		dialog.setLocation(point);
		dialog.setModal(true);
		dialog.show();
		if ( dialog.modalResult == JOptionPane.OK_OPTION ) {
			return dialog.getValue();
		} else return null;
	}

	class ActionOK extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			modalResult = JOptionPane.OK_OPTION;
			hide();
		}
	}

	class ActionCancel extends AbstractAction {
		public void actionPerformed(ActionEvent evt) {
			modalResult = JOptionPane.CANCEL_OPTION;
			hide();
		}
	}

}