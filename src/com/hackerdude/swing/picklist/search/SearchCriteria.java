package com.hackerdude.swing.picklist.search;

/**
 * This is the search criteria class. It acts as the storage class for a single
 * criteria search option (which includes criteria number, text, data type, and
 * object to search for).
 *
 * <P>This object is cloneable so that other threads can access a copy of the
 * object and do searches without changing the objects in the search parameter.
 * @author David Martinez
 * @version 1.0
 */
public class SearchCriteria implements Cloneable {

	String criteriaName;
	Class dataType;
	Object whatToSearchFor;

    public SearchCriteria(String criteriaName, Class dataType, Object whatToSearchFor) {
		this.criteriaName = criteriaName;
		this.dataType = dataType;
		this.whatToSearchFor = whatToSearchFor;
    }

	public SearchCriteria(String criteriaName, Class dataType) {
		this(criteriaName, dataType, null);
	}

	public SearchCriteria(String criteriaName, String whatToSearchFor) {
		this(criteriaName, String.class, whatToSearchFor);
	}

	public SearchCriteria(String criteriaName) {
		this(criteriaName, String.class, null);
	}

	public String getCriteriaName() { return criteriaName; }

	public Class getDataType() { return dataType; }

	public Object getWhatToSearchFor() { return whatToSearchFor; }

	public String toString() {
		return criteriaName;
	}


	public Object clone() throws CloneNotSupportedException { return super.clone(); }
}