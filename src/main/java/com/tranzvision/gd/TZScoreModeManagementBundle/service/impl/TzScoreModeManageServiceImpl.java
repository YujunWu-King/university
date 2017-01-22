package com.tranzvision.gd.TZScoreModeManagementBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzRsModalTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTbl;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzRsModalTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzScoreModeManageServiceImpl")
public class TzScoreModeManageServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Autowired
	private PsTzRsModalTblMapper psTzRsModalTblMapper;
	

	/**
	 * 成绩模型管理列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			//TZ_SCORE_MOD_COM.TZ_SCORE_MG_STD.TZ_RS_MODAL_TBL
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_JG_ID", "TZ_SCORE_MODAL_ID", "TZ_MODAL_NAME", "TREE_NAME", "TZ_MODAL_FLAG"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				int num = list.size();
				for (int i = 0; i < num; i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("orgId", rowList[0]);
					mapList.put("modeId", rowList[1]);
					mapList.put("modeDesc", rowList[2]);
					mapList.put("treeName", rowList[3]);
					
					//状态取值
					String status = rowList[4];
					String sql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_ISVALID' AND TZ_ZHZ_ID=?";
					status = jdbcTemplate.queryForObject(sql, new Object[]{status}, "String");
					
					mapList.put("status", status);

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
	
	
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRtn = "";
		Map<String, Object> mapForm = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String orgId = jacksonUtil.getString("orgId");
			String modeId = jacksonUtil.getString("modeId");

			if (null != orgId && !"".equals(orgId) && null != modeId && !"".equals(modeId)) {
				PsTzRsModalTblKey psTzRsModalTblKey = new PsTzRsModalTblKey();
				psTzRsModalTblKey.setTzJgId(orgId);
				psTzRsModalTblKey.setTzScoreModalId(modeId);
				PsTzRsModalTbl psTzRsModalTbl = psTzRsModalTblMapper.selectByPrimaryKey(psTzRsModalTblKey);
				if(psTzRsModalTbl != null){
					String modeDesc = psTzRsModalTbl.getTzModalName();
					String treeName = psTzRsModalTbl.getTreeName()==null ? "" : psTzRsModalTbl.getTreeName();
					String status = psTzRsModalTbl.getTzModalFlag();
					String totalScoreModel = psTzRsModalTbl.getTzMFbdzId()==null ? "" : psTzRsModalTbl.getTzMFbdzId();
					
					String sql = "SELECT TZ_JG_NAME FROM PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
					String orgName = jdbcTemplate.queryForObject(sql, new Object[]{orgId}, "String");
					
					String addOprid = psTzRsModalTbl.getRowAddedOprid();
					String updateOprid = psTzRsModalTbl.getRowLastmantOprid();
					
					sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
					String addOprName = jdbcTemplate.queryForObject(sql, new Object[]{addOprid}, "String");
					String updateOprName = jdbcTemplate.queryForObject(sql, new Object[]{updateOprid}, "String");
					
					Date addDate = psTzRsModalTbl.getRowAddedDttm();
					Date updateDate = psTzRsModalTbl.getRowLastmantDttm();
					
					String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
					SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
					String addoprDttm =  dttmSimpleDateFormat.format(addDate);
					String updateOprDttm =  dttmSimpleDateFormat.format(updateDate);
					
					mapForm.put("orgId", orgId);
					mapForm.put("modelId", modeId);
					mapForm.put("modeName", modeDesc);
					mapForm.put("status", status);
					mapForm.put("treeName", treeName);
					mapForm.put("totalScoreModel", totalScoreModel);
					mapForm.put("orgDesc", orgName);
					mapForm.put("addOprName", addOprName);
					mapForm.put("addoprDttm", addoprDttm);
					mapForm.put("updateOprName", updateOprName);
					mapForm.put("updateOprDttm", updateOprDttm);
				}
			}else{
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRtn = jacksonUtil.Map2json(mapForm);
		return strRtn;
	}
}
