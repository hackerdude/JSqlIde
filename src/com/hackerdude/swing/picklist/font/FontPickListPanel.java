package com.hackerdude.swing.picklist.font;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class FontPickListPanel extends JPanel {

	private FontNamesListModel fontNamesModel = new FontNamesListModel();
	private FontSizesListModel fontSizesModel = new FontSizesListModel();
	private FontStylesModel    fontStylesModel = new FontStylesModel();

    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel pnlCenterPanel = new JPanel();
    private GridBagLayout gridBagLayout1 = new GridBagLayout();
    private JPanel pnlPreview = new JPanel();
    private BorderLayout blPreview = new BorderLayout();
    private JLabel lblPreview = new JLabel();
    private JScrollPane scrollPreview = new JScrollPane();
    private JTextArea taPreview = new JTextArea();
    private JPanel pnlTopPanel = new JPanel();
    private JPanel pnlFontNames = new JPanel();
    private JLabel lblFontName = new JLabel();
    private BorderLayout borderLayout2 = new BorderLayout();
    private JPanel pnlFontSizes = new JPanel();
    private BorderLayout borderLayout3 = new BorderLayout();
    private JPanel pnlSizePanel = new JPanel();
    private BorderLayout borderLayout4 = new BorderLayout();
    private JLabel lblFontSizes = new JLabel();
    private JTextField jTextField1 = new JTextField();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JList lstFontNames = new JList();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JList lstSize = new JList();
    private JPanel pnlStyle = new JPanel();
    private JScrollPane jScrollPane3 = new JScrollPane();
    private BorderLayout borderLayout5 = new BorderLayout();
    private JList lstStyles = new JList();
    private JLabel lblStyle = new JLabel();

    public FontPickListPanel() {
        try {
            jbInit();
			lstFontNames.setModel(fontNamesModel);
			lstSize.setModel(fontSizesModel);
			lstStyles.setModel(fontStylesModel);

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        pnlCenterPanel.setLayout(gridBagLayout1);
        pnlPreview.setLayout(blPreview);
        lblPreview.setText("Preview:");
        taPreview.setText("The quick brown fox jumped over the lazy dog.\nABCDEFGabcdefg01234567890!@#$%^");
        pnlFontNames.setLayout(borderLayout2);
        lblFontName.setDisplayedMnemonic('N');
        lblFontName.setText("Font Names:");
        pnlFontSizes.setLayout(borderLayout3);
        pnlSizePanel.setLayout(borderLayout4);
        lblFontSizes.setDisplayedMnemonic('Z');
        lblFontSizes.setText("Size:");
        jTextField1.setText("11");
        pnlStyle.setLayout(borderLayout5);
        lblStyle.setDisplayedMnemonic('T');
        lblStyle.setText("Style:");
        this.add(pnlCenterPanel, BorderLayout.CENTER);
        this.add(pnlPreview,  BorderLayout.SOUTH);
        pnlPreview.add(lblPreview, BorderLayout.NORTH);
        pnlPreview.add(scrollPreview,  BorderLayout.CENTER);
        scrollPreview.getViewport().add(taPreview, null);
        this.add(pnlTopPanel, BorderLayout.NORTH);
        pnlCenterPanel.add(pnlFontNames,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        pnlFontNames.add(lblFontName,  BorderLayout.NORTH);
        pnlFontNames.add(jScrollPane1,  BorderLayout.CENTER);
        jScrollPane1.getViewport().add(lstFontNames, null);
        pnlCenterPanel.add(pnlFontSizes,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 30, 0));
        pnlFontSizes.add(pnlSizePanel, BorderLayout.NORTH);
        pnlSizePanel.add(lblFontSizes,  BorderLayout.WEST);
        pnlSizePanel.add(jTextField1,  BorderLayout.CENTER);
        pnlFontSizes.add(jScrollPane2,  BorderLayout.CENTER);
        pnlCenterPanel.add(pnlStyle, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pnlStyle.add(jScrollPane3, BorderLayout.CENTER);
        jScrollPane3.getViewport().add(lstStyles, null);
        jScrollPane2.getViewport().add(lstSize, null);
        pnlStyle.add(lblStyle, BorderLayout.NORTH);
    }




	public void setFont(Font newFont) {
//		this.font = newFont;
		/** @todo Implement */
	}

	public Font getFont() {
		/** @todo Implement */
		String fontName = (String)lstFontNames.getSelectedValue();
		Integer fontSize = (Integer)lstSize.getSelectedValue();
		int fontStyle = getFontStyle((String)lstStyles.getSelectedValue());
		return new Font(fontName, fontStyle, fontSize.intValue());
	}

	class FontNamesListModel extends AbstractListModel {
		String[] fonts;
		public FontNamesListModel() {
			fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		}

		public int getSize() {
			return fonts.length;
		}

		public Object getElementAt(int row) {
			return fonts[row];
		}

	}


	class FontSizesListModel extends AbstractListModel {
		Integer[] fontSizes = calculateFontSizes();
		public FontSizesListModel() {

		}

		public int getSize() {
			return fontSizes.length;
		}

		public Object getElementAt(int row) {
			return fontSizes[row];
		}

		public Integer[] calculateFontSizes() {
			ArrayList al = new ArrayList();
			for ( int i=6; i<100; i=i+2 ) {
				al.add(new Integer(i));
			}
			Integer[] result = new Integer[al.size()];
			result = (Integer[])al.toArray(result);
			return result;
		}
	}

	class FontStylesModel extends AbstractListModel {
		String[] fontStyles = { "Bold", "Italic", "Roman" };

		public int getSize() {
			return fontStyles.length;
		}

		public Object getElementAt(int row) {
			return fontStyles[row];
		}

	}
	public static int getFontStyle(String styleName) {
		int result = Font.PLAIN;
		if ( styleName == null ) return result;
		if ( styleName.equals("Bold") ) result = Font.BOLD;
		else if ( styleName.equals("Italic") ) result = Font.ITALIC;
		else if ( styleName.equals("Roman") ) result = Font.ROMAN_BASELINE;
		return result;
	}

	public static void main(String[] args) {
		FontPickListPanel panel = new FontPickListPanel();
		JDialog dlg = new JDialog();
		dlg.getContentPane().add(panel);
		dlg.pack();
		dlg.show();
	}

	public void updateFontPreview() {
		Font currentFont = getFont();
		taPreview.setFont(currentFont);
	}

}