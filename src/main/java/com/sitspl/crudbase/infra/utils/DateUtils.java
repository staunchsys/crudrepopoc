package com.sitspl.crudbase.infra.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DateUtils {

//    private static DateUtils me; //put into a ThreadLocal on service layer, as we're changing dates

    public SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a");
    public DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public DateTimeFormatter DATE_TIME_FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public DateTimeFormatter DATE_TIME_FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public DateTimeFormatter FPX_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public DateTimeFormatter CURLEC_FPX_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    public DateTimeFormatter FPX_DATE_TIME_DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy 'Time' HH:mm");
    public DateTimeFormatter CURRENCY_EXCHANGE_BNM_SESSION_INTERVAL_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
    
    public static final LocalTime MAX_TIME = LocalTime.of(23, 59, 59);
    
    private LocalDateTime now = LocalDateTime.now();

//    private ClockTicker clock = new ClockTicker(now);

    private boolean isSimulationStart = Boolean.FALSE;

//    public static DateUtils getInstance() {
//        if (me == null)
//            me = new DateUtils();
//
//        return me;
//    }

    public void alterNow(LocalDateTime localDateTime) {
        if (this.isSimulationStart) {
            now = localDateTime;
//            clock.setWhenStarted(localDateTime);
        }
        log.info("alterNow: " + now());
    }

    public LocalDateTime now() {
        if (this.isSimulationStart) {
            return now;
//            return LocalDateTime.now(clock);
        }
        return LocalDateTime.now();
    }

    public LocalDate nowDate() {
        if (this.isSimulationStart) {
            return now.toLocalDate();
//            return LocalDate.now(clock);
        }
        return LocalDate.now();
    }

    
	public LocalDate getFirstDayOfMonth(int monthDifference) {
		LocalDate currentDate = now.toLocalDate();
		LocalDateTime currentDateTime = now();

		if (monthDifference == 0)
			return YearMonth.from(currentDate).atDay(1);

		return YearMonth.from(currentDateTime.minusMonths(monthDifference).toLocalDate()).atDay(1);
	}
    
	private List<LocalDate> getMonthWiseStartAndEndDates(int monthDifference) {

		LocalDate currentDate = now.toLocalDate();
		Map<Integer, List<LocalDate>> monthDateMap = new HashMap<Integer, List<LocalDate>>();
		List<LocalDate> monthDateList = null;
		List<LocalDate> durationExecuteDateList = new ArrayList<LocalDate>();
		
		// compute the execution dates
		for (int i = 1; i <= monthDifference; i++) {
			monthDateList = new ArrayList<LocalDate>();
			LocalDate startDate = YearMonth.from(currentDate.minusMonths(i)).atDay(1);
			LocalDate endDate = YearMonth.from(currentDate.minusMonths(i)).atEndOfMonth();

			monthDateList.add(startDate);
			monthDateList.add(endDate);

			monthDateMap.put(i, monthDateList);

		}

		monthDateMap.forEach((k, v) -> durationExecuteDateList.addAll(v));

		return durationExecuteDateList;
	}
	
	public List<LocalDate> getAllMonthDates(LocalDate execMonthStartDate, int prevExecDays) {

		LocalDate currentDate = now.toLocalDate();
		List<LocalDate> durationExecuteDateList = new ArrayList<LocalDate>();


		long monthDiff = ChronoUnit.MONTHS.between(YearMonth.from(execMonthStartDate), YearMonth.from(currentDate));
		int monthKey = YearMonth.from(execMonthStartDate).getMonthValue();

		// compute the execution dates for month
		LocalDate executionDate = YearMonth.from(currentDate.minusMonths(monthDiff)).atDay(1);
		int monthDays = YearMonth.from(executionDate).lengthOfMonth();

		if (prevExecDays > 0)
			executionDate = executionDate.plusDays(prevExecDays); // move the execution start date after previous execution if any
		
		for (int j = 1; j <= monthDays; j++) {

			if (monthKey == YearMonth.from(executionDate).getMonthValue()) {
				durationExecuteDateList.add(executionDate);
				executionDate = executionDate.plusDays(1); // adding 1 days after each execution
			}
			else
				break;

		}
		return durationExecuteDateList;
	}
	
	public Map<Integer, List<LocalDate>> getExecutionStartAndEndDatesByMonth(int monthDifference) {

		Map<Integer, List<LocalDate>> monthDateMap = new HashMap<Integer, List<LocalDate>>();
		List<LocalDate> executionDates = getMonthWiseStartAndEndDates(monthDifference);

		// iterate on the execution dates and separate by month
		if (executionDates != null && !executionDates.isEmpty()) {

			executionDates.forEach(execDate -> {
				List<LocalDate> monthDateList = null;

				if (monthDateMap.containsKey(execDate.getMonthValue()))
					monthDateList = monthDateMap.get(execDate.getMonthValue());
				else
					monthDateList = new ArrayList<LocalDate>();

				monthDateList.add(execDate);
				monthDateMap.put(execDate.getMonthValue(), monthDateList);
			});

		}

		return monthDateMap;
	}

	public Map<Integer, List<LocalDate>> getMonthWiseStartDateAndEndDate(int monthDifference) {

		LocalDate currentDate = now.toLocalDate();
		Map<Integer, List<LocalDate>> monthDateMap = new HashMap<Integer, List<LocalDate>>();
		List<LocalDate> monthDateList = new ArrayList<LocalDate>();

		// compute the execution dates
		for (int i = 1; i <= monthDifference; i++) {

			LocalDate monthStartDate = YearMonth.from(currentDate.minusMonths(i)).atDay(1);
			LocalDate monthEndDate = YearMonth.from(currentDate.minusMonths(i)).atEndOfMonth();

			monthDateList.add(monthStartDate);
			monthDateList.add(monthEndDate);
			monthDateMap.put(i, monthDateList);
		}

		return monthDateMap;
	}
	
	public LocalDate getLastDayOfMonth(int monthDifference) {
		LocalDate currentDate = nowDate();
		LocalDateTime currentDateTime = now();

		if (monthDifference == 0)
			return YearMonth.from(currentDate).atEndOfMonth();

		return YearMonth.from(currentDateTime.minusMonths(monthDifference).toLocalDate()).atEndOfMonth();
	}
    
    public void setSimulationStart(boolean isSimulationStart) {
        this.isSimulationStart = isSimulationStart;
    }

    public boolean isSimulationStart() {
        return isSimulationStart;
    }

    // ************************************** DATE METHODS ****************************************
    public String buildStringFromCalendarDDMONYYYY(Calendar date) {
        StringBuffer sb = new StringBuffer();

        if (date != null) {
            sb.append(date.get(Calendar.DAY_OF_MONTH));
            sb.append(" ");
            sb.append(getMon(date.get(Calendar.MONTH)));
            sb.append(" ");
            sb.append(date.get(Calendar.YEAR));
        }

        return sb.toString();
    }

    public String getMon(int month) {
        switch (month) {
        case 0:
            return "Jan";
        case 1:
            return "Feb";
        case 2:
            return "Mar";
        case 3:
            return "Apr";
        case 4:
            return "May";
        case 5:
            return "Jun";
        case 6:
            return "Jul";
        case 7:
            return "Aug";
        case 8:
            return "Sep";
        case 9:
            return "Oct";
        case 10:
            return "Nov";
        case 11:
            return "Dec";
        }

        return "";
    }

    public String[] buildYearRange(String year) {
        String[] ret = new String[2];

        int yearInt = Integer.parseInt(year);

        // build start
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR, yearInt);

        ret[0] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

        // roll 1 year. and minus 1 day
        cal.roll(Calendar.YEAR, true);
        cal.add(Calendar.DAY_OF_MONTH, -1);

        ret[1] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

        return ret;
    }

    public String[] buildMonthRange(String month, String year) {
        String[] ret = new String[2];

        int monthInt = Integer.parseInt(month), yearInt = Integer.parseInt(year);

        // build start
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, monthInt - 1);
        cal.set(Calendar.YEAR, yearInt);

        ret[0] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

        // get last day of month
        if (monthInt == 1 || monthInt == 3 || monthInt == 5 || monthInt == 7 || monthInt == 8 || monthInt == 10 || monthInt == 12)
            cal.set(Calendar.DAY_OF_MONTH, 31);
        if (monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11)
            cal.set(Calendar.DAY_OF_MONTH, 30);
        // if feb, add month by 1, then roll back 1 day.
        if (monthInt == 2) {
            cal.roll(Calendar.MONTH, true);
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        ret[1] = this.convertDDMMYYtoYYMMDD(this.buildStringFromCalendar(cal));

        return ret;
    }

    public String convertDDMMYYtoYYMMDDDelimDash(String DDMMYY) {
        String ret = "";

        if (DDMMYY != null) {
            StringTokenizer st = new StringTokenizer(DDMMYY, "/");

            while (st.hasMoreTokens()) {
                String dd = st.nextToken();
                String mm = st.nextToken();
                String yy = st.nextToken();
                ret = yy + "-" + mm + "-" + dd;
            }
        }

        return ret;
    }

    public String convertDDMMYYtoYYMMDD(String DDMMYY) {
        String ret = "";

        if (DDMMYY != null) {
            StringTokenizer st = new StringTokenizer(DDMMYY, "/");

            while (st.hasMoreTokens()) {
                String dd = st.nextToken();
                String mm = st.nextToken();
                String yy = st.nextToken();
                ret = yy + "/" + mm + "/" + dd;
            }
        }

        return ret;
    }

    public String convertDDMMYYtoMMDDYY(String DDMMYY) {
        String ret = "";

        if (DDMMYY != null) {
            StringTokenizer st = new StringTokenizer(DDMMYY, "/");

            while (st.hasMoreTokens()) {
                String dd = st.nextToken();
                String mm = st.nextToken();
                String yy = st.nextToken();
                ret = mm + "/" + dd + "/" + yy;
            }
        }

        return ret;
    }

    public Calendar buildCalendarFromStringYYYYMMDD(String yyyyMMdd) {
        Calendar ret = null;

        StringTokenizer st = new StringTokenizer(yyyyMMdd, "/");
        while (st.hasMoreTokens()) {
            ret = new GregorianCalendar();

            ret.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
            ret.set(Calendar.MONTH, Integer.parseInt(st.nextToken()) - 1);
            ret.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st.nextToken()));
        }

        return ret;
    }

    public Calendar buildCalendarFromString(String ddMMyyyy) {
        Calendar ret = null;
        try {
            StringTokenizer st = new StringTokenizer(ddMMyyyy, "/");
            while (st.hasMoreTokens()) {
                ret = new GregorianCalendar();
                // nullify timings
                ret.set(Calendar.HOUR_OF_DAY, 0);
                ret.set(Calendar.MINUTE, 0);
                ret.set(Calendar.SECOND, 0);

                ret.set(Calendar.DAY_OF_MONTH, Integer.parseInt(st.nextToken()));
                ret.set(Calendar.MONTH, Integer.parseInt(st.nextToken()) - 1);
                ret.set(Calendar.YEAR, Integer.parseInt(st.nextToken()));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }

        return ret;
    }

    public String buildStringFromCalendarYYYYMMDD(Calendar date) {
        if (date != null)
            return date.get(Calendar.YEAR) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH);
        else
            return "";
    }

    public Date buildDateFromString(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String buildStringFromDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public String buildStringFromCalendarYYYY(Calendar date) {
        if (date != null)
            return NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.DAY_OF_MONTH), 2) + "/" + NumberUtils.getInstance().buildDigitNumber((date.get(Calendar.MONTH) + 1), 2) + "/"
                    + NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.YEAR), 4);
        else
            return "";
    }

    public String buildStringFromCalendar(Calendar date) {
        if (date != null)
            return NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.DAY_OF_MONTH), 2) + "/" + NumberUtils.getInstance().buildDigitNumber((date.get(Calendar.MONTH) + 1), 2) + "/"
                    + NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.YEAR), 2);
        else
            return "";
    }

    public String buildStringFromCalendarDelimDashYYYYMMDD(Calendar date) {
        if (date != null)
            return date.get(Calendar.YEAR) + "-" + NumberUtils.getInstance().buildDigitNumber((date.get(Calendar.MONTH) + 1), 2) + "-" + NumberUtils.getInstance().buildDigitNumber(date.get(Calendar.DAY_OF_MONTH), 2);
        else
            return "";
    }

    public String buildStringFromCalendarDateTimeDelimDashYYYYMMDD(Calendar cal) {
        return buildStringFromCalendarDelimDashYYYYMMDD(cal) + " " + NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.HOUR_OF_DAY), 2) + ":" + NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.MINUTE), 2) + ":"
                + NumberUtils.getInstance().buildDigitNumber(cal.get(Calendar.SECOND), 2);
    }

    public Date buildTimeFromString(String time) {
        try {
            return TIME_FORMAT.parse(time);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String buildStringFromTime(Time time) {
        Date date = new Date(time.getTime());
        return TIME_FORMAT.format(date);
    }

    public Date buildNullableDateFromString(String date) {
        try {
            if (null == date) {
                return null;
            }
            return DATE_FORMAT.parse(date);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String formatLocalDateTime(LocalDateTime dateTime) {
        return formatLocalDateTime(dateTime, DATE_TIME_FORMATTER);
    }

    public String formatLocalDateTime(LocalDateTime dateTime, DateTimeFormatter format) {
        try {
            if (null == dateTime) {
                return null;
            }
            return dateTime.format(format);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public String formatLocalDate(LocalDate date, DateTimeFormatter format) {
        try {
            if (null == date) {
                return null;
            }
            return date.format(format);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public LocalDateTime parseLocalDateTime(String s, DateTimeFormatter format) {
        try {
            if (s == null) {
                return null;
            }
            return LocalDateTime.parse(s, format);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
    
    public LocalTime parseLocalTime(String s, DateTimeFormatter format) {
        try {
            if (s == null) {
                return null;
            }
            return LocalTime.parse(s, format);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public long getEpochTime(LocalDate date) {
        if (null == date) {
            return 0;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        return date.atStartOfDay(zoneId).toEpochSecond();
    }
    
    public LocalDateTime[] getPreviousMonthDateStartAndEndDate(LocalDateTime date) {
        LocalDateTime now = date.withDayOfMonth(1); //get 1st day of the month
        LocalDateTime preLastDate = now.minusDays(1); //roll back 1 day
        LocalDateTime preStartDate = preLastDate.withDayOfMonth(1); //set to 1st day of last month
        return new LocalDateTime[] { preStartDate, preLastDate };
    }

    public LocalDate[] getPreviousMonthStartDateAndEndDate(LocalDate date) {
        LocalDateTime[] now = getPreviousMonthDateStartAndEndDate(date.atStartOfDay());
        return new LocalDate[] { now[0].toLocalDate(), now[1].toLocalDate() };
    }
    
    public long getDays(LocalDate startDate, LocalDate endDate) {
        return Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays() + 1;
    }
    
    public String fpxFormatLocalDateTime(LocalDateTime dateTime) {
        return formatLocalDateTime(dateTime, FPX_DATE_TIME_FORMATTER);
    }
    
    public LocalDateTime fpxFormatToLocalDateTime(String fpxDateTime) {
        return parseLocalDateTime(fpxDateTime, FPX_DATE_TIME_FORMATTER);
    }
    
    public LocalDateTime curlecFpxFormatToLocalDateTime(String fpxDateTime) {
        return parseLocalDateTime(fpxDateTime, CURLEC_FPX_DATE_TIME_FORMATTER);
    }
    
    public LocalTime currencyExchangeBNMSessionIntervalFormatToLocalTime(String sessionInterval) {
    	return parseLocalTime(sessionInterval, CURRENCY_EXCHANGE_BNM_SESSION_INTERVAL_FORMATTER);
    }
    
    public LocalDateTime convertToTimezone(LocalDateTime doingFor, String timezone) {
        LocalDateTime doingForInTimezone = doingFor;
        if (timezone != null) {
            doingForInTimezone = doingFor.atZone(ZoneId.systemDefault()).withZoneSameInstant(getZoneId(timezone)).toLocalDateTime();
        }
        return doingForInTimezone;
    }

    private ZoneId getZoneId(String timezone) {
        ZoneId zoneId = ZoneId.of(timezone);
        return zoneId;
    }
    
    public boolean isDaylightSavings(LocalDateTime doingFor, String timezone) {
        return getZoneId(timezone).getRules().isDaylightSavings(doingFor.toInstant(OffsetDateTime.now().getOffset()));
    }

}
