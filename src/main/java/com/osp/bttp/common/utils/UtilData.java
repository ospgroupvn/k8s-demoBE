package com.osp.bttp.common.utils;

/**
 * @author sangnk
 * @Created 09/10/2024 - 8:14 CH
 * @project = bttp
 * @_ Mô tả:
 */
public class UtilData {
    public static String paginationOracle(String sql, int offset, int pageNumber) {
        return "SELECT * FROM (SELECT pgn.*, DECODE(ROWNUM, NULL, 0, ROWNUM) AS R__ FROM (" + sql + ") pgn WHERE ROWNUM <= " + (offset + pageNumber) + ") WHERE R__ > " + offset;
    }


}
