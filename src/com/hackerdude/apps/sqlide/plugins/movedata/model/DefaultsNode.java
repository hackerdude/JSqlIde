package com.hackerdude.apps.sqlide.plugins.movedata.model;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Defaults node.
 * <P>A DataMover script can have a single defaults node.
 * This defaults node will help in simplifying the script.
 */
public class DefaultsNode extends AbstractScriptNode {
	/**
	 * The default source
	 */
	public DataSourceNode defaultSource;
	public DataDestinationNode defaultDestination;

	public String getNodeName() { return "DEFAULTS"; }

	public Node getNode(Document doc) {
		DocumentImpl impl = (DocumentImpl)doc;
		ElementImpl element = new ElementImpl(impl, getNodeName());
		if ( defaultSource != null ) element.appendChild(defaultSource.getNode(doc));
		if ( defaultDestination != null ) element.appendChild(defaultDestination.getNode(doc));
		return element;
	}


}
