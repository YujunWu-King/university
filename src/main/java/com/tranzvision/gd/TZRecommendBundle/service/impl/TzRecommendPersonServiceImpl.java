package com.tranzvision.gd.TZRecommendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * 推荐人管理列表页
 * 
 * @author 
 * @since 
 */
@Service("com.tranzvision.gd.TZRecommendBundle.service.impl.TzRecommendPersonServiceImpl")
public class TzRecommendPersonServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private FliterForm fliterForm;
	
	// 获取班级信息
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("classID")&&jacksonUtil.containsKey("batchID")) {
				// 班级编号;
				String strClassID = jacksonUtil.getString("classID");
				// 批次编号;
				String strBatchID = jacksonUtil.getString("batchID");
				// 班级名称，报名表模板编号，批次名称;
				String strClassName = "", strAppModalID = "",strBatchName = "";

				// 获取班级名称，报名表模板ID，批次名称;
				String sql = "SELECT A.TZ_CLASS_NAME,A.TZ_APP_MODAL_ID,B.TZ_BATCH_NAME FROM PS_TZ_CLASS_INF_T A INNER JOIN PS_TZ_CLS_BATCH_T B ON(A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_BATCH_ID=?) WHERE A.TZ_CLASS_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strBatchID,strClassID });
				if (map != null) {
					strClassName = (String) map.get("TZ_CLASS_NAME");
					strAppModalID = (String) map.get("TZ_APP_MODAL_ID");
					strBatchName = (String) map.get("TZ_BATCH_NAME");
					Map<String, Object> hMap = new HashMap<>();
					hMap.put("classID", strClassID);
					hMap.put("className", strClassName);
					hMap.put("modalID", strAppModalID);
					hMap.put("batchID", strBatchID);
					hMap.put("batchName", strBatchName);
					returnJsonMap.replace("formData", hMap);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择报考方向和批次";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		
		try {
			// 排序字段
			String[][] orderByArr = new String[][] { new String[] { "TZ_CLASS_ID", "DESC" }};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_CLASS_ID","TZ_BATCH_ID","TZ_REFERRER_NAME","TZ_COMP_CNAME","POSITION","TZ_EMAIL","TZ_PHONE","OLD_BOY",
					"TZ_REALNAME","TZ_MSSQH","TZ_APP_FORM_STA","TZ_APP_INS_ID","TZ_REF_LETTER_ID","OPRID"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classId", rowList[0]);
					mapList.put("batchId", rowList[1]);
					mapList.put("referrerName", rowList[2]);
					mapList.put("companyName", rowList[3]);
					mapList.put("position", rowList[4]);
					mapList.put("email", rowList[5]);
					mapList.put("phone", rowList[6]);
					mapList.put("oldBoy", rowList[7]);
					mapList.put("stuName", rowList[8]);
					mapList.put("applyNo", rowList[9]);
					mapList.put("letterState", rowList[10]);
					mapList.put("appInsID", rowList[11]);
					mapList.put("letterID", rowList[12]);
					mapList.put("oprID", rowList[13]);
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}
}
