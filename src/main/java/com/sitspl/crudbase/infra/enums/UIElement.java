package com.sitspl.crudbase.infra.enums;

public enum UIElement {

	/** The combo. */
    COMBO(FilterOperators.EQ),
    /** The text. */
    TEXT(FilterOperators.EQ, FilterOperators.NEQ, FilterOperators.STARTS_WITH, FilterOperators.ENDS_WITH, FilterOperators.HAS),
    /** The date. */
    DATERANGE(FilterOperators.BETWEEN, FilterOperators.NULL, FilterOperators.NOT_NULL),
    /** The date-time */
    DATETIMERANGE(FilterOperators.BETWEEN, FilterOperators.NULL, FilterOperators.NOT_NULL),
    /** The multi. */
    MULTI(FilterOperators.CONTAINS),
    /** The number. */
    NUMBER(FilterOperators.EQ, FilterOperators.LE, FilterOperators.LEQ, FilterOperators.GE, FilterOperators.GEQ),
    /** The singledate. */
    SINGLEDATE(FilterOperators.EQ, FilterOperators.LE, FilterOperators.LEQ, FilterOperators.GE, FilterOperators.GEQ),
    /** The Checkbox. */
    CHECKBOX(FilterOperators.HAS),
    /** The Time Range*/
    TIMERANGE(FilterOperators.BETWEEN),
    /** Default type to identify UIElement type based on Java type */
    NONE,
    /** Auto complete input box */
    AUTOCOMPLETETEXT;
    
	private FilterOperators[] availableFilterOperators;
	
	UIElement(FilterOperators... availableFilterOperators) {
		this.availableFilterOperators = availableFilterOperators;
	}

	public FilterOperators[] getAvailableFilterOperators() {
		return availableFilterOperators;
	}

}
