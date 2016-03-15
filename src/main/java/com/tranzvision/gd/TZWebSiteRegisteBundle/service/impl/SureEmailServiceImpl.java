package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteRegisteBundle.dao.PsTzDzyxYzmTblMapper;
import com.tranzvision.gd.TZWebSiteRegisteBundle.model.PsTzDzyxYzmTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 * 确认邮箱
 * 原： Record.WEBLIB_GD_USER, Field.TZ_GD_USER, "FieldFormula", "Iscript_SureEmail"
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.SureEmailServiceImpl")
public class SureEmailServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzDzyxYzmTblMapper psTzDzyxYzmTblMapper;
	
	@Override
	//确认修改邮箱
	public String tzGetHtmlContent(String strParams) {
		
		//确认修改后跳转页面;跳转到com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegEmailSuccessServiceImpl.tzGetHtmlContent;
		String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
		+ request.getContextPath() + "/dispatcher";
		String RegEmailSuccess = serv+"?classid=regemailsuccess";
		//String RegEmailSuccess = GenerateScriptContentURL(%Portal, %Node, Record.WEBLIB_GD_USER, Field.TZ_GD_USER, "FieldFormula", "Iscript_RegEmailSuccess");
		
		//验证是否通过;
		Date cntlogAddtiem;
		Date curDate = new Date();
		String dlzhId = "", tzJgId = "", tzEmail = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			String tokenCode = jacksonUtil.getString("TZ_TOKEN_CODE");

			String strTokenFLg = "";
			PsTzDzyxYzmTbl psTzDzyxYzmTbl = psTzDzyxYzmTblMapper.selectByPrimaryKey(tokenCode);
			if(psTzDzyxYzmTbl != null){
				strTokenFLg = psTzDzyxYzmTbl.getTzEffFlag();
				tzEmail = psTzDzyxYzmTbl.getTzEmail();
				dlzhId = psTzDzyxYzmTbl.getTzDlzhId();
				tzJgId = psTzDzyxYzmTbl.getTzJgId();
				if("Y".equals(strTokenFLg)){
					cntlogAddtiem = psTzDzyxYzmTbl.getTzCntlogAddtime();
					if(cntlogAddtiem.before(curDate)){
						// 验证码超时;
			            RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId + "&FLAGE=N&errorFlg=overtime";
					}else{
						// 查看是否绑定了邮箱，如果绑定了邮箱，则要同时修改绑定邮箱，同时要判断新的绑定邮箱是否在该机构下重复，如果重复，则修改失败，同时要提示用户;
				        String bindSQL = "select 'Y' from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_EMAIL=?";
						String strBindEmailFlg = jdbcTemplate.queryForObject(bindSQL, new Object[]{tzJgId,tzEmail},"String");
						if("Y".equals(strBindEmailFlg)){
							RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId + "&FLAGE=N&errorFlg=repeat";
						}else{
							String updateSQL = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_EMAIL=?, TZ_YXBD_BZ='Y' WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
							jdbcTemplate.update(updateSQL,new Object[]{tzEmail,dlzhId,tzJgId});
							
							updateSQL = "UPDATE PS_TZ_DZYX_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_TOKEN_CODE=? and TZ_EFF_FLAG='Y'";
							jdbcTemplate.update(updateSQL,new Object[]{tokenCode});
							
				            RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId + "&FLAGE=Y";
						}
					}
				}else{
					RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId +  "&FLAGE=N&errorFlg=codeerror";
				}
			}else{
				RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId +  "&FLAGE=N&errorFlg=codeerror";
			}
		}catch(Exception e ){
			RegEmailSuccess = RegEmailSuccess + "&strJgid=" + tzJgId +  "&FLAGE=N&errorFlg=codeerror";
		}
		return "<script>window.location.href='" + RegEmailSuccess + "';</script>";
	}
}
