package com.tranzvision.gd.TZUniPrintBundle.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZUniPrintBundle.dao.TzDymbTblMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
/*
 * 打印模板管理
 * 清华mba招生
 */
@Service("com.tranzvision.gd.TZUniPrintBundle.service.impl.UniPrintTplMgImpl")
public class UniPrintTplMgImpl  extends FrameworkImpl {
	@Autowired
	private TzDymbTblMapper TzDymbTblMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	
	/*加载打印模板列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart,
			String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_DYMB_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_JG_ID", "TZ_DYMB_ID", "TZ_DYMB_NAME", "TZ_DYMB_ZT", "TZ_TPL_NAME", "TZ_DYMB_MENO","TZ_DYMB_PDF_URL"};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
				numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("TZ_JG_ID", rowList[0]);
				mapList.put("TZ_DYMB_ID", rowList[1]);
				mapList.put("TZ_DYMB_NAME", rowList[2]);
				mapList.put("TZ_DYMB_ZT", rowList[3]);
				mapList.put("TZ_TPL_NAME", rowList[4]);
				mapList.put("TZ_DYMB_MENO", rowList[5]);
				mapList.put("TZ_DYMB_PDF_URL", rowList[6]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	// 功能说明：删除打印模板定义;
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

			try {
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					//提交信息
					String strForm = actData[num];
					jacksonUtil.json2Map(strForm);

					String jgId = jacksonUtil.getString("TZ_JG_ID");
					String tplId = jacksonUtil.getString("TZ_DYMB_ID");
					
					if (jgId != null && !"".equals(jgId) && tplId != null && !"".equals(tplId)) {
						//TzDymbTblMapper.deleteByPrimaryKey(jgId,tplId);
						/*模板字段表*/
						String TzDyMbFldTSql = "DELETE FROM TZ_DYMB_TBL WHERE TZ_JG_ID=? and TZ_DYMB_ID = ?";
						jdbcTemplate.update(TzDyMbFldTSql, new Object[]{jgId,tplId});
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
