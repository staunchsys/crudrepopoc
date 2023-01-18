package com.sitspl.crudbase.infra.controller;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController {

	public SimpleDateFormat DATE_FORMAT_FOR_FORM_OBJECT = new SimpleDateFormat(AbstractController.DATE_FORMAT_FOR_FORM);

	public static DateTimeFormatter LOCAL_DATE_FORMAT_FOR_FORM_OBJECT = DateTimeFormatter
			.ofPattern(AbstractController.DATE_FORMAT_FOR_FORM);

	public static DateTimeFormatter LOCAL_DATE_TIME_FORMAT_FOR_FORM_OBJECT = DateTimeFormatter
			.ofPattern(AbstractController.DATE_TIME_FORMAT_FOR_FORM);

	public static final String DATE_FORMAT_FOR_FORM = "yyyy-MM-dd";

	public static final String DATE_FORMAT_FOR_UI = "yyyy-mm-dd";

	public static final String DATE_TIME_FORMAT_FOR_UI = "yyyy-mm-dd hh:ii:ss";

	public static final String DATE_TIME_FORMAT_FOR_FORM = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FORMAT_FOR_GRAPH = "dd MMM";

	public static final String DOUBLE_FORMAT_FOR_PRINT = "#0.00";

	public static final String INTEGER_FORMAT_FOR_PRINT = "#0";

	public static final String INTEGER_FORMAT_FOR_REWARD_CUSTOMER_SERIAL_NUMBER = "#000000";

	public static final String INTEGER_FORMAT_WITH_SIGNS_FOR_PRINT = "'+'#0;'-'#0";

	public static final String CUSTOM_TIME_FORMAT = "H:mm";

	// In Filter.jsp, Date format is given manually because of moment.js
	// Below is compatible format for moment.js hence not following Java standard
	// but following moment.js standard
	public static final String DATE_FORMAT_FOR_FILTER = "YYYY/MM/DD";

	public static final String DATE_FORMAT_TO_BE_PARSED_FROM_FILTER = "yyyy/MM/dd";

	public static final String DATE_FORMAT_FOR_STATEMENT_USER = "MMM-yy";

	public static final String DATE_TIME_FORMAT_FOR_FILTER = "YYYY/MM/DD-HH:mm:ss";

	public static final String DATE_TIME_FORMAT_TO_BE_PARSED_FROM_FILTER = "yyyy/MM/dd-HH:mm:ss";

	public static final String FILTER_PREFIX_KEY = "f_";

	public static final String PREFIX_ILIKE = FILTER_PREFIX_KEY + "ilk_";

	public static final String PREFIX_DATETIME = FILTER_PREFIX_KEY + "dt_";

	public static final String PREFIX_DATE = FILTER_PREFIX_KEY + "d_";

	public static final String PREFIX_CHECKBOX = FILTER_PREFIX_KEY + "chk_";

	public static final String PREFIX_TIME_RANGE = FILTER_PREFIX_KEY + "tr_";

}
