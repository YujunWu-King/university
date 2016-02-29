package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormAtt2TMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormAtt2T;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 原ps:TZ_GD_BMGL_BMBSH_PKG:TZ_UPLOADFILES_CLS
 * @author tang
 * 上传附件
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzUploadFilesClsServiceImpl")
public class TzUploadFilesClsServiceImpl extends FrameworkImpl {
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzFormAtt2TMapper psTzFormAtt2TMapper;
	@Autowired
	private PsTzFormAttTMapper psTzFormAttTMapper;
	
	/* 新增类方法 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				
				
				
				// 附件存储路径;
				String strFileUrl = jacksonUtil.getString("fileUrl");
				// 报名表编号;
				String strAppId = jacksonUtil.getString("strAppId");
				// 学生姓名;
				//String stuName = jacksonUtil.getString("stuName");
				//  附件名称;
				String fileName = jacksonUtil.getString("FileName");
				// 附件系统唯一名称;
				String strSysFile = jacksonUtil.getString("strSysFile");
				// 附件文件名&stuName | "_" |;
				String refLetterFile = jacksonUtil.getString("refLetterFile");
				
				PsTzFormAtt2T psTzFormAtt2T = new PsTzFormAtt2T();
				psTzFormAtt2T.setTzAppInsId(Long.parseLong(strAppId));
				psTzFormAtt2T.setTzXxxBh(strSysFile);
				psTzFormAtt2T.setTzXxxMc(fileName);
				psTzFormAtt2TMapper.insert(psTzFormAtt2T);
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				
				PsTzFormAttT psTzFormAttT = new PsTzFormAttT();
				psTzFormAttT.setTzAppInsId(Long.parseLong(strAppId));
				psTzFormAttT.setTzXxxBh(strSysFile);
				psTzFormAttT.setTzIndex(1);
				psTzFormAttT.setAttachsysfilename(strSysFile);
				psTzFormAttT.setAttachuserfile(refLetterFile);
				psTzFormAttT.setTzAccessPath(strFileUrl);
				psTzFormAttT.setRowAddedDttm(new Date());
				psTzFormAttT.setRowAddedOprid(oprid);
				psTzFormAttT.setRowLastmantDttm(new Date());
				psTzFormAttT.setRowLastmantOprid(oprid);
				psTzFormAttTMapper.insert(psTzFormAttT);

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

}
