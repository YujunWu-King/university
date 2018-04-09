package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.SynthStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationCenterBundle.service.impl.ClassMutexServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * 手机版网站 报考方向选择
 * @author tzhjl
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.TzChooseanBatchServiceImpl")
public class TzChooseanBatchServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private ClassMutexServiceImpl classMutexServiceImpl;
	
	
	/**
	 * 方向选择 页面展示
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String strReturnHtml="";
		JacksonUtil jacksonUtil=new JacksonUtil();
		
		
		//System.out.println(strParams);
		try {
			
		String opriId=tzLoginServiceImpl.getLoginedManagerOprid(request);	
		
		String OrgID=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		
		String siteid="";
		
		jacksonUtil.json2Map(strParams);
		if(jacksonUtil.containsKey("siteId")){
			siteid = jacksonUtil.getString("siteId");
		}else{
			siteid = request.getParameter("siteId");
		}
		String menuId = "";
		if(jacksonUtil.containsKey("menuId")){
			menuId = jacksonUtil.getString("menuId");
		}else{
			menuId = request.getParameter("menuId");
		}
		if(menuId == null || "".equals(menuId)){
			menuId = "1";
		}
		
		
		String listHtml="";
		String batchHtml="";
		//班级Id
		String classId="";
		String className="";
		String classDesc="";

		
		
		String cssPath="";
		
		
		String sql = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME,TZ_CLASS_DESC from  PS_TZ_CLASS_INF_T where TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() and TZ_CLASS_ID not in (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?) AND TZ_PRJ_ID in (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T where TZ_SITEI_ID=?)";
		List<Map<String, Object>> batchmapList=sqlQuery.queryForList(sql,new Object[]{OrgID,opriId,siteid});
		System.out.println("batchmapList："+batchmapList.size());
		if (batchmapList.size()>0) {
			for (Map<String, Object> map : batchmapList) {
				classId=map.get("TZ_CLASS_ID")==null?"":map.get("TZ_CLASS_ID").toString();
				className=map.get("TZ_CLASS_NAME")==null?"":map.get("TZ_CLASS_NAME").toString();
				classDesc=map.get("TZ_CLASS_DESC")==null?"":map.get("TZ_CLASS_DESC").toString();
			/*	if("".equals(classDesc)){
					
				}else{
					className+="("+classDesc+")";
				}*/
				listHtml=listHtml+tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_GD_MCHOSE_BATCH_LIST_HTML",className,classId);
			}	
		}else{
			
			listHtml="";
			
		}
		
		batchHtml=tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_GD_MCHOSE_AN_BATCH_HTML",listHtml);
		
		 //css/js  路径
		
         cssPath+="<script type=\"text/javascript\" src=\""+request.getContextPath()+"/statics/js/website/m/rem.js\"></script>";
		 cssPath+="<script type=\"text/javascript\" src=\""+request.getContextPath()+"/statics/js/website/m/common.js\"></script>";
		 cssPath+="<script type=\"text/javascript\" src=\""+request.getContextPath()+"/statics/js/website/m/batchandmore.js\"></script>";
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from TZ_AQ_YHXX_TBL WHERE OPRID=?",new Object[]{opriId},"String");
			
			if (JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
		 strReturnHtml=tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML","报考方向选择",request.getContextPath(),cssPath,siteid,menuId,batchHtml,JGID);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "无法获取相关数据";
		}
		
		return strReturnHtml;
	}
	
	
	/**
	 * 批次选择确定
	 */
	
	@Override
	@Transactional
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("HaveHisApplyForm", "false");
		returnMap.put("HaveHCBJ", "false");
		returnMap.put("Apply", "false");
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		//System.out.println("strParams1选择"+strParams);

		try {
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("classId");
			String siteid = jacksonUtil.getString("siteid");
			
			//1:是否允许报名,"N"表示不允许报名;
			String isAllowedApp = "";
			//需要查询考生允许报名表 ；
			isAllowedApp = sqlQuery.queryForObject("select TZ_ALLOW_APPLY from PS_TZ_REG_USER_T where OPRID=?", new Object[]{m_curOPRID},"String");
			//黑名单
			String isBlack = sqlQuery.queryForObject("select TZ_BLACK_NAME from PS_TZ_REG_USER_T where OPRID=?", new Object[]{m_curOPRID},"String");
			if(!"Y".equals(isBlack) && "Y".equals(isAllowedApp)){
				returnMap.replace("Apply", "true");
			}
			// 根据班级编号查询机构;
			String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String jgId = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?",
					new Object[] { classId }, "String");
			int isBmYet = sqlQuery.queryForObject(
					"select count(1) from PS_TZ_FORM_WRK_T where TZ_CLASS_ID=? and OPRID=?",
					new Object[] { classId, oprId }, "Integer");
			if (isBmYet == 0) {
				// 有没有历史报名表;
				/*
				 * int isHaveHisApp = jdbcTemplate.queryForObject(
				 * "SELECT count(1) FROM PS_TZ_FORM_WRK_T A ,PS_TZ_CLASS_INF_T B WHERE A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.OPRID = ?  AND B.TZ_JG_ID = ?"
				 * , new Object[] { oprId ,jgId}, "Integer");
				 */
				int isHaveHisApp = sqlQuery.queryForObject(
						"select count(1) from PS_TZ_FORM_WRK_T c, PS_TZ_CLASS_INF_T d where c.TZ_CLASS_ID = d.TZ_CLASS_ID AND c.OPRID = ?  AND d.TZ_JG_ID = ? and d.TZ_PRJ_ID in (select a.TZ_PRJ_ID from PS_TZ_PRJ_INF_T a,PS_TZ_PROJECT_SITE_T b where a.TZ_PRJ_ID=b.TZ_PRJ_ID and b.TZ_SITEI_ID=?)",
						new Object[] { oprId, jgId, siteid }, "Integer");
				if (isHaveHisApp > 0) {
					// 查看是否有互斥班级;
					boolean bool = classMutexServiceImpl.isClassMutex(classId);
					returnMap.replace("HaveHisApplyForm", "true");
					if (bool) {
						returnMap.replace("HaveHCBJ", "true");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnMap);
	}
	
}
