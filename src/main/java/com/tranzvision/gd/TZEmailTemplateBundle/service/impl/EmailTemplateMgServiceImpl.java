package com.tranzvision.gd.TZEmailTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 邮件模板设置列表；原：TZ_GD_EMLSMSSET_PKG:TZ_GD_EMLTMPMG_CLS
 * 
 * @author tang
 * @since 2015-11-19
 */
@Service("com.tranzvision.gd.TZEmailTemplateBundle.service.impl.EmailTemplateMgServiceImpl")
public class EmailTemplateMgServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {};
			fliterForm.orderByArr = orderByArr;

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID", "TZ_TMPL_ID", "TZ_TMPL_NAME", "TZ_YMB_ID", "TZ_YMB_NAME", "TZ_USE_FLAG" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("emltemporg", rowList[0]);
					mapList.put("emltempid", rowList[1]);
					mapList.put("emltempname", rowList[2]);
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
	
	/* 删除邮件服务器参数*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 邮件模版编号和机构;
				String emltempid = jacksonUtil.getString("emltempid");
				String emltemporg = jacksonUtil.getString("emltemporg");
				if (emltempid != null && !"".equals(emltempid) && emltemporg != null && !"".equals(emltemporg)) {
					//删除邮件模板;
					String deletesql = "DELETE FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ? AND TZ_TMPL_ID = ?";
					jdbcTemplate.update(deletesql, new Object[]{emltemporg, emltempid});
					
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
			String str_orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			if("initializ".equals(oprType)){
				if(str_orgId != null && !"".equals(str_orgId) && !"Admin".equals(str_orgId)){
					/*删除该机构所有的元模版信息*/
					String deleteSQL = "DELETE FROM PS_TZ_TMP_DEFN_TBL WHERE TZ_JG_ID = ?";
					jdbcTemplate.update(deleteSQL,new Object[]{str_orgId});
					
					deleteSQL = "delete FROM PS_TZ_TMP_PARA_TBL  WHERE TZ_JG_ID = ? and EXISTS (SELECT 'Y' FROM PS_TZ_TMP_DEFN_TBL B WHERE PS_TZ_TMP_PARA_TBL.TZ_JG_ID = B.TZ_JG_ID AND PS_TZ_TMP_PARA_TBL.TZ_YMB_ID = B.TZ_YMB_ID)";
					jdbcTemplate.update(deleteSQL,new Object[]{str_orgId});
					
					deleteSQL = "delete FROM PS_TZ_TMP_RRKF_TBL  WHERE TZ_JG_ID = ? and EXISTS (SELECT 'Y' FROM PS_TZ_TMP_DEFN_TBL B WHERE PS_TZ_TMP_RRKF_TBL.TZ_JG_ID = B.TZ_JG_ID AND PS_TZ_TMP_RRKF_TBL.TZ_YMB_ID = B.TZ_YMB_ID)";
					jdbcTemplate.update(deleteSQL,new Object[]{str_orgId});
					
					deleteSQL="DELETE FROM PS_TZ_EMALTMPL_TBL WHERE TZ_JG_ID = ?";
					jdbcTemplate.update(deleteSQL,new Object[]{str_orgId});
					
					deleteSQL="DELETE FROM PS_TZ_SMSTMPL_TBL WHERE TZ_JG_ID = ?";
					jdbcTemplate.update(deleteSQL,new Object[]{str_orgId});
				}
			}
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
			
		return strRet;
	}

}
