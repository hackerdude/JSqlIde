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

import java.util.Vector;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public abstract class PATHELEMENTType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _pathElementFileNameList;


      //----------------/
     //- Constructors -/
    //----------------/

    public PATHELEMENTType() {
        super();
        _pathElementFileNameList = new Vector();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.PATHELEMENTType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vPathElementFileName
    **/
    public void addPathElementFileName(java.lang.String vPathElementFileName)
        throws java.lang.IndexOutOfBoundsException
    {
        _pathElementFileNameList.addElement(vPathElementFileName);
    } //-- void addPathElementFileName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vPathElementFileName
    **/
    public void addPathElementFileName(int index, java.lang.String vPathElementFileName)
        throws java.lang.IndexOutOfBoundsException
    {
        _pathElementFileNameList.insertElementAt(vPathElementFileName, index);
    } //-- void addPathElementFileName(int, java.lang.String) 

    /**
    **/
    public java.util.Enumeration enumeratePathElementFileName()
    {
        return _pathElementFileNameList.elements();
    } //-- java.util.Enumeration enumeratePathElementFileName() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getPathElementFileName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pathElementFileNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_pathElementFileNameList.elementAt(index);
    } //-- java.lang.String getPathElementFileName(int) 

    /**
    **/
    public java.lang.String[] getPathElementFileName()
    {
        int size = _pathElementFileNameList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_pathElementFileNameList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getPathElementFileName() 

    /**
    **/
    public int getPathElementFileNameCount()
    {
        return _pathElementFileNameList.size();
    } //-- int getPathElementFileNameCount() 

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
    **/
    public void removeAllPathElementFileName()
    {
        _pathElementFileNameList.removeAllElements();
    } //-- void removeAllPathElementFileName() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String removePathElementFileName(int index)
    {
        java.lang.Object obj = _pathElementFileNameList.elementAt(index);
        _pathElementFileNameList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removePathElementFileName(int) 

    /**
     * 
     * 
     * @param index
     * @param vPathElementFileName
    **/
    public void setPathElementFileName(int index, java.lang.String vPathElementFileName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pathElementFileNameList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _pathElementFileNameList.setElementAt(vPathElementFileName, index);
    } //-- void setPathElementFileName(int, java.lang.String) 

    /**
     * 
     * 
     * @param pathElementFileNameArray
    **/
    public void setPathElementFileName(java.lang.String[] pathElementFileNameArray)
    {
        //-- copy array
        _pathElementFileNameList.removeAllElements();
        for (int i = 0; i < pathElementFileNameArray.length; i++) {
            _pathElementFileNameList.addElement(pathElementFileNameArray[i]);
        }
    } //-- void setPathElementFileName(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
