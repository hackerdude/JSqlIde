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


/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class BROWSERPLUGINType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private NamedQueries _namedQueries;

    private PluginProperties _pluginProperties;


      //----------------/
     //- Constructors -/
    //----------------/

    public BROWSERPLUGINType() {
        super();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.BROWSERPLUGINType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'namedQueries'.
     * 
     * @return the value of field 'namedQueries'.
    **/
    public NamedQueries getNamedQueries()
    {
        return this._namedQueries;
    } //-- NamedQueries getNamedQueries() 

    /**
     * Returns the value of field 'pluginProperties'.
     * 
     * @return the value of field 'pluginProperties'.
    **/
    public PluginProperties getPluginProperties()
    {
        return this._pluginProperties;
    } //-- PluginProperties getPluginProperties() 

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
     * Sets the value of field 'namedQueries'.
     * 
     * @param namedQueries the value of field 'namedQueries'.
    **/
    public void setNamedQueries(NamedQueries namedQueries)
    {
        this._namedQueries = namedQueries;
    } //-- void setNamedQueries(NamedQueries) 

    /**
     * Sets the value of field 'pluginProperties'.
     * 
     * @param pluginProperties the value of field 'pluginProperties'
    **/
    public void setPluginProperties(PluginProperties pluginProperties)
    {
        this._pluginProperties = pluginProperties;
    } //-- void setPluginProperties(PluginProperties) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
