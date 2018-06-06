package com.tranzvision.gd.TZEnrollmentClueBundle.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsBmbTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 处理历史注册用户并生成线索，该方法仅在上线后执行一次即可
 * @author zhanglang
 * 
 */


@Controller
@RequestMapping("/hisRegClue")
public class HandlerHisRegClueController {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private PsTzXsxsBmbTMapper psTzXsxsBmbTMapper;
	
	
	
	
	@RequestMapping(value = { "/{orgId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String handlerHisRegClue(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(value = "orgId") String orgId) {
		
		String sql = "select 'Y' from PS_TZ_JG_BASE_T where TZ_JG_ID=? and TZ_JG_EFF_STA='Y'";
		String jgExists = sqlQuery.queryForObject(sql, new Object[]{ orgId }, "String");
		
		if("Y".equals(jgExists)){
			sql = "select B.OPRID,B.TZ_REALNAME,B.TZ_GENDER,B.TZ_COMPANY_NAME,B.TZ_LEN_PROID,C.TZ_ZY_SJ,C.TZ_ZY_EMAIL from PS_TZ_AQ_YHXX_TBL A join PS_TZ_REG_USER_T B on(A.OPRID=B.OPRID and A.TZ_RYLX='ZCYH') left join PS_TZ_LXFSINFO_TBL C on(TZ_LXFS_LY='ZCYH' and C.TZ_LYDX_ID=B.OPRID) where A.TZ_JG_ID=? and not exists(select 'Y' from PS_TZ_XSXS_INFO_T D where D.TZ_JG_ID=A.TZ_JG_ID and D.TZ_KH_OPRID=A.OPRID)";
			
			List<Map<String,Object>> list = sqlQuery.queryForList(sql, new Object[]{ orgId });
			if(list != null && list.size() > 0){
				for(Map<String,Object> oprMap: list){
					String oprid = oprMap.get("OPRID") == null ? "" : oprMap.get("OPRID").toString();
					String name = oprMap.get("TZ_REALNAME") == null ? "" : oprMap.get("TZ_REALNAME").toString();
					String gender = oprMap.get("TZ_GENDER") == null ? "" : oprMap.get("TZ_GENDER").toString();
					String company = oprMap.get("TZ_COMPANY_NAME") == null ? "" : oprMap.get("TZ_COMPANY_NAME").toString();
					String mobile = oprMap.get("TZ_ZY_SJ") == null ? "" : oprMap.get("TZ_ZY_SJ").toString();
					String email = oprMap.get("TZ_ZY_EMAIL") == null ? "" : oprMap.get("TZ_ZY_EMAIL").toString();
					
					int rtn = 0;
					String TZ_LEAD_ID = "";
					//根据手机和邮箱查询是否存在未关闭的线索，如果有则不用创建新线索
					String exSql = "select TZ_LEAD_ID from PS_TZ_XSXS_INFO_T where TZ_JG_ID=? and TZ_MOBILE=? and TZ_EMAIL=? and TZ_LEAD_STATUS<>'G' order by ROW_ADDED_DTTM desc limit 0,1";
					String existsClueId = sqlQuery.queryForObject(exSql, new Object[]{ orgId, mobile, email }, "String");
					if(existsClueId != null){
						PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = psTzXsxsInfoTMapper.selectByPrimaryKey(existsClueId);
						psTzXsxsInfoT.setTzKhOprid(oprid);
						TZ_LEAD_ID = existsClueId;
						
						if(name != null && !"".equals(name)
								&& !name.equals(psTzXsxsInfoT.getTzRealname())){
								if(psTzXsxsInfoT.getTzRealname() != null
										&& !"".equals(psTzXsxsInfoT.getTzRealname())){
									psTzXsxsInfoT.setTzRealname(psTzXsxsInfoT.getTzRealname() + "，" + name);
								}else{
									psTzXsxsInfoT.setTzRealname(name);
								}
						}
						
						psTzXsxsInfoT.setRowLastmantOprid(oprid);
						psTzXsxsInfoT.setRowLastmantDttm(new Date());
						rtn = psTzXsxsInfoTMapper.updateByPrimaryKeySelective(psTzXsxsInfoT);
						
					}else{
						TZ_LEAD_ID = String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
						// 线索创建方式-在线报名
						String strDefaultCreateWay = getHardCodePoint.getHardCodePointVal("TZ_LEAD_BM_CREWAY");
						// 分配状态-未分配
						String strDefaultStatus = getHardCodePoint.getHardCodePointVal("TZ_LEAD_BM_STATUS");
						
						
						PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = new PsTzXsxsInfoTWithBLOBs();
						psTzXsxsInfoT.setTzLeadId(TZ_LEAD_ID);
						psTzXsxsInfoT.setTzJgId(orgId);

						psTzXsxsInfoT.setTzRsfcreateWay(strDefaultCreateWay);
						psTzXsxsInfoT.setTzLeadStatus(strDefaultStatus);
						
						psTzXsxsInfoT.setTzRealname(name);
						psTzXsxsInfoT.setTzKhOprid(oprid);
						psTzXsxsInfoT.setTzCompCname(company);
						psTzXsxsInfoT.setTzSex(gender);
						psTzXsxsInfoT.setTzEmail(email);
						psTzXsxsInfoT.setTzMobile(mobile);
						psTzXsxsInfoT.setRowAddedDttm(new Date());
						psTzXsxsInfoT.setRowAddedOprid(oprid);
						psTzXsxsInfoT.setRowLastmantDttm(new Date());
						psTzXsxsInfoT.setRowLastmantOprid(oprid);
						
						rtn = psTzXsxsInfoTMapper.insert(psTzXsxsInfoT);
					}
					
					if(rtn > 0){
						//关联报名表
						String glSql = "select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T A where OPRID=? and not exists(select 'Y' from PS_TZ_XSXS_BMB_T where TZ_APP_INS_ID=A.TZ_APP_INS_ID) order by TZ_APP_INS_ID desc limit 0,1";
						Long appInsId = sqlQuery.queryForObject(glSql, new Object[]{ oprid }, "Long");
						
						if(appInsId != null && appInsId > 0){
							PsTzXsxsBmbT psTzXsxsBmbT = new PsTzXsxsBmbT();
							psTzXsxsBmbT.setTzLeadId(TZ_LEAD_ID);
							psTzXsxsBmbT.setTzAppInsId(appInsId);
							psTzXsxsBmbT.setRowAddedDttm(new Date());
							psTzXsxsBmbT.setRowAddedOprid(oprid);
							psTzXsxsBmbT.setRowLastmantDttm(new Date());
							psTzXsxsBmbT.setRowLastmantOprid(oprid);
							
							psTzXsxsBmbTMapper.insert(psTzXsxsBmbT);
						}
					}
				}
			}
		}else{
			return "机构编号不存在";
		}
		
		return "";
	}
}
