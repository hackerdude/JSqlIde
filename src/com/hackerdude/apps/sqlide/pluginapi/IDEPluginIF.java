package com.hackerdude.apps.sqlide.pluginapi;

import javax.swing.Icon;
import javax.swing.*;

/**
 * This is the basic interface for version 2 of the plugin
 * architecture.
 *
 * <P>This interface and its descendants replace the
 * SqlIdePluginBaseIF and SqlIdePluginIF in the
 * com.hackerdude.apps.sqlide.intf package.
 *
 * <P>This new version of the plugin architecture includes:
 *
 * <UL>
 *   <LI>An actual architected approach we can expand upon.</LI>
 *   <LI>Three types of plugins; <B>Non-Visual</B>, <B>Visual</B>, and
 *       <B>Node Context</B>. Your plugin can become all of these at the
 *       same time by implementing all interfaces, or each of them
 *       independently.</LI>
 *   <LI>Abstract plugins for Visual plugins for ease of development.</LI>
 * </UL>
 */
public interface IDEPluginIF {

  public final Action[] NULL_ACTIONS = {};

  /**
   * This call initializes the plug-in for operation.
   * <P>You can trust the SQL-IDE will call this function
   * when it is instantiating your plugin for operation,
   * as opposed of discovery.
   */
  public void initPlugin();

  /**
   * Implement this call to return the plug-in name.
   *
   * <P>Note: Your plugin might be instantiated several times
   * just to find its metadata (name, version, etc). Please try
   * to keep your constructors to a minimum.
   */
  public String getPluginName();

  /**
   * Implement this call to return the plug-in version.
   *
   * <P>Note: Your plugin might be instantiated several times
   * just to find its metadata (name, version, etc). Please try
   * to keep your constructors to a minimum.
   */
  public String getPluginVersion();

  /**
   * Implement this call to free the plugin's resources. This method
   * is called when SQLIDE wants this plugin to stop running.
   */
  void freePlugin();

  public String getPluginShortName();

  public Icon getPluginIcon();

}