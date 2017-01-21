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
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZZnxTemplateBundle.dao.PsTzZnxTmplTblMapper;
import com.tranzvision.gd.TZZnxTemplateBundle.model.PsTzZnxTmplTbl;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpDefnTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpParaTblMapper;
import com.tranzvision.gd.TZTemplateBundle.dao.PsTzTmpRrkfTblMapper;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpDefnTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpParaTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpRrkfTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author jufeng
 * @since 2017-01-17
 */
@Service("com.tranzvision.gd.TZZnxTemplateBundle.service.impl.ZnxTemplateMgServiceImpl")
public class ZnxTemplateMgServiceImpl extends FrameworkImpl {
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
	private PsTzZnxTmplTblMapper psTzZnxTmplTblMapper;
	
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
			String[] resultFldArray = { "TZ_JG_ID", "TZ_TMPL_ID", "TZ_TMPL_NAME","TZ_YMB_ID", "TZ_YMB_NAME", "TZ_USE_FLAG" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr,strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("znxtemporg", rowList[0]);
					mapList.put("znxtempid", rowList[1]);
					mapList.put("znxtempname", rowList[2]);
					mapList.put("restempid", rowList[3]);
					mapList.put("restempname", rowList[4]);
					boolean isuser = false;
					if(rowList[5] != null){
						isuser = Boolean.valueOf(rowList[5]);
					}
					mapList.put("isuse", isuser);

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
	
	/* 删除站内信服务器参数*/
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
				// 站内信模版编号和机构;
				String znxtempid = jacksonUtil.getString("znxtempid");
				String znxtemporg = jacksonUtil.getString("znxtemporg");
				if (znxtempid != null && !"".equals(znxtempid) && znxtemporg != null && !"".equals(znxtemporg)) {
					//删除站内信模板;
					String deletesql = "DELETE FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_ID = ?";
					jdbcTemplate.update(deletesql, new Object[]{znxtemporg, znxtempid});
					
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
					String deleteSQL = "DELETE FROM PS_TZ_ZNX_DEFN_TBL WHERE TZ_JG_ID = ?";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					deleteSQL = "delete FROM PS_TZ_TMP_PARA_TBL  WHERE TZ_JG_ID = ? and EXISTS (SELECT 'Y' FROM PS_TZ_TMP_DEFN_TBL B WHERE PS_TZ_TMP_PARA_TBL.TZ_JG_ID = B.TZ_JG_ID AND PS_TZ_TMP_PARA_TBL.TZ_YMB_ID = B.TZ_YMB_ID)";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					deleteSQL = "delete FROM PS_TZ_TMP_RRKF_TBL  WHERE TZ_JG_ID = ? and EXISTS (SELECT 'Y' FROM PS_TZ_TMP_DEFN_TBL B WHERE PS_TZ_TMP_RRKF_TBL.TZ_JG_ID = B.TZ_JG_ID AND PS_TZ_TMP_RRKF_TBL.TZ_YMB_ID = B.TZ_YMB_ID)";
					jdbcTemplate.update(deleteSQL,new Object[]{orgID});
					
					deleteSQL="DELETE FROM PS_TZ_ZNX_DEFN_TBL WHERE TZ_JG_ID = ?";
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
							
							/*站内信模版*/
							String getZnxTempletSQL = "SELECT TZ_TMPL_ID,TZ_TMPL_NAME,TZ_DYNAMIC_FLAG,TZ_TMPL_DESC,TZ_WEBMAL_FLAG,TZ_ZNX_SUBJUECT,TZ_ZNX_CONTENT FROM PS_TZ_ZNXTMPL_TBL WHERE TZ_JG_ID=? AND TZ_YMB_ID = ? AND TZ_USE_FLAG = 'Y'";
							List<Map<String, Object>> getZnxTempletList = jdbcTemplate.queryForList(getZnxTempletSQL, new Object[]{str_orgIdAdmin, strTmpIdAdmin});
							if(getZnxTempletList != null && getZnxTempletList.size()>0){
								for(int h = 0; h < getZnxTempletList.size(); h++){
									PsTzZnxTmplTbl psTzZnxTmplTbl = new PsTzZnxTmplTbl();
									Map<String, Object> znxTempletMap = getZnxTempletList.get(h);
									psTzZnxTmplTbl.setTzJgId(orgID);
									psTzZnxTmplTbl.setTzTmplId((String)znxTempletMap.get("TZ_TMPL_ID"));
									psTzZnxTmplTbl.setTzTmplName((String)znxTempletMap.get("TZ_TMPL_NAME"));
									psTzZnxTmplTbl.setTzUseFlag("Y");
									psTzZnxTmplTbl.setTzTmplDesc((String)znxTempletMap.get("TZ_TMPL_DESC"));
									psTzZnxTmplTbl.setTzYmbId(strTmpId);
									psTzZnxTmplTbl.setTzEmlservId(strTmpEmlServ);
									psTzZnxTmplTbl.setTzDynamicFlag((String)znxTempletMap.get("TZ_DYNAMIC_FLAG"));
									psTzZnxTmplTbl.setTzWebmalFlag((String)znxTempletMap.get("TZ_WEBMAL_FLAG"));
									psTzZnxTmplTbl.setTzZnxSubjuect((String)znxTempletMap.get("TZ_ZNX_SUBJUECT"));
									psTzZnxTmplTbl.setTzZnxContent((String)znxTempletMap.get("TZ_ZNX_CONTENT"));
									psTzZnxTmplTbl.setRowAddedDttm(new Date());
									psTzZnxTmplTbl.setRowAddedOprid(oprid);
									psTzZnxTmplTbl.setRowLastmantDttm(new Date());
									psTzZnxTmplTbl.setRowLastmantOprid(oprid);
									
									psTzZnxTmplTblMapper.insert(psTzZnxTmplTbl);
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
