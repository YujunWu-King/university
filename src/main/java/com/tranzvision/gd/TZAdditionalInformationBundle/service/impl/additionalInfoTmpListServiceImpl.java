package com.tranzvision.gd.TZAdditionalInformationBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAdditionalInformationBundle.dao.PsTzSbmInfTmpTMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
 
/**
 * 
 * @author 张彬彬; 
 * 功能说明：查询递交资料模型列表;
 * 原PS类：TZ_GD_SMTDTMG_PKG:TZ_GD_data1_CLS
 */
@Service("com.tranzvision.gd.TZAdditionalInformationBundle.service.impl.additionalInfoTmpListServiceImpl")
public class additionalInfoTmpListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzSbmInfTmpTMapper PsTzSbmInfTmpTMapper;
	/* 查询项目流程配置模版列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_SBMINF_TMP_ID", "TZ_SBMINF_TMP_NAME", "TZ_SBMINF_STATUS" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr,comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("smtDtTmpID", rowList[0]);
				mapList.put("smtDtName", rowList[1]);
				mapList.put("smtDtStatus", rowList[2]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/**
	 * 删除补充资料流程模版和模版相关信息
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 流程模版编号;
				String strAddilitionInfoTmpId = jacksonUtil.getString("smtDtTmpID");
				
				if (strAddilitionInfoTmpId != null && !"".equals(strAddilitionInfoTmpId)) {
					PsTzSbmInfTmpTMapper.deleteByPrimaryKey(strAddilitionInfoTmpId);
					/*同时删除对应的常用回复短语信息和补充资料信息*/
					Object[] args = new Object[] { strAddilitionInfoTmpId };
					sqlQuery.update("DELETE FROM PS_TZ_SBMINF_STP_T WHERE TZ_SBMINF_TMP_ID = ?", args);
					sqlQuery.update("DELETE FROM PS_TZ_SBMINF_REP_T WHERE TZ_SBMINF_TMP_ID = ?", args);
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
