package test.hackerdude.devtools.db.sqlide.panels.movedata;

import com.hackerdude.devtools.db.sqlide.plugins.movedata.model.*;
import com.hackerdude.devtools.db.sqlide.plugins.movedata.DataMoveParser;
import junit.framework.*;
import java.io.*;
import java.util.*;

/**
 * Title:        JSqlIde
 * Description:  A Java SQL Integrated Development Environment
 * Copyright:    Copyright (c) David Martinez
 * Company:
 * @author David Martinez
 * @version 1.0
 */

public class DataMoveParserTest extends TestCase {

    public DataMoveParserTest() {
		super("testParser");
    }

	public DataMoveParserTest(String name) {
		super(name);
	}

	public void testParser() {

		MoveDataModel sampleModel = createSampleModel();
		DataMoveParser parser = new DataMoveParser(sampleModel);
		String coolStuff = parser.toXML();

	}

	private MoveDataModel createSampleModel() {
		MoveDataModel model = new MoveDataModel();

		DataSourceNode st = new DataSourceNode();
		st.setTableName("AddressBook");
		model.addInstruction(st);

		return model;
	}



}