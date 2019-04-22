package com.tranzvision.gd.TZProAdjustBundle.service.impl;

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
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZProAdjustBundle.dao.PsTzProAdjustTMapper;
import com.tranzvision.gd.TZProAdjustBundle.model.PsTzProAdjustT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 申请项目调整功能
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZProAdjustBundle.service.impl.TZProAdjustServiceImpl")
public class TZProAdjustServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzProAdjustTMapper psTzProAdjustTMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("total", 0);
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

				// 排序字段
				String[][] orderByArr = new String[][] { { "apply_date", "DESC" } };

				// json数据要的结果字段;
				String[] resultFldArray = { "tz_proadjust_id", "tz_oprid", "classId", "appinsId",
						"applicationId", "submitState", "apply_date", "state","TZ_REALNAME", "TZ_CLASS_NAME" };

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart,
						errorMsg);

				if (obj != null && obj.length > 0) {

					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);

						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("tz_proadjust_id", rowList[0]);
						mapList.put("tz_oprid", rowList[1]);
						mapList.put("classId", rowList[2]);
						mapList.put("appinsId", rowList[3]);
						mapList.put("applicationId", rowList[4]);
						mapList.put("submitState", rowList[5]);
						mapList.put("apply_date", rowList[6]);
						mapList.put("state", rowList[7]);
						mapList.put("TZ_REALNAME", rowList[8]);
						mapList.put("TZ_CLASS_NAME", rowList[9]);


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
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		if("projectAdjust".equals(oprType)) {
			return projectAdjust(strParams,errorMsg);
		}
		if("setState".equals(oprType)) {
			return setState(strParams,errorMsg);
		}
		return super.tzOther(oprType, strParams, errorMsg);
	}

	/**
	 * 设置状态
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String setState(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			int tz_proadjust_id = jacksonUtil.getInt("tz_proadjust_id");
			int adjustStatus = jacksonUtil.getInt("adjustStatus");
			
			PsTzProAdjustT proAdjustT = psTzProAdjustTMapper.selectByPrimaryKey(tz_proadjust_id);
			if(proAdjustT == null) {
				errorMsg[0] = "1";
				errorMsg[1] = "找不到相应的调整记录!";
			}
			String oprid = proAdjustT.getTzOprid();
			String appinsId = proAdjustT.getAppinsid();
			//同意
			if(adjustStatus == 1) {
				//用户表里面的是否继续申请修改成可以
				String sql = "UPDATE PS_TZ_REG_USER_T SET TZ_ALLOW_APPLY='Y' WHERE OPRID=?";
				sqlQuery.update(sql,new Object[] {oprid});
				//把该报名表状态修改成撤销 OUT
				sql = "UPDATE PS_TZ_APP_INS_T SET TZ_APP_FORM_STA='OUT' WHERE TZ_APP_INS_ID = ?";
				sqlQuery.update(sql,new Object[] {appinsId});
				//修改审核状态
				proAdjustT.setState(1);
				proAdjustT.setSubmitstate("OUT");
			}else { //拒绝
				proAdjustT.setState(2);
			}
			psTzProAdjustTMapper.updateByPrimaryKeySelective(proAdjustT);
			//发送站内信
			String falg = send(oprid);
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 申请调整项目
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String projectAdjust(String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String classId = jacksonUtil.getString("classId");
			String appinsId = jacksonUtil.getString("appinsId");
			String applyId = jacksonUtil.getString("applyId");
			
			String sql = "select TZ_APP_FORM_STA  from PS_TZ_APP_INS_T where TZ_APP_INS_ID=?";
			String submitState = sqlQuery.queryForObject(sql, new Object[] {appinsId}, "String");
			
			PsTzProAdjustT proAdjustT = new PsTzProAdjustT();
			proAdjustT.setAppinsid(appinsId);
			proAdjustT.setApplicationid(applyId);
			proAdjustT.setClassid(classId);
			proAdjustT.setTzOprid(oprid);
			proAdjustT.setState(0);
			proAdjustT.setApplyDate(new Date());
			proAdjustT.setSubmitstate(submitState);
			psTzProAdjustTMapper.insertSelective(proAdjustT);
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
			e.printStackTrace();
		}
		return "";
	}
	
	public String send(String oprid){
        //发送站内信
            /*tzOnlineAppEngineImpl.sendSiteEmail( strAppInsID, "TZ_BMB_ZT", strOprID,
                    strAppOrgId, "报名表退回撤销发送站内信", "BMB", strClassID);*/
        // 创建邮件短信发送任务
        String strTaskId = createTaskServiceImpl.createTaskIns("MPACC", "TZ_PRO_ADJUST", "ZNX", "A");
        if (strTaskId == null || "".equals(strTaskId)) {
            return "false1";
        }
        // 创建短信、邮件发送的听众;
        String createAudience = createTaskServiceImpl.createAudience(strTaskId, "MPACC", "申请项目调整回复",
                "");
        if ("".equals(createAudience) || createAudience == null) {
            return "false2";
        }
        boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, "", "", "", "", "",
                "", "", oprid, "", "","");
        if (!addAudCy) {
            return "false3";
        }

        // 得到创建的任务ID
        if ("".equals(strTaskId) || strTaskId == null) {
            return "false4";
        } else {
            // 发送邮件
            sendSmsOrMalServiceImpl.send(strTaskId, "");
            return "true";
        }

    }
}
