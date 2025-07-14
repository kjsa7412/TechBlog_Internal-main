package com.SJTB.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Date 관련 Util 모음..
 */
@Slf4j
public class FrameDateUtil {

    private static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    protected FrameDateUtil() {
        //
    }

    /**
     * GMT 표준시를 리턴한다.
     *
     * @return
     */
    public static String getTimeGmt() {
        return getTimeGmt("");
    }

    public static String getTimeMilisecondGmt() {
        return getTimeGmt("yyyyMMddHHmmssSSS");
    }

    /**
     * GMT 표준시를 리턴한다.
     *
     * @param format
     * @return
     */
    public static String getTimeGmt(String format) {
        if (format == null || "".equals(format))
            format = "yyyyMMddHHmmss";

        Calendar cal = Calendar.getInstance(gmtTimeZone);
        //cal.add(Calendar.DAY_OF_MONTH, -1);
        Date date = cal.getTime();

        SimpleDateFormat formater = new SimpleDateFormat(format);
        formater.setTimeZone(gmtTimeZone);
        String timestamp = formater.format(date);

        return timestamp;
    }

    /**
     * 시스템의 현재 시간을 리턴한다
     *
     * @return
     */
    public static String getTimeLocale() {
        return getTimeLocale("");
    }

    /**
     * 시스템의 현재 시간을 리턴한다
     *
     * @param format
     * @return
     */
    public static String getTimeLocale(String format) {
        if (format == null || "".equals(format))
            format = "yyyyMMddHHmmss";

        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);
        Date date = cal.getTime();

        SimpleDateFormat formater = new SimpleDateFormat(format);
        formater.setTimeZone(tz);
        String timestamp = formater.format(date);

        return timestamp;
    }


    /**
     * 8자리 날짜표현을 지정된 날짜 포맷으로 변환하여 반환한다.
     *
     * @param strDate 8자리 날짜표현
     * @return 지정된 포맷으로 변환된 날짜, 날짜표현이 null이면 빈문자열을 반환한다.
     * @author
     */
    public static String gmtToLocal8(String strDate) {
        String result = strDate;

        try {
            strDate = strDate.replaceAll("-", "");
            strDate = strDate.replaceAll("/", "");
            strDate = strDate.replaceAll("\\.", "");

            // 변환할 날짜가 NULL이거나 빈문자열일 경우
            if (strDate == null || strDate.length() == 0) return "";
            if (strDate.length() < 8) return strDate;
            if (strDate.length() > 8) strDate = strDate.substring(0, 10);

            SimpleDateFormat dateFormatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
            dateFormatter.applyPattern("yyyyMMdd");

            // 지정된 날짜값을 가지는 Calendar 객체를 생성한다.
            Calendar calendar = Calendar.getInstance(gmtTimeZone);
            int iYear = Integer.parseInt(strDate.substring(0, 4));
            int iMonth = Integer.parseInt(strDate.substring(4, 6));
            int iDay = Integer.parseInt(strDate.substring(6, 8));

            calendar.set(iYear, iMonth - 1, iDay);

            DateFormat dateformat = DateFormat.getDateInstance(DateFormat.SHORT);
            dateformat.setTimeZone(TimeZone.getDefault());

            result = dateformat.format(calendar.getTime());
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    /**
     * 14자리 날짜표현을 지정된 날짜 포맷으로 변환하여 반환한다.
     *
     * @param parm 8자리 날짜표현
     * @return 지정된 포맷으로 변환된 날짜, 날짜표현이 null이면 빈문자열을 반환한다.
     * @author
     */
    public static String gmtToLocal14(String parm) {
        String result = parm;
        if (result.length() > 14) {
            result = result.substring(0, 14);
        }
        try {
            result = result.replaceAll("-", "");
            result = result.replaceAll("/", "");
            result = result.replaceAll("\\.", "");

            // 변환할 날짜가 NULL이거나 빈문자열일 경우
            if (result == null || result.length() == 0) return "";
            if (result.length() < 14) return result;

            //SimpleDateFormat dateFormatter = (SimpleDateFormat)SimpleDateFormat.getInstance();
            //dateFormatter.applyPattern("yyyyMMddHHmmss");

            // 지정된 날짜값을 가지는 Calendar 객체를 생성한다.
            Calendar calendar = Calendar.getInstance(gmtTimeZone);
            int iYear = Integer.parseInt(result.substring(0, 4));
            int iMonth = Integer.parseInt(result.substring(4, 6));
            int iDay = Integer.parseInt(result.substring(6, 8));

            int ihourOfDay = Integer.parseInt(result.substring(8, 10));
            int iminute = Integer.parseInt(result.substring(10, 12));
            int isecond = Integer.parseInt(result.substring(12, 14));

            calendar.set(iYear, iMonth - 1, iDay, ihourOfDay, iminute, isecond);

            DateFormat dateformat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM);
            dateformat.setTimeZone(TimeZone.getDefault());

            result = dateformat.format(calendar.getTime());
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    /**
     * gmt yyyyMMddhhmmss -> local yyyyMMddhhmmss 14 문자로 변경한다.
     */
    public static String transGmtToLocal14(String parm) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date localDate = FrameDateUtil.transGmtToLocalDate(parm);
            if (localDate != null) {
                return dateFormat.format(localDate);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    public static Date transGmtToLocalDate(String parm) {
        String result = parm;
        if (result.length() > 14) {
            result = result.substring(0, 14);
        }
        //UCALog.w("transGmtToLocalDate", "result : " + result);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            //gmt datetime
            Date gmtDate = dateFormat.parse(result);
            long lgmtDate = gmtDate.getTime();

            //시간차이
            TimeZone timezone = TimeZone.getDefault();
            int offset = timezone.getOffset(lgmtDate);
            long llocalDate = lgmtDate + offset;

            Date localDate = new Date(llocalDate);

            return localDate;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * LocalDateTime을 구한다.
     */
    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 월의 첫째날을 구한다.
     */
    public static Date getFirstDayOfMonth(Date parm) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(parm);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 월의 마지막날을 구한다.
     */
    public static Date getLastDayOfMonth(Date parm) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(parm);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    /**
     * 날짜에서 시간을 제외한 date 만들기
     */
    public static Date getRemovedTimedDate(Date parm) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(parm);
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        return cal.getTime();
    }

    /**
     * 특정 날짜에서 일 더하기 / 빼기
     *
     * @param parm
     * @param offset
     * @return
     */
    public static Date getAddDay(Date parm, int offset) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(parm);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + offset);
        return cal.getTime();
    }

    /**
     * 특정 날짜에서 월 더하기 / 빼기
     *
     * @param parm
     * @param offset
     * @return
     */
    public static Date getAddMonth(Date parm, int offset) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(parm);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + offset);
        return cal.getTime();
    }


    /**
     * date를 지정된 format 형식의 string으로 변경함
     */
    public static String getFormatedStringFromDate(Date date, String pattern) {
        SimpleDateFormat dateFormatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
        dateFormatter.applyPattern(pattern);
        return dateFormatter.format(date);
    }

    /**
     * yyyy mm dd (요일) 형태의 string으로 변경함
     *
     * @param date
     * @return
     */
    public static String getStringWeekDayFromDate(Date date) {
        DateFormat dateformat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        return dateformat.format(date) + " (" + formatter.format(date) + ")";
    }


    /**
     * 두 시간의 차이(분)을 구한다...
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static long getDifferMinutes(String startTime, String endTime) {

        long differMinutes = 0;
        Date dstart = null;
        Date dend = null;

        //date formatter
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            dstart = format.parse(startTime);
            dend = format.parse(endTime);
        } catch (Exception e) {
            log.error("", e);
        }

        if (dstart != null && dend != null) {
            long diff = dend.getTime() - dstart.getTime();
            differMinutes = diff / (60 * 1000);
        }

        return differMinutes;
    }

    /**
     * 두 시간의 차이(초)을 구한다...
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static long getDifferSeconds(String startTime, String endTime) {

        long differSeconse = 0;
        Date dsstart = null;
        Date dsend = null;

        //date formatter
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            dsstart = format.parse(startTime);
            dsend = format.parse(endTime);
        } catch (Exception e) {
            log.error("", e);
        }

        if (dsstart != null && dsend != null) {
            long diff = dsend.getTime() - dsstart.getTime();
            differSeconse = diff / (1 * 1000);
        }

        return differSeconse;
    }

    public static String getMinutesToHhmmFormat(long adifferMinutes) {
        String result = "";

        long h = adifferMinutes / 60;
        long m = adifferMinutes % 60;

        if (h < 10) {
            result = "0" + h;
        } else {
            result = "" + h;
        }

        result += ":";

        if (m < 10) {
            result += "0" + m;
        } else {
            result += "" + m;
        }


        return result;
    }

    public static String getSecondsToHhmmssFormat(long adifferSeconds) {
        String result = "";

        long h = adifferSeconds / 3600;
        long m = (adifferSeconds - (h * 3600)) / 60;
        long s = adifferSeconds - (h * 3600) - (m * 60);


        if (h < 10) {
            result = "0" + h;
        } else {
            result = "" + h;
        }

        result += ":";

        if (m < 10) {
            result += "0" + m;
        } else {
            result += "" + m;
        }

        result += ":";

        if (s < 10) {
            result += "0" + s;
        } else {
            result += "" + s;
        }


        return result;
    }

    public static String getHHMMFormat(String parm) {
        String result = "";
        if (parm != null && !"".equals(parm) && parm.length() > 11) {
            result = parm.substring(8, 10) + ":" + parm.substring(10, 12);
        }
        return result;
    }


    /**
     * 오늘 날짜에 특정 일을 더하거나 뺀 결과를 지정한 형식으로 반환한다.
     *
     * @param days   Calendar.HOUR 기준 시간정보
     * @param format 날짜 문자열 형식
     * @return String
     */
    public static String getDate(int days, String format) {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        gc.add(Calendar.DATE, days);

        return sdf.format(gc.getTime());
    }

    /**
     * 오늘 날짜에 특정 일을 더하거나 뺀 결과를 지정한 형식으로 반환한다.
     *
     * @param fields Calendar.DATE, Calendar.SECOND
     * @param num    더하거나 뺄 일 수. 오늘보다 과거로 가려면 음수 값을 넣는다.
     * @param format 날짜 문자열 형식
     * @return String
     */
    public static String getDate(int fields, int num, String format) {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        gc.add(fields, num);

        return sdf.format(gc.getTime());
    }

    public static String getDate(String format) {
        return getDate(0, format);
    }

    public static Date getDate(String date, String format) {
        Date rtnDate = null;
        try {
            rtnDate = (new SimpleDateFormat(((format == null || "".equals(format)) ? "yyyyMMdd" : format), Locale.KOREA).parse(date));
        } catch (Exception e) {
            //
        }
        return rtnDate;
    }

//    public static Date getDate(String date, String format, long min) {
//        Date rtnDate = null;
//        try {
//            rtnDate = DateUtils.addMinutes((new SimpleDateFormat(((format == null || "".equals(format))?"yyyyMMdd":format), Locale.KOREA).parse(date)), (int) min);
//        } catch (Exception e) {
//            log.error("", e);
//        }
//        return rtnDate;
//    }


    /**
     * 문자열 날짜를 실제 Date형식으로 변환하는 함수
     *
     * @param sDate 날짜 문자열
     * @return
     */
    public static Date getRealDate(String sDate) {
        Calendar cal = Calendar.getInstance();

        if (sDate.length() == 8) {
            cal.set(Integer.parseInt(sDate.substring(0, 4)), Integer.parseInt(sDate.substring(4, 6)) - 1, Integer.parseInt(sDate.substring(6, 8)));
        } else if (sDate.length() == 10) {
            cal.set(Integer.parseInt(sDate.substring(0, 4)), Integer.parseInt(sDate.substring(5, 7)) - 1, Integer.parseInt(sDate.substring(8, 10)));
        }

        return cal.getTime();
    }


    /**
     * 2개의 날짜를 비교할 수 있다.
     *
     * @return -1 : date1 < date2 0 : date1 = date2 1 : date1 > date2
     */
    public static int compareDate(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(date1.getTime());
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(date2.getTime());

        return compareDate(c1, c2);
    }

    /**
     * 2개의 날짜를 비교할 수 있다.
     *
     * @return -1 : cal1 < cal2 0 : cal1 = cal2 1 : cal1 > cal2
     */
    public static int compareDate(Calendar cal1, Calendar cal2) {
        int value = 9;

        if (cal1.before(cal2)) {
            value = -1;
        }
        if (cal1.after(cal2)) {
            value = 1;
        }
        if (cal1.equals(cal2)) {
            value = 0;
        }
        return value;
    }

    /*
    * 날짜와 패턴을 받아서 해당 형식으로 String을 return
    * */
    public static String formatLocalDateTimeWithPattern(LocalDateTime dateTime, String pattern) {

        String defaultPattern = "yyyy년 MM월 dd일 HH시 mm분";

        // 만약 pattern이 빈 값이라면 기본 패턴 사용
        if (pattern == null || pattern.isEmpty()) {
            pattern = defaultPattern;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return dateTime.format(formatter);
    }
}
