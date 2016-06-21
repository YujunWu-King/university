package com.tranzvision.gd.TZOnTrialBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZOnTrialBundle.dao.PsTzOnTrialTMapper;
import com.tranzvision.gd.TZOnTrialBundle.model.PsTzOnTrialTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZOnTrialBundle.service.impl.TzOnTrialManagerServiceImpl")
public class TzOnTrialManagerServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private PsTzOnTrialTMapper psTzOnTrialTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;

	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_SEQ_NUM", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_SEQ_NUM", "TZ_ORG_NAME", "TZ_CONTACT_NAME", "TZ_CONTACT_PHONE", "TZ_EMAIL",
					"TZ_ORG_WEBSITE", "ROW_ADD_TIME", "TZ_SH_RST", "TZ_START_TIME", "TZ_END_TIME","TZ_HMSR" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("seqNum", rowList[0]);
					mapList.put("orgName", rowList[1]);
					mapList.put("contactName", rowList[2]);
					mapList.put("contactPhone", rowList[3]);
					mapList.put("contactEmail", rowList[4]);
					mapList.put("website", rowList[5]);
					mapList.put("submitTime", rowList[6]);
					mapList.put("shRst", rowList[7]);
					mapList.put("startTime", rowList[8]);
					mapList.put("endTime", rowList[9]);
					mapList.put("hmsr", rowList[10]);

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 添加听众 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String audID = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return audID;
		}
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String str_jg_id = "ADMIN";

				audID = createTaskServiceImpl.createAudience("", str_jg_id, "试用申请批量邮件发送", "JSRW");

				// 群发邮件添加听众;
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");
				if (list != null && list.size() > 0) {
					for (int num_1 = 0; num_1 < list.size(); num_1++) {
						Map<String, Object> map = list.get(num_1);
						int tzSeqNum = Integer.parseInt(map.get("seqNum").toString());

						if (tzSeqNum > 0) {
							/* 为听众添加成员:姓名，称谓，报名人联系方式 */
							PsTzOnTrialTWithBLOBs psTzOnTrialTWithBLOBs = psTzOnTrialTMapper
									.selectByPrimaryKey(tzSeqNum);
							if (psTzOnTrialTWithBLOBs != null) {
								String strName = psTzOnTrialTWithBLOBs.getTzContactName();
								String mainEmail = psTzOnTrialTWithBLOBs.getTzEmail();

								createTaskServiceImpl.addAudCy(audID, strName, "", "", "", mainEmail, "", "", "", "",
										"", String.valueOf(tzSeqNum));
							}

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return audID;
	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("seqNum")) {
				int tzSeqNum = Integer.parseInt(jacksonUtil.getString("seqNum"));
				PsTzOnTrialTWithBLOBs psTzOnTrialT = psTzOnTrialTMapper.selectByPrimaryKey(tzSeqNum);
				if (psTzOnTrialT != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("seqNum", tzSeqNum);
					mapData.put("contactName", psTzOnTrialT.getTzContactName());
					mapData.put("contactPhone", psTzOnTrialT.getTzContactPhone());
					mapData.put("contactTel", psTzOnTrialT.getTzTel());
					mapData.put("contactEmail", psTzOnTrialT.getTzEmail());
					mapData.put("orgName", psTzOnTrialT.getTzOrgName());
					mapData.put("website", psTzOnTrialT.getTzOrgWebsite());
					mapData.put("hmsr", psTzOnTrialT.getTzHmsr());

					SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date submitTime = psTzOnTrialT.getRowAddTime();
					String submitTimeStr = "";
					if (submitTime != null) {
						submitTimeStr = datetimeFormate.format(submitTime);
					}
					mapData.put("submitTime", submitTimeStr);

					mapData.put("shRst", psTzOnTrialT.getTzShRst());
					mapData.put("remark", psTzOnTrialT.getTzRemark());

					Date startTime = psTzOnTrialT.getTzStartTime();
					String startTimeStr = "";
					if (startTime != null) {
						startTimeStr = datetimeFormate.format(startTime);
					}
					mapData.put("startTime", startTimeStr);

					Date endTime = psTzOnTrialT.getTzEndTime();
					String endTimeStr = "";
					if (endTime != null) {
						endTimeStr = datetimeFormate.format(endTime);
					}
					mapData.put("endTime", endTimeStr);
					
					mapData.put("orgId", psTzOnTrialT.getTzJgId());
					mapData.put("dlzh",psTzOnTrialT.getTzDlzhId());
					mapData.put("sfSale",psTzOnTrialT.getTzSfSale());
					
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "查询的数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "查询的数据不存在";
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
		// 返回值;
		String rest = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return rest;
		}
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("data")){
					Map<String, Object> map = jacksonUtil.getMap("data");
					
					int seqNum = Integer.parseInt((String)map.get("seqNum"));
					String shRst = (String)map.get("shRst");
					String remark = (String)map.get("remark");
					String orgId = (String)map.get("orgId");
					String dlzh = (String)map.get("dlzh");
					String sfSale = (String)map.get("sfSale");
					
					//查询账号是不是已经被其他机构试用了;
					if(orgId != null && !"".equals(orgId)
							&& dlzh != null && !"".equals(dlzh)){
						int count = jdbcTemplate.queryForObject("select COUNT(1) from PS_TZ_ON_TRIAL_T where TZ_JG_ID=? AND TZ_DLZH_ID=? AND TZ_SEQ_NUM<>?", new Object[]{orgId,dlzh,seqNum},"Integer");
						if(count > 0){
							errMsg[0] = "1";
							errMsg[1] = "该试用账号已经被其他机构使用，请选择其他的试用账号";
							return "";
						}
					}
					
					
					PsTzOnTrialTWithBLOBs psTzOnTrialT = psTzOnTrialTMapper.selectByPrimaryKey(seqNum);
					if(psTzOnTrialT != null){
						psTzOnTrialT.setTzShRst(shRst);
						psTzOnTrialT.setTzRemark(remark);
						psTzOnTrialT.setTzJgId(orgId);
						psTzOnTrialT.setTzDlzhId(dlzh);
						psTzOnTrialT.setTzSfSale(sfSale);
						int i = psTzOnTrialTMapper.updateByPrimaryKeySelective(psTzOnTrialT);
						if(i <= 0){
							errMsg[0] = "1";
							errMsg[1] = "更新数据失败";
						}
					}else{
						errMsg[0] = "1";
						errMsg[1] = "未找到对应的更新数据";
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应的更新数据";
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return rest;
	}
}
