package com.hackerdude.apps.sqlide.plugins.movedata.model;

import org.w3c.dom.*;

/**
 * An abstract script node.
 * <P>This gives the basic constructors and functionality around
 * returning node names, etc.
 */
public abstract class AbstractScriptNode {

	protected Node sourceNode = null;

	public AbstractScriptNode(Node node) {
		sourceNode = node;
	}

	public AbstractScriptNode() {
	}

	public abstract String getNodeName();

	public String getOpenTag() {
		return "<"+getNodeName()+">";
	}

	public String getCloseTag() {
		return "</"+getNodeName()+">";
	}

	public String getEmptyElement() {
		return "<"+getNodeName()+"/>";
	}

	public abstract Node getNode(Document doc);

}
