package com.hackerdude.apps.sqlide.plugins.movedata.model;

import org.w3c.dom.*;
import org.apache.xerces.dom.*;

/**
 * Copy Data Node.
 * <P>This is a node that copies from Source to Destination
 *
 */
public class CopyDataNode extends AbstractScriptNode {

	private DataSourceNode dataSourceNode;
	private DataDestinationNode dataDestinationNode;

	public Node getNode(Document doc) {
		DocumentImpl impl = (DocumentImpl)doc;
		ElementImpl element = new ElementImpl((DocumentImpl)doc, getNodeName());
		if ( dataSourceNode != null ) element.appendChild(dataSourceNode.getNode(doc));
		if ( dataDestinationNode != null ) element.appendChild(dataDestinationNode.getNode(doc));
		return element;
	}

	public String getNodeName() { return "COPYDATA"; }

}
