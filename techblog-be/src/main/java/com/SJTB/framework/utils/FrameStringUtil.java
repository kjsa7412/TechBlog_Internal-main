package com.SJTB.framework.utils;

import com.SJTB.project.img.ImgFileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 프레임 공통 String 함수 정의
 */
@Slf4j
public class FrameStringUtil {
    /**
     * 객체가 Null or 빈 문자열 여부를 확인
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }

    /**
     * 객체가 Null 여부 확인
     *
     * @param str
     * @return
     */
    public static boolean isNull(@Nullable Object str) {
        return str == null;
    }

    /**
     * 객체가 Null or 빈 문자열 일때 대체 문자를 리턴
     *
     * @param parm
     * @param defaultValue
     * @return
     */
    public static String isNullDefaultValue(String parm, String defaultValue) {
        if (isEmpty(parm)) {
            return defaultValue;
        } else {
            return parm;
        }
    }

    /**
     * 8자리 날짜표현을 지정된 날짜 포맷으로 변환하여 반환한다.
     *
     * @param strDate
     * @param strPattern
     * @return
     * @throws Exception
     */
    public static String dateFormat(String strDate, String strPattern) throws Exception {
        String returnValue = "";

        try {
            // 변환할 날짜가 NULL이거나 빈문자열일 경우
            if (strDate == null || strDate.length() == 0)
                return "";
            if (strDate.length() < 8)
                return strDate;
            if (strDate.length() > 8)
                strDate = strDate.substring(0, 10);

            boolean bNeedEncode = false;
            char chDelim = '-';

            if (strDate.indexOf('/') != -1) {
                chDelim = '/';
                bNeedEncode = true;
            } else if (strDate.indexOf('.') != -1) {
                chDelim = '.';
                bNeedEncode = true;
            } else if (strDate.indexOf('-') != -1) {
                chDelim = '-';
                bNeedEncode = true;
            }

            // 8자리 날짜로 변환한다.
            if (bNeedEncode) {
                StringBuffer strbufDate = new StringBuffer(strDate);
                int index = 0;

                // 날짜 형식에서 구분기호를 삭제하여 8자리 날짜 문자열로 변환한다.
                do {
                    index = strbufDate.toString().indexOf(chDelim);
                    if (index != -1)
                        strbufDate.deleteCharAt(index);
                } while (index != -1);

                strDate = strbufDate.toString();
            } // if (bNeedEncode)

            // SimpleDateFormat 객체를 반환받아서, 지정된 날짜 포맷으로 설정한다.
            SimpleDateFormat dateFormatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
            dateFormatter.applyPattern(strPattern);

            // 지정된 날짜값을 가지는 Calendar 객체를 생성한다.
            Calendar calendar = Calendar.getInstance();
            int iYear = Integer.parseInt(strDate.substring(0, 4));
            int iMonth = Integer.parseInt(strDate.substring(4, 6));
            int iDay = Integer.parseInt(strDate.substring(6, 8));
            calendar.set(iYear, iMonth - 1, iDay);

            returnValue = dateFormatter.format(calendar.getTime());
        } catch (Exception e) {
            log.error("dateFormat Exception!", e);
        }

        return returnValue;
    }

    public static String dateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    /**
     * 숫자를 지정된 포맷으로 변환하여 반환한다. 변환할 숫자문자열이 NULL이거나 빈문자열일 경우 빈문자열을 반환하거나 ""을 반환한다.
     *
     * @param strNumber
     * @param strPattern
     * @return
     */
    public static String numberFormat(String strNumber, String strPattern) {

        if ("".equals(isNullDefaultValue(strNumber, "")))
            return "";

        try {
            return numberFormat(Double.parseDouble(strNumber), strPattern);
        } catch (Exception e) {
            log.error("numberFormat Exception", e);
        }
        return "";
    }

    public static String numberFormat(double val, String strPattern) throws Exception {
        String rStr = "";
        String format = "#,###";
        DecimalFormat df = null;

        try {
            if (!"".equals(isNullDefaultValue(strPattern, "")))
                format = strPattern;

            int idx = -1;
            String key1 = ""; // 소수점 앞자리 값
            String key2 = ""; // 마지막 값

            if (format != null) {
                if (format.lastIndexOf('.') > -1) {
                    idx = format.lastIndexOf('.');
                    key1 = format.substring(idx - 1, idx);
                    key2 = format.substring(format.length() - 1, format.length());

                    if ("#".equals(key1) && "#".equals(key2) && 0 == val)
                        return "";
                    else {
                        df = new DecimalFormat(format);
                        rStr = df.format(val);
                    }
                } else {
                    key1 = format.substring(format.length() - 1, format.length());

                    if ("#".equals(key1) && 0 == val)
                        return "";
                    else {
                        df = new DecimalFormat(format);
                        rStr = df.format(val);
                    }
                }
            }


            return rStr;
        } catch (NullPointerException e) {
            log.error("numberFormat Exception", e);
        } catch (Exception e) {
            log.error("numberFormat Exception", e);
        }
        return null;

    }

    public static String numberFormat(String strNumber) throws Exception {
        try {
            if ("".equals(isNullDefaultValue(strNumber, "")))
                return "";

            return numberFormat(Double.parseDouble(strNumber), null);
        } catch (Exception e) {
            log.error("numberFormat Exception", e);
        }
        return null;
    }

    public static String numberFormat(double val) throws Exception {
        try {
            return numberFormat(val, null);
        } catch (Exception e) {
            log.error("numberFormat Exception", e);
        }
        return null;
    }

    /**
     * 전화번호 문자열을 전화번호형식으로 바꿔준다.
     *
     * @param str 전화번호 문자열
     * @return 전화번호형식의 문자열
     * @author 박영민
     */
    public static String telFormat(String str) {

        if (str == null || "".equals(str))
            return "";

        String rtnStr = str.replaceAll("[^0-9]", "");
        String loc = "";
        String num1 = "";
        String num2 = "";

        if (rtnStr.length() < 5) {
            return rtnStr;
        }

        if (rtnStr.substring(0, 2).equals("02")) {

            if (rtnStr.length() == 5) {
                loc = rtnStr.substring(0, 2);
                num1 = rtnStr.substring(2, 5);
            } else if (rtnStr.length() == 6) {
                loc = rtnStr.substring(0, 2);
                num1 = rtnStr.substring(2, 6);
            } else if (rtnStr.length() == 7) {
                loc = rtnStr.substring(0, 2);
                num1 = rtnStr.substring(2, 5);
                num2 = rtnStr.substring(5, 7);
            } else if (rtnStr.length() == 8) {
                loc = rtnStr.substring(0, 2);
                num1 = rtnStr.substring(2, 5);
                num2 = rtnStr.substring(5, 8);
            } else if (rtnStr.length() == 9) {
                loc = rtnStr.substring(0, 2);
                num1 = rtnStr.substring(2, 5);
                num2 = rtnStr.substring(5, 9);
            } else if (rtnStr.length() == 10) {
                loc = rtnStr.substring(0, 2);
                num1 = rtnStr.substring(2, 6);
                num2 = rtnStr.substring(6, 10);
            }
//			else if(rtnStr.length() > 10){
//				int iStrLen = rtnStr.length();
//				loc = rtnStr.substring(0, iStrLen-8);
//				num1 = rtnStr.substring(iStrLen-8, iStrLen-4);
//				num2 = rtnStr.substring(iStrLen-4, iStrLen);
//			} else
            {
                return rtnStr;
            }
        } else {
            if (rtnStr.length() == 6) {
                loc = rtnStr.substring(0, 3);
                num1 = rtnStr.substring(3, 6);
            } else if (rtnStr.length() == 7) {
                loc = rtnStr.substring(0, 3);
                num1 = rtnStr.substring(3, 7);
            } else if (rtnStr.length() == 8) {
                loc = rtnStr.substring(0, 4);
                num1 = rtnStr.substring(4, 8);
            } else if (rtnStr.length() == 9) {
                loc = rtnStr.substring(0, 2);
                num1 = rtnStr.substring(2, 5);
                num2 = rtnStr.substring(5, 9);
            } else if (rtnStr.length() == 10) {
                loc = rtnStr.substring(0, 3);
                num1 = rtnStr.substring(3, 6);
                num2 = rtnStr.substring(6, 10);
            } else if (rtnStr.length() == 11) {
                loc = rtnStr.substring(0, 3);
                num1 = rtnStr.substring(3, 7);
                num2 = rtnStr.substring(7, 11);
            } else if (rtnStr.length() > 11) {
                int iStrLen = rtnStr.length();
                loc = rtnStr.substring(0, iStrLen - 8);
                num1 = rtnStr.substring(iStrLen - 8, iStrLen - 4);
                num2 = rtnStr.substring(iStrLen - 4, iStrLen);
            } else {
                return rtnStr;
            }
        }

        rtnStr = loc + ((num1.isEmpty() ? "" : "-" + num1)) + ((num2.isEmpty() ? "" : "-" + num2));

        return rtnStr;
    }


    public static String lpad(String strVal, int len, String ch) {

        StringBuffer zeroChar = new StringBuffer();
        for (int i = 0; i < (len - strVal.length()); i++) {
            zeroChar.append(ch);
        }
        return (zeroChar.toString() + strVal);
    }

    public static String rpad(String strVal, int len, char ch) {
        StringBuffer zeroChar = new StringBuffer();
        for (int i = 0; i < (len - strVal.length()); i++) {
            zeroChar.append(ch);
        }
        return (strVal + zeroChar.toString());
    }

    public static String rpad(String strVal, int len, String ch) {
        StringBuffer zeroChar = new StringBuffer();
        for (int i = 0; i < (len - strVal.length()); i++) {
            zeroChar.append(ch);
        }
        return (strVal + zeroChar.toString());
    }

    /**
     * 문자를 카멜케이스로 변경한다.
     *
     * @param txt
     * @return
     */
    public static String toCamelCase(String txt) {
        String[] words = txt.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    /**
     * HTML 특수문자 변환
     *
     * @param str  문자열
     * @param type 복호:de
     * @return
     */
    public static String convertHtml(String str, String type) {
        if (type.equals("de")) {
            str = str.replaceAll("&amp;", "&");
            str = str.replaceAll("&quot;", "\"");
            str = str.replaceAll("&lt;", "<");
            str = str.replaceAll("&gt;", ">");
            str = str.replaceAll("&#146;", "'");
        } else {
            str = str.replaceAll("&", "&amp;");
            str = str.replaceAll("\"", "&quot;");
            str = str.replaceAll("<", "&lt;");
            str = str.replaceAll(">", "&gt;");
            str = str.replaceAll("'", "&#146;");
        }
        return str;
    }
    /*
    * http://localhost:3000/APICALL/public/img?filePath=/data/upload/image/board/2024/11/c5c5bd33375abfae7bb18f92e2b35aa7be387cdaffd9202190f364797cb8c99c.png
    * */
    public static String setThumbnailUrlPath(ImgFileEntity imgInfo){
        return "?filePath=" + imgInfo.getImgpath() + "/" + imgInfo.getImghashname();
    }


}
