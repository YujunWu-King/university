package com.tranzvision.gd.TZZnxTemplateBundle.service.impl;

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
import com.tranzvision.gd.TZZnxTemplateBundle.dao.PsTzZnxTmplTblMapper;
import com.tranzvision.gd.TZZnxTemplateBundle.model.PsTzZnxTmplTbl;
import com.tranzvision.gd.TZZnxTemplateBundle.model.PsTzZnxTmplTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author 
 * @since 2017-01-17
 */
@Service("com.tranzvision.gd.TZZnxTemplateBundle.service.impl.ZnxTemplateInfoServiceImpl")
public class ZnxTemplateInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzZnxTmplTblMapper psTzZnxTmpTblMapper;
	
	/* 新增站内信模板设置信息 */
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
					// 站内信模板编号;
					String str_znxTmplId = (String) dataMap.get("znxtempid");
					// 站内信模板名称;
					String str_znxTmplName = (String) dataMap.get("znxtempname");
					//机构;
					String str_restemporg = (String) dataMap.get("znxtemporg");
					//使用邮箱服务;
					String strEmailServId = (String) dataMap.get("tempemailserv");
					//是否启用
					String str_is_use = (String) dataMap.get("isuse");
					//是否判重
					String ifRpt = (String) dataMap.get("ifRpt");  
					//描述
					String str_emlTmplDesc = (String) dataMap.get("znxtmpdesc");
					//元模版
					String str_resTmplId = (String) dataMap.get("restempid");
					//是否包含动态个性化内容
					String str_ispersonalize = (String) dataMap.get("ispersonalize");
					//是否html
					String str_ishtml = (String) dataMap.get("ishtml");
					// ueditor是否实例化
					//String str_isUeditor = (String) dataMap.get("emlContentMode");
					//站内信主题
					String str_znxTmplSubject = (String) dataMap.get("znxtmpsubject");

					//站内信内容
					String str_znxTmplContent="";
					if("Y".equals(str_ishtml)){
						str_znxTmplContent = jacksonUtil.getString("znxtmpcontentHtml");
					}else{
						str_znxTmplContent = jacksonUtil.getString("znxtmpcontent");
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
							String hasEmlTmplSQL = "SELECT count(1) FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_YMB_ID = ? AND TZ_JG_ID = ? AND TZ_USE_FLAG = 'Y' AND TZ_TMPL_ID <> ?";
							hasEmlTmplNum = jdbcTemplate.queryForObject(hasEmlTmplSQL, new Object[]{str_resTmplId, str_restemporg, str_znxTmplId},"Integer");
							if(hasEmlTmplNum > 0){
								b_save = false;
								errMsg[0] = "1";
								errMsg[1] = "模版类型为" + str_ymb_name + "的模版只能存在一个启用的站内信模版，请重新选择模版类型。";
							}
						}
					}
					
					if(b_save){
						int existNum = 0;
						//编号是否存在;
						String existSQL = "SELECT count(1) FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_ID = ?";
						existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_znxTmplId},"Integer");
						if(existNum > 0){
							errMsg[0] = "1";
							errMsg[1] = "站内信模版编号已经被占用，请修改站内信模版编号。";
						}else{
							//名称是否已经被使用;
							existSQL = "SELECT count(1) FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_NAME = ? AND  TZ_TMPL_ID <> ?";
							existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_znxTmplName, str_znxTmplId},"Integer");
							if(existNum > 0){
								errMsg[0] = "1";
								errMsg[1] = "站内信模版名称已经被占用，请修改站内信模版名称。";
							}else{
								String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
								
								PsTzZnxTmplTbl psTzZnxTmpTbl = new PsTzZnxTmplTbl();
								psTzZnxTmpTbl.setTzJgId(str_restemporg);
								psTzZnxTmpTbl.setTzTmplId(str_znxTmplId);
								psTzZnxTmpTbl.setTzTmplName(str_znxTmplName);
								psTzZnxTmpTbl.setTzUseFlag(str_is_use);
								psTzZnxTmpTbl.setTzEmlIfPrt(ifRpt);
								psTzZnxTmpTbl.setTzTmplDesc(str_emlTmplDesc);
								psTzZnxTmpTbl.setTzYmbId(str_resTmplId);
								psTzZnxTmpTbl.setTzEmlservId(strEmailServId);
								psTzZnxTmpTbl.setTzDynamicFlag(str_ispersonalize);
								psTzZnxTmpTbl.setTzWebmalFlag(str_ishtml);
								psTzZnxTmpTbl.setTzZnxSubjuect(str_znxTmplSubject);
								psTzZnxTmpTbl.setTzZnxContent(str_znxTmplContent);
								psTzZnxTmpTbl.setRowAddedDttm(new Date());
								psTzZnxTmpTbl.setRowAddedOprid(oprid);
								psTzZnxTmpTbl.setRowLastmantDttm(new Date());
								psTzZnxTmpTbl.setRowLastmantOprid(oprid);
								psTzZnxTmpTblMapper.insert(psTzZnxTmpTbl);
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
	
	/* 新增站内信模板设置信息 */
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
					// 站内信模板编号;
					String str_znxTmplId = (String) dataMap.get("znxtempid");
					// 站内信模板名称;
					String str_znxTmplName = (String) dataMap.get("znxtempname");
					//机构;
					String str_restemporg = (String) dataMap.get("znxtemporg");
					//使用邮箱服务;
					String strEmailServId = (String) dataMap.get("tempemailserv");
					//是否启用
					String str_is_use = (String) dataMap.get("isuse");
					//是否判重
					String ifRpt = (String) dataMap.get("ifRpt");  
					//描述
					String str_emlTmplDesc = (String) dataMap.get("znxtmpdesc");
					//元模版
					String str_resTmplId = (String) dataMap.get("restempid");
					//是否包含动态个性化内容
					String str_ispersonalize = (String) dataMap.get("ispersonalize");
					//是否html
					String str_ishtml = (String) dataMap.get("ishtml");
					// ueditor是否实例化
					//String str_isUeditor = (String) dataMap.get("emlContentMode");
					//站内信主题
					String str_znxTmplSubject = (String) dataMap.get("znxtmpsubject");

					//站内信内容
					String str_znxTmplContent="";
					if("Y".equals(str_ishtml)){
						str_znxTmplContent = (String) dataMap.get("znxtmpcontentHtml");
					}else{
						str_znxTmplContent = (String) dataMap.get("znxtmpcontent");
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
							String hasEmlTmplSQL = "SELECT count(1) FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_YMB_ID = ? AND TZ_JG_ID = ? AND TZ_USE_FLAG = 'Y' AND TZ_TMPL_ID <> ?";
							hasEmlTmplNum = jdbcTemplate.queryForObject(hasEmlTmplSQL, new Object[]{str_resTmplId, str_restemporg, str_znxTmplId},"Integer");
							if(hasEmlTmplNum > 0){
								b_save = false;
								errMsg[0] = "1";
								errMsg[1] = "模版类型为" + str_ymb_name + "的模版只能存在一个启用的站内信模版，请重新选择模版类型。";
							}
						}
					}
					
					if(b_save){
						int existNum = 0;
						//编号是否存在;
						String existSQL = "SELECT count(1) FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_ID = ?";
						existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_znxTmplId},"Integer");
						if(existNum == 0){
							errMsg[0] = "1";
							errMsg[1] =  "模版编号为" + str_znxTmplId + "的站内信模版不存在。";
						}else{
							//名称是否已经被使用;
							existSQL = "SELECT count(1) FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_NAME = ? AND  TZ_TMPL_ID <> ?";
							existNum = jdbcTemplate.queryForObject(existSQL, new Object[]{str_restemporg, str_znxTmplName, str_znxTmplId},"Integer");
							if(existNum > 0){
								errMsg[0] = "1";
								errMsg[1] = "机构" + str_restemporg + "已经定义过模版名称为" + str_znxTmplName + "的站内信模版，请务重复定义。";
							}else{
								String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
								
								PsTzZnxTmplTbl psTzZnxTmpTbl = new PsTzZnxTmplTbl();
								psTzZnxTmpTbl.setTzJgId(str_restemporg);
								psTzZnxTmpTbl.setTzTmplId(str_znxTmplId);
								psTzZnxTmpTbl.setTzTmplName(str_znxTmplName);
								psTzZnxTmpTbl.setTzUseFlag(str_is_use);
								psTzZnxTmpTbl.setTzEmlIfPrt(ifRpt);
								psTzZnxTmpTbl.setTzTmplDesc(str_emlTmplDesc);
								psTzZnxTmpTbl.setTzYmbId(str_resTmplId);
								psTzZnxTmpTbl.setTzEmlservId(strEmailServId);
								psTzZnxTmpTbl.setTzDynamicFlag(str_ispersonalize);
								psTzZnxTmpTbl.setTzWebmalFlag(str_ishtml);
								psTzZnxTmpTbl.setTzZnxSubjuect(str_znxTmplSubject);
								psTzZnxTmpTbl.setTzZnxContent(str_znxTmplContent);
								psTzZnxTmpTbl.setRowLastmantDttm(new Date());
								psTzZnxTmpTbl.setRowLastmantOprid(oprid);
								psTzZnxTmpTblMapper.updateByPrimaryKeySelective(psTzZnxTmpTbl);
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
			if (jacksonUtil.containsKey("znxtempid") && jacksonUtil.containsKey("znxtemporg")) {
				// 站内信服务器参数id;
				String strTmpId = jacksonUtil.getString("znxtempid");
				String strOrgName = jacksonUtil.getString("znxtemporg");
				
				PsTzZnxTmplTblKey psTzZnxTmpTblKey = new PsTzZnxTmplTblKey();
				psTzZnxTmpTblKey.setTzJgId(strOrgName);
				psTzZnxTmpTblKey.setTzTmplId(strTmpId);
				
				PsTzZnxTmplTbl psTzZnxTmpTbl = psTzZnxTmpTblMapper.selectByPrimaryKey(psTzZnxTmpTblKey);
				if(psTzZnxTmpTbl != null){
					returnJsonMap.put("znxtempid", strTmpId);
					returnJsonMap.put("znxtempname", psTzZnxTmpTbl.getTzTmplName());
					returnJsonMap.put("znxtemporg", strOrgName);
					returnJsonMap.put("restempid", psTzZnxTmpTbl.getTzYmbId());
					returnJsonMap.put("tempemailserv",psTzZnxTmpTbl.getTzEmlservId() );
					returnJsonMap.put("isuse",psTzZnxTmpTbl.getTzUseFlag() );
					returnJsonMap.put("znxtmpdesc", psTzZnxTmpTbl.getTzTmplDesc());
					returnJsonMap.put("ispersonalize", psTzZnxTmpTbl.getTzDynamicFlag());
					returnJsonMap.put("ishtml", psTzZnxTmpTbl.getTzWebmalFlag());
					returnJsonMap.put("znxtmpsubject", psTzZnxTmpTbl.getTzZnxSubjuect());
					returnJsonMap.put("znxtmpcontent", psTzZnxTmpTbl.getTzZnxContent());
					returnJsonMap.put("znxtmpcontentHtml", psTzZnxTmpTbl.getTzZnxContent());
					returnJsonMap.put("ifRpt", psTzZnxTmpTbl.getTzEmlIfPrt());
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
