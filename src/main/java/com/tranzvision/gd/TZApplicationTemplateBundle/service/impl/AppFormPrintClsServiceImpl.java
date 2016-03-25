package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationTemplateBundle.dao.PsTzApptplDyTMapper;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzApptplDyTWithBLOBs;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;
/**
 * @author WRL 报名表打印配置
 */
@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormPrintClsServiceImpl")
public class AppFormPrintClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private PsTzApptplDyTMapper psTzApptplDyTMapper;
	
	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;
	/**
	 * 获取模板的页头、页尾信息
	 */
	@Override
	@Transactional
	public String tzQuery(String comParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if (jacksonUtil.containsKey("tplid")) {

				String tplid = jacksonUtil.getString("tplid");
				
				PsTzApptplDyTWithBLOBs psTzApptplDy = psTzApptplDyTMapper.selectByPrimaryKey(tplid);
				
				if (psTzApptplDy != null) {
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("tplid", tplid);
					mapData.put("header", psTzApptplDy.getTzHeader());
					mapData.put("footer", psTzApptplDy.getTzFooter());

					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("formData", mapData);

					strRet = jacksonUtil.Map2json(mapRet);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "该模板数据不存在";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	/**
	 * 更新报名表模板页头，页尾数据
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";

		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您当前没有机构，不能更新报名表模板页头页尾数据！";
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int dataLength = actData.length;

			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				String tplid = jacksonUtil.getString("tplid");
				String header = jacksonUtil.getString("header");
				String footer = jacksonUtil.getString("footer");

				PsTzApptplDyTWithBLOBs psTzApptplDyT = new PsTzApptplDyTWithBLOBs();
				psTzApptplDyT.setTzAppTplId(tplid);
				psTzApptplDyT.setTzHeader(header);
				psTzApptplDyT.setTzFooter(footer);
				int count = psTzApptplDyTMapper.updateByPrimaryKeySelective(psTzApptplDyT);
				if(count > 0){
					errMsg[0] = "0";
					errMsg[1] = "更新成功!";
					this.createHF(tplid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	/**
	 * 创建Header。html、Footer。html
	 * @param tplid
	 */
	private void createHF(String tplid) {
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		PsTzApptplDyTWithBLOBs psTzApptplDy = psTzApptplDyTMapper.selectByPrimaryKey(tplid);
		String header = psTzApptplDy.getTzHeader();
		String footer = psTzApptplDy.getTzFooter();

		try {
			if(header == null){
				header = "";
			}
			if(footer == null){
				footer = "";
			}
			header = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_FORM_EXPORT_HEADER",request.getContextPath(), header);
			
			footer = tzGdObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_FORM_EXPORT_FOOTER",footer);
			header = header.replaceAll("/university/", "/");
			footer = footer.replaceAll("/university/", "/");
			header = header.replaceAll("/statics/", "../../../../statics/");
			footer = footer.replaceAll("/statics/", "../../../../statics/");
		} catch (TzSystemException e) {
			e.printStackTrace();
		}

		String parentPath = "/bmb/export/" + orgid + "/" + tplid + "/";
		try {
			fileManageServiceImpl.DeleteFile(parentPath, "header.html");
			fileManageServiceImpl.CreateFile(parentPath, "header.html", header.getBytes());
			
			fileManageServiceImpl.DeleteFile(parentPath, "footer.html");
			fileManageServiceImpl.CreateFile(parentPath, "footer.html", footer.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
