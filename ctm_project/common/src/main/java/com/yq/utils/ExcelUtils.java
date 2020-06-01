package com.yq.utils;

import com.yq.entity.exl.ContentExl;
import com.yq.entity.exl.OutInfoExl;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
/**
 * excel表格导出
 * @Author qf
 * @Date 2019/8/24
 * @Version 1.0
 */
@Slf4j
public class ExcelUtils {

    static final short borderpx = 1;

    /**
     * 导出excel表格
     * @param head
     * @param body
     * @return
     */
    public static HSSFWorkbook expExcel(List<String> head, List<List<String>> body, Map<Integer,Double> map) {
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

        //判断map是否为空
        if (map != null) {
            //创建最后一行
            row = sheet.createRow(body.size() + 1);
            //遍历map
            Set<Map.Entry<Integer,Double>> entries = map.entrySet();

            for (Map.Entry<Integer,Double> entry : new HashSet<>(entries)) {
                cell = row.createCell(entry.getKey());
                cell.setCellValue(entry.getValue());
                cell.setCellStyle(cellStyle2);
            }
        }

        return workbook;
    }


    /**
     * 用模板
     * 导出excel表格
     * @param body
     * @return
     */
    public static Workbook exp2Excel(List<List<String>> body, ContentExl contentExl) {

        Workbook workbook = null;
        try {

//            FileInputStream is = new FileInputStream("E:\\MyWork\\导游接待生成表.xls");
            File directory = new File("");
            String coursePath = directory.getCanonicalPath();

            String path = coursePath + "/exl/doc/导游接待生成表.xls";

            FileInputStream is = new FileInputStream(path);

            workbook = WorkbookFactory.create (is);

            Sheet sheet = workbook.getSheet("Sheet1");

            Row row;

            Cell cell= null;

            CellStyle cellStyle2 = workbook.createCellStyle();
            setBorderStyle((HSSFCellStyle) cellStyle2, borderpx);
            cellStyle2.setFont(setFontStyle((HSSFWorkbook) workbook, "宋体", (short) 12));
            cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            CellStyle cellStyle3 = workbook.createCellStyle();
            setBorderStyle((HSSFCellStyle) cellStyle3, borderpx);
            cellStyle3.setFont(setFontStyle((HSSFWorkbook) workbook, "宋体", (short) 10));
            cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            CellStyle cellStyle4 = workbook.createCellStyle();
            setBorderStyle((HSSFCellStyle) cellStyle4, borderpx);
            cellStyle4.setFont(setFontStyle((HSSFWorkbook) workbook, "宋体", (short) 10));
            cellStyle4.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            CellStyle cellStyle5 = workbook.createCellStyle();
            setBorderStyle((HSSFCellStyle) cellStyle5, borderpx);
            cellStyle5.setFont(setFontStyle((HSSFWorkbook) workbook, "宋体", (short) 12));
            cellStyle5.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            //开始行
            Integer start = 4;
            if (body.size() > 2) {
                sheet.shiftRows(start, sheet.getLastRowNum(), body.size() - 2, true, false);

                Row row1 = sheet.getRow(2);
                CellStyle rowStyle = row1.getRowStyle();

                for (int i = start; i < start + body.size() - 2; i++) {
                    Row row2 = sheet.createRow(i);
                    if (rowStyle != null) {
                        row2.setRowStyle(rowStyle);
                        row2.setHeight(row1.getHeight());
                    }

                    for (int col = 0; col < row1.getLastCellNum(); col++) {
                        Cell cell1 = row1.getCell(col);
                        Cell cell2 = row2.createCell(col);
                        CellStyle style = cell1.getCellStyle();
                        if (style != null) {
                            cell2.setCellStyle(cell1.getCellStyle());
                        }

                    }
                }
            }

            for (int i = 0; i < body.size(); i++) {
                row = sheet.createRow(i + 2);
                List<String> paramList = body.get(i);
                for (int p = 0; p < paramList.size(); p++) {
                    cell = row.createCell(p);
                    cell.setCellValue(paramList.get(p));
                    cell.setCellStyle(cellStyle2);
                    if (p==2||p==11) {
                        cell.setCellStyle(cellStyle4);
                    }
                }
            }

            Integer size = body.size();
            if (body.size() <= 1) {
                size = 2;
            }

            //人数
            Row sheetRow = sheet.getRow(size+2);
            Cell rowCell = sheetRow.getCell(1);
            rowCell.setCellValue(contentExl.getNumber());
            rowCell.setCellStyle(cellStyle3);
            //总人数
            Cell cell1 = sheetRow.getCell(4);
            cell1.setCellValue(contentExl.getAllNumber());
            cell1.setCellStyle(cellStyle2);

            //本单团费收入
            Row sheetRow1 = sheet.getRow(size+3);
            Cell rowCell1 = sheetRow1.getCell(1);
            rowCell1.setCellValue(contentExl.getIncomeMoney());
            rowCell1.setCellStyle(cellStyle2);
            //经手人   客户要求不显示这一列
//            Cell row2 = sheetRow1.getCell(13);
//            row2.setCellValue(contentExl.getGuideName());
//            row2.setCellStyle(cellStyle2);

            //合计收入
            Cell cell2 = sheetRow1.getCell(3);
            cell2.setCellValue(contentExl.getAllPrice());
            cell2.setCellStyle(cellStyle2);

            //额外代收
            Row sheetRow2 = sheet.getRow(size+4);
            Cell rowCell2 = sheetRow2.getCell(1);
            rowCell2.setCellValue(contentExl.getMsPrice());
            rowCell2.setCellStyle(cellStyle2);

            //其他收入
            Row sheetRow3 = sheet.getRow(size+5);
            Cell rowCell3 = sheetRow3.getCell(1);
            rowCell3.setCellValue(contentExl.getOtherPrice());
            rowCell3.setCellStyle(cellStyle2);

            //本单利润
            Row sheetRow4 = sheet.getRow(size+6);
            Cell rowCell4 = sheetRow4.getCell(0);
            rowCell4.setCellValue(contentExl.getSetPrice());
            rowCell4.setCellStyle(cellStyle2);

            //报表人   财务审核人
            Row sheetRow5 = sheet.getRow(size+8);
            Cell rowCell5 = sheetRow5.getCell(1);
            rowCell5.setCellValue(contentExl.getGuideName());
            rowCell5.setCellStyle(cellStyle5);

            Cell rowCell6 = sheetRow5.getCell(11);
            if (StringUtils.isBlank(contentExl.getFinanceName())) {
                rowCell6.setCellValue ("");
            } else {
                rowCell6.setCellValue(contentExl.getFinanceName());
            }
            rowCell6.setCellStyle(cellStyle2);

            //当前车辆
            Cell rowCell7 = sheetRow5.getCell(3);
            rowCell7.setCellValue(contentExl.getCarNumber());
            rowCell7.setCellStyle(cellStyle5);

            Double allOutPrice = 0.0;
            //支出信息
            for (int i = 0; i<= contentExl.getList().size()-1; i++) {
                Row rows = sheet.getRow(size+2 + i);
                //门票
                Cell cellName = rows.getCell(6);
                cellName.setCellValue(contentExl.getList().get(i).getOutName());
                cellName.setCellStyle(cellStyle2);
                //门票价格
                Cell cellPrice = rows.getCell(11);
                cellPrice.setCellValue(contentExl.getList().get(i).getOutPrice());
                cellPrice.setCellStyle(cellStyle2);

                allOutPrice += contentExl.getList().get(i).getOutPrice();

            }

            //合计支出
            Row outRow = sheet.getRow(size+3);
            Cell outCell = outRow.getCell(12);
            outCell.setCellValue(allOutPrice);
            outCell.setCellStyle(cellStyle2);

        } catch (Exception e) {
            e.printStackTrace();
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
     * 文件输出
     * @param workbook 填充好的workbook
     * @param path 存放的位置
     */
    public static void outFile2(Workbook workbook, String path, HttpServletResponse response) {
        SimpleDateFormat fdate=new SimpleDateFormat("yyyyMMdd-HH点mm分");
        path = path.substring(0, path.lastIndexOf(".")) + fdate.format(new Date()) + path.substring(path.lastIndexOf("."));
        OutputStream os=null;
        File file = null;
        System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.print(path);
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
