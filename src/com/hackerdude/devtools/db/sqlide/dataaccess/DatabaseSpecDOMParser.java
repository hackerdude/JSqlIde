package com.hackerdude.devtools.db.sqlide.dataaccess;

import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.*;
import java.util.*;
import  org.apache.xml.serialize.OutputFormat;
import  org.apache.xml.serialize.XMLSerializer;
import org.apache.xerces.dom.*;

/**
 * This class can parse Database Specs using a DOM.
 */
public class DatabaseSpecDOMParser {
	protected static final String XSPEC_SPEC_ROOTNODE   = "databasespec";
	protected static final String XSPEC_CONNECTION_NODE ="connection";
	protected static final String XSPEC_JDBC_NODE       ="jdbc";
	protected static final String XSPEC_URL_NODE        ="url";
	protected static final String XSPEC_PROPERTY_NODE   = "property";
	protected static final String XSPEC_PROPERTY_NAME   = "name";
	protected static final String XSPEC_PROPERTY_VALUE  = "value";
	protected static final String XSPEC_SQLIDE          = "sqlidespecific";
	/**
	 * The number of connections to create.
	 */
	protected static final String specConnections = "numconnections";
	protected static final String specBaseUrl     = "url";
	protected static final String specDriver      = "driver"; // This is the actual Jdbc driver
	protected static final String specDbIntfClass = "ideDriver";
	protected static final String specPoliteName  = "specname";
	protected static final String specUserName    = "username";
	protected static final String specHostName    = "hostname";
	protected static final String specDriverJar   = "jarfile"; // Where is the jar file that implements this driver?
	protected static final String specGetAvaildbs = "getavailabledbs";
	protected static final String specDefaultCatalog = "defaultcatalog";


	public DatabaseSpecDOMParser() {
	}


	public DatabaseSpec parseDatabaseSpec(InputStream is) throws IOException, SAXException {
		DatabaseSpec result =  new DatabaseSpec();
		InputSource iSource = new InputSource(is);
		org.apache.xerces.parsers.DOMParser p = new org.apache.xerces.parsers.DOMParser();
		p.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
		p.parse(iSource);
		is.close();
		Document d = p.getDocument();
		readConfiguration(d, result);
		return result;
	}



	private void readConfiguration(Document doc, DatabaseSpec destination ) throws SAXException {
		Node rootNode = doc.getFirstChild();
		while ( rootNode.getNodeType() != Node.ELEMENT_NODE ) {
			rootNode = rootNode.getNextSibling();
			if ( rootNode == null ) throw new SAXException("There must be a root element node.");
		}
		String rootNodeName = rootNode.getNodeName();
		if ( ! rootNodeName.equals(XSPEC_SPEC_ROOTNODE) ) throw new SAXException("Root Element must be named "+XSPEC_SPEC_ROOTNODE);
		destination.setPoliteName(rootNode.getAttributes().getNamedItem(XSPEC_PROPERTY_NAME).getNodeValue());
		NodeList nl = rootNode.getChildNodes();
		for ( int i=0; i<nl.getLength(); i++){
			Node topic = nl.item(i);
			String nodeName = topic.getNodeName();
			if ( nodeName.equalsIgnoreCase(XSPEC_CONNECTION_NODE)   ) {  parseConnection(topic, destination); }
			else if ( nodeName.equalsIgnoreCase(XSPEC_JDBC_NODE)    ) { parseJdbc(topic, destination); }
			else if ( nodeName.equalsIgnoreCase(XSPEC_SQLIDE)  ) { parseSQLIDE(topic, destination); }
		}
	}


   private void parseConnection( Node connectionNode, DatabaseSpec destination ) {
	  NamedNodeMap nm = connectionNode.getAttributes();
	  String sConnections = nm.getNamedItem(specConnections).getNodeValue();
	  NodeList nl = connectionNode.getChildNodes();
	  destination.setConnections(Integer.parseInt(sConnections));
	  Properties connectionProperties = new Properties();
	  for ( int i=0; i<nl.getLength(); i++) {
		 Node current = nl.item(i);
		 String currentNodeName = current.getNodeName();
		 String currentNodeValue = current.getNodeValue();
		 if ( currentNodeName.equals(XSPEC_PROPERTY_NODE) ) {
			NamedNodeMap al = current.getAttributes();
			for ( int j=0; j<al.getLength(); j++) {
				String propertyName = al.item(j).getNodeName();
				String propertyValue = al.item(j).getNodeValue();
				connectionProperties.setProperty(propertyName, propertyValue);
			}
		 }
	  }
	  destination.setConnectionProperties(connectionProperties);
   }


/*	private void parseGeneral( Node generalNode ) {
	   NamedNodeMap nm = generalNode.getAttributes();
	   String sGeneral = nm.getNamedItem(specPoliteName).getNodeValue();
	   setPoliteName(sGeneral);
	}*/

	private void parseJdbc(Node jdbcNode, DatabaseSpec spec) throws SAXException {
		NodeList children = jdbcNode.getChildNodes();
		String driver = null;
		String url    = null;
		Node jarFileNode = jdbcNode.getAttributes().getNamedItem("jarfile");

		String jarFile = jarFileNode!=null?jarFileNode.getNodeValue():null;
		for ( int i=0; i<children.getLength(); i++ ) {
			Node aNode = children.item(i);
			if ( aNode.getNodeType() != Node.COMMENT_NODE ) {
				String aNodeName  = aNode.getNodeName();
				String aNodeValue = null;
				short nodeType = aNode.getNodeType();
				if ( nodeType == Node.ELEMENT_NODE ) {
					Node textNode = aNode.getFirstChild();
					if ( textNode != null && textNode.getNodeType() != Node.TEXT_NODE ) throw new SAXException("In element "+aNodeName+" - Elements in the JDBC node must be of the form <name>value</name>");
					aNodeValue = textNode.getNodeValue();
				}
				if ( aNodeName.equals(specDriver) ) driver = aNodeValue;
				if ( aNodeName.equals(specBaseUrl) ) url = aNodeValue;
			}
		}
		spec.setDriverName(driver);
		spec.setURL(url);
		spec.setJarFileName(jarFile);
	}

	private void parseSQLIDE(Node ideNode, DatabaseSpec spec) throws SAXException {
	   NodeList children = ideNode.getChildNodes();
		String _userName  = null;
		String _hostName  = null;
		String _defaultDb = null;
		for ( int i=0; i<children.getLength(); i++ ) {
			Node aNode = children.item(i);
			if ( aNode.getNodeType() != Node.COMMENT_NODE ) {
				String aNodeName  = aNode.getNodeName();
				String aNodeValue = null;
				short nodeType = aNode.getNodeType();
				if ( nodeType == Node.ELEMENT_NODE ) {
					Node textNode = aNode.getFirstChild();
					if ( textNode != null && textNode.getNodeType() != Node.TEXT_NODE ) throw new SAXException("In element "+aNodeName+" - Elements in the sqlidespecific node must be of the form <name>value</name>");
					if ( textNode != null ) aNodeValue = textNode.getNodeValue();
				}
				if ( aNodeName.equals(specUserName) ) _userName = aNodeValue;
				if ( aNodeName.equals(specHostName) ) _hostName = aNodeValue;
				if ( aNodeName.equals(specDefaultCatalog) ) _defaultDb = aNodeValue;


			}
		}
		if ( _userName == null ) throw new SAXException("On "+XSPEC_SQLIDE+" node, the username is not specified.");
		if ( _hostName == null ) throw new SAXException("On "+XSPEC_SQLIDE+" node, the Hostaname is not specified.");
		//if ( _defaultDb == null ) throw new SAXException("On "+XSPEC_SQLIDE+" node, the Default catalog is not specified");
		spec.setUserName(_userName);
		spec.setHostName(_hostName);
		spec.setDefaultCatalog(_defaultDb);
		String ideDriver = ideNode.getAttributes().getNamedItem(specDbIntfClass).getNodeValue();
		spec.setDbIntfClassName(ideDriver);
	}



	/**
	 * Stores the defaults on disk
	 */
	 /** @todo Make sure connections section and sqlide section are being accounted for.
	  *  Also the only general property was the polite name. This is now elsewhere. put it thw
	  *  way it's expected.*/
   public void save(DatabaseSpec spec) {
		DocumentImpl xmlDatabaseSpec = new DocumentImpl();

		Element rootNode    = xmlDatabaseSpec.createElement(XSPEC_SPEC_ROOTNODE);
		rootNode.setAttribute(XSPEC_PROPERTY_NAME, spec.getPoliteName());
//		Element generalNode = xmlDatabaseSpec.createElement(XSPEC_GENERAL);
		Element jdbcNode    = xmlDatabaseSpec.createElement(XSPEC_JDBC_NODE);
		Element connectionNode = xmlDatabaseSpec.createElement(XSPEC_CONNECTION_NODE);
		connectionNode.setAttribute(specConnections, Integer.toString(spec.getConnections()));
		jdbcNode.setAttribute("jarfile", spec.getJarFileName());
		// Save the connection properties
		Iterator propsIter = spec.getConnectionProperties().keySet().iterator();
		while ( propsIter.hasNext() ) {
			String keyName = (String)propsIter.next();
			String value   = (String)spec.getConnectionProperties().get(keyName);
			Element connPropNode = xmlDatabaseSpec.createElement(XSPEC_PROPERTY_NODE);
			connPropNode.setAttribute(keyName, value);
			connectionNode.appendChild(connPropNode);
		}


		Element urlNode     = xmlDatabaseSpec.createElement(XSPEC_URL_NODE);

		// SQLIDE node, with User, hostname and default catalog.
		Element sqlideNode = xmlDatabaseSpec.createElement(XSPEC_SQLIDE);
		sqlideNode.setAttribute(specDbIntfClass, spec.getDbIntfClassName());
		Element hostNameElement = xmlDatabaseSpec.createElement(specHostName);
		hostNameElement.appendChild(xmlDatabaseSpec.createTextNode(spec.getHostName()));
		sqlideNode.appendChild(hostNameElement);
		Element userNameElement = xmlDatabaseSpec.createElement(specUserName);
		userNameElement.appendChild(xmlDatabaseSpec.createTextNode(spec.getUserName()));

		sqlideNode.appendChild(userNameElement);
		Element defaultCatalogElement = xmlDatabaseSpec.createElement(specDefaultCatalog);
		defaultCatalogElement.appendChild(xmlDatabaseSpec.createTextNode(spec.getDefaultCatalog()));
		sqlideNode.appendChild(defaultCatalogElement);

		Element driverElement = xmlDatabaseSpec.createElement(specDriver);
		Text driverName    = xmlDatabaseSpec.createTextNode(spec.getDriverName());
		driverElement.appendChild(driverName);
		Element urlElement = xmlDatabaseSpec.createElement(specBaseUrl);
		Text urlName       = xmlDatabaseSpec.createTextNode(spec.getURL());
		urlElement.appendChild(urlName);

		jdbcNode.appendChild(driverElement);
		jdbcNode.appendChild(urlElement);

		jdbcNode.setAttribute(specDriverJar, spec.getJarFileName());
//		rootNode.appendChild(generalNode);
		rootNode.appendChild(jdbcNode);
		rootNode.appendChild(sqlideNode);
		rootNode.appendChild(connectionNode);
		xmlDatabaseSpec.appendChild(rootNode);

		try {
			OutputFormat    format  = new OutputFormat( xmlDatabaseSpec );   //Serialize DOM
			PrintWriter     writer = new PrintWriter( new FileWriter(spec.getFileName()));
			XMLSerializer   serial = new XMLSerializer( writer, format );
			serial.asDOMSerializer();                            // As a DOM Serializer

			serial.serialize( xmlDatabaseSpec.getDocumentElement() );

		} catch (IOException exc) {
			exc.printStackTrace();
		}

	}



}