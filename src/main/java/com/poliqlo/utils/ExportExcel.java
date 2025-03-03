package com.poliqlo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExportExcel<Type> {
    public ByteArrayInputStream export(List<Type> lstObj) throws IOException {
        if (lstObj == null || lstObj.isEmpty()) return null;


        Class<?> clazz = lstObj.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();
        //Tạo wb
        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyle style = workbook.createCellStyle();

            // Thiết lập màu nền của ô là màu xanh
            style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Tạo một Font mới cho chữ màu trắng
            Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);


            Sheet sheet = workbook.createSheet(clazz.getSimpleName());

            // Tạo header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fields.length; i++) {
                var cell =headerRow.createCell(i);
                cell.setCellValue(fields[i].getName());
                cell.setCellStyle(style);
            }
            // Tạo data row
            for (int i = 0; i < lstObj.size(); i++) {//Duyệt qua từng phần tử
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < fields.length; j++) {
                    fields[j].setAccessible(true); // Cho phép truy cập các trường private
                    Object value;
                    try {
                        value = fields[j].get(lstObj.get(i));
                    } catch (IllegalAccessException e) {
                        value = "N/A";
                    }
                    row.createCell(j).setCellValue(value==null?"":value.toString());
                }

            }
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return new ByteArrayInputStream(out.toByteArray());
            }
        }
    }
}


