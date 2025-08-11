package com.osp.bttp.common.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;


/**
 * @author sangnk
 * @Created 15/10/2024 - 9:59 SA
 * @project = bttp
 * @_ Mô tả:
 */
@Slf4j
public class ExcelUtils {

    public static void exportExcel(HttpServletResponse httpServletResponse, List<String> headers, List<List<String>> dataExport,
                                   String fileName, String sheetName, String title, String subTitle) throws IOException {
        XSSFWorkbook workbook = null;
        try {

            // Create a new workbook and sheet
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            // Set up styles for title, subtitle, headers, and data
            CellStyle titleStyle = workbook.createCellStyle();
            XSSFFont titleFont = workbook.createFont();
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle subtitleStyle = workbook.createCellStyle();
            XSSFFont subtitleFont = workbook.createFont();
            subtitleFont.setFontHeightInPoints((short) 12);
            subtitleStyle.setFont(subtitleFont);
            subtitleStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle headerStyle = createBorderedStyle(workbook);
            XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle dataStyle = createBorderedStyle(workbook);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);

            // Create the title row (row 0)
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(titleStyle);
            // Merge title row across all header columns
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.size() - 1));

            // Create the subtitle row (row 1)
            Row subTitleRow = sheet.createRow(1);
            Cell subTitleCell = subTitleRow.createCell(0);
            subTitleCell.setCellValue(subTitle);
            subTitleCell.setCellStyle(subtitleStyle);
            // Merge subtitle row across all header columns
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, headers.size() - 1));

            // Create the header row (row 2)
            Row headerRow = sheet.createRow(2);
            for (int i = 0; i < headers.size(); i++) {
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(headers.get(i));
                headerCell.setCellStyle(headerStyle);
            }

            // Create the data rows starting from row 3
            int rowNum = 3;
            for (List<String> rowData : dataExport) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < rowData.size(); i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(rowData.get(i));
                    cell.setCellStyle(dataStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            // Set the content type and headers for the response
            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

            // Write the workbook to the response output stream
            workbook.write(httpServletResponse.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("Error exporting Excel file: {}", e.getMessage());
            throw new IOException("Error exporting Excel file", e);
        } finally {
            MethodUtils.closeRessource(workbook);
        }
    }

    // Utility method to create a bordered style
    private static CellStyle createBorderedStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}

