package com.lingx.core.utils;

import java.util.List;
import java.util.Map;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
/** 
* @author www.lingx.com
* @version 创建时间：2021年1月19日 下午3:56:01 
* 类说明 
*/
public class ExcelUtils {
	public static void main(String[] args) {
		String path="D:/1541229585155.xlsx";
		try {
			System.out.println((ExcelUtils.readExcel(path)).size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Logger mLogger = LoggerFactory.getLogger(ExcelUtils.class);
	
	public static List<List<Object>> readList(String filepath){
		try {
			return readExcel(filepath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    /**
     * 对外提供读取excel的方法， 当且仅当只有一个sheet， 默认从第一个sheet读取数据
     * @param file
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, 0);
                list = getSheetData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet数据
     * @param file
     * @param sheetIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, int sheetIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, sheetIndex);
                list = getSheetData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param file
     * @param sheetIndex
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, int sheetIndex, int startRowIndex, int endRowIndex,
            int startColumnIndex, int endColumnIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(file);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetIndex);
            list = getSheetData(wb, sheet, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet数据
     * @param file
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, String sheetName) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(file);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetName);
            list = getSheetData(wb, sheet);
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param file
     * @param sheetName
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(File file, String sheetName, int startRowIndex, int endRowIndex,
            int startColumnIndex, int endColumnIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Workbook wb = getWorkbook(file);
        if (wb != null) {
            Sheet sheet = getSheet(wb, sheetName);
            list = getSheetData(wb, sheet, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
        }
        return list;
    }

    /**
     * 读取excel的正文内容
     * @param file
     * @param sheetIndex sheet的下标索引值
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(File file, int sheetIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, sheetIndex);
                list = getSheetBodyData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * 读取excel的正文内容
     * @param file
     * @param sheetName sheet的名称
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(File file, String sheetName) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (file.exists() && file.isFile()) {
            Workbook wb = getWorkbook(file);
            if (wb != null) {
                Sheet sheet = getSheet(wb, sheetName);
                list = getSheetBodyData(wb, sheet);
            }
        }
        return list;
    }

    /**
     * 对外提供读取excel的方法， 当且仅当只有一个sheet， 默认从第一个sheet读取数据
     * @param filePath
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath) throws IOException {
        File file = new File(filePath);
        return readExcel(file);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet数据
     * @param filePath
     * @param sheetIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, int sheetIndex) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetIndex);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet下标索引读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param filePath
     * @param sheetIndex
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, int sheetIndex, int startRowIndex, int endRowIndex,
            int startColumnIndex, int endColumnIndex) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetIndex, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet数据
     * @param filePath
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, String sheetName) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetName);
    }

    /**
     * 对外提供读取excel的方法， 根据sheet名称读取sheet对象， 并指定行列区间获取数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param filePath
     * @param sheetName
     * @param startRowIndex
     * @param endRowIndex
     * @param startColumnIndex
     * @param endColumnIndex
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcel(String filePath, String sheetName, int startRowIndex, int endRowIndex,
            int startColumnIndex, int endColumnIndex) throws IOException {
        File file = new File(filePath);
        return readExcel(file, sheetName, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex);
    }

    /**
     * 读取excel的正文内容
     * @param filePath
     * @param sheetIndex sheet的下标索引值
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(String filePath, int sheetIndex) throws IOException {
        File file = new File(filePath);
        return readExcelBody(file, sheetIndex);
    }

    /**
     * 读取excel的正文内容
     * @param filePath
     * @param sheetName sheet的名称
     * @return
     * @throws IOException
     */
    public static List<List<Object>> readExcelBody(String filePath, String sheetName) throws IOException {
        File file = new File(filePath);
        return readExcelBody(file, sheetName);
    }

    /**
     * 根据workbook获取该workbook的所有sheet
     * @param wb
     * @return List<Sheet>
     */
    public static List<Sheet> getAllSheets(Workbook wb) {
        int numOfSheets = wb.getNumberOfSheets();
        List<Sheet> sheets = new ArrayList<Sheet>();
        for (int i = 0; i < numOfSheets; i++) {
            sheets.add(wb.getSheetAt(i));
        }
        return sheets;
    }

    /**
     * 根据excel文件来获取workbook
     * @param file
     * @return workbook
     * @throws IOException
     */
    public static Workbook getWorkbook(File file) throws IOException {
        Workbook wb = null;
        if (file.exists() && file.isFile()) {
            String fileName = file.getName();
            String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1); // 获取文件后缀

            if ("xls".equals(extension)) { // for office2003
                wb = new HSSFWorkbook(new FileInputStream(file));
            } else if ("xlsx".equals(extension)) { // for office2007
                wb = new XSSFWorkbook(new FileInputStream(file));
            } else {
                throw new IOException("不支持的文件类型");
            }
        }
        return wb;
    }

    /**
     * 根据excel文件来获取workbook
     * @param filePath
     * @return workbook
     * @throws IOException
     */
    public static Workbook getWorkbook(String filePath) throws IOException {
        File file = new File(filePath);
        return getWorkbook(file);
    }

    /**
     * 根据excel文件输出路径来获取对应的workbook
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Workbook getExportWorkbook(String filePath) throws IOException {
        Workbook wb = null;
        File file = new File(filePath);

        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1); // 获取文件后缀

        if ("xls".equals(extension)) { // for 少量数据
            wb = new HSSFWorkbook();
        } else if ("xlsx".equals(extension)) { // for 大量数据
            wb = new SXSSFWorkbook(5000); // 定义内存里一次只留5000行
        } else {
            throw new IOException("不支持的文件类型");
        }
        return wb;
    }

    /**
     * 根据workbook和sheet的下标索引值来获取sheet
     * @param wb
     * @param sheetIndex
     * @return sheet
     */
    public static Sheet getSheet(Workbook wb, int sheetIndex) {
        return wb.getSheetAt(sheetIndex);
    }

    /**
     * 根据workbook和sheet的名称来获取sheet
     * @param wb
     * @param sheetName
     * @return sheet
     */
    public static Sheet getSheet(Workbook wb, String sheetName) {
        return wb.getSheet(sheetName);
    }

    /**
     * 根据sheet获取该sheet的所有数据
     * @param sheet
     * @return
     */
    public static List<List<Object>> getSheetData(Workbook wb, Sheet sheet) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            boolean allRowIsBlank = isBlankRow(wb, row);
            if (allRowIsBlank) { // 整行都空，就跳过
                continue;
            }
            List<Object> rowData = getRowData(wb, row);
            list.add(rowData);
        }
        return list;
    }

    /**
     * 读取正文数据， 从第二行起
     * @param wb
     * @param sheet
     * @return
     * @throws IOException 
     */
    public static List<List<Object>> getSheetBodyData(Workbook wb, Sheet sheet) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        // 获取标题行
        Row headerRow = sheet.getRow(0);
        // 标题总列数
        int colNum = headerRow.getPhysicalNumberOfCells();
        // 获取正文内容， 正文内容应该从第二行开始,第一行为表头的标题
        list.addAll(getSheetData(wb, sheet, 1, rowNum, 0, colNum));
        return list;
    }

    /**
     * 根据sheet获取该sheet的指定行列的数据[startRowIndex, endRowIndex), [startColumnIndex, endColumnIndex)
     * @param wb
     * @param sheet
     * @param startRowIndex	开始行索引值
     * @param endRowIndex	结束行索引值
     * @param startColumnIndex	开始列索引值
     * @param endColumnIndex	结束列索引值
     * @return
     * @throws IOException 
     */
    public static List<List<Object>> getSheetData(Workbook wb, Sheet sheet, int startRowIndex, int endRowIndex,
            int startColumnIndex, int endColumnIndex) throws IOException {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if (startRowIndex > endRowIndex || startColumnIndex > endColumnIndex) {
            return list;
        }

        /**
         * getLastRowNum方法能够正确返回最后一行的位置；
         * getPhysicalNumberOfRows方法能够正确返回物理的行数；
         */
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        // int rowNum = sheet.getLastRowNum();
        // 获取标题行
        Row headerRow = sheet.getRow(0);
        // 标题总列数
        int colNum = headerRow.getPhysicalNumberOfCells();

        if (endRowIndex > rowNum) {
            throw new IOException("行的最大下标索引超过了该sheet实际总行数(包括标题行)" + rowNum);
        }
        if (endColumnIndex > colNum) {
            throw new IOException("列的最大下标索引超过了实际标题总列数" + colNum);
        }
        for (int i = startRowIndex; i < endRowIndex; i++) {
            Row row = sheet.getRow(i);
            boolean allRowIsBlank = isBlankRow(wb, row);
            if (allRowIsBlank) { // 整行都空，就跳过
                continue;
            }
            List<Object> rowData = getRowData(wb, row, startColumnIndex, endColumnIndex);
            list.add(rowData);
        }
        return list;
    }

    /**
     * 根据指定列区间获取行的数据
     * @param wb
     * @param row
     * @param startColumnIndex	开始列索引值
     * @param endColumnIndex	结束列索引值
     * @return
     */
    public static List<Object> getRowData(Workbook wb, Row row, int startColumnIndex, int endColumnIndex) {
        List<Object> rowData = new ArrayList<Object>();
        for (int j = startColumnIndex; j < endColumnIndex; j++) {
            Cell cell = row.getCell(j);
            rowData.add(getCellValue(wb, cell));
        }
        return rowData;
    }

    /**
     * 判断整行是不是都为空
     * @param row
     * @return
     */
    public static boolean isBlankRow(Workbook wb, Row row) {
        boolean allRowIsBlank = true;
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Object cellValue = getCellValue(wb, cellIterator.next());
            if (cellValue != null && !"".equals(cellValue)) {
                allRowIsBlank = false;
                break;
            }
        }
        return allRowIsBlank;
    }

    /**
     * 获取行的数据
     * @param row
     * @param rowData
     * @return
     */
    public static List<Object> getRowData(Workbook wb, Row row) {
        List<Object> rowData = new ArrayList<Object>();
        /**
         * 不建议用row.cellIterator(), 因为空列会被跳过， 后面的列会前移， 建议用for循环， row.getLastCellNum()是获取最后一个不为空的列是第几个
         * 结论：空行可以跳过， 空列最好不要跳过
         */
        /*Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
        	Cell cell = cellIterator.next();
            Object cellValue = getCellValue(wb, cell);
            rowData.add(cellValue);
        }*/
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            Object cellValue = getCellValue(wb, cell);
            rowData.add(cellValue);
        }
        return rowData;
    }

    /**
     * 获取单元格值
     * @param cell
     * @return
     */
    public static Object getCellValue(Workbook wb, Cell cell) {
        if (cell == null
                || (cell.getCellType() == Cell.CELL_TYPE_STRING && Utils.isNull(cell.getStringCellValue()))) {
            return null;
        }
        /*if (cell == null) {
            return "";
        }*/
        // 如果该单元格为数字， 则设置该单元格类型为文本格式
        /*CellStyle cellStyle = wb.createCellStyle();
        DataFormat dataFormat = wb.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("@"));
        cell.setCellStyle(cellStyle);
        cell.setCellType(Cell.CELL_TYPE_STRING);*/

        DecimalFormat df = new DecimalFormat("0");// 格式化 number String字符
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 格式化日期字符串
        DecimalFormat nf = new DecimalFormat("0");// 格式化数字

        int cellType = cell.getCellType();
        switch (cellType) {
        case Cell.CELL_TYPE_BLANK:
            // return "";
            return null;
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue();
        case Cell.CELL_TYPE_ERROR:
            return cell.getErrorCellValue();
        case Cell.CELL_TYPE_FORMULA:
            return  String.valueOf(cell.getCellFormula());
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                String value = df.format(cell.getNumericCellValue());
                if (Utils.isNull(value)) {
                    return null;
                }
                return value;
            } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                String value = nf.format(cell.getNumericCellValue());
                if (Utils.isNull(value)) {
                    return null;
                }
                return value;
            } else {
                return cell.getNumericCellValue();
                /*double doubleValue = cell.getNumericCellValue();
                long longValue = (long) doubleValue;
                if (doubleValue - longValue > 0) {
                	return String.valueOf(doubleValue);
                } else {
                	return longValue;
                }*/
                /*DecimalFormat df = new DecimalFormat("#");
                String value = df.format(cell.getNumericCellValue()).toString();
                return value;*/
            }
        case Cell.CELL_TYPE_STRING:
            String value = cell.getStringCellValue();
            if (Utils.isNull(value)) {
                return null;
            } else {
                return value;
            }
            // return cell.getRichStringCellValue();
        default:
            // return "";
            return null;
        }
    }

    /**
     * 根据sheet返回该sheet的物理总行数
     * sheet.getPhysicalNumberOfRows方法能够正确返回物理的行数
     * @param sheet
     * @return
     */
    public static int getSheetPhysicalRowNum(Sheet sheet) {
        // 获取总行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        return rowNum;
    }

    /**
     * 获取操作的行数
     * @param startRowIndex sheet的开始行位置索引
     * @param endRowIndex sheet的结束行位置索引
     * @return
     */
    public static int getSheetDataPhysicalRowNum(int startRowIndex, int endRowIndex) {
        int rowNum = -1;
        if (startRowIndex >= 0 && endRowIndex >= 0 && startRowIndex <= endRowIndex) {
            rowNum = endRowIndex - startRowIndex + 1;
        }
        return rowNum;
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers   表格属性列名数组
     * @param dataset   需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                  javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param filePath  excel文件输出路径     
     */
    public static <T> void exportExcel(String[] headers, Collection<T> dataset, String filePath) {
        exportExcel(headers, dataset, filePath, null);
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param filePath  excel文件输出路径
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public static <T> void exportExcel(String[] headers, Collection<T> dataset, String filePath, String pattern) {
        try {
            // 声明一个工作薄
            Workbook workbook = getExportWorkbook(filePath);
            if (workbook != null) {
                // 生成一个表格
                Sheet sheet = workbook.createSheet();

                write2Sheet(sheet, headers, dataset, pattern);
                OutputStream out = new FileOutputStream(new File(filePath));
                workbook.write(out);
                out.close();
            }
        } catch (IOException e) {
            mLogger.error(e.toString(), e);
        }
    }

    /**
     * 导出数据到Excel文件
     * @param dataList 要输出到Excel文件的数据集
     * @param filePath  excel文件输出路径
     */
    public static void exportExcel(String[][] dataList, String filePath) {
        try {
            // 声明一个工作薄
            Workbook workbook = getExportWorkbook(filePath);
            if (workbook != null) {
                // 生成一个表格
                Sheet sheet = workbook.createSheet();

                for (int i = 0; i < dataList.length; i++) {
                    String[] r = dataList[i];
                    Row row = sheet.createRow(i);
                    for (int j = 0; j < r.length; j++) {
                        Cell cell = row.createCell(j);
                        // cell max length 32767
                        if (r[j].length() > 32767) {
                            mLogger.warn("异常处理", "--此字段过长(超过32767),已被截断--" + r[j]);
                            r[j] = r[j].substring(0, 32766);
                        }
                        cell.setCellValue(r[j]);
                    }
                }
                // 自动列宽
                if (dataList.length > 0) {
                    int colcount = dataList[0].length;
                    for (int i = 0; i < colcount; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
                OutputStream out = new FileOutputStream(new File(filePath));
                workbook.write(out);
                out.close();
            }
        } catch (IOException e) {
            mLogger.error(e.toString(), e);
        }
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于多个sheet
     * @param sheets ExcelSheet的集体
     * @param filePath excel文件路径
     */
    public static <T> void exportExcel(List<ExcelSheet<T>> sheets, String filePath) {
        exportExcel(sheets, filePath, null);
    }

    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于多个sheet
     *
     * @param sheets    ExcelSheet的集合
     * @param filePath  excel文件输出路径
     * @param pattern   如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public static <T> void exportExcel(List<ExcelSheet<T>> sheets, String filePath, String pattern) {
        if (sheets==null) {
            return;
        }
        try {
            // 声明一个工作薄
            Workbook workbook = getExportWorkbook(filePath);
            if (workbook != null) {
                for (ExcelSheet<T> sheetInfo : sheets) {
                    // 生成一个表格
                    Sheet sheet = workbook.createSheet(sheetInfo.getSheetName());
                    write2Sheet(sheet, sheetInfo.getHeaders(), sheetInfo.getDataset(), pattern);
                }
                OutputStream out = new FileOutputStream(new File(filePath));
                workbook.write(out);
                out.close();
            }
        } catch (IOException e) {
            mLogger.error(e.toString(), e);
        }
    }

    /**
     * 每个sheet的写入
     * @param sheet   页签
     * @param headers 表头
     * @param dataset 数据集合
     * @param pattern 日期格式
     */
    public static <T> void write2Sheet(Sheet sheet, String[] headers, Collection<T> dataset, String pattern) {
        // 产生表格标题行
        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            if (t instanceof Map) { // row data is map
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) t;
                int cellNum = 0;
                for (String k : headers) {
                    if (map.containsKey(k) == false) {
                        mLogger.error("Map 中 不存在 key [" + k + "]");
                        continue;
                    }
                    Cell cell = row.createCell(cellNum);
                    Object value = map.get(k);
                    if (value == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                    cellNum++;
                }
            } else if (t instanceof Object[]) { // row data is Object[]
                Object[] tObjArr = (Object[]) t;
                for (int i = 0; i < tObjArr.length; i++) {
                    Cell cell = row.createCell(i);
                    Object value = tObjArr[i];
                    if (value == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                }
            } else if (t instanceof List<?>) { // row data is List
                List<?> rowData = (List<?>) t;
                for (int i = 0; i < rowData.size(); i++) {
                    Cell cell = row.createCell(i);
                    Object value = rowData.get(i);
                    if (value == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(String.valueOf(value));
                    }
                }
            } else { // row data is vo
                // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
                Field[] fields = t.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Cell cell = row.createCell(i);
                    Field field = fields[i];
                    String fieldName = field.getName();
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    try {
                        Class<?> tClazz = t.getClass();
                        Method getMethod = tClazz.getMethod(getMethodName, new Class[] {});
                        Object value = getMethod.invoke(t, new Object[] {});
                        String textValue = null;
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue = (Boolean) value;
                            cell.setCellValue(bValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            textValue = value.toString();
                        }
                        if (textValue != null) {
                            // HSSFRichTextString richString = new
                            // HSSFRichTextString(textValue);
                            cell.setCellValue(textValue);
                        } else {
                            cell.setCellValue("");
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        // 设定自动宽度
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * EXCEL文件下载
     * @param path
     * @param response
     */
    public static void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 分别测试以下示例并成功通过：
     * 1. 单个sheet， 数据集类型是List<List<Object>>
     * 2. 单个sheet， 数据集类型是List<Object[]>
     * 3. 多个sheet， 数据集类型是List<ExcelSheet<List<Object>>>
     * 4. 多个sheet， 数据集类型是List<ExcelSheet<List<Object>>>
     * 5. 多个sheet， 数据集类型是List<ExcelSheet<List<Object>>>, 支持大数据量
     * @param args
     */
    public static void main1(String[] args) {
        // List<List<Object>> list = new ArrayList<List<Object>>();

        /*try {
            
            // list = readExcel(new File("D:/test.xlsx"));
            // 导入
            // list = readExcel(new File("D:/test.xlsx"), 1);
            list = readExcelBody("D:/test.xlsx", 1);
        
            List<Object[]> dataList = new ArrayList<Object[]>();
            for (int i = 0; i < list.size(); i++) {
                Object[] objArr = new Object[list.get(i).size()];
                List<Object> objList = list.get(i);
                for (int j = 0; j < objList.size(); j++) {
                    objArr[j] = objList.get(j);
                }
                dataList.add(objArr);
            }
            for (int i = 0; i < dataList.size(); i++) {
            	System.out.println(Arrays.toString(dataList.get(i)));
            }
        
            String[] headers = { "代理商ID", "代理商编码", "系统内代理商名称", "贷款代理商名称", "入网时长", "佣金账期", "佣金类型", "金额" };
            String filePath = "d://out_" + System.currentTimeMillis() + ".xlsx";
           
            ExcelSheet<List<Object>> sheet = new ExcelSheet<List<Object>>();
            sheet.setHeaders(headers);
            sheet.setSheetName("按入网时间提取佣金数");
            sheet.setDataset(list);
        
            ExcelSheet<Object[]> sheet = new ExcelSheet<Object[]>();
            sheet.setHeaders(headers);
            sheet.setSheetName("按入网时间提取佣金数");
            sheet.setDataset(dataList);
        
            // List<ExcelSheet<List<Object>>> sheets = new
            // ArrayList<ExcelSheet<List<Object>>>();
            List<ExcelSheet<Object[]>> sheets = new ArrayList<ExcelSheet<Object[]>>();
            sheets.add(sheet);
            // 导出
            // exportExcel(headers, list, os);
            // exportExcel(headers, dataList, os);
            exportExcel(sheets, filePath);
            // list = readExcel(new File("D:/test.xlsx"), "按入网时间提取佣金数");
            // list = readExcel(new File("D:/test.xlsx"), 0, 1, 85, 0, 6);
            // list = readExcel(new File("D:/test.xlsx"), "按入网时间提取佣金数", 1061,
            // 1062, 0, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // 有3个sheet的数据， 每个sheet数据为50万行， 共150万行数据输出到Excel文件, 性能测试。
        List<ExcelSheet<List<Object>>> sheetsData = new ArrayList<>();

        int sheetRowNum = 50000;

        for (int i = 0; i < 3; i++) {
            ExcelSheet<List<Object>> sheetData = new ExcelSheet<>();
            String[] headers = { "姓名", "手机号码", "性别", "身份证号码", "家庭住址" };
            String sheetName = "第" + (i + 1) + "个sheet";

            List<List<Object>> sheetDataList = new ArrayList<>();
            for (int j = 0; j < sheetRowNum; j++) {
                List<Object> rowData = new ArrayList<>();
                rowData.add("小明");
                rowData.add("18888888888");
                rowData.add("男");
                rowData.add("123123123123123123");
                rowData.add("广州市");
                sheetDataList.add(rowData);
            }
            sheetData.setSheetName(sheetName);
            sheetData.setHeaders(headers);
            sheetData.setDataset(sheetDataList);

            sheetsData.add(sheetData);
        }
        String filePath = "d://out_" + System.currentTimeMillis() + ".xlsx";
        exportExcel(sheetsData, filePath);
        System.out.println("-----end-----");
        /*for (int i = 0; i < list.size(); i++) {
        	System.out.println(list.get(i));
        }*/
    }
}
