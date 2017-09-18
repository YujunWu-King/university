package com.tranzvision.gd.TZAudMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAudMgBundle.dao.PsTzAudDefnTMapper;
import com.tranzvision.gd.TZAudMgBundle.dao.PsTzAudListTMapper;
import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudDefnT;
import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudListT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqComzcTblMapper;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 功能说明：听众管理相关类
 * 
 * @author 顾贤达 2017-1-19
 * 
 */
@Service("com.tranzvision.gd.TZAudMgBundle.service.impl.AudNewImpl")
public class AudNewImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzAudDefnTMapper psTzAudDefnTMapper;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	/* 查询听众管理列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_AUD_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_REALNAME", "TZ_DXZT", "TZ_MOBILE", "TZ_EMAIL", "TZ_LYDX_ID", "TZ_AUD_ID" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("audName", rowList[0]);
					mapList.put("audDxzt", rowList[1]);
					mapList.put("audMobile", rowList[2]);
					mapList.put("audMail", rowList[3]);
					mapList.put("dxID", rowList[4]);
					mapList.put("audID", rowList[5]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/* 获取听众详细信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String strAudID = jacksonUtil.getString("audId");
			String strAudName = jacksonUtil.getString("audName");
			String strAudStat = jacksonUtil.getString("audStat");
			String strAudType = jacksonUtil.getString("audType");
			String strAudTips = jacksonUtil.getString("audMS");
			String strAudSql = jacksonUtil.getString("audSQL");
			String strAudLY = jacksonUtil.getString("audLY");

			if (strAudID != null && !"".equals(strAudID)) {

				PsTzAudDefnT psTzAudDefnT = new PsTzAudDefnT();

				psTzAudDefnT.setTzAudId(strAudID);
				psTzAudDefnT.setTzAudNam(strAudName);
				psTzAudDefnT.setTzAudStat(strAudStat);
				psTzAudDefnT.setTzAudType(strAudType);
				psTzAudDefnT.setTzAudMs(strAudTips);
				psTzAudDefnT.setTzAudSql(strAudSql);
				psTzAudDefnT.setTzLxfsLy(strAudLY);

				returnJsonMap.put("audID", strAudID);
				returnJsonMap.put("audName", psTzAudDefnT.getTzAudNam());
				returnJsonMap.put("audStat", psTzAudDefnT.getTzAudStat());
				returnJsonMap.put("audType", psTzAudDefnT.getTzAudType());
				returnJsonMap.put("audMS", psTzAudDefnT.getTzAudMs());
				returnJsonMap.put("audSQL", psTzAudDefnT.getTzAudSql());
				returnJsonMap.put("audLY", psTzAudDefnT.getTzLxfsLy());

			} else {
				errMsg[0] = "1";
				errMsg[1] = "无法获取页面信息";
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		System.out.println("strtzQuery===" + strRet);
		return strRet;
	}

	/* 删除组件注册信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 组件ID;

				// String sComID = jacksonUtil.getString("comID");
				String strDxID = jacksonUtil.getString("dxID");
				String strAudID = jacksonUtil.getString("audID");

				String comPageSql = "DELETE FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=? and TZ_LYDX_ID=?";
				jdbcTemplate.update(comPageSql, new Object[] { strAudID, strDxID });

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	/* 新增 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 表单内容;
			String strForm = actData[0];
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			// 组件编号;
			String strJgID = jacksonUtil.getString("audJG");
			int strAudID = getSeqNum.getSeqNum("PS_TZ_AUD_DEFN_T", "TZ_AUD_ID");
			String strAudId = String.valueOf(strAudID);
			String strAudName = jacksonUtil.getString("audName");
			String strAudStat = jacksonUtil.getString("audStat");
			String strAudType = jacksonUtil.getString("audType");
			String strTips = jacksonUtil.getString("audMS");
			String strSql = jacksonUtil.getString("audSQL");

			String strLY = jacksonUtil.getString("audLY");
			System.out.println("ADD===LY" + strLY);

			// 查找当前组件下是否已经存在该页面;
			String isExistSql = "SELECT count(1) FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=? ";

			int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { strAudID }, "Integer");

			if (count == 0) {

				// 引用编号存在则必须唯一
				int refCodeExist = 0;
				/*
				 * if (refCode != null && !"".equals(refCode.trim())) { String
				 * refCodeUnqSQL =
				 * "SELECT count(1) FROM PS_TZ_AQ_PAGZC_TBL WHERE (TZ_COM_ID<>? OR TZ_PAGE_ID<>?) AND TZ_PAGE_REFCODE=?"
				 * ;
				 * 
				 * refCodeExist = jdbcTemplate.queryForObject(refCodeUnqSQL, new
				 * Object[] { strComID, strPageID, refCode }, "Integer");
				 * 
				 * }
				 */

				if (refCodeExist > 0) {
					errMsg[0] = "1";
					errMsg[1] = "ID出现重复!";
				} else {
					// 默认首页;
					/*
					 * if ("Y".equals(isDefault)) { // 默认首页只能有一个; String
					 * updateDefalutNoSQL =
					 * "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?"
					 * ; jdbcTemplate.update(updateDefalutNoSQL, new Object[] {
					 * strComID }); }
					 */
					PsTzAudDefnT psTzAudDefnT = new PsTzAudDefnT();
					psTzAudDefnT.setTzAudId(strAudId);
					psTzAudDefnT.setTzJgId(strJgID);
					// psTzAudDefnT.setTzAudId(strJgID);

					psTzAudDefnT.setTzAudNam(strAudName);
					psTzAudDefnT.setTzAudStat(strAudStat);
					psTzAudDefnT.setTzAudType(strAudType);
					psTzAudDefnT.setTzAudMs(strTips);
					psTzAudDefnT.setTzAudSql(strSql);
					psTzAudDefnT.setTzLxfsLy(strLY);

					String updateOperid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzAudDefnT.setRowAddedDttm(new Date());
					psTzAudDefnT.setRowAddedOprid(updateOperid);
					psTzAudDefnT.setRowLastmantDttm(new Date());
					psTzAudDefnT.setRowLastmantOprid(updateOperid);

					strRet = String.valueOf(strAudID);

					int i = psTzAudDefnTMapper.insert(psTzAudDefnT);
					if (i <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "保存失败";
					}

				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "请修改ID。";
				return strRet;
			}

		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			return strRet;
		}

		return strRet;

	}

	/* 修改 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {

				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				if ("FORM".equals(strFlag)) {

					String strJgID = (String) infoData.get("audJG");

					String strAudId = (String) infoData.get("audID");

					String strAudName = (String) infoData.get("audName");

					String strAudStat = (String) infoData.get("audStat");

					String strAudType = (String) infoData.get("audType");

					String strTips = (String) infoData.get("audMS");

					String strSql = (String) infoData.get("audSQL");

					String strLY = (String) infoData.get("audLY");

					// 是否已经存在;
					String comExistSql = "SELECT 'Y' FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=? ";
					String isExist = "";
					isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strAudId }, "String");

					if (!"Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "ID为：" + strAudId + "的信息不存在。";
						return strRet;
					}

					PsTzAudDefnT psTzAudDefnT = new PsTzAudDefnT();
					psTzAudDefnT.setTzAudId(strAudId);
					psTzAudDefnT.setTzJgId(strJgID);
					psTzAudDefnT.setTzAudNam(strAudName);

					psTzAudDefnT.setTzAudStat(strAudStat);
					psTzAudDefnT.setTzAudType(strAudType);
					psTzAudDefnT.setTzAudMs(strTips);
					psTzAudDefnT.setTzAudSql(strSql);
					psTzAudDefnT.setTzLxfsLy(strLY);

					// PsTzAudListT PsTzAudListT=new PsTzAudListT();
					// PsTzAudListT.setTzDxzt(strDxID);

					String updateOperid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					psTzAudDefnT.setRowAddedDttm(new Date());
					psTzAudDefnT.setRowAddedOprid(updateOperid);
					psTzAudDefnT.setRowLastmantDttm(new Date());
					psTzAudDefnT.setRowLastmantOprid(updateOperid);

					int i = psTzAudDefnTMapper.updateByPrimaryKeySelective(psTzAudDefnT);
					if (i <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}

					/*
					 * int j = psTzAudListTMapper.updateByPrimaryKeySelective(
					 * PsTzAudListT); if(j <= 0){ errMsg[0] = "1"; errMsg[1] =
					 * "更新失败"; }
					 */
				}

				if ("GRID".equals(strFlag)) {
					strRet = this.tzUpdateGridInfo(infoData, errMsg);
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}

	// grid 保存
	private String tzUpdateGridInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			// 组件编号;
			String strAudID = (String) infoData.get("audID");

			System.out.println("ID:" + strAudID);

			// 组件名称;
			String strDxID = (String) infoData.get("dxID");
			String straudDxzt = (String) infoData.get("audDxzt");

			// 序号;
			// String numOrderStr = String.valueOf(infoData.get("orderNum"));
			// short numOrder = Short.parseShort(numOrderStr);

			// 默认首页;
			/*
			 * boolean isDefaultBoolean = (boolean) infoData.get("isDefault");
			 * String isDefault = "N"; if (isDefaultBoolean) { isDefault = "Y";
			 * } else { isDefault = "N"; }
			 * 
			 * if ("Y".equals(isDefault)) { // 默认首页只能有一个; String
			 * updateDefalutNoSQL =
			 * "UPDATE PS_TZ_AQ_PAGZC_TBL SET TZ_PAGE_MRSY='N' WHERE TZ_COM_ID=?"
			 * ;
			 * 
			 * jdbcTemplate.update(updateDefalutNoSQL, new Object[] { strComID
			 * }); }
			 */
			// 页面注册信息表;

			PsTzAudListT psTzAudListT = new PsTzAudListT();
			psTzAudListT.setTzDxzt(straudDxzt);

			// psTzAqPagzcTblMapper.updateByPrimaryKeySelective(psTzAqPagzcTbl);

			String comPageSql = "UPDATE PS_TZ_AUD_LIST_T SET TZ_DXZT=? WHERE TZ_AUD_ID=? and TZ_LYDX_ID=?";

			jdbcTemplate.update(comPageSql, new Object[] { straudDxzt, strAudID, strDxID });

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 执行SQL */
	@Override
	// public String tzOther(String[] oprType,String[] actData, String[] errMsg)
	// {
	public String tzOther(String oprType, String actData, String[] errorMsg) {
		// 返回值;
		String strRet = "{}";
		// System.out.println(actData);
		// 若参数为空，直接返回;
		/*
		 * if (actData == null || actData.length == 0) { return strRet; }
		 */
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// 表单内容;
			String strForm = actData;
			// 将字符串转换成json;
			jacksonUtil.json2Map(strForm);
			// 组件ID;
			String SaudSQL = jacksonUtil.getString("audSQL");
			String SaudID = jacksonUtil.getString("audID");

			System.out.println(SaudID);
			String DeleteSql = "DELETE FROM PS_TZ_AUD_LIST_T WHERE TZ_AUD_ID=? and TZ_DXZT='A'";
			int ensure = jdbcTemplate.update(DeleteSql, new Object[] { SaudID });
			System.out.println("ensure" + ensure);

			// 执行;
			String deleteSQL = SaudSQL;



			jdbcTemplate.execute(deleteSQL);
			// isExist =jdbcTemplate.queryForObject(SaudSQL, "String");

			List<Map<String, Object>> resultlist = null;
			resultlist = jdbcTemplate.queryForList(SaudSQL);
			System.out.println("resultlist:" + resultlist);

			/*
			 * List<Map<String, Object>> list;
			 * 
			 * String[] resultFldArray = null; int resultFldNum =
			 * resultFldArray.length;
			 */
			for (int K = 0; K < resultlist.size(); K++) {
				Map<String, Object> resultMap = resultlist.get(K);
				System.out.println("resultMap.value:" + StringUtils.strip(resultMap.values().toString(), "[]"));

				String InsertID = StringUtils.strip(resultMap.values().toString(), "[]");

				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String Searchsql = "select TZ_RYLX from PS_TZ_AQ_YHXX_TBL where OPRID=?";
				String YHLX = jdbcTemplate.queryForObject(Searchsql, String.class, new Object[] { InsertID });

				String comPageSql = "insert into PS_TZ_AUD_LIST_T values(?,?,?,'A',?)";
				jdbcTemplate.update(comPageSql, new Object[] { SaudID, YHLX, InsertID, InsertID });

				/*
				 * String[] rowList = new String[resultFldNum]; int j = 0; for
				 * (Object value : resultMap.values()) {
				 * 
				 * rowList[j] = (String) value; j++; } // list.add(rowList);
				 * 
				 */
			}

			// System.out.println(deleteSQL);
			// }
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();

			return strRet;
		}

		return strRet;
	}

}
