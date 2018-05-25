package com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueAutoAssign;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsLogTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;

/**
 * EMBA，848系统创建招生线索
 * @author yuds
 *
 */
@Service("com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl.TZCreateLeadServiceImpl")
public class TZCreateLeadServiceImpl  extends FrameworkImpl {
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private GetHardCodePoint GetHardCodePoint;
	@Autowired
	private TzClueAutoAssign tzClueAutoAssign;
	
	public String tzGetHtmlContent(String strParams) {
		// 当前机构
		String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		//获取当前OPRID
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		//是否为手机访问
		Boolean isMobile = CommonUtils.isMobile(request);	
		
		//开发过程中使用EMBA招生站点的样式文件
		String strSiteId = GetHardCodePoint.getHardCodePointVal("TZ_EMBA_DEFAULT_SITE");
		
		//手机版返回方法
		String strMobileBack = GetHardCodePoint.getHardCodePointVal("TZ_EMBA_ZHBACK_URL");
			
		String strRtn = "";
		try{
			//一般职员管理所有地区，销售人员管理负责的地区
			String optionHtml = "";
			/*
			String salesRole = GetHardCodePoint.getHardCodePointVal("TZ_EMBA_SALES_ROLE");
			String salesFlag = sqlQuery.queryForObject("SELECT 'Y' FROM PSROLEUSER WHERE ROLEUSER=? AND ROLENAME=?", new Object[]{oprid,salesRole}, "String");
									
			if("Y".equals(salesFlag)){
				String strCuPsnPosSQL = "SELECT TZ_AQDQ_LABEL FROM PS_TZ_AQ_DQ_T WHERE OPRID=?";
				List<Map<String,Object>> dqList = sqlQuery.queryForList(strCuPsnPosSQL, new Object[]{oprid});
				if(dqList!=null){
					for(Object sObj:dqList){
						Map<String,Object> sMap = (Map<String, Object>) sObj;
						String locationId = (String) sMap.get("TZ_AQDQ_LABEL");
						String locationSQL = "SELECT TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_JG_ID=? AND TZ_LABEL_NAME=?";
						String locationDesc = sqlQuery.queryForObject(locationSQL, new Object[]{currentOrgId,locationId}, "String");
						optionHtml = optionHtml + "<option value='"+locationId+"'>"+locationDesc+"</option>";
					}
				}
			}else{
				String listSQL = "SELECT TZ_LABEL_NAME,TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_LABEL_STATUS='Y' AND TZ_JG_ID=?";
				List<Map<String,Object>> dqList = sqlQuery.queryForList(listSQL, new Object[]{currentOrgId});
				if(dqList!=null){
					for(Object sObj:dqList){
						Map<String,Object> sMap = (Map<String, Object>) sObj;
						String locationId = (String) sMap.get("TZ_LABEL_NAME");						
						String locationDesc = (String) sMap.get("TZ_LABEL_DESC");		
						optionHtml = optionHtml + "<option value='"+locationId+"'>"+locationDesc+"</option>";
					}
				}
			}*/
			
			String listSQL = "SELECT TZ_LABEL_NAME,TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_LABEL_STATUS='Y' AND TZ_JG_ID=?";
			List<Map<String,Object>> dqList = sqlQuery.queryForList(listSQL, new Object[]{currentOrgId});
			if(dqList!=null){
				for(Object sObj:dqList){
					Map<String,Object> sMap = (Map<String, Object>) sObj;
					String locationId = (String) sMap.get("TZ_LABEL_NAME");						
					String locationDesc = (String) sMap.get("TZ_LABEL_DESC");		
					optionHtml = optionHtml + "<option value='"+locationId+"'>"+locationDesc+"</option>";
				}
			}
			
			String ctxPath = request.getContextPath();
			
			strRtn = tzGDObject.getHTMLText("HTML.TZMyEnrollmentClueBundle.TZ_M_CREATE_LEAD_HTML",ctxPath,currentOrgId,oprid,strSiteId,optionHtml,"Y",strMobileBack);
						
			strRtn = objRep.repCss(strRtn, strSiteId);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return strRtn;
	}
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];				
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				String lxrName = jacksonUtil.getString("lxrName");
				String mobile = jacksonUtil.getString("mobile");
				String company = jacksonUtil.getString("company");
				String positon = jacksonUtil.getString("positon");
				String lendProid = jacksonUtil.getString("lendProid");				
				String descr = jacksonUtil.getString("descr");
				//String siteId = jacksonUtil.getString("siteId");
				
				String result  = createLead(orgid,oprid,lxrName,mobile,company,positon,lendProid,descr);
				
				strRet = "{\"result\":\"" + result + "\"}";
			}
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			strRet = "{\"result\":\"创建线索失败，请联系管理员。错误信息为："+e.toString()+"\"}";
			e.printStackTrace();
		}
		return strRet;
	}
	
	public String createLead(String orgid,String oprid,String lxrName,String mobile,String company,String positon,String lendProid,String descr){
		String strRet = "";
		try{
					
			String checkRepeatFlg = "";
			if(!"".equals(mobile)&&mobile!=null){
				//根据手机号查找是否有未关闭的由责任人的相同的线索	
				String checkRepeatSQL = "SELECT 'Y' FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_STATUS<>'G' AND TZ_ZR_OPRID<>'' AND TZ_MOBILE=? limit 0,1";
				checkRepeatFlg = sqlQuery.queryForObject(checkRepeatSQL, new Object[]{mobile}, "String");
			}
			
			if("Y".equals(checkRepeatFlg)){
				//存在重复的线索
				strRet = "已存在该考生的线索，请勿重复创建";
			}else{
				//当前人是否为所选地区的区域负责人				
				String existSQL = "SELECT 'Y' FROM PS_TZ_AQ_DQ_T WHERE OPRID=? AND TZ_AQDQ_LABEL=? limit 0,1";
				String existFlg = sqlQuery.queryForObject(existSQL,new Object[]{oprid,lendProid},"String");
				
				String tzLeadId=String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
				PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
				PsTzXsxsInfoT.setTzLeadId(tzLeadId);
				PsTzXsxsInfoT.setTzJgId(orgid);			
				//线索创建方式-手工创建
				String strDefaultCreateWay = GetHardCodePoint.getHardCodePointVal("TZ_LEAD_ZY_CREWAY");
				PsTzXsxsInfoT.setTzRsfcreateWay(strDefaultCreateWay);
				
				
				PsTzXsxsInfoT.setTzRealname(lxrName);
				PsTzXsxsInfoT.setTzCompCname(company);
				PsTzXsxsInfoT.setTzMobile(mobile);
				PsTzXsxsInfoT.setTzPosition(positon);
				PsTzXsxsInfoT.setTzXsquId(lendProid);
				PsTzXsxsInfoT.setTzBz(descr);
				
				PsTzXsxsInfoT.setRowAddedOprid(oprid);
				PsTzXsxsInfoT.setRowLastmantOprid(oprid);
				PsTzXsxsInfoT.setRowAddedDttm(new java.util.Date());
				PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
				
				if("Y".equals(existFlg)){
					//自己负责地区默认线索状态默认状态为跟进中
					String strDefaultStatus = GetHardCodePoint.getHardCodePointVal("TZ_LEAD_ZY_STATUS");
					PsTzXsxsInfoT.setTzLeadStatus(strDefaultStatus);
					//自己负责的地区，线索分配给自己
					PsTzXsxsInfoT.setTzZrOprid(oprid);
					psTzXsxsInfoTMapper.insert(PsTzXsxsInfoT);
				}else{
					//其他人负责地区默认线索状态默认状态为未分配
					String strDefaultStatus = GetHardCodePoint.getHardCodePointVal("TZ_LEAD_XS_STATUS");
					PsTzXsxsInfoT.setTzLeadStatus(strDefaultStatus);
					//其他人负责的地区，线索自动分配
					psTzXsxsInfoTMapper.insert(PsTzXsxsInfoT);
					
					String[] errorMsg = null;
					tzClueAutoAssign.autoAssign(orgid, oprid, tzLeadId, "CHN", lendProid, errorMsg);
				}
				
				strRet = "success";
				
				//不管手机号码存在与否，判断是否存在同名的线索
				String checkRepeatByNameSQL = "SELECT 'Y' FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_STATUS<>'G' AND TZ_ZR_OPRID<>'' AND TZ_REALNAME=? AND TZ_LEAD_ID<>? limit 0,1";
				String checkRepeatFlgByName = sqlQuery.queryForObject(checkRepeatByNameSQL, new Object[]{lxrName,tzLeadId}, "String");
				if("Y".equals(checkRepeatFlgByName)){
					strRet = "repeatByName";
				}				
			}
											
		}catch(Exception e){
			strRet = "创建线索失败，请联系管理员，错误信息为：" + e.toString();
		}
		
		return strRet;
	}
}
