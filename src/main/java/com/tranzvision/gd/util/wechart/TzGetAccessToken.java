package com.tranzvision.gd.util.wechart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZWeChatBundle.dao.PsTzWxGzhcsTMapper;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxGzhcsT;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxGzhcsTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.httpclient.HttpClientService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;

/**
 * 获取微信服务号基础access_token
 * @author zhang lang
 *
 */
@Service
public class TzGetAccessToken {
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private PsTzWxGzhcsTMapper psTzWxGzhcsTMapper;
	

	//企业号获取access_token URL
	private static final String qy_gettoken_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
	//公众号获取access_token URL
	private static final String gz_gettoken_url = "https://api.weixin.qq.com/cgi-bin/token";
	

	/**
	 * 
	 * 获取微信基础access_token
	 * @param appId		微信APPID	
	 * @param secret	微信APPSECRET
	 * @param wxType	微信公众号类型：QY-企业号，GZ-公众号
	 * @param httpReq	是否直接发送http请求获取access_token,一般情况下请传false。只有在特殊情况下才穿true,比如：access_token有效期内失效。
	 * @return
	 * @throws TzException
	 */
	public String getBaseAccessToken(String appId,String secret ,String wxType, Boolean httpReq) throws TzException{
		String access_token = "";
		try{
			boolean getWxTokenFlag = false;
			String strAccessToken = "";
			String strExpires_in = "";
			String getStrTokenTime = "";
			//最后更新时间
			Date lastUpdateTime = null;
			if(!httpReq){
				String sqlGetWxInfo = tzGDObject.getSQLText("SQL.TZWeChatBundle.TzGetWxGzhCsInfo");
				Map<String, Object> dataMap = sqlQuery.queryForMap(sqlGetWxInfo, new Object[] { appId,secret });
				if (dataMap != null) {
					String dtFormat = getSysHardCodeVal.getDateTimeFormat();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);
					
					strAccessToken = dataMap.get("TZ_ACCESS_TOKEN") == null ? "" : String.valueOf(dataMap.get("TZ_ACCESS_TOKEN"));
					strExpires_in = dataMap.get("TZ_TOKEN_YXQ") == null ? "" : String.valueOf(dataMap.get("TZ_TOKEN_YXQ"));
					getStrTokenTime = dataMap.get("TZ_UPDATE_DTTM") == null ? "" : String.valueOf(dataMap.get("TZ_UPDATE_DTTM"));
					if(!"".equals(getStrTokenTime) && !"".equals(strAccessToken)){
						Date getTokenTime = simpleDateFormat.parse(getStrTokenTime);
						
						lastUpdateTime = getTokenTime;
						getTokenTime = new Date(getTokenTime.getTime() +  Integer.parseInt(strExpires_in) * 1000);
						Date dateNow = new Date();
						if (getTokenTime.getTime() > dateNow.getTime()) {
							/*直接返回token*/
							access_token = strAccessToken;
						}else{
							/*通过微信接口获取token*/
							getWxTokenFlag = true;
						}
					}else{
						/*通过微信接口获取token*/
						getWxTokenFlag = true;
					}
					
				}else{
					/*通过微信接口获取token*/
					getWxTokenFlag = true;
				}
			}
			
			if(getWxTokenFlag || httpReq){
				//发送http请求获取access_token
				access_token = this.accessTokenHttpsRequest(appId, secret, wxType, lastUpdateTime);
			}
		}catch(Exception e){
			throw new TzException("获取access_token失败,"+e.getMessage());
		}
		
		return access_token;
	}
	
	
	
	/**
	 * http请求获取access_token
	 * @param appId
	 * @param secret	
	 * @param wxType		微信公众号类型
	 * @param lastUpdateTime	最后更新时间
	 * @return
	 * @throws Exception
	 */
	private String accessTokenHttpsRequest(String appId,String secret ,String wxType, Date lastUpdateTime) throws Exception{
		/************************************************************************************************/
		/**
		 * 要保证只能有一个人发送获取access_token请求，因为重复请求会导致之前获取到的access_token失效。
		 * 如果并发多个人发送请求，先进行排队，执行前查询access_token是否被更新，如果更新不发请求
		 */
		/*************************************************************************************************/
		String access_token = "";
		Semaphore tmpSemaphore = null;
		boolean hasTmpSemaphore = false; 
		boolean isLocked = false;
		
		try{
			//获取当前序列对应的信号灯
			Map.Entry<String,Semaphore> tmpSemaphoreObject = tzGDObject.getSemaphore(appId + "-" + secret);
			if(tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null || tmpSemaphoreObject.getValue() == null)
			{
				throw new TzException("获取access_token失败");
			}else{
				tmpSemaphore = tmpSemaphoreObject.getValue();
				
				//获取访问许可
				tmpSemaphore.acquire();
				
				hasTmpSemaphore = true;
			}
			
			//不同服务器之间防止多个人同时访问
			int counter = 0;
			//如果已经被其他人插入表，则插入失败，说明其他人正在请求access_token,最多尝试20次
			while(!isLocked){
				counter++;
				try
			    {
					TzRecord lockRecord = tzGDObject.createRecord("PS_TZ_WXREQ_LOCK_TBL");
					lockRecord.setColumnValue("TZ_WX_APPID", appId);
					lockRecord.setColumnValue("TZ_APPSECRET", secret);
					lockRecord.setColumnValue("TZ_TYPE", "ACCESS_TOKEN");
													
					if(lockRecord.insert() == false){
					    throw new TzException("锁表失败");
					}else{
					    isLocked = true;
					}
				}
			    catch(Exception e)
			    {
			        e.printStackTrace();
			    }
				
				if(isLocked || counter >= 20){
					break;
				}else{
					Thread.sleep(20);
				}
			}
			
			if(isLocked){
				//如果已被其他人更新，直接取更新后的access_token
				if(lastUpdateTime != null){
					String sql = "select TZ_ACCESS_TOKEN from PS_TZ_WX_GZHCS_T where TZ_WX_APPID=? and TZ_APPSECRET=? and TZ_UPDATE_DTTM > ?";
					String new_access_token = sqlQuery.queryForObject(sql, new Object[]{ appId, secret, lastUpdateTime }, "String");
					
					if(new_access_token != null && !"".equals(new_access_token)){
						return new_access_token;
					}
				}
				
				/*通过微信接口获取token*/
				String getTokenUrl = "";
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				if("QY".equals(wxType)){
					 /*企业号*/
					getTokenUrl = qy_gettoken_url;
					
					paramsMap.put("corpid", appId);
					paramsMap.put("corpsecret", secret);
				}else{
					 /*公众号*/
					getTokenUrl = gz_gettoken_url;
					
					paramsMap.put("grant_type", "client_credential");
					paramsMap.put("appid", appId);
					paramsMap.put("secret", secret);
				}
				
				/*通过微信接口获取token信息和有效时间*/
				HttpClientService HttpClientService = new HttpClientService(getTokenUrl,"GET",paramsMap,"UTF-8");
				String strHttpResult = HttpClientService.sendRequest();
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.json2Map(strHttpResult);
				
				if(jacksonUtil.containsKey("access_token")){
					access_token = jacksonUtil.getString("access_token").trim();
					String strExpires_in = jacksonUtil.getString("expires_in").trim();
					
					/*插入数据或者更新微信参数信息表*/
					PsTzWxGzhcsTKey psTzWxGzhcsTKey = new PsTzWxGzhcsTKey();
					psTzWxGzhcsTKey.setTzWxAppid(appId);
					psTzWxGzhcsTKey.setTzAppsecret(secret);
					PsTzWxGzhcsT psTzWxGzhcsT = psTzWxGzhcsTMapper.selectByPrimaryKey(psTzWxGzhcsTKey);
					if(psTzWxGzhcsT != null){
						psTzWxGzhcsT.setTzAccessToken(access_token);
						psTzWxGzhcsT.setTzUpdateDttm(new Date());
						psTzWxGzhcsT.setTzTokenYxq(Integer.parseInt(strExpires_in));
						psTzWxGzhcsTMapper.updateByPrimaryKeySelective(psTzWxGzhcsT);
					}else{
						psTzWxGzhcsT = new PsTzWxGzhcsT();
						psTzWxGzhcsT.setTzWxAppid(appId);
						psTzWxGzhcsT.setTzAppsecret(secret);
						
						psTzWxGzhcsT.setTzAccessToken(access_token);
						psTzWxGzhcsT.setTzUpdateDttm(new Date());
						psTzWxGzhcsT.setTzTokenYxq(Integer.parseInt(strExpires_in));
						psTzWxGzhcsTMapper.insertSelective(psTzWxGzhcsT);
					}
				}else if(jacksonUtil.containsKey("errcode")){
					throw new TzException("获取access_token失败："+jacksonUtil.getString("errmsg"));
				}
			}else{
				//获取access_token失败
				return "";
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally {
			if(isLocked){
		        //删除插入PS_TZ_WXREQ_LOCK_TBL中的数据
				sqlQuery.update("delete from PS_TZ_WXREQ_LOCK_TBL where TZ_WX_APPID=? and TZ_APPSECRET=? and TZ_TYPE='ACCESS_TOKEN'", new Object[]{ appId, secret });
		    }
		    //释放信号量
		    if(hasTmpSemaphore){
		    	tmpSemaphore.release();
		    }
		}

		return access_token;
	}
	

}
