package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * @author WRL TZ_ONLINE_REG_COM:TZ_ONLINE_FORM_STD 
 * 报名表预览、管理员查看
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormViewClsServiceImpl")
public class AppFormViewClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzGdObject;
	/**
	 * 报名表模板预览、管理员查看
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			String tplId = jacksonUtil.getString("TZ_APP_TPL_ID");
			String mode = jacksonUtil.getString("mode");
			String regInsId = jacksonUtil.getString("TZ_APP_INS_ID");
			String clsId = jacksonUtil.getString("TZ_CLASS_ID");   
			String oprId = jacksonUtil.getString("OPRID");
			
			
			/*--- TAB页签  BEGIN ---*/
			String sql = "SELECT TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_TAPSTYLE FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? ORDER BY TZ_ORDER ASC";
			List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { tplId });
			String tabHtml = "";
			int i = 1;
			for (Object obj : resultlist) {
				Map<String, Object> result = (Map<String, Object>) obj;

				String xxxBh = result.get("TZ_XXX_BH") == null ? "" : String.valueOf(result.get("TZ_XXX_BH"));
				String xxxName = result.get("TZ_XXX_MC") == null ? "" : String.valueOf(result.get("TZ_XXX_MC"));
				String xxxTitle = result.get("TZ_TITLE") == null ? "" : String.valueOf(result.get("TZ_TITLE"));
				String tapStyle = result.get("TZ_TAPSTYLE") == null ? "" : String.valueOf(result.get("TZ_TAPSTYLE"));
				String divClass = "";
				if(i == 1){
					divClass = "tabNav_c tabNav_bg_c2";
				}else{
					divClass = "tabNav tabNav_bg2";
				}
				
				tabHtml = tabHtml + TZ_TABS_DIV
						try {
							tplHtml = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_TEMPLATE_HTML", true,
									request.getContextPath(), tplName, tplId, componentData, tzGeneralURL, msgSet, contextUrl);
						} catch (TzSystemException e) {
							e.printStackTrace();
							tplHtml = "";
						}
				i++;
			}
			
			
			

			   
			   Local string &str_xxx_bh, &strXxxmc, &strDivClass, &strTabs, &strTitle;
			   Local number &numIndex = 1;
			   Local string &str_tap_style = "";
			   
			   Local SQL &sqlTabs = CreateSQL("", &strTplId);
			   While &sqlTabs.Fetch(&str_xxx_bh, &strXxxmc, &strTitle, &str_tap_style)
			      If &numIndex = 1 Then
			         &strDivClass = "tabNav_c tabNav_bg_c2";
			      Else
			         &strDivClass = "tabNav tabNav_bg2";
			      End-If;
			      rem 20160201,李丹丹，添加转义;
			      &strTabs = &strTabs | GetHTMLText(HTML.TZ_TABS_DIV, &strDivClass, %This.Transformchar(&strTitle), &str_tap_style, %This.Transformchar(&str_xxx_bh));
			      &numIndex = &numIndex + 1;
			   End-While;
			   &strTabs = GetHTMLText(HTML.TZ_STATIC_TABS, &strTabs);
			   /*--- TAB页签  END ---*/
			
			
			
			
			String editHtml = templateEngine.init(tplId,"");
			return editHtml;
	}
}
