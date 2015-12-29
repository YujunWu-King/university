package com.tranzvision.gd.TZPermissionDefnBundle.service.impl;

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
import com.tranzvision.gd.TZPermissionDefnBundle.dao.PsClassDefnMapper;
import com.tranzvision.gd.TZPermissionDefnBundle.model.PsClassDefn;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 类方法定义， 原PS类：TZ_GD_PLST_PKG:TZ_GD_PERMINFO_CLS
 * @author tang
 */
@Service("com.tranzvision.gd.TZPermissionDefnBundle.service.impl.PermissionInfoServiceImpl")
public class PermissionInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsClassDefnMapper psClassDefnMapper;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	/*新增许可权信息*/
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				// 类型标志;
			    String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				
				// 许可权信息;
				if("PERM".equals(strFlag)){
					// 许可权编号;
			        String strPermID = (String) infoData.get("permID");
			        //许可权描述;
			        String strPermDesc = (String) infoData.get("permDesc");
			        String sql = "select COUNT(1) from PSCLASSDEFN WHERE CLASSID=?";
			        int count = jdbcTemplate.queryForObject(sql, new Object[] { strPermID }, "Integer");
					if (count > 0) {
						errMsg[0] = "1";
						errMsg[1] = "许可权ID为：" + strPermID + "的信息已经存在，请修改许可权ID。";
					} else {
						PsClassDefn psClassDefn = new PsClassDefn();
						psClassDefn.setClassid(strPermID);
						psClassDefn.setClassdefndesc(strPermDesc);
						psClassDefn.setVersion(1);
						psClassDefn.setLastupddttm(new Date());
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						psClassDefn.setLastupdoprid(oprid);
						int i = psClassDefnMapper.insert(psClassDefn);
						if(i <= 0){
							errMsg[0] = "1";
							errMsg[1] = "保存失败";
						}
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
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				//JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				jacksonUtil.json2Map(strForm);
				// 类型标志;
			    String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> infoData = jacksonUtil.getMap("data");
				
				// 许可权信息;
				if("PERM".equals(strFlag)){
					// 许可权编号;
			        String strPermID = (String) infoData.get("permID");
			        //许可权描述;
			        String strPermDesc = (String) infoData.get("permDesc");
			        String sql = "select COUNT(1) from PSCLASSDEFN WHERE CLASSID=?";
			        int count = jdbcTemplate.queryForObject(sql, new Object[] { strPermID },"Integer");
					if (count > 0) {
						PsClassDefn psClassDefn = new PsClassDefn();
						psClassDefn.setClassid(strPermID);
						psClassDefn.setClassdefndesc(strPermDesc);
						psClassDefn.setVersion(1);
						psClassDefn.setLastupddttm(new Date());
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						psClassDefn.setLastupdoprid(oprid);
						int i = psClassDefnMapper.updateByPrimaryKeySelective(psClassDefn);
						if(i <= 0){
							errMsg[0] = "1";
							errMsg[1] = "保存失败";
						}
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
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();		
		try {
			
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("permID")) {
				// 类方法ID;
				String strPermID = jacksonUtil.getString("permID");
				PsClassDefn psClassDefn = psClassDefnMapper.selectByPrimaryKey(strPermID);
				if (psClassDefn != null) {
					// 组件注册信息;
					Map<String, Object> jsonMap = new HashMap<>();
					jsonMap.put("permID", psClassDefn.getClassid());
					jsonMap.put("permDesc", psClassDefn.getClassdefndesc());
					returnJsonMap.replace("formData", jsonMap);
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
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/*获取授权组件列表*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_COM_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "CLASSID", "TZ_COM_ID", "TZ_COM_MC"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("permID", rowList[0]);
					mapList.put("comID", rowList[1]);
					mapList.put("comName", rowList[2]);
					
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
	
	/* 删除许可权组件授权信息 */
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
				// 提交信息
				String strForm = actData[num];

				//JSONObject CLASSJson = PaseJsonUtil.getJson(strForm);
				jacksonUtil.json2Map(strForm);
				// 许可权ID;
				String sPermID = jacksonUtil.getString("permID");
				// 组件ID;
			    String sComID = jacksonUtil.getString("comID");
			    
				if (sPermID != null && !"".equals(sPermID) && sComID != null && !"".equals(sComID)) {
					//删除role下的权限;
					String sql = "DELETE FROM PS_TZ_AQ_COMSQ_TBL WHERE CLASSID=? AND TZ_COM_ID=?";
					jdbcTemplate.update(sql,new Object[]{sPermID,sComID});
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
