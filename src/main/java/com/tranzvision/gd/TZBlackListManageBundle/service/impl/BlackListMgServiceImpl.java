package com.tranzvision.gd.TZBlackListManageBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 黑名单管理
 * 
 * @author xzx
 * @since 2017-1-17 
 */
@Service("com.tranzvision.gd.TZBlackListManageBundle.service.impl.BlackListMgServiceImpl")
@SuppressWarnings("unchecked")
public class BlackListMgServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_EMAIL", "TZ_MOBILE", "TZ_MSSQH","TZ_BEIZHU"};
			
			String admin = "\"TZ_JG_ID-operator\":\"01\",\"TZ_JG_ID-value\":\"ADMIN\",";
			strParams.replaceAll(admin, "");
			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("OPRID", rowList[0]);
					mapList.put("userName", rowList[1]);
					mapList.put("userEmail", rowList[2]);
					mapList.put("userPhone", rowList[3]);
					mapList.put("msSqh", rowList[4]);
					mapList.put("beizhu", rowList[5]);
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
	
	
	/*删除用户账号信息*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String oprID = "";
				// 用户账号信息;
				String strUserInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strUserInfo);
				// 获取用户ID
				oprID = jacksonUtil.getString("OPRID");
				
			    if(oprID != null && !"".equals(oprID)){
			    	//删除照片信息；
			    	String photoSQL1 = "DELETE FROM PS_TZ_OPR_PHOTO_T WHERE TZ_ATTACHSYSFILENA IN (SELECT TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T WHERE OPRID=?)";
			    	jdbcTemplate.update(photoSQL1, new Object[]{oprID});
					
			    	
			    	String photoSQL2 = "DELETE FROM PS_TZ_OPR_PHT_GL_T WHERE OPRID=?";
			    	jdbcTemplate.update(photoSQL2, new Object[]{oprID});
			    	
			    	
			    	//删除会员用户的注册信息;
			    	String deleteHYSQL = "DELETE FROM PS_TZ_REG_USER_T WHERE OPRID=?";
			    	jdbcTemplate.update(deleteHYSQL, new Object[]{oprID});
			    	
			    	//删除会员用户的注册信息;
			    	String deleteLXFSSQL = "DELETE FROM PS_TZ_LXFSINFO_TBL WHERE ( TZ_LXFS_LY='ZCYH' OR  TZ_LXFS_LY='NBYH') AND TZ_LYDX_ID=?";
			    	jdbcTemplate.update(deleteLXFSSQL, new Object[]{oprID});
			    	
			    	//删除用户角色;
			    	String deleteROLESQL = "DELETE FROM PSROLEUSER WHERE ROLEUSER=?";
			    	jdbcTemplate.update(deleteROLESQL, new Object[]{oprID});
			    	
			    	//删除用户信息记录信息;
			    	String deleteYHXXSQL = "DELETE FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
			    	jdbcTemplate.update(deleteYHXXSQL, new Object[]{oprID} );

			    	//删除用户;
			    	String deleteOPDSQL = "DELETE FROM PSOPRDEFN WHERE OPRID=?";
			    	jdbcTemplate.update(deleteOPDSQL, new Object[]{oprID} );
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
