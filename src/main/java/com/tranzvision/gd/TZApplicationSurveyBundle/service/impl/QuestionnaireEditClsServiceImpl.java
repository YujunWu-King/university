package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcBgtZwtTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtZwtT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxJygzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxPzTWithBLOBs;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * @author RS
 * @version 调查问卷--列表显示处理类
 */
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireEditClsServiceImpl")
public class QuestionnaireEditClsServiceImpl extends FrameworkImpl {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private QuestionnaireEditorEngineImpl questionnaireEditorEngineImpl;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 在线调查问卷编辑
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		System.out.println("====question===strParams:"+strParams);
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		//问卷ID
		String wjId;
		try {
			wjId = String.valueOf(jacksonUtil.getInt("ZXDC_WJ_ID"));
			System.out.println(wjId);
		} catch (Exception e) {
			wjId=jacksonUtil.getString("ZXDC_WJ_ID");
		}
		String editHtml = questionnaireEditorEngineImpl.init(wjId);
		return editHtml;
	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		System.out.println("=======tzUpdate执行=======");
		String strRet = "{}";
		try {
			int dataLength = actData.length;
			String userID = tzLoginServiceImpl.getLoginedManagerOprid(request);
			JacksonUtil jacksonUtil = new JacksonUtil();
			String survyID = null;
			Map<String, Object> mapData = null;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				System.out.println("====表单内容=====strForm："+strForm);
				jacksonUtil.json2Map(strForm);
				survyID = jacksonUtil.getString("tid");
				mapData = jacksonUtil.getMap("data");
				System.out.println("=====update===before==");
				strRet = questionnaireEditorEngineImpl.saveSurvy(survyID, mapData, userID, errMsg);
				System.out.println("=====update===end==");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}


}
