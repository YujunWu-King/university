package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * 厦门大学联考成绩数据导入
 * @author xzp
 *
 */
@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportXMDXLkcjImpl")
public class ImportXMDXLkcjImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private UnifiedImportImpl unifiedImportImpl;
	
	
	private boolean equalString(String str1,String str2){
		if(str1==null && str2==null){
			return true;
		}else if("".equals(str1) && "".equals("str2")){
			return true;
		}else if(str1==null&&str2!=null){
			return false;
		}else if(str1!=null&&str2==null){
			return false;
		}else{
			return str1.equals(str2);
		}
		
		
	}
	public void tzValidate(List<Map<String, Object>> data, List<String> fields, String targetTbl, Object[] result,
			String[] errMsg) {
		try {

			ArrayList<String> resultMsg = new ArrayList<String>();

			// 开始校验数据
			if (data != null && data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					String TZ_BMH_ID = ((String) data.get(i).get("TZ_BMH_ID"));
					System.out.println(TZ_BMH_ID);
                    String TZ_STU_NAME=((String) data.get(i).get("TZ_STU_NAME"));
                    String TZ_STU_CREDIT=((String) data.get(i).get("TZ_STU_CREDIT"));
					if (TZ_BMH_ID == null || "".equals(TZ_BMH_ID)) {
						result[0] = false;
						resultMsg.add("第[" + (i + 1) + "]行报名号不能为空");
					} else {
						// 检查报名号是否存在
						String TZ_BMH_ID_EXIST = sqlQuery.queryForObject(
								"SELECT 'Y' FROM PS_TZ_FORM_WRK_T  WHERE TZ_BMH_ID=?", new Object[] { TZ_BMH_ID },
								"String");
						if (!"Y".equals(TZ_BMH_ID_EXIST)) {
							result[0] = false;
							resultMsg.add("第[" + (i + 1) + "]行报名号不存在");
						}else{
							String sql="select TZ_REALNAME,NATIONAL_ID from PS_TZ_REG_USER_T where OPRID =(select OPRID from PS_TZ_FORM_WRK_T where TZ_BMH_ID=?)";
							Map<String, Object> userMap=sqlQuery.queryForMap(sql, new Object[]{TZ_BMH_ID});
							String realName=(String) userMap.get("TZ_REALNAME");
							String  nationalID=(String) userMap.get("NATIONAL_ID");
							if(!this.equalString(realName, TZ_STU_NAME)||!this.equalString(nationalID, TZ_STU_CREDIT)){
								result[0] = false;
								resultMsg.add("第[" + (i + 1) + "]行用户姓名或身份证不匹配");
							}
						}
					}
				}
			} else {
				resultMsg.add("您没有导入任何数据！");
			}

			result[1] = String.join("，", (String[]) resultMsg.toArray(new String[resultMsg.size()]));

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

	}

	@Override
	public String tzSave(List<Map<String, Object>> data, List<String> fields, String targetTbl, int[] result,
			String[] errMsg) {
		
		return unifiedImportImpl.tzSave(data, fields, targetTbl, result, errMsg);
	}

}
