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
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsPwTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsPwTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * MBA材料面试评审-面试规则-添加评委页
 * 
 * @author tzhjl
 * @since 2017-03-13
 */

@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewMsRuleJudgeServiceImpl")
public class TzReviewMsRuleJudgeServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzMsPsPwTblMapper psTzMsPsPwTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;

	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", "");
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID", "OPRID", "TZ_REALNAME", "TZ_DLZH_ID" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {

					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					if (rowList[0].equals("SEM")) {
						mapList.put("judgId", rowList[1]);
						mapList.put("judzhxx", rowList[3]);
						mapList.put("judgName", rowList[2]);
						mapList.put("judgGroupId", "");
					} else {

					}

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);

	}

	// 添加考生
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();

		String judgId = "";
		String judgGroupId = "";
		String judgName = "";

		String ksName = "";
		int count = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			for (int i = 2; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				judgId = jacksonUtil.getString("judgId");
				judgGroupId = jacksonUtil.getString("judgGroupId");
				judgName = jacksonUtil.getString("judgName");

				String sql = "SELECT COUNT(1) from PS_TZ_MSPS_PW_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_PWEI_OPRID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, judgId }, "Integer");
				if (count > 0) {
					errMsg[0] = "1";
					errMsg[1] = "评委:" + judgName + "已存在，不能重新添加！";

				} else {
					PsTzMsPsPwTbl psTzMsPsPwTbl = new PsTzMsPsPwTbl();
					psTzMsPsPwTbl.setTzClassId(classId);
					psTzMsPsPwTbl.setTzApplyPcId(batchId);
					psTzMsPsPwTbl.setTzPweiOprid(judgId);
					psTzMsPsPwTbl.setTzPweiGrpid(judgGroupId);
					psTzMsPsPwTbl.setTzPweiZhzt("");
					psTzMsPsPwTbl.setRowAddedDttm(nowdate);
					psTzMsPsPwTbl.setRowAddedOprid(Oprid);
					psTzMsPsPwTbl.setRowLastmantDttm(nowdate);
					psTzMsPsPwTbl.setRowLastmantOprid(Oprid);
					psTzMsPsPwTblMapper.insertSelective(psTzMsPsPwTbl);

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
