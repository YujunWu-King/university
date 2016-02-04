/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlcTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsBmlchfTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsDjzlTMapper;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsHfdyTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTWithBLOBs;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlchfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsDjzlT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsHfdyT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 班级管理-列表信息 原PS：TZ_GD_BJGL_CLS:TZ_GD_BJ_QTXX
 * 
 * @author SHIHUA
 * @since 2016-02-02
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassListServiceImpl")
public class TzClassListServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzClsBmlcTMapper psTzClsBmlcTMapper;

	@Autowired
	private PsTzClsBmlchfTMapper psTzClsBmlchfTMapper;

	@Autowired
	private PsTzClsDjzlTMapper psTzClsDjzlTMapper;

	@Autowired
	private PsTzClsHfdyTMapper psTzClsHfdyTMapper;

	/**
	 * 专业方向列表查询
	 * 
	 * @param comParams
	 * @param errorMsg
	 * @return String
	 */
	public String queryFxList(String comParams, String[] errorMsg) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			String str_bj_id = jacksonUtil.getString("bj_id");

			// int page = Integer.parseInt(request.getParameter("page"));
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if (null != str_bj_id && !"".equals(str_bj_id)) {
				if (limit > 0) {

					String sql = "select count(*) from PS_TZ_CLS_MAJOR_T where TZ_CLASS_ID=?";
					int total = sqlQuery.queryForObject(sql, new Object[] { str_bj_id }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetMajorList");

						List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
								new Object[] { str_bj_id, start, limit });

						for (Map<String, Object> mapData : listData) {

							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("bj_id", str_bj_id);

							mapJson.put("fx_xh", mapData.get("TZ_SORT_NUM") == null ? "0"
									: String.valueOf(mapData.get("TZ_SORT_NUM")));

							mapJson.put("fx_id", mapData.get("TZ_MAJOR_ID") == null ? ""
									: String.valueOf(mapData.get("TZ_MAJOR_ID")));

							mapJson.put("fx_name", mapData.get("TZ_MAJOR_NAME") == null ? ""
									: String.valueOf(mapData.get("TZ_MAJOR_NAME")));

							listJson.add(mapJson);

						}

					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("total", total);
					mapRet.put("root", listJson);

					strRet = jacksonUtil.Map2json(mapRet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[0] = "参数不正确！";
		}

		return strRet;
	}

	/**
	 * 批次管理列表查询
	 * 
	 * @param comParams
	 * @param errorMsg
	 * @return String
	 */
	public String queryPcList(String comParams, String[] errorMsg) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			String str_bj_id = jacksonUtil.getString("bj_id");

			// int page = Integer.parseInt(request.getParameter("page"));
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if (null != str_bj_id && !"".equals(str_bj_id)) {
				if (limit > 0) {

					String sql = "select count(*) from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=?";
					int total = sqlQuery.queryForObject(sql, new Object[] { str_bj_id }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetBatchList");

						List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
								new Object[] { str_bj_id, start, limit });

						String dateFormat = getSysHardCodeVal.getDateFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

						for (Map<String, Object> mapData : listData) {

							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("bj_id", str_bj_id);

							mapJson.put("pc_id", mapData.get("TZ_BATCH_ID") == null ? ""
									: String.valueOf(mapData.get("TZ_BATCH_ID")));

							mapJson.put("pc_name", mapData.get("TZ_BATCH_NAME") == null ? ""
									: String.valueOf(mapData.get("TZ_BATCH_NAME")));

							mapJson.put("pc_st_time", mapData.get("TZ_START_DT") == null ? ""
									: simpleDateFormat.format(mapData.get("TZ_START_DT")));

							mapJson.put("pc_sp_time", mapData.get("TZ_END_DT") == null ? ""
									: simpleDateFormat.format(mapData.get("TZ_END_DT")));

							mapJson.put("pc_stbm_time", mapData.get("TZ_APP_END_DT") == null ? ""
									: simpleDateFormat.format(mapData.get("TZ_APP_END_DT")));

							mapJson.put("pc_fb",
									mapData.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(mapData.get("TZ_ZHZ_DMS")));

							listJson.add(mapJson);

						}

					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("total", total);
					mapRet.put("root", listJson);

					strRet = jacksonUtil.Map2json(mapRet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[0] = "参数不正确！";
		}

		return strRet;
	}

	/**
	 * 管理人员列表查询
	 * 
	 * @param comParams
	 * @param errorMsg
	 * @return String
	 */
	public String queryGlList(String comParams, String[] errorMsg) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			String str_bj_id = jacksonUtil.getString("bj_id");

			// int page = Integer.parseInt(request.getParameter("page"));
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if (null != str_bj_id && !"".equals(str_bj_id)) {
				if (limit > 0) {

					String sql = "select count(*) from PS_TZ_CLS_ADMIN_T where TZ_CLASS_ID=?";
					int total = sqlQuery.queryForObject(sql, new Object[] { str_bj_id }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetAdminList");

						List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
								new Object[] { str_bj_id, start, limit });

						for (Map<String, Object> mapData : listData) {

							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("bj_id", str_bj_id);

							// ps中是用的OPRID，但OPRID用户时看不懂的，这里改为登录账号
							// mapJson.put("ry_id", mapData.get("OPRID") == null
							// ? "": String.valueOf(mapData.get("OPRID")));

							mapJson.put("ry_id",
									mapData.get("TZ_DLZH_ID") == null ? "" : String.valueOf(mapData.get("TZ_DLZH_ID")));

							mapJson.put("gl_name", mapData.get("TZ_REALNAME") == null ? ""
									: String.valueOf(mapData.get("TZ_REALNAME")));

							mapJson.put("gl_phone",
									mapData.get("TZ_MOBILE") == null ? "" : String.valueOf(mapData.get("TZ_MOBILE")));

							mapJson.put("gl_email",
									mapData.get("TZ_EMAIL") == null ? "" : String.valueOf(mapData.get("TZ_EMAIL")));

							listJson.add(mapJson);

						}

					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("total", total);
					mapRet.put("root", listJson);

					strRet = jacksonUtil.Map2json(mapRet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[0] = "参数不正确！";
		}

		return strRet;
	}

	/**
	 * 报名流程列表查询
	 * 
	 * @param comParams
	 * @param errorMsg
	 * @return String
	 */
	public String queryBmlcList(String comParams, String[] errorMsg) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_bmlc_id = jacksonUtil.getString("lc_id");
			if (str_bmlc_id == null) {
				str_bmlc_id = "";
			}

			// int page = Integer.parseInt(request.getParameter("page"));
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if (null != str_bj_id && !"".equals(str_bj_id) && limit > 0) {
				if ("".equals(str_bmlc_id)) {

					String sql = "select count(*) from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?";
					int total = sqlQuery.queryForObject(sql, new Object[] { str_bj_id }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetBmlcList");

						List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
								new Object[] { str_bj_id, start, limit });

						for (Map<String, Object> mapData : listData) {

							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("bj_id", str_bj_id);

							mapJson.put("bmlc_id", mapData.get("TZ_APPPRO_ID") == null ? ""
									: String.valueOf(mapData.get("TZ_APPPRO_ID")));

							mapJson.put("bmlc_xh", mapData.get("TZ_SORT_NUM") == null ? "0"
									: String.valueOf(mapData.get("TZ_SORT_NUM")));

							mapJson.put("bmlc_name", mapData.get("TZ_APPPRO_NAME") == null ? ""
									: String.valueOf(mapData.get("TZ_APPPRO_NAME")));

							mapJson.put("bmlc_desc", "");

							listJson.add(mapJson);

						}

					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("total", total);
					mapRet.put("root", listJson);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					// 参数str_bmlc_id不为空
					String sql = "select count(*) from PS_TZ_APPPRO_STP_T where TZ_APPPRO_TMP_ID=?";
					int total = sqlQuery.queryForObject(sql, new Object[] { str_bmlc_id }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetBmlcbzList");

						List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
								new Object[] { str_bmlc_id, start, limit });

						sql = "delete from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?";
						sqlQuery.update(sql, new Object[] { str_bj_id });

						for (Map<String, Object> mapData : listData) {

							String tzAppproId = mapData.get("TZ_APPPRO_ID") == null ? ""
									: String.valueOf(mapData.get("TZ_APPPRO_ID"));
							int tzSortNum = mapData.get("TZ_SORT_NUM") == null ? 0
									: Integer.parseInt(String.valueOf(mapData.get("TZ_SORT_NUM")));
							String tzAppproName = mapData.get("TZ_APPPRO_NAME") == null ? ""
									: String.valueOf(mapData.get("TZ_APPPRO_NAME"));

							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("bj_id", str_bj_id);

							mapJson.put("bmlc_id", tzAppproId);

							mapJson.put("bmlc_xh", String.valueOf(tzSortNum));

							mapJson.put("bmlc_name", tzAppproName);

							mapJson.put("bmlc_desc", "");

							listJson.add(mapJson);

							// 重新创建班级报名流程记录
							PsTzClsBmlcTWithBLOBs psTzClsBmlcTWithBLOBs = new PsTzClsBmlcTWithBLOBs();
							psTzClsBmlcTWithBLOBs.setTzClassId(str_bj_id);
							psTzClsBmlcTWithBLOBs.setTzAppproId(tzAppproId);
							psTzClsBmlcTWithBLOBs.setTzSortNum(tzSortNum);
							psTzClsBmlcTWithBLOBs.setTzAppproName(tzAppproName);
							psTzClsBmlcTWithBLOBs.setTzDefContent("");
							psTzClsBmlcTWithBLOBs.setTzIsPublic("");
							psTzClsBmlcTWithBLOBs.setTzTmpContent("");
							psTzClsBmlcTMapper.insert(psTzClsBmlcTWithBLOBs);

							// 更新保留流程步骤回复内容
							sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetBmlcbzhfList");
							List<Map<String, Object>> listBmlcbzhf = sqlQuery.queryForList(sql,
									new Object[] { str_bmlc_id, tzAppproId });

							for (Map<String, Object> mapBmlcbzhf : listBmlcbzhf) {

								String tzAppproHfBh = mapBmlcbzhf.get("TZ_APPPRO_HF_BH") == null ? ""
										: String.valueOf(mapBmlcbzhf.get("TZ_APPPRO_HF_BH"));
								String tzAppproColor = mapBmlcbzhf.get("TZ_APPPRO_COLOR") == null ? ""
										: String.valueOf(mapBmlcbzhf.get("TZ_APPPRO_COLOR"));
								String tzClsResult = mapBmlcbzhf.get("TZ_CLS_RESULT") == null ? ""
										: String.valueOf(mapBmlcbzhf.get("TZ_CLS_RESULT"));
								String tzAppproContent = mapBmlcbzhf.get("TZ_APPPRO_CONTENT") == null ? ""
										: String.valueOf(mapBmlcbzhf.get("TZ_APPPRO_CONTENT"));
								String tzWfbDefaltBz = mapBmlcbzhf.get("TZ_WFB_DEFALT_BZ") == null ? ""
										: String.valueOf(mapBmlcbzhf.get("TZ_WFB_DEFALT_BZ"));

								PsTzClsBmlchfT psTzClsBmlchfT = new PsTzClsBmlchfT();
								psTzClsBmlchfT.setTzClassId(str_bj_id);
								psTzClsBmlchfT.setTzAppproId(tzAppproId);
								psTzClsBmlchfT.setTzAppproHfBh(tzAppproHfBh);
								psTzClsBmlchfT.setTzAppproColor(tzAppproColor);
								psTzClsBmlchfT.setTzClsResult(tzClsResult);
								psTzClsBmlchfT.setTzAppproContent(tzAppproContent);
								psTzClsBmlchfT.setTzWfbDefaltBz(tzWfbDefaltBz);

								sql = "select 'Y' from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=? and TZ_APPPRO_HF_BH=?";
								String recExists = sqlQuery.queryForObject(sql,
										new Object[] { str_bj_id, tzAppproId, tzAppproHfBh }, "String");

								if ("Y".equals(recExists)) {
									psTzClsBmlchfTMapper.updateByPrimaryKeyWithBLOBs(psTzClsBmlchfT);
								} else {
									psTzClsBmlchfTMapper.insert(psTzClsBmlchfT);
								}

							}

						}

					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("total", total);
					mapRet.put("root", listJson);

					strRet = jacksonUtil.Map2json(mapRet);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[0] = "参数不正确！";
		}

		return strRet;
	}

	/**
	 * 递交资料列表查询
	 * 
	 * @param comParams
	 * @param errorMsg
	 * @return String
	 */
	public String queryZlList(String comParams, String[] errorMsg) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_djzl_id = jacksonUtil.getString("zl_id");
			if (str_djzl_id == null) {
				str_djzl_id = "";
			}

			// int page = Integer.parseInt(request.getParameter("page"));
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));

			if (null != str_bj_id && !"".equals(str_bj_id) && limit > 0) {
				if ("".equals(str_djzl_id)) {

					String sql = "select count(*) from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=?";
					int total = sqlQuery.queryForObject(sql, new Object[] { str_bj_id }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetDjzlList");

						List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
								new Object[] { str_bj_id, start, limit });

						for (Map<String, Object> mapData : listData) {

							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("bj_id", str_bj_id);

							mapJson.put("djzl_id", mapData.get("TZ_SBMINF_ID") == null ? ""
									: String.valueOf(mapData.get("TZ_SBMINF_ID")));

							mapJson.put("djzl_xh", mapData.get("TZ_SORT_NUM") == null ? "0"
									: String.valueOf(mapData.get("TZ_SORT_NUM")));

							mapJson.put("djzl_name", mapData.get("TZ_CONT_INTRO") == null ? ""
									: String.valueOf(mapData.get("TZ_CONT_INTRO")));

							mapJson.put("djzl_bz",
									mapData.get("TZ_REMARK") == null ? "" : String.valueOf(mapData.get("TZ_REMARK")));

							listJson.add(mapJson);

						}

					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("total", total);
					mapRet.put("root", listJson);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					// 参数str_djzl_id不为空
					String sql = "select count(*) from PS_TZ_SBMINF_STP_T where TZ_SBMINF_TMP_ID=?";
					int total = sqlQuery.queryForObject(sql, new Object[] { str_djzl_id }, "int");

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetDjzlbzList");

						List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
								new Object[] { str_djzl_id, start, limit });

						sql = "delete from PS_TZ_CLS_DJZL_T where TZ_CLASS_ID=?";
						sqlQuery.update(sql, new Object[] { str_bj_id });

						for (Map<String, Object> mapData : listData) {

							String tzSbminfId = mapData.get("TZ_SBMINF_ID") == null ? ""
									: String.valueOf(mapData.get("TZ_SBMINF_ID"));
							int tzSortNum = mapData.get("TZ_SORT_NUM") == null ? 0
									: Integer.parseInt(String.valueOf(mapData.get("TZ_SORT_NUM")));
							String tzContIntro = mapData.get("TZ_CONT_INTRO") == null ? ""
									: String.valueOf(mapData.get("TZ_CONT_INTRO"));
							String tzRemark = mapData.get("TZ_REMARK") == null ? ""
									: String.valueOf(mapData.get("TZ_REMARK"));

							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("bj_id", str_bj_id);

							mapJson.put("djzl_id", tzSbminfId);

							mapJson.put("djzl_xh", String.valueOf(tzSortNum));

							mapJson.put("djzl_name", tzContIntro);

							mapJson.put("djzl_bz", tzRemark);

							listJson.add(mapJson);

							// 重新创建递交资料流程记录
							PsTzClsDjzlT psTzClsDjzlT = new PsTzClsDjzlT();
							psTzClsDjzlT.setTzClassId(str_bj_id);
							psTzClsDjzlT.setTzSbminfId(tzSbminfId);
							psTzClsDjzlT.setTzSortNum(tzSortNum);
							psTzClsDjzlT.setTzContIntro(tzContIntro);
							psTzClsDjzlT.setTzRemark(tzRemark);
							psTzClsDjzlTMapper.insert(psTzClsDjzlT);

							// 更新保留流程步骤回复内容
							sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetDjzlbzhfList");
							List<Map<String, Object>> listDjzlbzhf = sqlQuery.queryForList(sql,
									new Object[] { str_djzl_id, tzSbminfId });

							for (Map<String, Object> mapDjzlbzhf : listDjzlbzhf) {

								String tzSbminfRepId = mapDjzlbzhf.get("TZ_SBMINF_REP_ID") == null ? ""
										: String.valueOf(mapDjzlbzhf.get("TZ_SBMINF_REP_ID"));
								String tzSbminfRep = mapDjzlbzhf.get("TZ_SBMINF_REP") == null ? ""
										: String.valueOf(mapDjzlbzhf.get("TZ_SBMINF_REP"));

								PsTzClsHfdyT psTzClsHfdyT = new PsTzClsHfdyT();
								psTzClsHfdyT.setTzClassId(str_bj_id);
								psTzClsHfdyT.setTzSbminfId(tzSbminfId);
								psTzClsHfdyT.setTzSbminfRepId(tzSbminfRepId);
								psTzClsHfdyT.setTzSbminfRep(tzSbminfRep);

								sql = "select 'Y' from PS_TZ_CLS_HFDY_T where TZ_CLASS_ID=? and TZ_SBMINF_ID=? and TZ_SBMINF_REP_ID=?";
								String recExists = sqlQuery.queryForObject(sql,
										new Object[] { str_bj_id, tzSbminfId, tzSbminfRepId }, "String");

								if ("Y".equals(recExists)) {
									psTzClsHfdyTMapper.updateByPrimaryKey(psTzClsHfdyT);
								} else {
									psTzClsHfdyTMapper.insert(psTzClsHfdyT);
								}

							}

						}

					}

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("total", total);
					mapRet.put("root", listJson);

					strRet = jacksonUtil.Map2json(mapRet);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[0] = "参数不正确！";
		}

		return strRet;
	}

	/**
	 * 互斥班级查询
	 * 
	 * @param comParams
	 * @param errorMsg
	 * @return String
	 */
	public String queryHCCls(String comParams, String[] errorMsg) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(comParams);
			String strClassIdReq = jacksonUtil.getString("bj_id");

			String sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetHcbjCount");
			int total = sqlQuery.queryForObject(sql, new Object[] { strClassIdReq, orgid, strClassIdReq }, "int");

			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

			if (total > 0) {

				sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetHcbjList");

				List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
						new Object[] { strClassIdReq, orgid, strClassIdReq });

				for (Map<String, Object> mapData : listData) {

					Map<String, Object> mapJson = new HashMap<String, Object>();

					mapJson.put("hcgzTabClsId",
							mapData.get("TZ_CLASS_ID") == null ? "" : String.valueOf(mapData.get("TZ_CLASS_ID")));
					mapJson.put("hcgzTabClsName",
							mapData.get("TZ_CLASS_NAME") == null ? "" : String.valueOf(mapData.get("TZ_CLASS_NAME")));

					listJson.add(mapJson);
				}

			}

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", total);
			mapRet.put("root", listJson);

			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[0] = "参数不正确！";
		}

		return strRet;
	}

}
