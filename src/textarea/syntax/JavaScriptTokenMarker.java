/*
 * JavaScriptTokenMarker.java - JavaScript token marker
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
package textarea.syntax;

import javax.swing.text.Segment;

/**
 * JavaScript token marker.
 *
 * @author Slava Pestov
 * @version $Id$
 */
public class JavaScriptTokenMarker extends CTokenMarker
{
	public JavaScriptTokenMarker()
	{
		super(false,getKeywords());
	}

	public static KeywordMap getKeywords()
	{
		if(javaScriptKeywords == null)
		{
			javaScriptKeywords = new KeywordMap(false);
			javaScriptKeywords.add("function",Token.KEYWORD3);
			javaScriptKeywords.add("var",Token.KEYWORD3);
			javaScriptKeywords.add("else",Token.KEYWORD1);
			javaScriptKeywords.add("for",Token.KEYWORD1);
			javaScriptKeywords.add("if",Token.KEYWORD1);
			javaScriptKeywords.add("in",Token.KEYWORD1);
			javaScriptKeywords.add("new",Token.KEYWORD1);
			javaScriptKeywords.add("return",Token.KEYWORD1);
			javaScriptKeywords.add("while",Token.KEYWORD1);
			javaScriptKeywords.add("with",Token.KEYWORD1);
			javaScriptKeywords.add("break",Token.KEYWORD1);
			javaScriptKeywords.add("case",Token.KEYWORD1);
			javaScriptKeywords.add("continue",Token.KEYWORD1);
			javaScriptKeywords.add("default",Token.KEYWORD1);
			javaScriptKeywords.add("false",Token.LABEL);
			javaScriptKeywords.add("this",Token.LABEL);
			javaScriptKeywords.add("true",Token.LABEL);
		}
		return javaScriptKeywords;
	}

	// private members
	private static KeywordMap javaScriptKeywords;
}

/*
 * ChangeLog:
 * $Log$
 * Revision 1.1  2001/09/07 02:47:44  davidmartinez
 * Initial revision
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
 * Revision 1.2  1999/06/05 00:22:58  sp
 * LGPL'd syntax package
 *
 * Revision 1.1  1999/03/13 09:11:46  sp
 * Syntax code updates, code cleanups
 *
 */
