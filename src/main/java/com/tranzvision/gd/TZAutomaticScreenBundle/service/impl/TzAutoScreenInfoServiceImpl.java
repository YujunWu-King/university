package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormLabelTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormLabelTKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;


/**
 * 自动初筛详情
 * @author zhanglang
 * 2017-02-16
 */
@Service("com.tranzvision.gd.TZAutomaticScreenBundle.service.impl.TzAutoScreenInfoServiceImpl")
public class TzAutoScreenInfoServiceImpl extends FrameworkImpl{

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal; 
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzCsKsTblMapper psTzCsKsTblMapper;
	
	@Autowired
	private PsTzFormLabelTMapper psTzFormLabelTMapper;
	
	
	
	@Override
	public String tzQuery(String strParams, String[] errorMsg) {
		String strRtn = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appId = jacksonUtil.getString("appId");
			
			String sql = "select TZ_KSH_CSJG,TZ_KSH_PSPM,ROW_LASTMANT_OPRID,ROW_LASTMANT_DTTM from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?;";
			Map<String,Object> csKsMap = jdbcTemplate.queryForMap(sql, new Object[]{ classId,batchId,appId });
			if(csKsMap != null){
				String status = csKsMap.get("TZ_KSH_CSJG").toString();
				String ranking = csKsMap.get("TZ_KSH_PSPM").toString();
				String updateOpr = csKsMap.get("ROW_LASTMANT_OPRID") == null ? "" 
						: csKsMap.get("ROW_LASTMANT_OPRID").toString();
				
				mapRet.put("classId", classId);
				mapRet.put("batchId", batchId);
				mapRet.put("appId", appId);
				mapRet.put("status", status);
				mapRet.put("ranking", ranking);
				mapRet.put("updateOpr", updateOpr);
				
				String dttmFormat = getSysHardCodeVal.getDateTimeFormat();
				SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
				
				if(csKsMap.get("ROW_LASTMANT_DTTM") != null){
					Date updateDate = (Date) csKsMap.get("ROW_LASTMANT_DTTM");
					mapRet.put("updateDttm", dttmSimpleDateFormat.format(updateDate));
				}
			}
			
			//自动标签
			int i = 0;
			sql = "select TZ_ZDBQ_ID from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
			List<Map<String,Object>> ksbqList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
			String [] ksbqArr = new String[ksbqList.size()];
			for(Map<String,Object> ksbqMap : ksbqList){
				ksbqArr[i] = ksbqMap.get("TZ_ZDBQ_ID").toString();
				i++;
			}
			mapRet.put("autoLabel", ksbqArr);
			
			
			//负面清单
			sql = "select TZ_FMQD_ID from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
			List<Map<String,Object>> fmqdList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
			String [] fmbqArr = new String[fmqdList.size()];
			i = 0;
			for(Map<String,Object> fmbqMap : fmqdList){
				fmbqArr[i] = fmbqMap.get("TZ_FMQD_ID").toString();
				i++;
			}
			mapRet.put("negativeList", fmbqArr);
			
			
			//手工标签
			sql = "select TZ_LABEL_ID from PS_TZ_FORM_LABEL_T where TZ_APP_INS_ID=?";
			List<Map<String,Object>> labelList = jdbcTemplate.queryForList(sql, new Object[]{ appId });
			String [] labelArr = new String[labelList.size()];
			i = 0;
			for(Map<String,Object> labelMap: labelList){
				if(labelMap.get("TZ_LABEL_ID") != null){
					labelArr[i] = labelMap.get("TZ_LABEL_ID").toString();
					i++;
				}
			}
			mapRet.put("manualLabel", labelArr);
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
	
	
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String appId = jacksonUtil.getString("appId");
			String queryType = jacksonUtil.getString("queryType");
			
			String sql;
			int count = 0;
			Map<String,Object> bqMap = null;
			ArrayList<Map<String, Object>> rootList = new ArrayList<Map<String, Object>>(); 
			if("KSBQ".equals(queryType)){
				sql = "select TZ_ZDBQ_ID,(select TZ_BIAOQZ_NAME from PS_TZ_BIAOQZ_BQ_T where TZ_BIAOQ_ID=TZ_ZDBQ_ID limit 1) as TZ_BIAOQZ_NAME from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
				List<Map<String,Object>> ksbqList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
				for(Map<String,Object> ksbqMap : ksbqList){
					count++;
					String id = ksbqMap.get("TZ_ZDBQ_ID").toString();
					String desc = ksbqMap.get("TZ_BIAOQZ_NAME") == null ? id : ksbqMap.get("TZ_BIAOQZ_NAME").toString();
					
					bqMap = new HashMap<String, Object>();
					bqMap.put("id", id);
					bqMap.put("desc", desc);
					
					rootList.add(bqMap);
				}
				
			}else if("FMQD".equals(queryType)){
				sql = "select TZ_FMQD_ID,(select TZ_BIAOQZ_NAME from PS_TZ_BIAOQZ_BQ_T where TZ_BIAOQ_ID=TZ_FMQD_ID limit 1) as TZ_BIAOQZ_NAME from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
				List<Map<String,Object>> fmqdList = jdbcTemplate.queryForList(sql, new Object[]{ classId,batchId,appId });
				for(Map<String,Object> ksbqMap : fmqdList){
					count++;
					String id = ksbqMap.get("TZ_FMQD_ID").toString();
					String desc = ksbqMap.get("TZ_BIAOQZ_NAME") == null ? id : ksbqMap.get("TZ_BIAOQZ_NAME").toString();
					
					bqMap = new HashMap<String, Object>();
					bqMap.put("id", id);
					bqMap.put("desc", desc);
					
					rootList.add(bqMap);
				}
			}
			
			mapRet.replace("total", count);
			mapRet.replace("root", rootList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String classId = jacksonUtil.getString("classId");
				String batchId = jacksonUtil.getString("batchId");
				Long appId = Long.valueOf(jacksonUtil.getString("appId"));
				
				String status = jacksonUtil.getString("status");
				//String manualLabel = jacksonUtil.getString("manualLabel");
				List<String> manualLabelList = (List<String>) jacksonUtil.getList("manualLabel");
				
				
				PsTzCsKsTblKey psTzCsKsTblKey = new PsTzCsKsTblKey();
				psTzCsKsTblKey.setTzClassId(classId);
				psTzCsKsTblKey.setTzApplyPcId(batchId);
				psTzCsKsTblKey.setTzAppInsId(appId);
				PsTzCsKsTbl psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);
				
				if(psTzCsKsTbl != null){
					Date currDate = new Date();
					psTzCsKsTbl.setTzKshCsjg(status);
					psTzCsKsTbl.setRowLastmantDttm(currDate);
					psTzCsKsTbl.setRowLastmantOprid(oprid);
					psTzCsKsTblMapper.updateByPrimaryKey(psTzCsKsTbl);
					
					Map<String,Object> formMap = new HashMap<String,Object>();
					
					String dttmFormat = getSysHardCodeVal.getDateTimeFormat();
					SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
					formMap.put("updateDttm", dttmSimpleDateFormat.format(currDate));
					formMap.put("updateOpr", oprid);
					
					mapRet.replace("formData", formMap);
				}
				
				
				String delSql = "delete from PS_TZ_FORM_LABEL_T where TZ_APP_INS_ID=?";
				jdbcTemplate.update(delSql, new Object[]{ appId });
				for(String label : manualLabelList){
					PsTzFormLabelTKey psTzFormLabelTKey = new PsTzFormLabelTKey();
					psTzFormLabelTKey.setTzAppInsId(appId);
					psTzFormLabelTKey.setTzLabelId(label);
					
					psTzFormLabelTMapper.insert(psTzFormLabelTKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
}
