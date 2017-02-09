package com.tranzvision.gd.TZSchlrBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSchlrBundle.dao.PsTzSchlrTblMapper;
import com.tranzvision.gd.TZSchlrBundle.model.PsTzSchlrTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * @author LDD 奖学金录入
 */
@Service("com.tranzvision.gd.TZSchlrBundle.service.impl.TzSchLrClsServiceImpl")
public class TzSchLrClsServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
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
	private PsTzSchlrTblMapper psTzSchlrTblMapper;
	@Autowired
	private QuestionnaireEditorEngineImpl questionnaireEditorEngineImpl;
	
	
	/*获取用户账号信息列表*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams,int numLimit, int numStart, String[] errorMsg){
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//排序字段如果没有不要赋值
			//String[][] orderByArr=new String[][]{{"ROW_ADDED_DTTM","DESC"}};
			String[][] orderByArr=new String[][]{};
			//json数据要的结果字段;
			String[] resultFldArray = { "TZ_SCHLR_ID","TZ_SCHLR_NAME", "TZ_STATE", "TZ_DC_WJ_ID","TZ_DC_WJ_KSRQ","TZ_DC_WJ_JSRQ"};
					
			//可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit,numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("TZ_SCHLR_ID", rowList[0]);
					mapList.put("TZ_SCHLR_NAME", rowList[1]);
					mapList.put("TZ_JXJ_STATE", rowList[2]);
					mapList.put("TZ_DC_WJ_ID", rowList[3]);
					mapList.put("TZ_DC_WJ_KSRQ", rowList[4]);
					mapList.put("TZ_DC_WJ_JSRQ", rowList[5]);
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

	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		if (actData.length <= 0){
			return null;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(actData[0]);

		String dataWjId = jacksonUtil.getString("id");
		String shcolarName = jacksonUtil.getString("name");
		String TZ_SCHLR_ID="";
		final String SQL = "select TZ_APPTPL_JSON_STR,TZ_DC_JWNR,TZ_DC_JTNR,TZ_APP_TPL_LAN from PS_TZ_DC_DY_T where TZ_APP_TPL_ID=?";
		Map<String, Object> tplDataMap = new HashMap<String, Object>();
		// dataWjId是前台传入的id中的数据 这里传入的是模板ID
		tplDataMap = jdbcTemplate.queryForMap(SQL, new Object[] { dataWjId });
		if (tplDataMap != null ) {
			// 调查问卷定义表 对应实体类
			PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs = new PsTzDcWjDyTWithBLOBs();
			String TZ_DC_WJ_ID = "" + getSeqNum.getSeqNum("TZ_DC_WJ_DY_T", "TZ_DC_WJ_ID");
			psTzDcWjDyTWithBLOBs.setTzDcWjId(TZ_DC_WJ_ID);
			// 模板ID
			psTzDcWjDyTWithBLOBs.setTzAppTplId(dataWjId);
			// 问卷标题(默认和奖学金标题相同)
			psTzDcWjDyTWithBLOBs.setTzDcWjbt(shcolarName);
			// 问卷状态(默认为进行中)
			psTzDcWjDyTWithBLOBs.setTzDcWjZt("1");
			// 发布状态 默认为发布
			psTzDcWjDyTWithBLOBs.setTzDcWjFb("1");
			// 发布URL
			String TZ_DC_WJ_URL = "/university/dispatcher?classid=surveyapp&SURVEY_WJ_ID=" + TZ_DC_WJ_ID;
			psTzDcWjDyTWithBLOBs.setTzDcWjUrl(TZ_DC_WJ_URL);
			// 机构ID
			String TZ_JG_ID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			psTzDcWjDyTWithBLOBs.setTzJgId(TZ_JG_ID);
			//当前登录人
			String TZ_OPRID=tzLoginServiceImpl.getLoginedManagerOprid(request);

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
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowTimeStr = simpleDateFormat.format(new Date());
			
			try {
				Date nowTime = simpleDateFormat.parse(nowTimeStr);
				psTzDcWjDyTWithBLOBs.setRowAddedDttm(nowTime);
				psTzDcWjDyTWithBLOBs.setRowLastmantDttm(nowTime);
				psTzDcWjDyTWithBLOBs.setRowAddedOprid(TZ_OPRID);
				psTzDcWjDyTWithBLOBs.setRowLastmantOprid(TZ_OPRID);

				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				//开始日期
				psTzDcWjDyTWithBLOBs.setTzDcWjKsrq(dateFormat.parse(dateFormat.format(new Date())));
				// 开始时间
				psTzDcWjDyTWithBLOBs.setTzDcWjKssj(timeFormat.parse("08:30:00"));
				//结束日期
				psTzDcWjDyTWithBLOBs.setTzDcWjJsrq(dateFormat.parse(dateFormat.format(new Date())));
				// 结束时间
				psTzDcWjDyTWithBLOBs.setTzDcWjJssj( timeFormat.parse("17:30:00"));
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

			/*建立奖学金和问卷之间的关系*/
			PsTzSchlrTbl PsTzSchlrTbl=new PsTzSchlrTbl();
			 TZ_SCHLR_ID = "" + getSeqNum.getSeqNum("PS_TZ_SCHLR_TBL", "TZ_SCHLR_ID");
			PsTzSchlrTbl.setTzSchlrId(TZ_SCHLR_ID);
			PsTzSchlrTbl.setTzSchlrName(shcolarName);
			PsTzSchlrTbl.setTzJgId(TZ_JG_ID);
			PsTzSchlrTbl.setTzDcWjId(TZ_DC_WJ_ID);
			//默认为有效
			PsTzSchlrTbl.setTzState("Y");
			PsTzSchlrTbl.setRowAddedOprid(TZ_OPRID);
			PsTzSchlrTbl.setRowLastmantOprid(TZ_OPRID);
			try {
				Date nowTime = simpleDateFormat.parse(nowTimeStr);
				PsTzSchlrTbl.setRowAddedDttm(nowTime);
				PsTzSchlrTbl.setRowLastmantDttm(nowTime);
			} catch (Exception e) {
			}
			psTzSchlrTblMapper.insert(PsTzSchlrTbl);
			
			
			/* 复制模板逻辑-在线调查问卷逻辑规则定义表 */
			final String mbLJGZSql = "select TZ_DC_LJTJ_ID,TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH from PS_TZ_DC_MB_LJGZ_T where TZ_APP_TPL_ID=?";
			List<Map<String, Object>> mbLJGZDataList = new ArrayList<Map<String, Object>>();
			mbLJGZDataList = jdbcTemplate.queryForList(mbLJGZSql, new Object[] { dataWjId });
			String TZ_DC_LJTJ_ID = null;
			if (mbLJGZDataList != null) {
				for (int i = 0; i < mbLJGZDataList.size(); i++) {
					Map<String, Object> mbLJGZMap = mbLJGZDataList.get(i);

					TZ_DC_LJTJ_ID = mbLJGZMap.get("TZ_DC_LJTJ_ID") == null ? null: mbLJGZMap.get("TZ_DC_LJTJ_ID").toString();
					String logicalId = "L" + getSeqNum.getSeqNum("TZ_DC_WJ_LJGZ_T", "TZ_DC_LJTJ_ID");
					String TZ_XXX_BH = mbLJGZMap.get("TZ_XXX_BH") == null ? null: mbLJGZMap.get("TZ_XXX_BH").toString();
					String TZ_LJ_LX = mbLJGZMap.get("TZ_LJ_LX") == null ? null: mbLJGZMap.get("TZ_LJ_LX").toString();
					String TZ_PAGE_NO = mbLJGZMap.get("TZ_PAGE_NO") == null ? null: mbLJGZMap.get("TZ_PAGE_NO").toString();
					String TZ_LJTJ_XH = mbLJGZMap.get("TZ_LJTJ_XH") == null ? null: mbLJGZMap.get("TZ_LJTJ_XH").toString();

					PsTzDcWjLjgzT psTzDcWjLjgzT = new PsTzDcWjLjgzT();
					psTzDcWjLjgzT.setTzDcLjtjId(logicalId);

					psTzDcWjLjgzT.setTzDcWjId(TZ_DC_WJ_ID);
					psTzDcWjLjgzT.setTzXxxBh(TZ_XXX_BH);
					psTzDcWjLjgzT.setTzLjLx(TZ_LJ_LX);
					psTzDcWjLjgzT.setTzPageNo(Integer.valueOf(TZ_PAGE_NO));
					psTzDcWjLjgzT.setTzLjtjXh(Integer.valueOf(TZ_LJTJ_XH));

					psTzDcWjLjgzTMapper.insert(psTzDcWjLjgzT);
					/* 复制模板逻辑-在线调查问卷一般题型逻辑规则关系表 */
					final String mbYBGZSql = "select TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_MB_YBGZ_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";

					List<Map<String, Object>> mbYBGZDataList = new ArrayList<Map<String, Object>>();
					mbYBGZDataList = jdbcTemplate.queryForList(mbYBGZSql, new Object[] { dataWjId, TZ_DC_LJTJ_ID });

					if (mbYBGZDataList != null) {
						for (int j = 0; j < mbYBGZDataList.size(); j++) {
							Map<String, Object> mbYBGZMap = new HashMap<String, Object>();
							mbYBGZMap = mbYBGZDataList.get(j);

							TZ_XXX_BH = mbYBGZMap.get("TZ_XXX_BH") == null ? null: mbYBGZMap.get("TZ_XXX_BH").toString();
							String TZ_XXXKXZ_MC = mbYBGZMap.get("TZ_XXXKXZ_MC") == null ? null: mbYBGZMap.get("TZ_XXXKXZ_MC").toString();
							String TZ_IS_SELECTED = mbYBGZMap.get("TZ_IS_SELECTED") == null ? null: mbYBGZMap.get("TZ_IS_SELECTED").toString();

							PsTzDcWjYbgzT psTzDcWjYbgzT = new PsTzDcWjYbgzT();
							psTzDcWjYbgzT.setTzDcLjtjId(logicalId);
							psTzDcWjYbgzT.setTzDcWjId(TZ_DC_WJ_ID);
							psTzDcWjYbgzT.setTzXxxBh(TZ_XXX_BH);
							psTzDcWjYbgzT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
							psTzDcWjYbgzT.setTzIsSelected(TZ_IS_SELECTED);

							psTzDcWjYbgzTMapper.insert(psTzDcWjYbgzT);
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

							TZ_XXX_BH = mbGZGXSMap.get("TZ_XXX_BH") == null ? null: mbGZGXSMap.get("TZ_XXX_BH").toString();
							String TZ_XXXZWT_MC = mbGZGXSMap.get("TZ_XXXZWT_MC") == null ? null: mbGZGXSMap.get("TZ_XXXZWT_MC").toString();
							String TZ_XXXKXZ_MC = mbGZGXSMap.get("TZ_XXXKXZ_MC") == null ? null: mbGZGXSMap.get("TZ_XXXKXZ_MC").toString();
							String TZ_IS_SELECTED = mbGZGXSMap.get("TZ_IS_SELECTED") == null ? null: mbGZGXSMap.get("TZ_IS_SELECTED").toString();

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
							TZ_XXX_BH = mbLJXSMap.get("TZ_XXX_BH") == null ? null: mbLJXSMap.get("TZ_XXX_BH").toString();

							PsTzDcWjLjxsTKey psTzDcWjLjxsTKey = new PsTzDcWjLjxsTKey();
							psTzDcWjLjxsTKey.setTzDcLjtjId(logicalId);
							psTzDcWjLjxsTKey.setTzDcWjId(TZ_DC_WJ_ID);
							psTzDcWjLjxsTKey.setTzXxxBh(TZ_XXX_BH);
							psTzDcWjLjxsTMapper.insert(psTzDcWjLjxsTKey);
						}
					}

				}
			}
		}
		return "{\"TZ_SCHLR_ID\":\""+TZ_SCHLR_ID+"\"}";
	}
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
	    DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss"); 
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		    
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("TZ_SCHLR_ID")) {
				// 测试问卷编号;
				String tzSchLrId = jacksonUtil.getString("TZ_SCHLR_ID");
				PsTzSchlrTbl PsTzSchlrTbl =psTzSchlrTblMapper.selectByPrimaryKey(tzSchLrId);
				if (PsTzSchlrTbl != null) {
					PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs = psTzDcWjDyTMapper.selectByPrimaryKey(PsTzSchlrTbl.getTzDcWjId());
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("TZ_SCHLR_ID", tzSchLrId);
					retMap.put("TZ_SCHLR_NAME", PsTzSchlrTbl.getTzSchlrName());
					retMap.put("TZ_JXJ_STATE", PsTzSchlrTbl.getTzState());
					retMap.put("TZ_DC_WJ_ID", PsTzSchlrTbl.getTzDcWjId());
					retMap.put("TZ_DC_WJ_ZT", psTzDcWjDyTWithBLOBs.getTzDcWjZt());
					if(psTzDcWjDyTWithBLOBs.getTzDcWjKsrq()!=null){
						retMap.put("TZ_DC_WJ_KSRQ", dateFormat.format(psTzDcWjDyTWithBLOBs.getTzDcWjKsrq()));
				    };
					retMap.put("TZ_DC_WJ_KSSJ", timeFormat.format(psTzDcWjDyTWithBLOBs.getTzDcWjKssj()));
					//结束日期
					if(psTzDcWjDyTWithBLOBs.getTzDcWjJsrq()!=null){
						retMap.put("TZ_DC_WJ_JSRQ",dateFormat.format(psTzDcWjDyTWithBLOBs.getTzDcWjJsrq()));
				    };
					retMap.put("TZ_DC_WJ_JSSJ", timeFormat.format(psTzDcWjDyTWithBLOBs.getTzDcWjJssj()));
					returnJsonMap.replace("formData",retMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "奖学金数据不存在！";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "奖学金数据不存在！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {

		for(int index=0;index<actData.length;index++)
		System.out.println();
		String strRet = "{}";

		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String userID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能保存奖学金！";
			return strRet;
		}

		if (userID.equals("TZ_GUEST")) {
			errMsg[0] = "1";
			errMsg[1] = "请先登录再操作";
			return strRet;
		}

		try {
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				
				strRet = this.tzUpdateFattrInfo(strForm, orgId, userID, errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	/**
	 * 
	 * @param strForm
	 * @return 设置 问卷模板 模板设置 修改问卷模板的时，语言不可更改
	 */
	private String tzUpdateFattrInfo(String strForm, String orgId, String userID, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		String TZ_SCHLR_ID = null;
	    String TZ_STATE=null;
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strForm);
			TZ_SCHLR_ID = jacksonUtil.getString("TZ_SCHLR_ID");
			TZ_STATE = jacksonUtil.getString("TZ_JXJ_STATE");
			String isTPL = "";
			String isZcSQL = "SELECT 'Y' FROM PS_TZ_SCHLR_TBL WHERE TZ_JG_ID=?  and  TZ_SCHLR_ID=?";
			isTPL = jdbcTemplate.queryForObject(isZcSQL, new Object[] { orgId, TZ_SCHLR_ID }, "String");
			// 存在就修改
			if ("Y".equals(isTPL)) {
				PsTzSchlrTbl PsTzSchlrTbl=new PsTzSchlrTbl();
				PsTzSchlrTbl.setTzSchlrId(TZ_SCHLR_ID);
				PsTzSchlrTbl.setTzState(TZ_STATE);
				PsTzSchlrTbl.setRowLastmantDttm(new java.util.Date());
				PsTzSchlrTbl.setRowLastmantOprid(userID);
				int i = psTzSchlrTblMapper.updateByPrimaryKeySelective(PsTzSchlrTbl);
				if (i > 0) {
					HashMap<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("TZ_SCHLR_ID", TZ_SCHLR_ID);
					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新奖学金失败！";
					return strRet;
				}
			} 
			errMsg[0] = "0";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
   
	
	
}
