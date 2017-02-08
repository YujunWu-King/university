package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QustionnairePersonClsImpl")
public class QustionnairePersonClsImpl extends FrameworkImpl{
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;

	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		Map<String, Object> conditionJson = jacksonUtil.getMap("condition");
		String TZ_SCHLR_ID=(String) conditionJson.get("TZ_SCHLR_ID");
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = {"TZ_DC_WJ_ID","OPRID","TZ_REALNAME","TZ_MOBILE","TZ_EMAIL","TZ_STATE","TZ_APP_INS_ID"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
            //获取奖学金编号
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("wjId", rowList[0]);
					mapList.put("oprid", rowList[1]);
					mapList.put("name", rowList[2]);
					mapList.put("phone", rowList[3]);
					mapList.put("email", rowList[4]);
					mapList.put("wjInsId", rowList[6]);
					if("0".equals(rowList[5])){
					  mapList.put("dcState", "已完成");
					}else{
				       mapList.put("dcState", "未完成");		
					}
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
	public String tzUpdate(String[] actData, String[] errMsg) {
		return "";
	}
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

				audID = createTaskServiceImpl.createAudience("", str_jg_id, "参与人管理批量邮件发送", "JSRW");

				// 群发邮件添加听众;
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");
				String flag=jacksonUtil.getString("flag");
				if (list != null && list.size() > 0) {
					for (int num_1 = 0; num_1 < list.size(); num_1++) {
						Map<String, Object> map = list.get(num_1);
						//int tzSeqNum = Integer.parseInt(map.get("seqNum").toString());
                        String name=map.get("name").toString();
                        String email=map.get("email").toString();
                        String phone=map.get("phone").toString();
						if (!("").equals(email)&&("EML").equals(flag)) {
							/* 为听众添加成员:姓名，称谓，报名人联系方式 */
							//PsTzOnTrialTWithBLOBs psTzOnTrialTWithBLOBs = psTzOnTrialTMapper.selectByPrimaryKey(tzSeqNum);
								createTaskServiceImpl.addAudCy(audID, name, "", "", "", email, "", "", "", "","", "");
						}
						if (!("").equals(phone)&&("SMS").equals(flag)) {
								createTaskServiceImpl.addAudCy(audID, name, "", phone, "", "", "", "", "", "","", "");
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
	
}
