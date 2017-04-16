package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * Excel视图
 * 
 * @author Yi Zhao
 */
public class ExcelData {

	/** 文件名称 */
	private String filename;

	/** 表名称 */
	private String sheetName;

	/** 标题 */
	private String[] titles;

	/** 列宽 */
	private Integer[] widths;

	/** 数据 */
	private double[][] data;
	// data[col][row], indexed from 0.
	// data[*][0] --> title

	/**
	 * Construct an excelData object.
	 * 
	 * @param filename
	 *            文件名称
	 * @param sheetName
	 *            表名称
	 * @param titles
	 *            标题
	 * @param widths
	 *            列宽
	 * @param data
	 *            数据
	 */
	public ExcelData(String filename, String sheetName, String[] titles, Integer[] widths, double[][] data) {
		this.filename = filename;
		this.sheetName = sheetName;
		this.titles = titles;
		this.widths = widths;
		this.data = data;
	}

	/**
	 * Read excel data from excel file.
	 * 
	 * @param filename
	 *            文件名称
	 * @param sheetName
	 *            表名称
	 * @throws FileNotFoundException
	 */
	public static ExcelData buildFromFile(File file, String sheetName) throws Exception {
		ExcelData excelData = null;

		FileInputStream fileInputStream = new FileInputStream(file);
		String filename = file.getAbsolutePath();
		String[] titles;
		double[][] data;

		int columnNumber = 0;
		int rowNumber = 0;

		HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
		HSSFSheet sheet = workbook.getSheet(sheetName);
		HSSFRow titleRow = sheet.getRow(sheet.getFirstRowNum());

		if (titleRow != null) {
			rowNumber = sheet.getLastRowNum() + 1;
			columnNumber = titleRow.getLastCellNum();
			data = new double[columnNumber][rowNumber];
			titles = new String[columnNumber];
			// read titles
			for (int col = 0; col < columnNumber; col++) {
				titles[col] = titleRow.getCell(col).getStringCellValue();
			}
			// read data
			for (int row = 1; row < rowNumber; row++) {
				HSSFRow dataRow = sheet.getRow(row);
				for (int col = 0; col < columnNumber; col++) {
					data[col][row] = dataRow.getCell(col).getNumericCellValue();
				}
			}
			excelData = new ExcelData(filename, sheetName, titles, null, data);
		}

		workbook.close();
		return excelData;
	}

	/**
	 * 导出Excel文档
	 * 
	 * @param model
	 *            数据
	 * @param workbook
	 *            workbook
	 * @param request
	 *            request
	 * @param response
	 *            response
	 */
	public void exportToFile() throws Exception {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet;
		if (StringUtils.isNotEmpty(sheetName)) {
			sheet = workbook.createSheet(sheetName);
		} else {
			sheet = workbook.createSheet();
		}
		int row = 0;
		if (titles != null && titles.length > 0) {
			HSSFRow titleRow = sheet.createRow(row);
			titleRow.setHeight((short) 400);
			for (int i = 0; i < titles.length; i++) {
				HSSFCell cell = titleRow.createCell(i);

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				HSSFFont font = workbook.createFont();
				font.setFontHeightInPoints((short) 11);
				font.setBold(true);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);

				cell.setCellValue(titles[i] != null ? titles[i] : "");
				if (widths != null && widths.length > i && widths[i] != null) {
					sheet.setColumnWidth(i, widths[i]);
				} else {
					sheet.autoSizeColumn(i);
				}
			}
			row++;
		}
		if (data != null) {
			int rowNumber = data[0].length;
			for (row = 1; row < rowNumber; row++) {
				HSSFRow dataRow = sheet.createRow(row);
				for (int col = 0; col < data.length; col++) {
					HSSFCell cell = dataRow.createCell(col);
					cell.setCellValue(data[col][row]);
				}
			}
		}
		File file = new File(filename);
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		workbook.write(fileOutputStream);
		fileOutputStream.flush();
		fileOutputStream.close();
		workbook.close();
	}

	/**
	 * 获取文件名称
	 * 
	 * @return 文件名称
	 */
	public String getFileName() {
		return filename;
	}

	/**
	 * 设置文件名称
	 * 
	 * @param filename
	 *            文件名称
	 */
	public void setFileName(String filename) {
		this.filename = filename;
	}

	/**
	 * 获取表名称
	 * 
	 * @return 表名称
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * 设置表名称
	 * 
	 * @param sheetName
	 *            表名称
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * 获取标题
	 * 
	 * @return 标题
	 */
	public String[] getTitles() {
		return titles;
	}

	/**
	 * 设置标题
	 * 
	 * @param titles
	 *            标题
	 */
	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	/**
	 * 获取列宽
	 * 
	 * @return 列宽
	 */
	public Integer[] getWidths() {
		return widths;
	}

	/**
	 * 设置列宽
	 * 
	 * @param widths
	 *            列宽
	 */
	public void setWidths(Integer[] widths) {
		this.widths = widths;
	}

	/**
	 * 获取数据
	 * 
	 * @return 数据
	 */
	public double[][] getData() {
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 *            数据
	 */
	public void setData(double[][] data) {
		this.data = data;
	}

}