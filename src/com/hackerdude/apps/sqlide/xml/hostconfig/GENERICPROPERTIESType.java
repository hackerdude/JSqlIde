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
 * A collaction of properties.
 * 	 
 * 
 * @version $Revision$ $Date$
**/
public abstract class GENERICPROPERTIESType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GENERICPROPERTIESType() {
        super();
        _propertyList = new ArrayList();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.GENERICPROPERTIESType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProperty
    **/
    public void addProperty(Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.add(vProperty);
    } //-- void addProperty(Property) 

    /**
     * 
     * 
     * @param index
     * @param vProperty
    **/
    public void addProperty(int index, Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.add(index, vProperty);
    } //-- void addProperty(int, Property) 

    /**
    **/
    public void clearProperty()
    {
        _propertyList.clear();
    } //-- void clearProperty() 

    /**
    **/
    public java.util.Enumeration enumerateProperty()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_propertyList.iterator());
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * 
     * 
     * @param index
    **/
    public Property getProperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Property) _propertyList.get(index);
    } //-- Property getProperty(int) 

    /**
    **/
    public Property[] getProperty()
    {
        int size = _propertyList.size();
        Property[] mArray = new Property[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Property) _propertyList.get(index);
        }
        return mArray;
    } //-- Property[] getProperty() 

    /**
    **/
    public int getPropertyCount()
    {
        return _propertyList.size();
    } //-- int getPropertyCount() 

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
     * @param vProperty
    **/
    public boolean removeProperty(Property vProperty)
    {
        boolean removed = _propertyList.remove(vProperty);
        return removed;
    } //-- boolean removeProperty(Property) 

    /**
     * 
     * 
     * @param index
     * @param vProperty
    **/
    public void setProperty(int index, Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propertyList.set(index, vProperty);
    } //-- void setProperty(int, Property) 

    /**
     * 
     * 
     * @param propertyArray
    **/
    public void setProperty(Property[] propertyArray)
    {
        //-- copy array
        _propertyList.clear();
        for (int i = 0; i < propertyArray.length; i++) {
            _propertyList.add(propertyArray[i]);
        }
    } //-- void setProperty(Property) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
