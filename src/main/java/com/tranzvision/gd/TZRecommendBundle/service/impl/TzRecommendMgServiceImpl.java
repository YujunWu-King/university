package com.tranzvision.gd.TZRecommendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * 推荐人管理列表页
 * 
 * @author 
 * @since 
 */
@Service("com.tranzvision.gd.TZRecommendBundle.service.impl.TzRecommendMgServiceImpl")
public class TzRecommendMgServiceImpl extends FrameworkImpl {
	
	@Autowired
	private HttpServletRequest request;


	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzLmNrGlTMapper psTzLmNrGlTMapper;
	
	@Autowired
	private ArtContentHtml artContentHtml;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;
	
	@Autowired
	private TZGDObject tzGDObject;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段
			String[][] orderByArr = new String[][] { new String[] { "TZ_CLASS_NAME", "DESC" },{"TZ_START_DT", "DESC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_BATCH_ID","TZ_CLASS_ID","TZ_START_DT", "TZ_CLASS_NAME", "TZ_BATCH_NAME","TZ_RX_DT"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("batchId", rowList[0]);
					mapList.put("classId", rowList[1]);
					mapList.put("startDate", rowList[2]);
					mapList.put("className", rowList[3]);
					mapList.put("batchName", rowList[4]);
					mapList.put("entranceYear", rowList[5]);
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}
	
}
