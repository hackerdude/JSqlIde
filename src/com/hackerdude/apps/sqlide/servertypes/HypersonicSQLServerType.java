package com.hackerdude.apps.sqlide.servertypes;

import java.io.File;
import java.util.HashMap;

/**
 * Hypersonic server helper.
 *
 * 
 * @author David Martinez
 * @version 1.0
 */
public class HypersonicSQLServerType extends ServerType {

  public HypersonicSQLServerType() {
  }
  public boolean supportsSQLDotNotation() { return true; }

  public HashMap getProperties() {return new HashMap();  }

  public String getServerTitle() { return "Hypersonic SQL"; }
  public String getDefaultCatalog() { return "MyCatalog"; }

  public String getClassName() { return "org.hsqldb.jdbcDriver"; }

  public String getURL() {
    return "jdbc:hsqldb:"+System.getProperty("user.home")+File.separator+"MyDBFile";
  }

  public String toString() { return "Hypersonic SQL Driver"; }

}