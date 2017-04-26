package com.tranzvision.gd.TZReferenceMaterialBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZReferenceMaterialBundle.service.impl.TzGenRefMaterialPageServiceImpl")
public class TzGenRefMaterialPageServiceImpl {

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private HttpServletRequest request;
	
	
	/**
	 * 生成参考资料-打分过程html
	 * @param classId   班级ID
	 * @param batchId	批次ID
	 * @param appinsId	报名表实例ID
	 * @param cjxId		成绩项ID
	 * @return
	 */
	public String getScoreProcessHtml(String classId, String batchId, String appInsId,String cjxId) throws Exception{
		String scoreProcessHtml = "";
		
		//成绩单ID
		String scoreInsId = "";
		String sql = "select TZ_SCORE_INS_ID from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
		scoreInsId = sqlQuery.queryForObject(sql, new Object[]{ classId, batchId, appInsId }, "String");
		
		if(scoreInsId!=null && !"".equals(scoreInsId)){
			sql = "select TZ_SCORE_DFGC from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
			String scoreDfgc = sqlQuery.queryForObject(sql, new Object[]{ scoreInsId, cjxId }, "String");
			//有打分过程
			if(scoreDfgc != null && !"".equals(scoreDfgc)){
				String [] processArr = scoreDfgc.split("\\|");
				if(processArr.length > 0){
					for(int i=0; i<processArr.length; i++){
						scoreProcessHtml = scoreProcessHtml + tzGdObject.getHTMLText("HTML.TZReferenceMaterialBundle.TZ_REF_SCORE_LABEL_HTML",processArr[i]);
					}
				}
			}
		}

		return scoreProcessHtml;
	}
	
	
	
	
	/**
	 * 生成参考资料-考生信息html
	 * @param appInsId	报名表实例ID
	 * @param appTmpId	报名表ID，用于展现参考资料的子模板ID
	 * @return
	 */
	public String getStuInfoUrl(String appInsId, String strTplId) throws Exception{
		String appUrl = "";
		
		String contextUrl = request.getContextPath();
		String strTzGeneralURL = contextUrl + "/refMaterial/attachApp";
		
		
		appUrl = strTzGeneralURL+ "?isEdit=N&TZ_APP_INS_ID="+appInsId+"&TZ_APP_TPL_ID="+strTplId;
		
		return appUrl;
	}
}
