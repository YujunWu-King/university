package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZClassSetBundle.dao.PsTzClsHfdyTMapper;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsHfdyT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsHfdyTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_BKMSG_CLS
 * 
 * @author tang 报名管理-报名表审核
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglBkmsgClsServiceImpl")
public class TzGdBmglBkmsgClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzClsHfdyTMapper psTzClsHfdyTMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	
	/* 获取常用回复短语信息 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			
			// 班级编号;
			String strClassID = jacksonUtil.getString("classID");
			// 递交资料编号;
			String strFileID = jacksonUtil.getString("fileID");
			
			if(strClassID != null && !"".equals(strClassID)
					&& strFileID != null && !"".equals(strFileID)){
				String strRet = this.tzQueryBackMsgList(strClassID, strFileID, numLimit, numStart, errorMsg);
				return strRet;
			}
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	/*修改常用回复短语信息*/
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strRet;
		}

		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				
			    // 类型标志;
			    //String strFlag = jacksonUtil.getString("typeFlag");
			    // 信息内容;
			    Map<String, Object> infoData = jacksonUtil.getMap("data");
			      
			    strRet = this.tzEditBackMsgInfo(infoData, errMsg);
			}
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	/* *删除常用回复短语信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strReturn = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strReturn;
		}

		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 班级编号;
			    String strClassID = jacksonUtil.getString("classID");
			    // 递交资料编号;
			    String strFileID = jacksonUtil.getString("fileID");
			    // 消息ID;
			    String strMsgID = jacksonUtil.getString("msgID");
			    if(strClassID != null && !"".equals(strClassID)
			    	&& strFileID != null && !"".equals(strFileID)
			    	&& strMsgID != null && !"".equals(strMsgID)){
			    	jdbcTemplate.update("DELETE FROM PS_TZ_CLS_HFDY_T WHERE TZ_CLASS_ID=? AND TZ_SBMINF_ID=? AND TZ_SBMINF_REP_ID=?",new Object[]{strClassID, strFileID, strMsgID});
			    }
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strReturn;
	}
	
	/*修改常用回复短语信息*/
	public String tzEditBackMsgInfo(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			//班级编号;
		    String strClassID = (String)infoData.get("classID");
		    // 递交资料编号;
		    String strFileID = (String)infoData.get("fileID");
		    // 短语编号;
		    String strMsgID = (String)infoData.get("msgID");
		    // 短语内容;
		    String strMsgContent = (String)infoData.get("msgContent");
		    
		    PsTzClsHfdyTKey psTzClsHfdyTkey = new PsTzClsHfdyTKey();
		    psTzClsHfdyTkey.setTzClassId(strClassID);
		    psTzClsHfdyTkey.setTzSbminfId(strFileID);
		    psTzClsHfdyTkey.setTzSbminfRepId(strMsgID);
		    PsTzClsHfdyT psTzClsHfdyT = psTzClsHfdyTMapper.selectByPrimaryKey(psTzClsHfdyTkey);
		    if(psTzClsHfdyT != null){
		    	psTzClsHfdyT.setTzClassId(strClassID);
		    	psTzClsHfdyT.setTzSbminfId(strFileID);
		    	psTzClsHfdyT.setTzSbminfRepId(strMsgID);
		    	psTzClsHfdyT.setTzSbminfRep(strMsgContent);
		    	psTzClsHfdyTMapper.updateByPrimaryKeySelective(psTzClsHfdyT);
		    }else{
		    	psTzClsHfdyT = new PsTzClsHfdyT();
		    	psTzClsHfdyT.setTzClassId(strClassID);
		    	psTzClsHfdyT.setTzSbminfId(strFileID);
		    	strMsgID = "00000" + getSeqNum.getSeqNum("TZ_CLS_HFDY_T", "TZ_CLASS_ID" + strClassID);
		    	strMsgID = strMsgID.substring(strMsgID.length()-5, strMsgID.length());
		    	psTzClsHfdyT.setTzSbminfRepId(strMsgID);
		    	psTzClsHfdyT.setTzSbminfRep(strMsgContent);
		    	psTzClsHfdyTMapper.insert(psTzClsHfdyT);
		    }
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
				
		return strRet;
	}
	
	
	/*获取常用回复短语列表*/
	public String tzQueryBackMsgList(String strClassID, String strFileID, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String sql = "";
			List<Map<String, Object>> list ;
			if(numLimit > 0){
				sql = "SELECT TZ_SBMINF_REP_ID,TZ_SBMINF_REP FROM PS_TZ_CLS_HFDY_T WHERE TZ_CLASS_ID=? AND TZ_SBMINF_ID=? order by TZ_SBMINF_REP_ID ";
				list = jdbcTemplate.queryForList(sql,new Object[]{strClassID, strFileID});
			}else{
				sql = "SELECT TZ_SBMINF_REP_ID,TZ_SBMINF_REP FROM PS_TZ_CLS_HFDY_T WHERE TZ_CLASS_ID=? AND TZ_SBMINF_ID=? order by TZ_SBMINF_REP_ID limit ?,?";
				list = jdbcTemplate.queryForList(sql,new Object[]{strClassID, strFileID,numStart,numLimit});
			}
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					String strMsgID = (String)list.get(i).get("TZ_SBMINF_REP_ID");
					String strMsgContent = (String)list.get(i).get("TZ_SBMINF_REP");
					Map<String, Object> map = new HashMap<>();
					map.put("classID", strClassID);
					map.put("fileID", strFileID);
					map.put("msgID", strMsgID);
					map.put("msgContent", strMsgContent);
					listData.add(map);
					
				}
			}
			
			// 获取总数;
		    int numTotal = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_CLS_HFDY_T WHERE TZ_CLASS_ID=? AND TZ_SBMINF_ID=?", new Object[]{strClassID, strFileID},"Integer");
		    mapRet.replace("total", numTotal);
			mapRet.replace("root", listData);
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
}
