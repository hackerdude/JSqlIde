package com.hackerdude.apps.sqlide.dataaccess;

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
public class ConnectionConfigDOMParser {
	protected static final String XSPEC_SPEC_ROOTNODE   = "databasespec";
	protected static final String XSPEC_CONNECTION_NODE ="connection";
	protected static final String XSPEC_JDBC_NODE       ="jdbc";
	protected static final String XSPEC_URL_NODE        ="url";
	protected static final String XSPEC_PROPERTY_NODE   = "property";
	protected static final String XSPEC_PROPERTY_NAME   = "name";
	protected static final String XSPEC_PROPERTY_VALUE  = "value";
	protected static final String XSPEC_SQLIDE          = "sqlidespecific";
	protected static final String XSPEC_CLASSPATH       = "classpath";


	/**
	 * The number of connections to create.
	 */
	protected static final String ATTRIBUTE_NUM_CONNECTIONS = "numconnections";
	protected static final String ATTRIBUTE_IDE_DRIVER = "ideDriver";

	protected static final String ELEMENT_BASE_URL     = "url";
	protected static final String ELEMENT_DRIVER      = "driver"; // This is the actual Jdbc driver
	protected static final String ELEMENT_USER_NAME    = "username";
	protected static final String ELEMENT_HOST_NAME    = "hostname";
	protected static final String ELEMENT_JAR_FILE   = "jarfile"; // Where is the jar file that implements this driver?
	protected static final String ELEMENT_DEFAULT_CATALOG = "defaultcatalog";

	protected static final String ELEMENT_SQL_GENERATION = "sqlgeneration";
	protected static final String ATTRIBUTE_SUPPORTS_DOT_NOTATION = "dotnotation";

	public ConnectionConfigDOMParser() {
	}


	public ConnectionConfig parseDatabaseSpec(InputStream is) throws IOException, SAXException {
		ConnectionConfig result =  new ConnectionConfig();
		InputSource iSource = new InputSource(is);
		org.apache.xerces.parsers.DOMParser p = new org.apache.xerces.parsers.DOMParser();
		p.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", false);
		p.parse(iSource);
		is.close();
		Document d = p.getDocument();
		readConfiguration(d, result);
		return result;
	}



	private void readConfiguration(Document doc, ConnectionConfig destination ) throws SAXException {
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
			else if ( nodeName.equalsIgnoreCase(XSPEC_CLASSPATH) ) { parseClassPathNode(topic, destination); }
			else if ( nodeName.equals(ELEMENT_SQL_GENERATION) ) { parseSQLGenerationNode(topic, destination); }
		}
	}

	private void parseSQLGenerationNode(Node sqlGenerationNode, ConnectionConfig destination ) {
		NamedNodeMap attr = sqlGenerationNode.getAttributes();
		Node supportsDotNotationNode = attr.getNamedItem(ATTRIBUTE_SUPPORTS_DOT_NOTATION);
		if ( supportsDotNotationNode == null ) return;
		String supportsDotNotation = supportsDotNotationNode.getNodeValue();
		destination.setSupportsDotNotation(Boolean.valueOf(supportsDotNotation).booleanValue());
	}


   private void parseConnection( Node connectionNode, ConnectionConfig destination ) {
	  NamedNodeMap nm = connectionNode.getAttributes();
	  String sConnections = nm.getNamedItem(ATTRIBUTE_NUM_CONNECTIONS).getNodeValue();
	  NodeList nl = connectionNode.getChildNodes();
	  destination.setMaxConnections(Integer.parseInt(sConnections));
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

	private void parseJdbc(Node jdbcNode, ConnectionConfig spec) throws SAXException {
		NodeList children = jdbcNode.getChildNodes();
		String driver = null;
		String url    = null;
		Node jarFileNode = jdbcNode.getAttributes().getNamedItem(ELEMENT_JAR_FILE);

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
				if ( aNodeName.equals(ELEMENT_DRIVER) ) driver = aNodeValue;
				if ( aNodeName.equals(ELEMENT_BASE_URL) ) url = aNodeValue;
			}
		}
		spec.setDriverClassName(driver);
		spec.setJDBCURL(url);
	}


	private void parseClassPathNode(Node classpathNode, ConnectionConfig spec) throws SAXException {
		NodeList children = classpathNode.getChildNodes();
		int itemLength = children.getLength();
		ArrayList arrayList = new ArrayList();
		for ( int i=0; i<itemLength; i++ ) {
			Node thisChild = children.item(i);
			if ( thisChild.getNodeName().equals(ELEMENT_JAR_FILE) ) {
				Node jarFileNode = thisChild.getFirstChild();
				String jarFile = jarFileNode.getNodeValue();
				arrayList.add(jarFile);
			}
		}
		String[] files = new String[arrayList.size()];
		files = (String[])arrayList.toArray(files);
		spec.setJarFileName(files);
	}

	private void parseSQLIDE(Node ideNode, ConnectionConfig spec) throws SAXException {
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
				if ( aNodeName.equals(ELEMENT_USER_NAME) ) _userName = aNodeValue;
				if ( aNodeName.equals(ELEMENT_HOST_NAME) ) _hostName = aNodeValue;
				if ( aNodeName.equals(ELEMENT_DEFAULT_CATALOG) ) _defaultDb = aNodeValue;


			}
		}
		if ( _userName == null ) throw new SAXException("On "+XSPEC_SQLIDE+" node, the username is not specified.");
		if ( _hostName == null ) throw new SAXException("On "+XSPEC_SQLIDE+" node, the Hostaname is not specified.");
		//if ( _defaultDb == null ) throw new SAXException("On "+XSPEC_SQLIDE+" node, the Default catalog is not specified");
		spec.setUserName(_userName);
		spec.setHostName(_hostName);
		spec.setDefaultCatalog(_defaultDb);
		String ideDriver = ideNode.getAttributes().getNamedItem(ATTRIBUTE_IDE_DRIVER).getNodeValue();
		spec.setDbIntfClassName(ideDriver);
	}



	/**
	 * Stores the defaults on disk
	 */
	 /** @todo Make sure connections section and sqlide section are being accounted for.
	  *  Also the only general property was the polite name. This is now elsewhere. put it thw
	  *  way it's expected.*/
   public void save(ConnectionConfig spec) {
		DocumentImpl xmlDatabaseSpec = new DocumentImpl();

		Element rootNode    = xmlDatabaseSpec.createElement(XSPEC_SPEC_ROOTNODE);
		rootNode.setAttribute(XSPEC_PROPERTY_NAME, spec.getPoliteName());
//		Element generalNode = xmlDatabaseSpec.createElement(XSPEC_GENERAL);
		Element jdbcNode    = xmlDatabaseSpec.createElement(XSPEC_JDBC_NODE);
		Element connectionNode = xmlDatabaseSpec.createElement(XSPEC_CONNECTION_NODE);
		Element classPathNode = xmlDatabaseSpec.createElement(XSPEC_CLASSPATH);
		Element sqlGenerationNode = xmlDatabaseSpec.createElement(ELEMENT_SQL_GENERATION);
		sqlGenerationNode.setAttribute(ATTRIBUTE_SUPPORTS_DOT_NOTATION, spec.isSupportsDotNotation()?Boolean.TRUE.toString():Boolean.FALSE.toString());
		connectionNode.setAttribute(ATTRIBUTE_NUM_CONNECTIONS, Integer.toString(spec.getMaxConnections()));

//		jdbcNode.setAttribute(specDriverJar, spec.getJarFileName());
		// Save the connection properties
		Iterator propsIter = spec.getConnectionProperties().keySet().iterator();
		while ( propsIter.hasNext() ) {
			String keyName = (String)propsIter.next();
			String value   = (String)spec.getConnectionProperties().get(keyName);
			Element connPropNode = xmlDatabaseSpec.createElement(XSPEC_PROPERTY_NODE);
			connPropNode.setAttribute(keyName, value);
			connectionNode.appendChild(connPropNode);
		}

		String[] jarFiles = spec.getJarFileNames();
		if ( jarFiles != null && jarFiles.length> 0 ) {
			for ( int i=0; i<jarFiles.length; i++) {
				Node pathElementNode = xmlDatabaseSpec.createElement(ELEMENT_JAR_FILE);
				Node pathText = xmlDatabaseSpec.createTextNode(jarFiles[i]);
				pathElementNode.appendChild(pathText);
				classPathNode.appendChild(pathElementNode);
			}
			rootNode.appendChild(classPathNode);
		}
		Element urlNode     = xmlDatabaseSpec.createElement(XSPEC_URL_NODE);

		// SQLIDE node, with User, hostname and default catalog.
		Element sqlideNode = xmlDatabaseSpec.createElement(XSPEC_SQLIDE);
		sqlideNode.setAttribute(ATTRIBUTE_IDE_DRIVER, spec.getDbIntfClassName());
		Element hostNameElement = xmlDatabaseSpec.createElement(ELEMENT_HOST_NAME);
		hostNameElement.appendChild(xmlDatabaseSpec.createTextNode(spec.getHostName()));
		sqlideNode.appendChild(hostNameElement);
		Element userNameElement = xmlDatabaseSpec.createElement(ELEMENT_USER_NAME);
		userNameElement.appendChild(xmlDatabaseSpec.createTextNode(spec.getUserName()));

		sqlideNode.appendChild(userNameElement);
		Element defaultCatalogElement = xmlDatabaseSpec.createElement(ELEMENT_DEFAULT_CATALOG);
		defaultCatalogElement.appendChild(xmlDatabaseSpec.createTextNode(spec.getDefaultCatalog()));
		sqlideNode.appendChild(defaultCatalogElement);

		Element driverElement = xmlDatabaseSpec.createElement(ELEMENT_DRIVER);
		Text driverName    = xmlDatabaseSpec.createTextNode(spec.getDriverClassName());
		driverElement.appendChild(driverName);
		Element urlElement = xmlDatabaseSpec.createElement(ELEMENT_BASE_URL);
		Text urlName       = xmlDatabaseSpec.createTextNode(spec.getJDBCURL());
		urlElement.appendChild(urlName);

		jdbcNode.appendChild(driverElement);
		jdbcNode.appendChild(urlElement);

		/** @todo Save the file name */
//		jdbcNode.setAttribute(specDriverJar, spec.getJarFileName());
//		rootNode.appendChild(generalNode);
		rootNode.appendChild(jdbcNode);
		rootNode.appendChild(sqlideNode);
		rootNode.appendChild(connectionNode);
		rootNode.appendChild(sqlGenerationNode);
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
