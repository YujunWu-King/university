package com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjGzgxTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjLjgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjLjxsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjYbgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjGzgxT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjxsTKey;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjYbgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireEditorEngineImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.dao.PsTzCswjDcxTblMapper;
import com.tranzvision.gd.TZCanInTsinghuaBundle.dao.PsTzCswjPctTblMapper;
import com.tranzvision.gd.TZCanInTsinghuaBundle.dao.PsTzCswjTblMapper;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjDcxTbl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjPctTbl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzCswjXxxServiceImpl")
public class TzCswjXxxServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzDcWjDyTMapper psTzDcWjDyTMapper;
	@Autowired
	private PsTzDcWjLjgzTMapper psTzDcWjLjgzTMapper;
	@Autowired
	private PsTzDcWjYbgzTMapper psTzDcWjYbgzTMapper;
	@Autowired
	private PsTzDcWjLjxsTMapper psTzDcWjLjxsTMapper;
	@Autowired
	private PsTzDcWjGzgxTMapper psTzDcWjGzgxTMapper;
	@Autowired
	private PsTzCswjTblMapper PsTzCswjTblMapper;
	@Autowired
	private PsTzCswjDcxTblMapper PsTzCswjDcxTblMapper;
	@Autowired
	private PsTzCswjPctTblMapper PsTzCswjPctTblMapper;
	@Autowired
	private QuestionnaireEditorEngineImpl questionnaireEditorEngineImpl;

	/* 查询信息项可选值列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(comParams);
		// 测试问卷编号
		String strCswjId = jacksonUtil.getString("cswjId");
		// 问卷编号
		String strWjId = jacksonUtil.getString("wjId");
		// 问卷编号
		String strXxxBh = jacksonUtil.getString("xxxBh");
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try {
			int total = 0;
			// 查询总数;
			String totalSQL = "select count(*) from PS_TZ_CSWJ_PCT_TBL where TZ_CS_WJ_ID=? and TZ_DC_WJ_ID=? and TZ_XXX_BH=?";
			total = jdbcTemplate.queryForObject(totalSQL, new Object[] { strCswjId, strWjId, strXxxBh }, "Integer");
			String sql = "select TZ_XXXKXZ_MC,TZ_XXXKXZ_MS,TZ_U_LIMIT,TZ_L_LIMIT,TZ_HISTORY_VAL,TZ_CURYEAR_VAL from  PS_TZ_CSWJ_PCT_TBL where TZ_CS_WJ_ID=? and TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_XXXKXZ_MC+0 LIMIT ?,?";
			List<?> listData = jdbcTemplate.queryForList(sql,
					new Object[] { strCswjId, strWjId, strXxxBh, numStart, numLimit });
			for (Object objData : listData) {
				Map<String, Object> mapData = (Map<String, Object>) objData;
				int TZ_ORDER = mapData.get("TZ_ORDER") == null ? 0: Integer.valueOf(String.valueOf(mapData.get("TZ_ORDER")));
				float TZ_L_LIMIT = mapData.get("TZ_L_LIMIT") == null ? 0: Float.valueOf(String.valueOf(mapData.get("TZ_L_LIMIT")));
				float TZ_U_LIMIT = mapData.get("TZ_U_LIMIT") == null ? 0: Float.valueOf(String.valueOf(mapData.get("TZ_U_LIMIT")));
				float TZ_HISTORY_VAL = mapData.get("TZ_HISTORY_VAL") == null ? 0: Float.valueOf(String.valueOf(mapData.get("TZ_HISTORY_VAL")));
				float TZ_CURYEAR_VAL = mapData.get("TZ_CURYEAR_VAL") == null ? 0: Float.valueOf(String.valueOf(mapData.get("TZ_CURYEAR_VAL")));
				String TZ_XXXKXZ_MC = String.valueOf(mapData.get("TZ_XXXKXZ_MC"));
				//String TZ_XXXKXZ_MS = jdbcTemplate.queryForObject("select TZ_XXXKXZ_MS from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?",new Object[] { strWjId, strXxxBh, TZ_XXXKXZ_MC }, "String");
				String TZ_XXXKXZ_MS=(mapData.get("TZ_XXXKXZ_MS")==null?"":String.valueOf(mapData.get("TZ_XXXKXZ_MS")));
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("TZ_CS_WJ_ID", strCswjId);
				mapJson.put("TZ_DC_WJ_ID", strWjId);
				mapJson.put("TZ_XXX_BH", strXxxBh);
				mapJson.put("TZ_XXXKXZ_MC", TZ_XXXKXZ_MC);
				mapJson.put("TZ_XXXKXZ_MS", TZ_XXXKXZ_MS);
				mapJson.put("TZ_ORDER", TZ_ORDER);
				mapJson.put("TZ_L_LIMIT", TZ_L_LIMIT);
				mapJson.put("TZ_U_LIMIT", TZ_U_LIMIT);
				mapJson.put("TZ_HISTORY_VAL", TZ_HISTORY_VAL);
				mapJson.put("TZ_CURYEAR_VAL", TZ_CURYEAR_VAL);
				listJson.add(mapJson);
				mapRet.replace("total", total);
			}

			mapRet.replace("root", listJson);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/*开通在线调查
	 * 根据问卷模板创建在线调查，在线调查创建成功后，建立与测试问卷之间的关系
	 * */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		for (int i = 0; i < actData.length; i++) {
			System.out.println("------新建问卷tzAdd-------actData:" + actData[i]);
		}
		// 从actData中取出数据id
		// 调查问卷定义表 对应实体类
		PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs = new PsTzDcWjDyTWithBLOBs();
		// JS中传递的数据为null直接跳出
		if (actData.length <= 0)
			return null;
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(actData[0]);

		String dataWjId = jacksonUtil.getString("tplId");
		String csWjId = jacksonUtil.getString("csWjId");
		System.out.println("====tzAdd===dataWjId:" + dataWjId + "====csWjId:" + csWjId);
		// 情况1：问卷ID为空--->新建空白问卷
		String TZ_DC_WJ_ID = dataWjId;
		String TZ_DC_WJBT = null;
		if (dataWjId == null || dataWjId.equals("")) {
			return null;
		} else {
			// 情况3：type为add，id不为空 从模板复制问卷
			// PS_TZ_DC_DY_T 表中的SETID缺失？
			final String SQL = "select TZ_APPTPL_JSON_STR,TZ_DC_JWNR,TZ_DC_JTNR,TZ_APP_TPL_LAN from PS_TZ_DC_DY_T where TZ_APP_TPL_ID=?";
			final String csSql = "select TZ_CS_WJ_NAME,TZ_DC_WJ_KSRQ,TZ_DC_WJ_KSSJ,TZ_DC_WJ_JSRQ,TZ_DC_WJ_JSSJ,TZ_DC_WJ_ZT from PS_TZ_CSWJ_TBL WHERE TZ_CS_WJ_ID=?";
			Map<String, Object> tplDataMap = new HashMap<String, Object>();
			Map<String, Object> cswjDataMap = new HashMap<String, Object>();
			// dataWjId是前台传入的id中的数据 这里传入的是模板ID
			tplDataMap = jdbcTemplate.queryForMap(SQL, new Object[] { dataWjId });
			cswjDataMap = jdbcTemplate.queryForMap(csSql, new Object[] { csWjId });
			if (tplDataMap != null && cswjDataMap != null) {
				TZ_DC_WJ_ID = "" + getSeqNum.getSeqNum("TZ_DC_WJ_DY_T", "TZ_DC_WJ_ID");
				psTzDcWjDyTWithBLOBs.setTzDcWjId(TZ_DC_WJ_ID);
				// 模板ID
				psTzDcWjDyTWithBLOBs.setTzAppTplId(dataWjId);
				// 问卷标题
				TZ_DC_WJBT = cswjDataMap.get("TZ_CS_WJ_NAME") == null ? null: cswjDataMap.get("TZ_CS_WJ_NAME").toString();
				psTzDcWjDyTWithBLOBs.setTzDcWjbt(TZ_DC_WJBT);
				// 问卷状态
				String TZ_DC_WJ_ZT = cswjDataMap.get("TZ_DC_WJ_ZT") == null ? null: cswjDataMap.get("TZ_DC_WJ_ZT").toString();
				psTzDcWjDyTWithBLOBs.setTzDcWjZt(TZ_DC_WJ_ZT);
				// 发布状态 默认为已发布
				psTzDcWjDyTWithBLOBs.setTzDcWjFb("1");
				// 发布URL
				String TZ_DC_WJ_URL = "/university/dispatcher?classid=surveyapp&SURVEY_WJ_ID=" + TZ_DC_WJ_ID;
				psTzDcWjDyTWithBLOBs.setTzDcWjUrl(TZ_DC_WJ_URL);
				// 机构ID
				String TZ_JG_ID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				psTzDcWjDyTWithBLOBs.setTzJgId(TZ_JG_ID);

				String TZ_APPTPL_JSON_STR = tplDataMap.get("TZ_APPTPL_JSON_STR") == null ? null: tplDataMap.get("TZ_APPTPL_JSON_STR").toString();
				psTzDcWjDyTWithBLOBs.setTzApptplJsonStr(TZ_APPTPL_JSON_STR);

				String TZ_DC_JWNR = tplDataMap.get("TZ_DC_JWNR") == null ? null: tplDataMap.get("TZ_DC_JWNR").toString();
				psTzDcWjDyTWithBLOBs.setTzDcJwnr(TZ_DC_JWNR);

				String TZ_DC_JTNR = tplDataMap.get("TZ_DC_JTNR") == null ? null: tplDataMap.get("TZ_DC_JTNR").toString();
				psTzDcWjDyTWithBLOBs.setTzDcJtnr(TZ_DC_JTNR);

				String TZ_APP_TPL_LAN = tplDataMap.get("TZ_APP_TPL_LAN") == null ? null: tplDataMap.get("TZ_APP_TPL_LAN").toString();
				psTzDcWjDyTWithBLOBs.setTzAppTplLan(TZ_APP_TPL_LAN);
				// 答题规则
				String TZ_DC_WJ_DTGZ = "0";
				psTzDcWjDyTWithBLOBs.setTzDcWjDtgz(TZ_DC_WJ_DTGZ);
				//
				String TZ_DC_WJ_IPGZ = "3";
				psTzDcWjDyTWithBLOBs.setTzDcWjIpgz(TZ_DC_WJ_IPGZ);
				//
				String TZ_DC_WJ_JSGZ = "1";
				psTzDcWjDyTWithBLOBs.setTzDcWjJsgz(TZ_DC_WJ_JSGZ);
				// 非登录用户也可以参与本次调查
				String TZ_DC_WJ_DLZT = "Y";
				psTzDcWjDyTWithBLOBs.setTzDcWjDlzt(TZ_DC_WJ_DLZT);
				// 添加时间
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String nowTimeStr = simpleDateFormat.format(new Date());
					Date nowTime = simpleDateFormat.parse(nowTimeStr);
					psTzDcWjDyTWithBLOBs.setRowAddedDttm(nowTime);

					DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					// 开始日期
					Date TZ_DC_WJ_KSRQ = cswjDataMap.get("TZ_DC_WJ_KSRQ") == null ? null: dateFormat.parse(cswjDataMap.get("TZ_DC_WJ_KSRQ").toString());
					psTzDcWjDyTWithBLOBs.setTzDcWjKsrq(TZ_DC_WJ_KSRQ);
					// 开始时间
					Date TZ_DC_WJ_KSSJ = cswjDataMap.get("TZ_DC_WJ_KSSJ") == null ? null: timeFormat.parse(cswjDataMap.get("TZ_DC_WJ_KSSJ").toString());
					psTzDcWjDyTWithBLOBs.setTzDcWjKssj(TZ_DC_WJ_KSSJ);
					// 结束日期
					Date TZ_DC_WJ_JSRQ = cswjDataMap.get("TZ_DC_WJ_JSRQ") == null ? null: dateFormat.parse(cswjDataMap.get("TZ_DC_WJ_JSRQ").toString());
					psTzDcWjDyTWithBLOBs.setTzDcWjJsrq(TZ_DC_WJ_JSRQ);
					// 结束时间
					Date TZ_DC_WJ_JSSJ = cswjDataMap.get("TZ_DC_WJ_JSSJ") == null ? null: timeFormat.parse(cswjDataMap.get("TZ_DC_WJ_JSSJ").toString());
					psTzDcWjDyTWithBLOBs.setTzDcWjJssj(TZ_DC_WJ_JSSJ);
				} catch (Exception e) {
					System.out.println("====问卷添加==时间转换错误=");
				}
				psTzDcWjDyTMapper.insert(psTzDcWjDyTWithBLOBs);

				Map<String, Object> mapData = new HashMap<String, Object>();
				JacksonUtil jsonUtil = new JacksonUtil();
				jsonUtil.json2Map(TZ_APPTPL_JSON_STR);
				mapData = jsonUtil.getMap();
				String userID = tzLoginServiceImpl.getLoginedManagerOprid(request);
				/* 保存信息配置项表 */
				questionnaireEditorEngineImpl.saveSurvy(TZ_DC_WJ_ID, mapData, userID, new String[2]);

				/* 建立问卷和测试问卷之间的关系*/
				PsTzCswjTbl PsTzCswjTbl = new PsTzCswjTbl();
				PsTzCswjTbl.setTzCsWjId(csWjId);
				PsTzCswjTbl.setTzCsWjName(TZ_DC_WJBT);
				PsTzCswjTbl.setTzDcWjId(TZ_DC_WJ_ID);
				PsTzCswjTblMapper.updateByPrimaryKeySelective(PsTzCswjTbl);

				/* 初始化测试问卷调查项配置表 */
				//您的本科院校 信息项编号定义为hardcode点
				String	strBkSchool=jdbcTemplate.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?", new Object[]{"TZ_BKYX_XXXBH"}, "String");
					
				final String cswjDcxSql = "select TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_COM_LMC from PS_TZ_DCWJ_XXX_VW where TZ_DC_WJ_ID=? ORDER BY TZ_ORDER";
				List<Map<String, Object>> cswjDcxDataList = new ArrayList<Map<String, Object>>();
				cswjDcxDataList = jdbcTemplate.queryForList(cswjDcxSql, new Object[] { TZ_DC_WJ_ID });
				if (cswjDcxDataList != null) {
					int j = 0;
					for (int k = 0; k < cswjDcxDataList.size(); k++) {
						j = j + 1;
						Map<String, Object> mbLJXSMap = new HashMap<String, Object>();
						mbLJXSMap = cswjDcxDataList.get(k);
						String TZ_XXX_BH = mbLJXSMap.get("TZ_XXX_BH") == null ? null: mbLJXSMap.get("TZ_XXX_BH").toString();
						String TZ_XXX_MC = mbLJXSMap.get("TZ_XXX_MC") == null ? null: mbLJXSMap.get("TZ_XXX_MC").toString();
						String TZ_TITLE = mbLJXSMap.get("TZ_TITLE") == null ? null: mbLJXSMap.get("TZ_TITLE").toString();
						//组件类型
						String TZ_COM_LMC=mbLJXSMap.get("TZ_COM_LMC") == null ? null: mbLJXSMap.get("TZ_COM_LMC").toString();
						//填空题只统计 您的院校类型，其它的填空题不统计
						if(TZ_COM_LMC.equals("autoCompletion")&&TZ_XXX_BH.equals(strBkSchool)){
							PsTzCswjDcxTbl PsTzCswjDcxTbl = new PsTzCswjDcxTbl();
							PsTzCswjDcxTbl.setTzCsWjId(csWjId);
							PsTzCswjDcxTbl.setTzDcWjId(TZ_DC_WJ_ID);
							PsTzCswjDcxTbl.setTzOrder(j);
							PsTzCswjDcxTbl.setTzXxxBh(TZ_XXX_BH);
							PsTzCswjDcxTbl.setTzXxxDesc(TZ_TITLE);
							PsTzCswjDcxTbl.setTzXxxMc(TZ_XXX_MC);
							PsTzCswjDcxTblMapper.insert(PsTzCswjDcxTbl);
						}else{
							if(!TZ_COM_LMC.equals("autoCompletion")){
								PsTzCswjDcxTbl PsTzCswjDcxTbl = new PsTzCswjDcxTbl();
								PsTzCswjDcxTbl.setTzCsWjId(csWjId);
								PsTzCswjDcxTbl.setTzDcWjId(TZ_DC_WJ_ID);
								PsTzCswjDcxTbl.setTzOrder(j);
								PsTzCswjDcxTbl.setTzXxxBh(TZ_XXX_BH);
								PsTzCswjDcxTbl.setTzXxxDesc(TZ_TITLE);
								PsTzCswjDcxTbl.setTzXxxMc(TZ_XXX_MC);
								PsTzCswjDcxTblMapper.insert(PsTzCswjDcxTbl);
							}
						}
						
					}
				}

				/* 初始化调查项统计百分比配置表 */
				List<Map<String, Object>> cswjXxxDataList = jdbcTemplate.queryForList("select TZ_XXX_BH from PS_TZ_DCWJ_XXX_VW where TZ_DC_WJ_ID=? and TZ_XXX_BH<>?", new Object[] { TZ_DC_WJ_ID,strBkSchool});
				if (cswjXxxDataList != null) {
					for (int z = 0; z < cswjXxxDataList.size(); z++) {
						Map<String, Object> cswjXXMap = new HashMap<String, Object>();
						cswjXXMap = cswjXxxDataList.get(z);
						String TZ_XXX_BH = cswjXXMap.get("TZ_XXX_BH") == null ? null: cswjXXMap.get("TZ_XXX_BH").toString();
						
						final String cswjPctSql = "select a.TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T a where  a.TZ_DC_WJ_ID=? and a.TZ_XXX_BH=? ORDER BY a.TZ_ORDER";
						List<Map<String, Object>> cswjPctDataList = new ArrayList<Map<String, Object>>();
						cswjPctDataList = jdbcTemplate.queryForList(cswjPctSql, new Object[] { TZ_DC_WJ_ID ,TZ_XXX_BH});
						if (cswjPctDataList != null) {
							int j = 0;
							for (int k = 0; k < cswjPctDataList.size(); k++) {
								//当前年份初始取值,开通在线调查的时候，在线调查填写人是是0(初始化的时候分母为0？？,暂不初始化)
								//int preset=jdbcTemplate.queryForObject("SELECT TZ_PRESET_NUM from PS_TZ_CSWJ_TBL where TZ_DC_WJ_ID=?", new Object[]{csWjId}, "Integer");
								j = j + 1;
								Map<String, Object> mbLJXSMap = new HashMap<String, Object>();
								mbLJXSMap = cswjPctDataList.get(k);
								String TZ_XXXKXZ_MC = mbLJXSMap.get("TZ_XXXKXZ_MC") == null ? null: mbLJXSMap.get("TZ_XXXKXZ_MC").toString();
								String TZ_XXXKXZ_MS = jdbcTemplate.queryForObject("select TZ_XXXKXZ_MS from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?",new Object[] { TZ_DC_WJ_ID, TZ_XXX_BH, TZ_XXXKXZ_MC }, "String");
								PsTzCswjPctTbl PsTzCswjPctTbl = new PsTzCswjPctTbl();
								PsTzCswjPctTbl.setTzCsWjId(csWjId);
								PsTzCswjPctTbl.setTzDcWjId(TZ_DC_WJ_ID);
								PsTzCswjPctTbl.setTzXxxBh(TZ_XXX_BH);
								PsTzCswjPctTbl.setTzXxxkxzMc(TZ_XXXKXZ_MC);
								PsTzCswjPctTbl.setTzXxxkxzMs(TZ_XXXKXZ_MS);
								PsTzCswjPctTbl.setTzOrder(j);
								PsTzCswjPctTblMapper.insert(PsTzCswjPctTbl);
							}
						}
					}
				}
				/* 复制模板逻辑-在线调查问卷逻辑规则定义表 */
				final String mbLJGZSql = "select TZ_DC_LJTJ_ID,TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH from PS_TZ_DC_MB_LJGZ_T where TZ_APP_TPL_ID=?";
				List<Map<String, Object>> mbLJGZDataList = new ArrayList<Map<String, Object>>();
				mbLJGZDataList = jdbcTemplate.queryForList(mbLJGZSql, new Object[] { dataWjId });
				String TZ_DC_LJTJ_ID = null;
				if (mbLJGZDataList != null) {
					for (int i = 0; i < mbLJGZDataList.size(); i++) {
						Map<String, Object> mbLJGZMap = mbLJGZDataList.get(i);

						TZ_DC_LJTJ_ID = mbLJGZMap.get("TZ_DC_LJTJ_ID") == null ? null
								: mbLJGZMap.get("TZ_DC_LJTJ_ID").toString();
						String logicalId = "L" + getSeqNum.getSeqNum("TZ_DC_WJ_LJGZ_T", "TZ_DC_LJTJ_ID");
						String TZ_XXX_BH = mbLJGZMap.get("TZ_XXX_BH") == null ? null
								: mbLJGZMap.get("TZ_XXX_BH").toString();
						String TZ_LJ_LX = mbLJGZMap.get("TZ_LJ_LX") == null ? null
								: mbLJGZMap.get("TZ_LJ_LX").toString();
						String TZ_PAGE_NO = mbLJGZMap.get("TZ_PAGE_NO") == null ? null
								: mbLJGZMap.get("TZ_PAGE_NO").toString();
						String TZ_LJTJ_XH = mbLJGZMap.get("TZ_LJTJ_XH") == null ? null
								: mbLJGZMap.get("TZ_LJTJ_XH").toString();

						PsTzDcWjLjgzT psTzDcWjLjgzT = new PsTzDcWjLjgzT();
						psTzDcWjLjgzT.setTzDcLjtjId(logicalId);

						psTzDcWjLjgzT.setTzDcWjId(TZ_DC_WJ_ID);
						psTzDcWjLjgzT.setTzXxxBh(TZ_XXX_BH);
						psTzDcWjLjgzT.setTzLjLx(TZ_LJ_LX);
						psTzDcWjLjgzT.setTzPageNo(Integer.valueOf(TZ_PAGE_NO));
						psTzDcWjLjgzT.setTzLjtjXh(Integer.valueOf(TZ_LJTJ_XH));

						psTzDcWjLjgzTMapper.insert(psTzDcWjLjgzT);
						// System.out.println("==执行==psTzDcWjLjgzTMapper.insert()");
						/* 复制模板逻辑-在线调查问卷一般题型逻辑规则关系表 */
						final String mbYBGZSql = "select TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_MB_YBGZ_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";

						List<Map<String, Object>> mbYBGZDataList = new ArrayList<Map<String, Object>>();
						mbYBGZDataList = jdbcTemplate.queryForList(mbYBGZSql, new Object[] { dataWjId, TZ_DC_LJTJ_ID });

						if (mbYBGZDataList != null) {
							for (int j = 0; j < mbYBGZDataList.size(); j++) {
								Map<String, Object> mbYBGZMap = new HashMap<String, Object>();
								mbYBGZMap = mbYBGZDataList.get(j);

								TZ_XXX_BH = mbYBGZMap.get("TZ_XXX_BH") == null ? null
										: mbYBGZMap.get("TZ_XXX_BH").toString();
								String TZ_XXXKXZ_MC = mbYBGZMap.get("TZ_XXXKXZ_MC") == null ? null
										: mbYBGZMap.get("TZ_XXXKXZ_MC").toString();
								String TZ_IS_SELECTED = mbYBGZMap.get("TZ_IS_SELECTED") == null ? null
										: mbYBGZMap.get("TZ_IS_SELECTED").toString();

								PsTzDcWjYbgzT psTzDcWjYbgzT = new PsTzDcWjYbgzT();
								psTzDcWjYbgzT.setTzDcLjtjId(logicalId);
								psTzDcWjYbgzT.setTzDcWjId(TZ_DC_WJ_ID);
								psTzDcWjYbgzT.setTzXxxBh(TZ_XXX_BH);
								psTzDcWjYbgzT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
								psTzDcWjYbgzT.setTzIsSelected(TZ_IS_SELECTED);

								psTzDcWjYbgzTMapper.insert(psTzDcWjYbgzT);
								// System.out.println("==执行==psTzDcWjYbgzTMapper.insert()");
							}
						}
						/* 复制模板逻辑-在线调查问卷表格题逻辑规则关系表 */
						final String mbGZGXSqlCopy = "select TZ_XXX_BH,TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_MB_GZGX_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
						List<Map<String, Object>> mbGZGXSDataList = new ArrayList<Map<String, Object>>();
						mbGZGXSDataList = jdbcTemplate.queryForList(mbGZGXSqlCopy,
								new Object[] { dataWjId, TZ_DC_LJTJ_ID });
						if (mbGZGXSDataList != null) {
							for (int m = 0; m < mbGZGXSDataList.size(); m++) {
								Map<String, Object> mbGZGXSMap = new HashMap<String, Object>();
								mbGZGXSMap = mbGZGXSDataList.get(m);

								TZ_XXX_BH = mbGZGXSMap.get("TZ_XXX_BH") == null ? null
										: mbGZGXSMap.get("TZ_XXX_BH").toString();
								String TZ_XXXZWT_MC = mbGZGXSMap.get("TZ_XXXZWT_MC") == null ? null
										: mbGZGXSMap.get("TZ_XXXZWT_MC").toString();
								String TZ_XXXKXZ_MC = mbGZGXSMap.get("TZ_XXXKXZ_MC") == null ? null
										: mbGZGXSMap.get("TZ_XXXKXZ_MC").toString();
								String TZ_IS_SELECTED = mbGZGXSMap.get("TZ_IS_SELECTED") == null ? null
										: mbGZGXSMap.get("TZ_IS_SELECTED").toString();

								PsTzDcWjGzgxT psTzDcWjGzgxT = new PsTzDcWjGzgxT();
								psTzDcWjGzgxT.setTzDcLjtjId(logicalId);
								psTzDcWjGzgxT.setTzDcWjId(TZ_DC_LJTJ_ID);
								psTzDcWjGzgxT.setTzXxxBh(TZ_XXX_BH);
								psTzDcWjGzgxT.setTzXxxzwtMc(TZ_XXXZWT_MC);
								psTzDcWjGzgxT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
								psTzDcWjGzgxT.setTzIsSelected(TZ_IS_SELECTED);

								psTzDcWjGzgxTMapper.insert(psTzDcWjGzgxT);

							}
						}

						/* 复制逻辑-在线调查问卷关联显示逻辑关系表 */
						final String mbGZGXSql = "select TZ_XXX_BH from PS_TZ_DC_MB_LJXS_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
						List<Map<String, Object>> mbLJXSDataList = new ArrayList<Map<String, Object>>();
						mbLJXSDataList = jdbcTemplate.queryForList(mbGZGXSql, new Object[] { dataWjId, TZ_DC_LJTJ_ID });
						if (mbLJXSDataList != null) {
							for (int k = 0; k < mbLJXSDataList.size(); k++) {
								Map<String, Object> mbLJXSMap = new HashMap<String, Object>();
								mbLJXSMap = mbLJXSDataList.get(k);
								TZ_XXX_BH = mbLJXSMap.get("TZ_XXX_BH") == null ? null
										: mbLJXSMap.get("TZ_XXX_BH").toString();

								PsTzDcWjLjxsTKey psTzDcWjLjxsTKey = new PsTzDcWjLjxsTKey();
								psTzDcWjLjxsTKey.setTzDcLjtjId(logicalId);
								psTzDcWjLjxsTKey.setTzDcWjId(TZ_DC_WJ_ID);
								psTzDcWjLjxsTKey.setTzXxxBh(TZ_XXX_BH);

								psTzDcWjLjxsTMapper.insert(psTzDcWjLjxsTKey);
								// System.out.println("==执行==psTzDcWjLjxsTMapper.insert()");
							}
						}

					}
				}
			}
		}

		return "{\"id\":\"" + TZ_DC_WJ_ID + "\",\"name\":\"" + TZ_DC_WJBT + "\"}";
	}

	/**
	 * 更新调查项统计百分比配置(需要加以判断，百分比相加要么为0要么相加等于100,已经在JS端加以判断了)
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("PAGE".equals(typeFlag)) {
					String TZ_CS_WJ_ID = String.valueOf(mapData.get("TZ_CS_WJ_ID"));
					String TZ_DC_WJ_ID = String.valueOf(mapData.get("TZ_DC_WJ_ID"));
					String TZ_XXX_BH = String.valueOf(mapData.get("TZ_XXX_BH"));

					String TZ_XXXKXZ_MC = String.valueOf(mapData.get("TZ_XXXKXZ_MC"));
					String TZ_XXXKXZ_MS = String.valueOf(mapData.get("TZ_XXXKXZ_MS"));
					/*数字题的可选值编号自增*/
					if(TZ_XXXKXZ_MC.equals("null")){
						TZ_XXXKXZ_MC="" + getSeqNum.getSeqNum("PS_TZ_CSWJ_PCT_TBL", "TZ_XXXKXZ_MC");
					}
					int TZ_ORDER = mapData.get("TZ_ORDER") == null ? 0: Integer.valueOf(String.valueOf(mapData.get("TZ_ORDER")));
					float TZ_L_LIMIT = mapData.get("TZ_L_LIMIT") == null ? 0: Float.valueOf(String.valueOf(mapData.get("TZ_L_LIMIT")));
					float TZ_U_LIMIT = mapData.get("TZ_U_LIMIT") == null ? 0: Float.valueOf(mapData.get("TZ_U_LIMIT").toString());
					float TZ_HISTORY_VAL = mapData.get("TZ_HISTORY_VAL") == null ? 0: Float.valueOf(mapData.get("TZ_HISTORY_VAL").toString());
					float TZ_CURYEAR_VAL = mapData.get("TZ_CURYEAR_VAL") == null ? 0: Float.valueOf(String.valueOf(mapData.get("TZ_CURYEAR_VAL")));

					String sqlGetMsgInfoTmp = "select count(*) from PS_TZ_CSWJ_PCT_TBL where TZ_CS_WJ_ID=? and TZ_DC_WJ_ID=? and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?";
					int count = jdbcTemplate.queryForObject(sqlGetMsgInfoTmp,new Object[] { TZ_CS_WJ_ID, TZ_DC_WJ_ID, TZ_XXX_BH, TZ_XXXKXZ_MC }, "Integer");
					if (count > 0) {
						PsTzCswjPctTbl PsTzCswjPctTbl = new PsTzCswjPctTbl();
						PsTzCswjPctTbl.setTzCsWjId(TZ_CS_WJ_ID);
						PsTzCswjPctTbl.setTzDcWjId(TZ_DC_WJ_ID);
						PsTzCswjPctTbl.setTzXxxBh(TZ_XXX_BH);
						PsTzCswjPctTbl.setTzXxxkxzMc(TZ_XXXKXZ_MC);
						PsTzCswjPctTbl.setTzXxxkxzMs(TZ_XXXKXZ_MS);
						PsTzCswjPctTbl.setTzCuryearVal(TZ_CURYEAR_VAL);
						PsTzCswjPctTbl.setTzHistoryVal(TZ_HISTORY_VAL);
						PsTzCswjPctTbl.setTzLLimit(TZ_L_LIMIT);
						PsTzCswjPctTbl.setTzULimit(TZ_U_LIMIT);
						PsTzCswjPctTbl.setTzOrder(TZ_ORDER);
						PsTzCswjPctTblMapper.updateByPrimaryKeySelective(PsTzCswjPctTbl);
					} else {
						PsTzCswjPctTbl PsTzCswjPctTbl = new PsTzCswjPctTbl();
						PsTzCswjPctTbl.setTzCsWjId(TZ_CS_WJ_ID);
						PsTzCswjPctTbl.setTzDcWjId(TZ_DC_WJ_ID);
						PsTzCswjPctTbl.setTzXxxBh(TZ_XXX_BH);
						PsTzCswjPctTbl.setTzXxxkxzMs(TZ_XXXKXZ_MS);
						PsTzCswjPctTbl.setTzXxxkxzMc(TZ_XXXKXZ_MC);
						PsTzCswjPctTbl.setTzCuryearVal(TZ_CURYEAR_VAL);
						PsTzCswjPctTbl.setTzHistoryVal(TZ_HISTORY_VAL);
						PsTzCswjPctTbl.setTzLLimit(TZ_L_LIMIT);
						PsTzCswjPctTbl.setTzULimit(TZ_U_LIMIT);
						PsTzCswjPctTbl.setTzOrder(TZ_ORDER);
						PsTzCswjPctTblMapper.insertSelective(PsTzCswjPctTbl);
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
	
	/*
	 * 删除信息项配置表
	 * */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		//String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strCswjID="";
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				// 测试问卷编号;
				 strCswjID = jacksonUtil.getString("TZ_CS_WJ_ID");
				//问卷编号
				String strWjID = jacksonUtil.getString("TZ_DC_WJ_ID");
				//信息项编号
				String strXxBh = jacksonUtil.getString("TZ_XXX_BH");
				//信息项可选值编号
				String strXxxKxzBh = jacksonUtil.getString("TZ_XXXKXZ_MC");
				
				if (strCswjID != null && !"".equals(strCswjID) && strWjID != null && !"".equals(strWjID)&&strXxBh != null && !"".equals(strXxBh)&&strXxxKxzBh != null && !"".equals(strXxxKxzBh)) {
					PsTzCswjPctTbl PsTzCswjPctTbl = new PsTzCswjPctTbl();
					PsTzCswjPctTbl.setTzCsWjId(strCswjID);
					PsTzCswjPctTbl.setTzDcWjId(strWjID);
					PsTzCswjPctTbl.setTzXxxBh(strXxBh);
					PsTzCswjPctTbl.setTzXxxkxzMc(strXxxKxzBh);
					PsTzCswjPctTblMapper.deleteByPrimaryKey(PsTzCswjPctTbl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return "{\"delete\":\"true\"}";
	}
}
