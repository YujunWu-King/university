package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbGzgxTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbLjgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbLjxsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcMbYbgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbGzgxT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbLjgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbLjxsTKey;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbYbgzT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author caoy
 * @version 创建时间：2016年8月2日 上午10:44:47 类说明   新建问卷模板
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.SurveyMNGClsServiceImpl")
public class SurveyMNGClsServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzDcDyTMapper psTzDcDyTMapper;

	@Autowired
	private TempEditorEngineImpl tempEditorEngineImpl;

	@Autowired
	private PsTzDcMbLjgzTMapper psTzDcMbLjgzTMapper;

	@Autowired
	private PsTzDcMbGzgxTMapper psTzDcMbGzgxTMapper;

	@Autowired
	private PsTzDcMbLjxsTMapper psTzDcMbLjxsTMapper;

	@Autowired
	private PsTzDcMbYbgzTMapper psTzDcMbYbgzTMapper;

	/*
	 * 根据传入参数判断参数是否合法，若不合法，直接返回空; 若合法，查询满足条件的数据，并返回json报文。
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		System.out.println("==SurveyMNGClsServiceImpl类==tzQueryList执行");
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		int total = 0;
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			/* 总记录数 */
			String sql = "SELECT COUNT(1) FROM PS_TZ_DC_DY_T WHERE TZ_JG_ID = ? ";
			total = jdbcTemplate.queryForObject(sql, new Object[] { orgId }, "Integer");
			
			List<Map<String, Object>> list = null;
			if (numLimit == 0) {
				sql = "SELECT TZ_APP_TPL_ID,TZ_APP_TPL_MC,TZ_EFFEXP_ZT from PS_TZ_DC_DY_T where TZ_JG_ID=? ORDER BY ROW_LASTMANT_DTTM DESC";
				list = jdbcTemplate.queryForList(sql, new Object[] { orgId });
			} else {
				sql = "SELECT TZ_APP_TPL_ID,TZ_APP_TPL_MC,TZ_EFFEXP_ZT from PS_TZ_DC_DY_T where TZ_JG_ID=? ORDER BY ROW_LASTMANT_DTTM DESC  limit ?,?";
				list = jdbcTemplate.queryForList(sql, new Object[] { orgId, numStart, numLimit });
			}
			String TZ_APP_TPL_ID = "";
			String TZ_APP_TPL_MC = "";
			String TZ_EFFEXP_ZT = "";
			String strTplStateDesc = "";

			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					TZ_APP_TPL_ID = (String) list.get(i).get("TZ_APP_TPL_ID");
					TZ_APP_TPL_MC = (String) list.get(i).get("TZ_APP_TPL_MC");
					TZ_EFFEXP_ZT = (String) list.get(i).get("TZ_EFFEXP_ZT");
					if (TZ_EFFEXP_ZT != null && TZ_EFFEXP_ZT.equals("Y")) {
						strTplStateDesc = "有效";
					} else {
						strTplStateDesc = "无效";
					}

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("tplid", TZ_APP_TPL_ID);
					mapList.put("tplname", TZ_APP_TPL_MC);
					mapList.put("activestate", TZ_EFFEXP_ZT);
					mapList.put("activestatedesc", strTplStateDesc);
					listData.add(mapList);
				}
			}
			mapRet.replace("total", total);
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 复制模板
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		System.out.println("==SurveyMNGClsServiceImpl类==tzAdd执行==copy?");
		String strRet = "{}";

		if (actData.length == 0) {
			errMsg[0] = "1";
			errMsg[1] = "参数不能为空！";
			return strRet;
		}
		try {
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String userID = tzLoginServiceImpl.getLoginedManagerOprid(request);

			JacksonUtil jacksonUtil = new JacksonUtil();
			String strForm = "";
			String strTplId = "";
			String strTplName = "";

			strForm = actData[0];
			jacksonUtil.json2Map(strForm);
			strTplId = jacksonUtil.getString("id");
			strTplName = jacksonUtil.getString("name");
			String sql = "";
			if (StringUtils.isBlank(strTplName)) {
				errMsg[0] = "1";
				errMsg[1] = "未提供新的模板名称！";
				return strRet;
			} else {
				sql = "select 'Y' from PS_TZ_DC_DY_T  WHERE TZ_JG_ID= ? AND TZ_APP_TPL_MC= ?";
				String recExists = jdbcTemplate.queryForObject(sql, new Object[] { orgId, strTplName }, "String");
				if ("Y".equals(recExists)) {
					errMsg[0] = "1";
					errMsg[1] = "模板名称重复！";
					return strRet;
				}
				String strNewTplid = "" + getSeqNum.getSeqNum("TZ_DC_DY_T", "TZ_APP_TPL_ID");
				sql = "select TZ_APP_TPL_LAN,TZ_APP_TPL_LX,TZ_EFFEXP_ZT,TZ_APPTPL_JSON_STR,TZ_DC_JTNR,TZ_DC_JWNR from  PS_TZ_DC_DY_T where TZ_APP_TPL_ID= ?";
				// 加载模板ID 模板名称 以及 机构名称
				String TZ_APP_TPL_LAN = "";
				String TZ_APP_TPL_LX = "";
				String TZ_EFFEXP_ZT = "";
				String TZ_APPTPL_JSON_STR = "";
				String TZ_DC_JTNR = "";
				String TZ_DC_JWNR = "";
				Map<String, Object> items = null;
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strTplId });
				if (map != null) {
					TZ_APP_TPL_LAN = map.get("TZ_APP_TPL_LAN") == null ? "" : String.valueOf(map.get("TZ_APP_TPL_LAN"));
					TZ_APP_TPL_LX = map.get("TZ_APP_TPL_LX") == null ? "" : String.valueOf(map.get("TZ_APP_TPL_LX"));
					TZ_EFFEXP_ZT = map.get("TZ_EFFEXP_ZT") == null ? "" : String.valueOf(map.get("TZ_EFFEXP_ZT"));
//					TZ_APPTPL_JSON_STR = map.get("TZ_APPTPL_JSON_STR") == null ? ""
//							: String.valueOf(map.get("TZ_APPTPL_JSON_STR"));
					TZ_DC_JTNR = map.get("TZ_DC_JTNR") == null ? "" : String.valueOf(map.get("TZ_DC_JTNR"));
					TZ_DC_JWNR = map.get("TZ_DC_JWNR") == null ? "" : String.valueOf(map.get("TZ_DC_JWNR"));
				}

				// 将JSON类型的字符串转换成json类型的对象
				if (map.get("TZ_APPTPL_JSON_STR")!=null) {
					Map<String,Object>itemFather=new HashMap<String,Object>();
					jacksonUtil.json2Map( map.get("TZ_APPTPL_JSON_STR").toString());
					itemFather=jacksonUtil.getMap();
					//System.out.println("itemFather:"+jacksonUtil.Map2json(itemFather));
					if(itemFather.get("items")!=null){
						items = (Map<String, Object>) itemFather.get("items");
					}
					else
						items=null;
				}

				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("tplId", strNewTplid);
				mapRet.put("tplName", strTplName);
				mapRet.put("tplLx", TZ_APP_TPL_LX);
				mapRet.put("header", TZ_DC_JTNR);
				mapRet.put("footer", TZ_DC_JWNR);
				mapRet.put("state", TZ_EFFEXP_ZT);
				mapRet.put("depId", orgId);
				mapRet.put("depName", orgId);
				if(items!=null)
					mapRet.put("items", items);
				else
					mapRet.put("items", new HashMap<String,Object>());
				mapRet.put("tplType", TZ_APP_TPL_LX);

				TZ_APPTPL_JSON_STR = jacksonUtil.Map2json(mapRet);

				if (!StringUtils.isBlank(strNewTplid)) {
					PsTzDcDyTWithBLOBs newRec = new PsTzDcDyTWithBLOBs();
					newRec.setTzAppTplId(strNewTplid);
					newRec.setTzAppTplMc(strTplName);
					newRec.setTzJgId(orgId);
					newRec.setTzAppTplLan(TZ_APP_TPL_LAN);
					newRec.setTzAppTplLx(TZ_APP_TPL_LX);
					newRec.setTzEffexpZt(TZ_EFFEXP_ZT);
					newRec.setTzApptplJsonStr(TZ_APPTPL_JSON_STR);
					newRec.setTzDcJtnr(TZ_DC_JTNR);
					newRec.setTzDcJwnr(TZ_DC_JWNR);
					newRec.setRowAddedDttm(new java.util.Date());
					newRec.setRowAddedOprid(userID);
					newRec.setRowLastmantDttm(new java.util.Date());
					newRec.setRowLastmantOprid(userID);

					if (psTzDcDyTMapper.insert(newRec) > 0) {
						strRet = "{\"id\":\"" + strNewTplid + "\"}";
					} else {
						errMsg[0] = "1";
						errMsg[1] = "复制问卷调查失败！";
						return strRet;
					}

					/* 调用保存操作 */
					Map<String, Object> mapData = jacksonUtil.parseJson2Map(TZ_APPTPL_JSON_STR);
					String strReturn = tempEditorEngineImpl.saveTpl(strNewTplid, mapData, userID, errMsg);

					/* 复制模板逻辑-在线调查模板逻辑规则定义表 */
					sql = "select TZ_DC_LJTJ_ID,TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH from PS_TZ_DC_MB_LJGZ_T where TZ_APP_TPL_ID= ?";
					List<Map<String,Object>> resultlist = jdbcTemplate.queryForList(sql, new Object[] { strTplId });
					List<Map<String,Object>> childresultlist = null;
					String TZ_DC_LJTJ_ID = "";
					String TZ_XXX_BH = "";
					String TZ_LJTJ_XH = "";
					String TZ_LJ_LX = "";
					String TZ_PAGE_NO = "";
					String logicalId = "";
					Map<String, Object> result = null;
					Map<String, Object> childresult = null;
					PsTzDcMbLjgzT psTzDcMbLjgzT = null;
					String TZ_XXXKXZ_MC = "";
					String TZ_IS_SELECTED = "";
					String TZ_XXXZWT_MC = "";
					String sql2 = "";
					String str = "";

					for (Object obj : resultlist) {
						result = (Map<String, Object>) obj;

						TZ_DC_LJTJ_ID = result.get("TZ_DC_LJTJ_ID") == null ? ""
								: String.valueOf(result.get("TZ_DC_LJTJ_ID"));
						TZ_XXX_BH = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
						TZ_LJTJ_XH = result.get("TZ_LJTJ_XH") == null ? "" : String.valueOf(result.get("TZ_LJTJ_XH"));
						TZ_LJ_LX = result.get("TZ_LJ_LX") == null ? "" : String.valueOf(result.get("TZ_LJ_LX"));
						TZ_PAGE_NO = result.get("TZ_PAGE_NO") == null ? "" : String.valueOf(result.get("TZ_LJ_LX"));

						if (!StringUtils.isBlank(TZ_DC_LJTJ_ID)) {
							logicalId = "L" + getSeqNum.getSeqNum("TZ_DC_MB_LJGZ_T", "TZ_DC_LJTJ_ID");
							if (psTzDcMbLjgzTMapper.selectByPrimaryKey(logicalId) == null) {
								psTzDcMbLjgzT = new PsTzDcMbLjgzT();
								psTzDcMbLjgzT.setTzDcLjtjId(logicalId);
								psTzDcMbLjgzT.setTzXxxBh(TZ_XXX_BH);
								psTzDcMbLjgzT.setTzAppTplId(strNewTplid);
								psTzDcMbLjgzT.setTzLjLx(TZ_LJ_LX);
								psTzDcMbLjgzT.setTzPageNo(new Integer(TZ_PAGE_NO));
								psTzDcMbLjgzT.setTzLjtjXh(new Integer(TZ_LJTJ_XH));
								psTzDcMbLjgzTMapper.insert(psTzDcMbLjgzT);
								System.out.println("====psTzDcMbLjgzT==insert=");
							}

							/* 复制模板逻辑-在线调查模板一般题型逻辑规则关系表 */
							sql = "select TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_MB_YBGZ_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
							childresultlist = jdbcTemplate.queryForList(sql, new Object[] { strTplId, TZ_DC_LJTJ_ID });
							for (Object obj2 : childresultlist) {
								childresult = (Map<String, Object>) obj2;
								TZ_XXX_BH = childresult.get("TZ_XXX_BH") == null ? ""
										: String.valueOf(childresult.get("TZ_XXX_BH"));
								TZ_XXXKXZ_MC = childresult.get("TZ_XXXKXZ_MC") == null ? ""
										: String.valueOf(childresult.get("TZ_XXXKXZ_MC"));
								TZ_IS_SELECTED = childresult.get("TZ_IS_SELECTED") == null ? ""
										: String.valueOf(childresult.get("TZ_IS_SELECTED"));

								PsTzDcMbYbgzT psTzDcMbYbgzT = new PsTzDcMbYbgzT();
								psTzDcMbYbgzT.setTzAppTplId(strNewTplid);
								psTzDcMbYbgzT.setTzDcLjtjId(logicalId);
								psTzDcMbYbgzT.setTzXxxBh(TZ_XXX_BH);
								psTzDcMbYbgzT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
								if (psTzDcMbYbgzTMapper.selectByPrimaryKey(psTzDcMbYbgzT) == null) {
									psTzDcMbYbgzT.setTzIsSelected(TZ_IS_SELECTED);
									psTzDcMbYbgzTMapper.insert(psTzDcMbYbgzT);
									System.out.println("====psTzDcMbYbgzT==insert=");
								}
							}

							/* 复制模板逻辑-在线调查模板表格题逻辑规则关系表 */
							sql = "select TZ_XXX_BH,TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_MB_GZGX_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
							childresultlist = jdbcTemplate.queryForList(sql, new Object[] { strTplId, TZ_DC_LJTJ_ID });
							for (Object obj2 : childresultlist) {
								childresult = (Map<String, Object>) obj2;
								TZ_XXX_BH = childresult.get("TZ_XXX_BH") == null ? ""
										: String.valueOf(childresult.get("TZ_XXX_BH"));
								TZ_XXXKXZ_MC = childresult.get("TZ_XXXKXZ_MC") == null ? ""
										: String.valueOf(childresult.get("TZ_XXXKXZ_MC"));
								TZ_XXXZWT_MC = childresult.get("TZ_XXXZWT_MC") == null ? ""
										: String.valueOf(childresult.get("TZ_XXXZWT_MC"));
								TZ_IS_SELECTED = childresult.get("TZ_IS_SELECTED") == null ? ""
										: String.valueOf(childresult.get("TZ_IS_SELECTED"));
								PsTzDcMbGzgxT psTzDcMbGzgxT = new PsTzDcMbGzgxT();
								psTzDcMbGzgxT.setTzAppTplId(strNewTplid);
								psTzDcMbGzgxT.setTzDcLjtjId(logicalId);
								psTzDcMbGzgxT.setTzXxxBh(TZ_XXX_BH);
								psTzDcMbGzgxT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
								psTzDcMbGzgxT.setTzXxxzwtMc(TZ_XXXZWT_MC);
								if (psTzDcMbGzgxTMapper.selectByPrimaryKey(psTzDcMbGzgxT) == null) {
									psTzDcMbGzgxT.setTzIsSelected(TZ_IS_SELECTED);
									psTzDcMbGzgxTMapper.insert(psTzDcMbGzgxT);
									System.out.println("====psTzDcMbGzgxT==insert=");
								}
							}

							/* 复制逻辑-在线调查模板关联显示逻辑关系表 */

							sql = "select TZ_XXX_BH from PS_TZ_DC_MB_LJXS_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
							childresultlist = jdbcTemplate.queryForList(sql, new Object[] { strTplId, TZ_DC_LJTJ_ID });
							for (Object obj2 : childresultlist) {
								childresult = (Map<String, Object>) obj2;
								TZ_XXX_BH = childresult.get("TZ_XXX_BH") == null ? ""
										: String.valueOf(childresult.get("TZ_XXX_BH"));
								PsTzDcMbLjxsTKey psTzDcMbLjxsTKey = new PsTzDcMbLjxsTKey();
								psTzDcMbLjxsTKey.setTzAppTplId(strNewTplid);
								psTzDcMbLjxsTKey.setTzDcLjtjId(logicalId);
								psTzDcMbLjxsTKey.setTzXxxBh(TZ_XXX_BH);

								sql2 = "select 'Y' from PS_TZ_DC_MB_LJXS_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=? and TZ_XXX_BH=?";
								str = jdbcTemplate.queryForObject(sql2,
										new Object[] { strNewTplid, logicalId, TZ_XXX_BH }, "String");
								if (!"Y".equals(str)) {
									psTzDcMbLjxsTMapper.insert(psTzDcMbLjxsTKey);
									System.out.println("====psTzDcMbLjxsTKey==insert=");
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
