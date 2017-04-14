package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.PsTzMpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * MBA材料面试评审-面试规则-设置指定评委页
 * 
 * @author tzhjl
 * @since 2017-03-15
 */
@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewMsSetJudgesServiceImpl")
public class TzReviewMsSetJudgesServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzMpPwKsTblMapper psTzMpPwKsTblMapper;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回内容
		Map<String, Object> mapRetn = new HashMap<String, Object>();
		mapRetn.put("total", 0);
		mapRetn.put("root", "");
		ArrayList<Map<String, Object>> lisdata = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_CLASS_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_PWEI_GRPID", "TZ_PWEI_OPRID",
					"TZ_CLPS_GR_NAME", "TZ_REALNAME", "TZ_DLZH_ID" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classId", rowList[0]);
					mapList.put("batchId", rowList[1]);
					mapList.put("judgeID", rowList[3]);
					mapList.put("judgeGroup", rowList[4]);
					mapList.put("judgeName", rowList[5]);
					mapList.put("judgzhxx", rowList[6]);

					lisdata.add(mapList);
				}

				mapRetn.replace("total", obj[0]);
				mapRetn.replace("root", lisdata);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();

		}

		return jacksonUtil.Map2json(mapRetn);
	}

	// 设定评委
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();
		Long appinsId = (long) 0;
		String judgeID = "";
		int count = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			jacksonUtil.json2Map(actData[2]);
			appinsId = Long.valueOf(jacksonUtil.getString("appInsId"));
			for (int i = 3; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// 解析 json
				jacksonUtil.json2Map(strForm);

				judgeID = jacksonUtil.getString("judgeID");
				String sql = "SELECT COUNT(1) from PS_TZ_MP_PW_KS_TBL where TZ_PWEI_OPRID=?  and TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_APP_INS_ID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { judgeID, classId, batchId, appinsId }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "评委:" + judgeID + "已存在，请重新添加！";

				} else {
					PsTzMpPwKsTbl psTzMpPwKsTbl = new PsTzMpPwKsTbl();
					psTzMpPwKsTbl.setTzClassId(classId);
					psTzMpPwKsTbl.setTzApplyPcId(batchId);
					psTzMpPwKsTbl.setTzAppInsId(appinsId);
					psTzMpPwKsTbl.setTzPweiOprid(judgeID);
					psTzMpPwKsTbl.setRowAddedDttm(nowdate);
					psTzMpPwKsTbl.setRowAddedOprid(Oprid);
					psTzMpPwKsTbl.setRowLastmantDttm(nowdate);
					psTzMpPwKsTbl.setRowLastmantOprid(Oprid);
					psTzMpPwKsTblMapper.insertSelective(psTzMpPwKsTbl);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		return null;
	}

}
