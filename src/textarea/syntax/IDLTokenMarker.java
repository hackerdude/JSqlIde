/*
 * IDLTokenMarker.java - IDL token marker
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 1999 Juha Lindfors
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


/**
 * IDL token marker.
 *
 * @author Slava Pestov
 * @author Juha Lindfors
 * @version $Id$
 */
public class IDLTokenMarker extends CTokenMarker
{
	public IDLTokenMarker()
	{
		super(true,getKeywords());
	}

	public static KeywordMap getKeywords()
	{
		if(idlKeywords == null)
		{
			idlKeywords = new KeywordMap(false);

            idlKeywords.add("any",      Token.KEYWORD3);
            idlKeywords.add("attribute",Token.KEYWORD1);
            idlKeywords.add("boolean",  Token.KEYWORD3);
            idlKeywords.add("case",     Token.KEYWORD1);
            idlKeywords.add("char",     Token.KEYWORD3);
            idlKeywords.add("const",    Token.KEYWORD1);
            idlKeywords.add("context",  Token.KEYWORD1);
            idlKeywords.add("default",  Token.KEYWORD1);
            idlKeywords.add("double",   Token.KEYWORD3);
            idlKeywords.add("enum",     Token.KEYWORD3);
            idlKeywords.add("exception",Token.KEYWORD1);
            idlKeywords.add("FALSE",    Token.LITERAL2);
            idlKeywords.add("fixed",    Token.KEYWORD1);
            idlKeywords.add("float",    Token.KEYWORD3);
            idlKeywords.add("in",       Token.KEYWORD1);
            idlKeywords.add("inout",    Token.KEYWORD1);
            idlKeywords.add("interface",Token.KEYWORD1);
            idlKeywords.add("long",     Token.KEYWORD3);
            idlKeywords.add("module",   Token.KEYWORD1);
            idlKeywords.add("Object",   Token.KEYWORD3);
            idlKeywords.add("octet",    Token.KEYWORD3);
            idlKeywords.add("oneway",   Token.KEYWORD1);
            idlKeywords.add("out",      Token.KEYWORD1);
            idlKeywords.add("raises",   Token.KEYWORD1);
            idlKeywords.add("readonly", Token.KEYWORD1);
            idlKeywords.add("sequence", Token.KEYWORD3);
            idlKeywords.add("short",    Token.KEYWORD3);
            idlKeywords.add("string",   Token.KEYWORD3);
            idlKeywords.add("struct",   Token.KEYWORD3);
            idlKeywords.add("switch",   Token.KEYWORD1);
            idlKeywords.add("TRUE",     Token.LITERAL2);
            idlKeywords.add("typedef",  Token.KEYWORD3);
            idlKeywords.add("unsigned", Token.KEYWORD3);
            idlKeywords.add("union",    Token.KEYWORD3);
            idlKeywords.add("void",     Token.KEYWORD3);
            idlKeywords.add("wchar",    Token.KEYWORD3);
            idlKeywords.add("wstring",  Token.KEYWORD3);
		}
		return idlKeywords;
	}

	// private members
	private static KeywordMap idlKeywords;
}

/*
 * ChangeLog:
 * $Log$
 * Revision 1.2  2005/03/17 21:51:15  davidmartinez
 * Turning into an eclipse project, global import optimize and warning-busting
 *
 * Revision 1.1.1.1  2001/09/07 02:47:44  davidmartinez
 * Initial Checkin of the Alpha tree
 *
 * Revision 1.1.1.1  1999/10/20 00:09:22  david
 * Initial Import
 *
 * Revision 1.2  1999/10/20 00:09:22  david
 * Ran Dos2Unix on all the .java files within the textarea package.
 *
 * Revision 1.1.1.1  1999/10/18 15:00:24  david
 * Initial Check-in
 *
 * Revision 1.1  1999/10/03 03:47:16  sp
 * Minor stupidity, IDL mode
 * 
 */
