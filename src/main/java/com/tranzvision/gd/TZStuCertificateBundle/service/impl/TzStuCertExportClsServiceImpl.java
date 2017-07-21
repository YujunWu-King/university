package com.tranzvision.gd.TZStuCertificateBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZStuCertificateBundle.service.impl.TzStuCertExportClsServiceImpl")
public class TzStuCertExportClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			// 导出学生信息
			List<?> stuSeqList = jacksonUtil.getList("stuSeqs");
			String filepath = this.exportApplyInfo(stuSeqList, errorMsg);
			mapRet.put("fileUrl", filepath);
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}

		return strRet;
	}
   //导出学生信息
	private String exportApplyInfo(List<?> stuSeqList, String[] errorMsg) {
		String strRet = "";

		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			///String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();

			// 活动报名信息excel存储路径
			String eventExcelPath = "/stuCert/xlsx";

			// 完整的存储路径
			String fileDirPath = fileBasePath + eventExcelPath;

			// 生成数据
			// 表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			//List<String[]> listDataFields = new ArrayList<String[]>();
			dataCellKeys.add(new String[] { "TZ_REALNAME", "姓名" });
			dataCellKeys.add(new String[] { "TZ_GENDER", "性别" });
			dataCellKeys.add(new String[] { "TZ_CLASS_NAME", "班级名称" });
			dataCellKeys.add(new String[] { "TZ_PRG_NAME", "项目" });
			dataCellKeys.add(new String[] { "TZ_MOBILE_PHONE", "手机" });
			dataCellKeys.add(new String[] { "TZ_EMAIL", "邮箱" });
			dataCellKeys.add(new String[] { "TZ_CERT_TYPE_NAME", "证书类型" });
			dataCellKeys.add(new String[] { "TZ_ZHSH_ID", "证书编号" });
			dataCellKeys.add(new String[] { "TZ_BF_STATUS", "电子证书状态" });
			dataCellKeys.add(new String[] { "TZ_VIEW_NUM", "查询次数" });
			dataCellKeys.add(new String[] { "TZ_SHARE_NUM", "转发分享次数" });
			dataCellKeys.add(new String[] { "TZ_YZ_NUM", "验证次数" });
			// 数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			int num = stuSeqList.size();
			if(num>0){
			for (int i = 0; i < num; i++) {
				String stuSeqId = String.valueOf(stuSeqList.get(i));
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData=sqlQuery.queryForMap("select TZ_REALNAME,TZ_GENDER,TZ_CLASS_NAME,TZ_PRG_NAME,TZ_MOBILE_PHONE,TZ_EMAIL,TZ_CERT_TYPE_NAME,TZ_ZHSH_ID,TZ_BF_STATUS,TZ_VIEW_NUM,TZ_SHARE_NUM,TZ_YZ_NUM from PS_TZ_STU_ZS_VW  where TZ_SEQNUM=?", new Object[]{stuSeqId});
				String TZ_REALNAME=mapData.get("TZ_REALNAME")==null?"":mapData.get("TZ_REALNAME").toString();
				String TZ_CLASS_NAME=mapData.get("TZ_CLASS_NAME")==null?"":mapData.get("TZ_CLASS_NAME").toString();
				String TZ_PRG_NAME=mapData.get("TZ_PRG_NAME")==null?"":mapData.get("TZ_PRG_NAME").toString();
				String TZ_MOBILE_PHONE=mapData.get("TZ_MOBILE_PHONE")==null?"":mapData.get("TZ_MOBILE_PHONE").toString();
				String TZ_EMAIL=mapData.get("TZ_EMAIL")==null?"":mapData.get("TZ_EMAIL").toString();
				String TZ_CERT_TYPE_NAME=mapData.get("TZ_CERT_TYPE_NAME")==null?"":mapData.get("TZ_CERT_TYPE_NAME").toString();
				String TZ_ZHSH_ID=mapData.get("TZ_ZHSH_ID")==null?"":mapData.get("TZ_ZHSH_ID").toString();
				String TZ_VIEW_NUM=mapData.get("TZ_VIEW_NUM")==null?"":mapData.get("TZ_VIEW_NUM").toString();
				String TZ_SHARE_NUM=mapData.get("TZ_SHARE_NUM")==null?"":mapData.get("TZ_SHARE_NUM").toString();
				String TZ_YZ_NUM=mapData.get("TZ_YZ_NUM")==null?"":mapData.get("TZ_YZ_NUM").toString();
				String TZ_GENDER=sqlQuery.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_GENDER' AND TZ_ZHZ_ID=?", new Object[]{mapData.get("TZ_GENDER")}, "String");
				String TZ_BF_STATUS=sqlQuery.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BF_STATUS' AND TZ_ZHZ_ID=?", new Object[]{mapData.get("TZ_BF_STATUS")}, "String");
				mapData.put("TZ_REALNAME", TZ_REALNAME);
				mapData.put("TZ_GENDER", TZ_GENDER);
				mapData.put("TZ_CLASS_NAME", TZ_CLASS_NAME);
				mapData.put("TZ_PRG_NAME", TZ_PRG_NAME);
				mapData.put("TZ_MOBILE_PHONE", TZ_MOBILE_PHONE);
				mapData.put("TZ_EMAIL", TZ_EMAIL);
				mapData.put("TZ_CERT_TYPE_NAME", TZ_CERT_TYPE_NAME);
				mapData.put("TZ_ZHSH_ID", TZ_ZHSH_ID);
				mapData.put("TZ_BF_STATUS", TZ_BF_STATUS);
				mapData.put("TZ_VIEW_NUM", TZ_VIEW_NUM);
				mapData.put("TZ_SHARE_NUM", TZ_SHARE_NUM);
				mapData.put("TZ_YZ_NUM", TZ_YZ_NUM);
				dataList.add(mapData);
			 }
			}else{
				Map<String, Object> mapData = new HashMap<String, Object>();
				List<Map<String, Object>> list= sqlQuery.queryForList("select TZ_SEQNUM,TZ_REALNAME,TZ_GENDER,TZ_CLASS_NAME,TZ_PRG_NAME,TZ_MOBILE_PHONE,TZ_EMAIL,TZ_CERT_TYPE_NAME,TZ_ZHSH_ID,TZ_BF_STATUS,TZ_VIEW_NUM,TZ_SHARE_NUM,TZ_YZ_NUM from PS_TZ_STU_ZS_VW  order by TZ_SORT");
				for(int j = 0;j<list.size();j++){
				mapData=list.get(j);
				String TZ_REALNAME=mapData.get("TZ_REALNAME")==null?"":mapData.get("TZ_REALNAME").toString();
				String TZ_CLASS_NAME=mapData.get("TZ_CLASS_NAME")==null?"":mapData.get("TZ_CLASS_NAME").toString();
				String TZ_PRG_NAME=mapData.get("TZ_PRG_NAME")==null?"":mapData.get("TZ_PRG_NAME").toString();
				String TZ_MOBILE_PHONE=mapData.get("TZ_MOBILE_PHONE")==null?"":mapData.get("TZ_MOBILE_PHONE").toString();
				String TZ_EMAIL=mapData.get("TZ_EMAIL")==null?"":mapData.get("TZ_EMAIL").toString();
				String TZ_CERT_TYPE_NAME=mapData.get("TZ_CERT_TYPE_NAME")==null?"":mapData.get("TZ_CERT_TYPE_NAME").toString();
				String TZ_ZHSH_ID=mapData.get("TZ_ZHSH_ID")==null?"":mapData.get("TZ_ZHSH_ID").toString();
				String TZ_VIEW_NUM=mapData.get("TZ_VIEW_NUM")==null?"":mapData.get("TZ_VIEW_NUM").toString();
				String TZ_SHARE_NUM=mapData.get("TZ_SHARE_NUM")==null?"":mapData.get("TZ_SHARE_NUM").toString();
				String TZ_YZ_NUM=mapData.get("TZ_YZ_NUM")==null?"":mapData.get("TZ_YZ_NUM").toString();
				String TZ_GENDER=sqlQuery.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_GENDER' AND TZ_ZHZ_ID=?", new Object[]{mapData.get("TZ_GENDER")}, "String");
				String TZ_BF_STATUS=sqlQuery.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BF_STATUS' AND TZ_ZHZ_ID=?", new Object[]{mapData.get("TZ_BF_STATUS")}, "String");
				mapData.put("TZ_REALNAME", TZ_REALNAME);
				mapData.put("TZ_GENDER", TZ_GENDER);
				mapData.put("TZ_CLASS_NAME", TZ_CLASS_NAME);
				mapData.put("TZ_PRG_NAME", TZ_PRG_NAME);
				mapData.put("TZ_MOBILE_PHONE", TZ_MOBILE_PHONE);
				mapData.put("TZ_EMAIL", TZ_EMAIL);
				mapData.put("TZ_CERT_TYPE_NAME", TZ_CERT_TYPE_NAME);
				mapData.put("TZ_ZHSH_ID", TZ_ZHSH_ID);
				mapData.put("TZ_BF_STATUS", TZ_BF_STATUS);
				mapData.put("TZ_VIEW_NUM", TZ_VIEW_NUM);
				mapData.put("TZ_SHARE_NUM", TZ_SHARE_NUM);
				mapData.put("TZ_YZ_NUM", TZ_YZ_NUM);
				dataList.add(mapData);
			   }
			}

			// 生成本次导出的文件名
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Random random = new Random();
			int max = 999999999;
			int min = 100000000;
			/*String fileName = simpleDateFormat.format(new Date()) + "_" + oprid.toUpperCase() + "_"
					+ String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";*/
			String fileName = simpleDateFormat.format(new Date()) +"_"+ String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";
			ExcelHandle excelHandle = new ExcelHandle(request, fileDirPath, orgid, "apply");
			boolean rst = excelHandle.export2Excel(fileName, dataCellKeys, dataList);
			if (rst) {
				strRet = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + excelHandle.getExportExcelPath();
			} else {
				System.out.println("导出失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

		return strRet;
	}
}
