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
 * 
 * @version $Revision$ $Date$
**/
public abstract class QUERYHISTORYType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _historyItemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public QUERYHISTORYType() {
        super();
        _historyItemList = new ArrayList();
    } //-- com.hackerdude.apps.sqlide.xml.hostconfig.QUERYHISTORYType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vHistoryItem
    **/
    public void addHistoryItem(java.lang.String vHistoryItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _historyItemList.add(vHistoryItem);
    } //-- void addHistoryItem(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vHistoryItem
    **/
    public void addHistoryItem(int index, java.lang.String vHistoryItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _historyItemList.add(index, vHistoryItem);
    } //-- void addHistoryItem(int, java.lang.String) 

    /**
    **/
    public void clearHistoryItem()
    {
        _historyItemList.clear();
    } //-- void clearHistoryItem() 

    /**
    **/
    public java.util.Enumeration enumerateHistoryItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_historyItemList.iterator());
    } //-- java.util.Enumeration enumerateHistoryItem() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getHistoryItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _historyItemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_historyItemList.get(index);
    } //-- java.lang.String getHistoryItem(int) 

    /**
    **/
    public java.lang.String[] getHistoryItem()
    {
        int size = _historyItemList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_historyItemList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getHistoryItem() 

    /**
    **/
    public int getHistoryItemCount()
    {
        return _historyItemList.size();
    } //-- int getHistoryItemCount() 

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
     * @param vHistoryItem
    **/
    public boolean removeHistoryItem(java.lang.String vHistoryItem)
    {
        boolean removed = _historyItemList.remove(vHistoryItem);
        return removed;
    } //-- boolean removeHistoryItem(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vHistoryItem
    **/
    public void setHistoryItem(int index, java.lang.String vHistoryItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _historyItemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _historyItemList.set(index, vHistoryItem);
    } //-- void setHistoryItem(int, java.lang.String) 

    /**
     * 
     * 
     * @param historyItemArray
    **/
    public void setHistoryItem(java.lang.String[] historyItemArray)
    {
        //-- copy array
        _historyItemList.clear();
        for (int i = 0; i < historyItemArray.length; i++) {
            _historyItemList.add(historyItemArray[i]);
        }
    } //-- void setHistoryItem(java.lang.String) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
