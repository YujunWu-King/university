/**
 * 
 */
package com.tranzvision.gd.util.poi.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;

/**
 * 处理导出excel工具类
 * 
 * @author SHIHUA
 * @since 2016-02-25
 */
@SuppressWarnings("rawtypes")
public class ExcelHandle {

	private Map<String, HashMap[]> tempFileMap = new HashMap<String, HashMap[]>();
	private Map<String, Map<String, Cell>> cellMap = new HashMap<String, Map<String, Cell>>();
	private Map<String, FileInputStream> tempStream = new HashMap<String, FileInputStream>();
	private Map<String, Workbook> tempWorkbook = new HashMap<String, Workbook>();
	private Map<String, Workbook> dataWorkbook = new HashMap<String, Workbook>();

	private HttpServletRequest request;

	/**
	 * 当前操作的机构，类实例化时赋值
	 */
	private String orgid;

	/**
	 * 当前操作的模块名称，用于创建导出excel的路径 类实例化时赋值，默认为：default
	 */
	private String moudelName = "default";

	/**
	 * 系统规划的下载文件存储路径，类实例化时赋值
	 */
	private String downloadDir;

	/**
	 * 读取到的excel列表数据
	 */
	private ArrayList<Map<String, Object>> excelListData = new ArrayList<Map<String, Object>>();

	/**
	 * 读取到的excel模板单字段数据
	 */
	private Map<String, Object> excelMapData = new HashMap<String, Object>();

	/**
	 * 导出数据，生成的Excel文件路径
	 */
	private String exportExcelPath;

	private String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 读取文件用构造函数
	 * 
	 * @param request
	 */
	public ExcelHandle(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * 导出文件用构造函数
	 * 
	 * @param request
	 * @param downloadPath
	 *            系统规划的下载文件存储路径
	 * @param loginOrgid
	 *            当前登录的机构
	 * @param mdlName
	 *            当前操作的模块名称，用于创建导出excel的路径，默认为：default
	 */
	public ExcelHandle(HttpServletRequest request, String downloadPath, String loginOrgid, String mdlName) {
		this.request = request;
		this.downloadDir = downloadPath == null ? "" : downloadPath;
		this.orgid = loginOrgid == null ? "" : loginOrgid.toLowerCase();
		this.moudelName = mdlName == null ? "" : mdlName;
		if (!"".equals(moudelName)) {
			TzFilterIllegalCharacter tzFilterIllegalCharacter = new TzFilterIllegalCharacter();
			moudelName = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(moudelName);
		}
	}

	/**
	 * 获取读取到的列表数据
	 * 
	 * @return
	 */
	public ArrayList<Map<String, Object>> getExcelListData() {
		return excelListData;
	}

	/**
	 * 获取读取到的单一字段数据
	 * 
	 * @return
	 */
	public Map<String, Object> getExcelMapData() {
		return excelMapData;
	}

	/**
	 * 获取生成的Excel文件路径
	 * 
	 * @return
	 */
	public String getExportExcelPath() {
		return exportExcelPath;
	}

	/**
	 * 设置默认的日期显示格式，如不设置，默认是：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dtFormat
	 */
	public void setDateFormat(String dtFormat) {
		this.defaultDateFormat = dtFormat;
	}

	/**
	 * 单无格类
	 */
	class Cell {
		private int column;// 列
		private int line;// 行
		private CellStyle cellStyle;

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

		public CellStyle getCellStyle() {
			return cellStyle;
		}

		public void setCellStyle(CellStyle cellStyle) {
			this.cellStyle = cellStyle;
		}
	}

	/**
	 * 向Excel中输入相同title的多条数据
	 * 
	 * @param tempFilePath
	 *            excel模板文件路径
	 * @param cellList
	 *            需要填充的数据（模板<!%后的字符串）
	 * @param dataList
	 *            填充的数据
	 * @param sheet
	 *            填充的excel sheet,从0开始
	 * @throws IOException
	 */
	public void writeListData(String tempFilePath, List<String> cellList, List<Map<String, Object>> dataList, int sheet)
			throws IOException {
		// 获取模板填充格式位置等数据
		HashMap temp = getTemp(tempFilePath, sheet);
		// 按模板为写入板
		Workbook temWorkbook = getTempWorkbook(tempFilePath);
		// 获取数据填充开始行
		int startCell = Integer.parseInt((String) temp.get("STARTCELL"));
		// 数据填充的sheet
		Sheet wsheet = temWorkbook.getSheetAt(sheet);
		// 移除模板开始行数据即<!%
		wsheet.removeRow(wsheet.getRow(startCell));
		if (dataList != null && dataList.size() > 0) {
			for (Map<String, Object> map : dataList) {
				for (String cell : cellList) {
					// 获取对应单元格数据
					Cell c = getCell(cell, temp, temWorkbook, tempFilePath);
					// 写入数据
					ExcelUtil.setValue(wsheet, startCell, c.getColumn(), map.get(cell), c.getCellStyle());
				}
				startCell++;
			}
		}
	}

	/**
	 * 按模板向Excel中相应地方填充数据
	 * 
	 * @param tempFilePath
	 *            excel模板文件路径
	 * @param cellList
	 *            需要填充的数据（模板<%后的字符串）
	 * @param dataMap
	 *            填充的数据
	 * @param sheet
	 *            填充的excel sheet,从0开始
	 * @throws IOException
	 */
	public void writeData(String tempFilePath, List<String> cellList, Map<String, Object> dataMap, int sheet)
			throws IOException {
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 按模板为写入板
		Workbook wbModule = getTempWorkbook(tempFilePath);
		// 数据填充的sheet
		Sheet wsheet = wbModule.getSheetAt(sheet);
		if (dataMap != null && dataMap.size() > 0) {
			for (String cell : cellList) {
				// 获取对应单元格数据
				Cell c = getCell(cell, tem, wbModule, tempFilePath);
				ExcelUtil.setValue(wsheet, c.getLine(), c.getColumn(), dataMap.get(cell), c.getCellStyle());
			}
		}
	}

	/**
	 * 读取指定模板实例的单个字段值
	 * 
	 * @param tempFilePath
	 * @param cell
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public Object getValue(String tempFilePath, String cell, int sheet, File excelFile) throws IOException {
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 模板工作区
		Workbook temWorkbook = getTempWorkbook(tempFilePath);
		// 数据工作区
		Workbook dataWorkbook = getDataWorkbook(tempFilePath, excelFile);
		// 获取对应单元格数据
		Cell c = getCell(cell, tem, temWorkbook, tempFilePath);
		// 数据sheet
		Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
		return ExcelUtil.getCellValue(dataSheet, c.getLine(), c.getColumn());
	}

	/**
	 * 读取指定模板实例的列表值
	 * 
	 * @param tempFilePath
	 * @param cell
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Map<String, Object>> getListValue(String tempFilePath, List<String> cellList, int sheet,
			File excelFile) throws IOException {
		ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 获取数据填充开始行
		int startCell = Integer.parseInt((String) tem.get("STARTCELL"));
		// 将Excel文件转换为工作区间
		Workbook dataWorkbook = getDataWorkbook(tempFilePath, excelFile);
		// 数据sheet
		Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
		// 文件最后一行
		int lastLine = dataSheet.getLastRowNum();

		for (int i = startCell; i <= lastLine; i++) {
			dataList.add(getListLineValue(i, tempFilePath, cellList, sheet, excelFile));
		}
		return dataList;
	}

	/**
	 * 读取指定模板实例列表的具体某一行值
	 * 
	 * @param tempFilePath
	 * @param cell
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> getListLineValue(int line, String tempFilePath, List<String> cellList, int sheet,
			File excelFile) throws IOException {
		Map<String, Object> lineMap = new HashMap<String, Object>();
		// 获取模板填充格式位置等数据
		HashMap tem = getTemp(tempFilePath, sheet);
		// 按模板为写入板
		Workbook temWorkbook = getTempWorkbook(tempFilePath);
		// 将Excel文件转换为工作区间
		Workbook dataWorkbook = getDataWorkbook(tempFilePath, excelFile);
		// 数据sheet
		Sheet dataSheet = dataWorkbook.getSheetAt(sheet);
		for (String cell : cellList) {
			// 获取对应单元格数据
			Cell c = getCell(cell, tem, temWorkbook, tempFilePath);
			lineMap.put(cell, ExcelUtil.getCellValue(dataSheet, line, c.getColumn()));
		}
		return lineMap;
	}

	/**
	 * 获得excel文件（或模板）输入流
	 * 
	 * @param tempFilePath
	 * @return
	 * @throws FileNotFoundException
	 */
	private FileInputStream getFileInputStream(String tempFilePath) throws FileNotFoundException {
		if (!tempStream.containsKey(tempFilePath)) {
			tempStream.put(tempFilePath, new FileInputStream(tempFilePath));
		}

		return tempStream.get(tempFilePath);
	}

	/**
	 * 获得excel文件（或模板输入）工作区
	 * 
	 * @param tempFilePath
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Workbook getTempWorkbook(String tempFilePath) throws FileNotFoundException, IOException {
		if (!tempWorkbook.containsKey(tempFilePath)) {
			if (tempFilePath.endsWith(".xlsx")) {
				tempWorkbook.put(tempFilePath, new XSSFWorkbook(getFileInputStream(tempFilePath)));
			} else if (tempFilePath.endsWith(".xls")) {
				tempWorkbook.put(tempFilePath, new HSSFWorkbook(getFileInputStream(tempFilePath)));
			}
		}
		return tempWorkbook.get(tempFilePath);
	}

	/**
	 * 获取对应单元格样式等数据
	 * 
	 * @param cell
	 * @param tem
	 * @param wbModule
	 * @param tempFilePath
	 * @return
	 */
	private Cell getCell(String cell, HashMap tem, Workbook wbModule, String tempFilePath) {
		if (!cellMap.get(tempFilePath).containsKey(cell)) {
			Cell c = new Cell();

			int[] pos = ExcelUtil.getPos(tem, cell);
			if (pos.length > 1) {
				c.setLine(pos[1]);
			}
			c.setColumn(pos[0]);
			c.setCellStyle((ExcelUtil.getStyle(tem, cell, wbModule)));
			cellMap.get(tempFilePath).put(cell, c);
		}
		return cellMap.get(tempFilePath).get(cell);
	}

	/**
	 * 获取模板数据
	 * 
	 * @param tempFilePath
	 *            模板文件路径
	 * @param sheet
	 * @return
	 * @throws IOException
	 */
	private HashMap getTemp(String tempFilePath, int sheet) throws IOException {
		if (!tempFileMap.containsKey(tempFilePath)) {
			tempFileMap.put(tempFilePath, ExcelUtil.getTemplateFile(tempFilePath));
			cellMap.put(tempFilePath, new HashMap<String, Cell>());
		}
		return tempFileMap.get(tempFilePath)[sheet];
	}

	/**
	 * 资源关闭
	 * 
	 * @param tempFilePath
	 *            模板文件路径
	 * @param os
	 *            输出流
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void writeAndClose(String tempFilePath, OutputStream os) throws FileNotFoundException, IOException {
		if (getTempWorkbook(tempFilePath) != null) {
			getTempWorkbook(tempFilePath).write(os);
			tempWorkbook.remove(tempFilePath);
		}
		if (getFileInputStream(tempFilePath) != null) {
			getFileInputStream(tempFilePath).close();
			tempStream.remove(tempFilePath);
		}
	}

	/**
	 * 获得读取数据工作间
	 * 
	 * @param tempFilePath
	 * @param excelFile
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private Workbook getDataWorkbook(String tempFilePath, File excelFile) throws FileNotFoundException, IOException {
		if (!dataWorkbook.containsKey(tempFilePath)) {
			if (tempFilePath.endsWith(".xlsx")) {
				dataWorkbook.put(tempFilePath, new XSSFWorkbook(new FileInputStream(excelFile)));
			} else if (tempFilePath.endsWith(".xls")) {
				dataWorkbook.put(tempFilePath, new HSSFWorkbook(new FileInputStream(excelFile)));
			}
		}
		return dataWorkbook.get(tempFilePath);
	}

	/**
	 * 读取数据后关闭
	 * 
	 * @param tempFilePath
	 */
	public void readClose(String tempFilePath) {
		dataWorkbook.remove(tempFilePath);
	}

	/**
	 * 创建日期目录名
	 * 
	 * @return
	 */
	private String getDateNow() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		return (new StringBuilder()).append(year).append(month).append(day).toString();
	}

	/**
	 * 生成文件名
	 * 
	 * @return
	 */
	private String genFileName() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		int hour = cal.get(10);
		int minute = cal.get(12);
		int second = cal.get(13);
		int mi = cal.get(14);
		long num = cal.getTimeInMillis();
		int rand = (int) (Math.random() * 899999 + 100000);
		return (new StringBuilder()).append(year).append(month).append(day).append(hour).append(minute).append(second)
				.append(mi).append(num).append("_").append(rand).toString();
	}

	/**
	 * 创建要生成的File实例
	 * 
	 * @param expFileName
	 *            文件名
	 * @return
	 */
	private File newFileObject(String expFileName) {

		String expDirPath = downloadDir + "/" + orgid + "/" + getDateNow() + "/" + moudelName;

		String absexpDirPath = request.getServletContext().getRealPath(expDirPath);

		File dir = new File(absexpDirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		if (null == expFileName || "".equals(expFileName)) {
			// 如果不传文件名，则表示让系统自动命名
			expFileName = genFileName() + ".xlsx";
		}

		String dirAbsolutePath = dir.getAbsolutePath() + File.separator;
		File expFile = new File(dirAbsolutePath + expFileName);

		while (expFile.exists()) {
			expFileName = genFileName() + ".xlsx";
			expFile = new File(dirAbsolutePath + expFileName);
		}

		exportExcelPath = expDirPath + "/" + expFileName;

		return expFile;
	}

	/**
	 * 按指定的Excel模板导出数据文件
	 * 
	 * @param tplFilePath
	 *            模板文件路径
	 * @param expFileName
	 *            生成的excel文件名
	 * @param dataListCellKeys
	 *            列表数据的字段名
	 * @param dataList
	 *            列表数据
	 * @param dataCellKeysList
	 *            单个字段的字段名集合
	 * @param dataCelValsMap
	 *            单个字段的数据
	 */
	public boolean export2ExcelByTpl(String tplFilePath, String expFileName, List<String> dataListCellKeys,
			List<Map<String, Object>> dataList, List<String> dataCellKeysList, Map<String, Object> dataCelValsMap) {

		boolean ret = false;

		if (null == orgid || "".equals(orgid) || null == downloadDir || "".equals(downloadDir)) {
			return false;
		}

		try {
			tplFilePath = request.getServletContext().getRealPath(tplFilePath);

			// 导出列表数据到模板实例
			this.writeListData(tplFilePath, dataListCellKeys, dataList, 0);

			// 导出单个字段数据到模板实例
			this.writeData(tplFilePath, dataCellKeysList, dataCelValsMap, 0);

			File expFile = newFileObject(expFileName);

			OutputStream os = new FileOutputStream(expFile);
			// 写到输出流并关闭资源
			this.writeAndClose(tplFilePath, os);

			os.flush();
			os.close();

			this.readClose(tplFilePath);

			ret = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * 导出数据集到Excel文件
	 * 
	 * @param expFileName
	 *            生成的excel文件名
	 * @param dataCellKeys
	 *            导出的excel的表头，String[]: 0 => 字段名，1 => 表头显示内容；
	 * @param dataList
	 *            要导出的数据集，Map eg: Map<字段名,字段值>
	 * @return boolean
	 */
	public boolean export2Excel(String expFileName, List<String[]> dataCellKeys, List<Map<String, Object>> dataList) {

		boolean ret = false;

		if (null == orgid || "".equals(orgid) || null == downloadDir || "".equals(downloadDir)) {
			return false;
		}

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("DataExport");

		int rownum = 0;
		for (Map<String, Object> mapData : dataList) {

			int cellnum = 0;
			if (rownum == 0) {
				// 如果是第一行，则写入表头
				Row row = sheet.createRow(rownum);
				for (String[] keysAry : dataCellKeys) {
					String key = keysAry[0];
					String keyName = key;
					if (keysAry.length > 1) {
						keyName = keysAry[1];
					}
					org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellnum);
					cell.setCellValue(keyName);
					cellnum++;
				}
				rownum++;
			}

			// 生成数据行
			Row row = sheet.createRow(rownum);
			cellnum = 0;
			for (String[] keysAry : dataCellKeys) {
				String key = keysAry[0];
				org.apache.poi.ss.usermodel.Cell cell = row.createCell(cellnum);
				Object obj = mapData.get(key);

				String dateFormat = "";
				if (obj instanceof Object[]) {
					Object[] objAry = (Object[]) obj;
					if (objAry[0] instanceof Date) {
						// 获取日期格式
						obj = objAry[0];
						dateFormat = String.valueOf(objAry[1]);
					}
				}

				if (obj instanceof Date) {

					if ("".equals(dateFormat)) {
						dateFormat = defaultDateFormat;
					}

					CellStyle cellStyle = workbook.createCellStyle();
					CreationHelper createHelper = workbook.getCreationHelper();
					cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));
					cell.setCellValue((Date) obj);
					cell.setCellStyle(cellStyle);

				} else if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				else if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Double)
					cell.setCellValue((Double) obj);
				else if (obj instanceof Integer)
					cell.setCellValue(Double.parseDouble(obj.toString()));

				cellnum++;
			}

			rownum++;
		}

		try {
			File expFile = newFileObject(expFileName);
			FileOutputStream os = new FileOutputStream(expFile);
			workbook.write(os);
			os.close();
			workbook.close();
			ret = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;

	}

	/**
	 * 读取指定模板实例数据
	 * 
	 * @param tplFilePath
	 *            模板文件路径
	 * @param dataFilePath
	 *            Excel实例文件的路径
	 * @param dataListCellKeys
	 *            列表数据的字段名
	 * @param dataCellKeys
	 *            单个字段的字段名集合
	 */
	public void readExcelByTpl(String tplFilePath, String dataFilePath, List<String> dataListCellKeys,
			List<String> dataCellKeys) {

		try {

			tplFilePath = request.getServletContext().getRealPath(tplFilePath);

			dataFilePath = request.getServletContext().getRealPath(dataFilePath);

			File file = new File(dataFilePath);

			// 取单个字段的值
			for (String key : dataCellKeys) {
				Object obj = this.getValue(tplFilePath, key, 0, file);
				excelMapData.put(key, obj);
			}

			// 取列表数据
			excelListData = this.getListValue(tplFilePath, dataListCellKeys, 0, file);

			this.readClose(tplFilePath);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取Excel列表
	 * 
	 * @param dataFilePath
	 *            Excel文件路径
	 * @param dataListCellKeys
	 *            要读取的表头字段名，传 null 则表示读取所有列
	 * @param firstRowKeys
	 *            第一行是否为表头：true 是；false 否；
	 */
	public void readExcel(String dataFilePath, List<String> dataListCellKeys, boolean firstRowKeys) {

		try {

			dataFilePath = request.getServletContext().getRealPath(dataFilePath);

			File file = new File(dataFilePath);

			Workbook workbook;

			if (dataFilePath.toLowerCase().endsWith(".xlsx")) {

				workbook = new XSSFWorkbook(new FileInputStream(file));

			} else if (dataFilePath.toLowerCase().endsWith(".xls")) {

				workbook = new HSSFWorkbook(new FileInputStream(file));

			} else {
				return;
			}

			Sheet sheet = workbook.getSheetAt(0);

			// 存储excel第一行表头字段名
			List<String> excelFirstRowKeys = new ArrayList<String>();

			// 遍历行记录
			int rownum = 0;
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {

				Row row = rowIterator.next();

				Map<String, Object> mapCell = new HashMap<String, Object>();

				// 遍历所有列
				int cellnum = 0;
				Iterator<org.apache.poi.ss.usermodel.Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {

					org.apache.poi.ss.usermodel.Cell cell = cellIterator.next();

					if (rownum == 0 && firstRowKeys) {
						// 如果是第一行，并且第一行包含表头，则读取表头
						String keyName = "";
						switch (cell.getCellType()) {
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
							keyName = String.valueOf(cell.getNumericCellValue());
							break;
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
							keyName = cell.getStringCellValue();
							break;
						case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
							keyName = cell.getStringCellValue();
							break;
						}

						excelFirstRowKeys.add(keyName.trim());
						cellnum++;
						continue;
					}

					//String cellKey = excelFirstRowKeys.get(cellnum);
					String cellKey = String.valueOf(cellnum);
					if (null != dataListCellKeys && dataListCellKeys.size() > 0 && firstRowKeys) {
						cellKey = excelFirstRowKeys.get(cellnum);
						if (!dataListCellKeys.contains(cellKey)) {
							// 若不需要获取当前列数据，则跳过
							cellnum++;
							continue;
						}
					}

					switch (cell.getCellType()) {
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
						mapCell.put(cellKey, cell.getBooleanCellValue());
						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:

						if (DateUtil.isCellDateFormatted(cell)) {
							Date date = cell.getDateCellValue();
							mapCell.put(cellKey, cell.getDateCellValue());
							SimpleDateFormat sdf = new SimpleDateFormat(defaultDateFormat);
							mapCell.put(cellKey + "_format", sdf.format(date));
						} else {
							NumberFormat nf = NumberFormat.getInstance();
							nf.setGroupingUsed(false);// true时的格式：1,234,567,890
							String cellValue = nf.format(cell.getNumericCellValue());// 数值类型的数据为double，所以需要转换一下
							mapCell.put(cellKey, cellValue);
						}

						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
						mapCell.put(cellKey, cell.getStringCellValue());
						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
						mapCell.put(cellKey, cell.getCellFormula());
						break;
					case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
						mapCell.put(cellKey, "");
						break;
					default:
						mapCell.put(cellKey, "");
						break;
					}
					cellnum++;
				}

				if (!firstRowKeys || rownum > 0) {
					excelListData.add(mapCell);
				}

				rownum++;

			}

			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
