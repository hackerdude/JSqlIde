package com.hackerdude.apps.sqlide.intf;

/**
 * This interface specifies the interfaces a class must implement to be
 * considered a basic plugin. Non visual and visual plugins both implement
 * this interface.
 * @deprecated Use pluginapi.IDE* interfaces instead.
 */
public interface SqlIdePluginBaseIF {


	/**
	 * This is useful to enable-disable buttons and menus
	 * on the fly. Typically, the function will be called
	 * by an external entity mentioning the action it wants
	 * to update.
	 */
	public boolean isActionPossible( String action );

	/**
	 * IDEPanelInterfaces implement this function so
	 * the external programs can ask the program to
	 * execute a specific simple action.
	 */
	public boolean executeAction( String actionName );

	/**
	 * Refresh the panel's user interface. This method will be called
	 * by the main program whenever the configuration has changed. Make
	 * sure you reset all configurable items your panel contains.
	 */
	public void refreshPanel();

	/**
	 * Returns the "short name" for this plugin (to be put on
	 * the tabs on a TabbedPane, for instance).
	 */
	public String getShortName();

	/**
	 * Returns the version of this plugin as a string.
	 */
	public String getPluginVersion();


}
