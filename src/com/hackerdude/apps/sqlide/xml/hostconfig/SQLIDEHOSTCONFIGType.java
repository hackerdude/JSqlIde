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

import java.util.ArrayList;

/**
 * 
 * Our root element.
 * 	 
 * 
 * @version $Revision$ $Date$
**/
public abstract class SQLIDEHOSTCONFIGType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _version;

    private java.lang.String _fileName;

    private java.lang.String _name;

    private General _general;

    private Jdbc _jdbc;

    private BrowserPlugin _browserPlugin;

    private QueryHistory _queryHistory;

    private java.util.ArrayList _pluginSpecificList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SQLIDEHOSTCONFIGType() {
        super();
        _pluginSpecificList = new ArrayList();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.SQLIDEHOSTCONFIGType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vPluginSpecific
    **/
    public void addPluginSpecific(PluginSpecific vPluginSpecific)
        throws java.lang.IndexOutOfBoundsException
    {
        _pluginSpecificList.add(vPluginSpecific);
    } //-- void addPluginSpecific(PluginSpecific) 

    /**
     * 
     * 
     * @param index
     * @param vPluginSpecific
    **/
    public void addPluginSpecific(int index, PluginSpecific vPluginSpecific)
        throws java.lang.IndexOutOfBoundsException
    {
        _pluginSpecificList.add(index, vPluginSpecific);
    } //-- void addPluginSpecific(int, PluginSpecific) 

    /**
    **/
    public void clearPluginSpecific()
    {
        _pluginSpecificList.clear();
    } //-- void clearPluginSpecific() 

    /**
    **/
    public java.util.Enumeration enumeratePluginSpecific()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_pluginSpecificList.iterator());
    } //-- java.util.Enumeration enumeratePluginSpecific() 

    /**
     * Returns the value of field 'browserPlugin'.
     * 
     * @return the value of field 'browserPlugin'.
    **/
    public BrowserPlugin getBrowserPlugin()
    {
        return this._browserPlugin;
    } //-- BrowserPlugin getBrowserPlugin() 

    /**
     * Returns the value of field 'fileName'.
     * 
     * @return the value of field 'fileName'.
    **/
    public java.lang.String getFileName()
    {
        return this._fileName;
    } //-- java.lang.String getFileName() 

    /**
     * Returns the value of field 'general'.
     * 
     * @return the value of field 'general'.
    **/
    public General getGeneral()
    {
        return this._general;
    } //-- General getGeneral() 

    /**
     * Returns the value of field 'jdbc'.
     * 
     * @return the value of field 'jdbc'.
    **/
    public Jdbc getJdbc()
    {
        return this._jdbc;
    } //-- Jdbc getJdbc() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * 
     * 
     * @param index
    **/
    public PluginSpecific getPluginSpecific(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pluginSpecificList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (PluginSpecific) _pluginSpecificList.get(index);
    } //-- PluginSpecific getPluginSpecific(int) 

    /**
    **/
    public PluginSpecific[] getPluginSpecific()
    {
        int size = _pluginSpecificList.size();
        PluginSpecific[] mArray = new PluginSpecific[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (PluginSpecific) _pluginSpecificList.get(index);
        }
        return mArray;
    } //-- PluginSpecific[] getPluginSpecific() 

    /**
    **/
    public int getPluginSpecificCount()
    {
        return _pluginSpecificList.size();
    } //-- int getPluginSpecificCount() 

    /**
     * Returns the value of field 'queryHistory'.
     * 
     * @return the value of field 'queryHistory'.
    **/
    public QueryHistory getQueryHistory()
    {
        return this._queryHistory;
    } //-- QueryHistory getQueryHistory() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return the value of field 'version'.
    **/
    public java.lang.String getVersion()
    {
        return this._version;
    } //-- java.lang.String getVersion() 

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
     * 
     * 
     * @param vPluginSpecific
    **/
    public boolean removePluginSpecific(PluginSpecific vPluginSpecific)
    {
        boolean removed = _pluginSpecificList.remove(vPluginSpecific);
        return removed;
    } //-- boolean removePluginSpecific(PluginSpecific) 

    /**
     * Sets the value of field 'browserPlugin'.
     * 
     * @param browserPlugin the value of field 'browserPlugin'.
    **/
    public void setBrowserPlugin(BrowserPlugin browserPlugin)
    {
        this._browserPlugin = browserPlugin;
    } //-- void setBrowserPlugin(BrowserPlugin) 

    /**
     * Sets the value of field 'fileName'.
     * 
     * @param fileName the value of field 'fileName'.
    **/
    public void setFileName(java.lang.String fileName)
    {
        this._fileName = fileName;
    } //-- void setFileName(java.lang.String) 

    /**
     * Sets the value of field 'general'.
     * 
     * @param general the value of field 'general'.
    **/
    public void setGeneral(General general)
    {
        this._general = general;
    } //-- void setGeneral(General) 

    /**
     * Sets the value of field 'jdbc'.
     * 
     * @param jdbc the value of field 'jdbc'.
    **/
    public void setJdbc(Jdbc jdbc)
    {
        this._jdbc = jdbc;
    } //-- void setJdbc(Jdbc) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vPluginSpecific
    **/
    public void setPluginSpecific(int index, PluginSpecific vPluginSpecific)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pluginSpecificList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _pluginSpecificList.set(index, vPluginSpecific);
    } //-- void setPluginSpecific(int, PluginSpecific) 

    /**
     * 
     * 
     * @param pluginSpecificArray
    **/
    public void setPluginSpecific(PluginSpecific[] pluginSpecificArray)
    {
        //-- copy array
        _pluginSpecificList.clear();
        for (int i = 0; i < pluginSpecificArray.length; i++) {
            _pluginSpecificList.add(pluginSpecificArray[i]);
        }
    } //-- void setPluginSpecific(PluginSpecific) 

    /**
     * Sets the value of field 'queryHistory'.
     * 
     * @param queryHistory the value of field 'queryHistory'.
    **/
    public void setQueryHistory(QueryHistory queryHistory)
    {
        this._queryHistory = queryHistory;
    } //-- void setQueryHistory(QueryHistory) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
    **/
    public void setVersion(java.lang.String version)
    {
        this._version = version;
    } //-- void setVersion(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
