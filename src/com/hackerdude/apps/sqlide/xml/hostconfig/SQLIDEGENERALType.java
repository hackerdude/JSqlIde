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
 * 
 * This type represents general information for SQL-IDE.
 * 
 * 	 
 * 
 * @version $Revision$ $Date$
**/
public abstract class SQLIDEGENERALType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private boolean _supportsDotNotation;

    /**
     * keeps track of state for field: _supportsDotNotation
    **/
    private boolean _has_supportsDotNotation;


      //----------------/
     //- Constructors -/
    //----------------/

    public SQLIDEGENERALType() {
        super();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.SQLIDEGENERALType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'supportsDotNotation'.
     * 
     * @return the value of field 'supportsDotNotation'.
    **/
    public boolean getSupportsDotNotation()
    {
        return this._supportsDotNotation;
    } //-- boolean getSupportsDotNotation() 

    /**
    **/
    public boolean hasSupportsDotNotation()
    {
        return this._has_supportsDotNotation;
    } //-- boolean hasSupportsDotNotation() 

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
     * Sets the value of field 'supportsDotNotation'.
     * 
     * @param supportsDotNotation the value of field
     * 'supportsDotNotation'.
    **/
    public void setSupportsDotNotation(boolean supportsDotNotation)
    {
        this._supportsDotNotation = supportsDotNotation;
        this._has_supportsDotNotation = true;
    } //-- void setSupportsDotNotation(boolean) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
