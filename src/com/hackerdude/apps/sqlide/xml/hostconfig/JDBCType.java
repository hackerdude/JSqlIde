/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package com.hackerdude.apps.sqlide.xml.hostconfig;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * The JDBC Type allows us to specify values for JDBC connectivity.
 * 		
 * 
 * @version $Revision$ $Date$
**/
public abstract class JDBCType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _driver;

    private java.lang.String _url;

    private java.lang.String _userName;

    private ConnectionProperties _connectionProperties;

    private ClassPath _classPath;


      //----------------/
     //- Constructors -/
    //----------------/

    public JDBCType() {
        super();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.JDBCType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'classPath'.
     * 
     * @return the value of field 'classPath'.
    **/
    public ClassPath getClassPath()
    {
        return this._classPath;
    } //-- ClassPath getClassPath() 

    /**
     * Returns the value of field 'connectionProperties'.
     * 
     * @return the value of field 'connectionProperties'.
    **/
    public ConnectionProperties getConnectionProperties()
    {
        return this._connectionProperties;
    } //-- ConnectionProperties getConnectionProperties() 

    /**
     * Returns the value of field 'driver'.
     * 
     * @return the value of field 'driver'.
    **/
    public java.lang.String getDriver()
    {
        return this._driver;
    } //-- java.lang.String getDriver() 

    /**
     * Returns the value of field 'url'.
     * 
     * @return the value of field 'url'.
    **/
    public java.lang.String getUrl()
    {
        return this._url;
    } //-- java.lang.String getUrl() 

    /**
     * Returns the value of field 'userName'.
     * 
     * @return the value of field 'userName'.
    **/
    public java.lang.String getUserName()
    {
        return this._userName;
    } //-- java.lang.String getUserName() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public abstract void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * 
     * 
     * @param handler
    **/
    public abstract void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException;

    /**
     * Sets the value of field 'classPath'.
     * 
     * @param classPath the value of field 'classPath'.
    **/
    public void setClassPath(ClassPath classPath)
    {
        this._classPath = classPath;
    } //-- void setClassPath(ClassPath) 

    /**
     * Sets the value of field 'connectionProperties'.
     * 
     * @param connectionProperties the value of field
     * 'connectionProperties'.
    **/
    public void setConnectionProperties(ConnectionProperties connectionProperties)
    {
        this._connectionProperties = connectionProperties;
    } //-- void setConnectionProperties(ConnectionProperties) 

    /**
     * Sets the value of field 'driver'.
     * 
     * @param driver the value of field 'driver'.
    **/
    public void setDriver(java.lang.String driver)
    {
        this._driver = driver;
    } //-- void setDriver(java.lang.String) 

    /**
     * Sets the value of field 'url'.
     * 
     * @param url the value of field 'url'.
    **/
    public void setUrl(java.lang.String url)
    {
        this._url = url;
    } //-- void setUrl(java.lang.String) 

    /**
     * Sets the value of field 'userName'.
     * 
     * @param userName the value of field 'userName'.
    **/
    public void setUserName(java.lang.String userName)
    {
        this._userName = userName;
    } //-- void setUserName(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
