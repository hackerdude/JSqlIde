package com.hackerdude.apps.sqlide.pluginapi;

/**
* Right now the Non-Visual plugin interface is simply a
* marker so the SQLIDE can determine that the plugin is
* a non-visual plugin and not any other type.
*
* Non-Visual plugins typically insert their own menu
* actions to the existing menu structure and stick
* around listening to those actions.
*/
public interface IDENonVisualPluginIF extends IDEPluginIF {

}
