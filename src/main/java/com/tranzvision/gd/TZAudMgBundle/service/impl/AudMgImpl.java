package com.tranzvision.gd.TZAudMgBundle.service.impl;

import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.Map;

	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAudMgBundle.dao.PsTzAudDefnTMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
	import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
	
	import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqComzcTblMapper;
	
	import com.tranzvision.gd.util.base.JacksonUtil;
	import com.tranzvision.gd.util.sql.SqlQuery;

	/**
	 * 功能说明：听众管理相关类
	 * @author 顾贤达
	 * 2017-1-19
	 * 
	 */
	@Service("com.tranzvision.gd.TZAudMgBundle.service.impl.AudMgImpl")
	public class AudMgImpl extends FrameworkImpl {
		@Autowired
		private SqlQuery jdbcTemplate;
		@Autowired
		private PsTzAqComzcTblMapper psTzAqComzcTblMapper;
		@Autowired
		private PsTzAudDefnTMapper psTzAudDefnTMapper;
		@Autowired
		private FliterForm fliterForm;
		
		
		/* 查询听众管理列表 */
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
				// 排序字段如果没有不要赋值
				String[][] orderByArr = new String[][] { { "TZ_AUD_ID", "ASC" } };

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_AUD_NAM", "TZ_AUD_STAT","TZ_AUD_TYPE","TZ_AUD_ID","TZ_AUD_MS","TZ_AUD_SQL" };

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
				if (obj != null && obj.length > 0) {
					
					
					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("audName", rowList[0]);
						mapList.put("audType", rowList[1]);
						mapList.put("audStat", rowList[2]);
						mapList.put("audId", rowList[3]);
						mapList.put("audMS", rowList[4]);
						mapList.put("audSQL", rowList[5]);
						
						listData.add(mapList);
					}

					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);

				}

			} catch (Exception e) {
				e.printStackTrace();
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
			
			return jacksonUtil.Map2json(mapRet);
		}

		/* 删除组件注册信息 */
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
					// 组件ID;
					
				//	String sComID = jacksonUtil.getString("comID");
					String strAudID = jacksonUtil.getString("audId");
					psTzAudDefnTMapper.deleteByPrimaryKey(strAudID);
					
					String comPageSql = "DELETE FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_ID=?";
					jdbcTemplate.update(comPageSql,new Object[]{strAudID});		
					
				}
			} catch (Exception e) {
				errMsg[0] = "1";
				errMsg[1] = e.toString();
				return strRet;
			}

			return strRet;
		}
	}