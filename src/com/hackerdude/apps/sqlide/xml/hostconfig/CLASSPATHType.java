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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class CLASSPATHType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _pathelementList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CLASSPATHType() {
        super();
        _pathelementList = new ArrayList();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.CLASSPATHType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vPathelement
    **/
    public void addPathelement(java.lang.String vPathelement)
        throws java.lang.IndexOutOfBoundsException
    {
        _pathelementList.add(vPathelement);
    } //-- void addPathelement(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vPathelement
    **/
    public void addPathelement(int index, java.lang.String vPathelement)
        throws java.lang.IndexOutOfBoundsException
    {
        _pathelementList.add(index, vPathelement);
    } //-- void addPathelement(int, java.lang.String) 

    /**
    **/
    public void clearPathelement()
    {
        _pathelementList.clear();
    } //-- void clearPathelement() 

    /**
    **/
    public java.util.Enumeration enumeratePathelement()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_pathelementList.iterator());
    } //-- java.util.Enumeration enumeratePathelement() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getPathelement(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pathelementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_pathelementList.get(index);
    } //-- java.lang.String getPathelement(int) 

    /**
    **/
    public java.lang.String[] getPathelement()
    {
        int size = _pathelementList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_pathelementList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getPathelement() 

    /**
    **/
    public int getPathelementCount()
    {
        return _pathelementList.size();
    } //-- int getPathelementCount() 

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
     * @param vPathelement
    **/
    public boolean removePathelement(java.lang.String vPathelement)
    {
        boolean removed = _pathelementList.remove(vPathelement);
        return removed;
    } //-- boolean removePathelement(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vPathelement
    **/
    public void setPathelement(int index, java.lang.String vPathelement)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pathelementList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _pathelementList.set(index, vPathelement);
    } //-- void setPathelement(int, java.lang.String) 

    /**
     * 
     * 
     * @param pathelementArray
    **/
    public void setPathelement(java.lang.String[] pathelementArray)
    {
        //-- copy array
        _pathelementList.clear();
        for (int i = 0; i < pathelementArray.length; i++) {
            _pathelementList.add(pathelementArray[i]);
        }
    } //-- void setPathelement(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
