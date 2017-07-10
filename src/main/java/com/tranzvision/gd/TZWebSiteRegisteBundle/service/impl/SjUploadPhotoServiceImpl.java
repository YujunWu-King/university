package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.mail.util.BASE64DecoderStream;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzOprPhotoTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzOprPhtGlTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzOprPhotoT;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzOprPhtGlT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import sun.misc.BASE64Decoder;

/**
 * 手机版上传照片
 * @author tzhjl
 *
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.SjUploadPhotoServiceImpl")
public class SjUploadPhotoServiceImpl extends FrameworkImpl {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzOprPhtGlTMapper psTzOprPhtGlTMapper;
	@Autowired
	private PsTzOprPhotoTMapper psTzOprPhotoTMapper;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String language = "", tmpId = "", siteId = "", jgId = "", skinId = "";
			Map<String, Object> map;
			String languageSQL = "";
			if (jacksonUtil.containsKey("siteId")) {
				siteId = jacksonUtil.getString("siteId");
				languageSQL = "select TZ_JG_ID,TZ_SITE_LANG,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				map = jdbcTemplate.queryForMap(languageSQL, new Object[] { siteId });
				if (map != null) {
					language = (String) map.get("TZ_SITE_LANG");
					jgId = (String) map.get("TZ_JG_ID");
					skinId = (String) map.get("TZ_SKIN_ID");
				}
			} else {
				if (jacksonUtil.containsKey("TPLID")) {
					tmpId = jacksonUtil.getString("TPLID");
					languageSQL = "SELECT TZ_JG_ID,TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?";
					map = jdbcTemplate.queryForMap(languageSQL, new Object[] { tmpId });
					if (map != null) {
						language = (String) map.get("TZ_APP_TPL_LAN");
						jgId = (String) map.get("TZ_JG_ID");
						String skinSQL = "select TZ_SITEI_ID,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y' LIMIT 0,1";
						Map<String, Object> skinMap = jdbcTemplate.queryForMap(skinSQL, new Object[] { jgId });
						if (skinMap != null) {
							siteId = (String) skinMap.get("TZ_SITEI_ID");
							skinId = (String) skinMap.get("TZ_SKIN_ID");
						}
					}
				}
			}

			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 查询当前文件
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String tzAttaUrl, tzAttashSysFile;
			String sql = "SELECT TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.OPRID=?";
			Map<String, Object> mapHeadImgNow = jdbcTemplate.queryForMap(sql, new Object[] { oprid });
			if (mapHeadImgNow != null) {
				tzAttaUrl = (String) mapHeadImgNow.get("TZ_ATT_A_URL");
				tzAttashSysFile = (String) mapHeadImgNow.get("TZ_ATTACHSYSFILENA");
				tzAttaUrl = request.getContextPath() + tzAttaUrl + tzAttashSysFile;
			}else{
				tzAttaUrl = "";
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}
			if (jgId == null || "".equals(jgId)) {
				jgId = "ADMIN";
			}

			String LOAD = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET", "LOAD",
					"图像上传中，请稍等……", "图像上传中，请稍等……");
			String xuanzhuang = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_WHIRLING", "正在拼命旋转……", "正在拼命旋转……");
			String pleaseupload = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_PLEASE_UPLOAD", "请上传格式为", "请上传格式为");
			String fileSize = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_FILE_SIZE", "文件，大小为", "文件，大小为");
			String in_M = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_IN_M", "M以内", "M以内");
			String TZ_FILE_PROCESSING = validateUtil.getMessageTextWithLanguageCd(jgId, language,
					"TZGD_PERSON_PHOTO_MSGSET", "TZ_FILE_PROCESSING", "文件处理中……（处理时间取决于您上传文件的大小及网络带宽）",
					"文件处理中……（处理时间取决于您上传文件的大小及网络带宽）");
			String TZ_TAILORING = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_TAILORING", "文件处理完成，请裁剪！", "文件处理完成，请裁剪！");
			String TZ_P_UPLOAD = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_P_UPLOAD", "请上传", "请上传");
			String TZ_INSIZE_FILE = validateUtil.getMessageTextWithLanguageCd(jgId, language,
					"TZGD_PERSON_PHOTO_MSGSET", "TZ_INSIZE_FILE", "M以内的文件", "M以内的文件");
			String TZ_FORMAT_ERROR = validateUtil.getMessageTextWithLanguageCd(jgId, language,
					"TZGD_PERSON_PHOTO_MSGSET", "TZ_FORMAT_ERROR", "文件格式不正确", "文件格式不正确");
			String TZ_SAVE_ERROR = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_SAVE_ERROR", "保存失败，请重新尝试！", "保存失败，请重新尝试！");
			String TZ_UPLOAD_PHOTO = validateUtil.getMessageTextWithLanguageCd(jgId, language,
					"TZGD_PERSON_PHOTO_MSGSET", "TZ_UPLOAD_PHOTO", "请先上传照片", "请先上传照片");
			String TZ_PHOTO_PROCESSING = validateUtil.getMessageTextWithLanguageCd(jgId, language,
					"TZGD_PERSON_PHOTO_MSGSET", "TZ_PHOTO_PROCESSING", "头像处理中…………", "头像处理中…………");
			String TZ_LOAD_PHOTO = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_LOAD_PHOTO", "上传头像（请上传个人证件照片！）", "上传头像（请上传个人证件照片！）");
			String TZ_FILE_FORMAT = validateUtil.getMessageTextWithLanguageCd(jgId, language,
					"TZGD_PERSON_PHOTO_MSGSET", "TZ_FILE_FORMAT", "请上传格式为.GIF、.JPG、.PNG文件，大小为2M以内。",
					"请上传格式为.GIF、.JPG、.PNG文件，大小为2M以内。");
			// String TZ_LEFT = validateUtil.getMessageTextWithLanguageCd(jgId,
			// language,"TZGD_PERSON_PHOTO_MSGSET", "TZ_LEFT", "左旋转", "左旋转");
			// String TZ_RIGHT = validateUtil.getMessageTextWithLanguageCd(jgId,
			// language,"TZGD_PERSON_PHOTO_MSGSET", "TZ_RIGHT", "右旋转", "右旋转");
			String TZ_SIZE_TITLE = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_SIZE_TITLE", "您上传的头像会自动生成小尺寸头像，请注意小尺寸的头像 是否清晰！", "您上传的头像会自动生成小尺寸头像，请注意小尺寸的头像 是否清晰！");
			// String TZ_SAVE_BT =
			// validateUtil.getMessageTextWithLanguageCd(jgId,
			// language,"TZGD_PERSON_PHOTO_MSGSET", "TZ_SAVE_BT",
			// "save_photo_bt.png", "save_photo_bt.png");
			String UpPhoto = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_UPPHOTO", "上传图片", "Upload");
			String SavePhoto = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZGD_PERSON_PHOTO_MSGSET",
					"TZ_SAVEPHOTO", "保存头像", "Save");

			String contextPath = request.getContextPath();
			String phoToData = contextPath + "/dispatcher";
			if( "".equals(tzAttaUrl)){
				//默认图片;
				tzAttaUrl = contextPath + "/statics/images/appeditor/bjphoto.jpg";
			}
			String TZ_ENROLL_UPLOADPHO = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PHO_HTML3", phoToData,
					LOAD, xuanzhuang, pleaseupload, fileSize, in_M, TZ_FILE_PROCESSING, TZ_TAILORING, TZ_P_UPLOAD,
					TZ_INSIZE_FILE, TZ_FORMAT_ERROR, TZ_SAVE_ERROR, TZ_UPLOAD_PHOTO, TZ_PHOTO_PROCESSING, TZ_LOAD_PHOTO,
					TZ_FILE_FORMAT, TZ_SIZE_TITLE, UpPhoto, SavePhoto, contextPath, imgPath, tzAttaUrl);
			TZ_ENROLL_UPLOADPHO = objRep.repTitle(TZ_ENROLL_UPLOADPHO, siteId);
			TZ_ENROLL_UPLOADPHO = objRep.repCss(TZ_ENROLL_UPLOADPHO, siteId);
			return TZ_ENROLL_UPLOADPHO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String sysFileName = jacksonUtil.getString("sysFileName");
			String filename = jacksonUtil.getString("filename");
			String imaPath = jacksonUtil.getString("imaPath");
			if (sysFileName != null && !"".equals(sysFileName) && filename != null && !"".equals(filename)
					&& imaPath != null && !"".equals(imaPath)) {

				String path = request.getServletContext().getRealPath(imaPath);

				if ((path.lastIndexOf(File.separator) + 1) != path.length()) {
					path = path + File.separator;
				}

				File file = new File(path + sysFileName);
				if (file.exists() && file.isFile()) {
					String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					// 查询原来文件;
					String tzAttpUrl, tzAttashSysFile;
					String sql = "SELECT TZ_ATT_P_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.OPRID=?";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { oprid });
					if (map != null) {
						tzAttpUrl = (String) map.get("TZ_ATT_P_URL");
						tzAttashSysFile = (String) map.get("TZ_ATTACHSYSFILENA");

						// 删除原数据;
						psTzOprPhtGlTMapper.deleteByPrimaryKey(oprid);
						psTzOprPhotoTMapper.deleteByPrimaryKey(tzAttashSysFile);
						if ((tzAttpUrl.lastIndexOf(File.separator) + 1) != tzAttpUrl.length()) {
							tzAttpUrl = tzAttpUrl + File.separator;
						}

						File yFile = new File(tzAttpUrl + tzAttashSysFile);
						if (yFile.exists() && yFile.isFile()) {
							String ishasSql = "SELECT 'Y' FROM PS_TZ_APP_CC_T WHERE TZ_APP_S_TEXT REGEXP BINARY  '" + tzAttashSysFile + "$' limit 0,1";
							String isHas = jdbcTemplate.queryForObject(ishasSql, new Object[] {}, "String");
							if(!StringUtils.equals("Y", isHas)){
								yFile.delete();
							}
						}
					}

					// 添加现有数据;
					PsTzOprPhtGlT psTzOprPhtGlT = new PsTzOprPhtGlT();
					psTzOprPhtGlT.setOprid(oprid);
					psTzOprPhtGlT.setTzAttachsysfilena(sysFileName);
					int success = psTzOprPhtGlTMapper.insert(psTzOprPhtGlT);
					if (success <= 0) {
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PHOTO_UPLOAD_JSON", "N", "");
					}

					PsTzOprPhotoT psTzOprPhotoT = new PsTzOprPhotoT();
					psTzOprPhotoT.setTzAttachsysfilena(sysFileName);
					psTzOprPhotoT.setTzAttachfileName(filename);
					psTzOprPhotoT.setTzAttPUrl(path);
					psTzOprPhotoT.setTzAttAUrl(imaPath);
					success = psTzOprPhotoTMapper.insert(psTzOprPhotoT);
					if (success <= 0) {
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PHOTO_UPLOAD_JSON", "N", "");
					}
					
					
					
					
					
					
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PHOTO_UPLOAD_JSON", "Y",
							imaPath + "/" + sysFileName);
				} else {
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PHOTO_UPLOAD_JSON", "N", "");
				}
			}
			return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PHOTO_UPLOAD_JSON", "N", "");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PHOTO_UPLOAD_JSON", "N", "");
			} catch (Exception e1) {
				e1.printStackTrace();
				return "【TZ_PHOTO_UPLOAD_JSON】HTML对象未定义";
			}
		}
	}
	
	
	/****
	 * 
	 * 通过图片流来保存图片到服务器
	 * 
	 */
	@Override
	public String tzQuery(String strParams, String[] errMsg)   {
		// TODO Auto-generated method stub
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		
		String base64ImgData=jacksonUtil.getString("imageDate");
		
		
		String filePath=request.getServletContext().getRealPath(jacksonUtil.getString("imaPath"));
		
		
		
		try {
			this.convertBase64DataToImage(base64ImgData,filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/****
	 * 处理base64的图片数据流
	 * @param base64ImgData//64位的图片
	 * @param filePath//保存的路径
	 * @throws IOException
	 */
	
     private void convertBase64DataToImage(String base64ImgData,String filePath) throws IOException { 
    	
        BASE64Decoder d = new BASE64Decoder();  
        byte[] bs = d.decodeBuffer(base64ImgData);  
        FileOutputStream os = new FileOutputStream(filePath);  
        os.write(bs);  
        os.close();  
    }  

}
