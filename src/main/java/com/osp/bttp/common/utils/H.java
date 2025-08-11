package com.osp.bttp.common.utils;

import com.osp.bttp.dao.model.entity.base.Creatable;
import org.springframework.util.MultiValueMap;

import jakarta.servlet.http.HttpServletRequest;

import java.io.Reader;
import java.sql.Clob;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class H {
    public static Boolean isTrue(Object value) {

        if (value == null) return false;

        if (value instanceof String ) {
            if (value.equals("null") || value.equals("undefined")) {
                return false;
            }
        }

        if (value instanceof String) return !((String) value).trim().isEmpty();



//        if (value instanceof Number) return !((Number) value).equals(Long.valueOf(0));

        if (value instanceof Boolean) return (Boolean) value;

        if (value instanceof Collection) return !((Collection) value).isEmpty();

        if (value instanceof Object[]) return ((Object[]) value).length > 0;


        return true;
    }

    public static String toQueryString(MultiValueMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            for (String value : params.get(key)) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(value);
            }
        }
        return sb.toString();
    }

    public static String encodeCCCD(String id_passpost) {
//        if (id_passpost == null) {
//            return id_passpost;
//        }
//        if (id_passpost.length() <= 7) {
//            return id_passpost;
//        }
//        if (id_passpost.length() <= 10) {
//            return id_passpost.substring(0, 6) + "****";
//        }
//        if (id_passpost.length() <= 12) {
//            //mã hóa 4 số đầu và 4 số cuối ( vd: 12345678900 -> ****567****)
//            return "****" + id_passpost.substring(4, id_passpost.length() - 4) + "****";
//        }
//       return id_passpost;

        //mã hóa các ký tự đứng truoc 3 số cuối ( vd: 12345678900 -> ******123)
        if (id_passpost == null) {
            return id_passpost;
        }
        if (id_passpost.length() <= 3) {
            return id_passpost;
        }
        String last3 = id_passpost.substring(id_passpost.length() - 3);
        String first = "";
        for (int i = 0; i < id_passpost.length() - 3; i++) {
            first += "*";
        }
        return first + last3;
    }

    public static boolean isNumber(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String convertObjectToJson(Map<String, Object> mapData) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Object> entry : mapData.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                sb.append("\"").append(entry.getValue()).append("\",");
            } else {
                sb.append(entry.getValue()).append(",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    public static String convertClobToString(Clob clob) {
        try {
            Reader r = clob.getCharacterStream();
            StringBuffer buffer = new StringBuffer();
            int ch;
            while ((ch = r.read()) != -1) {
                buffer.append("" + (char) ch);
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void each(List<T> list, Each<T> each) throws RuntimeException {

        if (!H.isTrue(list)) {
            return;
        }

        for (int index = 0; index < list.size(); index++) {
            each.do_(index, list.get(index));
        }
    }
    public interface Each<T> {

        void do_(int index, T item) throws RuntimeException;
    }
}
