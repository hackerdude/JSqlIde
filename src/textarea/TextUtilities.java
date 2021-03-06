/*
 * TextUtilities.java - Utility functions used by the text area classes
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

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Class with several utility functions used by the text area component.
 * @author Slava Pestov
 * @version $Id$
 */
public class TextUtilities
{
	/**
	 * Returns the offset of the bracket matching the one at the
	 * specified offset of the document, or -1 if the bracket is
	 * unmatched (or if the character is not a bracket).
	 * @param doc The document
	 * @param offset The offset
	 * @exception BadLocationException If an out-of-bounds access
	 * was attempted on the document text
	 */
	public static int findMatchingBracket(Document doc, int offset)
		throws BadLocationException
	{
		if(doc.getLength() == 0)
			return -1;
		char c = doc.getText(offset,1).charAt(0);
		char cprime; // c` - corresponding character
		boolean direction; // true = back, false = forward

		switch(c)
		{
		case '(': cprime = ')'; direction = false; break;
		case ')': cprime = '('; direction = true; break;
		case '[': cprime = ']'; direction = false; break;
		case ']': cprime = '['; direction = true; break;
		case '{': cprime = '}'; direction = false; break;
		case '}': cprime = '{'; direction = true; break;
		default: return -1;
		}

		int count;

		// How to merge these two cases is left as an exercise
		// for the reader.

		// Go back or forward
		if(direction)
		{
			// Count is 1 initially because we have already
			// `found' one closing bracket
			count = 1;

			// Get text[0,offset-1];
			String text = doc.getText(0,offset);

			// Scan backwards
			for(int i = offset - 1; i >= 0; i--)
			{
				// If text[i] == c, we have found another
				// closing bracket, therefore we will need
				// two opening brackets to complete the
				// match.
				char x = text.charAt(i);
				if(x == c)
					count++;

				// If text[i] == cprime, we have found a
				// opening bracket, so we return i if
				// --count == 0
				else if(x == cprime)
				{
					if(--count == 0)
						return i;
				}
			}
		}
		else
		{
			// Count is 1 initially because we have already
			// `found' one opening bracket
			count = 1;

			// So we don't have to + 1 in every loop
			offset++;

			// Number of characters to check
			int len = doc.getLength() - offset;

			// Get text[offset+1,len];
			String text = doc.getText(offset,len);

			// Scan forwards
			for(int i = 0; i < len; i++)
			{
				// If text[i] == c, we have found another
				// opening bracket, therefore we will need
				// two closing brackets to complete the
				// match.
				char x = text.charAt(i);

				if(x == c)
					count++;

				// If text[i] == cprime, we have found an
				// closing bracket, so we return i if
				// --count == 0
				else if(x == cprime)
				{
					if(--count == 0)
						return i + offset;
				}
			}
		}

		// Nothing found
		return -1;
	}
}

/*
 * ChangeLog:
 * $Log$
 * Revision 1.2  2005/03/17 21:51:13  davidmartinez
 * Turning into an eclipse project, global import optimize and warning-busting
 *
 * Revision 1.1.1.1  2001/09/07 02:47:41  davidmartinez
 * Initial Checkin of the Alpha tree
 *
 * Revision 1.1.1.1  1999/10/20 00:09:22  david
 * Initial Import
 *
 * Revision 1.2  1999/10/20 00:09:22  david
 * Ran Dos2Unix on all the .java files within the textarea package.
 *
 * Revision 1.1.1.1  1999/10/18 15:00:23  david
 * Initial Check-in
 *
 * Revision 1.2  1999/07/16 23:45:49  sp
 * 1.7pre6 BugFree version
 *
 * Revision 1.1  1999/06/29 09:03:18  sp
 * oops, forgot to add TextUtilities.java
 *
 */
