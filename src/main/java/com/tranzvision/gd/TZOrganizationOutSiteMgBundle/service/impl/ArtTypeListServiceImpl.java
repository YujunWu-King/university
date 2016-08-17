package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao.PsTzArtTypeTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author zhangbb
 * @version 创建时间：2016年8月16日 下午16:28:30 类说明 内容类型管理
 */
@Service("com.tranzvision.gd.TZOrganizationOutSiteMgBundle.service.impl.ArtTypeListServiceImpl")
public class ArtTypeListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private PsTzArtTypeTMapper psTzArtTypeTMapper;

	/* 查询类型类型列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("total", 0);
				ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
				mapRet.put("root", listData);
				JacksonUtil jacksonUtil = new JacksonUtil();

				// 排序字段如果没有不要赋值
				String[][] orderByArr = new String[][] { { "TZ_ART_TYPE_ID", "ASC" } };

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_ART_TYPE_ID", "TZ_ART_TYPE_NAME", "IS_ENABLED_FLG" };

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams,
						numLimit, numStart, errorMsg);

				if (obj != null && obj.length > 0) {
					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("artTypeId", rowList[0]);
						mapList.put("artTypeName", rowList[1]);
						mapList.put("isused", rowList[2]);
						listData.add(mapList);
					}
					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);
				}

				return jacksonUtil.Map2json(mapRet);
	}

	/* 删除 */
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
				// hardcode ID;
				String artTypeId = jacksonUtil.getString("artTypeId");
				if (artTypeId != null && !"".equals(artTypeId)) {
					psTzArtTypeTMapper.deleteByPrimaryKey(artTypeId);
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
