/*
 * DefaultInputHandler.java - Default implementation of an input handler
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

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

/**
 * The default input handler. It maps sequences of keystrokes into actions
 * and inserts key typed events into the text area. It also defines the
 * standard set of text area actions which can be used in any input handler.
 * @author Slava Pestov
 * @version $Id$
 */
public class DefaultInputHandler implements InputHandler
{
	public static final ActionListener BACKSPACE = new backspace();
	public static final ActionListener DELETE = new delete();
	public static final ActionListener END = new end(false);
	public static final ActionListener SELECT_END = new end(true);
	public static final ActionListener INSERT_BREAK = new insert_break();
	public static final ActionListener INSERT_TAB = new insert_tab();
	public static final ActionListener HOME = new home(false);
	public static final ActionListener SELECT_HOME = new home(true);
	public static final ActionListener NEXT_CHAR = new next_char(false);
	public static final ActionListener NEXT_LINE = new next_line(false);
	public static final ActionListener NEXT_PAGE = new next_page(false);
	public static final ActionListener NEXT_WORD = new next_word(false);
	public static final ActionListener SELECT_NEXT_CHAR = new next_char(true);
	public static final ActionListener SELECT_NEXT_LINE = new next_line(true);
	public static final ActionListener SELECT_NEXT_PAGE = new next_page(true);
	public static final ActionListener SELECT_NEXT_WORD = new next_word(true);
	public static final ActionListener OVERWRITE = new overwrite();
	public static final ActionListener PREV_CHAR = new prev_char(false);
	public static final ActionListener PREV_LINE = new prev_line(false);
	public static final ActionListener PREV_PAGE = new prev_page(false);
	public static final ActionListener PREV_WORD = new prev_word(false);
	public static final ActionListener SELECT_PREV_CHAR = new prev_char(true);
	public static final ActionListener SELECT_PREV_LINE = new prev_line(true);
	public static final ActionListener SELECT_PREV_PAGE = new prev_page(true);
	public static final ActionListener SELECT_PREV_WORD = new prev_word(true);
	public static final ActionListener TOGGLE_RECT = new toggle_rect();

	public static final ActionListener[] ACTIONS = {
		BACKSPACE, DELETE, END, SELECT_END, INSERT_BREAK,
		INSERT_TAB, HOME, SELECT_HOME, NEXT_CHAR, NEXT_LINE,
		NEXT_PAGE, NEXT_WORD, SELECT_NEXT_CHAR, SELECT_NEXT_LINE,
		SELECT_NEXT_PAGE, SELECT_NEXT_WORD, OVERWRITE, PREV_CHAR,
		PREV_LINE, PREV_PAGE, PREV_WORD, SELECT_PREV_CHAR,
		SELECT_PREV_LINE, SELECT_PREV_PAGE, SELECT_PREV_WORD,
		TOGGLE_RECT };

	public static final String[] ACTION_NAMES = {
		"backspace", "delete", "end", "select-end", "insert-break",
		"insert-tab", "home", "select-home", "next-char", "next-line",
		"next-page", "next-word", "select-next-char", "select-next-line",
		"select-next-page", "select-next-word", "overwrite", "prev-char",
		"prev-line", "prev-page", "prev-word", "select-prev-char",
		"select-prev-line", "select-prev-page", "select-prev-word", "toggle-rect" };

	/**
	 * Creates a new input handler with no key bindings defined.
	 */
	public DefaultInputHandler()
	{
		bindings = currentBindings = new Hashtable();
	}

	/**
	 * Sets up the default key bindings.
	 */
	public void addDefaultKeyBindings()
	{
		addKeyBinding("BACK_SPACE",BACKSPACE);
		addKeyBinding("DELETE",DELETE);

		addKeyBinding("ENTER",INSERT_BREAK);
		addKeyBinding("TAB",INSERT_TAB);

		addKeyBinding("INSERT",OVERWRITE);
		addKeyBinding("C+\\",TOGGLE_RECT);

		addKeyBinding("HOME",HOME);
		addKeyBinding("END",END);
		addKeyBinding("S+HOME",SELECT_HOME);
		addKeyBinding("S+END",SELECT_END);

		addKeyBinding("PAGE_UP",PREV_PAGE);
		addKeyBinding("PAGE_DOWN",NEXT_PAGE);
		addKeyBinding("S+PAGE_UP",SELECT_PREV_PAGE);
		addKeyBinding("S+PAGE_DOWN",SELECT_NEXT_PAGE);

		addKeyBinding("LEFT",PREV_CHAR);
		addKeyBinding("S+LEFT",SELECT_PREV_CHAR);
		addKeyBinding("C+LEFT",PREV_WORD);
		addKeyBinding("CS+LEFT",SELECT_PREV_WORD);
		addKeyBinding("RIGHT",NEXT_CHAR);
		addKeyBinding("S+RIGHT",SELECT_NEXT_CHAR);
		addKeyBinding("C+RIGHT",NEXT_WORD);
		addKeyBinding("CS+RIGHT",SELECT_NEXT_WORD);
		addKeyBinding("UP",PREV_LINE);
		addKeyBinding("S+UP",SELECT_PREV_LINE);
		addKeyBinding("DOWN",NEXT_LINE);
		addKeyBinding("S+DOWN",SELECT_NEXT_LINE);
	}

	/**
	 * Adds a key binding to this input handler. The key binding is
	 * a list of white space separated key strokes of the form
	 * <i>[modifiers+]key</i> where modifier is C for Control, A for Alt,
	 * or S for Shift, and key is either a character (a-z) or a field
	 * name in the KeyEvent class prefixed with VK_ (e.g., BACK_SPACE)
	 * @param keyBinding The key binding
	 * @param action The action
	 */
	public void addKeyBinding(String keyBinding, ActionListener action)
	{
	        Hashtable current = bindings;

		StringTokenizer st = new StringTokenizer(keyBinding);
		while(st.hasMoreTokens())
		{
			KeyStroke keyStroke = parseKeyStroke(st.nextToken());
			if(keyStroke == null)
				return;

			if(st.hasMoreTokens())
			{
				Object o = current.get(keyStroke);
				if(o instanceof Hashtable)
					current = (Hashtable)o;
				else
				{
					o = new Hashtable();
					current.put(keyStroke,o);
					current = (Hashtable)o;
				}
			}
			else
				current.put(keyStroke,action);
		}
	}

	/**
	 * Removes a key binding from this input handler. This is not yet
	 * implemented.
	 * @param keyBinding The key binding
	 */
	public void removeKeyBinding(String keyBinding)
	{
		throw new InternalError("Not yet implemented");
	}

	/**
	 * Removes all key bindings from this input handler.
	 */
	public void removeAllKeyBindings()
	{
		bindings.clear();
	}

	/**
	 * Grabs the next key typed event and invokes the specified
	 * action with the key as a the action command.
	 * @param action The action
	 */
	public void grabNextKeyStroke(ActionListener listener)
	{
		grabAction = listener;
	}

	/**
	 * Returns a copy of this input handler that shares the same
	 * key bindings. Setting key bindings in the copy will also
	 * set them in the original.
	 */
	public InputHandler copy()
	{
		return new DefaultInputHandler(this);
	}

	/**
	 * Handle a key pressed event. This will look up the binding for
	 * the key stroke and execute it.
	 */
	public void keyPressed(KeyEvent evt)
	{
		int keyCode = evt.getKeyCode();
		int modifiers = evt.getModifiers();
		if((modifiers & ~KeyEvent.SHIFT_MASK) != 0
			|| evt.isActionKey()
			|| keyCode == KeyEvent.VK_BACK_SPACE
			|| keyCode == KeyEvent.VK_DELETE
			|| keyCode == KeyEvent.VK_ENTER
			|| keyCode == KeyEvent.VK_TAB)
		{
			if(grabAction != null)
			{
				grabAction.actionPerformed(new ActionEvent(
					evt.getSource(),ActionEvent.ACTION_PERFORMED,
					"\0",modifiers));
				grabAction = null;
				return;
			}

			KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode,
				modifiers);
			Object o = currentBindings.get(keyStroke);
			if(o == null)
			{
				// Don't beep if the user presses some
				// key we don't know about unless a
				// prefix is active. Otherwise it will
				// beep when caps lock is pressed, etc.
				if(currentBindings != bindings)
				{
					Toolkit.getDefaultToolkit().beep();
					// F10 should be passed on, but C+e F10
					// shouldn't
					evt.consume();
				}
				currentBindings = bindings;
				return;
			}
			else if(o instanceof ActionListener)
			{
				((ActionListener)o).actionPerformed(
					new ActionEvent(evt.getSource(),
					ActionEvent.ACTION_PERFORMED,
					null,modifiers));
				currentBindings = bindings;
				evt.consume();
				return;
			}
			else if(o instanceof Hashtable)
			{
				currentBindings = (Hashtable)o;
				evt.consume();
				return;
			}
			else if(keyCode != KeyEvent.VK_ALT
				&& keyCode != KeyEvent.VK_CONTROL
				&& keyCode != KeyEvent.VK_SHIFT
				&& keyCode != KeyEvent.VK_META)
			{
				return;
			}
		}
	}

	/**
	 * Handle a key released event. These are ignored.
	 */
	public void keyReleased(KeyEvent evt)
	{
	}

	/**
	 * Handle a key typed event. This inserts the key into the text area.
	 */
	public void keyTyped(KeyEvent evt)
	{
		int modifiers = evt.getModifiers();
		char c = evt.getKeyChar();
		if(c != KeyEvent.CHAR_UNDEFINED &&
			(modifiers & KeyEvent.ALT_MASK) == 0)
		{
			if(c >= 0x20 && c != 0x7f)
			{
				currentBindings = bindings;

				JEditTextArea textArea = getTextArea(evt);

				if(grabAction != null)
				{
					grabAction.actionPerformed(new ActionEvent(
						textArea,ActionEvent.ACTION_PERFORMED,
						String.valueOf(c)));
					grabAction = null;
					return;
				}
				
				if(!textArea.isEditable())
				{
					textArea.getToolkit().beep();
					return;
				}

				textArea.overwriteSetSelectedText(String.valueOf(c));
			}
		}
	}

	/**
	 * Converts a string to a keystroke. The string should be of the
	 * form <i>modifiers</i>+<i>shortcut</i> where <i>modifiers</i>
	 * is any combination of A for Alt, C for Control, S for Shift
	 * or M for Meta, and <i>shortcut</i> is either a single character,
	 * or a keycode name from the <code>KeyEvent</code> class, without
	 * the <code>VK_</code> prefix.
	 * @param keyStroke A string description of the key stroke
	 */
	public static KeyStroke parseKeyStroke(String keyStroke)
	{
		if(keyStroke == null)
			return null;
		int modifiers = 0;
		int ch = '\0';
		int index = keyStroke.indexOf('+');
		if(index != -1)
		{
			for(int i = 0; i < index; i++)
			{
				switch(Character.toUpperCase(keyStroke
					.charAt(i)))
				{
				case 'A':
					modifiers |= InputEvent.ALT_MASK;
					break;
				case 'C':
					modifiers |= InputEvent.CTRL_MASK;
					break;
				case 'M':
					modifiers |= InputEvent.META_MASK;
					break;
				case 'S':
					modifiers |= InputEvent.SHIFT_MASK;
					break;
				}
			}
		}
		String key = keyStroke.substring(index + 1);
		if(key.length() == 1)
			ch = Character.toUpperCase(key.charAt(0));
		else if(key.length() == 0)
		{
			System.err.println("Invalid key stroke: " + keyStroke);
			return null;
		}
		else
		{
			try
			{
				ch = KeyEvent.class.getField("VK_".concat(key))
					.getInt(null);
			}
			catch(Exception e)
			{
				System.err.println("Invalid key stroke: "
					+ keyStroke);
				return null;
			}
		}		
		return KeyStroke.getKeyStroke(ch,modifiers);
	}

	public static JEditTextArea getTextArea(EventObject evt)
	{
		if(evt != null)
		{
			Object o = evt.getSource();
			if(o instanceof Component)
			{
				// find the parent text area
				Component c = (Component)o;
				for(;;)
				{
					if(c instanceof JEditTextArea)
						return (JEditTextArea)c;
					else if(c == null)
						break;
					if(c instanceof JPopupMenu)
						c = ((JPopupMenu)c)
							.getInvoker();
					else
						c = c.getParent();
				}
			}
		}

		// this shouldn't happen
		System.err.println("BUG: getTextArea() returning null");
		System.err.println("Report this to Slava Pestov <sp@gjt.org>");
		return null;
	}

	// private members
	private Hashtable bindings;
	private Hashtable currentBindings;
	private ActionListener grabAction;

	private DefaultInputHandler(DefaultInputHandler copy)
	{
		bindings = currentBindings = copy.bindings;
	}

	public static class backspace implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(textArea.getSelectionStart()
			   != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}
			else
			{
				int caret = textArea.getCaretPosition();
				if(caret == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				try
				{
					textArea.getDocument().remove(caret - 1,1);
				}
				catch(BadLocationException bl)
				{
					bl.printStackTrace();
				}
			}
		}
	}

	public static class delete implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(textArea.getSelectionStart()
			   != textArea.getSelectionEnd())
			{
				textArea.setSelectedText("");
			}
			else
			{
				int caret = textArea.getCaretPosition();
				if(caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				try
				{
					textArea.getDocument().remove(caret,1);
				}
				catch(BadLocationException bl)
				{
					bl.printStackTrace();
				}
			}
		}
	}

	public static class end implements ActionListener
	{
		private boolean select;

		public end(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			int caret = textArea.getCaretPosition();

			int lastOfLine = textArea.getLineEndOffset(
				textArea.getCaretLine()) - 1;
			int lastVisibleLine = textArea.getFirstLine()
				+ textArea.getVisibleLines();
			if(lastVisibleLine >= textArea.getLineCount())
			{
				lastVisibleLine = Math.min(textArea.getLineCount() - 1,
					lastVisibleLine);
			}
			else
				lastVisibleLine -= (textArea.getElectricScroll() + 1);

			int lastVisible = textArea.getLineEndOffset(lastVisibleLine) - 1;
			int lastDocument = textArea.getDocumentLength();

			if(caret == lastDocument)
			{
				textArea.getToolkit().beep();
				return;
			}
			else if(caret == lastVisible)
				caret = lastDocument;
			else if(caret == lastOfLine)
				caret = lastVisible;
			else
				caret = lastOfLine;

			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class home implements ActionListener
	{
		private boolean select;

		public home(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			int caret = textArea.getCaretPosition();

			int firstLine = textArea.getFirstLine();

			int firstOfLine = textArea.getLineStartOffset(
				textArea.getCaretLine());
			int firstVisibleLine = (firstLine == 0 ? 0 :
				firstLine + textArea.getElectricScroll());
			int firstVisible = textArea.getLineStartOffset(
				firstVisibleLine);

			if(caret == 0)
			{
				textArea.getToolkit().beep();
				return;
			}
			else if(caret == firstVisible)
				caret = 0;
			else if(caret == firstOfLine)
				caret = firstVisible;
			else
				caret = firstOfLine;

			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class insert_break implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			textArea.setSelectedText("\n");
		}
	}

	public static class insert_tab implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);

			if(!textArea.isEditable())
			{
				textArea.getToolkit().beep();
				return;
			}

			textArea.overwriteSetSelectedText("\t");
		}
	}

	public static class next_char implements ActionListener
	{
		private boolean select;

		public next_char(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			if(caret == textArea.getDocumentLength())
			{
				textArea.getToolkit().beep();
				return;
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					caret + 1);
			else
				textArea.setCaretPosition(caret + 1);
		}
	}

	public static class next_line implements ActionListener
	{
		private boolean select;

		public next_line(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();

			if(line == textArea.getLineCount() - 1)
			{
				textArea.getToolkit().beep();
				return;
			}

			int magic = textArea.getMagicCaretPosition();
			if(magic == -1)
			{
				magic = textArea.offsetToX(line,
					caret - textArea.getLineStartOffset(line));
			}

			caret = textArea.getLineStartOffset(line + 1)
				+ textArea.xToOffset(line + 1,magic);
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
			textArea.setMagicCaretPosition(magic);
		}
	}

	public static class next_page implements ActionListener
	{
		private boolean select;

		public next_page(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int lineCount = textArea.getLineCount();
			int firstLine = textArea.getFirstLine();
			int visibleLines = textArea.getVisibleLines();
			int line = textArea.getCaretLine();

			firstLine += visibleLines;

			if(firstLine + visibleLines >= lineCount - 1)
				firstLine = lineCount - visibleLines;

			textArea.setFirstLine(firstLine);

			int caret = textArea.getLineStartOffset(
				Math.min(textArea.getLineCount() - 1,
				line + visibleLines));
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class next_word implements ActionListener
	{
		private boolean select;

		public next_word(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			caret -= lineStart;

			String lineText = textArea.getLineText(textArea
				.getCaretLine());

			if(caret == lineText.length())
			{
				if(lineStart + caret == textArea.getDocumentLength())
				{
					textArea.getToolkit().beep();
					return;
				}
				caret++;
			}
			else
			{

				char ch = lineText.charAt(caret);

				String noWordSep = (String)textArea.getDocument()
					.getProperty("noWordSep");
				if(noWordSep == null)
					noWordSep = "";
				boolean selectNoLetter = (!Character
					.isLetterOrDigit(ch)
					&& noWordSep.indexOf(ch) == -1);

				int wordEnd = lineText.length();
				for(int i = caret; i < lineText.length(); i++)
				{
					ch = lineText.charAt(i);
					if(selectNoLetter ^ (!Character
						.isLetterOrDigit(ch) &&
						noWordSep.indexOf(ch) == -1))
					{
						wordEnd = i;
						break;
					}
				}
				caret = wordEnd;
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					lineStart + caret);
			else
				textArea.setCaretPosition(lineStart + caret);
		}
	}

	public static class overwrite implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			textArea.setOverwriteEnabled(
				!textArea.isOverwriteEnabled());
		}
	}

	public static class prev_char implements ActionListener
	{
		private boolean select;

		public prev_char(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			if(caret == 0)
			{
				textArea.getToolkit().beep();
				return;
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					caret - 1);
			else
				textArea.setCaretPosition(caret - 1);
		}
	}

	public static class prev_line implements ActionListener
	{
		private boolean select;

		public prev_line(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();

			if(line == 0)
			{
				textArea.getToolkit().beep();
				return;
			}

			int magic = textArea.getMagicCaretPosition();
			if(magic == -1)
			{
				magic = textArea.offsetToX(line,
					caret - textArea.getLineStartOffset(line));
			}

			caret = textArea.getLineStartOffset(line - 1)
				+ textArea.xToOffset(line - 1,magic);
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
			textArea.setMagicCaretPosition(magic);
		}
	}

	public static class prev_page implements ActionListener
	{
		private boolean select;

		public prev_page(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int firstLine = textArea.getFirstLine();
			int visibleLines = textArea.getVisibleLines();
			int line = textArea.getCaretLine();

			if(firstLine < visibleLines)
				firstLine = visibleLines;

			textArea.setFirstLine(firstLine - visibleLines);

			int caret = textArea.getLineStartOffset(
				Math.max(0,line - visibleLines));
			if(select)
				textArea.select(textArea.getMarkPosition(),caret);
			else
				textArea.setCaretPosition(caret);
		}
	}

	public static class prev_word implements ActionListener
	{
		private boolean select;

		public prev_word(boolean select)
		{
			this.select = select;
		}

		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			int caret = textArea.getCaretPosition();
			int line = textArea.getCaretLine();
			int lineStart = textArea.getLineStartOffset(line);
			caret -= lineStart;

			String lineText = textArea.getLineText(textArea
				.getCaretLine());

			if(caret == 0)
			{
				if(lineStart == 0)
				{
					textArea.getToolkit().beep();
					return;
				}
				caret--;
			}
			else
			{
				char ch = lineText.charAt(caret - 1);

				String noWordSep = (String)textArea.getDocument()
					.getProperty("noWordSep");
				if(noWordSep == null)
					noWordSep = "";
				boolean selectNoLetter = (!Character
					.isLetterOrDigit(ch)
					&& noWordSep.indexOf(ch) == -1);

				int wordStart = 0;
				for(int i = caret - 1; i >= 0; i--)
				{
					ch = lineText.charAt(i);
					if(selectNoLetter ^ (!Character
						.isLetterOrDigit(ch) &&
						noWordSep.indexOf(ch) == -1))
					{
						wordStart = i + 1;
						break;
					}
				}
				caret = wordStart;
			}

			if(select)
				textArea.select(textArea.getMarkPosition(),
					lineStart + caret);
			else
				textArea.setCaretPosition(lineStart + caret);
		}
	}

	public static class toggle_rect implements ActionListener
	{
		public void actionPerformed(ActionEvent evt)
		{
			JEditTextArea textArea = getTextArea(evt);
			textArea.setSelectionRectangular(
				!textArea.isSelectionRectangular());
		}
	}
}

/*
 * ChangeLog:
 * $Log$
 * Revision 1.2  2005/03/17 21:51:13  davidmartinez
 * Turning into an eclipse project, global import optimize and warning-busting
 *
 * Revision 1.1.1.1  2001/09/07 02:47:32  davidmartinez
 * Initial Checkin of the Alpha tree
 *
 * Revision 1.1.1.1  1999/10/20 00:09:21  david
 * Initial Import
 *
 * Revision 1.2  1999/10/20 00:09:21  david
 * Ran Dos2Unix on all the .java files within the textarea package.
 *
 * Revision 1.1.1.1  1999/10/18 15:00:23  david
 * Initial Check-in
 *
 * Revision 1.6  1999/09/30 12:21:05  sp
 * No net access for a month... so here's one big jEdit 2.1pre1
 *
 */
