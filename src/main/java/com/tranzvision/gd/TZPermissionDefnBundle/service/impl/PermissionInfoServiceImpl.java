package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsClassDefnMapper;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsClassDefn;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.base.TZUtility;

import net.sf.json.JSONObject;

/*
 * 类方法定义， 原PS类：TZ_GD_PLST_PKG:TZ_GD_PERMINFO_CLS
 * @author tang
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionInfoServiceImpl")
public class PermissionInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private PsClassDefnMapper psClassDefnMapper;
	
	/*新增许可权信息*/
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 类型标志;
			    String strFlag = CLASSJson.getString("typeFlag");
				// 信息内容;
				String infoData = CLASSJson.getString("data");
				
				// 许可权信息;
				if("PERM".equals(strFlag)){
					// 将字符串转换成json;
					JSONObject Json = PaseJsonUtil.getJson(infoData);
					// 许可权编号;
			        String strPermID = Json.getString("permID");
			        //许可权描述;
			        String strPermDesc = Json.getString("permDesc");
			        String sql = "select COUNT(1) from PSCLASSDEFN WHERE CLASSID=?";
			        int count = jdbcTemplate.queryForObject(sql, new Object[] { strPermID }, Integer.class);
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "许可权ID为：" + strPermID + "的信息已经存在，请修改许可权ID。";
					} else {
						PsClassDefn psClassDefn = new PsClassDefn();
						psClassDefn.setClassid(strPermID);
						psClassDefn.setClassdefndesc(strPermDesc);
						psClassDefn.setVersion(1);
						psClassDefn.setLastupddttm(new Date());
						/***TODO %USERID**/
						psClassDefn.setLastupdoprid("TZ_7");
						psClassDefnMapper.insert(psClassDefn);
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/* 修改许可权信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 类型标志;
			    String strFlag = CLASSJson.getString("typeFlag");
				// 信息内容;
				String infoData = CLASSJson.getString("data");
				
				// 许可权信息;
				if("PERM".equals(strFlag)){
					// 将字符串转换成json;
					JSONObject Json = PaseJsonUtil.getJson(infoData);
					// 许可权编号;
			        String strPermID = Json.getString("permID");
			        //许可权描述;
			        String strPermDesc = Json.getString("permDesc");
			        String sql = "select COUNT(1) from PSCLASSDEFN WHERE CLASSID=?";
			        int count = jdbcTemplate.queryForObject(sql, new Object[] { strPermID }, Integer.class);
					if (count > 0) {
						PsClassDefn psClassDefn = new PsClassDefn();
						psClassDefn.setClassid(strPermID);
						psClassDefn.setClassdefndesc(strPermDesc);
						psClassDefn.setVersion(1);
						psClassDefn.setLastupddttm(new Date());
						/***TODO %USERID**/
						psClassDefn.setLastupdoprid("TZ_7");
						psClassDefnMapper.updateByPrimaryKeySelective(psClassDefn);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "许可权ID为：" + strPermID + "的信息不存在";
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	

	/*获取许可权信息*/
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);

			if (CLASSJson.containsKey("permID")) {
				// 类方法ID;
				String strPermID = CLASSJson.getString("permID");
				PsClassDefn psClassDefn = psClassDefnMapper.selectByPrimaryKey(strPermID);
				if (psClassDefn != null) {
					strRet = "{\"formData\":{\"permID\":\"" + TZUtility.transFormchar(psClassDefn.getClassid())
							+ "\",\"permDesc\":\"" + TZUtility.transFormchar(psClassDefn.getClassdefndesc())
							+ "\"}}";
				} else {
					errMsg[0] = "1";
					errMsg[1] = "请选择许可权定义";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择许可权定义";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	/*获取授权组件列表*/
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		try {
			FliterForm fliterForm = new FliterForm();

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_COM_ID", "ASC" } };
			fliterForm.orderByArr = orderByArr;

			// json数据要的结果字段;
			String[] resultFldArray = { "CLASSID", "TZ_COM_ID", "TZ_COM_MC"};
			String jsonString = "";

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, comParams, numLimit, numStart, errorMsg);

			if (obj == null || obj.length == 0) {
				strRet = "{\"total\":0,\"root\":[]}";
			} else {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					jsonString = jsonString + ",{\"permID\":\"" + rowList[0] + "\",\"comID\":\"" + rowList[1]
							+ "\",\"comName\":\"" + rowList[2]+ "\"}";
				}

				if (!"".equals(jsonString)) {
					jsonString = jsonString.substring(1);
				}

				strRet = "{\"total\":" + obj[0] + ",\"root\":[" + jsonString + "]}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
	
	/* 删除许可权组件授权信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];

				JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				// 许可权ID;
				String sPermID = CLASSJson.getString("permID");
				// 组件ID;
			    String sComID = CLASSJson.getString("comID");
			    
				if (sPermID != null && !"".equals(sPermID) && sComID != null && !"".equals(sComID)) {
					//删除role下的权限;
					try{
						String sql = "DELETE FROM PS_TZ_AQ_COMSQ_TBL WHERE CLASSID=? AND TZ_COM_ID=?";
						jdbcTemplate.update(sql,sPermID,sComID);
					}catch(DataAccessException e){
						e.printStackTrace();
					}
					
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
