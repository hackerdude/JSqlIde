/*
 * SQLTokenMarker.java - Generic SQL token marker
 * Copyright (C) 1999 mike dillon
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
 */
package textarea.syntax;

import javax.swing.text.Segment;

/**
 * SQL token marker.
 *
 * @author mike dillon
 * @version $Id$
 */
public class SQLTokenMarker extends TokenMarker
{
	private int offset, lastOffset, lastKeyword, length;

	// public members
	public SQLTokenMarker(KeywordMap k)
	{
		this(k, false);
	}

	public SQLTokenMarker(KeywordMap k, boolean tsql)
	{
		keywords = k;
		isTSQL = tsql;
	}

	public byte markTokensImpl(byte token, Segment line, int lineIndex)
	{
		offset = lastOffset = lastKeyword = line.offset;
		length = line.count + offset;

loop:
		for(int i = offset; i < length; i++)
		{
			switch(line.array[i])
			{
			case '*':
				if(token == Token.COMMENT1 && length - i >= 1 && line.array[i+1] == '/')
				{
					token = Token.NULL;
					i++;
					addToken((i + 1) - lastOffset,Token.COMMENT1);
					lastOffset = i + 1;
				}
				else if (token == Token.NULL)
				{
					searchBack(line, i);
					addToken(1,Token.OPERATOR);
					lastOffset = i + 1;
				}
				break;
			case '[':
				if(token == Token.NULL)
				{
					searchBack(line, i);
					token = Token.LITERAL1;
					literalChar = '[';
					lastOffset = i;
				}
				break;
			case ']':
				if(token == Token.LITERAL1 && literalChar == '[')
				{
					token = Token.NULL;
					literalChar = 0;
					addToken((i + 1) - lastOffset,Token.LITERAL1);
					lastOffset = i + 1;
				}
				break;
			case '.': case ',': case '(': case ')':
				if (token == Token.NULL) {
					searchBack(line, i);
					addToken(1, Token.NULL);
					lastOffset = i + 1;
				}
				break;
			case '+': case '%': case '&': case '|': case '^':
			case '~': case '<': case '>': case '=':
				if (token == Token.NULL) {
					searchBack(line, i);
					addToken(1,Token.OPERATOR);
					lastOffset = i + 1;
				}
				break;
			case ' ': case '\t':
				if (token == Token.NULL) {
					searchBack(line, i, false);
				}
				break;
			case ':':
				if(token == Token.NULL)
				{
					addToken((i+1) - lastOffset,Token.LABEL);
					lastOffset = i + 1;
				}
				break;
			case '/':
				if(token == Token.NULL)
				{
					if (length - i >= 2 && line.array[i + 1] == '*')
					{
						searchBack(line, i);
						token = Token.COMMENT1;
						lastOffset = i;
						i++;
					}
					else
					{
						searchBack(line, i);
						addToken(1,Token.OPERATOR);
						lastOffset = i + 1;
					}
				}
				break;
			case '-':
				if(token == Token.NULL)
				{
					if (length - i >= 2 && line.array[i+1] == '-')
					{
						searchBack(line, i);
						addToken(length - i,Token.COMMENT1);
						lastOffset = length;
						break loop;
					}
					else
					{
						searchBack(line, i);
						addToken(1,Token.OPERATOR);
						lastOffset = i + 1;
					}
				}
				break;
			case '!':
				if(isTSQL && token == Token.NULL && length - i >= 2 &&
				(line.array[i+1] == '=' || line.array[i+1] == '<' || line.array[i+1] == '>'))
				{
					searchBack(line, i);
					addToken(1,Token.OPERATOR);
					lastOffset = i + 1;
				}
				break;
			case '"': case '\'':
				if(token == Token.NULL)
				{
					token = Token.LITERAL1;
					literalChar = line.array[i];
					addToken(i - lastOffset,Token.NULL);
					lastOffset = i;
				}
				else if(token == Token.LITERAL1 && literalChar == line.array[i])
				{
					token = Token.NULL;
					literalChar = 0;
					addToken((i + 1) - lastOffset,Token.LITERAL1);
					lastOffset = i + 1;
				}
				break;
			default:
				break;
			}
		}
		if(token == Token.NULL)
			searchBack(line, length, false);
		if(lastOffset != length)
			addToken(length - lastOffset,token);
		return token;
	}

	// protected members
	protected boolean isTSQL = false;

	// private members
	private KeywordMap keywords;
	private char literalChar = 0;

	private void searchBack(Segment line, int pos)
	{
		searchBack(line, pos, true);
	}

	private void searchBack(Segment line, int pos, boolean padNull)
	{
		int len = pos - lastKeyword;
		byte id = keywords.lookup(line,lastKeyword,len);
		if(id != Token.NULL)
		{
			if(lastKeyword != lastOffset)
				addToken(lastKeyword - lastOffset,Token.NULL);
			addToken(len,id);
			lastOffset = pos;
		}
		lastKeyword = pos + 1;
		if (padNull && lastOffset < pos)
			addToken(pos - lastOffset, Token.NULL);
	}



	public static KeywordMap getKeywords()
	{
		if(SQLKeywords == null)
		{
		    
		    // Flow-Control

		    // Verbage.
		    SQLKeywords = new KeywordMap(true);
		    SQLKeywords.add("CREATE",Token.KEYWORD1);
		    SQLKeywords.add("DROP",Token.KEYWORD1);
		    SQLKeywords.add("CHECKPOINT",Token.KEYWORD1);
		    SQLKeywords.add("ROLLBACK",Token.KEYWORD1);
		    SQLKeywords.add("COMMIT",Token.KEYWORD1);
		    SQLKeywords.add("OPEN",Token.KEYWORD1);

		    SQLKeywords.add("FETCH",Token.KEYWORD1);
		    SQLKeywords.add("CLOSE",Token.KEYWORD1);
		    SQLKeywords.add("ALLOCATE",Token.KEYWORD1);
		    SQLKeywords.add("DEALLOCATE",Token.KEYWORD1);
		    SQLKeywords.add("DECLARE",Token.KEYWORD1);
		    SQLKeywords.add("DUMP",Token.KEYWORD1);
		    SQLKeywords.add("EXECUTE",Token.KEYWORD1);
		    SQLKeywords.add("GRANT",Token.KEYWORD1);
		    SQLKeywords.add("DENY",Token.KEYWORD1);
		    SQLKeywords.add("INSERT",Token.KEYWORD1);
		    SQLKeywords.add("SELECT",Token.KEYWORD1);
		    SQLKeywords.add("DELETE",Token.KEYWORD1);
		    SQLKeywords.add("UPDATE",Token.KEYWORD1);
		    SQLKeywords.add("KILL",Token.KEYWORD1);
		    SQLKeywords.add("LOAD",Token.KEYWORD1);
		    SQLKeywords.add("SAVE",Token.KEYWORD1);
		    SQLKeywords.add("PRINT",Token.KEYWORD1);
		    SQLKeywords.add("RAISE",Token.KEYWORD1);
		    SQLKeywords.add("RAISERROR",Token.KEYWORD1);
		    SQLKeywords.add("RECONFIGURE",Token.KEYWORD1);
		    SQLKeywords.add("REVOKE",Token.KEYWORD1);
		    SQLKeywords.add("SET",Token.KEYWORD1);
		    SQLKeywords.add("SETUSER",Token.KEYWORD1);
		    SQLKeywords.add("SHUTDOWN",Token.KEYWORD1);
		    SQLKeywords.add("TRUNCATE",Token.KEYWORD1);
		    SQLKeywords.add("READTEXT",Token.KEYWORD1);
		    SQLKeywords.add("UPDATETEXT",Token.KEYWORD1);
		    SQLKeywords.add("WRITETEXT",Token.KEYWORD1);
		    
		    // Functions

		    //  Nouns
                    SQLKeywords.add("TABLE", Token.LITERAL1);
		    SQLKeywords.add("DATABASE", Token.LITERAL1);
		    SQLKeywords.add("DEFAULT", Token.LITERAL1);
		    SQLKeywords.add("INDEX", Token.LITERAL1);
		    SQLKeywords.add("PROCEDURE", Token.LITERAL1);
		    SQLKeywords.add("DELAY", Token.LITERAL1);
		    SQLKeywords.add("TIME", Token.LITERAL1);
		    SQLKeywords.add("TRANSACTION", Token.LITERAL1);
		    SQLKeywords.add("RULE", Token.LITERAL1);
		    SQLKeywords.add("TRIGGER", Token.LITERAL1);
		    SQLKeywords.add("VIEW", Token.LITERAL1);
		    SQLKeywords.add("CURSOR", Token.LITERAL1);
		    SQLKeywords.add("DISK", Token.LITERAL1);
		    SQLKeywords.add("DEFAULT", Token.LITERAL1);
		    SQLKeywords.add("ALL", Token.LITERAL1);
		    SQLKeywords.add("PUBLIC", Token.LITERAL1);
		    SQLKeywords.add("ERROR", Token.LITERAL1);
		    SQLKeywords.add("RIGHT", Token.LITERAL1);
		    SQLKeywords.add("LEFT", Token.LITERAL1);
		    SQLKeywords.add("TRUE", Token.LITERAL1);
		    SQLKeywords.add("FALSE", Token.LITERAL1);
		    
		    // Operators

		    // Data Types
		    SQLKeywords.add("binary", Token.KEYWORD3);
		    SQLKeywords.add("varbinary",Token.KEYWORD3);
		    SQLKeywords.add("char",Token.KEYWORD3);
		    SQLKeywords.add("varchar",Token.KEYWORD3);
		    SQLKeywords.add("datetime",Token.KEYWORD3);
		    SQLKeywords.add("smalldatetime",Token.KEYWORD3);
		    SQLKeywords.add("decimal",Token.KEYWORD3);
		    SQLKeywords.add("dec",Token.KEYWORD3);
		    SQLKeywords.add("numeric",Token.KEYWORD3);
		    SQLKeywords.add("float",Token.KEYWORD3);
		    SQLKeywords.add("real",Token.KEYWORD3);
		    SQLKeywords.add("int",Token.KEYWORD3);
		    SQLKeywords.add("integer",Token.KEYWORD3);
		    SQLKeywords.add("smallint",Token.KEYWORD3);
		    SQLKeywords.add("tinyint",Token.KEYWORD3);
		    SQLKeywords.add("money",Token.KEYWORD3);
		    SQLKeywords.add("smallmoney",Token.KEYWORD3);
		    SQLKeywords.add("bit",Token.KEYWORD3);
		    SQLKeywords.add("timestamp",Token.KEYWORD3);
		    SQLKeywords.add("text",Token.KEYWORD3);
		    SQLKeywords.add("image",Token.KEYWORD3);
		    SQLKeywords.add("varying",Token.KEYWORD3);
		    SQLKeywords.add("character",Token.KEYWORD3);
		    SQLKeywords.add("blob",Token.KEYWORD3);
		   
			
		}
		return SQLKeywords;
	}

	// private members
	private static KeywordMap SQLKeywords;


}

/*
 * ChangeLog:
 * $Log$
 * Revision 1.1  2001/09/07 02:47:50  davidmartinez
 * Initial revision
 *
 * Revision 1.1.1.1  1999/10/19 23:59:39  david
 * Initial Import
 *
 * Revision 1.3  1999/10/19 23:59:39  david
 * Added some more sql keyword groups.
 *
 * Revision 1.2  1999/10/19 23:31:27  david
 * Dos2Unixed it
 *
 * Revision 1.1  1999/10/19 23:28:51  david
 * Initial revision
 *
 * Revision 1.1.1.1  1999/10/18 15:00:24  david
 * Initial Check-in
 *
 * Revision 1.6  1999/04/19 05:38:20  sp
 * Syntax API changes
 *
 * Revision 1.5  1999/03/15 03:40:23  sp
 * Search and replace updates, TSQL mode/token marker updates
 *
 * Revision 1.4  1999/03/13 00:09:07  sp
 * Console updates, uncomment removed cos it's too buggy, cvs log tags added
 *
 * Revision 1.3  1999/03/12 23:51:00  sp
 * Console updates, uncomment removed cos it's too buggy, cvs log tags added
 *
 */
