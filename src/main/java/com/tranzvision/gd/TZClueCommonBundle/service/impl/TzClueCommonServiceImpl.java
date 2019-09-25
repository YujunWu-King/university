package com.tranzvision.gd.TZClueCommonBundle.service.impl;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZClueCommonBundle.service.impl.TzClueCommonServiceImpl")
public class TzClueCommonServiceImpl extends FrameworkImpl {

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private  GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Override
	public String tzGetHtmlContent(String comParams) {
		String contextUrl = request.getContextPath();
		String jgId = request.getParameter("jgId");
		String color = "#078445";
		String text = "咨询报名华理MBA课程";
		String str_appform_main_html = "";
		boolean isMobile = CommonUtils.isMobile(request);
		String sql = "SELECT COUNT(*) FROM PS_TZ_JG_BASE_T WHERE TZ_JG_ID=?";
		try {
			if(!"".equals(jgId)&&jgId!=null){
				int res = sqlQuery.queryForObject(sql, new Object[]{ jgId }, "Integer");
				if(res>0){
					//根据机构id判断主题颜色和标题下内容
					if("SEM".equals(jgId)){
						color = "#078445";
						text = "咨询报名华理MBA课程";
					}
					if("MEM".equals(jgId)){
						color = "#1e7fb8";
						text = "咨询报名华理MEM课程";
					}
					if("MPACC".equals(jgId)){
						color = "#e3001e";
						text = "咨询报名华理MPACC课程";
					}
					if("MF".equals(jgId)){
						color = "#be9c5a";
						text = "咨询报名华理MF课程";
					}
					if("IMBA".equals(jgId)){
						color = "#40a4dc";
						text = "咨询报名华理中澳合作MBA课程";
					}
					
					if(isMobile){
						System.out.println("手机端");
						str_appform_main_html = tzGdObject.getHTMLTextForDollar(
								"HTML.TZClueCommonBundle.TZ_CLUEM_COMMON_HTML", true,contextUrl,jgId,color,text);
					}else{
						System.out.println("PC端");
						str_appform_main_html = tzGdObject.getHTMLTextForDollar(
								"HTML.TZClueCommonBundle.TZ_CLUE_COMMON_HTML", true,contextUrl,jgId,color,text);
					}
				}else{
					System.out.println("错误");
					str_appform_main_html = tzGdObject.getHTMLTextForDollar(
							"HTML.TZClueCommonBundle.TZ_ERROR_CLUE_HTML", true);
				}
			}else{
				System.out.println("错误");
				str_appform_main_html = tzGdObject.getHTMLTextForDollar(
						"HTML.TZClueCommonBundle.TZ_ERROR_CLUE_HTML", true);
			}
			
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		return str_appform_main_html;
	}
	
	@Override
	public String tzOther(String operateType,String comParams,String[] errMsg){
		String strRet = "";
		Map<String, Object> map = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		jacksonUtil.json2Map(comParams);
		
		String xm = "";
		String xb = "";
		String nl = "";
		String sj = "";
		String yx = "";
		String zgxl = "";
		String gzdw = "";
		String xrzw = "";
		String gznx = "";
		String glnx = "";
		String zxnr = "";
		
		String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
		String jgId="";
		
		String clueId = "";
		String fromType = "";
		String createWay="";
		String clueState="";
		String chargeOprid="";
		String refereeName="";
		String colorType="";
		String tjr="";
		String fdb="";
		String backReasonId="";
		String closeReasonId="";
		String contactDate="";
		try {
			if(comParams!=null&&!"".equals(comParams)){
				xm = jacksonUtil.getString("xm") == null?"":jacksonUtil.getString("xm");
				xb = jacksonUtil.getString("xb") == null?"":jacksonUtil.getString("xb");
				nl = jacksonUtil.getString("nl") == null?"":jacksonUtil.getString("nl");
				sj = jacksonUtil.getString("sj") == null?"":jacksonUtil.getString("sj");
				yx = jacksonUtil.getString("yx") == null?"":jacksonUtil.getString("yx");
				zgxl = jacksonUtil.getString("zgxl") == null?"":jacksonUtil.getString("zgxl");
				gzdw = jacksonUtil.getString("gzdw") == null?"":jacksonUtil.getString("gzdw");
				xrzw = jacksonUtil.getString("xrzw") == null?"":jacksonUtil.getString("xrzw");
				gznx = jacksonUtil.getString("gznx") == null?"":jacksonUtil.getString("gznx");
				glnx = jacksonUtil.getString("glnx") == null?"":jacksonUtil.getString("glnx");
				zxnr = jacksonUtil.getString("zxnr") == null?"":jacksonUtil.getString("zxnr");
				
				clueId = jacksonUtil.getString("clueId") == null?"":jacksonUtil.getString("clueId");
				fromType="I";
				clueState="C";
				chargeOprid=jacksonUtil.getString("chargeOprid") == null?"":jacksonUtil.getString("chargeOprid");
				refereeName=jacksonUtil.getString("refereeName") == null?"":jacksonUtil.getString("refereeName");
				colorType=jacksonUtil.getString("colorType") == null?"":jacksonUtil.getString("colorType");
				tjr=jacksonUtil.getString("tjr") == null?"":jacksonUtil.getString("tjr");
				fdb=jacksonUtil.getString("fdb") == null?"":jacksonUtil.getString("fdb");
				backReasonId=jacksonUtil.getString("backReasonId") == null?"":jacksonUtil.getString("backReasonId");
				closeReasonId=jacksonUtil.getString("closeReasonId") == null?"":jacksonUtil.getString("closeReasonId");
				contactDate=jacksonUtil.getString("contactDate") == null?"":jacksonUtil.getString("contactDate");
				jgId=jacksonUtil.getString("jgId") == null?"":jacksonUtil.getString("jgId");
			}
			String sqlClue = "select TZ_LEAD_ID  from PS_TZ_XSXS_INFO_T where TZ_MOBILE = ? order by ROW_LASTMANT_DTTM desc limit 1";
			clueId = sqlQuery.queryForObject(sqlClue, new Object[]{ sj }, "String");
			if("".equals(clueId)||clueId==null){
				//创建方式：招生线索管理-手工创建、我的招生线索-自主开发
				if("MYXS".equals(fromType)) {
					createWay = "I";
				} else {
					createWay = "G";
				}
				
				String sql = "select count(*) from PS_TZ_XSXS_INFO_T where TZ_JG_ID=? and TZ_LEAD_STATUS<>'G' and ((TZ_MOBILE<>' ' and TZ_MOBILE=?) or (TZ_EMAIL<>' '  and TZ_EMAIL=?))";
				int existsCount = sqlQuery.queryForObject(sql, new Object[]{ jgId,sj,yx }, "int");
				if(existsCount > 0){
					errMsg[0] = "1";
					errMsg[1] = "申请失败，手机或邮箱已存在对应线索！";
					map.put("msg", errMsg[1]);
				}else{
					clueId=String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
					PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
					PsTzXsxsInfoT.setTzLeadId(clueId);
					PsTzXsxsInfoT.setTzJgId(jgId);
					PsTzXsxsInfoT.setTzLeadStatus(clueState);
					//线索创建方式
					PsTzXsxsInfoT.setTzRsfcreateWay(createWay);	
					PsTzXsxsInfoT.setTzZrOprid(chargeOprid);
					PsTzXsxsInfoT.setTzRealname(xm);
					PsTzXsxsInfoT.setTzCompCname(gzdw);
					PsTzXsxsInfoT.setTzMobile(sj);
					PsTzXsxsInfoT.setTzPhone(sj);
					PsTzXsxsInfoT.setTzEmail(yx);
					PsTzXsxsInfoT.setTzPosition(xrzw);
					PsTzXsxsInfoT.setTzRefereeName(refereeName);
					PsTzXsxsInfoT.setTzBz(zxnr);
					PsTzXsxsInfoT.setTzColourSortId(colorType);
					PsTzXsxsInfoT.setRowAddedOprid(oprid);
					PsTzXsxsInfoT.setRowLastmantOprid(oprid);
					PsTzXsxsInfoT.setRowAddedDttm(new java.util.Date());
					PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
					
					PsTzXsxsInfoT.setTzAge(nl);
					PsTzXsxsInfoT.setTzSex(xb);
					PsTzXsxsInfoT.setTzTjr(tjr);
					PsTzXsxsInfoT.setTzFdb(fdb);
					PsTzXsxsInfoT.setTzZgxl(zgxl);
					PsTzXsxsInfoT.setTzGznx(gznx);
					PsTzXsxsInfoT.setTzGlnx(glnx);
					int res = psTzXsxsInfoTMapper.insert(PsTzXsxsInfoT);
					if(res > 0){
						map.put("msg", "申请成功！");
						map.put("clueId", clueId);
						map.put("bkStatus", "A");
					}else{
						errMsg[0] = "1";
						errMsg[1] = "申请失败！";
						map.put("statu", "insert");
						map.put("msg", errMsg[1]);
					}
					
					/*查询是否存在姓名相同未关闭的线索*/
					Integer existName = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID<>? AND TZ_LEAD_STATUS<>'G' AND TZ_REALNAME=?", new Object[]{clueId,xm},"Integer");
					if(existName>0) {
						map.put("existName", "Y");
					} else {
						map.put("existName", "");
					}
				}
			}else{
				PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
				PsTzXsxsInfoT.setTzLeadId(clueId);
				PsTzXsxsInfoT.setTzJgId(jgId);
//				PsTzXsxsInfoT.setTzLeadStatus(clueState);  保存的时候不要直接保存线索状态
				
				PsTzXsxsInfoT.setTzThyyId(backReasonId);
				PsTzXsxsInfoT.setTzGbyyId(closeReasonId);
				if(contactDate!=null && !"".equals(contactDate)) {
					PsTzXsxsInfoT.setTzJyGjRq(sdf.parse(contactDate));
				}
				PsTzXsxsInfoT.setTzZrOprid(chargeOprid);
				PsTzXsxsInfoT.setTzRealname(xm);
				PsTzXsxsInfoT.setTzCompCname(gzdw);
				PsTzXsxsInfoT.setTzMobile(sj);
				PsTzXsxsInfoT.setTzPhone(sj);
				PsTzXsxsInfoT.setTzEmail(yx);
				PsTzXsxsInfoT.setTzPosition(xrzw);
				PsTzXsxsInfoT.setTzRefereeName(refereeName);
				PsTzXsxsInfoT.setTzBz(zxnr);
				PsTzXsxsInfoT.setTzColourSortId(colorType);
				PsTzXsxsInfoT.setRowLastmantOprid(oprid);
				PsTzXsxsInfoT.setRowLastmantDttm(new java.util.Date());
				
				PsTzXsxsInfoT.setTzAge(nl);
				PsTzXsxsInfoT.setTzSex(xb);
				PsTzXsxsInfoT.setTzTjr(tjr);
				PsTzXsxsInfoT.setTzFdb(fdb);
				PsTzXsxsInfoT.setTzZgxl(zgxl);
				PsTzXsxsInfoT.setTzGznx(gznx);
				PsTzXsxsInfoT.setTzGlnx(glnx);
				int res = psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
				if(res > 0){
					map.put("msg", "申请成功！");
					map.put("clueId", clueId);
					map.put("bkStatus", "A");
					map.put("statu", "update");
				}else{
					errMsg[0] = "1";
					errMsg[1] = "申请失败！";
					map.put("msg", errMsg[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			map.put("msg", errMsg[1]);
		}
	
		strRet = jacksonUtil.Map2json(map);
		return strRet;
	}
		
}
