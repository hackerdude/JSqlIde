/*
 * TextAreaDefaults.java - Encapsulates default values for various settings
 * Copyright (C) 1999 Slava Pestov
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 */

package textarea;

import textarea.syntax.*;
import javax.swing.JPopupMenu;
import java.awt.Color;

/**
 * Encapsulates default settings for a text area. This can be passed
 * to the constructor once the necessary fields have been filled out.
 * The advantage of doing this over calling lots of set() methods after
 * creating the text area is that this method is faster.
 */
public class TextAreaDefaults
{
	private static InputHandler DEFAULT_INPUT_HANDLER;
	private static TextAreaDefaults DEFAULTS;

	public InputHandler inputHandler;
	public SyntaxDocument document;
	public boolean editable;

	public boolean caretVisible;
	public boolean caretBlinks;
	public boolean blockCaret;
	public int electricScroll;

	public int cols;
	public int rows;
	public SyntaxStyle[] styles;
	public Color caretColor;
	public Color selectionColor;
	public Color lineHighlightColor;
	public boolean lineHighlight;
	public Color bracketHighlightColor;
	public boolean bracketHighlight;
	public Color eolMarkerColor;
	public boolean eolMarkers;

	public JPopupMenu popup;

	/**
	 * Returns a new TextAreaDefaults object with the default values filled
	 * in.
	 */
	public static TextAreaDefaults getDefaults()
	{
		if(DEFAULTS == null)
		{
			DEFAULTS = new TextAreaDefaults();

			if(DEFAULT_INPUT_HANDLER == null)
			{
				DEFAULT_INPUT_HANDLER = new DefaultInputHandler();
				DEFAULT_INPUT_HANDLER.addDefaultKeyBindings();
			}

			DEFAULTS.inputHandler = DEFAULT_INPUT_HANDLER;
			DEFAULTS.document = new SyntaxDocument();
			DEFAULTS.editable = true;

			DEFAULTS.caretVisible = true;
			DEFAULTS.caretBlinks = true;
			DEFAULTS.electricScroll = 3;

			DEFAULTS.cols = 80;
			DEFAULTS.rows = 25;
			DEFAULTS.styles = SyntaxUtilities.getDefaultSyntaxStyles();
			DEFAULTS.caretColor = Color.red;
			DEFAULTS.selectionColor = new Color(0xccccff);
			DEFAULTS.lineHighlightColor = new Color(0xe0e0e0);
			DEFAULTS.lineHighlight = true;
			DEFAULTS.bracketHighlightColor = Color.black;
			DEFAULTS.bracketHighlight = true;
			DEFAULTS.eolMarkerColor = new Color(0x009999);
			DEFAULTS.eolMarkers = true;
		}

		return DEFAULTS;
	}
}
