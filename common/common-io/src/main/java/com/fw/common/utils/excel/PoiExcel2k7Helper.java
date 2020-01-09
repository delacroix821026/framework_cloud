package com.fw.common.utils.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Excel 读取（2007+新格式）
 * Created by wangweijun on 2018/01/19.
 */
public class PoiExcel2k7Helper extends PoiExcelHelper {
    /**
     * 获取sheet列表
     */
    public ArrayList<String> getSheetList(InputStream inputStream) {
        ArrayList<String> sheetList = new ArrayList<String>(0);
        try {
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            int sheetSize = wb.getNumberOfSheets();
            for (int i = 0; i < sheetSize; i++){
                sheetList.add(wb.getSheetName(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sheetList;
    }

    /**
     * 读取Excel文件内容
     */
    public ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex, String rows, String columns) {
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
        try {
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = wb.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, getColumnNumber(sheet, columns));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 读取Excel文件内容
     */
    public ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex, String rows, int[] cols) {
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
        try {
            XSSFWorkbook wb = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = wb.getSheetAt(sheetIndex);

            dataList = readExcel(sheet, rows, cols);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
