package com.tranzvision.gd.TZLabelSetBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
 * @author  WRL TZ_GD_BMGL_TAG_PKG:TZ_GD_TAG_BJ_CLS
 * 标签定义
 *
 */
@Service("com.tranzvision.gd.TZLabelSetBundle.service.impl.LabelInfoClsServiceImpl")
public class LabelInfoClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzLabelDfnTMapper psTzLabelDfnTMapper;
	
	/**
	 * 获取标签定义信息
	 * 
	 * @param comParams
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzQuery(String comParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if (jacksonUtil.containsKey("tagId")) {

				String tagId = jacksonUtil.getString("tagId");
				
				PsTzLabelDfnT psTzLabelDfnT = psTzLabelDfnTMapper.selectByPrimaryKey(tagId);
				
				if (psTzLabelDfnT != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					
					mapData.put("tagId", psTzLabelDfnT.getTzLabelId());
					mapData.put("tagName", psTzLabelDfnT.getTzLabelName());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该标签数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/**
	 * 修改标签定义
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		
		if(StringUtils.isBlank(orgId)){
			errMsg[0] = "1";
			errMsg[1] = "您不属于任何机构，不能修改标签定义！";
			return strRet;
		}
		try {
			int dataLength = actData.length;
			Date dateNow = new Date();
			
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				String tagId = jacksonUtil.getString("tagId");
				String tagName = jacksonUtil.getString("tagName");
				
				if(StringUtils.equals(tagId, "NEXT")){
					
					String sql = "SELECT COUNT(1) FROM PS_TZ_LABEL_DFN_T WHERE TZ_JG_ID = ? AND TZ_LABEL_NAME = ? AND TZ_LABEL_STATUS <> 'N'";
					int count = sqlQuery.queryForObject(sql, new Object[] { orgId,tagName }, "Integer");

					if (count > 0) {
//						errMsg[0] = "1";
//						errMsg[1] = "当前部门存在有相同名称的标签定义。";
//						return strRet;
					}else{
						PsTzLabelDfnT psTzLabelDfnT = new PsTzLabelDfnT();
						tagId = "" + getSeqNum.getSeqNum("TZ_LABEL_DFN_T", "TZ_LABEL_ID");
						StringUtils.leftPad(tagId,5,'0');
						psTzLabelDfnT.setTzLabelId(tagId);
						psTzLabelDfnT.setTzLabelName(tagName);
						psTzLabelDfnT.setTzLabelDesc("");
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
}
