package com.hackerdude.devtools.db.sqlide.servertypes;
import java.util.HashMap;

/**
 * ServerType is an abstract that describes how the
 * different servers could be configured by default with
 * the SQLIDE New Server Wizard.
 */
public abstract class ServerType {
	public static final String _ASK_ = "____<ASK>____";

	public abstract HashMap getProperties();
	public abstract String getURL();
	public abstract String getClassName();
	public abstract String getServerTitle();
	public abstract String getDefaultCatalog();

	public abstract boolean supportsSQLDotNotation();

}