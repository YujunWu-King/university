package com.tranzvision.gd.TZEventsMobileBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzLxfsinfoTblMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 手机版活动在线报名
 * @author zhanglang
 * 2017/03/02
 */
@Service("com.tranzvision.gd.TZEventsMobileBundle.service.impl.TzEventsApplyFormServiceImpl")
public class TzEventsApplyFormServiceImpl extends FrameworkImpl{

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	
	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	PsTzNaudlistTMapper psTzNaudlistTMapper;

	@Autowired
	PsTzLxfsinfoTblMapper psTzLxfsinfoTblMapper;

	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String strRet = "";
		Map<String,Object> itemsMap = new HashMap<String,Object>(); 
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			// 活动ID
			//String strApplyId = jacksonUtil.getString("APPLYID");
			String strApplyId = request.getParameter("actId");
			strApplyId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(strApplyId);

			// 当前登录人登录账号
			String userDLZH = tzWebsiteLoginServiceImpl.getLoginedUserDlzhid(request);

			// 当前登录人所属机构
			String orgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
			//当前登录人oprid
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);

			String contextPath = request.getContextPath();
			// 统一URL;
			String ZSGL_URL = contextPath + "/dispatcher";

			String sql = "select TZ_REALNAME,TZ_EMAIL,TZ_MOBILE from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID=? and TZ_JG_ID=?";
			Map<String, Object> mapUserInfo = sqlQuery.queryForMap(sql, new Object[] { userDLZH, orgid });

			// 姓名
			String name = "";
			// 邮箱
			String email = "";
			// 手机
			String mobile = "";

			if (null != mapUserInfo) {
				name = mapUserInfo.get("TZ_REALNAME") == null ? "" : String.valueOf(mapUserInfo.get("TZ_REALNAME"));
				//email = mapUserInfo.get("TZ_EMAIL") == null ? "" : String.valueOf(mapUserInfo.get("TZ_EMAIL"));
				//mobile = mapUserInfo.get("TZ_MOBILE") == null ? "" : String.valueOf(mapUserInfo.get("TZ_MOBILE"));
			}
			
			/*手机邮箱信息从联系方式表中获取*/
			sql = "select TZ_ZY_EMAIL,TZ_ZY_SJ from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
			Map<String, Object> mapUserEmlPhone = sqlQuery.queryForMap(sql, new Object[] { oprid });
			if(mapUserEmlPhone != null){
				email = mapUserEmlPhone.get("TZ_ZY_EMAIL") == null ? "" : String.valueOf(mapUserEmlPhone.get("TZ_ZY_EMAIL"));
				mobile = mapUserEmlPhone.get("TZ_ZY_SJ") == null ? "" : String.valueOf(mapUserEmlPhone.get("TZ_ZY_SJ"));
			}

			// 必填项的html标识
			String strBtHtml = "<span>*</span>";

			// 活动ID的隐藏域
			String str_items_html = "<input type=\"hidden\" id=\"TZ_APPLY_ID\" name=\"TZ_APPLY_ID\" value=\""
					+ strApplyId + "\"/>";

			// 双语化
			sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

			sql = "select TZ_ZXBM_XXX_ID,TZ_ZXBM_XXX_NAME,TZ_ZXBM_XXX_BT,TZ_ZXBM_XXX_ZSXS from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=? order by TZ_PX_XH";
			List<Map<String, Object>> listItems = sqlQuery.queryForList(sql, new Object[] { strApplyId });

			for (Map<String, Object> mapItem : listItems) {
				// 信息项编号
				String strItemId = mapItem.get("TZ_ZXBM_XXX_ID") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_ID"));
				// 信息项名称
				String strItemName = mapItem.get("TZ_ZXBM_XXX_NAME") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_NAME"));
				// 信息项是否必填
				String strBT = mapItem.get("TZ_ZXBM_XXX_BT") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_BT"));
				// 信息项显示模式
				String strType = mapItem.get("TZ_ZXBM_XXX_ZSXS") == null ? "" : String.valueOf(mapItem.get("TZ_ZXBM_XXX_ZSXS"));

				// 判断站点语言
				if ("ENG".equals(tzSiteLang)) {
					sql = "select TZ_ZXBM_XXX_NAME from PS_TZ_ZXBM_XXX_E_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and LANGUAGE_CD='ENG'";
					strItemName = sqlQuery.queryForObject(sql, new Object[] { strApplyId, strItemId }, "String");
				}

				String itemNameHtml = "";
				String required = "";
				if ("Y".equals(strBT)) {
					itemNameHtml = strItemName + strBtHtml;
					required = "required";
				}else{
					itemNameHtml = strItemName;
				}

				switch (strType) {
				case "1":
					if ("TZ_CYR_NAME".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, name,contextPath);
					} else if ("TZ_ZY_SJ".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, mobile,contextPath);
					} else if ("TZ_ZY_EMAIL".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, email,contextPath);
					} else {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_TEXT_HTML", itemNameHtml, strItemId, strItemName, required, "",contextPath);
					}
					break;

				case "2":
					sql = "select TZ_XXX_TRANS_ID,TZ_XXX_TRANS_NAME from PS_TZ_XXX_TRANS_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? order by TZ_PX_XH";
					List<Map<String, Object>> listOpts = sqlQuery.queryForList(sql,
							new Object[] { strApplyId, strItemId });

					String strOptHtml = "<option><option>";

					for (Map<String, Object> mapOpt : listOpts) {
						String strOptId = mapOpt.get("TZ_XXX_TRANS_ID") == null ? ""
								: String.valueOf(mapOpt.get("TZ_XXX_TRANS_ID"));
						String strOptVal = mapOpt.get("TZ_XXX_TRANS_NAME") == null ? ""
								: String.valueOf(mapOpt.get("TZ_XXX_TRANS_NAME"));

						// 下拉值双语化
						if ("ENG".equals(tzSiteLang)) {
							sql = "select TZ_OPT_VALUE from PS_TZ_XXX_TR_EN_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and TZ_XXX_TRANS_ID=? and LANGUAGE_CD='ENG'";
							strOptVal = sqlQuery.queryForObject(sql, new Object[] { strApplyId, strItemId, strOptId },
									"String");
						}

						strOptId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(strOptId);
						strOptHtml = strOptHtml + "<option value=\"" + strOptId + "\">" + strOptVal + "</option>";

					}

					str_items_html = str_items_html + tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_SELECT_HTML", itemNameHtml,strItemId, strOptHtml,required);
					break;
				}
				itemsMap.put(strItemId, strBT);
			}

			// 验证码
			str_items_html = str_items_html + tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_CODE_HTML");
			
			strRet = tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_APPLY_REG_FORM_HTML", contextPath,str_items_html, ZSGL_URL,jacksonUtil.Map2json(itemsMap));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
}
