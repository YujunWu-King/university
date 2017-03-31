package com.tranzvision.gd.TZAutoScoringRulerBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutoScoringRulerBundle.dao.PsTzZdCsDfGzTblMapper;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

@Service("com.tranzvision.gd.TZAutoScoringRulerBundle.service.impl.autoScoringRulerListImpl")
public class autoScoringRulerListImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzZdCsDfGzTblMapper PsTzZdCsDfGzTblMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/* 查询项目分类列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		// 获取当前登陆人机构ID
		String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值

		String[][] orderByArr = new String[][] {};

		// json数据要的结果字段;

		String[] resultFldArray = { "TZ_ZDCSGZ_ID", "TZ_ZDCSGZ_NAME", "TZ_ZDCSGZ" };

		// 可配置搜索通用函数;
		Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();

				mapList.put("zhjgID", rowList[0]);

				mapList.put("zhjgName", rowList[1]);

				mapList.put("zhjgNameID", rowList[2]);

				listData.add(mapList);

			}

			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}

		return jacksonUtil.Map2json(mapRet);
	}

	/* 删除院校类型 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		// 获取当前登陆人机构ID
		String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 主题 ID;
				String zhjgID = jacksonUtil.getString("zhjgID");
				System.out.println(zhjgID);
				if (zhjgID != null && !"".equals(zhjgID)) {
					/*
					 * PsTzZdCsDfGzTbl PsTzZdCsDfGzTbl = new PsTzZdCsDfGzTbl();
					 * PsTzZdCsDfGzTbl.setTzZdcsgzId(zhjgID);
					 */

					PsTzZdCsDfGzTblMapper.deleteByPrimaryKey(zhjgID);

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
