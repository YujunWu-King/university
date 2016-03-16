package com.tranzvision.gd.TZSmsTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailTemplateBundle.dao.PsTzEmalTmplTblMapper;
import com.tranzvision.gd.TZEmailTemplateBundle.dao.PsTzSmsTmplTblMapper;
import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzEmalTmplTbl;
import com.tranzvision.gd.TZEmailTemplateBundle.model.PsTzSmsTmplTbl;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpDefnTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpParaTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpRrkfTblMapper;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpParaTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpRrkfTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZSmsTemplateBundle.service.impl.TzGdSmsTmpMgClsServiceImpl")
public class TzGdSmsTmpMgClsServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzTmpDefnTblMapper psTzTmpDefnTblMapper;
	@Autowired
	private PsTzTmpParaTblMapper psTzTmpParaTblMapper;
	@Autowired
	private PsTzTmpRrkfTblMapper psTzTmpRrkfTblMapper;
	@Autowired
	private PsTzEmalTmplTblMapper psTzEmalTmplTblMapper;
	@Autowired
	private PsTzSmsTmplTblMapper psTzSmsTmplTblMapper;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID", "TZ_TMPL_ID", "TZ_TMPL_NAME", "TZ_YMB_ID", "TZ_YMB_NAME", "TZ_USE_FLAG"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr,strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("smstemporg", rowList[0]);
					mapList.put("smstempid", rowList[1]);
					mapList.put("smstempname", rowList[2]);
					mapList.put("restempid", rowList[3]);
					mapList.put("restempname", rowList[4]);
					if("true".equals(rowList[5])){
						mapList.put("isuse", true);
					}else{
						mapList.put("isuse", false);
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
	
	/*动态查询短信服务器*/
	@Override
	public String tzQuery(String strParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("tempsmsserv", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("searchType")){
				String str_type = jacksonUtil.getString("searchType");
				if("searchResTmpl".equals(str_type)){
					 String str_resTmplId = jacksonUtil.getString("restempid");
					 String strTmpSmsServ = jdbcTemplate.queryForObject("SELECT B.TZ_SMS_SERV_ID FROM PS_TZ_TMP_DEFN_TBL A,PS_TZ_SMSSERV_TBL B WHERE A.TZ_YMB_ID = ? AND A.TZ_SMS_SERV_ID = B.TZ_SMS_SERV_ID limit 0,1", new Object[]{str_resTmplId},"String");
					 mapRet.replace("tempsmsserv", strTmpSmsServ);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	/* 删除邮件服务器参数*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 短信模版编号和机构;
				String str_smsTempId = jacksonUtil.getString("smstempid");
				String str_smsTempOrg = jacksonUtil.getString("smstemporg");
				if (str_smsTempId != null && !"".equals(str_smsTempId) && str_smsTempOrg != null && !"".equals(str_smsTempOrg)) {
					//删除邮件模板;
					String deletesql = "DELETE FROM PS_TZ_SMSTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_ID = ?";
					jdbcTemplate.update(deletesql, new Object[]{str_smsTempOrg, str_smsTempId});
					
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	 /*初始化模版*/
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strRet = "";
		try{
			//当前登录的机构;
			String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			if("initializ".equals(oprType)){
				if(orgID != null && !"".equals(orgID) && !"ADMIN".equals(orgID.toUpperCase())){
					/*删除该机构所有的元模版信息*/
					String deleteSQL = "DELETE FROM PS_TZ_TMP_DEFN_TBL WHERE TZ_JG_ID = ?";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					deleteSQL = "delete FROM PS_TZ_TMP_PARA_TBL  WHERE TZ_JG_ID = ? and EXISTS (SELECT 'Y' FROM PS_TZ_TMP_DEFN_TBL B WHERE PS_TZ_TMP_PARA_TBL.TZ_JG_ID = B.TZ_JG_ID AND PS_TZ_TMP_PARA_TBL.TZ_YMB_ID = B.TZ_YMB_ID)";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					deleteSQL = "delete FROM PS_TZ_TMP_RRKF_TBL  WHERE TZ_JG_ID = ? and EXISTS (SELECT 'Y' FROM PS_TZ_TMP_DEFN_TBL B WHERE PS_TZ_TMP_RRKF_TBL.TZ_JG_ID = B.TZ_JG_ID AND PS_TZ_TMP_RRKF_TBL.TZ_YMB_ID = B.TZ_YMB_ID)";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					deleteSQL="DELETE FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ?";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					deleteSQL="DELETE FROM PS_TZ_SMSTMPL_TBL WHERE TZ_JG_ID = ?";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					/*复制平台的元模版数据*/
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					String emlsServDefSQL= "SELECT TZ_EMLSERV_ID FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_JG_ID = ? AND TZ_IS_DEFAULT = 'Y'";
					String strTmpEmlServ = jdbcTemplate.queryForObject(emlsServDefSQL,new Object[]{orgID},"String");
					String str_orgIdAdmin = "ADMIN";
					String getResTempletSQL = "SELECT TZ_YMB_ID,TZ_YMB_NAME,TZ_EXTEND_C_TMPL,TZ_DTGXHHB_FLG,TZ_YMB_DESC,TZ_SMS_SERV_ID,TZ_YMB_CSLBM,TZ_YMB_NRGM,TZ_APPCLS,TZ_DSREC_NAME,TZ_DSREC_ALIAS FROM PS_TZ_TMP_DEFN_TBL WHERE TZ_USE_FLAG = 'Y' AND TZ_DEFAULT_OPEN = 'Y' AND TZ_JG_ID = ?";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(getResTempletSQL,new Object[]{str_orgIdAdmin} );
					if(list != null && list.size()>0){
						for(int i = 0 ; i < list.size(); i++){
							
							Map<String, Object> tmpDefnMap = list.get(i);
							
							String strTmpId = String.valueOf(getSeqNum.getSeqNum("TZ_TMP_DEFN_TBL", "TZ_YMB_ID"));
							PsTzTmpDefnTbl psTzTmpDefnTbl = new PsTzTmpDefnTbl();
							psTzTmpDefnTbl.setTzJgId(orgID);
							psTzTmpDefnTbl.setTzYmbId(strTmpId);
							psTzTmpDefnTbl.setTzYmbName((String)tmpDefnMap.get("TZ_YMB_NAME"));
							psTzTmpDefnTbl.setTzUseFlag("Y");
							psTzTmpDefnTbl.setTzDefaultOpen("Y");
							psTzTmpDefnTbl.setTzExtendCTmpl((String)tmpDefnMap.get("TZ_EXTEND_C_TMPL"));
							psTzTmpDefnTbl.setTzDtgxhhbFlg((String)tmpDefnMap.get("TZ_DTGXHHB_FLG"));
							psTzTmpDefnTbl.setTzYmbDesc((String)tmpDefnMap.get("TZ_YMB_DESC"));
							psTzTmpDefnTbl.setTzSmsServId((String)tmpDefnMap.get("TZ_SMS_SERV_ID"));
							psTzTmpDefnTbl.setTzEmlservId(strTmpEmlServ);
							psTzTmpDefnTbl.setTzYmbCslbm((String)tmpDefnMap.get("TZ_YMB_CSLBM"));
							psTzTmpDefnTbl.setTzYmbNrgm((String)tmpDefnMap.get("TZ_YMB_NRGM"));
							psTzTmpDefnTbl.setTzAppcls((String)tmpDefnMap.get("TZ_APPCLS"));
							psTzTmpDefnTbl.setTzDsrecName((String)tmpDefnMap.get("TZ_DSREC_NAME"));
							psTzTmpDefnTbl.setTzDsrecAlias((String)tmpDefnMap.get("TZ_DSREC_ALIAS"));
							psTzTmpDefnTbl.setRowAddedDttm(new Date());
							psTzTmpDefnTbl.setRowAddedOprid(oprid);
							psTzTmpDefnTbl.setRowLastmantDttm(new Date());
							psTzTmpDefnTbl.setRowLastmantOprid(oprid);
							psTzTmpDefnTblMapper.insert(psTzTmpDefnTbl);
							
							String strTmpIdAdmin = (String)tmpDefnMap.get("TZ_YMB_ID");
							String strTmpSmsServ = (String)tmpDefnMap.get("TZ_SMS_SERV_ID");
							/*复制参数信息*/
							String getResTempParaInfoSQL = "SELECT TZ_PARA_ID,TZ_PARA_ALIAS,TZ_SYSVARID FROM PS_TZ_TMP_PARA_TBL WHERE TZ_JG_ID = ? AND TZ_YMB_ID = ?";
							List<Map<String, Object>> resTmpContentParaList = jdbcTemplate.queryForList(getResTempParaInfoSQL, new Object[]{str_orgIdAdmin, strTmpIdAdmin});
							if(resTmpContentParaList != null && resTmpContentParaList.size() > 0){
								for(int j = 0 ; j < resTmpContentParaList.size(); j++){
									PsTzTmpParaTbl psTzTmpParaTbl = new PsTzTmpParaTbl();
									psTzTmpParaTbl.setTzJgId(orgID);
									psTzTmpParaTbl.setTzYmbId(strTmpId);
									psTzTmpParaTbl.setTzParaId((String)resTmpContentParaList.get(j).get("TZ_PARA_ID"));
									psTzTmpParaTbl.setTzParaAlias((String)resTmpContentParaList.get(j).get("TZ_PARA_ALIAS"));
									psTzTmpParaTbl.setTzSysvarid((String)resTmpContentParaList.get(j).get("TZ_SYSVARID"));
									psTzTmpParaTblMapper.insert(psTzTmpParaTbl);
								}
							}
							
							/*复制数据采集规则信息*/
							String getResTempContentParaInfoSQL = "SELECT TZ_KEY_NAME,TZ_PARA_ID FROM PS_TZ_TMP_RRKF_TBL WHERE TZ_JG_ID = ? AND TZ_YMB_ID = ?";
							List<Map<String, Object>> resTempContentParaInfoList = jdbcTemplate.queryForList(getResTempContentParaInfoSQL, new Object[]{str_orgIdAdmin, strTmpIdAdmin});
							if(resTempContentParaInfoList != null && resTempContentParaInfoList.size()>0){
								for(int k = 0 ;k < resTempContentParaInfoList.size(); k++){
									PsTzTmpRrkfTbl psTzTmpRrkfTbl = new PsTzTmpRrkfTbl();
									psTzTmpRrkfTbl.setTzJgId(orgID);
									psTzTmpRrkfTbl.setTzYmbId(strTmpId);
									psTzTmpRrkfTbl.setTzKeyName((String)resTempContentParaInfoList.get(k).get("TZ_KEY_NAME"));
									psTzTmpRrkfTbl.setTzParaId((String)resTempContentParaInfoList.get(k).get("TZ_PARA_ID"));
									psTzTmpRrkfTblMapper.insert(psTzTmpRrkfTbl);
								}
							}
							
							/*邮件模版*/
							String getEmlTempletSQL = "SELECT TZ_TMPL_ID,TZ_TMPL_NAME,TZ_DYNAMIC_FLAG,TZ_TMPL_DESC,TZ_WEBMAL_FLAG,TZ_MAL_SUBJUECT,TZ_MAL_CONTENT FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID=? AND TZ_YMB_ID = ? AND TZ_USE_FLAG = 'Y'";
							List<Map<String, Object>> getEmlTempletList = jdbcTemplate.queryForList(getEmlTempletSQL, new Object[]{str_orgIdAdmin, strTmpIdAdmin});
							if(getEmlTempletList != null && getEmlTempletList.size()>0){
								for(int h = 0; h < getEmlTempletList.size(); h++){
									PsTzEmalTmplTbl psTzEmalTmplTbl = new PsTzEmalTmplTbl();
									Map<String, Object> emlTempletMap = getEmlTempletList.get(h);
									psTzEmalTmplTbl.setTzJgId(orgID);
									psTzEmalTmplTbl.setTzTmplId((String)emlTempletMap.get("TZ_TMPL_ID"));
									psTzEmalTmplTbl.setTzTmplName((String)emlTempletMap.get("TZ_TMPL_NAME"));
									psTzEmalTmplTbl.setTzUseFlag("Y");
									psTzEmalTmplTbl.setTzTmplDesc((String)emlTempletMap.get("TZ_TMPL_DESC"));
									psTzEmalTmplTbl.setTzYmbId(strTmpId);
									psTzEmalTmplTbl.setTzEmlservId(strTmpEmlServ);
									psTzEmalTmplTbl.setTzDynamicFlag((String)emlTempletMap.get("TZ_DYNAMIC_FLAG"));
									psTzEmalTmplTbl.setTzWebmalFlag((String)emlTempletMap.get("TZ_WEBMAL_FLAG"));
									psTzEmalTmplTbl.setTzMalSubjuect((String)emlTempletMap.get("TZ_MAL_SUBJUECT"));
									psTzEmalTmplTbl.setTzMalContent((String)emlTempletMap.get("TZ_MAL_CONTENT"));
									psTzEmalTmplTbl.setRowAddedDttm(new Date());
									psTzEmalTmplTbl.setRowAddedOprid(oprid);
									psTzEmalTmplTbl.setRowLastmantDttm(new Date());
									psTzEmalTmplTbl.setRowLastmantOprid(oprid);
									
									psTzEmalTmplTblMapper.insert(psTzEmalTmplTbl);
								}
							}
							
							/*短信内容*/
							String getSmsTempletSQL = "SELECT TZ_TMPL_ID, TZ_TMPL_NAME,TZ_DYNAMIC_FLAG,TZ_TMPL_DESC,TZ_SMS_CONTENT FROM PS_TZ_SMSTMPL_TBL WHERE TZ_JG_ID=? AND TZ_YMB_ID = ? AND TZ_USE_FLAG = 'Y'";
							List<Map<String, Object>> smsTempletList = jdbcTemplate.queryForList(getSmsTempletSQL, new Object[]{str_orgIdAdmin, strTmpIdAdmin});
							if(smsTempletList != null && smsTempletList.size()>0){
								for(int l = 0; l < smsTempletList.size(); l++){
									Map<String, Object> smsTempletMap = smsTempletList.get(l);
									PsTzSmsTmplTbl psTzSmsTmplTbl = new PsTzSmsTmplTbl();
									psTzSmsTmplTbl.setTzJgId(orgID);
									psTzSmsTmplTbl.setTzTmplId((String)smsTempletMap.get("TZ_TMPL_ID"));
									psTzSmsTmplTbl.setTzTmplName((String)smsTempletMap.get("TZ_TMPL_NAME"));
									psTzSmsTmplTbl.setTzUseFlag("Y");
									psTzSmsTmplTbl.setTzTmplDesc((String)smsTempletMap.get("TZ_TMPL_DESC"));
									psTzSmsTmplTbl.setTzYmbId(strTmpId);
									psTzSmsTmplTbl.setTzSmsServId(strTmpSmsServ);
									psTzSmsTmplTbl.setTzDynamicFlag((String)smsTempletMap.get("TZ_DYNAMIC_FLAG"));
									psTzSmsTmplTbl.setTzSmsContent((String)smsTempletMap.get("TZ_SMS_CONTENT"));
									psTzSmsTmplTbl.setRowAddedDttm(new Date());
									psTzSmsTmplTbl.setRowAddedOprid(oprid);
									psTzSmsTmplTbl.setRowLastmantDttm(new Date());
									psTzSmsTmplTbl.setRowLastmantOprid(oprid);
									psTzSmsTmplTblMapper.insert(psTzSmsTmplTbl);
								}
							}
						}
					}
					
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
			
		return strRet;
	}
}
