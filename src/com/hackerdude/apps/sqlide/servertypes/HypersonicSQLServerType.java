package com.hackerdude.apps.sqlide.servertypes;

import java.util.HashMap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
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
    return "jdbc:hsqldb:MyDBFile";
  }

  public String toString() { return "Hypersonic SQL Driver"; }

}