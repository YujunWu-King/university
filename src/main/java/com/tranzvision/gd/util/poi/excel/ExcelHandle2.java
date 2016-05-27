/**
 * 
 */
package com.tranzvision.gd.util.poi.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 处理导出excel工具类
 * 
 * @author SHIHUA
 * @since 2016-02-25
 */
public class ExcelHandle2 {
	
	/**
	 * 导出数据，生成的Excel文件路径
	 */
	private String exportExcelPath;
	
	private String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";
	
	//下载路径;
	private String expDirPath;
	//实际路径;
	private String absexpDirPath;
	
	
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
	public ExcelHandle2(String expDirPath,String absexpDirPath) {
		this.expDirPath = expDirPath;
		this.absexpDirPath = absexpDirPath;
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
	 * 创建要生成的File实例
	 * 
	 * @param expFileName
	 *            文件名
	 * @return
	 */
	private File newFileObject(String expFileName) {

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
	 * 获取生成的Excel文件路径
	 * 
	 * @return
	 */
	public String getExportExcelPath() {
		return exportExcelPath;
	}

}
