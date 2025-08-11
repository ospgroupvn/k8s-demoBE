package com.osp.bttp.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static String convertDateToStringWithType(Date date, String type) {
        if (date == null) {
            return null;
        }
        return new java.text.SimpleDateFormat(type).format(date);
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static String date2str(Date input, String oFormat) {
        String result = "";
        if (input != null) {
            try {
                DateFormat df = new SimpleDateFormat(oFormat);
                result = df.format(input);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static Date str2date(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);//"yyyy-MM-dd HH:mm"
        Date date = null;
        try {
            if (StringUtils.isNotBlank(dateStr)) {
                String[] parts = dateStr.split("/");
                if (parts[1].length() == 1) {
                    parts[1] = "0" + parts[1]; // Thêm 0 vào tháng nếu chỉ có 1 chữ số
                }
                String formattedDate = String.join("/", parts);
                date = sdf.parse(formattedDate);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return date;
    }
}
