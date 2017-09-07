package com.tranzvision.gd.TZWeChatMaterialBundle.service.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxApiObject;
import com.tranzvision.gd.TZWeChatMaterialBundle.dao.PsTzWxMediaTblMapper;
import com.tranzvision.gd.TZWeChatMaterialBundle.model.PsTzWxMediaTbl;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
@Service("com.tranzvision.gd.TZWeChatMaterialBundle.service.impl.TzWeChatMaterPicServiceImpl")
public class TzWeChatMaterPicServiceImpl extends FrameworkImpl {
	
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzWxMediaTblMapper psTzWxMediaTblMapper;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String jgId=jacksonUtil.getString("jgId").toString();
			String wxAppId=jacksonUtil.getString("wxAppId").toString();
			String tzSeq=jacksonUtil.getString("tzSeq").toString();
			Map<String,Object> map=sqlQuery.queryForMap("select TZ_MEDIA_ID,TZ_SC_NAME,TZ_SC_REMARK,TZ_PUB_STATE,TZ_IMAGE_PATH,date_format(ROW_LASTMANT_DTTM,'%Y-%m-%d %H:%i:%s') ROW_LASTMANT_DTTM,date_format(TZ_SYNC_DTIME,'%Y-%m-%d %H:%i:%s') TZ_SYNC_DTIME,TZ_PUB_STATE from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_XH=?;", new Object[]{jgId,wxAppId,tzSeq});
			Map<String, Object> jsonMap2 = new HashMap<String, Object>();
			jsonMap2.put("jgId",jgId);
			jsonMap2.put("wxAppId", wxAppId);
			jsonMap2.put("tzSeq", tzSeq);
			jsonMap2.put("filePath", map.get("TZ_IMAGE_PATH").toString());
			jsonMap2.put("name", map.get("TZ_SC_NAME").toString());
			jsonMap2.put("status", map.get("TZ_PUB_STATE").toString());
			
			if(map.get("TZ_MEDIA_ID")!=null){
				jsonMap2.put("mediaId", map.get("TZ_MEDIA_ID").toString());
			}
			if(map.get("TZ_SC_REMARK")!=null){
				jsonMap2.put("bz", map.get("TZ_SC_REMARK").toString());
			}
			if(map.get("ROW_LASTMANT_DTTM")!=null){
				jsonMap2.put("editTime", map.get("ROW_LASTMANT_DTTM").toString());
			}
			if(map.get("TZ_SYNC_DTIME")!=null){
				jsonMap2.put("tbTime", map.get("TZ_SYNC_DTIME").toString());
			}
			returnJsonMap.replace("formData", jsonMap2);	
		}catch(Exception e){
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	/*图片素材保存*/
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		if(actData!=null)
		{
			jacksonUtil.json2Map(actData[0]);
			String jgId=jacksonUtil.getString("jgId");
			String wxAppId=jacksonUtil.getString("wxAppId");
			String tzSeq=jacksonUtil.getString("tzSeq");
			String scName=jacksonUtil.getString("name");
			String filepath=jacksonUtil.getString("filePath");
			String bz=jacksonUtil.getString("bz");
			String userId=tzLoginServiceImpl.getLoginedManagerOprid(request);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		    String nowTimeStr = simpleDateFormat.format(new Date());
		    Date nowTime=null;
			 try {
				 nowTime = simpleDateFormat.parse(nowTimeStr);
			 } catch (ParseException e) {
				e.printStackTrace();
			 }
			PsTzWxMediaTbl PsTzWxMediaTbl=new PsTzWxMediaTbl();
			if("".equals(tzSeq)){
				tzSeq=String.valueOf(getSeqNum.getSeqNum("TZ_WX_MEDIA_TBL", "TZ_XH"));
				PsTzWxMediaTbl.setTzJgId(jgId);
				PsTzWxMediaTbl.setTzWxAppid(wxAppId);
				PsTzWxMediaTbl.setTzXh(tzSeq);
				
				PsTzWxMediaTbl.setTzMediaType("A");
				PsTzWxMediaTbl.setTzScName(scName);
				PsTzWxMediaTbl.setTzScRemark(bz);
				PsTzWxMediaTbl.setTzImagePath(filepath);
				PsTzWxMediaTbl.setTzPubState("N");
				
				PsTzWxMediaTbl.setRowAddedDttm(nowTime);
				PsTzWxMediaTbl.setRowAddedOprid(userId);
				PsTzWxMediaTbl.setRowLastmantDttm(nowTime);
				PsTzWxMediaTbl.setRowLastmantOprid(userId);
				psTzWxMediaTblMapper.insert(PsTzWxMediaTbl);
				
			}else{
				PsTzWxMediaTbl.setTzJgId(jgId);
				PsTzWxMediaTbl.setTzWxAppid(wxAppId);
				PsTzWxMediaTbl.setTzXh(tzSeq);
				
				PsTzWxMediaTbl.setTzMediaType("A");
				PsTzWxMediaTbl.setTzScName(scName);
				PsTzWxMediaTbl.setTzScRemark(bz);
				PsTzWxMediaTbl.setTzImagePath(filepath);
				PsTzWxMediaTbl.setTzPubState("N");
				
				PsTzWxMediaTbl.setRowLastmantDttm(nowTime);
				PsTzWxMediaTbl.setRowLastmantOprid(userId);
				psTzWxMediaTblMapper.updateByPrimaryKeySelective(PsTzWxMediaTbl);
			}
			returnMap.put("tzSeq", tzSeq);
			returnMap.put("lastUpdateTime", nowTimeStr);
		}
		return jacksonUtil.Map2json(returnMap);
	}
	
	/*发布与撤销发布*/
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String fbStatus="N";
		//参数为空直接跳出方法
		if(actData!=null){
			jacksonUtil.json2Map(actData[0]);
			String type = jacksonUtil.getString("type");
			Map<String, Object> dataMap = jacksonUtil.getMap("data");
			String jgId=dataMap.get("jgId").toString();
			String wxAppId=dataMap.get("wxAppId").toString();
			String tzSeq=dataMap.get("wxAppId").toString();
			String filepath=dataMap.get("filePath").toString();
			String mediaId="";
			String url="";
			
			String userId=tzLoginServiceImpl.getLoginedManagerOprid(request);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   		    String nowTimeStr = simpleDateFormat.format(new Date());
   		    Date nowTime=null;
   			 try {
   				 nowTime = simpleDateFormat.parse(nowTimeStr);
   			 } catch (ParseException e) {
   				e.printStackTrace();
   			 }
	   			 
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
	        TzWxApiObject tzWxApiObject = (TzWxApiObject) getSpringBeanUtil.getSpringBeanByID("tzWxApiObject");
	        //发布
	        if("publish".equals(type)){
	        	File file = new File(filepath);
	        	Map<String,Object> map=tzWxApiObject.addOtherMaterial(jgId, wxAppId, file, "image", "", "");
    	       if(map.get("media_id")!=null){
    	    	   mediaId=map.get("media_id").toString();
    	    	   fbStatus="Y";
	        	} 
    	       if(map.get("url")!=null){
    	    	   url=map.get("url").toString();
	        	} 
    	       
  			   
  			    PsTzWxMediaTbl PsTzWxMediaTbl=new PsTzWxMediaTbl();
  			    PsTzWxMediaTbl.setTzJgId(jgId);
				PsTzWxMediaTbl.setTzWxAppid(wxAppId);
				PsTzWxMediaTbl.setTzXh(tzSeq);
	
				PsTzWxMediaTbl.setTzPubState(fbStatus);
				PsTzWxMediaTbl.setTzMediaId(mediaId);
				PsTzWxMediaTbl.setTzMediaUrl(url);
				
				PsTzWxMediaTbl.setRowLastmantDttm(nowTime);
				PsTzWxMediaTbl.setRowLastmantOprid(userId);
				psTzWxMediaTblMapper.updateByPrimaryKeySelective(PsTzWxMediaTbl);
				returnMap.put("status", fbStatus);
				returnMap.put("tbTime", nowTime);
	        }
	        
	        //撤销发布
			 if("revoke".equals(type)){
				    mediaId=sqlQuery.queryForObject("select TZ_MEDIA_ID from PS_TZ_WX_MEDIA_TBL where TZ_JG_ID=? AND TZ_WX_APPID=? AND TZ_XH=?", new Object[]{jgId,wxAppId,tzSeq}, "String");
				    
				    //从微信端删除素材
				    tzWxApiObject.deleteMaterial(jgId, wxAppId, mediaId);
				    PsTzWxMediaTbl PsTzWxMediaTbl=new PsTzWxMediaTbl();
	  			    PsTzWxMediaTbl.setTzJgId(jgId);
					PsTzWxMediaTbl.setTzWxAppid(wxAppId);
					PsTzWxMediaTbl.setTzXh(tzSeq);
		
					PsTzWxMediaTbl.setTzPubState("N");
					PsTzWxMediaTbl.setTzMediaId("");
					PsTzWxMediaTbl.setTzMediaUrl("");
					
					PsTzWxMediaTbl.setRowLastmantDttm(nowTime);
					PsTzWxMediaTbl.setRowLastmantOprid(userId);
					psTzWxMediaTblMapper.updateByPrimaryKeySelective(PsTzWxMediaTbl);
					returnMap.put("status", "N");
			 }
		}
		
		return "";
	}
}
