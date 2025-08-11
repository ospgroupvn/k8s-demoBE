package com.osp.bttp.common.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class UtilsDate {

    private static final Logger logger = LoggerFactory.getLogger(UtilsDate.class);
    private SimpleDateFormat calendarDatetimeSdf = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
    public static String dateFormatDDMMYYY = "dd/MM/yyyy HH:mm:ss";
    public static String formatDDMMYYY = "dd-MM-yyyy";
    public static String formatDateSQL = "yyyyMMddHHmiss";
    public static String formatDateInMariaDb = "%d/%m/%Y %H:%i:%s"; //01/12/2022 00:00:00
    public static String formatDateScr = "dd/MM/yyyy";
    public static String formatCode = "YYMMdd";
    public static String formatFileName = "YYYYMMdd";
    public static String formatDateTimeSQL = "YYYY-MM-dd HH:mm:ss"; //2022-08-18 09:48:22

    public static String sqlFormatyyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static String getLocalDateStr(LocalDate date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDDMMYYY);
        return date.format(formatter);
    }

    //Bat dau ngay
    public static Date startDay(Date date) {
        Date result = date;
        try {
            result = str2date(String.format("%s 00:00:00", date2str(date, formatDateScr)), dateFormatDDMMYYY);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    //ket thuc ngay
    public static Date endDay(Date date) {
        Date result = date;
        try {
            result = str2date(String.format("%s 23:59:59", date2str(date, formatDateScr)), dateFormatDDMMYYY);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public static Date str2date(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);//"yyyy-MM-dd HH:mm"
        Date date = null;
        try {
            if (StringUtils.isNotBlank(dateStr))
                date = sdf.parse(dateStr);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return date;
    }

    public static Date str2date(String dateStr, String format, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);//"yyyy-MM-dd HH:mm"
        Date date = null;
        try {
            sdf.setTimeZone(timeZone);
            if (StringUtils.isNotBlank(dateStr))
                date = sdf.parse(dateStr);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return date;
    }

    public static String date2str(Date input, String oFormat) {
        String result = "";
        if (input != null) {
            try {
                DateFormat df = new SimpleDateFormat(oFormat);
                result = df.format(input);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return result;
    }

    // cộng trừ ngày
    public static Date dayDate(Date input, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(input);
        c.roll(Calendar.DATE, day);
        return c.getTime();
    }

    // cộng trừ tháng
    public static Date monthDate(Date input, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(input);
        c.roll(Calendar.MONTH, day);
        return c.getTime();
    }

    // cộng trừ năm
    public static Date yearDate(Date input, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(input);
        c.roll(Calendar.YEAR, day);
        return c.getTime();
    }

    // date 1 lớn hơn date 2
    public static boolean compareDate(Date date1, Date date2) {
        return date1.after(date2);
    }

    // Đếm số ngày giữa 2 mốc thời gian
    public static long daysBetween2Dates(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        return (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);
    }

    // Kiểm tra có phải năm nhuận hay không
    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
            return true;
        }
        return false;
    }

    // Đếm số ngày trong một tháng
    public static int daysInMonth(Date input) {
        Calendar c = Calendar.getInstance();
        c.setTime(input);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int [] daysInMonths = {31,28,31,30,31,30,31,31,30,31,30,31};
        daysInMonths[1] += isLeapYear(year) ? 1 : 0;  // Sử dụng phương thức kiểm tra năm nhuần ở trên
        return daysInMonths[c.get(Calendar.MONTH)];
    }

    // So sánh giờ hiện tại có nằm trong khoảng giờ nào đó trong này hay không
    public static boolean isInHappyHour(String startHour, Date midlle, String endHour) throws ParseException, ParseException {
        boolean result = false;
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        Date start = hourFormat.parse(startHour);
        Date end = hourFormat.parse(endHour);

        String nowHourStr = hourFormat.format(midlle.getTime());
        try {
            Date nowHour = hourFormat.parse(nowHourStr);
            if (nowHour.after(start) && nowHour.before(end) || (nowHour.equals(start) || (nowHour.equals(end)))) {
                result = true;
            }
        } catch (ParseException e) {
            result = false;
        }
        return result;
    }

    
    public static String toStringCalendarDatetime(Date date) {
        UtilsDate constructor = new UtilsDate();
        if (date == null) {
            return "";
        }
        return constructor.calendarDatetimeSdf.format(date);
    }

    public static String toCalendarString(Calendar calendar) {
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);
        int second = calendar.get(calendar.SECOND);

        int day = calendar.get(calendar.DAY_OF_MONTH);
        int month = calendar.get(calendar.MONTH) + 1;
        int year = calendar.get(calendar.YEAR);
        String signDate = getNumber(day) + "/" + getNumber(month) + "/" + year + " " + getNumber(hour) + ":"
                + getNumber(minute) + ":" + getNumber(second);
        return signDate;
    }

    private static String getNumber(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return String.valueOf(number);
        }
    }
    
    // Xác định giữa 2 khoảng thời gian có bao nhiêu giờ, bao nhiêu phút, bao nhiêu giây:
    public static void main(String[] args) {

        String dateStart = "2012-03-14 09:33:58";

        String dateStop = "2012-03-14 10:34:59";

        // Custom date format

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;

        Date d2 = null;

        try {

            d1 = format.parse(dateStart);

            d2 = format.parse(dateStop);

        } catch (ParseException e) {

        }

        // Get msec from each, and subtract.

        long diff = d2.getTime() - d1.getTime();

        long diffSeconds = diff / 1000;

        long diffMinutes = diff / (60 * 1000);

        long diffHours = diff / (60 * 60 * 1000);

        //-----------------------------------------------------

        // LocalDate: mô tả kiểu dữ liệu date chỉ bao gồm ngày, tháng, năm
        // Thường được sử dụng để lưu trữ, mô tả: ngày sinh, ngày tốt nghiệp
        // hay ngày vào 1 công ty ...
        // Để sử dụng LocalDate chúng ta sẽ import gói thư viện java.time.LocalDate của Java
        // LocalDate.of(2010, 1, 15) sẽ trả về biến startDate có ngày, tháng, năm lần lượt là
        // 15, 1 và 2010
        LocalDate startDate = LocalDate.of(2010, 1, 15);

        LocalDate endDate = LocalDate.of(2011, 3, 18);

        // tính sự chênh lệch giữa startDate và endDate
        // sử dụng hàm between() của Peridot
        // Để sử dụng Peridot chúng ta sẽ import gói thư viện java.time.Period của Java
        Period different = Period.between(startDate, endDate);

    }

    public static Date startMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date addSeconds(Date date, int seconds) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        return calendar.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }

    public static Date getFirstDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);

        return calendar.getTime();
    }

    public static Date getLastDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));

        return calendar.getTime();
    }

    public static Date convertStringToDate(String dateStr, String format, Integer timeSelect) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            if (StringUtils.isNotBlank(dateStr))
                date = sdf.parse(dateStr);
            //if format = "dd/MM/yyyy" => get fist time of day start and get last time of day end
            if (format.equals(formatDateScr) && timeSelect.intValue() == 1) {
                date = startDay(date);
            }
            if (format.equals(formatDateScr) && timeSelect.intValue() == 2) {
                date = endDay(date);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return date;
    }
}
