package com.tranzvision.gd.TZLabelSetBundle.service.impl;

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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLabelSetBundle.dao.PsTzLabelDfnTMapper;
import com.tranzvision.gd.TZLabelSetBundle.model.PsTzLabelDfnT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * @author  WRL TZ_GD_BMGL_TAG_PKG:TZ_GD_TAG_LIST_CLS
 * 标签管理
 *
 */
@Service("com.tranzvision.gd.TZLabelSetBundle.service.impl.LabelListClsServiceImpl")
public class LabelListClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzLabelDfnTMapper psTzLabelDfnTMapper;
	/**
	 * 展示 列表显示
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "{}";
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if(StringUtils.isBlank(orgId)){
			errorMsg[0] = "1";
			errorMsg[1] = "您不属于任何机构！";
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		try {
			
			//列表SQL
			String sql = "";
			//查询结果
			List<?> resultlist = null;
			
			if(numLimit == 0){
				sql = "SELECT TZ_LABEL_ID, TZ_LABEL_NAME, TZ_LABEL_DESC FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID = ? AND TZ_LABEL_STATUS <> 'N' ORDER BY TZ_LABEL_ID";
				resultlist = sqlQuery.queryForList(sql, new Object[] { orgId });
			}else{
				sql = "SELECT TZ_LABEL_ID,TZ_LABEL_NAME,TZ_LABEL_DESC FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID = ? AND TZ_LABEL_STATUS <> 'N' ORDER BY TZ_LABEL_ID LIMIT ?,?";
				resultlist = sqlQuery.queryForList(sql, new Object[] { orgId, numStart, numStart + numLimit });
			}
			
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String tagId = result.get("TZ_LABEL_ID") == null ? "" : String.valueOf(result.get("TZ_LABEL_ID"));
				String tagName = result.get("TZ_LABEL_NAME") == null ? "" : String.valueOf(result.get("TZ_LABEL_NAME"));
				String tagDesc = result.get("TZ_LABEL_DESC") == null ? "" : String.valueOf(result.get("TZ_LABEL_DESC"));

				Map<String, Object> mapRetJson = new HashMap<String, Object>();
				mapRetJson.put("tagId", tagId);
				mapRetJson.put("tagName", tagName);
				mapRetJson.put("tagDesc", tagDesc);
				
				listData.add(mapRetJson);
			}
		
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID = ? AND TZ_LABEL_STATUS <> 'N'";
			int numTotal = sqlQuery.queryForObject(totalSQL, new Object[] { orgId}, "Integer");
			
			mapRet.replace("total", numTotal);
			mapRet.replace("root", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "数据异常，请重试！";
		}

		return jacksonUtil.Map2json(mapRet);

	}
	
	
	/**
	 * 新增或修改类别信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if(StringUtils.isBlank(orgId)){
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能添加标签！";
			return strRet;
		}
		
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			Date dateNow = new Date();
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				Map<String, Object> infoData = jacksonUtil.getMap("data");
				String tagId = (String)infoData.get("tagId");
				String tagName = (String)infoData.get("tagName");
				String tagDesc = (String)infoData.get("tagDesc");
		
				if(StringUtils.equals(tagId, "NEXT")){
					
					String sql = "SELECT COUNT(1) FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID = ? AND TZ_LABEL_NAME = ? AND TZ_LABEL_STATUS<>'N'";
					int count = sqlQuery.queryForObject(sql, new Object[] { orgId,tagName }, "Integer");

					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前部门存在有相同名称标签定义!";
						return strRet;
					}else{
						PsTzLabelDfnT psTzLabelDfnT = new PsTzLabelDfnT();
						tagId = "" + getSeqNum.getSeqNum("PS_TZ_LABEL_DFN_T", "TZ_LABEL_ID");
						StringUtils.leftPad(tagId,5,'0');
						psTzLabelDfnT.setTzLabelId(tagId);
						psTzLabelDfnT.setTzLabelName(tagName);
						psTzLabelDfnT.setTzLabelDesc(tagDesc);
						psTzLabelDfnT.setTzLabelStatus("Y");
						psTzLabelDfnT.setTzJgId(orgId);
						psTzLabelDfnT.setRowAddedDttm(dateNow);
						psTzLabelDfnT.setRowAddedOprid(oprid);
						psTzLabelDfnT.setRowLastmantDttm(dateNow);
						psTzLabelDfnT.setRowLastmantOprid(oprid);
						
						psTzLabelDfnTMapper.insert(psTzLabelDfnT);
					}
				}else{
					String sql = "SELECT COUNT(1) FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID = ? AND TZ_LABEL_NAME = ? AND TZ_LABEL_ID <> ? AND TZ_LABEL_STATUS <> 'N'";
					int count = sqlQuery.queryForObject(sql, new Object[] { orgId,tagName,tagId }, "Integer");
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "当前部门存在有相同名称标签定义!";
						return strRet;
					}else{
						PsTzLabelDfnT psTzLabelDfnT = new PsTzLabelDfnT();
						psTzLabelDfnT.setTzLabelId(tagId);
						psTzLabelDfnT.setTzLabelName(tagName);
						psTzLabelDfnT.setTzLabelDesc(tagDesc);
						psTzLabelDfnT.setRowLastmantDttm(dateNow);
						psTzLabelDfnT.setRowLastmantOprid(oprid);
						
						psTzLabelDfnTMapper.updateByPrimaryKeySelective(psTzLabelDfnT);
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
	
	/***
	 * 删除标签信息
	 */
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
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 标签id
				String tagId = jacksonUtil.getString("tagId");
				
				if(StringUtils.isNotBlank(tagId) && !StringUtils.equals(tagId, "NEXT")){
					sqlQuery.update("UPDATE PS_TZ_LABEL_DFN_T SET TZ_LABEL_STATUS = 'N' WHERE TZ_LABEL_ID = ?", new Object[] { tagId });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
