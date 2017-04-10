package com.tranzvision.gd.TZClmsCsCkzlManagementBundle.service.impl;

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
import com.tranzvision.gd.TZClmsCsCkzlManagementBundle.dao.PsTzCkzlTMapper;
import com.tranzvision.gd.TZClmsCsCkzlManagementBundle.model.PsTzCkzlT;
import com.tranzvision.gd.TZComRegMgBundle.dao.PsTzAqComzcTblMapper;
import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqComzcTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

	/**
	 * 功能说明：参考资料相关类
	 * @author 顾贤达
	 * 2017-1-19
	 * 
	 */
	@Service("com.tranzvision.gd.TZClmsCsCkzlManagementBundle.service.impl.TzZdcsCkzlManageServiceImpl")
	public class TzZdcsCkzlManageServiceImpl extends FrameworkImpl {
		@Autowired
		private SqlQuery jdbcTemplate;
		@Autowired
		private TzLoginServiceImpl tzLoginServiceImpl;
		@Autowired
		private HttpServletRequest request;
		@Autowired
		private PsTzCkzlTMapper psTzCkzlTMapper;
		@Autowired
		private FliterForm fliterForm;
		@Autowired
		private GetSeqNum getSeqNum;

		
		
		/* 查询列表 */
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
				String[][] orderByArr = new String[][] { { "TZ_CKZL_ID", "ASC" } };

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_CKZL_ID","TZ_CKZL_NAME","TZ_APP_JAVA" };

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
				if (obj != null && obj.length > 0) {
					
					
					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("ckzlid", rowList[0]);
						mapList.put("ckzlName", rowList[1]);
						mapList.put("java", rowList[2]);
						
						
						
						listData.add(mapList);
					}

					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);

				}

			} catch (Exception e) {
				e.printStackTrace();
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
			
			return jacksonUtil.Map2json(mapRet);
		}

		/* 删除信息 */
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
					// 表单内容;
					String strForm = actData[num];
					// 将字符串转换成json;
					jacksonUtil.json2Map(strForm);
					// 组件ID;
					
				//	String sComID = jacksonUtil.getString("comID");
					String strCkzlID = jacksonUtil.getString("ckzlid");
					
					System.out.println(strCkzlID);
					psTzCkzlTMapper.deleteByPrimaryKey(strCkzlID);
					
					String comPageSql = "DELETE FROM PS_TZ_CKZL_T WHERE TZ_CKZL_ID=?";
					jdbcTemplate.update(comPageSql,new Object[]{strCkzlID});		
					
				}
			} catch (Exception e) {
				errMsg[0] = "1";
				errMsg[1] = e.toString();
				return strRet;
			}

			return strRet;
		}
		
		
		/* 新增组件注册信息 */
		@Override
		public String tzAdd(String[] actData, String[] errMsg) {
			String strRet = "{}";
			// 若参数为空，直接返回;
			if (actData == null || actData.length == 0) {
				return strRet;
			}

			try {
				JacksonUtil jacksonUtil = new JacksonUtil();
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					// 表单内容;
					String strForm = actData[num];
					// 将字符串转换成json;
					jacksonUtil.json2Map(strForm);
					// 类型标志;
					String strFlag = jacksonUtil.getString("typeFlag");
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					if ("COM".equals(strFlag)) {
						
						int NewCkzlID=getSeqNum.getSeqNum("PS_TZ_CKZL_T", "TZ_CKZL_ID");
						String strNewCkzlID =	String.valueOf(NewCkzlID);
						String strCkzlID =	String.valueOf(strNewCkzlID);
//						String strCkzlID = (String) infoData.get("ckzlid");
						
						String strCkzlName = (String) infoData.get("ckzlName");
						String strJava = (String) infoData.get("java");
						String strJGID = (String) infoData.get("jgName");

						// 是否已经存在;
						String comExistSql = "SELECT 'Y' FROM PS_TZ_CKZL_T WHERE TZ_CKZL_ID=?";
						String isExist = "";
						isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strCkzlID }, "String");

						if ("Y".equals(isExist)) {
							errMsg[0] = "1";
							errMsg[1] = "组件ID为：" + strCkzlID + "的信息已经注册，请修改组件ID。";
							return strRet;
						}
						
//						psTzCkzlTMapper.deleteByPrimaryKey(strCkzlID);
					//	PsTzAqComzcTbl psTzAqComzcTbl = new PsTzAqComzcTbl();
						PsTzCkzlT psTzCkzlT = new PsTzCkzlT();
						psTzCkzlT.setTzCkzlId(strCkzlID);
						psTzCkzlT.setTzCkzlName(strCkzlName);
						psTzCkzlT.setTzAppJava(strJava);
						psTzCkzlT.setTzJgId(strJGID);
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						psTzCkzlT.setRowAddedOprid(oprid);
						psTzCkzlT.setRowAddedDttm(new Date());
						psTzCkzlT.setRowLastmantOprid(oprid);
						psTzCkzlT.setRowLastmantDttm(new Date());
						int i = psTzCkzlTMapper.insert(psTzCkzlT);
						if(i <= 0){
							errMsg[0] = "1";
							errMsg[1] = "保存失败";
						}
					}

			/*		if ("PAGE".equals(strFlag)) {
						strRet = this.tzUpdatePageInfo(infoData, errMsg);
					}
*/
				}
			} catch (Exception e) {
				errMsg[0] = "1";
				errMsg[1] = e.toString();
				return strRet;
			}

			return strRet;
		}

		/* 修改组件注册信息 */
		@Override
		public String tzUpdate(String[] actData, String[] errMsg) {
			// 返回值;
			String strRet = "{}";

			// 若参数为空，直接返回;
			if (actData == null || actData.length == 0) {
				return strRet;
			}

			try {
				JacksonUtil jacksonUtil = new JacksonUtil();
				int num = 0;
				for (num = 0; num < actData.length; num++) {
					// 表单内容;
					String strForm = actData[num];
					// 将字符串转换成json;
					jacksonUtil.json2Map(strForm);
					// 类型标志;
					String strFlag = jacksonUtil.getString("typeFlag");
					// 信息内容;
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					if ("COM".equals(strFlag)) {
						
						// 页面信息;
						String strCkzlID = (String) infoData.get("ckzlid");
						String strCkzlName = (String) infoData.get("ckzlName");
						String strJava = (String) infoData.get("java");
						String strJGID = (String) infoData.get("jgName");
						System.out.println(strJGID);
						System.out.println(strJava);
						// 是否已经存在;
						String comExistSql = "SELECT 'Y' FROM PS_TZ_CKZL_T WHERE TZ_CKZL_ID=?";
						String isExist = "";
						isExist = jdbcTemplate.queryForObject(comExistSql, new Object[] { strCkzlID }, "String");

						if (!"Y".equals(isExist)) {
							errMsg[0] = "1";
							errMsg[1] = "组件ID为：" + strCkzlID + "的信息不存在。";
							return strRet;
						}
				
						
				/*		PsTzAqComzcTbl psTzAqComzcTbl = new PsTzAqComzcTbl();
						psTzAqComzcTbl.setTzComId(strComID);
						psTzAqComzcTbl.setTzComMc(comName);
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						psTzAqComzcTbl.setRowLastmantOprid(oprid);
						psTzAqComzcTbl.setRowLastmantDttm(new Date());
						int i = psTzAqComzcTblMapper.updateByPrimaryKeySelective(psTzAqComzcTbl);
						if(i <= 0){
							errMsg[0] = "1";
							errMsg[1] = "更新失败";
						
							
						PsTzCkzlT A=new PsTzCkzlT();
						A.setTzCkzlId(strCkzlID);
						A.setTzCkzlId(strJGID);
						A.setTzCkzlName(strCkzlName);
				*/			
						PsTzCkzlT psTzCkzlT = new PsTzCkzlT();
						psTzCkzlT.setTzCkzlId(strCkzlID);
						psTzCkzlT.setTzJgId(strJGID);
						psTzCkzlT.setTzCkzlName(strCkzlName);
						psTzCkzlT.setTzAppJava(strJava);
						String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
						psTzCkzlT.setRowLastmantOprid(oprid);
						psTzCkzlT.setRowLastmantDttm(new Date());
						
						
						int i = psTzCkzlTMapper.updateByPrimaryKeySelective(psTzCkzlT);
				//		int i = psTzCkzlTMapper.updateByPrimaryKey(A);
				//		int i=psTzCkzlTMapper.updateByPrimaryKey(A);
						
						
						if(i <= 0){
							errMsg[0] = "1";
							errMsg[1] = "更新失败";
						}
					}

			/*		if ("PAGE".equals(strFlag)) {
						strRet = this.tzUpdatePageInfo(infoData, errMsg);
					}
			 			*/
				}
			} catch (Exception e) {
				errMsg[0] = "1";
				errMsg[1] = e.toString();
				return strRet;
			}

			return strRet;
		}

		/* 获取组件注册信息 */
		@Override
		public String tzQuery(String strParams, String[] errMsg) {
			// 返回值;
			String strRet = "";
			Map<String, Object> returnJsonMap = new HashMap<String, Object>();
			returnJsonMap.put("formData", "");
			JacksonUtil jacksonUtil = new JacksonUtil();
			try {
				jacksonUtil.json2Map(strParams);

				if (jacksonUtil.containsKey("ckzlid")) {
					
					String strCkzlID = jacksonUtil.getString("ckzlid");
					String strCkzlName = jacksonUtil.getString("ckzlName");
					String strJava = jacksonUtil.getString("java");
					if (strCkzlID != null && !"".equals(strCkzlID)) {

					//	PsTzCkzlT psTzCkzlT = psTzCkzlTMapper.selectByPrimaryKey(strCkzlID);
					//	if (psTzCkzlT != null) {
							// 组件注册信息;
							Map<String, Object> jsonMap = new HashMap<>();
							
							jsonMap.put("ckzlid", strCkzlID);
							jsonMap.put("ckzlName",strCkzlName );
							jsonMap.put("java", strJava);
							returnJsonMap.replace("formData", jsonMap);
					/*	} else {
							errMsg[0] = "1";
							errMsg[1] = "无法获取组件信息1";
						}
*/
					} else {
						errMsg[0] = "1";
						errMsg[1] = "无法获取组件信息2";
					}
				} else {
					errMsg[0] = "1";
					errMsg[1] = "无法获取组件信息3";
				}
			} catch (Exception e) {
				e.printStackTrace();
				errMsg[0] = "1";
				errMsg[1] = e.toString();
			}
			strRet = jacksonUtil.Map2json(returnJsonMap);
			return strRet;
		}
	}