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
 * This type represents configuration that is plugin-specific, but
 * also
 * connection-specific. This is for plugins that are not native to
 * the SQLIDE-Base
 * to be able to save their own configuration information (that
 * depends on the
 * database specification).
 * 	 
 * 
 * @version $Revision$ $Date$
**/
public abstract class PLUGINSPECIFICType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _pluginName;

    private PluginProperties _pluginProperties;


      //----------------/
     //- Constructors -/
    //----------------/

    public PLUGINSPECIFICType() {
        super();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.PLUGINSPECIFICType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'pluginName'.
     * 
     * @return the value of field 'pluginName'.
    **/
    public java.lang.String getPluginName()
    {
        return this._pluginName;
    } //-- java.lang.String getPluginName() 

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
     * Sets the value of field 'pluginName'.
     * 
     * @param pluginName the value of field 'pluginName'.
    **/
    public void setPluginName(java.lang.String pluginName)
    {
        this._pluginName = pluginName;
    } //-- void setPluginName(java.lang.String) 

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
