package com.tranzvision.gd.TZProjectSetBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZProjectSetBundle.dao.PsTzPrjMajorTMapper;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjMajorT;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjMajorTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 张彬彬; 
 * 功能说明：专业方向页面;
 * 原PS类：TZ_GD_PROMG_PKG:TZ_GD_PRJ_ZYFX
 */
@Service("com.tranzvision.gd.TZProjectSetBundle.service.impl.tzProZyfxClsServiceImpl")
public class tzProZyfxClsServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzPrjMajorTMapper PsTzPrjMajorTMapper;
	
	/* 获取项目详情信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("projectId") && jacksonUtil.containsKey("professionId")) {
				// 项目分类编号;
				String strProjectId = jacksonUtil.getString("projectId");
				String strProfessionId = jacksonUtil.getString("professionId");
				
				PsTzPrjMajorTKey psTzPrjMajorTKey = new PsTzPrjMajorTKey();
				psTzPrjMajorTKey.setTzPrjId(strProjectId);
				psTzPrjMajorTKey.setTzMajorId(strProfessionId);
				PsTzPrjMajorT psTzPrjMajorT = PsTzPrjMajorTMapper.selectByPrimaryKey(psTzPrjMajorTKey);
				if (psTzPrjMajorT != null) {
					returnJsonMap.put("projectId", psTzPrjMajorT.getTzPrjId());
					returnJsonMap.put("pro_zyfx_id", psTzPrjMajorT.getTzMajorId());
					returnJsonMap.put("pro_zyfx_name", psTzPrjMajorT.getTzMajorName());
					returnJsonMap.put("sortNum", String.valueOf(psTzPrjMajorT.getTzSortNum()));
				} else {
					errMsg[0] = "1";
					errMsg[1] = "专业方向不存在.";
				}
				
			} else {
				errMsg[0] = "1";
				errMsg[1] = "专业方向不存在.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	/**
	 * 添加专业方向
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strPojectId = String.valueOf(jacksonUtil.getString("projectId"));
				String strMajorId = String.valueOf(jacksonUtil.getString("pro_zyfx_id")).toUpperCase();
				String strMajorName = String.valueOf(jacksonUtil.getString("pro_zyfx_name"));
				int sortNum = Integer.parseInt(String.valueOf(jacksonUtil.getString("sortNum")));
				/*项目名称是否已经存在*/
				String sql = "SELECT count(1) FROM PS_TZ_PRJ_MAJOR_T WHERE TZ_PRJ_ID= ? AND TZ_MAJOR_ID = ?";

				int isExistPrjMajorNum = sqlQuery.queryForObject(sql, new Object[] { strPojectId,strMajorId }, "Integer");
				
				if (isExistPrjMajorNum == 0) {
					PsTzPrjMajorT psTzPrjMajorT = new PsTzPrjMajorT();
					psTzPrjMajorT.setTzPrjId(strPojectId);
					psTzPrjMajorT.setTzMajorId(strMajorId);
					psTzPrjMajorT.setTzMajorName(strMajorName);
					psTzPrjMajorT.setTzSortNum(sortNum);
					PsTzPrjMajorTMapper.insert(psTzPrjMajorT);	
				}else{
					PsTzPrjMajorT psTzPrjMajorT = new PsTzPrjMajorT();
					psTzPrjMajorT.setTzPrjId(strPojectId);
					psTzPrjMajorT.setTzMajorId(strMajorId);
					psTzPrjMajorT.setTzMajorName(strMajorName);
					psTzPrjMajorT.setTzSortNum(sortNum);
					PsTzPrjMajorTMapper.updateByPrimaryKeySelective(psTzPrjMajorT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	/**
	 * 更新专业方向信息
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

				String strPojectId = String.valueOf(jacksonUtil.getString("projectId"));
				String strMajorId = String.valueOf(jacksonUtil.getString("pro_zyfx_id")).toUpperCase();
				String strMajorName = String.valueOf(jacksonUtil.getString("pro_zyfx_name"));
				int sortNum = Integer.parseInt(String.valueOf(jacksonUtil.getString("sortNum")));
				/*项目名称是否已经存在*/
				String sql = "SELECT count(1) FROM PS_TZ_PRJ_MAJOR_T WHERE TZ_PRJ_ID= ? AND TZ_MAJOR_ID = ?";

				int isExistPrjMajorNum = sqlQuery.queryForObject(sql, new Object[] { strPojectId,strMajorId }, "Integer");
				
				if (isExistPrjMajorNum == 0) {
					
					PsTzPrjMajorT psTzPrjMajorT = new PsTzPrjMajorT();
					psTzPrjMajorT.setTzPrjId(strPojectId);
					psTzPrjMajorT.setTzMajorId(strMajorId);
					psTzPrjMajorT.setTzMajorName(strMajorName);
					psTzPrjMajorT.setTzSortNum(sortNum);
					PsTzPrjMajorTMapper.insert(psTzPrjMajorT);
					
				}else{
					PsTzPrjMajorT psTzPrjMajorT = new PsTzPrjMajorT();
					psTzPrjMajorT.setTzPrjId(strPojectId);
					psTzPrjMajorT.setTzMajorId(strMajorId);
					psTzPrjMajorT.setTzMajorName(strMajorName);
					psTzPrjMajorT.setTzSortNum(sortNum);
					PsTzPrjMajorTMapper.updateByPrimaryKeySelective(psTzPrjMajorT);
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
