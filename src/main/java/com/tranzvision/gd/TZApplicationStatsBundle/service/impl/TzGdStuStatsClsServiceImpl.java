package com.tranzvision.gd.TZApplicationStatsBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationStatsBundle.dao.PsTzStatsProjectTMapper;
import com.tranzvision.gd.TZApplicationStatsBundle.dao.PsTzStuStatsTMapper;
import com.tranzvision.gd.TZApplicationStatsBundle.model.PsTzStatsProjectT;
import com.tranzvision.gd.TZApplicationStatsBundle.model.PsTzStatsProjectTKey;
import com.tranzvision.gd.TZApplicationStatsBundle.model.PsTzStuStatsT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author WRL 报名管理-学生报考多项目统计
 */
@Service("com.tranzvision.gd.TZApplicationStatsBundle.service.impl.TzGdStuStatsClsServiceImpl")
public class TzGdStuStatsClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PsTzStuStatsTMapper psTzStuStatsTMapper;
	
	@Autowired
	private PsTzStatsProjectTMapper psTzStatsProjectTMapper;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/* 新增考生统计信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
		Date dateNow = new Date();
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
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				// 待统计学生所属项目;
				String tzClassId = (String) infoData.get("projectName");
				Long tzStatsId = Long.parseLong("" + getSeqNum.getSeqNum("PS_TZ_STU_STATS_T", "TZ_STATS_ID"));
				PsTzStuStatsT psTzStuStatsT = new PsTzStuStatsT();
				psTzStuStatsT.setTzStatsId(tzStatsId);
				psTzStuStatsT.setTzClassId(tzClassId);
				psTzStuStatsT.setTzJgId(orgId);
				psTzStuStatsT.setRowAddedDttm(dateNow);
				psTzStuStatsT.setRowAddedOprid(oprId);
				psTzStuStatsT.setRowLastmantDttm(dateNow);
				psTzStuStatsT.setRowLastmantOprid(oprId);


				int i = psTzStuStatsTMapper.insert(psTzStuStatsT);
				if(i <= 0){
					errMsg[0] = "1";
					errMsg[1] = "保存失败";
				}else{
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("statsId", tzStatsId);
					strRet = jacksonUtil.Map2json(mapJson);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	/* 修改统计信息 */
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
			String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
			Date dateNow = new Date();
			
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
		        
				if ("CLS".equals(strFlag)) {
					// 统计编号;
			        String statsId = infoData.get("statsId") == null ? "0" : String.valueOf(infoData.get("statsId"));
					//班级编号
			        String classId = infoData.get("classId") == null ? "" : String.valueOf(infoData.get("classId"));
					//排序顺序
			        String strOrder = infoData.get("order") == null ? "0" : String.valueOf(infoData.get("order"));
					
					int order = 0;
					if(StringUtils.isNotBlank(strOrder)){
						order = Integer.valueOf(strOrder);
					}
					String sql = "SELECT 'Y' FROM PS_TZ_STATS_PROJECT_T WHERE TZ_STATS_ID = ? AND TZ_CLASS_ID = ?";
					String isHas = sqlQuery.queryForObject(sql, new Object[] { statsId,classId}, "String");
					
					PsTzStatsProjectT psTzStatsProjectT = new PsTzStatsProjectT();
					psTzStatsProjectT.setTzClassId(classId);
					psTzStatsProjectT.setTzStatsId(Long.valueOf(statsId));
					psTzStatsProjectT.setTzOrder(order);
					int i = -1;
					if(StringUtils.equals("Y", isHas)){
						i = psTzStatsProjectTMapper.updateByPrimaryKeySelective(psTzStatsProjectT);
					}else{
						i = psTzStatsProjectTMapper.insert(psTzStatsProjectT);
					}
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}
				}

				if ("STATS".equals(strFlag)) {

					String statsId = infoData.get("statsId") == null ? "0" : String.valueOf(infoData.get("statsId"));
					String tzClassId = infoData.get("projectName") == null ? "" : String.valueOf(infoData.get("projectName"));
					
					PsTzStuStatsT psTzStuStatsT = new PsTzStuStatsT();
					psTzStuStatsT.setTzStatsId(Long.valueOf(statsId));
					psTzStuStatsT.setTzClassId(tzClassId);
					psTzStuStatsT.setRowLastmantDttm(dateNow);
					psTzStuStatsT.setRowLastmantOprid(oprId);

					int i = psTzStuStatsTMapper.updateByPrimaryKeySelective(psTzStuStatsT);
					if(i <= 0){
						errMsg[0] = "1";
						errMsg[1] = "更新失败";
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	/* 删除统计项目信息列表 */
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
				
				// 统计编号;
		        Long statsId = jacksonUtil.getLong("statsId");
				// 班级编号
		        String classId = jacksonUtil.getString("classId");
		        PsTzStatsProjectTKey psTzStatsProjectTKey = new PsTzStatsProjectTKey();
		        psTzStatsProjectTKey.setTzClassId(classId);
		        psTzStatsProjectTKey.setTzStatsId(statsId);
		        
		        int i = psTzStatsProjectTMapper.deleteByPrimaryKey(psTzStatsProjectTKey);
				if(i <= 0){
					errMsg[0] = "1";
					errMsg[1] = "更新失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	@Override
	public String tzGetJsonData(String strParams) {
		String strRet = "{}";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String oType = jacksonUtil.getString("type");

		if (StringUtils.isBlank(oType)) {
			return strRet;
		}
		/* 班级列表 */
		if (StringUtils.equals("PNAME", oType)) {

			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			/*获取当前机构下，最后一次统计记录*/
			String sqlClass = "SELECT TZ_STATS_ID,TZ_CLASS_ID FROM PS_TZ_STU_STATS_T WHERE TZ_JG_ID = ? ORDER BY ROW_LASTMANT_DTTM DESC LIMIT 0,1";

			Map<String, Object> statsMap = sqlQuery.queryForMap(sqlClass, new Object[] { orgId });
			String statsId = "";
			String classid = "";
			if(statsMap != null){
				statsId = statsMap.get("TZ_STATS_ID") == null ? "0" : String.valueOf(statsMap.get("TZ_STATS_ID"));
				classid = statsMap.get("TZ_CLASS_ID") == null ? "" : String.valueOf(statsMap.get("TZ_CLASS_ID"));
			}

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("statsId", statsId);
			mapRet.put("projectName", classid);

			return jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}
	
	/* 查询统计列表 */
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
			jacksonUtil.json2Map(comParams);
			// 统计编号
			String statsId = jacksonUtil.getString("statsId");
			
			String sql = "SELECT PRO.TZ_ORDER,PRO.TZ_CLASS_ID,CLA.TZ_CLASS_NAME,CLA.TZ_IS_APP_OPEN FROM PS_TZ_STATS_PROJECT_T PRO LEFT JOIN PS_TZ_CLASS_INF_T CLA ON PRO.TZ_CLASS_ID = CLA.TZ_CLASS_ID WHERE TZ_STATS_ID = ? ORDER BY PRO.TZ_ORDER";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { statsId });
			if(resultlist == null){
				return jacksonUtil.Map2json(mapRet);
			}
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;
				String order = result.get("TZ_ORDER") == null ? "" : String.valueOf(result.get("TZ_ORDER"));
				String classId = result.get("TZ_CLASS_ID") == null ? "" : String.valueOf(result.get("TZ_CLASS_ID"));
				String className = result.get("TZ_CLASS_NAME") == null ? "" : String.valueOf(result.get("TZ_CLASS_NAME"));
				String classStatus = result.get("TZ_IS_APP_OPEN") == null ? "" : String.valueOf(result.get("TZ_IS_APP_OPEN"));
				
				if(StringUtils.isBlank(classStatus)){
					classStatus = "N";
				}
		        
				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("statsId", statsId);
				mapRetJson.put("classId", classId);
				mapRetJson.put("className", className);
				mapRetJson.put("classStatus", classStatus);
				mapRetJson.put("order", order);

				listData.add(mapRetJson);
			}

			mapRet.replace("total", resultlist.size());
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
}
