package com.hackerdude.devtools.db.sqlide.dataaccess;

import javax.xml.parsers.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;
import java.io.*;
import java.util.*;

/**
 * The parsing and generator of the Database Spec object has moved here
 * so that the DatabaseSpec can become a simple accessor/mutator class.
 */
public class DatabaseSpecSAXParser {

	public DatabaseSpec getDatabaseSpec(String fileName) throws Exception {
		FileInputStream fis = new FileInputStream(fileName);
//		InputSource isource = new InputSource(fis);
		DatabaseSpec spec = getDatabaseSpec(fis);
		return spec;
	}


	public DatabaseSpec getDatabaseSpec(InputStream is) throws Exception {
		DatabaseSpec spec = null;
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		DBSpecHandler handler = new DBSpecHandler();
		parser.parse(is, handler);
		return spec;
	}



	public class DBSpecHandler extends HandlerBase {

		String currentElement = null;
		Attributes currentAttributes;

		public void startElement(String qName, Attributes attributes) {
			currentElement = qName;
			currentAttributes = attributes;
		}

		public void endElement(String qName, String something, String somethingElse) {
			System.out.println("[DatabaseSpecSAXParser] End Element: "+qName);
		}

	}


	public static void main(String[] args) {
		DatabaseSpecSAXParser dbParser = new DatabaseSpecSAXParser();
		InputStream is = dbParser.getClass().getResourceAsStream("default.db.xml");
//		InputSource iSource = new InputSource(is);
		try {
		DatabaseSpec spec = dbParser.getDatabaseSpec(is);
		} catch (Exception exc) { exc.printStackTrace(); }



	}

}