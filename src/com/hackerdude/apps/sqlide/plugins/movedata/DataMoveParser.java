package com.hackerdude.apps.sqlide.plugins.movedata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Node;

import com.hackerdude.apps.sqlide.plugins.movedata.model.AbstractScriptNode;
import com.hackerdude.apps.sqlide.plugins.movedata.model.MoveDataModel;

/**
 * This is a two way parser for MoveDataModel. It both interpret and generate
 * XML for MoveDataInstructions.
 *
 * 
 * @author David Martinez
 * @version 1.0
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
