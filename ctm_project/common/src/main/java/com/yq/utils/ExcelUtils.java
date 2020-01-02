package com.yq.utils;

import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * excel表格导出
 * @Author qf
 * @Date 2019/8/24
 * @Version 1.0
 */
public class ExcelUtils {

    static final short borderpx = 1;

    /**
     * 导出excel表格
     * @param head
     * @param body
     * @return
     */
    public static HSSFWorkbook expExcel(List<String> head, List<List<String>> body) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell= null;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorderStyle(cellStyle, borderpx);
        cellStyle.setFont(setFontStyle(workbook, "黑体", (short) 14));
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet.createFreezePane(0,1,0,1);

        for (int i = 0; i<head.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(head.get(i));
            cell.setCellStyle(cellStyle);
        }

        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        setBorderStyle(cellStyle2, borderpx);
        cellStyle2.setFont(setFontStyle(workbook, "宋体", (short) 12));
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        for (int i = 0; i < body.size(); i++) {
            row = sheet.createRow(i + 1);
            List<String> paramList = body.get(i);
            for (int p = 0; p < paramList.size(); p++) {
                cell = row.createCell(p);
                cell.setCellValue(paramList.get(p));
                cell.setCellStyle(cellStyle2);
            }
        }
        for (int i = 0, isize = head.size(); i < isize; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }


    /**
     * 以同样
     * 导出excel表格
     * @param body
     * @return
     */
    public static HSSFWorkbook exp2Excel(List<List<String>> body) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");
        HSSFRow row = sheet.createRow(0);
        HSSFRow row1 = sheet.createRow(1);
        HSSFCell cell= null;
        List<String> head = new ArrayList<>();
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        setBorderStyle(cellStyle, borderpx);
        cellStyle.setFont(setFontStyle(workbook, "黑体", (short) 14));
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        sheet.createFreezePane(0,1,0,1);

        head.add("同行名称");
        head.add("出行日期");
        head.add("线路名称");
        head.add("游客姓名");
        head.add("联系电话");
        head.add("人数");
        head.add("合计");
        head.add("费用总额");
        head.add("接送地址");
        head.add("备注");
        head.add("接待人");
        for (int i = 0; i<head.size(); i++) {
            if (i == 5) {
                cell = row1.createCell(i);
                cell.setCellValue("成人");
                cell.setCellValue("小童");
                cell.setCellValue("老人");
                cell.setCellValue("幼儿");
                cell.setCellStyle(cellStyle);
            }
            cell = row.createCell(i);
            cell.setCellValue(head.get(i));
            cell.setCellStyle(cellStyle);
        }

        HSSFCellStyle cellStyle2 = workbook.createCellStyle();
        setBorderStyle(cellStyle2, borderpx);
        cellStyle2.setFont(setFontStyle(workbook, "宋体", (short) 12));
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        for (int i = 0; i < body.size(); i++) {
            row = sheet.createRow(i + 1);
            List<String> paramList = body.get(i);
            for (int p = 0; p < paramList.size(); p++) {
                cell = row.createCell(p);
                cell.setCellValue(paramList.get(p));
                cell.setCellStyle(cellStyle2);
            }
        }
        for (int i = 0, isize = head.size(); i < isize; i++) {
            sheet.autoSizeColumn(i);
        }
        return workbook;
    }


    /**
     * 文件输出
     * @param workbook 填充好的workbook
     * @param path 存放的位置
     */
    public static void outFile(HSSFWorkbook workbook, String path, HttpServletResponse response) {
        SimpleDateFormat fdate=new SimpleDateFormat("yyyyMMdd-HH点mm分");
        path = path.substring(0, path.lastIndexOf(".")) + fdate.format(new Date()) + path.substring(path.lastIndexOf("."));
        OutputStream os=null;
        File file = null;
        try {
            file = new File(path);
            String filename = file.getName();
            os = new FileOutputStream(file);
            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8"));
            os= new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            workbook.write(os);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.flush();
            os.close();
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置字体样式
     * @param workbook 工作簿
     * @param name 字体类型
     * @param height 字体大小
     * @return HSSFFont
     */
    private static HSSFFont setFontStyle(HSSFWorkbook workbook, String name, short height) {
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(height);
        font.setFontName(name);
        return font;
    }

    /**
     * 设置单元格样式
     * @param cellStyle 工作簿
     * @param border border样式
     */
    private static void setBorderStyle(HSSFCellStyle cellStyle, short border) {
        // 下边框
        cellStyle.setBorderBottom(border);
        // 左边框
        cellStyle.setBorderLeft(border);
        // 上边框
        cellStyle.setBorderTop(border);
        // 右边框
        cellStyle.setBorderRight(border);
    }

}
