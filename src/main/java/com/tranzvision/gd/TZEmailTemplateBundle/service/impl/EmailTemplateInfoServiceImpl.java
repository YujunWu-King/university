package com.tranzvision.gd.TZEmailTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailTemplateBundle.dao.PsTzEmalTmplTblMapper;
import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzEmalTmplTbl;
import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzEmalTmplTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 邮件模板设置；原：TZ_GD_EMLSMSSET_PKG:TZ_GD_EMLTMPINFO_CLS
 * 
 * @author tang
 * @since 2015-11-19
 */
@Service("com.tranzvision.gd.TZEmailTemplateBundle.service.impl.EmailTemplateInfoServiceImpl")
public class EmailTemplateInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzEmalTmplTblMapper psTzEmalTmpTblMapper;
	
	/* 新增邮件模板设置信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 类型标志;
				String typeFlag = jacksonUtil.getString("typeFlag");
				if("EMLTMPLINFO".equals(typeFlag)){
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					// 邮件模板编号;
					String str_emlTmplId = (String) dataMap.get("emltempid");
					// 邮件模板名称;
					String str_emlTmplName = (String) dataMap.get("emltempname");
					//机构;
					String str_restemporg = (String) dataMap.get("emltemporg");
					//使用邮箱服务;
					String strEmailServId = (String) dataMap.get("tempemailserv");
					//是否启用
					String str_is_use = (String) dataMap.get("isuse");
					//是否判重
					String ifRpt = (String) dataMap.get("ifRpt");  
					//描述
					String str_emlTmplDesc = (String) dataMap.get("emltmpdesc");
					//元模版
					String str_resTmplId = (String) dataMap.get("restempid");
					//是否包含动态个性化内容
					String str_ispersonalize = (String) dataMap.get("ispersonalize");
					//是否html
					String str_ishtml = (String) dataMap.get("ishtml");
					// ueditor是否实例化
					String str_isUeditor = (String) dataMap.get("emlContentMode");
					//邮件主题
					String str_emlTmplSubject = (String) dataMap.get("emltmpsubject");

					//邮件内容
					String str_emlTmplContent="";
					if("Y".equals(str_ishtml)){
						if( "ueditor".equals(str_isUeditor)){
							str_emlTmplContent = jacksonUtil.getString("emltmpcontentHtml");
						}else{
							str_emlTmplContent = jacksonUtil.getString("emltmpcontent");
						}
					}else{
						str_emlTmplContent = jacksonUtil.getString("emltmpcontent");
					}
					
					/*检查选择的元模版是否可扩展子模版，如果不可扩展子模版，检查该元模版是否已经存在已经启用的业务模版，如果已经存在，则不允许保存*/
					boolean b_save = true;
					if("Y".equals(str_is_use)){
						String str_ymb_extent_c = "";
						String str_ymb_name = "";
						String extentSQL = "SELECT TZ_EXTEND_C_TMPL,TZ_YMB_NAME FROM PS_TZ_TMP_DEFN_TBL WHERE TZ_YMB_ID = ?";
						Map<String, Object> map = jdbcTemplate.queryForMap(extentSQL,new Object[]{str_resTmplId});
						str_ymb_extent_c = (String) map.get("TZ_EXTEND_C_TMPL");
						str_ymb_name = (String) map.get("TZ_YMB_NAME");
						if(!"Y".equals(str_ymb_extent_c)){
							int hasEmlTmplNum = 0;
							String hasEmlTmplSQL = "SELECT count(1) FROM PS_TZ_EMALTMPL_TBL WHERE TZ_YMB_ID = ? AND TZ_JG_ID = ? AND TZ_USE_FLAG = 'Y' AND TZ_TMPL_ID <> ?";
							hasEmlTmplNum = jdbcTemplate.queryForObject(hasEmlTmplSQL, new Object[]{str_resTmplId, str_restemporg, str_emlTmplId},"Integer");
							if(hasEmlTmplNum > 0){
								b_save = false;
								errMsg[0] = "1";
								errMsg[1] = "模版类型为" + str_ymb_name + "的模版只能存在一个启用的邮件模版，请重新选择模版类型。";
							}
						}
					}
					
					if(b_save){
						int existNum = 0;
						//编号是否存在;
						String existSQL = "SELECT count(1) FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_ID = ?";
						existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_emlTmplId},"Integer");
						if(existNum > 0){
							errMsg[0] = "1";
							errMsg[1] = "机构" + str_restemporg + "已经定义过模版编号为" + str_emlTmplId + "的邮件模版，请务重复定义。";
						}else{
							//名称是否已经被使用;
							existSQL = "SELECT count(1) FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_NAME = ? AND  TZ_TMPL_ID <> ?";
							existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_emlTmplName, str_emlTmplId},"Integer");
							if(existNum > 0){
								errMsg[0] = "1";
								errMsg[1] = "机构" + str_restemporg + "已经定义过模版名称为" + str_emlTmplName + "的邮件模版，请务重复定义。";
							}else{
								String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
								
								PsTzEmalTmplTbl psTzEmalTmpTbl = new PsTzEmalTmplTbl();
								psTzEmalTmpTbl.setTzJgId(str_restemporg);
								psTzEmalTmpTbl.setTzTmplId(str_emlTmplId);
								psTzEmalTmpTbl.setTzTmplName(str_emlTmplName);
								psTzEmalTmpTbl.setTzUseFlag(str_is_use);
								psTzEmalTmpTbl.setTzEmlIfPrt(ifRpt);
								psTzEmalTmpTbl.setTzTmplDesc(str_emlTmplDesc);
								psTzEmalTmpTbl.setTzYmbId(str_resTmplId);
								psTzEmalTmpTbl.setTzEmlservId(strEmailServId);
								psTzEmalTmpTbl.setTzDynamicFlag(str_ispersonalize);
								psTzEmalTmpTbl.setTzWebmalFlag(str_ishtml);
								psTzEmalTmpTbl.setTzMalSubjuect(str_emlTmplSubject);
								psTzEmalTmpTbl.setTzMalContent(str_emlTmplContent);
								psTzEmalTmpTbl.setRowAddedDttm(new Date());
								psTzEmalTmpTbl.setRowAddedOprid(oprid);
								psTzEmalTmpTbl.setRowLastmantDttm(new Date());
								psTzEmalTmpTbl.setRowLastmantOprid(oprid);
								psTzEmalTmpTblMapper.insert(psTzEmalTmpTbl);
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
		return strRet;
	}
	
	/* 新增邮件模板设置信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 类型标志;
				String typeFlag = jacksonUtil.getString("typeFlag");
				if("EMLTMPLINFO".equals(typeFlag)){
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					// 邮件模板编号;
					String str_emlTmplId = (String) dataMap.get("emltempid");
					// 邮件模板名称;
					String str_emlTmplName = (String) dataMap.get("emltempname");
					//机构;
					String str_restemporg = (String) dataMap.get("emltemporg");
					//使用邮箱服务;
					String strEmailServId = (String) dataMap.get("tempemailserv");
					//是否启用
					String str_is_use = (String) dataMap.get("isuse");
					//是否判重
					String ifRpt = (String) dataMap.get("ifRpt");  
					//描述
					String str_emlTmplDesc = (String) dataMap.get("emltmpdesc");
					//元模版
					String str_resTmplId = (String) dataMap.get("restempid");
					//是否包含动态个性化内容
					String str_ispersonalize = (String) dataMap.get("ispersonalize");
					//是否html
					String str_ishtml = (String) dataMap.get("ishtml");
					// ueditor是否实例化
					String str_isUeditor = (String) dataMap.get("emlContentMode");
					//邮件主题
					String str_emlTmplSubject = (String) dataMap.get("emltmpsubject");

					//邮件内容
					String str_emlTmplContent="";
					if("Y".equals(str_ishtml)){
						if( "ueditor".equals(str_isUeditor)){
							str_emlTmplContent = (String) dataMap.get("emltmpcontentHtml");
						}else{
							str_emlTmplContent = (String) dataMap.get("emltmpcontent");
						}
					}else{
						str_emlTmplContent = (String) dataMap.get("emltmpcontent");
					}
					
					/*检查选择的元模版是否可扩展子模版，如果不可扩展子模版，检查该元模版是否已经存在已经启用的业务模版，如果已经存在，则不允许保存*/
					boolean b_save = true;
					if("Y".equals(str_is_use)){
						String str_ymb_extent_c = "";
						String str_ymb_name = "";
						String extentSQL = "SELECT TZ_EXTEND_C_TMPL,TZ_YMB_NAME FROM PS_TZ_TMP_DEFN_TBL WHERE TZ_YMB_ID = ?";
						Map<String, Object> map = jdbcTemplate.queryForMap(extentSQL,new Object[]{str_resTmplId});
						str_ymb_extent_c = (String) map.get("TZ_EXTEND_C_TMPL");
						str_ymb_name = (String) map.get("TZ_YMB_NAME");
						if(!"Y".equals(str_ymb_extent_c)){
							int hasEmlTmplNum = 0;
							String hasEmlTmplSQL = "SELECT count(1) FROM PS_TZ_EMALTMPL_TBL WHERE TZ_YMB_ID = ? AND TZ_JG_ID = ? AND TZ_USE_FLAG = 'Y' AND TZ_TMPL_ID <> ?";
							hasEmlTmplNum = jdbcTemplate.queryForObject(hasEmlTmplSQL, new Object[]{str_resTmplId, str_restemporg, str_emlTmplId},"Integer");
							if(hasEmlTmplNum > 0){
								b_save = false;
								errMsg[0] = "1";
								errMsg[1] = "模版类型为" + str_ymb_name + "的模版只能存在一个启用的邮件模版，请重新选择模版类型。";
							}
						}
					}
					
					if(b_save){
						int existNum = 0;
						//编号是否存在;
						String existSQL = "SELECT count(1) FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_ID = ?";
						existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_emlTmplId},"Integer");
						if(existNum == 0){
							errMsg[0] = "1";
							errMsg[1] =  "模版编号为" + str_emlTmplId + "的邮件模版不存在。";
						}else{
							//名称是否已经被使用;
							existSQL = "SELECT count(1) FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_NAME = ? AND  TZ_TMPL_ID <> ?";
							existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_emlTmplName, str_emlTmplId},"Integer");
							if(existNum > 0){
								errMsg[0] = "1";
								errMsg[1] = "机构" + str_restemporg + "已经定义过模版名称为" + str_emlTmplName + "的邮件模版，请务重复定义。";
							}else{
								String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
								
								PsTzEmalTmplTbl psTzEmalTmpTbl = new PsTzEmalTmplTbl();
								psTzEmalTmpTbl.setTzJgId(str_restemporg);
								psTzEmalTmpTbl.setTzTmplId(str_emlTmplId);
								psTzEmalTmpTbl.setTzTmplName(str_emlTmplName);
								psTzEmalTmpTbl.setTzUseFlag(str_is_use);
								psTzEmalTmpTbl.setTzEmlIfPrt(ifRpt);
								psTzEmalTmpTbl.setTzTmplDesc(str_emlTmplDesc);
								psTzEmalTmpTbl.setTzYmbId(str_resTmplId);
								psTzEmalTmpTbl.setTzEmlservId(strEmailServId);
								psTzEmalTmpTbl.setTzDynamicFlag(str_ispersonalize);
								psTzEmalTmpTbl.setTzWebmalFlag(str_ishtml);
								psTzEmalTmpTbl.setTzMalSubjuect(str_emlTmplSubject);
								psTzEmalTmpTbl.setTzMalContent(str_emlTmplContent);
								psTzEmalTmpTbl.setRowLastmantDttm(new Date());
								psTzEmalTmpTbl.setRowLastmantOprid(oprid);
								psTzEmalTmpTblMapper.updateByPrimaryKeySelective(psTzEmalTmpTbl);
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
		return strRet;
	}
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("restempid")){
				String strTmpId = jacksonUtil.getString("restempid");
				String tmpSQL = "SELECT TZ_YMB_CSLBM,TZ_YMB_NRGM,TZ_DSREC_NAME,TZ_DSREC_ALIAS from PS_TZ_TMP_DEFN_TBL WHERE TZ_YMB_ID = ?";
				Map<String, Object> map = jdbcTemplate.queryForMap(tmpSQL, new Object[]{strTmpId});
				if(map != null){
					  //String str_TmpNrgm = (String) map.get("TZ_YMB_NRGM");
					  String str_ymb_clsbm = (String) map.get("TZ_YMB_CSLBM");
					  String str_ds_recName = (String) map.get("TZ_DSREC_NAME");
					  String str_ds_recAlias = (String) map.get("TZ_DSREC_ALIAS");
					  
					  if(str_ds_recAlias == null || "".equals(str_ds_recAlias)){
						  str_ds_recAlias = str_ds_recName;
					  }
					  int total = 0;
					  //元模板参数列表;
					  String sql = "SELECT TZ_PARA_ID,TZ_PARA_ALIAS FROM PS_TZ_TMP_PARA_TBL WHERE TZ_YMB_ID=?";
					  List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{strTmpId});
					  if(list != null && list.size()>0){
						  for(int i = 0 ; i < list.size(); i++){
							  total = total + 1;
							  String str_ParaId = (String) list.get(i).get("TZ_PARA_ID");
							  String str_paraAlias = (String) list.get(i).get("TZ_PARA_ALIAS");
							  String str_ParaItem = "[" + str_ymb_clsbm + "." + str_ParaId + "." + str_paraAlias + "]";
							  
							  Map<String, Object> mapList = new HashMap<String, Object>();
							  mapList.put("parainfoitem", str_ParaItem);
							  listData.add(mapList);
						  }
						  
						  mapRet.replace("total", total);
						  mapRet.replace("root", listData);
					  }
				}
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("emltempid") && jacksonUtil.containsKey("emltemporg")) {
				// 邮件服务器参数id;
				String strTmpId = jacksonUtil.getString("emltempid");
				String strOrgName = jacksonUtil.getString("emltemporg");
				
				PsTzEmalTmplTblKey psTzEmalTmpTblKey = new PsTzEmalTmplTblKey();
				psTzEmalTmpTblKey.setTzJgId(strOrgName);
				psTzEmalTmpTblKey.setTzTmplId(strTmpId);
				
				PsTzEmalTmplTbl psTzEmalTmpTbl = psTzEmalTmpTblMapper.selectByPrimaryKey(psTzEmalTmpTblKey);
				if(psTzEmalTmpTbl != null){
					returnJsonMap.put("emltempid", strTmpId);
					returnJsonMap.put("emltempname", psTzEmalTmpTbl.getTzTmplName());
					returnJsonMap.put("emltemporg", strOrgName);
					returnJsonMap.put("restempid", psTzEmalTmpTbl.getTzYmbId());
					returnJsonMap.put("tempemailserv",psTzEmalTmpTbl.getTzEmlservId() );
					returnJsonMap.put("isuse",psTzEmalTmpTbl.getTzUseFlag() );
					returnJsonMap.put("emltmpdesc", psTzEmalTmpTbl.getTzTmplDesc());
					returnJsonMap.put("ispersonalize", psTzEmalTmpTbl.getTzDynamicFlag());
					returnJsonMap.put("ishtml", psTzEmalTmpTbl.getTzWebmalFlag());
					returnJsonMap.put("emltmpsubject", psTzEmalTmpTbl.getTzMalSubjuect());
					returnJsonMap.put("emltmpcontent", psTzEmalTmpTbl.getTzMalContent());
					returnJsonMap.put("emltmpcontentHtml", psTzEmalTmpTbl.getTzMalContent());
					returnJsonMap.put("ifRpt", psTzEmalTmpTbl.getTzEmlIfPrt());
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
}
