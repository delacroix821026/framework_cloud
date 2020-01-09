package com.fw.common.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Excel统一POI处理类（针对2003以前和2007以后两种格式的兼容处理）
 * Created by wangweijun on 2018/01/19.
 * copy on http://cgs1999.iteye.com/blog/1525665
 */
public abstract class PoiExcelHelper {
    public static final String SEPARATOR = ",";
    public static final String CONNECTOR = "-";

    /**
     * 获取sheet列表，子类必须实现
     *
     * @param inputStream InputStream
     * @return ArrayList
     */
    public abstract ArrayList<String> getSheetList(InputStream inputStream);

    /**
     * 读取Excel文件数据
     *
     * @param inputStream InputStream
     * @param sheetIndex  int
     * @return ArrayList
     */
    public ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex) {
        return readExcel(inputStream, sheetIndex, "1-", "1-");
    }

    /**
     * 读取Excel文件数据
     *
     * @param inputStream InputStream
     * @param sheetIndex  int
     * @param rows        String
     * @return ArrayList
     */
    public ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex, String rows) {
        return readExcel(inputStream, sheetIndex, rows, "1-");
    }

    /**
     * 读取Excel文件数据
     *
     * @param inputStream InputStream
     * @param sheetIndex  int
     * @param columns     String[]
     * @return ArrayList
     */
    public ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex, String[] columns) {
        return readExcel(inputStream, sheetIndex, "1-", columns);
    }

    /**
     * 读取Excel文件数据，子类必须实现
     *
     * @param inputStream InputStream
     * @param sheetIndex  int
     * @param rows        String
     * @param columns     String
     * @return ArrayList
     */
    public abstract ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex, String rows, String columns);

    /**
     * 读取Excel文件数据
     *
     * @param inputStream InputStream
     * @param sheetIndex  int
     * @param rows        String
     * @param columns     String[]
     * @return ArrayList
     */
    public ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex, String rows, String[] columns) {
        int[] cols = getColumnNumber(columns);

        return readExcel(inputStream, sheetIndex, rows, cols);
    }

    /**
     * 读取Excel文件数据，子类必须实现
     *
     * @param inputStream InputStream
     * @param sheetIndex  int
     * @param rows        String
     * @param cols        int[]
     * @return ArrayList
     */
    public abstract ArrayList<ArrayList<String>> readExcel(InputStream inputStream, int sheetIndex, String rows, int[] cols);

    /**
     * 读取Excel文件内容
     *
     * @param sheet Sheet
     * @param rows  String
     * @param cols  int[]
     * @return ArrayList
     */
    protected ArrayList<ArrayList<String>> readExcel(Sheet sheet, String rows, int[] cols) {
        ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
        // 处理行信息，并逐行列块读取数据
        String[] rowList = rows.split(SEPARATOR);
        for (String rowStr : rowList) {
            if (rowStr.contains(CONNECTOR)) {
                String[] rowArr = rowStr.trim().split(CONNECTOR);
                int start = Integer.parseInt(rowArr[0]) - 1;
                int end;
                if (rowArr.length == 1) {
                    end = sheet.getLastRowNum();
                } else {
                    end = Integer.parseInt(rowArr[1].trim()) - 1;
                }
                dataList.addAll(getRowsValue(sheet, start, end, cols));
            } else {
                dataList.add(getRowValue(sheet, Integer.parseInt(rowStr) - 1, cols));
            }
        }
        return dataList;
    }

    /**
     * 获取连续行、列数据
     *
     * @param sheet    Sheet
     * @param startRow int
     * @param endRow   int
     * @param startCol int
     * @param endCol   int
     * @return ArrayList
     */
    protected ArrayList<ArrayList<String>> getRowsValue(Sheet sheet, int startRow, int endRow,
                                                        int startCol, int endCol) {
        if (endRow < startRow || endCol < startCol) {
            return null;
        }

        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        for (int i = startRow; i <= endRow; i++) {
            data.add(getRowValue(sheet, i, startCol, endCol));
        }
        return data;
    }

    /**
     * 获取连续行、不连续列数据
     *
     * @param sheet    Sheet
     * @param startRow int
     * @param endRow   int
     * @param cols     int[]
     * @return ArrayList
     */
    private ArrayList<ArrayList<String>> getRowsValue(Sheet sheet, int startRow, int endRow, int[] cols) {
        if (endRow < startRow) {
            return null;
        }

        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        for (int i = startRow; i <= endRow; i++) {
            data.add(getRowValue(sheet, i, cols));
        }
        return data;
    }

    /**
     * 获取行连续列数据
     *
     * @param sheet    Sheet
     * @param rowIndex int
     * @param startCol int
     * @param endCol   int
     * @return ArrayList
     */
    private ArrayList<String> getRowValue(Sheet sheet, int rowIndex, int startCol, int endCol) {
        if (endCol < startCol) {
            return null;
        }

        Row row = sheet.getRow(rowIndex);
        ArrayList<String> rowData = new ArrayList<String>();
        for (int i = startCol; i <= endCol; i++) {
            rowData.add(getCellValue(row, i));
        }
        return rowData;
    }

    /**
     * 获取行不连续列数据
     *
     * @param sheet    Sheet
     * @param rowIndex int
     * @param cols     int[]
     * @return ArrayList
     */
    private ArrayList<String> getRowValue(Sheet sheet, int rowIndex, int[] cols) {
        Row row = sheet.getRow(rowIndex);
        ArrayList<String> rowData = new ArrayList<String>();
        for (int colIndex : cols) {
            rowData.add(getCellValue(row, colIndex));
        }
        return rowData;
    }

    /**
     * 获取单元格内容
     *
     * @param row    Row
     * @param column a excel column string like 'A', 'C' or "AA".
     * @return String
     */
    protected String getCellValue(Row row, String column) {
        return getCellValue(row, getColumnNumber(column));
    }

    /**
     * 获取单元格内容
     *
     * @param row Row
     * @param col a excel column index from 0 to 65535
     * @return String
     */
    private String getCellValue(Row row, int col) {
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(col);
        return getCellValue(cell);
    }

    /**
     * 获取单元格内容
     *
     * @param cell Cell
     * @return String
     */
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

//        String value = cell.toString().trim();
        String value = getStringVal(cell);
        try {
            // This step is used to prevent Integer string being output with
            // '.0'.
            Float.parseFloat(value);
            value = value.replaceAll("\\.0$", "");
            value = value.replaceAll("\\.0+$", "");
            return value;
        } catch (NumberFormatException ex) {
            return value;
        }
    }

    /**
     * Change excel column letter to integer number
     *
     * @param columns column letter of excel file, like A,B,AA,AB
     * @return int[]
     */
    private int[] getColumnNumber(String[] columns) {
        int[] cols = new int[columns.length];
        for (int i = 0; i < columns.length; i++) {
            cols[i] = getColumnNumber(columns[i]);
        }
        return cols;
    }

    /**
     * Change excel column letter to integer number
     *
     * @param column column letter of excel file, like A,B,AA,AB
     * @return int
     */
    private int getColumnNumber(String column) {
        int length = column.length();
        short result = 0;
        for (int i = 0; i < length; i++) {
            char letter = column.toUpperCase().charAt(i);
            int value = letter - 'A' + 1;
            result += value * Math.pow(26, length - i - 1);
        }
        return result - 1;
    }

    /**
     * Change excel column string to integer number array
     *
     * @param sheet   excel sheet
     * @param columns column letter of excel file, like A,B,AA,AB
     * @return int[]
     */
    protected int[] getColumnNumber(Sheet sheet, String columns) {
        // 拆分后的列为动态，采用List暂存
        ArrayList<Integer> result = new ArrayList<Integer>();
        String[] colList = columns.split(SEPARATOR);
        for (String colStr : colList) {
            if (colStr.contains(CONNECTOR)) {
                String[] colArr = colStr.trim().split(CONNECTOR);
                int start = Integer.parseInt(colArr[0]) - 1;
                int end;
                if (colArr.length == 1) {
                    end = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() - 1;
                } else {
                    end = Integer.parseInt(colArr[1].trim()) - 1;
                }
                for (int i = start; i <= end; i++) {
                    result.add(i);
                }
            } else {
                result.add(Integer.parseInt(colStr) - 1);
            }
        }

        // 将List转换为数组
        int len = result.size();
        int[] cols = new int[len];
        for (int i = 0; i < len; i++) {
            cols[i] = result.get(i).intValue();
        }

        return cols;
    }

    /**
     * 解决读取数字和日期格式的问题
     *
     * @param cell Cell
     * @return String
     */
    public String getStringVal(Cell cell) {
        switch (cell.getCellType()) {
//            case HSSFCell.CELL_TYPE_BLANK:
//                return "";
//            case HSSFCell.CELL_TYPE_ERROR:
//                return cell.toString();
//            case HSSFCell.CELL_TYPE_BOOLEAN:
//                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
//            case HSSFCell.CELL_TYPE_FORMULA:
//                return cell.getCellFormula();
            case Cell.CELL_TYPE_NUMERIC:
                cell.setCellType(Cell.CELL_TYPE_STRING);
//                formatter.formatCellValue(sheet.getRow(col).getCell(row));
                return cell.getStringCellValue();
//            case HSSFCell.CELL_TYPE_STRING:
//                return cell.getStringCellValue();
            default:
                return cell.toString();
        }
    }
}
