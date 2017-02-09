package com.tranzvision.gd.TZExportTplMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZExportTplMgBundle.dao.TzExpTplDfnTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;

/*
 * 导入模板管理
 * @author shaweyet
 */
@Service("com.tranzvision.gd.TZExportTplMgBundle.service.impl.ExportTplMgImpl")
public class ExportTplMgImpl extends FrameworkImpl {
	@Autowired
	private TzExpTplDfnTMapper TzExpTplDfnTMapper;
	@Autowired
	private FliterForm fliterForm;
	
	/*加载导出模板列表 */
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
		String[][] orderByArr = new String[][] { { "TZ_TPL_ID", "ASC" } };

		// json数据要的结果字段;
		String[] resultFldArray = { "TZ_TPL_ID", "TZ_TPL_NAME", "TZ_JAVA_CLASS"};

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
				numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("tplId", rowList[0]);
				mapList.put("tplName", rowList[1]);
				mapList.put("javaClass", rowList[2]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	// 功能说明：删除导出模板定义;
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

					String tplId = jacksonUtil.getString("tplId");
					if (tplId != null && !"".equals(tplId)) {
						TzExpTplDfnTMapper.deleteByPrimaryKey(tplId);
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
