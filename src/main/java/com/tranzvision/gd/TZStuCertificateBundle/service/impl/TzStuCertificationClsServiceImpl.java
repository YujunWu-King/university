package com.tranzvision.gd.TZStuCertificateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZStuCertificateBundle.dao.PsTzCertInfoTblMapper;
import com.tranzvision.gd.TZStuCertificateBundle.dao.PsTzCertOprLogMapper;
import com.tranzvision.gd.TZStuCertificateBundle.model.PsTzCertInfoTbl;
import com.tranzvision.gd.TZStuCertificateBundle.model.PsTzCertOprLog;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author LDD 学生证书管理
 */
@Service("com.tranzvision.gd.TZStuCertificateBundle.service.impl.TzStuCertificationClsServiceImpl")
public class TzStuCertificationClsServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private PsTzCertInfoTblMapper psTzCertInfoTblMapper;

	@Autowired
	private PsTzCertOprLogMapper psTzCertOprLogMapper;

	/* 学生证书管理列表 */

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;

	/* 学生证书管理列表 */

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
			String[][] orderByArr = new String[][] { { "TZ_SORT", "ASC" } };
			 //String[][] orderByArr = new String[][] {};
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_REALNAME", "TZ_GENDER", "TZ_CLASS_NAME", "TZ_PRG_NAME", "TZ_MOBILE_PHONE",
					"TZ_EMAIL", "TZ_CERT_TYPE_NAME", "TZ_ZHSH_ID", "TZ_BF_STATUS", "TZ_VIEW_NUM", "TZ_SHARE_NUM",

					"TZ_YZ_NUM", "OPRID", "TZ_SEQNUM" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				@SuppressWarnings("unchecked")
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("TZ_REALNAME", rowList[0]);
					mapList.put("TZ_GENDER", rowList[1]);
					mapList.put("TZ_CLASS_NAME", rowList[2]);
					mapList.put("TZ_PRG_NAME", rowList[3]);
					mapList.put("TZ_MOBILE_PHONE", rowList[4]);
					mapList.put("TZ_EMAIL", rowList[5]);
					mapList.put("TZ_CERT_TYPE_NAME", rowList[6]);
					mapList.put("TZ_ZHSH_ID", rowList[7]);
					mapList.put("TZ_BF_STATUS", rowList[8]);
					mapList.put("TZ_VIEW_NUM", rowList[9]);
					mapList.put("TZ_SHARE_NUM", rowList[10]);
					mapList.put("TZ_YZ_NUM", rowList[11]);
					mapList.put("OPRID", rowList[12]);
					mapList.put("TZ_SEQNUM", rowList[13]);

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
   /**
    * 给学生发送电子邮件或短信
    */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		String audID = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return strRet;
		}
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");
				String flag = jacksonUtil.getString("flag");
				if (list != null && list.size() > 0) {
					// 群发邮件添加听众;
					audID = createTaskServiceImpl.createAudience("", str_jg_id, "电子结业证书短信邮件发送", "JSRW");
					for (int num_1 = 0; num_1 < list.size(); num_1++) {
						Map<String, Object> map = list.get(num_1);
						String name = String.valueOf(map.get("name"));
						String email = String.valueOf(map.get("email"));
						String phone = String.valueOf(map.get("phone"));
						String tzSeq = String.valueOf(map.get("tzSeq"));
						String oprid = map.get("oprid") == null ? "" : String.valueOf(map.get("oprid"));
						if (!("").equals(email) && ("E").equals(flag)) {
							createTaskServiceImpl.addAudCy(audID, name, "", phone, "", email, "", "", oprid, "", tzSeq,"");
						}
						if (!("").equals(phone) && ("M").equals(flag)) {
							createTaskServiceImpl.addAudCy(audID, name, "", phone, "", email, "", "", oprid, "", tzSeq,"");
						}
					}
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("audienceId", audID);
					strRet = jacksonUtil.Map2json(mapRet);
				}else{
					strRet=this.sendEmailForAll(flag,errMsg);
					return strRet;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
 /**
  * 给所有学生发送短信或者邮件
  * @param flag 
  * @param errMsg
  * @return audId
  */
 private String sendEmailForAll(String flag, String[] errMsg) {
	String strRet="";
	JacksonUtil jacksonUtil = new JacksonUtil();
    Map<String, Object> mapData = new HashMap<String, Object>();
    try {
	    String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String audID = createTaskServiceImpl.createAudience("", str_jg_id, "电子结业证书短信邮件发送", "JSRW");
	    List<Map<String,Object>> list=jdbcTemplate.queryForList("select TZ_SEQNUM,TZ_REALNAME,TZ_MOBILE_PHONE,TZ_EMAIL from PS_TZ_CERT_INFO_TBL where TZ_BF_STATUS='B'");
	    for(int i=0;i<list.size();i++){
	    	mapData=list.get(i);
	    	String name=mapData.get("TZ_REALNAME")==null?"":mapData.get("TZ_REALNAME").toString();
	    	String tzSeq=mapData.get("TZ_SEQNUM")==null?"":mapData.get("TZ_SEQNUM").toString();
	    	String phone=mapData.get("TZ_MOBILE_PHONE")==null?"":mapData.get("TZ_MOBILE_PHONE").toString();
	    	String email=mapData.get("TZ_EMAIL")==null?"":mapData.get("TZ_EMAIL").toString();
	    	String oprid=mapData.get("OPRID")==null?"":mapData.get("OPRID").toString();
	    	if (!("").equals(email) && ("E").equals(flag)) {
				createTaskServiceImpl.addAudCy(audID, name, "", phone, "", email, "", "", oprid, "", tzSeq,"");
			}
			if (!("").equals(phone) && ("M").equals(flag)) {
				createTaskServiceImpl.addAudCy(audID, name, "", phone, "", email, "", "", oprid, "", tzSeq,"");
			}
	    }
	    Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("audienceId", audID);
		strRet = jacksonUtil.Map2json(mapRet);
	} catch (Exception e) {
		e.printStackTrace();
		errMsg[0] = "1";
		errMsg[1] = e.toString();
	}
   
	return strRet;
 }
	/**
	 * add by caoy
	 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("TZ_SEQNUM")) {
				// 保存从数据库获取的数据
				Map<String, Object> dataMap = null;

				// 证书ID
				String TZ_SEQNUM = jacksonUtil.getString("TZ_SEQNUM");

				String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);

				String str_name = "",eng_name = "", oprid = "";
				String str_email = "", str_phone = "", className = "", certTypeName = "", certNo = "", issueDate = "",
						certStatus = "", certImage = "", certQRCode = "";

				String workUnit = "", department = "", post = "", contactInformation = "", zipCode = "", XXXXX = "";
				int queryTimes = 0, sharingTimes = 0, verificationTimes = 0;
				String TZ_CZ_TYPE = "", TZ_ZHSH_IMAGE_URL = "", TZ_ZHGL_IMAGE_URL = "", TZ_CERT_IMAGE_URL = "";

				String TZ_ZHDX_REASON_ID = "";
				String TZ_DX_DEMO = "";
				// 证书信息
				String sql = tzSQLObject.getSQLText("SQL.TZStuCertificateBundle.TZSelectUserInfo");
				dataMap = null;
				dataMap = jdbcTemplate.queryForMap(sql, new Object[] { TZ_SEQNUM });

				if (dataMap != null) {
					oprid = (String) dataMap.get("OPRID");
					str_name = (String) dataMap.get("TZ_REALNAME");
					eng_name = (String) dataMap.get("TZ_ENG_NAME");
					str_email = (String) dataMap.get("TZ_EMAIL");
					str_phone = (String) dataMap.get("TZ_MOBILE_PHONE");
					className = (String) dataMap.get("TZ_CLASS_NAME");
					certTypeName = (String) dataMap.get("TZ_CERT_TYPE_NAME");
					certNo = (String) dataMap.get("TZ_ZHSH_ID");
					issueDate = (String) dataMap.get("TZ_BF_RQ");
					certStatus = (String) dataMap.get("TZ_BF_STATUS");
					TZ_ZHDX_REASON_ID = (String) dataMap.get("TZ_ZHDX_REASON_ID");
					TZ_DX_DEMO = (String) dataMap.get("TZ_DX_DEMO");

					Map<String, Object> jsonMap2 = new HashMap<String, Object>();
					jsonMap2.put("SEQNUM", TZ_SEQNUM);
					jsonMap2.put("OPRID", oprid);
					jsonMap2.put("userName", str_name);
					jsonMap2.put("engName", eng_name);
					jsonMap2.put("userEmail", str_email);
					jsonMap2.put("userPhone", str_phone);
					jsonMap2.put("className", className);
					jsonMap2.put("certTypeName", certTypeName);
					jsonMap2.put("certNo", certNo);
					jsonMap2.put("issueDate", issueDate);
					jsonMap2.put("certStatus", certStatus);

					if (certStatus.equals("D")) {
						jsonMap2.put("dxReason", TZ_ZHDX_REASON_ID);
						jsonMap2.put("dxDemo", TZ_DX_DEMO);
					} else {
						jsonMap2.put("dxReason", "");
						jsonMap2.put("dxDemo", "");
					}

					jsonMap2.put("jgId", str_jg_id);

					jsonMap2.put("picOp", "N");
					jsonMap2.put("certImage", certImage);
					jsonMap2.put("certQRCode", certQRCode);

					jsonMap2.put("workUnit", workUnit);
					jsonMap2.put("department", department);
					jsonMap2.put("post", post);
					jsonMap2.put("contactInformation", contactInformation);
					jsonMap2.put("zipCode", zipCode);
					jsonMap2.put("XXXXX", XXXXX);

					// 获取证书历史信息
					sql = tzSQLObject.getSQLText("SQL.TZStuCertificateBundle.TZSelectStuCertHistory");
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { certNo });

					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							TZ_CZ_TYPE = (String) list.get(i).get("TZ_CZ_TYPE");
							if (TZ_CZ_TYPE.equals("CK")) {
								queryTimes = queryTimes + 1;
							} else if (TZ_CZ_TYPE.equals("ZF")) {
								sharingTimes = sharingTimes + 1;
							} else if (TZ_CZ_TYPE.equals("YZ")) {
								sharingTimes = sharingTimes + 1;
							}
						}
					}
					jsonMap2.put("queryTimes", queryTimes);
					jsonMap2.put("sharingTimes", sharingTimes);
					jsonMap2.put("verificationTimes", verificationTimes);

					// 查询用户头像
					sql = tzSQLObject.getSQLText("SQL.TZStuCertificateBundle.TZSelectUserPic");
					dataMap = jdbcTemplate.queryForMap(sql, new Object[] { oprid });
					String titleImageUrl = "";
					if (dataMap != null) {
						String tzAttAUrl = (String) dataMap.get("TZ_ATT_A_URL");
						String sysImgName = (String) dataMap.get("TZ_ATTACHSYSFILENA");
						if (tzAttAUrl != null && !"".equals(tzAttAUrl) && sysImgName != null
								&& !"".equals(sysImgName)) {
							if (tzAttAUrl.endsWith("/")) {
								titleImageUrl = tzAttAUrl + sysImgName;
							} else {
								titleImageUrl = tzAttAUrl + "/" + sysImgName;
							}
						}
					}

					//System.out.println("titleImageUrl:" + titleImageUrl);
					jsonMap2.put("titleImageUrl", titleImageUrl);

					// 查询证书相关图片
					sql = tzSQLObject.getSQLText("SQL.TZStuCertificateBundle.TZSelectStuCertPic");
					dataMap = jdbcTemplate.queryForMap(sql, new Object[] { TZ_SEQNUM });
					String TZ_ZHSH_URL = "", TZ_ZHGL_URL = "";
					// TZ_ZHSH_IMAGE_URL = "", TZ_ZHGL_IMAGE_URL = "",
					// TZ_CERT_IMAGE_URL = ""
					if (dataMap != null) {
						String TZ_ZHSH_IMAGE = (String) dataMap.get("TZ_ZHSH_IMAGE");
						String ZHSH_URL = (String) dataMap.get("TZ_ZHSH_IMAGE_URL");
						String TZ_ZHGL_IMAGE = (String) dataMap.get("TZ_ZHGL_IMAGE");
						String ZHGL_URL = (String) dataMap.get("TZ_ZHGL_IMAGE_URL");
						String TZ_CERT_IMAGE = (String) dataMap.get("TZ_CERT_IMAGE");
						String CERT_URL = (String) dataMap.get("TZ_CERT_IMAGE_URL");

						TZ_ZHSH_URL = (String) dataMap.get("TZ_ZHSH_URL");
						TZ_ZHGL_URL = (String) dataMap.get("TZ_ZHGL_URL");
						if (ZHSH_URL != null && !"".equals(ZHSH_URL) && TZ_ZHSH_IMAGE != null
								&& !"".equals(TZ_ZHSH_IMAGE)) {
							if (ZHSH_URL.endsWith("/")) {
								TZ_ZHSH_IMAGE_URL = ZHSH_URL + TZ_ZHSH_IMAGE;
							} else {
								TZ_ZHSH_IMAGE_URL = ZHSH_URL + "/" + TZ_ZHSH_IMAGE;
							}
						}
						if (ZHGL_URL != null && !"".equals(ZHGL_URL) && TZ_ZHGL_IMAGE != null
								&& !"".equals(TZ_ZHGL_IMAGE)) {
							if (ZHGL_URL.endsWith("/")) {
								TZ_ZHGL_IMAGE_URL = ZHGL_URL + TZ_ZHGL_IMAGE;
							} else {
								TZ_ZHGL_IMAGE_URL = ZHGL_URL + "/" + TZ_ZHGL_IMAGE;
							}
						}
						if (CERT_URL != null && !"".equals(CERT_URL) && TZ_CERT_IMAGE != null
								&& !"".equals(TZ_CERT_IMAGE)) {
							if (CERT_URL.endsWith("/")) {
								TZ_CERT_IMAGE_URL = CERT_URL + TZ_CERT_IMAGE;
							} else {
								TZ_CERT_IMAGE_URL = CERT_URL + "/" + TZ_CERT_IMAGE;
							}
						}
					}

					jsonMap2.put("certImage", TZ_CERT_IMAGE_URL);
					jsonMap2.put("certXKQRCode", TZ_ZHSH_IMAGE_URL);
					jsonMap2.put("certGLQRCode", TZ_ZHGL_IMAGE_URL);
					jsonMap2.put("certXKQRUrl", TZ_ZHSH_URL);
					jsonMap2.put("certGLQRUrl", TZ_ZHGL_URL);
					//System.out.println("certImage:" + certImage);
					//System.out.println("certXKQRCode:" + TZ_ZHSH_IMAGE_URL);
					//System.out.println("certGLQRCode:" + TZ_ZHGL_IMAGE_URL);
					//System.out.println("certXKQRUrl:" + TZ_ZHSH_URL);
					//System.out.println("certGLQRUrl:" + TZ_ZHGL_URL);

					returnJsonMap.replace("formData", jsonMap2);
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 吊销学生证书 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("SEQNUM", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String SEQNUM = jacksonUtil.getString("SEQNUM");
				String deleteReason = jacksonUtil.getString("deleteReason");
				String remarks = jacksonUtil.getString("remarks");

				//System.out.println("SEQNUM:" + SEQNUM);
				//System.out.println("deleteReason:" + deleteReason);
				//System.out.println("remarks:" + remarks);

				PsTzCertInfoTbl psTzCertInfoTbl = new PsTzCertInfoTbl();
				psTzCertInfoTbl.setTzSeqnum(new Long(SEQNUM));
				// TZ_ZHDX_REASON_ID
				psTzCertInfoTbl.setTzZhdxReasonId(deleteReason);
				// TZ_DX_DEMO
				psTzCertInfoTbl.setTzDxDemo(remarks);
				// TZ_BF_STATUS
				psTzCertInfoTbl.setTzBfStatus("D");
				// TZ_EXPRQ
				psTzCertInfoTbl.setTzExprq(new java.util.Date());
				String opid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				psTzCertInfoTbl.setRowLastmantOprid(opid);
				psTzCertInfoTbl.setRowLastmantDttm(new java.util.Date());

				int i = psTzCertInfoTblMapper.updateByPrimaryKeySelective(psTzCertInfoTbl);
				if (i > 0) {
					PsTzCertInfoTbl certInfo = psTzCertInfoTblMapper.selectByPrimaryKey(new Long(SEQNUM));
					// 插入操作日志表
					PsTzCertOprLog psTzCertOprLog = new PsTzCertOprLog();
					// TZ_CERT_LSH
					// String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_DEFN_T",
					// "TZ_SITEI_ID"))
					psTzCertOprLog.setTzCertLsh(
							new Long(String.valueOf(getSeqNum.getSeqNum("PS_TZ_CERT_OPR_LOG", "TZ_CERT_LSH"))));
					psTzCertOprLog.setTzJgId(certInfo.getTzJgId());
					psTzCertOprLog.setTzCzType("DX");
					psTzCertOprLog.setOprid(certInfo.getOprid());
					psTzCertOprLog.setTzCertTypeId(certInfo.getTzCertTypeId());
					psTzCertOprLog.setTzZhshId(certInfo.getTzZhshId());
					psTzCertOprLog.setRowAddedDttm(new java.util.Date());
					psTzCertOprLog.setRowAddedOprid(opid);
					psTzCertOprLog.setRowLastmantOprid(opid);
					psTzCertOprLog.setRowLastmantDttm(new java.util.Date());

					psTzCertOprLogMapper.insertSelective(psTzCertOprLog);
					returnJsonMap.replace("SEQNUM", SEQNUM);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "站点模板集合信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 修改头像 */
	@Override
	@Transactional
	public String tzOther(String oprType, String strParams, String[] errMsg) {
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("SEQNUM", "");
		JacksonUtil jacksonUtil = new JacksonUtil();

		// ////System.out.println("strParams：" + strParams);
		try {
			// 加载报名表模板字段
			if ("UpdateTitleImage".equals(oprType)) {
				jacksonUtil.json2Map(strParams);
				// 报名表导出模版编号;
				String picOp = jacksonUtil.getString("picOp");
				String picOldName = jacksonUtil.getString("picOldName");
				String picSysName = jacksonUtil.getString("picSysName");
				String picPath = jacksonUtil.getString("picPath");
				String OPRID = jacksonUtil.getString("OPRID");
				// 什么都不错
				String sql = "";
				//System.out.println("picOp：" + picOp);
				//System.out.println("picOldName：" + picOldName);
				//System.out.println("picSysName：" + picSysName);
				//System.out.println("picPath：" + picPath);
				//System.out.println("OPRID：" + OPRID);
				if (picOp.equals("N")) {

					return "";
				}
				// 删除
				int i = 0;
				if (picOp.equals("D")) {
					sql = "delete from PS_TZ_OPR_PHOTO_T where TZ_ATTACHSYSFILENA in (select TZ_ATTACHSYSFILENA from PS_TZ_OPR_PHT_GL_T where OPRID=?)";
					i = jdbcTemplate.update(sql, new Object[] { OPRID });
					//System.out.println("i：" + i);
					sql = "update PS_TZ_OPR_PHT_GL_T set TZ_ATTACHSYSFILENA='' where OPRID=?";
					i = jdbcTemplate.update(sql, new Object[] { OPRID });
					//System.out.println("i：" + i);
				}
				// 上传
				if (picOp.equals("Y")) {
					String realPath = request.getServletContext().getRealPath(picPath);
					sql = "update PS_TZ_OPR_PHOTO_T set TZ_ATTACHSYSFILENA=?,TZ_ATTACHFILE_NAME=?,TZ_ATT_P_URL=?,TZ_ATT_A_URL=?   where TZ_ATTACHSYSFILENA in (select TZ_ATTACHSYSFILENA from PS_TZ_OPR_PHT_GL_T where OPRID=?)";
					i = jdbcTemplate.update(sql, new Object[] { picSysName, picOldName, realPath, picPath, OPRID });
					//System.out.println("i：" + i);
					if (i <= 0) {
						sql = "insert into PS_TZ_OPR_PHOTO_T values(?,?,?,?)";
						i = jdbcTemplate.update(sql, new Object[] { picSysName, picOldName, realPath, picPath });
					}
					//System.out.println("i：" + i);
					sql = "update PS_TZ_OPR_PHT_GL_T set TZ_ATTACHSYSFILENA=? where OPRID=?";
					i = jdbcTemplate.update(sql, new Object[] { picSysName, OPRID });
					//System.out.println("i：" + i);
					if (i <= 0) {
						sql = "insert into PS_TZ_OPR_PHT_GL_T values(?,?)";
						i = jdbcTemplate.update(sql, new Object[] { OPRID, picSysName });
					}
					//System.out.println("i：" + i);
					if (i <= 0) {
						errMsg[0] = "1";
						errMsg[1] = "图像处理失败";
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		String strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
}
