package com.hackerdude.devtools.db.sqlide.plugins.movedata;

import org.xml.sax.*;
import com.hackerdude.devtools.db.sqlide.plugins.movedata.model.*;
import org.w3c.dom.*;
import org.apache.xerces.dom.*;
import org.apache.xerces.jaxp.*;
import org.apache.xml.serialize.*;
import java.io.*;

/**
 * This is a two way parser for MoveDataModel. It both interpret and generate
 * XML for MoveDataInstructions.
 */
public class DataMoveParser {

	MoveDataModel model;

	public final static String QUERY_TABLE = "table";
	public final static String QUERY_STTMT = "statement";
	public final static String QUERY_WHERE = "where";

	public DataMoveParser(MoveDataModel model) {
		this.model = model;
	}

	public String toXML() {
		DocumentImpl doc = new DocumentImpl();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String result;
		OutputFormat of = new OutputFormat();
		try {
		XMLSerializer serial = new XMLSerializer(os, of);
		AbstractScriptNode[] instructions = model.getInstructions();
		for ( int i=0; i<instructions.length; i++) {
			AbstractScriptNode current = instructions[i];
			Node node = current.getNode(doc);
			doc.appendChild(node);
		}
		serial.serialize(doc);
		result = os.toString();
		} catch ( IOException exc ) { result = "Could not generate XML document"; }
//
		return result;
	}

}