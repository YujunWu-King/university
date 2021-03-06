package com.tranzvision.gd.TZLeaguerAccountBundle.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglStuClsServiceImpl;

/**
 * 申请用户管理；原：TZ_GD_USERGL_PKG:TZ_GD_USERGL_CLS
 * 
 * @author tang
 * @since 2015-11-20
 */
@Service("com.tranzvision.gd.TZLeaguerAccountBundle.service.impl.LeaguerAccountMgServiceImpl")
@SuppressWarnings("unchecked")
public class LeaguerAccountMgServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private PsTzRegUserTMapper PsTzRegUserTMapper;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private TzGdBmglStuClsServiceImpl TzGdBmglStuClsServiceImpl;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private GetSeqNum getSeqNum;
	// @Override
	public String tzQueryList11(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// 获取当前机构;
			String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			System.out.println("s="+strJgid);
			Integer userCount = 0;
			String strUserCountSql = "SELECT COUNT(*) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=?";
			userCount = SqlQuery.queryForObject(strUserCountSql, new Object[] { strJgid }, "Integer");

			// 显示的注册项;
			int showFieldNum = 0;
			String showFieldNumSQL = "SELECT COUNT(1) from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' and TZ_REG_FIELD_ID not in ('TZ_PASSWORD','TZ_REPASSWORD') order by TZ_ORDER asc limit 0,10";
			showFieldNum = SqlQuery.queryForObject(showFieldNumSQL, new Object[] { strJgid }, "Integer");

			// json数据要的结果字段;
			String[] resultFldArray = new String[showFieldNum + 1];
			resultFldArray[0] = "OPRID";
			if (showFieldNum != 0) {
				String showSQL = "SELECT TZ_REG_FIELD_ID from PS_TZ_REG_FIELD_T where TZ_JG_ID=? and TZ_ENABLE='Y' and TZ_REG_FIELD_ID not in ('TZ_PASSWORD','TZ_REPASSWORD') order by TZ_ORDER asc limit 0,10";
				List<Map<String, Object>> list = SqlQuery.queryForList(showSQL, new Object[] { strJgid });
				for (int j = 0; j < list.size(); j++) {
					String regFieldId = (String) list.get(j).get("TZ_REG_FIELD_ID");
					if ("TZ_GENDER".equals(regFieldId)) {
						regFieldId = "TZ_ZHZ_DMS";
					}
					resultFldArray[j + 1] = regFieldId;
				}
			}

			String[][] orderByArr = null;
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("OPRID", rowList[0]);

					int rowListLen = rowList.length;
					for (int k = 0; k < 10; k++) {
						if (k < rowListLen - 1) {
							mapList.put("fieldStr_" + (k + 1), rowList[k + 1]);
						} else {
							mapList.put("fieldStr_" + (k + 1), "");
						}
					}
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
			/*
			 * String strTransSql =
			 * "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'"
			 * ;
			 * 
			 * //姓名、证件号、面试申请号、性别、手机、邮箱、报考批次（取值如下图，显示完整的班级批次名称，多个批次需要合并显示）、激活状态、
			 * 创建时间、账号锁定状态、黑名单 String strRegUserList =
			 * "SELECT A.OPRID,A.TZ_REALNAME,A.TZ_GENDER,A.NATIONAL_ID,(SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='TZ_GENDER' AND A.TZ_ZHZ_ID=A.TZ_GENDER AND TZ_EFF_STATUS='A' limit 0,1) TZ_GENDERDESC,B.TZ_ZY_SJ,B.TZ_ZY_EMAIL,C.TZ_JIHUO_ZT,(SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='TZ_JIHUO_ZT' AND A.TZ_ZHZ_ID=C.TZ_JIHUO_ZT AND TZ_EFF_STATUS='A' limit 0,1) TZ_JIHUODESC,date_format(C.TZ_ZHCE_DT,'%Y-%m-%d %H:%i') TZ_ZHCE_DT,A.TZ_BLACK_NAME,(SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='TZ_BLACK_NAME' AND A.TZ_ZHZ_ID=A.TZ_BLACK_NAME AND TZ_EFF_STATUS='A' limit 0,1) TZ_BLACKUSER,C.TZ_MSH_ID FROM PS_TZ_REG_USER_T A LEFT JOIN PS_TZ_LXFSINFO_TBL B ON A.OPRID=B.TZ_LYDX_ID JOIN PS_TZ_AQ_YHXX_TBL C ON A.OPRID=C.TZ_DLZH_ID WHERE B.TZ_LXFS_LY='ZCYH' AND TZ_JG_ID=? limit ?,?"
			 * ; List<Map<String, Object>> userMap =
			 * SqlQuery.queryForList(strRegUserList, new Object[] { strJgid,
			 * numStart,numLimit}); if(userMap!=null&&userMap.size()>0){
			 * for(Object userMapObj:userMap){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String strOprid =
			 * result1.get("OPRID")==null ? "" :
			 * String.valueOf(result1.get("OPRID")); String strName =
			 * result1.get("TZ_REALNAME")==null ? "" :
			 * String.valueOf(result1.get("TZ_REALNAME")); String strGender =
			 * result1.get("TZ_GENDER")==null ? "" :
			 * String.valueOf(result1.get("TZ_GENDER")); String strGenderDesc =
			 * result1.get("TZ_GENDERDESC")==null ? "" :
			 * String.valueOf(result1.get("TZ_GENDERDESC")); String strNationId
			 * = result1.get("NATIONAL_ID")==null ? "" :
			 * String.valueOf(result1.get("NATIONAL_ID")); String strPhone =
			 * result1.get("TZ_ZY_SJ")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZY_SJ")); String strEmail =
			 * result1.get("TZ_ZY_EMAIL")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZY_EMAIL")); String strActive =
			 * result1.get("TZ_JIHUO_ZT")==null ? "" :
			 * String.valueOf(result1.get("TZ_JIHUO_ZT")); String strActiveDesc
			 * = result1.get("TZ_JIHUODESC")==null ? "" :
			 * String.valueOf(result1.get("TZ_JIHUODESC")); String
			 * strAddDateTime = result1.get("TZ_ZHCE_DT")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHCE_DT")); String strBlackUserFlg
			 * = result1.get("TZ_BLACK_NAME")==null ? "" :
			 * String.valueOf(result1.get("TZ_BLACK_NAME")); String
			 * strBlackUserFlgDesc = result1.get("TZ_BLACKUSER")==null ? "" :
			 * String.valueOf(result1.get("TZ_BLACKUSER")); String strMshID =
			 * result1.get("ROW_ADDED_DTTM")==null ? "" :
			 * String.valueOf(result1.get("TZ_MSH_ID")); String strLockSql =
			 * "SELECT (SELECT A.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A WHERE A.TZ_ZHZJH_ID='ACCTLOCK' AND A.TZ_ZHZ_ID=P.ACCTLOCK AND TZ_EFF_STATUS='A' limit 0,1) TZ_ZHZ_DMS FROM PSOPRDEFN P WHERE OPRID=?"
			 * ; String strLockFlg = SqlQuery.queryForObject(strLockSql, new
			 * Object[] {strOprid}, "String"); strNationId =
			 * "130827198811111111"; strMshID = "2017XXXXX"; Map<String, Object>
			 * mapList = new HashMap<String, Object>(); mapList.put("OPRID",
			 * strOprid); mapList.put("userName", strName);
			 * mapList.put("userSex", strGenderDesc); mapList.put("nationId",
			 * strNationId); mapList.put("userEmail", strEmail);
			 * mapList.put("userPhone", strPhone); mapList.put("jihuoZt",
			 * strActiveDesc); mapList.put("zcTime", strAddDateTime);
			 * mapList.put("acctlock", strLockFlg); mapList.put("hmdUser",
			 * strBlackUserFlgDesc); mapList.put("mshId", strMshID);
			 * mapList.put("applyInfo", "2017年国际MBA招生第一批次;2017年国际MBA招生第=批次");
			 * 
			 * //"TZ_REALNAME", "TZ_ZHZ_DMS", "TZ_EMAIL", "TZ_MOBILE",
			 * "TZ_JIHUO_ZT_DESC", "TZ_ZHCE_DT", "ACCTLOCK", "TZ_BLACK_NAME"};
			 * 
			 * listData.add(mapList); } } mapRet.replace("total", userCount);
			 * mapRet.replace("root", listData);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		System.out.println("enter tzQueryList");
		try {
			/*String[][] orderByArr;
			
			String sort = request.getParameter("sort");
			if (sort != null && !"".equals(sort)) {
				sort = "{\"sort\":" + sort + "}";
				jacksonUtil.json2Map(sort);
				List<Map<String, String>> sortlist = (List<Map<String, String>>) jacksonUtil.getList("sort");
				List<String[]> orderList = new ArrayList<String[]>();
				for (Map<String, String> sortMap : sortlist) {
					String columnField = sortMap.get("property");
					String sortMethod = sortMap.get("direction");
					if ("OPRID".equals(columnField)) {
						orderList.add(new String[] { "OPRID", sortMethod });
					}
					if ("userName".equals(columnField)) {
						orderList.add(new String[] { "TZ_REALNAME", sortMethod });
					}
					if ("userSex".equals(columnField)) {
						orderList.add(new String[] { "TZ_GENDER", sortMethod });
					}
					if ("userEmail".equals(columnField)) {
						orderList.add(new String[] { "TZ_EMAIL", sortMethod });
					}
					if ("userPhone".equals(columnField)) {
						orderList.add(new String[] { "TZ_MOBILE", sortMethod });
					}
					if ("jihuoZt".equals(columnField)) {
						orderList.add(new String[] { "TZ_JIHUO_ZT", sortMethod });
					}
					if ("zcTime".equals(columnField)) {
						orderList.add(new String[] { "TZ_ZHCE_DT", sortMethod });
					}
					if ("acctlock".equals(columnField)) {
						orderList.add(new String[] { "ACCTLOCK", sortMethod });
					}
					if ("hmdUser".equals(columnField)) {
						orderList.add(new String[] { "TZ_BLACK_NAME", sortMethod });
					}
					if ("nationId".equals(columnField)) {
						orderList.add(new String[] { "NATIONAL_ID", sortMethod });
					}
					if ("mshId".equals(columnField)) {
						orderList.add(new String[] { "TZ_MSH_ID", sortMethod });
					}
					if ("applyInfo".equals(columnField)) {
						orderList.add(new String[] { "TZ_CLASS_NAME", sortMethod });
					}
					if ("classID".equals(columnField)) {
						orderList.add(new String[] { "TZ_CLASS_ID", sortMethod });
					}
					if ("appInsID".equals(columnField)) {
						orderList.add(new String[] { "TZ_APP_INS_ID", sortMethod });
					}
					if ("fillProportion".equals(columnField)) {
						orderList.add(new String[] { "TZ_FILL_PROPORTION", sortMethod });
					}
					if ("bitch".equals(columnField)) {
						orderList.add(new String[] { "TZ_BATCH_NAME", sortMethod });
					}
					if ("ms_result".equals(columnField)) {
						orderList.add(new String[] { "TZ_RESULT_CODE", sortMethod });
					}
					if("tj_zt".equals(columnField)){
						orderList.add(new String[]{"TZ_APP_FORM_STA",sortMethod});
					}
					if("remark".equals(columnField)){
						orderList.add(new String[]{"TZ_REMARK",sortMethod});
					}
				}
				orderByArr = new String[orderList.size()][2];
				for (int i = 0; i < orderList.size(); i++) {
					orderByArr[i] = orderList.get(i);
				}

			} else {
				orderByArr = new String[][] {{ "TZ_FILL_PROPORTION", "ASC" }};
			}*/

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {new String[]{"TZ_ZHCE_DT","DESC"}};
			// json数据要的结果字段;
			String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_MOBILE", "TZ_EMAIL","TZ_JIHUO_ZT", "TZ_ZHCE_DT","TZ_MSH_ID","TZ_JG_ID"};

		
			// 可配置搜索通用函数;
		
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);
		//	System.out.println(obj[1]);
			/*String TZ_FILL_PROPORTION = "";
			String strBmbTplSQL = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String viewNameSQL = "";
			String strBmbTpl="";*/
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			//	System.out.println(list.get(0));
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
				//	System.out.println(rowList[0]);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("OPRID", rowList[0]);
					mapList.put("TZ_REALNAME", rowList[1]);
					mapList.put("TZ_MOBILE", rowList[2]);
					mapList.put("TZ_EMAIL", rowList[3]);
					mapList.put("TZ_JIHUO_ZT", rowList[4]);
					mapList.put("TZ_ZHCE_DT", rowList[5]);
					mapList.put("TZ_MSH_ID", rowList[6]);
					mapList.put("TZ_JG_ID", rowList[7]);
					/*mapList.put("acctlock", rowList[7]);
					mapList.put("hmdUser", rowList[8]);
					mapList.put("nationId", rowList[9]);
					mapList.put("mshId", rowList[10]);
					mapList.put("applyInfo", rowList[11]);
					mapList.put("bitch", rowList[15]);
					mapList.put("ms_result", rowList[16]);
					mapList.put("tj_zt", rowList[17]);
					mapList.put("remark", rowList[18]!=null?stripHtml(rowList[18]):rowList[18]);*/
				//	mapList.put("zcTime", rowList[9]);
				//	mapList.put("acctlock", rowList[10]);
				//	mapList.put("hmdUser", rowList[11]);
					//mapList.put("classID", rowList[12]);
					//mapList.put("appInsID", rowList[13]);
					/*TZ_FILL_PROPORTION =  rowList[14];
					int leng = 0;*/
					
					
					/*strBmbTpl = jdbcTemplate.queryForObject(strBmbTplSQL, new Object[] { rowList[12] },
							"String");*/
					
				//	 System.out.println("班级id"+rowList[12]);
					/*if (strBmbTpl == null) {
						strBmbTpl = "";
					}*/
					/*if (!strBmbTpl.equals("")) {
						// 报名表总页数
						// 情况1：双层报名表
						viewNameSQL = "select count(1) from PS_TZ_APP_XXXPZ_T where  TZ_APP_TPL_ID=? and TZ_COM_LMC=? and TZ_FPAGE_BH !=?";
						leng = jdbcTemplate.queryForObject(viewNameSQL, new Object[] { strBmbTpl, "Page", "" },
								"Integer");
						// System.out.println("长度1"+leng);
						// 情况2：单层报名表
						if (leng == 0) {
							// System.out.println("长度"+leng);
							viewNameSQL = "select count(1) from PS_TZ_APP_XXXPZ_T where  TZ_APP_TPL_ID=? and TZ_COM_LMC=?";
							leng = jdbcTemplate.queryForObject(viewNameSQL, new Object[] { strBmbTpl, "Page" },
									"Integer");
							// System.out.println("长度2"+leng);
						}
					}*/
					// 最后一页不算
					// if (leng > 1) {
					// leng = leng - 1;
					// }
					// System.out.println("实例id"+rowList[13]);
					// System.out.println("长度3"+leng);
					//mapList.put("fillProportion", TzGdBmglStuClsServiceImpl.getBMBFillProportion(rowList[13], leng,TZ_FILL_PROPORTION));
					listData.add(mapList);

				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/* 关闭账号 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if (jacksonUtil.containsKey("OPRID")) {
					// 用户账号;
					String strOprId = jacksonUtil.getString("OPRID");

					Psoprdefn psoprdefn = new Psoprdefn();
					psoprdefn.setOprid(strOprId);
					psoprdefn.setAcctlock(Short.valueOf("1"));
					int i = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
					if (i > 0) {
					} else {
						errMsg[0] = "1";
						errMsg[1] = "信息保存失败";
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 加入黑名单 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if (jacksonUtil.containsKey("OPRID")) {
					// 用户账号;
					String strOprId = jacksonUtil.getString("OPRID");
					// TZ_REG_USER_T
					PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
					psTzRegUserT.setOprid(strOprId);
					psTzRegUserT.setTzBlackName("Y");
					int i = PsTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
					if (i > 0) {
					} else {
						errMsg[0] = "1";
						errMsg[1] = "信息保存失败";
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	// 重置密码等操作;
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("success", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 重置密码检查;
			if ("CHGPWD".equals(oprType)) {
				jacksonUtil.json2Map(strParams);
				if (jacksonUtil.containsKey("data")) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("data");
					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							String OPRID = (String) list.get(i).get("OPRID");
							String sql = "select count(1) from PS_TZ_AQ_YHXX_TBL a, PSOPRDEFN b where a.OPRID = b.OPRID and b.OPRID=? and (a.TZ_JIHUO_ZT<>'Y' or b.ACCTLOCK=1)";
							int count = SqlQuery.queryForObject(sql, new Object[] { OPRID }, "Integer");
							if (count > 0) {
								errorMsg[0] = "1";
								errorMsg[1] = "不能修改未激活或锁定的账号的密码";
								returnJsonMap.replace("success", "false");
							} else {
								returnJsonMap.replace("success", "true");
							}
						}
					}
				}
			}

			// 修改密码;
			if ("PWD".equals(oprType)) {
				jacksonUtil.json2Map(strParams);
				if (jacksonUtil.containsKey("password") && jacksonUtil.containsKey("data")) {
					String password = jacksonUtil.getString("password");
					if (password == null || "".equals(password.trim())) {
						errorMsg[0] = "1";
						errorMsg[1] = "密码不能为空";
					}

					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("data");
					if (list != null && !"1".equals(errorMsg[0])) {
						for (int i = 0; i < list.size(); i++) {
							String OPRID = (String) list.get(i).get("OPRID");
							String tmpPassword = DESUtil.encrypt(password, "TZGD_Tranzvision");

							Psoprdefn psoprdefn = new Psoprdefn();
							psoprdefn.setOprid(OPRID);
							psoprdefn.setOperpswd(tmpPassword);

							int success = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
							if (success > 0) {
								returnJsonMap.replace("success", "true");
							} else {
								errorMsg[0] = "1";
								errorMsg[1] = "修改用户密码失败。";
								returnJsonMap.replace("success", "false");
							}
						}
					}
				}
			}
			// 获取搜索条件中下拉框
			if ("GETVALUE".equals(oprType)) {
				returnJsonMap.replace("success", "true");
				returnJsonMap.put("dataStore", this.getSearchTranslateValue());
			}
			if("exportApplyInfo".equals(oprType)){
				returnJsonMap.replace("success", "true");
				returnJsonMap.put("fileUrl", this.exportApplyInfo());
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	// 搜索条件中的下拉框值
	public String getSearchTranslateValue() {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("success", "success");
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			String strTranslateSql = "SELECT TZ_ZHZ_ID,TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_EFF_STATUS='A'";
			// 申请的专业
			String strReturn1Json = "";
			List<Map<String, Object>> mapList1 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_MBAX_ZHUANY" });
			if (mapList1 != null && mapList1.size() > 0) {
				for (Object userMapObj : mapList1) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn1Json)) {
						strReturn1Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn1Json = strReturn1Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}

				}
			}
			returnJsonMap.put("applyMajorStore", "[" + strReturn1Json + "]");
			// 批次-TZ_APPLY_PCH
			String strReturn2Json = "";
			List<Map<String, Object>> mapList2 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_APPLY_PCH" });
			if (mapList2 != null && mapList2.size() > 0) {
				for (Object userMapObj : mapList2) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn2Json)) {
						strReturn2Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn2Json = strReturn2Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}

				}
			}
			returnJsonMap.put("batchStore", "[" + strReturn2Json + "]");
			// 志愿-TZ_APPLY_ZY_ID
			String strReturn3Json = "";
			List<Map<String, Object>> mapList3 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_APPLY_ZY_ID" });
			if (mapList3 != null && mapList3.size() > 0) {
				for (Object userMapObj : mapList3) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn3Json)) {
						strReturn3Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn3Json = strReturn3Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}

				}
			}
			returnJsonMap.put("zhiyStore", "[" + strReturn3Json + "]");
			// 报名表状态-TZ_APP_FORM_STA
			String strReturn4Json = "";
			List<Map<String, Object>> mapList4 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_APPFORM_STATE" });
			if (mapList4 != null && mapList4.size() > 0) {
				for (Object userMapObj : mapList4) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn4Json)) {
						strReturn4Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn4Json = strReturn4Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("appStatusStore", "[" + strReturn4Json + "]");
			// 条件录取资格面试结果 - TZ_TJLQZG
			String strReturn5Json = "";
			List<Map<String, Object>> mapList5 = SqlQuery.queryForList(strTranslateSql, new Object[] { "TZ_TJLQZG" });
			if (mapList5 != null && mapList5.size() > 0) {
				for (Object userMapObj : mapList5) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn5Json)) {
						strReturn5Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn5Json = strReturn5Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("luStore", "[" + strReturn5Json + "]");
			// 条件录取资格项目 - TZ_TJLQZG_XM
			String strReturn6Json = "";
			List<Map<String, Object>> mapList6 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_TJLQZG_XM" });
			if (mapList6 != null && mapList6.size() > 0) {
				for (Object userMapObj : mapList6) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn6Json)) {
						strReturn6Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn6Json = strReturn6Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("lu2Store", "[" + strReturn6Json + "]");
			// 获得结果批次-与报考批次一样
			String strReturn7Json = "";
			/*
			 * List<Map<String, Object>> mapList7 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_MSPS_JGPC"}); if(mapList7!=null&&mapList7.size()>0){
			 * for(Object userMapObj:mapList7){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn7Json)){ strReturn7Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn7Json = strReturn7Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("getResultStore", "[" + strReturn7Json + "]");
			// 面试批次-与报考批次一样
			String strReturn8Json = "";
			/*
			 * List<Map<String, Object>> mapList8 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_MSPS_PC"}); if(mapList8!=null&&mapList8.size()>0){
			 * for(Object userMapObj:mapList8){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn8Json)){ strReturn8Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn8Json = strReturn8Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("getResultStore", "[" + strReturn8Json + "]");
			// 最初奖学金授予情况/最终奖学金授予情况-TZ_MBA_SCHOLARSHIP
			String strReturn9Json = "";
			List<Map<String, Object>> mapList9 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_MBA_SCHOLARSHIP" });
			if (mapList9 != null && mapList9.size() > 0) {
				for (Object userMapObj : mapList9) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn9Json)) {
						strReturn9Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn9Json = strReturn9Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("zcJxjStore", "[" + strReturn9Json + "]");
			// 联考前条件录取资格-与条件录取资格一样
			String strReturn10Json = "";
			/*
			 * List<Map<String, Object>> mapList10 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_LKQTJLQZGZT"}); if(mapList10!=null&&mapList10.size()>0){
			 * for(Object userMapObj:mapList10){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn10Json)){ strReturn10Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn10Json = strReturn10Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("lkqTjlqStore", "[" + strReturn10Json + "]");
			// 联考前条件录取项目-与条件录取项目一样
			String strReturn11Json = "";
			/*
			 * List<Map<String, Object>> mapList11 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_LKQTJLQZGZT_XM"}); if(mapList11!=null&&mapList11.size()>0){
			 * for(Object userMapObj:mapList11){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn11Json)){ strReturn11Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn11Json = strReturn11Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("lkqTjlqXmStore", "[" + strReturn11Json + "]");
			// 预录取资格-与条件录取资格一样
			String strReturn12Json = "";
			/*
			 * List<Map<String, Object>> mapList12 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_YLQZGZT"}); if(mapList12!=null&&mapList12.size()>0){
			 * for(Object userMapObj:mapList12){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn12Json)){ strReturn12Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn12Json = strReturn12Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("ylqZgStore", "[" + strReturn12Json + "]");
			// 预录取资格项目-与条件录取项目一样
			String strReturn13Json = "";
			/*
			 * List<Map<String, Object>> mapList13 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_YLQZGZT_XM"}); if(mapList13!=null&&mapList13.size()>0){
			 * for(Object userMapObj:mapList13){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn13Json)){ strReturn13Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn13Json = strReturn13Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("ylqZgXmStore", "[" + strReturn13Json + "]");
			// 正式录取资格
			String strReturn14Json = "";
			List<Map<String, Object>> mapList14 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_ZSLQZGZT" });
			if (mapList14 != null && mapList14.size() > 0) {
				for (Object userMapObj : mapList14) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn14Json)) {
						strReturn14Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn14Json = strReturn14Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("zslqZgStore", "[" + strReturn14Json + "]");
			// 正式录取资格项目-与条件录取项目一样
			String strReturn15Json = "";
			/*
			 * List<Map<String, Object>> mapList15 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_ZSLQZGZT_XM"}); if(mapList15!=null&&mapList15.size()>0){
			 * for(Object userMapObj:mapList15){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn15Json)){ strReturn15Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn15Json = strReturn15Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("zslqZgXmStore", "[" + strReturn15Json + "]");
			// 入学情况
			String strReturn16Json = "";
			List<Map<String, Object>> mapList16 = SqlQuery.queryForList(strTranslateSql, new Object[] { "TZ_RXQK" });
			if (mapList16 != null && mapList16.size() > 0) {
				for (Object userMapObj : mapList16) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn16Json)) {
						strReturn16Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn16Json = strReturn16Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("ruXueQkStore", "[" + strReturn16Json + "]");
			// 入学项目-与条件录取项目一样
			String strReturn17Json = "";
			/*
			 * List<Map<String, Object>> mapList17 =
			 * SqlQuery.queryForList(strTranslateSql, new Object[]
			 * {"TZ_RXQK_XM"}); if(mapList17!=null&&mapList17.size()>0){
			 * for(Object userMapObj:mapList17){ Map<String,Object>
			 * result1=(Map<String,Object>) userMapObj; String TValue =
			 * result1.get("TZ_ZHZ_ID")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_ID")); String TLDesc =
			 * result1.get("TZ_ZHZ_DMS")==null ? "" :
			 * String.valueOf(result1.get("TZ_ZHZ_DMS"));
			 * if("".equals(strReturn17Json)){ strReturn17Json =
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; }else{ strReturn17Json = strReturn17Json + "," +
			 * tzGdObject.getHTMLText(
			 * "HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",TValue,TLDesc)
			 * ; } } }
			 */
			returnJsonMap.put("ruXueXmStore", "[" + strReturn17Json + "]");
			// 工作所在省市
			String strReturn18Json = "";
			String strProvinceSql = "SELECT STATE,DESCR FROM PS_STATE_TBL WHERE COUNTRY='CHN'";
			List<Map<String, Object>> mapList18 = SqlQuery.queryForList(strProvinceSql, new Object[] {});
			if (mapList18 != null && mapList18.size() > 0) {
				for (Object userMapObj : mapList18) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("STATE") == null ? "" : String.valueOf(result1.get("STATE"));
					String TLDesc = result1.get("DESCR") == null ? "" : String.valueOf(result1.get("DESCR"));
					if ("".equals(strReturn18Json)) {
						strReturn18Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn18Json = strReturn18Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("workProvinceStore", "[" + strReturn18Json + "]");
			// 行业类别
			String strReturn19Json = "";
			List<Map<String, Object>> mapList19 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_MBA_ZS_HYLB" });
			if (mapList19 != null && mapList19.size() > 0) {
				for (Object userMapObj : mapList19) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn19Json)) {
						strReturn19Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn19Json = strReturn19Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("industryTypeStore", "[" + strReturn19Json + "]");
			// 工作职能
			String strReturn20Json = "";
			List<Map<String, Object>> mapList20 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_MBA_ZS_GWZN" });
			if (mapList20 != null && mapList20.size() > 0) {
				for (Object userMapObj : mapList20) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn20Json)) {
						strReturn20Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn20Json = strReturn20Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("workZhNStore", "[" + strReturn20Json + "]");
			// 职位类型-TZ_WORK_TYPE
			String strReturn21Json = "";
			List<Map<String, Object>> mapList21 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_MBA_ZW_LX" });
			if (mapList21 != null && mapList21.size() > 0) {
				for (Object userMapObj : mapList21) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn21Json)) {
						strReturn21Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn21Json = strReturn21Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("PositionTypeStore", "[" + strReturn21Json + "]");
			// 是否
			String strReturn22Json = "";
			List<Map<String, Object>> mapList22 = SqlQuery.queryForList(strTranslateSql, new Object[] { "TZ_SF_SALE" });
			if (mapList22 != null && mapList22.size() > 0) {
				for (Object userMapObj : mapList22) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn22Json)) {
						strReturn22Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn22Json = strReturn22Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("isYN", "[" + strReturn22Json + "]");
			// 政治面貌
			String strReturn23Json = "";
			List<Map<String, Object>> mapList23 = SqlQuery.queryForList(strTranslateSql,
					new Object[] { "TZ_POLITICAL" });
			if (mapList23 != null && mapList23.size() > 0) {
				for (Object userMapObj : mapList23) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn23Json)) {
						strReturn23Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn23Json = strReturn23Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("ZhZhMMStore", "[" + strReturn23Json + "]");
			// 是否有效
			String strReturn24Json = "";
			List<Map<String, Object>> mapList24 = SqlQuery.queryForList(strTranslateSql, new Object[] { "TZ_ISVALID" });
			if (mapList24 != null && mapList24.size() > 0) {
				for (Object userMapObj : mapList24) {
					Map<String, Object> result1 = (Map<String, Object>) userMapObj;
					String TValue = result1.get("TZ_ZHZ_ID") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_ID"));
					String TLDesc = result1.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(result1.get("TZ_ZHZ_DMS"));
					if ("".equals(strReturn24Json)) {
						strReturn24Json = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE",
								TValue, TLDesc);
					} else {
						strReturn24Json = strReturn24Json + "," + tzGdObject
								.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ITEM_STORE", TValue, TLDesc);
					}
				}
			}
			returnJsonMap.put("isValStore", "[" + strReturn24Json + "]");

			strRet = tzGdObject.getHTMLText("HTML.TZLeaguerAccountBundle.TZ_SEARCH_ALL_STORE", strReturn1Json,
					strReturn2Json, strReturn3Json, strReturn4Json, strReturn5Json, strReturn6Json, strReturn7Json,
					strReturn8Json, strReturn9Json, strReturn10Json, strReturn11Json, strReturn12Json, strReturn13Json,
					strReturn14Json, strReturn15Json, strReturn16Json, strReturn17Json, strReturn18Json,
					strReturn19Json, strReturn20Json, strReturn21Json, strReturn22Json, strReturn23Json,
					strReturn24Json);

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return strRet;
	}
	//提出备注自带<p>标签
	public static String stripHtml(String content) { 
		// <p>段落替换为换行 
		content = content.replaceAll("<p .*?>", ""); 
		// <br><br/>替换为换行 
		content = content.replaceAll("<br\\s*/?>", ""); 
		// 去掉其它的<>之间的东西 
		content = content.replaceAll("\\<.*?>", ""); 
		
		return content; 
	}
	//导出当前机构所有注册但是未报名的人的基本信息
	public String exportApplyInfo(){
		// 获取当前机构;
		String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String sql ="SELECT A.OPRID,B.TZ_REALNAME, B.TZ_MOBILE, B.TZ_EMAIL,	A.TZ_LEN_PROID,A.TZ_GENDER, DATE_FORMAT(A.ROW_ADDED_DTTM,'%Y-%m-%d %h:%i:%s')ROW_ADDED_DTTM FROM 	PS_TZ_REG_USER_T A , PS_TZ_AQ_YHXX_TBL B WHERE A.OPRID = B.OPRID AND B.TZ_JG_ID = ? ORDER BY A.ROW_ADDED_DTTM DESC ";
//		String countSql = "SELECT COUNT(1) FROM 	PS_TZ_REG_USER_T A JOIN  PS_TZ_AQ_YHXX_TBL B ON (A.OPRID = B.OPRID AND B.TZ_JG_ID = ?) LEFT JOIN PS_TZ_FORM_WRK_T C ON(B.OPRID=C.OPRID) WHERE C.TZ_BATCH_ID IS NULL";
//		int total = SqlQuery.queryForObject(countSql, new Object[] { strJgid }, "int");
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
//		if(total>0){
			String downloadPath = getSysHardCodeVal.getDownloadPath();
			String expDirPath = downloadPath + "/" + strJgid + "/" + getDateNow() + "/" + "ApplyInfoExpExcel";
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
			// 表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			dataCellKeys.add(new String[]{ "NAME", "姓名" });
			dataCellKeys.add(new String[]{ "GENDER", "性别" });
			dataCellKeys.add(new String[]{ "PHONE", "手机" });
			dataCellKeys.add(new String[]{ "EMAIL", "邮箱" });
			dataCellKeys.add(new String[]{ "PROID", "常驻地" });
			dataCellKeys.add(new String[]{ "TIME", "注册时间" });
			List<Map<String, Object>> listData = SqlQuery.queryForList(sql, new Object[] {strJgid});
			for (Map<String, Object> mapData : listData) {
				Map<String, Object> mapDataex = new HashMap<String, Object>();
				String oprid = mapData.get("OPRID")==null?"":String.valueOf(mapData.get("OPRID")) ;
				String name=mapData.get("TZ_REALNAME")==null?"":String.valueOf(mapData.get("TZ_REALNAME")) ;
				String mobile=mapData.get("TZ_MOBILE")==null?"":String.valueOf(mapData.get("TZ_MOBILE")) ;
				String email=mapData.get("TZ_EMAIL")==null?"":String.valueOf(mapData.get("TZ_EMAIL")) ;
				String priod=mapData.get("TZ_LEN_PROID")==null?"":String.valueOf(mapData.get("TZ_LEN_PROID")) ;
				String gender = mapData.get("TZ_GENDER")==null?"":String.valueOf(mapData.get("TZ_GENDER")) ;
				String time = mapData.get("ROW_ADDED_DTTM")==null?"":String.valueOf(mapData.get("ROW_ADDED_DTTM")) ;
				String sex="";
				if("M".equals(gender)){
					sex="男";
				}else if("F".equals(gender)){
					sex="女";
				}
			String batchId = SqlQuery.queryForObject(
					"SELECT TZ_BATCH_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID=? ORDER BY ROW_ADDED_DTTM DESC LIMIT 0,1",
					new Object[] {oprid}, "String");
				if ("".equals(batchId)||batchId==null){
					mapDataex.put("NAME", name);
					mapDataex.put("GENDER", sex);
					mapDataex.put("PHONE", mobile);
					mapDataex.put("EMAIL",email);
					mapDataex.put("PROID", priod);
					mapDataex.put("TIME", time);
					dataList.add(mapDataex);
				}
			}
			/* 将文件上传之前，先重命名该文件 */
			Date dt = new Date();
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String sDttm = datetimeFormate.format(dt);
			String strUseFileName = "APPLYINFO_"+sDttm + "_" + strJgid + "." + "xlsx"; 
			
			ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
			boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
			if (rst) {
				String urlExcel = request.getContextPath() + excelHandle.getExportExcelPath();
				return urlExcel;
			}
//		}
		
		return "";
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
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String audID= "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return audID;
		}
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String strType = jacksonUtil.getString("type");
				String sql = jacksonUtil.getString("sql");
				boolean selyjType = false;
				boolean seldxType = false;
				
				if("SELYJ".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",orgId,"给搜索结果发送邮件", "SQYH");
					selyjType = true;
				}
				
				if("SELDX".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",orgId,"给搜索结果发送短信", "SQYH");
					seldxType = true;
				}
				
				
				if(selyjType||seldxType){
					//搜索结果发送
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					if("default".equals(sql)){
						sql = "SELECT ifnull(OPRID,'') OPRID FROM PS_TZ_REG_USE2_V WHERE TZ_JG_ID LIKE?";
						String sqlStr = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE,TZ_APP_INS_ID,";
						sql = sql.replace("SELECT ",sqlStr );
						String jgStr = "%"+orgId+"%";
						list = jdbcTemplate.queryForList(sql,new Object[]{jgStr});
					}else{
						String sqlStr = "SELECT TZ_REALNAME,TZ_EMAIL,TZ_MOBILE,TZ_APP_INS_ID,";
						sql = sql.replace("SELECT ",sqlStr );
						list = jdbcTemplate.queryForList(sql);
					}
					int audCyId = getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID",list.size(),0)-list.size();
					for(int num_1=0;num_1<list.size();num_1++){
						Map<String, Object> map = list.get(num_1);
						String appInsId=String.valueOf(map.get("TZ_APP_INS_ID"));
						String name = (String)map.get("TZ_REALNAME");
			            String email = (String)map.get("TZ_EMAIL");
			            String mobile=(String)map.get("TZ_MOBILE");
			            if(oprid != null && !"".equals(oprid)){
			                createTaskServiceImpl.addAudCy2(audID,name, "", mobile, mobile, email, email, "", oprid, "", "", appInsId,audCyId);
			                audCyId++;
			            }
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return audID;
	}
}
