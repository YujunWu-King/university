package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * @author caoy
 * @version 创建时间：2016年7月28日 下午4:13:52 类说明 问卷模板编辑
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.SurveyEditClsServiceImpl")
public class SurveyEditClsServiceImpl extends FrameworkImpl {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TempEditorEngineImpl tempEditorEngineImpl;

	@Autowired
	private HttpServletRequest request;
	

	/**
	 * 报名表模板预览、管理员查看
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String tplId = jacksonUtil.getString("ZXDC_TPL_ID");
		String editHtml = tempEditorEngineImpl.init(tplId, "");
		//System.out.println("editHtml的值========="+editHtml);
		return editHtml;
	}

	

	/**
	 * 设置 问卷模板 模板设置
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {


		System.out.println("===survy======update=====");


		String strRet = "{}";
		try {
			int dataLength = actData.length;
			String userID = tzLoginServiceImpl.getLoginedManagerOprid(request);
			JacksonUtil jacksonUtil = new JacksonUtil();
			String strTid = null;
			Map<String, Object> mapData = null;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				strTid = jacksonUtil.getString("tid");
				mapData = jacksonUtil.getMap("data");
				strRet = tempEditorEngineImpl.saveTpl(strTid, mapData, userID, errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	

}
