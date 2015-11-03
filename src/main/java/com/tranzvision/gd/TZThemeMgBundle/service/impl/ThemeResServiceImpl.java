package com.tranzvision.gd.TZThemeMgBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZHardCodeMgBundle.model.PsCmbcHardcdPnt;
import com.tranzvision.gd.TZThemeMgBundle.dao.PsTzPtZtxxTblMapper;
import com.tranzvision.gd.TZThemeMgBundle.model.PsTzPtZtxxTbl;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.base.TZUtility;

import net.sf.json.JSONObject;

@Service("com.tranzvision.gd.TZThemeMgBundle.service.impl.ThemeResServiceImpl")
public class ThemeResServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzPtZtxxTblMapper psTzPtZtxxTblMapper;
	/* 获取主题定义信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);

			if (CLASSJson.containsKey("themeID")) {
				// hardcode ID;
				String str_zt_id = CLASSJson.getString("themeID");
				PsTzPtZtxxTbl psTzPtZtxxTbl=  psTzPtZtxxTblMapper.selectByPrimaryKey(str_zt_id);
				if (psTzPtZtxxTbl != null) {
					strRet = "{\"themeID\":\"" + TZUtility.transFormchar(psTzPtZtxxTbl.getTzZtId())
							+ "\",\"themeName\":\"" + TZUtility.transFormchar(psTzPtZtxxTbl.getTzZtMc())
							+ "\",\"themeDesc\":\"" + TZUtility.transFormchar(psTzPtZtxxTbl.getTzZtMs())
							+ "\",\"themeState\":\"" + TZUtility.transFormchar(psTzPtZtxxTbl.getTzYxx())
							+ "\"}";
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该主题数据不存在";
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "该hardcode数据不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
