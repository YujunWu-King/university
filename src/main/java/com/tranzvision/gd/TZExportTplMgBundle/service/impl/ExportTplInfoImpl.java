package com.tranzvision.gd.TZExportTplMgBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZExportTplMgBundle.dao.TzExpTplDfnTMapper;
import com.tranzvision.gd.TZExportTplMgBundle.model.TzExpTplDfnT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 导入模板信息
 * @author shaweyet
 */
@Service("com.tranzvision.gd.TZExportTplMgBundle.service.impl.ExportTplInfoImpl")
public class ExportTplInfoImpl extends FrameworkImpl {
	@Autowired
	private TzExpTplDfnTMapper TzExpTplDfnTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 获取导出模板定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("tplId")) {
				String tplId = jacksonUtil.getString("tplId");
				TzExpTplDfnT tzExpTplDfnT=  TzExpTplDfnTMapper.selectByPrimaryKey(tplId);
				if (tzExpTplDfnT != null) {
					returnJsonMap.put("tplId", tzExpTplDfnT.getTzTplId());
					returnJsonMap.put("tplName", tzExpTplDfnT.getTzTplName());
					returnJsonMap.put("javaClass", tzExpTplDfnT.getTzJavaClass());
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该导入模板不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该导入模板不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

		@Override
		public String tzAdd(String[] actData, String[] errMsg) {
			String strRet = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					// 表单内容;
					String strForm = actData[num];
					// 将字符串转换成json;
					jacksonUtil.json2Map(strForm);
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");

					String type = (String) jacksonUtil.getString("type");
					switch(type){
					case "TPL":
						String tplId = (String) infoData.get("tplId");
						String tplName = (String) infoData.get("tplName");
						String javaClass = (String) infoData.get("javaClass");
						
						String sql = "SELECT COUNT(1) from TZ_EXP_TPL_DFN_T WHERE TZ_TPL_ID=?";
						int count = jdbcTemplate.queryForObject(sql, new Object[] { tplId }, "Integer");
						if (count > 0) {
							errMsg[0] = "1";
							errMsg[1] = "模板编号为：" + tplId + "的信息已经存在，请修改模板编号。";
						} else {
							TzExpTplDfnT tzImpTplDfnT = new TzExpTplDfnT();
							tzImpTplDfnT.setTzTplId(tplId);
							tzImpTplDfnT.setTzTplName(tplName);
							tzImpTplDfnT.setTzJavaClass(javaClass);
							
							TzExpTplDfnTMapper.insert(tzImpTplDfnT);
						}
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg[0] = "1";
				errMsg[1] = e.toString();
			}
			return strRet;
		}

		@Override
		public String tzUpdate(String[] actData, String[] errMsg) {
			String strRet = "";
			JacksonUtil jacksonUtil = new JacksonUtil();
			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					// 表单内容;
					String strForm = actData[num];
					// 将字符串转换成json;
					jacksonUtil.json2Map(strForm);
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");

					String type = (String) jacksonUtil.getString("type");
					switch(type){
					case "TPL":
						
						String tplId = (String) infoData.get("tplId");
						String tplName = (String) infoData.get("tplName");
						String javaClass = (String) infoData.get("javaClass");
	
						String sql = "SELECT COUNT(1) from TZ_EXP_TPL_DFN_T WHERE TZ_TPL_ID=?";
						int count = jdbcTemplate.queryForObject(sql, new Object[] { tplId }, "Integer");
						if (count > 0) {
							TzExpTplDfnT tzExpTplDfnT = new TzExpTplDfnT();
							tzExpTplDfnT.setTzTplId(tplId);
							tzExpTplDfnT.setTzTplName(tplName);
							tzExpTplDfnT.setTzJavaClass(javaClass);
							
							TzExpTplDfnTMapper.updateByPrimaryKey(tzExpTplDfnT);
						} else {
							errMsg[0] = "1";
							errMsg[1] = "模板编号为：" + tplId + "的信息不存在。";						
						}
						break;
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
