package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormAtt2TMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormAtt2T;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormAtt2TKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

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
	@Autowired
	private SqlQuery sqlQuery;
	
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
				// 文件类型 0：普通附件 1：复试资料;
				int fileType = jacksonUtil.getInt("fileType");
				
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
				psTzFormAttT.setFiletype(fileType);
				psTzFormAttTMapper.insert(psTzFormAttT);

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/*报名人删除上传的附件*/
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("success", "false");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return jacksonUtil.Map2json(returnMap);
		}
		
		try {
					
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				//信息;
				String deleteFileInfo = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(deleteFileInfo);
				// 报名表id;
				String strAppId = jacksonUtil.getString("appins");
				// 上传附件系统文件名;
				String sysfileName = jacksonUtil.getString("sysfileName");
					
				if(strAppId != null && !"".equals(strAppId) && sysfileName != null && !"".equals(sysfileName)){
					//查看当前登录人;
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					//查看传入的报名表id和当前登录人是否对应;
					long appins = Long.parseLong(strAppId);
					String existSQL = "select count(1) from PS_TZ_FORM_WRK_T where OPRID=? and TZ_APP_INS_ID=?";
					int existCount = sqlQuery.queryForObject(existSQL, new Object[]{oprid,appins},"Integer");
					if(existCount > 0){
						//PsTzFormAtt2TKey psTzFormAtt2T = new PsTzFormAtt2T();
						//psTzFormAtt2T.setTzAppInsId(appins);
						//psTzFormAtt2T.setTzXxxBh(sysfileName);
						//psTzFormAtt2TMapper.deleteByPrimaryKey(psTzFormAtt2T);
						
						List<Map<String, Object>> list = new ArrayList<>();
						list = sqlQuery.queryForList("select TZ_INDEX from PS_TZ_FORM_ATT_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?",new Object[]{appins,sysfileName});
						if(list != null && list.size() > 0){
							for(int j = 0; j < list.size(); j++){
								int index = Integer.valueOf(String.valueOf(list.get(j).get("TZ_INDEX")));
								PsTzFormAttTKey psTzFormAttT = new PsTzFormAttTKey();
								psTzFormAttT.setTzAppInsId(appins);
								psTzFormAttT.setTzXxxBh(sysfileName);
								psTzFormAttT.setTzIndex(index);
								psTzFormAttTMapper.deleteByPrimaryKey(psTzFormAttT);
							}
						}
						
						returnMap.replace("success", "true");
					}
					
									
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnMap);
	}
}
