package com.hackerdude.apps.sqlide.plugins.movedata.model;

/**
 * A moveData Node is a single node in a moveData XML script.
 */
public abstract class MoveDataNode extends CopyDataNode {

	public String getNodeName() { return "MOVEDATA"; }

}
