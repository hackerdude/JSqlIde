package com.hackerdude.devtools.db.sqlide.plugins.movedata.model;

import org.w3c.dom.*;
import org.apache.xerces.dom.*;


/**
 * A comment Node for dataMover scripts. Basically a
 * thin wrapper around an XML comment.
 */
public class CommentInstruction extends AbstractScriptNode {

	private String comment;

	public CommentInstruction(String comment) {
	}

	public CommentInstruction(Node node) {
		super(node);
		comment = node.getNodeValue();
	}

	public String toString() { return comment; }

	public String getNodeName() { return null; }

	public Node getNode(Document doc) {
		if ( sourceNode != null ) { return sourceNode; }
		DocumentImpl xDoc = (DocumentImpl)doc;
		CommentImpl node = new CommentImpl(xDoc, comment);
		return node;
	}

	public void setComment(String comment) {
		this.comment = comment;
		if ( sourceNode != null ) sourceNode.setNodeValue(comment);
	}

	public String getComment() {
		return comment;
	}

}