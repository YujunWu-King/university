/**
 * 
 */
package com.tranzvision.gd.util.poi.excel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;

/**
 * excel导出、导入测试样例类
 * 
 * @author SHIHUA
 * @since 2016-02-26
 */
@Service("com.tranzvision.gd.util.poi.excel.Test")
public class Test {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	/**
	 * 导出数据到excel文件
	 */
	public void exportDataSet() {

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String downloadPath = getSysHardCodeVal.getDownloadPath();
		String dtFormat = getSysHardCodeVal.getDateFormat();
		//String dtimeFormat = getSysHardCodeVal.getDateTimeFormat();

		ExcelHandle excelHandle = new ExcelHandle(request, downloadPath, orgid, "test");

		List<String[]> dataCellKeys = new ArrayList<String[]>();
		dataCellKeys.add(new String[] { "id", "编号" });
		dataCellKeys.add(new String[] { "name", "姓名" });
		dataCellKeys.add(new String[] { "sex", "性别" });
		dataCellKeys.add(new String[] { "age", "年龄" });
		dataCellKeys.add(new String[] { "birthday", "生日" });
		dataCellKeys.add(new String[] { "salary", "年薪" });

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		Map<String, Object> mapData1 = new HashMap<String, Object>();
		mapData1.put("id", "1");
		mapData1.put("name", "张三");
		mapData1.put("sex", "男");
		mapData1.put("age", 30);
		// mapData1.put("birthday", new Object[]{new Date(),dtimeFormat});
		mapData1.put("birthday", new Date());
		mapData1.put("salary", 10000.20d);
		dataList.add(mapData1);

		Map<String, Object> mapData2 = new HashMap<String, Object>();
		mapData2.put("id", "2");
		mapData2.put("name", "李四");
		mapData2.put("sex", "女");
		mapData2.put("age", 20);
		mapData2.put("birthday", new Object[] { new Date(), dtFormat });
		mapData2.put("salary", 10000.12);
		dataList.add(mapData2);

		String tdate = "1990-10-01";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

		Map<String, Object> mapData3 = new HashMap<String, Object>();
		mapData3.put("id", "3");
		mapData3.put("name", "王五");
		mapData3.put("sex", "男");
		mapData3.put("age", 26);
		try {
			Date birthday = simpleDateFormat.parse(tdate);
			mapData3.put("birthday", new Object[] { birthday, dtFormat });
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mapData3.put("salary", 500000.01);
		dataList.add(mapData3);

		boolean rst = excelHandle.export2Excel("excelName.xlsx", dataCellKeys, dataList);
		if (rst) {
			System.out.println("---------生成的excel文件路径----------");
			System.out.println(excelHandle.getExportExcelPath());
		} else {
			System.out.println("导出失败！");
		}

	}

	/**
	 * 生成模板数据文件
	 */
	public void exportDataByTemplate() {
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String downloadPath = getSysHardCodeVal.getDownloadPath();
		String uploadPath = getSysHardCodeVal.getTmpFileUploadPath();

		ExcelHandle excelHandle = new ExcelHandle(request, downloadPath, orgid, "test");

		String tplFilePath = uploadPath + File.separator + "tplexcel.xlsx";

		List<String> dataListCellKeys = new ArrayList<String>();
		dataListCellKeys.add("id");
		dataListCellKeys.add("name");
		dataListCellKeys.add("sex");
		dataListCellKeys.add("age");
		dataListCellKeys.add("birthday");
		dataListCellKeys.add("salary");

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		Map<String, Object> mapData1 = new HashMap<String, Object>();
		mapData1.put("id", "1");
		mapData1.put("name", "张三");
		mapData1.put("sex", "男");
		mapData1.put("age", 30);
		mapData1.put("birthday", new Date());
		mapData1.put("salary", 10000.00);
		dataList.add(mapData1);

		Map<String, Object> mapData2 = new HashMap<String, Object>();
		mapData2.put("id", "2");
		mapData2.put("name", "李四");
		mapData2.put("sex", "女");
		mapData2.put("age", 20);
		mapData2.put("birthday", new Date());
		mapData2.put("salary", 10000.00);
		dataList.add(mapData2);

		String tdate = "1990-10-01";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Map<String, Object> mapData3 = new HashMap<String, Object>();
		mapData3.put("id", "3");
		mapData3.put("name", "王五");
		mapData3.put("sex", "男");
		mapData3.put("age", 26);
		try {
			Date birthday = simpleDateFormat.parse(tdate);
			mapData3.put("birthday", birthday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mapData3.put("salary", 500000.00);
		dataList.add(mapData3);

		List<String> dataCellKeysList = new ArrayList<String>();
		dataCellKeysList.add("orgid");
		dataCellKeysList.add("orgname");
		dataCellKeysList.add("total");

		Map<String, Object> dataCelValsMap = new HashMap<String, Object>();
		dataCelValsMap.put("orgid", "MBA");
		dataCelValsMap.put("orgname", "工商管理硕士");
		dataCelValsMap.put("total", 5);

		boolean rst = excelHandle.export2ExcelByTpl(tplFilePath, "测试导出数据.xlsx", dataListCellKeys, dataList,
				dataCellKeysList, dataCelValsMap);
		if (rst) {
			System.out.println("---------生成的excel文件路径----------");
			System.out.println(excelHandle.getExportExcelPath());
		} else {
			System.out.println("导出失败！");
		}

	}

	/**
	 * 读取excel数据文件（简单列表）
	 */
	public void importData() {
		String uploadPath = getSysHardCodeVal.getTmpFileUploadPath();

		String dataFilePath = uploadPath + File.separator + "readexcel.xlsx";

		List<String> dataListCellKeys = new ArrayList<String>();
		// dataListCellKeys.add("id");
		// dataListCellKeys.add("name");

		ExcelHandle excelHandle = new ExcelHandle(request);
		excelHandle.readExcel(dataFilePath, dataListCellKeys, true);

		JacksonUtil jacksonUtil = new JacksonUtil();
		ArrayList<Map<String, Object>> listData = excelHandle.getExcelListData();

		System.out.println("---------读取的excel列表数据----------");
		System.out.println(jacksonUtil.List2json(listData));

	}

	/**
	 * 读取指定模板实例的数据文件
	 */
	public void importDataTpl() {
		String uploadPath = getSysHardCodeVal.getTmpFileUploadPath();

		String tplFilePath = uploadPath + File.separator + "tplexcel.xlsx";
		String dataFilePath = uploadPath + File.separator + "readtplexcel.xlsx";

		List<String> dataCellKeys = new ArrayList<String>();
		dataCellKeys.add("orgid");
		dataCellKeys.add("orgname");
		dataCellKeys.add("total");

		List<String> dataListCellKeys = new ArrayList<String>();
		dataListCellKeys.add("id");
		dataListCellKeys.add("name");
		dataListCellKeys.add("sex");
		dataListCellKeys.add("age");
		dataListCellKeys.add("birthday");
		dataListCellKeys.add("salary");

		ExcelHandle excelHandle = new ExcelHandle(request);
		excelHandle.readExcelByTpl(tplFilePath, dataFilePath, dataListCellKeys, dataCellKeys);

		JacksonUtil jacksonUtil = new JacksonUtil();
		ArrayList<Map<String, Object>> listData = excelHandle.getExcelListData();
		Map<String, Object> mapData = excelHandle.getExcelMapData();

		System.out.println("---------独立字段数据----------");
		System.out.println(jacksonUtil.Map2json(mapData));

		System.out.println("---------列表数据----------");
		System.out.println(jacksonUtil.List2json(listData));

	}

}
